package com.imprexion.adplayer.main.activation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.main.AdActivity;
import com.imprexion.adplayer.bean.EventBusMessage;
import com.imprexion.adplayer.bean.TrackingMessage;
import com.imprexion.library.logger.YxLogger;
import com.imprexion.adplayer.tools.VoicePlay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GestureActiveTwoStepFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.iv_elephant_enter)
    ImageView ivElephantEnter;
    @BindView(R.id.iv_elephant_exit)
    ImageView ivElephantExit;
    @BindView(R.id.iv_elephant_stop)
    ImageView ivElephantStop;
    @BindView(R.id.tv_guide_tip_1)
    TextView tvGuideTip1;
    @BindView(R.id.iv_leftprint_1)
    ImageView ivLeftprint1;
    @BindView(R.id.iv_leftprint_2)
    ImageView ivLeftprint2;
    @BindView(R.id.iv_leftprint_3)
    ImageView ivLeftprint3;
    @BindView(R.id.iv_leftprint_4)
    ImageView ivLeftprint4;
    @BindView(R.id.iv_leftprint_5)
    ImageView ivLeftprint5;
    @BindView(R.id.iv_leftprint_6)
    ImageView ivLeftprint6;
    @BindView(R.id.iv_rightprint_1)
    ImageView ivRightprint1;
    @BindView(R.id.iv_rightprint_2)
    ImageView ivRightprint2;
    @BindView(R.id.iv_rightprint_3)
    ImageView ivRightprint3;
    @BindView(R.id.iv_rightprint_4)
    ImageView ivRightprint4;
    @BindView(R.id.iv_rightprint_5)
    ImageView ivRightprint5;
    @BindView(R.id.iv_rightprint_6)
    ImageView ivRightprint6;
    @BindView(R.id.rl_footprint)
    RelativeLayout rlFootprint;
    @BindView(R.id.iv_bottom_floor)
    ImageView ivBottomFloor;
    @BindView(R.id.iv_wave_hands_tip)
    ImageView ivWaveHandsTip;
    @BindView(R.id.iv_leftprint)
    ImageView ivLeftprint;
    @BindView(R.id.iv_rightprint)
    ImageView ivRightprint;
    @BindView(R.id.iv_injecting_water_tip)
    ImageView ivInjectingWaterTip;
    @BindView(R.id.fl_fragment_gesture)
    FrameLayout flFragmentGesture;

    private AnimationDrawable mElephantEnterAnimation;
    private AnimationDrawable mElephantStopAnimation;
    private AnimationDrawable mElephantExitAnimation;
    private AnimationDrawable mInjectingWaterAnimation;
    private Message mMessage;
    private AnimationDrawable mWaveHandsAnimation;
    private ObjectAnimator mFootprintRotateObjAnimator;
    private ObjectAnimator mEEnterObjAnimator;
    private ObjectAnimator mEExitObjAnimator;
    private ObjectAnimator mETipEnterObjAnimator;
    private ObjectAnimator mETipExitObjAnimator;
    private ObjectAnimator mFloorEnterObjAnimator;
    private ObjectAnimator mFloorExitObjAnimator;
    private AnimatorSet mAnimatorSet;
    private Unbinder mUnbinder;
    private final static int FOOTPRINT_1 = 1;
    private final static int FOOTPRINT_2 = 2;
    private final static int FOOTPRINT_3 = 3;
    private final static int FOOTPRINT_4 = 4;
    private final static int FOOTPRINT_5 = 5;
    private final static int FOOTPRINT_6 = 6;
    private final static int FULL_FOOTPRINT = 7;
    private final static int INJECT_WATER = 14;
    private final static int INJECTED_WATER = 15;
    private final static int INJECTED_WATER_FADE_OUT = 16;
    private final static int ELEPHANT_EXIT = 19;
    private final static int ELEPHANT_EXIT_DURATION = 2000;
    private final static int ELEPHANT_ENTER_DURATION = 1500;
    private boolean isResume;
    private boolean isStandHere;
    private boolean isWaveForActive;
    private boolean isRotateForStandRight;
    private VoicePlay mVoicePlay;
    private static final String TAG = "GestureActiveTwoStepFragment";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FOOTPRINT_1:
                    if (ivRightprint1 != null && ivLeftprint1 != null) {
                        ivLeftprint1.setVisibility(View.VISIBLE);
                        ivRightprint1.setVisibility(View.VISIBLE);
                    }
                    break;
                case FOOTPRINT_2:
                    if (ivRightprint2 != null && ivLeftprint2 != null) {
                        ivLeftprint2.setVisibility(View.VISIBLE);
                        ivRightprint2.setVisibility(View.VISIBLE);
                    }
                    break;
                case FOOTPRINT_3:
                    if (ivRightprint3 != null && ivLeftprint3 != null) {
                        ivLeftprint3.setVisibility(View.VISIBLE);
                        ivRightprint3.setVisibility(View.VISIBLE);
                    }
                    break;
                case FOOTPRINT_4:
                    if (ivRightprint4 != null && ivLeftprint4 != null) {
                        ivLeftprint4.setVisibility(View.VISIBLE);
                        ivRightprint4.setVisibility(View.VISIBLE);
                    }
                    break;
                case FOOTPRINT_5:
                    if (ivRightprint5 != null && ivLeftprint5 != null) {
                        ivLeftprint5.setVisibility(View.VISIBLE);
                        ivRightprint5.setVisibility(View.VISIBLE);
                    }
                    break;
                case FOOTPRINT_6:
                    if (ivRightprint6 != null && ivLeftprint6 != null) {
                        ivLeftprint6.setVisibility(View.VISIBLE);
                        ivRightprint6.setVisibility(View.VISIBLE);
                    }
                    break;
                case FULL_FOOTPRINT:
                    startRotateFootprint();
                    break;
                case INJECT_WATER:
                    injectingWaterAnimation();
                    break;
                case INJECTED_WATER:
                    mInjectingWaterAnimation.stop();
                    if (getActivity() != null) {
                        ((AdActivity) getActivity()).startAIScreenApp();
                    }
                    break;
                case INJECTED_WATER_FADE_OUT:
                    stopInjectWaterAnimation();
                    break;
                case ELEPHANT_EXIT:
                    startElephantExitAnimation();
                    break;
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
        View view = inflater.inflate(R.layout.fragment_gesture_activation, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        mVoicePlay = new VoicePlay(getContext());
        mEEnterObjAnimator = ObjectAnimator.ofFloat(ivElephantEnter, "translationX", 400, 0);
        mEExitObjAnimator = ObjectAnimator.ofFloat(ivElephantExit, "translationX", 0, 400);
        mFootprintRotateObjAnimator = ObjectAnimator.ofFloat(rlFootprint, "rotationX", 0, 30, 0);
        mFloorExitObjAnimator = ObjectAnimator.ofFloat(ivBottomFloor, "translationY", 0, 800);
        mFloorEnterObjAnimator = ObjectAnimator.ofFloat(ivBottomFloor, "translationY", 800, 0);
        mETipEnterObjAnimator = ObjectAnimator.ofFloat(tvGuideTip1, "translationX", 800, 0);
        mETipExitObjAnimator = ObjectAnimator.ofFloat(tvGuideTip1, "translationX", 0, 800);
        mElephantExitAnimation = (AnimationDrawable) ivElephantExit.getDrawable();
        mElephantEnterAnimation = (AnimationDrawable) ivElephantEnter.getDrawable();
        mElephantStopAnimation = (AnimationDrawable) ivElephantStop.getDrawable();
        mInjectingWaterAnimation = (AnimationDrawable) ivInjectingWaterTip.getDrawable();
        mWaveHandsAnimation = (AnimationDrawable) ivWaveHandsTip.getDrawable();
        startElephantEnterAnimation();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        isResume = true;
    }

    private void startElephantEnterAnimation() {
        YxLogger.d(TAG, "startElephantEnterAnimation");
        if (!mElephantEnterAnimation.isRunning()) {
            mElephantEnterAnimation.start();
        }
        if (!mEEnterObjAnimator.isRunning()) {
            mETipEnterObjAnimator.setDuration(ELEPHANT_ENTER_DURATION);
            mETipEnterObjAnimator.setStartDelay(500);
            mETipEnterObjAnimator.start();
            mFloorEnterObjAnimator.setDuration(500);
            mFloorEnterObjAnimator.setInterpolator(new DecelerateInterpolator());
            mFloorEnterObjAnimator.start();
            mEEnterObjAnimator.setDuration(ELEPHANT_ENTER_DURATION);
            mEEnterObjAnimator.setInterpolator(new LinearInterpolator());
            mEEnterObjAnimator.setStartDelay(500);
            mEEnterObjAnimator.start();
            mEEnterObjAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    YxLogger.d(TAG, toString() + "onAnimationStart");
                    super.onAnimationStart(animation);
                    if (ivElephantEnter != null) {
                        ivElephantEnter.setVisibility(View.VISIBLE);
                    }
                    if (tvGuideTip1 != null) {
                        tvGuideTip1.setVisibility(View.VISIBLE);
                    }
                    showFootPrint();
                    if (isResume) {
                        mVoicePlay.playVoice(R.raw.please_stand_footprint);
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mElephantEnterAnimation.stop();
                    mEEnterObjAnimator.cancel();
                    mETipEnterObjAnimator.cancel();
                    mFloorEnterObjAnimator.cancel();
                    if (tvGuideTip1 != null) {
                        tvGuideTip1.setText(R.string.guide_tips_1);
                    }
                    startElephantStopAnimation();
                }
            });
        }
    }

    private void startElephantStopAnimation() {
        if (ivElephantEnter != null) {
            ivElephantEnter.setVisibility(View.INVISIBLE);
        }
        if (ivElephantStop != null) {
            ivElephantStop.setVisibility(View.VISIBLE);
        }
        if (!mElephantStopAnimation.isRunning()) {
            mElephantStopAnimation.start();
        }
//        nextAfterStandRight();

    }

    private void startRotateFootprint() {
        if (!mFootprintRotateObjAnimator.isRunning()) {
            mFootprintRotateObjAnimator.setRepeatCount(20);
            mFootprintRotateObjAnimator.setDuration(1500);
            mFootprintRotateObjAnimator.start();
        }
        isRotateForStandRight = true;
        if (mTrackingMessage != null && !mTrackingMessage.isStandHere()) {
            nextAfterStandRight();
        }
    }

    private void nextAfterStandRight() {
        YxLogger.d(TAG, "nextAfterStandRight");
        if (mFootprintRotateObjAnimator != null) {
            mFootprintRotateObjAnimator.end();
            mFootprintRotateObjAnimator.cancel();
        }
        if (tvGuideTip1 != null) {
            tvGuideTip1.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    tvGuideTip1.setText(getString(R.string.guide_tips_2));
                    showFullFootprint();
//                    mVoicePlay.playVoice(R.raw.great);
                }
            }, 50);

            tvGuideTip1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (tvGuideTip1 != null) {
                        tvGuideTip1.setText(R.string.guide_tips_3);
                    }
                    hideFullFootprint();
                    startWaveHandTipAnimation();
                    if (isResume) {
                        mVoicePlay.playVoice(R.raw.wave_your_right_hands);
                    }
                }
            }, 500);
        }
    }

    private void startWaveHandTipAnimation() {
        YxLogger.d(TAG, "startWaveHandTipAnimation");
        fadeIn(ivWaveHandsTip);
        mWaveHandsAnimation.start();
        //test,isActived=true接收信号执行注水ing动画
        isWaveForActive = true;
        if (mTrackingMessage != null && mTrackingMessage.isActived()) {
            waveActiveSucess();
        }
    }

    private void waveActiveSucess() {
        //接收信号执行注水ing动画
        mMessage = mHandler.obtainMessage();
        mMessage.what = INJECT_WATER;
        mHandler.sendMessageDelayed(mMessage, 200);//此处提示摇手3000ms(自定义摇多久，是循环的帧动画)

        mMessage = mHandler.obtainMessage();
        mMessage.what = ELEPHANT_EXIT;
        mHandler.sendMessageDelayed(mMessage, 200);//此处提示摇手3000ms(自定义摇多久，是循环的帧动画)+注水的一半时间1000ms =4000ms，后开始退出动画
    }

    /*
     * 小象退出总时长=ELEPHANT_EXIT_DURATION:2000
     * 地面退出时长300，延时1000执行
     * 挥手人形退出时长1000，延时1000执行
     * */
    private void startElephantExitAnimation() {
        if (ivElephantStop != null) {
            ivElephantStop.setVisibility(View.INVISIBLE);
        }
        if (ivElephantExit != null) {
            ivElephantExit.setVisibility(View.VISIBLE);
        }
        if (!mElephantStopAnimation.isRunning()) {
            mElephantStopAnimation.stop();
        }
        if (!mElephantExitAnimation.isRunning()) {
            mElephantExitAnimation.start();
        }
        if (!mEExitObjAnimator.isRunning()) {
            mEExitObjAnimator.setInterpolator(new LinearInterpolator());
            mETipExitObjAnimator.setInterpolator(new LinearInterpolator());
            mFloorExitObjAnimator.setDuration(300);
            mFloorExitObjAnimator.setStartDelay(1000);
            mFloorExitObjAnimator.start();
            mFloorExitObjAnimator.setInterpolator(new AccelerateInterpolator());
            if (mAnimatorSet == null) {
                mAnimatorSet = new AnimatorSet();
            }
            mAnimatorSet.play(mEExitObjAnimator).with(mETipExitObjAnimator);
            mAnimatorSet.setDuration(ELEPHANT_EXIT_DURATION);
            mAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mMessage = mHandler.obtainMessage();
                    mMessage.what = INJECTED_WATER_FADE_OUT;
//                    mHandler.sendMessageDelayed(mMessage, 1000);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mElephantExitAnimation.stop();
                    mEExitObjAnimator.cancel();
                    mETipEnterObjAnimator.cancel();
                    if (ivElephantExit != null) {
                        ivElephantExit.setVisibility(View.INVISIBLE);
                    }
                    if (tvGuideTip1 != null) {
                        tvGuideTip1.setText(R.string.guide_tips_1);
                    }
                }
            });
            mAnimatorSet.start();
        }
    }

    private void stopInjectWaterAnimation() {
        fadeOut(ivInjectingWaterTip, 1000);
    }

    private void injectingWaterAnimation() {
        if (ivInjectingWaterTip != null) {
            ivInjectingWaterTip.setVisibility(View.VISIBLE);
        }
        if (ivWaveHandsTip != null) {
            ivWaveHandsTip.setVisibility(View.INVISIBLE);
        }
        mWaveHandsAnimation.stop();
        mInjectingWaterAnimation.start();
        mMessage = mHandler.obtainMessage();
        mMessage.what = INJECTED_WATER;
        mHandler.sendMessageDelayed(mMessage, 570);
    }


    private void showFootPrint() {
        for (int i = 0; i < 7; i++) {
            mMessage = mHandler.obtainMessage();
            mMessage.what = i + 1;
            mHandler.sendMessageDelayed(mMessage, i * 200 + 2000);
        }
    }

    private void hideFootprint() {
        if (ivLeftprint1 != null) {
            ivLeftprint1.setVisibility(View.GONE);
        }
        if (ivLeftprint2 != null) {
            ivLeftprint2.setVisibility(View.GONE);
        }
        if (ivLeftprint3 != null) {
            ivLeftprint3.setVisibility(View.GONE);
        }
        if (ivLeftprint4 != null) {
            ivLeftprint4.setVisibility(View.GONE);
        }
        if (ivLeftprint5 != null) {
            ivLeftprint5.setVisibility(View.GONE);
        }
        if (ivLeftprint6 != null) {
            ivLeftprint6.setVisibility(View.GONE);
        }
        if (ivRightprint1 != null) {
            ivRightprint1.setVisibility(View.GONE);
        }
        if (ivRightprint2 != null) {
            ivRightprint2.setVisibility(View.GONE);
        }
        if (ivRightprint3 != null) {
            ivRightprint3.setVisibility(View.GONE);
        }
        if (ivRightprint4 != null) {
            ivRightprint4.setVisibility(View.GONE);
        }
        if (ivRightprint5 != null) {
            ivRightprint5.setVisibility(View.GONE);
        }
        if (ivRightprint6 != null) {
            ivRightprint6.setVisibility(View.GONE);
        }
    }


    private void hideFullFootprint() {
        fadeOut(ivLeftprint, 1000);
        fadeOut(ivRightprint, 1000);
    }

    private void showFullFootprint() {
        if (ivLeftprint != null) {
            ivLeftprint.setVisibility(View.VISIBLE);
        }
        if (ivRightprint != null) {
            ivRightprint.setVisibility(View.VISIBLE);
        }
        hideFootprint();
    }

    private void fadeIn(View view) {
        Animation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(2000);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
            view.startAnimation(animation);
        }
    }

    private void fadeOut(View view, int duration) {
        Animation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(duration);
        if (view != null) {
            view.startAnimation(animation);
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onPause() {
        super.onPause();
        isResume = false;
//        onDestroy();
        EventBus.getDefault().unregister(this);
        if (mElephantEnterAnimation.isRunning()) {
            mElephantEnterAnimation.stop();
        }
        if (mElephantStopAnimation.isRunning()) {
            mElephantStopAnimation.stop();
        }
        if (mElephantExitAnimation.isRunning()) {
            mElephantExitAnimation.stop();
        }
        if (mInjectingWaterAnimation.isRunning()) {
            mInjectingWaterAnimation.stop();
        }
        if (mWaveHandsAnimation.isRunning()) {
            mWaveHandsAnimation.stop();
        }

        if (mFootprintRotateObjAnimator.isRunning()) {
            mFootprintRotateObjAnimator.cancel();
        }
        if (mEEnterObjAnimator.isRunning()) {
            mEEnterObjAnimator.cancel();
        }
        if (mEExitObjAnimator.isRunning()) {
            mEExitObjAnimator.cancel();
        }
        if (mETipEnterObjAnimator.isRunning()) {
            mETipEnterObjAnimator.cancel();
        }
        if (mETipExitObjAnimator.isRunning()) {
            mETipExitObjAnimator.cancel();
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
        YxLogger.d(TAG, toString() + ": onMessageShowEvent");
        if (message.getType() == EventBusMessage.ACTIVE_TIP) {
            mTrackingMessage = (TrackingMessage) message.getObject();
            if (mTrackingMessage.isActived() && isWaveForActive) {
                waveActiveSucess();
                isWaveForActive = false;
            }
            if (!mTrackingMessage.isStandHere() && isRotateForStandRight && !isWaveForActive) {
                nextAfterStandRight();
                isRotateForStandRight = false;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mVoicePlay != null) {
            mVoicePlay.stop();
        }
//        EventBus.getDefault().unregister(this);
    }
}
