package com.imprexion.adplayer.main.activation;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.bean.EventBusMessage;
import com.imprexion.adplayer.bean.TrackingMessage;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.adplayer.tools.VoicePlay;
import com.imprexion.library.YxLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GestureActiveFootPrintFragment extends Fragment {

    @BindView(R.id.tv_guide_tip_1)
    TextView tvGuideTip1;
    @BindView(R.id.iv_bottom_floor)
    ImageView ivBottomFloor;
    @BindView(R.id.fl_fragment_gesture)
    FrameLayout flFragmentGesture;
    @BindView(R.id.iv_footprint)
    ImageView ivFootprint;
    @BindView(R.id.rl_gesture)
    RelativeLayout rlGesture;

    private Message mMessage;
    private ObjectAnimator mFootprintRotateObjAnimator;
    private ObjectAnimator mFloorEnterObjAnimator;
    private ObjectAnimator mFloorExitObjAnimator;
    private Unbinder mUnbinder;
    private boolean isResume;
    private boolean isStandHere;
    private boolean isWaveForActive;
    private boolean isRotateForStandRight;
    private VoicePlay mVoicePlay;
    private static final String TAG = "GestureActiveFootPrintFragment";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                default:
                    break;
            }
        }
    };
    private TrackingMessage mTrackingMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gesture_footprint_activation, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
//        mVoicePlay = new VoicePlay(getContext(), VoicePlay.MEDIAPLAYER);
        mVoicePlay = new VoicePlay(getActivity(), VoicePlay.SOUNDPOOL);
//        mEExitObjAnimator = ObjectAnimator.ofFloat(ivElephantExit, "translationX", 0, Tools.dpToPx(400, getContext()));
        mFootprintRotateObjAnimator = ObjectAnimator.ofFloat(ivFootprint, "rotationX", 0, Tools.dpToPx(30, getContext()), 0);
        mFloorExitObjAnimator = ObjectAnimator.ofFloat(ivBottomFloor, "translationY", 0, Tools.dpToPx(800, getContext()));
        mFloorEnterObjAnimator = ObjectAnimator.ofFloat(ivBottomFloor, "translationY", Tools.dpToPx(800, getContext()), 0);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
        startRotateFootprint();
    }

    private void startRotateFootprint() {
        if (isResume) {
            mVoicePlay.playVoiceBySoundpoolOnce(R.raw.please_stand_footprint);
        }
        if (!mFloorEnterObjAnimator.isRunning()) {
            mFloorEnterObjAnimator.setDuration(500);
            mFloorEnterObjAnimator.setInterpolator(new DecelerateInterpolator());
            mFloorEnterObjAnimator.start();
        }
        if (!mFootprintRotateObjAnimator.isRunning()) {
            mFootprintRotateObjAnimator.setRepeatCount(2000);//不站对脚印，旋转到地老天荒
            mFootprintRotateObjAnimator.setDuration(1500);
            mFootprintRotateObjAnimator.start();
            isRotateForStandRight = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isResume = false;
        EventBus.getDefault().unregister(this);

        if (mFootprintRotateObjAnimator.isRunning()) {
            mFootprintRotateObjAnimator.cancel();
        }
        if (mFloorEnterObjAnimator.isRunning()) {
            mFloorEnterObjAnimator.cancel();
        }
        if (mFloorExitObjAnimator.isRunning()) {
            mFloorExitObjAnimator.cancel();
        }
        if (mVoicePlay != null) {
            mVoicePlay.pause();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageShowEvent(EventBusMessage message) {
        YxLog.d(TAG, toString() + ": onMessageShowEvent");
        if (message.getType() == EventBusMessage.ACTIVE_TIP) {
            mTrackingMessage = (TrackingMessage) message.getObject();
            if (mTrackingMessage.isActived() && isWaveForActive) {
//                waveActiveSucess();
                isWaveForActive = false;
            }
            if (!mTrackingMessage.isStandHere() && isRotateForStandRight) {
                nextAfterStandRight();
                isRotateForStandRight = false;
            }
        }
    }

    private void nextAfterStandRight() {
        if (ivFootprint != null) {
            ivFootprint.setImageResource(R.drawable.footprint_end);
        }
        if(tvGuideTip1 != null){
            tvGuideTip1.setText(R.string.guide_tips_2);
        }
        if (mFootprintRotateObjAnimator.isRunning()) {
            mFootprintRotateObjAnimator.pause();
        }
        Tools.fadeOut(ivFootprint, 500);
        Tools.fadeOut(tvGuideTip1,500);
        Tools.fadeOut(ivBottomFloor,500);
    }

    @Override
    public void onDestroy() {
        YxLog.d(TAG, "onDestroy");
        super.onDestroy();
        mUnbinder.unbind();
//        if (mVoicePlay != null) {
//            mVoicePlay.stop();
//        }
//        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
