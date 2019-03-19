package com.imprexion.aiscreen.advertising;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imprexion.aiscreen.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvertisingActivity extends AppCompatActivity {

    //    @BindView(R.id.lav_inject_water)
//    LottieAnimationView lavInjectWater;
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

    private AnimationDrawable mElephantEnterAnimation;
    private AnimationDrawable mElephantStopAnimation;
    private AnimationDrawable mElephantExitAnimation;
    private Message mMessage;
    private AnimationDrawable mInjectWaterAnimation;
    private ObjectAnimator mLFootprintScaleObjAnimatorX;
    private ObjectAnimator mLFootprintScaleObjAnimatorY;
    private ObjectAnimator mRFootprintScaleObjAnimatorX;
    private ObjectAnimator mRFootprintScaleObjAnimatorY;
    private ObjectAnimator mEEnterObjAnimator;
    private ObjectAnimator mEExitObjAnimator;
    private ObjectAnimator mETipObjAnimator;
    private ObjectAnimator mFloorObjAnimator;
    private ObjectAnimator mInjectWaterObjAnimator;
    private List<Message> mMessageList;
    private AnimatorSet mAnimatorSet;
    private final static int LEFT_FOOTPRINT_1 = 1;
    private final static int LEFT_FOOTPRINT_2 = 2;
    private final static int LEFT_FOOTPRINT_3 = 3;
    private final static int LEFT_FOOTPRINT_4 = 4;
    private final static int LEFT_FOOTPRINT_5 = 5;
    private final static int LEFT_FOOTPRINT_6 = 6;
    private final static int RIGHT_FOOTPRINT_1 = 7;
    private final static int RIGHT_FOOTPRINT_2 = 8;
    private final static int RIGHT_FOOTPRINT_3 = 9;
    private final static int RIGHT_FOOTPRINT_4 = 10;
    private final static int RIGHT_FOOTPRINT_5 = 11;
    private final static int RIGHT_FOOTPRINT_6 = 12;
    private final static int FULL_FOOTPRINT = 13;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LEFT_FOOTPRINT_1:
                    ivLeftprint1.setVisibility(View.VISIBLE);
                    break;
                case LEFT_FOOTPRINT_2:
                    ivLeftprint2.setVisibility(View.VISIBLE);
                    break;
                case LEFT_FOOTPRINT_3:
                    ivLeftprint3.setVisibility(View.VISIBLE);
                    break;
                case LEFT_FOOTPRINT_4:
                    ivLeftprint4.setVisibility(View.VISIBLE);
                    break;
                case LEFT_FOOTPRINT_5:
                    ivLeftprint5.setVisibility(View.VISIBLE);
                    break;
                case LEFT_FOOTPRINT_6:
                    ivLeftprint6.setVisibility(View.VISIBLE);
                    break;
                case RIGHT_FOOTPRINT_1:
                    ivRightprint1.setVisibility(View.VISIBLE);
                    break;
                case RIGHT_FOOTPRINT_2:
                    ivRightprint2.setVisibility(View.VISIBLE);
                    break;
                case RIGHT_FOOTPRINT_3:
                    ivRightprint3.setVisibility(View.VISIBLE);
                    break;
                case RIGHT_FOOTPRINT_4:
                    ivRightprint4.setVisibility(View.VISIBLE);
                    break;
                case RIGHT_FOOTPRINT_5:
                    ivRightprint5.setVisibility(View.VISIBLE);
                    break;
                case RIGHT_FOOTPRINT_6:
                    ivRightprint6.setVisibility(View.VISIBLE);
                    break;
                case FULL_FOOTPRINT:
//                    hideFootprint();
//                    rlFootprint.setBackground(getResources().getDrawable(R.drawable.full_footprint));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        ButterKnife.bind(this);
        mEEnterObjAnimator = ObjectAnimator.ofFloat(ivElephantEnter, "translationX", 400, 0);
        mEExitObjAnimator = ObjectAnimator.ofFloat(ivElephantExit, "translationX", 0, 400);
//        mETipObjAnimator = ObjectAnimator.ofFloat(tvGuideTip1, "translationX", 800, 0);
        mElephantExitAnimation = (AnimationDrawable) ivElephantExit.getDrawable();
        mElephantEnterAnimation = (AnimationDrawable) ivElephantEnter.getDrawable();
        mElephantStopAnimation = (AnimationDrawable) ivElephantStop.getDrawable();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startElephantEnterAnimation();
    }

    private void startElephantEnterAnimation() {
        if (!mElephantEnterAnimation.isRunning()) {
            mElephantEnterAnimation.start();
        }
        if (!mEEnterObjAnimator.isRunning()) {
            mETipObjAnimator = ObjectAnimator.ofFloat(tvGuideTip1, "translationX", 800, 0);
            mFloorObjAnimator = ObjectAnimator.ofFloat(ivBottomFloor, "translationY", 800, 0);
            mETipObjAnimator.setDuration(3000);
            mETipObjAnimator.setStartDelay(500);
            mETipObjAnimator.start();
            mFloorObjAnimator.setDuration(500);
            mFloorObjAnimator.setInterpolator(new DecelerateInterpolator());
            mFloorObjAnimator.start();
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
                    mETipObjAnimator.cancel();
                    mFloorObjAnimator.cancel();
                    startElephantStopAnimation();
                }
            });
        }
    }

    private void startElephantStopAnimation() {
        ivElephantEnter.setVisibility(View.INVISIBLE);
        ivElephantStop.setVisibility(View.VISIBLE);
//        tvGuideTip1.setVisibility(View.VISIBLE);
        tvGuideTip1.setText(getString(R.string.guide_tips_1));
        if (!mElephantStopAnimation.isRunning()) {
            mElephantStopAnimation.start();
            ivElephantStop.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startElephantExitAnimation();
                }
            }, 9000);
        }
        tvGuideTip1.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvGuideTip1.setText(getString(R.string.guide_tips_2));
                showFullFootprint();
            }
        }, 3000);
        tvGuideTip1.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvGuideTip1.setText(getString(R.string.guide_tips_3));
                hideFullFootprint();
                startInjectWaterAnimation();
            }
        }, 6000);

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
            mETipObjAnimator = ObjectAnimator.ofFloat(tvGuideTip1, "translationX", 0, 800);
            mFloorObjAnimator = ObjectAnimator.ofFloat(ivBottomFloor, "translationY", 0, 800);
            mEExitObjAnimator.setInterpolator(new LinearInterpolator());
            mETipObjAnimator.setInterpolator(new LinearInterpolator());
            mFloorObjAnimator.setDuration(500);
            mFloorObjAnimator.setStartDelay(2500);
            mFloorObjAnimator.start();
            mFloorObjAnimator.setInterpolator(new AccelerateInterpolator());
            if (mAnimatorSet == null) {
                mAnimatorSet = new AnimatorSet();
            }
            mAnimatorSet.play(mEExitObjAnimator).with(mETipObjAnimator);
            mAnimatorSet.setDuration(3000);
            mAnimatorSet.start();
            mAnimatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mElephantExitAnimation.stop();
                    mEExitObjAnimator.cancel();
                    mETipObjAnimator.cancel();
                    ivElephantExit.setVisibility(View.INVISIBLE);
