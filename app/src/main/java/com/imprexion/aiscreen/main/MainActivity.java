package com.imprexion.aiscreen.main;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.animation.SnowWeather;
import com.imprexion.aiscreen.functionPart.lottery.LotteryActivity;
import com.imprexion.aiscreen.functionPart.membership.MemberShipActivity;
import com.imprexion.aiscreen.functionPart.navigation.FloorMapActivity;
import com.imprexion.aiscreen.functionPart.parking.ParkingActivity;
import com.imprexion.aiscreen.functionPart.promotion.PromotionActivity;
import com.imprexion.aiscreen.status.StatusFragment;
import com.imprexion.aiscreen.tools.ScreenUtils;
import com.imprexion.aiscreen.tools.Tools;
import com.plattysoft.leonids.ParticleSystem;
import com.plattysoft.leonids.modifiers.AlphaModifier;
import com.plattysoft.leonids.modifiers.ScaleModifier;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ScreenUtils.NavigationListener {

    @BindView(R.id.rl_status)
    RelativeLayout rlStatus;
    @BindView(R.id.smoke_emiter)
    View smokeEmiter;
    @BindView(R.id.fl_back)
    FrameLayout flBack;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.rv_1)
    RippleView rv1;
    @BindView(R.id.rv_2)
    RippleView rv2;
    @BindView(R.id.rv_3)
    RippleView rv3;
    @BindView(R.id.lav)
    LottieAnimationView lav;
    @BindView(R.id.snow_weather_1)
    SnowWeather snowWeather1;
    @BindView(R.id.snow_weather_2)
    SnowWeather snowWeather2;
    @BindView(R.id.iv_navigation)
    ImageView ivNavigation;
    @BindView(R.id.iv_park)
    ImageView ivPark;
    @BindView(R.id.iv_membership)
    ImageView ivMembership;
    @BindView(R.id.iv_promotion)
    ImageView ivPromotion;
    @BindView(R.id.iv_lottery)
    ImageView ivLottery;
    @BindView(R.id.iv_emojidancer)
    ImageView ivEmojidancer;
    private ParticleSystem mParticleSystem;
    private boolean isHoverExit1 = true;
    private boolean isHoverExit2 = true;
    private boolean isHoverExit3 = true;
    private boolean isHoverExit4 = true;
    private boolean isHoverExit5 = true;
    private boolean isHoverExit6 = true;
    private static final String TAG = "MainActivity";
    private static final String URL = "http://172.16.2.207:5000/";
    private StatusFragment mStatusFragment;
    private boolean bgFlag = true;
    private ObjectAnimator mObjectAnimator;
    private ObjectAnimator mObjectAnimator2;
    private int mDuration = 8000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        lav.setImageAssetsFolder("images");
        lav.setAnimation("snow_man_json.json");
        lav.playAnimation();
        initStatus();
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
        smokeEmiter.post(new Runnable() {
            @Override
            public void run() {
                startSmokeAnim();
            }
        });
        setOnHoverListener1(ivNavigation);
        setOnHoverListener2(ivPark);
        setOnHoverListener3(ivMembership);
        setOnHoverListener4(ivPromotion);
        setOnHoverListener5(ivLottery);
        setOnHoverListener6(ivEmojidancer);

        ScreenUtils.setNavigationListener(rlMain, this);

        rv1.startSpread();
        rv2.startSpread();
        rv3.startSpread();
        snowWeather1.post(new Runnable() {
            @Override
            public void run() {
                startSnowAnimation();
            }
        });


    }

    private void startSnowAnimation() {

        snowWeather1.post(new Runnable() {
            @Override
            public void run() {
                mObjectAnimator = ObjectAnimator.ofFloat(snowWeather1, "translationY", -snowWeather1.getHeight(), snowWeather1.getHeight());
                mObjectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimator.setDuration(mDuration);
                mObjectAnimator.setInterpolator(new LinearInterpolator());
                mObjectAnimator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        snowWeather1.initData();
                    }
                });
                mObjectAnimator.start();
            }
        });

        snowWeather2.post(new Runnable() {
            @Override
            public void run() {
                snowWeather2.setVisibility(View.INVISIBLE);
                mObjectAnimator2 = ObjectAnimator.ofFloat(snowWeather2, "translationY", -snowWeather2.getHeight(), snowWeather2.getHeight());
                mObjectAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimator2.setDuration(mDuration);
                mObjectAnimator2.setStartDelay(mDuration / 2);
                mObjectAnimator2.setInterpolator(new LinearInterpolator());
                mObjectAnimator2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        snowWeather2.initData();
                    }

                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        snowWeather2.setVisibility(View.VISIBLE);
                    }
                });
                mObjectAnimator2.start();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mParticleSystem != null) {
            mParticleSystem.cancel();
            mParticleSystem = null;
        }
        if (rv1 != null) {
            rv1.stopSpread();
        }
        if (rv2 != null) {
            rv2.stopSpread();
        }
        if (rv3 != null) {
            rv3.stopSpread();
        }
    }


    @OnClick({R.id.iv_navigation, R.id.iv_park, R.id.iv_membership, R.id.iv_promotion, R.id.iv_lottery, R.id.iv_emojidancer, R.id.fl_back, R.id.rl_main})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_navigation:
                startActivity(new Intent(this, FloorMapActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/Floor"));
                break;
            case R.id.iv_park:
                startActivity(new Intent(this, ParkingActivity.class).putExtra("url", URL + "app/park"));
                break;
            case R.id.iv_membership:
                startActivity(new Intent(this, MemberShipActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/VipCard"));
                break;
            case R.id.iv_promotion:
                startActivity(new Intent(this, PromotionActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/Promotion"));
                break;
            case R.id.iv_lottery:
                startActivity(new Intent(this, LotteryActivity.class).putExtra("url", "http://oss.imprexion.cn/application/lottery/index.html"));
                break;
            case R.id.iv_emojidancer:
//                startActivity(new Intent(this, JsonActivity.class).putExtra("url", URL + "app"));
//                AISApplication.getSmdtManager().setMouseIcon(i++%5);
                break;
            case R.id.fl_back:
                onBackPressed();
                break;
            case R.id.rl_main:
                if (bgFlag) {
                    snowWeather1.setVisibility(View.GONE);
                    snowWeather2.setVisibility(View.GONE);
                    mObjectAnimator.cancel();
                    mObjectAnimator2.cancel();
                    mParticleSystem.cancel();
                    mParticleSystem = null;
                    lav.cancelAnimation();
                    lav.setImageAssetsFolder("imagesHome");
                    lav.setAnimation("goHome_json.json");
                    lav.playAnimation();
                    bgFlag = !bgFlag;
                } else {
                    snowWeather1.setVisibility(View.VISIBLE);
//                    snowWeather2.setVisibility(View.VISIBLE);
                    mObjectAnimator.start();
                    mObjectAnimator2.start();
                    if (mParticleSystem == null) {
                        startSmokeAnim();
                    }
                    lav.cancelAnimation();
                    lav.setImageAssetsFolder("images");
                    lav.setAnimation("snow_man_json.json");
                    lav.playAnimation();
                    bgFlag = !bgFlag;
                }
            default:
                break;
        }

    }

    private void setOnHoverListener1(final View view) {
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER: //鼠标进入view
                        isHoverExit1 = false;
                        Drawable drawable1 = getResources().getDrawable(R.drawable.count_hands_3);
                        Drawable zoomDrawable1 = Tools.zoomDrawable(drawable1, 200, 200);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER);
                        ((ImageView) view).setImageDrawable(zoomDrawable1);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit1) {
                                    Drawable drawable2 = getResources().getDrawable(R.drawable.count_hands_2);
                                    Drawable zoomDrawable2 = Tools.zoomDrawable(drawable2, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable2);
                                }
                            }
                        }, 1000);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit1) {
                                    Drawable drawable3 = getResources().getDrawable(R.drawable.count_hands_1);
                                    Drawable zoomDrawable3 = Tools.zoomDrawable(drawable3, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable3);
                                    onClick(view);
                                }
                            }
                        }, 2000);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: //鼠标离开view
                        ((ImageView) view).setImageDrawable(null);
                        isHoverExit1 = true;
                        break;
                }
                return false;
            }

        });
    }

    private void setOnHoverListener2(final View view) {
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER: //鼠标进入view
                        isHoverExit2 = false;
                        Drawable drawable1 = getResources().getDrawable(R.drawable.count_hands_3);
                        Drawable zoomDrawable1 = Tools.zoomDrawable(drawable1, 200, 200);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER);
                        ((ImageView) view).setImageDrawable(zoomDrawable1);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit2) {
                                    Drawable drawable2 = getResources().getDrawable(R.drawable.count_hands_2);
                                    Drawable zoomDrawable2 = Tools.zoomDrawable(drawable2, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable2);
                                }
                            }
                        }, 1000);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit2) {
                                    Drawable drawable3 = getResources().getDrawable(R.drawable.count_hands_1);
                                    Drawable zoomDrawable3 = Tools.zoomDrawable(drawable3, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable3);
                                    onClick(view);

                                }
                            }
                        }, 2000);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: //鼠标离开view
                        ((ImageView) view).setImageDrawable(null);
                        isHoverExit2 = true;
                        break;
                }
                return false;
            }

        });
    }

    private void setOnHoverListener3(final View view) {
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER: //鼠标进入view
                        isHoverExit3 = false;
                        Drawable drawable1 = getResources().getDrawable(R.drawable.count_hands_3);
                        Drawable zoomDrawable1 = Tools.zoomDrawable(drawable1, 200, 200);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER);
                        ((ImageView) view).setImageDrawable(zoomDrawable1);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit3) {
                                    Drawable drawable2 = getResources().getDrawable(R.drawable.count_hands_2);
                                    Drawable zoomDrawable2 = Tools.zoomDrawable(drawable2, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable2);
                                }
                            }
                        }, 1000);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit3) {
                                    Drawable drawable3 = getResources().getDrawable(R.drawable.count_hands_1);
                                    Drawable zoomDrawable3 = Tools.zoomDrawable(drawable3, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable3);
                                    onClick(view);

                                }
                            }
                        }, 2000);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: //鼠标离开view
                        ((ImageView) view).setImageDrawable(null);
                        isHoverExit3 = true;
                        break;
                }
                return false;
            }

        });
    }

    private void setOnHoverListener4(final View view) {
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER: //鼠标进入view
                        isHoverExit4 = false;
                        Drawable drawable1 = getResources().getDrawable(R.drawable.count_hands_3);
                        Drawable zoomDrawable1 = Tools.zoomDrawable(drawable1, 200, 200);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER);
                        ((ImageView) view).setImageDrawable(zoomDrawable1);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit4) {
                                    Drawable drawable2 = getResources().getDrawable(R.drawable.count_hands_2);
                                    Drawable zoomDrawable2 = Tools.zoomDrawable(drawable2, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable2);
                                }
                            }
                        }, 1000);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit4) {
                                    Drawable drawable3 = getResources().getDrawable(R.drawable.count_hands_1);
                                    Drawable zoomDrawable3 = Tools.zoomDrawable(drawable3, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable3);
                                    onClick(view);

                                }
                            }
                        }, 2000);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: //鼠标离开view
                        ((ImageView) view).setImageDrawable(null);
                        isHoverExit4 = true;
                        break;
                }
                return false;
            }

        });
    }

    private void setOnHoverListener5(final View view) {
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER: //鼠标进入view
                        isHoverExit5 = false;
                        Drawable drawable1 = getResources().getDrawable(R.drawable.count_hands_3);
                        Drawable zoomDrawable1 = Tools.zoomDrawable(drawable1, 200, 200);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER);
                        ((ImageView) view).setImageDrawable(zoomDrawable1);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit5) {
                                    Drawable drawable2 = getResources().getDrawable(R.drawable.count_hands_2);
                                    Drawable zoomDrawable2 = Tools.zoomDrawable(drawable2, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable2);
                                }
                            }
                        }, 1000);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit5) {
                                    Drawable drawable3 = getResources().getDrawable(R.drawable.count_hands_1);
                                    Drawable zoomDrawable3 = Tools.zoomDrawable(drawable3, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable3);
                                    onClick(view);

                                }
                            }
                        }, 2000);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: //鼠标离开view
                        ((ImageView) view).setImageDrawable(null);
                        isHoverExit5 = true;
                        break;
                }
                return false;
            }

        });
    }

    private void setOnHoverListener6(final View view) {
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER: //鼠标进入view
                        isHoverExit6 = false;
                        Drawable drawable1 = getResources().getDrawable(R.drawable.count_hands_3);
                        Drawable zoomDrawable1 = Tools.zoomDrawable(drawable1, 200, 200);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER);
                        ((ImageView) view).setImageDrawable(zoomDrawable1);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit6) {
                                    Drawable drawable2 = getResources().getDrawable(R.drawable.count_hands_2);
                                    Drawable zoomDrawable2 = Tools.zoomDrawable(drawable2, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable2);
                                }
                            }
                        }, 1000);
                        view.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!isHoverExit6) {
                                    Drawable drawable3 = getResources().getDrawable(R.drawable.count_hands_1);
                                    Drawable zoomDrawable3 = Tools.zoomDrawable(drawable3, 200, 200);
                                    ((ImageView) view).setImageDrawable(zoomDrawable3);
                                    onClick(view);

                                }
                            }
                        }, 2000);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: //鼠标离开view
                        ((ImageView) view).setImageDrawable(null);
                        isHoverExit6 = true;
                        break;
                }
                return false;
            }

        });
    }

    private void toastHover() {
        Toast.makeText(this, "hover", Toast.LENGTH_SHORT).show();
    }

    private void toastHoverMove() {
        Toast.makeText(this, "hoverMove", Toast.LENGTH_SHORT).show();
    }

    private void toastHoverExit() {
        Toast.makeText(this, "hoverExit", Toast.LENGTH_SHORT).show();
    }

    private void startSmokeAnim() {
        if (mParticleSystem == null) {
            mParticleSystem = new ParticleSystem(this, 20, R.drawable.smoke, 1000);
            mParticleSystem.setSpeedByComponentsRange(-0.025f, 0.025f, -0.06f, -0.08f)
                    .setAcceleration(0.0001f, -45)
                    .setInitialRotationRange(0, 360)
                    .addModifier(new AlphaModifier(255, 0, 0, 1000))
                    .addModifier(new ScaleModifier(0.4f, 0.9f, 0, 1000))
                    .emit(smokeEmiter, 20);
        }
    }


    @Override
    public void show() {
        Log.d(TAG, "navigation show");
        rlMain.postInvalidate();
        if (mParticleSystem != null) {
            mParticleSystem.cancel();
            mParticleSystem = null;
            startSmokeAnim();
        }
    }

    @Override
    public void hide() {
        Log.d(TAG, "navigation hide");
        rlMain.postInvalidate();
        if (mParticleSystem != null) {
            mParticleSystem.cancel();
            mParticleSystem = null;
            startSmokeAnim();
        }
    }
}
