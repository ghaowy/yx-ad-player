package com.imprexion.adplayer.main.activation;


import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Message;
import android.view.View;
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

public class ActiveFootPrintView extends FloatView {

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
    private boolean isRotateForStandRight;
    private VoicePlay mVoicePlay;
    private static final String TAG = "ActiveFootPrintView";
    private TrackingMessage mTrackingMessage;


    public ActiveFootPrintView(Context context) {
        View view = setFloatViewLayout(R.layout.fragment_gesture_footprint_activation);

        mUnbinder = ButterKnife.bind(this, view);
        mVoicePlay = new VoicePlay(context, VoicePlay.SOUNDPOOL);
        mFootprintRotateObjAnimator = ObjectAnimator.ofFloat(ivFootprint, "rotationX", 0, Tools.dpToPx(30, context), 0);
        mFloorExitObjAnimator = ObjectAnimator.ofFloat(ivBottomFloor, "translationY", 0, Tools.dpToPx(800, context));
        mFloorEnterObjAnimator = ObjectAnimator.ofFloat(ivBottomFloor, "translationY", Tools.dpToPx(800, context), 0);
    }

    public void showActiveTip(){
        if (!isResume) {
            if (ivFootprint != null) {
                ivFootprint.setImageResource(R.drawable.footprint_rock);
            }
            if(tvGuideTip1 != null){
                tvGuideTip1.setText(R.string.guide_tips_1);
            }
            isResume = true;
            Tools.fadeIn(ivFootprint, 500);
            Tools.fadeIn(tvGuideTip1,500);
            Tools.fadeIn(ivBottomFloor,500);
            EventBus.getDefault().register(this);
            startRotateFootprint();
            showFloatView();
        }
    }

    public void dismissActiveTip(){
        if (isResume) {
            isResume = false;
            EventBus.getDefault().unregister(this);
//        mUnbinder.unbind();
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
            dismissWindow();
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageShowEvent(EventBusMessage message) {
        if (message.getType() == EventBusMessage.ACTIVE_TIP) {
            mTrackingMessage = (TrackingMessage) message.getObject();
            if (!mTrackingMessage.isStandHere() && isRotateForStandRight) {
                nextAfterStandRight();
                isRotateForStandRight = false;
            }
        }
    }
}