//                    tvGuideTip1.setVisibility(View.INVISIBLE);
                    tvGuideTip1.setText(R.string.guide_tips_1);
                    stopInjectWaterAnimation();
                }
            });
        }
    }


    private void showFootPrint() {
//        if (mMessageList == null) {
//            mMessageList = new ArrayList<>();
//            for (int i = 0; i < 13; i++) {
//                mMessage = new Message();
//                mMessage.what = i + 1;
//                mMessageList.add(mMessage);
//            }
//        }
        for (int i = 0; i < 13; i++) {
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
        rlFootprint.setBackground(null);
        mLFootprintScaleObjAnimatorX = ObjectAnimator.ofFloat(ivLeftprint, "ScaleX", 1, 0);
        mLFootprintScaleObjAnimatorY = ObjectAnimator.ofFloat(ivLeftprint, "ScaleY", 1, 0);
        mRFootprintScaleObjAnimatorX = ObjectAnimator.ofFloat(ivRightprint, "ScaleX", 1, 0);
        mRFootprintScaleObjAnimatorY = ObjectAnimator.ofFloat(ivRightprint, "ScaleY", 1, 0);
        mRFootprintScaleObjAnimatorX.setDuration(500);
        mRFootprintScaleObjAnimatorY.setDuration(500);
        mRFootprintScaleObjAnimatorX.setDuration(500);
        mRFootprintScaleObjAnimatorY.setDuration(500);
        mRFootprintScaleObjAnimatorX.setStartDelay(500);
        mRFootprintScaleObjAnimatorY.setStartDelay(500);
        mLFootprintScaleObjAnimatorX.start();
        mLFootprintScaleObjAnimatorY.start();
        mRFootprintScaleObjAnimatorX.start();
        mRFootprintScaleObjAnimatorY.start();
        mRFootprintScaleObjAnimatorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ivLeftprint.setVisibility(View.INVISIBLE);
                ivRightprint.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void showFullFootprint() {
        ivLeftprint.setVisibility(View.INVISIBLE);
        ivRightprint.setVisibility(View.INVISIBLE);
//        rlFootprint.setBackground(getResources().getDrawable(R.drawable.full_footprint));
        mLFootprintScaleObjAnimatorX = ObjectAnimator.ofFloat(ivLeftprint, "ScaleX", 0, 1);
        mLFootprintScaleObjAnimatorY = ObjectAnimator.ofFloat(ivLeftprint, "ScaleY", 0, 1);
        mRFootprintScaleObjAnimatorX = ObjectAnimator.ofFloat(ivRightprint, "ScaleX", 0, 1);
        mRFootprintScaleObjAnimatorY = ObjectAnimator.ofFloat(ivRightprint, "ScaleY", 0, 1);
//        mRFootprintScaleObjAnimatorX.setDuration(500);
//        mRFootprintScaleObjAnimatorY.setDuration(500);
//        mRFootprintScaleObjAnimatorX.setDuration(500);
//        mRFootprintScaleObjAnimatorY.setDuration(500);
        mRFootprintScaleObjAnimatorX.start();
        mRFootprintScaleObjAnimatorY.start();
        mLFootprintScaleObjAnimatorX.start();
        mLFootprintScaleObjAnimatorY.start();
        mLFootprintScaleObjAnimatorY.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                hideFootprint();
                ivLeftprint.setVisibility(View.VISIBLE);
                ivRightprint.setVisibility(View.VISIBLE);
            }
        });

    }


    private void startInjectWaterAnimation() {
//        lavInjectWater.setVisibility(View.VISIBLE);
//        lavInjectWater.setImageAssetsFolder("imagesInjectWater");
//        lavInjectWater.setAnimation("inject_water_json.json");
//        lavInjectWater.playAnimation();
        mInjectWaterObjAnimator = ObjectAnimator.ofFloat(ivInjectWaterTip, "translationX", -800, 0);
        mInjectWaterObjAnimator.setDuration(2000);
        mInjectWaterObjAnimator.setInterpolator(new DecelerateInterpolator());
        mInjectWaterObjAnimator.start();
        ivInjectWaterTip.setVisibility(View.VISIBLE);
        if (mInjectWaterAnimation == null) {
            mInjectWaterAnimation = (AnimationDrawable) ivInjectWaterTip.getDrawable();
        }
        mInjectWaterAnimation.start();
    }

    private void stopInjectWaterAnimation() {
//        lavInjectWater.clearAnimation();
//        lavInjectWater.cancelAnimation();
//        lavInjectWater.clearAnimation();
//        lavInjectWater.setVisibility(View.INVISIBLE);
        mInjectWaterObjAnimator = ObjectAnimator.ofFloat(ivInjectWaterTip, "translationX", 0, -1000);
        mInjectWaterObjAnimator.setDuration(2000);
        mInjectWaterObjAnimator.setInterpolator(new DecelerateInterpolator());
        mInjectWaterObjAnimator.start();
        mInjectWaterObjAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ivInjectWaterTip.setVisibility(View.INVISIBLE);
                mInjectWaterAnimation.stop();
                startElephantEnterAnimation();//循环
            }
        });
    }
}
