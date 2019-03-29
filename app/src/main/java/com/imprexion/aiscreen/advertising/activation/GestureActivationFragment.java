package com.imprexion.aiscreen.advertising.activation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
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

import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.advertising.AdvertisingActivity;
import com.imprexion.aiscreen.main.MainActivity;
import com.imprexion.aiscreen.main.camera.CameraFragment;
import com.imprexion.aiscreen.main.rainControl.RainControlFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class GestureActivationFragment extends Fragment implements View.OnClickListener {

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
    @BindView(R.id.iv_inject_water_tip)
    ImageView ivInjectWaterTip;
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
    private AnimationDrawable mInjectWaterAnimation;
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
    private final static int ADD_CAMERE = 17;
    private final static int ADD_RAIN = 18;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case FOOTPRINT_1:
                    ivLeftprint1.setVisibility(View.VISIBLE);
                    ivRightprint1.setVisibility(View.VISIBLE);
                    break;
                case FOOTPRINT_2:
                    ivLeftprint2.setVisibility(View.VISIBLE);
                    ivRightprint2.setVisibility(View.VISIBLE);
                    break;
                case FOOTPRINT_3:
                    ivLeftprint3.setVisibility(View.VISIBLE);
                    ivRightprint3.setVisibility(View.VISIBLE);
                    break;
                case FOOTPRINT_4:
                    ivLeftprint4.setVisibility(View.VISIBLE);
                    ivRightprint4.setVisibility(View.VISIBLE);
                    break;
                case FOOTPRINT_5:
                    ivLeftprint5.setVisibility(View.VISIBLE);
                    ivRightprint5.setVisibility(View.VISIBLE);
                    break;
                case FOOTPRINT_6:
                    ivLeftprint6.setVisibility(View.VISIBLE);
                    ivRightprint6.setVisibility(View.VISIBLE);
                    break;
                case FULL_FOOTPRINT:
                    startRotateFootprint();
                    break;
                case INJECT_WATER:
                    injectingWaterAnimation();
                    break;
                case INJECTED_WATER:
                    mInjectingWaterAnimation.stop();
                    break;
                case INJECTED_WATER_FADE_OUT:
                    stopInjectWaterAnimation();
                    break;
                case ADD_CAMERE:
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_gesture, new CameraFragment()).commitAllowingStateLoss();
                    break;
                case ADD_RAIN:
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment_gesture, new RainControlFragment()).commitAllowingStateLoss();
                    break;
                default:
                    break;
            }
        }
    };

    private void startRotateFootprint() {
        if (!mFootprintRotateObjAnimator.isRunning()) {
            mFootprintRotateObjAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            mFootprintRotateObjAnimator.setDuration(1500);
            mFootprintRotateObjAnimator.start();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_gesture_activation, container, false);
        mUnbinder = ButterKnife.bind(this, view);
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
        mInjectWaterAnimation = (AnimationDrawable) ivInjectWaterTip.getDrawable();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flFragmentGesture.getChildCount() == 0) {
            mMessage = mHandler.obtainMessage();
            mMessage.what = ADD_CAMERE;
            mHandler.sendMessageDelayed(mMessage, 100);
            mMessage = mHandler.obtainMessage();
            mMessage.what = ADD_RAIN;
            mHandler.sendMessageDelayed(mMessage, 400);
        }
        startElephantEnterAnimation();

    }

    private void startElephantEnterAnimation() {
        if (!mElephantEnterAnimation.isRunning()) {
            mElephantEnterAnimation.start();
        }
//        ivElephantExit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(AdvertisingActivity.this, MainActivity.class));
//            }
//        });
        if (!mEEnterObjAnimator.isRunning()) {
            mETipEnterObjAnimator.setDuration(3000);
            mETipEnterObjAnimator.setStartDelay(500);
            mETipEnterObjAnimator.start();
            mFloorEnterObjAnimator.setDuration(500);
            mFloorEnterObjAnimator.setInterpolator(new DecelerateInterpolator());
            mFloorEnterObjAnimator.start();
            mEEnterObjAnimator.setDuration(3000);
            mEEnterObjAnimator.setInterpolator(new LinearInterpolator());
            mEEnterObjAnimator.setStartDelay(500);
            mEEnterObjAnimator.start();
            mEEnterObjAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    ivElephantEnter.setVisibility(View.VISIBLE);
                    tvGuideTip1.setVisibility(View.VISIBLE);
                    showFootPrint();
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mElephantEnterAnimation.stop();
                    mEEnterObjAnimator.cancel();
                    mETipEnterObjAnimator.cancel();
                    mFloorEnterObjAnimator.cancel();
                    startElephantStopAnimation();
                }
            });
        }
    }

    private void startElephantStopAnimation() {
        ivElephantEnter.setVisibility(View.INVISIBLE);
        ivElephantStop.setVisibility(View.VISIBLE);
        tvGuideTip1.setText(getString(R.string.guide_tips_1));
        if (!mElephantStopAnimation.isRunning()) {
            mElephantStopAnimation.start();
            ivElephantStop.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startElephantExitAnimation();
                }
            }, 12000);
        }
        tvGuideTip1.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvGuideTip1.setText(getString(R.string.guide_tips_2));
                showFullFootprint();
            }
        }, 4000);
        tvGuideTip1.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvGuideTip1.setText(getString(R.string.guide_tips_3));
                hideFullFootprint();
                startInjectWaterAnimation();
            }
        }, 7000);

    }

    private void startElephantExitAnimation() {
        ivElephantStop.setVisibility(View.INVISIBLE);
        ivElephantExit.setVisibility(View.VISIBLE);
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
            mFloorExitObjAnimator.setStartDelay(2000);
            mFloorExitObjAnimator.start();
            mFloorExitObjAnimator.setInterpolator(new AccelerateInterpolator());
            if (mAnimatorSet == null) {
                mAnimatorSet = new AnimatorSet();
            }
            mAnimatorSet.play(mEExitObjAnimator).with(mETipExitObjAnimator);
            mAnimatorSet.setDuration(3000);
            mAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mElephantExitAnimation.stop();
                    mEExitObjAnimator.cancel();
                    mETipEnterObjAnimator.cancel();
                    ivElephantExit.setVisibility(View.INVISIBLE);
                    tvGuideTip1.setText(R.string.guide_tips_1);
//                    startElephantEnterAnimation();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    mMessage = mHandler.obtainMessage();
                    mMessage.what = INJECTED_WATER_FADE_OUT;
                    mHandler.sendMessageDelayed(mMessage, 1000);
                }
            });
            mAnimatorSet.start();
        }
    }


    private void showFootPrint() {
        for (int i = 0; i < 7; i++) {
            mMessage = mHandler.obtainMessage();
            mMessage.what = i + 1;
            mHandler.sendMessageDelayed(mMessage, i * 200 + 2000);
        }
    }

    private void hideFootprint() {
        ivLeftprint1.setVisibility(View.INVISIBLE);
        ivLeftprint2.setVisibility(View.INVISIBLE);
        ivLeftprint3.setVisibility(View.INVISIBLE);
        ivLeftprint4.setVisibility(View.INVISIBLE);
        ivLeftprint5.setVisibility(View.INVISIBLE);
        ivLeftprint6.setVisibility(View.INVISIBLE);
        ivRightprint1.setVisibility(View.INVISIBLE);
        ivRightprint2.setVisibility(View.INVISIBLE);
        ivRightprint3.setVisibility(View.INVISIBLE);
        ivRightprint4.setVisibility(View.INVISIBLE);
        ivRightprint5.setVisibility(View.INVISIBLE);
        ivRightprint5.setVisibility(View.INVISIBLE);
        ivRightprint6.setVisibility(View.INVISIBLE);
    }

    private void hideFullFootprint() {
        fadeOut(ivLeftprint, 1000);
        fadeOut(ivRightprint, 1000);
    }

    private void showFullFootprint() {
        if (mFootprintRotateObjAnimator.isRunning()) {
            mFootprintRotateObjAnimator.cancel();
        }
        ivLeftprint.setVisibility(View.VISIBLE);
        ivRightprint.setVisibility(View.VISIBLE);
        hideFootprint();
    }


    private void startInjectWaterAnimation() {
        fadeIn(ivInjectWaterTip);
        mInjectWaterAnimation.start();

        //接收信号执行注水ing动画
        mMessage = mHandler.obtainMessage();
        mMessage.what = INJECT_WATER;
        mHandler.sendMessageDelayed(mMessage, 3000);
    }

    private void stopInjectWaterAnimation() {
        fadeOut(ivInjectingWaterTip, 2000);
    }

    private void injectingWaterAnimation() {
        ivInjectingWaterTip.setVisibility(View.VISIBLE);
        ivInjectWaterTip.setVisibility(View.INVISIBLE);
        mInjectWaterAnimation.stop();
        mInjectingWaterAnimation.start();
        mMessage = mHandler.obtainMessage();
        mMessage.what = INJECTED_WATER;
        mHandler.sendMessageDelayed(mMessage, 1970);
    }

    private void fadeIn(View view) {
        view.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(2000);
        view.startAnimation(animation);
    }

    private void fadeOut(View view, int duration) {
        Animation animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(duration);
        view.startAnimation(animation);
        view.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_gesture:
                startActivity(new Intent(getContext(), MainActivity.class));
                ((AdvertisingActivity) getActivity()).removeGestureFragment();
                break;
            default:
                break;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        onDestroy();
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
        if (mInjectWaterAnimation.isRunning()) {
            mInjectWaterAnimation.stop();
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }
}
