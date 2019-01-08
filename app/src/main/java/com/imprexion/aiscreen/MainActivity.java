package com.imprexion.aiscreen;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imprexion.aiscreen.tools.IconFontTextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.elephant_animation)
    ImageView elephantAnimation;
    @BindView(R.id.iv_fist)
    ImageView ivFist;
    @BindView(R.id.ll_ad)
    LinearLayout llAd;
    @BindView(R.id.iftv)
    IconFontTextView iftv;
    @BindView(R.id.iv_sun)
    ImageView ivSun;
    @BindView(R.id.tv_current_time)
    TextView tvCurrentTime;

    private AnimationDrawable mAnimationDrawable;
    private ObjectAnimator mObjectAnimator;
    private ObjectAnimator mObjectAnimatorScale;
    private ObjectAnimator mObjectAnimatorSun;
    private AnimatorSet mAnimatorSet = new AnimatorSet();

    private Thread mTimeThread;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    tvCurrentTime.setText(simpleDateFormat.format(new Date()));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnim();
        setcurrentTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAnim();
    }

    private void startAnim() {
        mAnimationDrawable = (AnimationDrawable) elephantAnimation.getDrawable();
        if (!mAnimationDrawable.isRunning()) {
            mAnimationDrawable.start();
        }
        //底部拳头动画
        ivFist.post(new Runnable() {
            @Override
            public void run() {
                float currentpositionY = ivFist.getTranslationY();
                mObjectAnimator = ObjectAnimator.ofFloat(ivFist, "translationY",
                        currentpositionY, currentpositionY - 50, currentpositionY);
                mObjectAnimator.setInterpolator(new BounceInterpolator());
                mObjectAnimator.setDuration(1500);
                mObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimator.start();

            }
        });
        //问号缩放动画
        iftv.post(new Runnable() {
            @Override
            public void run() {
                mObjectAnimatorScale = ObjectAnimator.ofFloat(iftv, "scaleX", 0.5f, 1, 0.5f);
                mObjectAnimatorScale.setRepeatCount(ObjectAnimator.INFINITE);
                ObjectAnimator mObjectAnimatorScale2 = ObjectAnimator.ofFloat(iftv, "scaleY", 0.5f, 1, 0.5f);
                mObjectAnimatorScale2.setRepeatCount(ObjectAnimator.INFINITE);
                mAnimatorSet.play(mObjectAnimatorScale).with(mObjectAnimatorScale2);
                mAnimatorSet.setDuration(3000);
                mAnimatorSet.start();
            }
        });
        //太阳平移动画
        ivSun.post(new Runnable() {
            @Override
            public void run() {
                float currentpositionX = ivSun.getTranslationX();
                mObjectAnimatorSun = ObjectAnimator.ofFloat(ivSun, "translationX", currentpositionX, currentpositionX - 300, currentpositionX);
                mObjectAnimatorSun.setInterpolator(new LinearInterpolator());
                mObjectAnimatorSun.setDuration(40000);
                mObjectAnimatorSun.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimatorSun.start();
            }
        });
    }

    private void stopAnim() {
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }
        if (mObjectAnimator != null) {
            mObjectAnimator.cancel();
        }
        if (mAnimatorSet != null) {
            mAnimatorSet.cancel();
        }
        if (mObjectAnimatorSun != null) {
            mObjectAnimatorSun.cancel();
        }
    }

    private void setcurrentTime() {
        mTimeThread = new Thread() {
            @Override
            public void run() {
                do {
                    try {
                        Thread.sleep(1000);
                        Message message = Message.obtain();
                        message.what = 1;
                        mHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (true);
            }
        };
        mTimeThread.start();
    }


}
