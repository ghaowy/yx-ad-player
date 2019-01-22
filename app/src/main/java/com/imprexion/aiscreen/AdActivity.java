package com.imprexion.aiscreen;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imprexion.aiscreen.ad.AdContract;
import com.imprexion.aiscreen.ad.AdPresenter;
import com.imprexion.aiscreen.bean.WeatherInfo;
import com.imprexion.aiscreen.main.MainActivity;
import com.imprexion.aiscreen.status.StatusFragment;
import com.imprexion.aiscreen.tools.IconFontTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdActivity extends AppCompatActivity implements AdContract.AdView {

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
    @BindView(R.id.rl_status)
    RelativeLayout rlStatus;
    @BindView(R.id.tempreture_1)
    IconFontTextView tempreture1;
    @BindView(R.id.tempreture_2)
    IconFontTextView tempreture2;
    @BindView(R.id.tv_toutaio_icon)
    IconFontTextView tvToutaioIcon;
    @BindView(R.id.tv_weather_location)
    TextView tvWeatherLocation;

    private String tempretures[];
    private static final String TAG = "AdActivity";


    private AnimationDrawable mAnimationDrawable;
    private ObjectAnimator mObjectAnimator;
    private ObjectAnimator mObjectAnimatorScale;
    private ObjectAnimator mObjectAnimatorSun;
    private ObjectAnimator mObjectAnimatorToutiaoY;
    private ObjectAnimator mObjectAnimatorToutiaoX;
    private AnimatorSet mAnimatorSetToutiao = new AnimatorSet();
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private StatusFragment mStatusFragment;
    private float mCurrentpositionY;
    private float mCurrentpositionX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        ButterKnife.bind(this);
        initStatus();
        ivFist.post(new Runnable() {
            @Override
            public void run() {
                mCurrentpositionY = ivFist.getTranslationY();
            }
        });
        ivSun.post(new Runnable() {
            @Override
            public void run() {
                mCurrentpositionX = ivSun.getTranslationX();
            }
        });

    }

    private void initStatus() {

        if (mStatusFragment == null) {
            mStatusFragment = new StatusFragment();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rl_status, mStatusFragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnim();
        setWeatherInfo();

    }

    private void setWeatherInfo() {
        AdPresenter adPresenter = new AdPresenter(this);
        adPresenter.updateWeather();
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
                mObjectAnimator = ObjectAnimator.ofFloat(ivFist, "translationY",
                        mCurrentpositionY, mCurrentpositionY - 50, mCurrentpositionY);
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
                mObjectAnimatorSun = ObjectAnimator.ofFloat(ivSun, "translationX", mCurrentpositionX, mCurrentpositionX - 300, mCurrentpositionX);
                mObjectAnimatorSun.setInterpolator(new LinearInterpolator());
                mObjectAnimatorSun.setDuration(40000);
                mObjectAnimatorSun.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimatorSun.start();
            }
        });
        //头条广播动画
        tvToutaioIcon.post(new Runnable() {
            @Override
            public void run() {
                mObjectAnimatorToutiaoX = ObjectAnimator.ofFloat(tvToutaioIcon, "scaleX", 0.5f, 1);
                mObjectAnimatorToutiaoY = ObjectAnimator.ofFloat(tvToutaioIcon, "scaleY", 0.5f, 1);
                mObjectAnimatorToutiaoX.setRepeatCount(ObjectAnimator.RESTART);
                mObjectAnimatorToutiaoX.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimatorToutiaoY.setRepeatCount(ObjectAnimator.RESTART);
                mObjectAnimatorToutiaoY.setRepeatCount(ObjectAnimator.INFINITE);
                mAnimatorSetToutiao.play(mObjectAnimatorToutiaoX).with(mObjectAnimatorToutiaoY);
                mAnimatorSetToutiao.setDuration(1000);
                mAnimatorSetToutiao.start();
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

    @OnClick({R.id.ll_ad})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_ad:
                startActivity(new Intent(AdActivity.this, MainActivity.class));
                break;
            default:
                break;
        }
    }


    @Override
    public void updateWeather(WeatherInfo weatherInfo) {
        tempretures = getResources().getStringArray(R.array.tempretures);
        WeatherInfo.HeWeather6 heWeather6 = weatherInfo.getHeWeather6().get(0);
//        Log.d(TAG,"heWeather6="+new Gson().toJson(heWeather6));
        String status = heWeather6.getStatus();
        if (!status.equals("ok")) {
            return;
        }
        int temp = 0;
        if (heWeather6 != null) {
            temp = weatherInfo.getHeWeather6().get(0).getNow().getTmp();
        }
        if (temp / 10 != 0) {
            tempreture1.setVisibility(View.VISIBLE);
            tempreture1.setText(tempretures[temp / 10]);
        } else {
            tempreture1.setVisibility(View.INVISIBLE);
        }
        tempreture2.setText(tempretures[temp % 10]);
        tvWeatherLocation.setText(heWeather6.getBasic().getLocation()+"."+heWeather6.getNow().getCond_txt());
    }
}
