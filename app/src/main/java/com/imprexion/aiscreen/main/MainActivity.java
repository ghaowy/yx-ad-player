package com.imprexion.aiscreen.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.animation.SnowWeather;
import com.imprexion.aiscreen.lottery.LotteryActivity;
import com.imprexion.aiscreen.membership.MemberShipActivity;
import com.imprexion.aiscreen.navigation.FloorMapActivity;
import com.imprexion.aiscreen.parking.ParkingActivity;
import com.imprexion.aiscreen.promotion.PromotionActivity;
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

    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.iv_4)
    ImageView iv4;
    @BindView(R.id.iv_5)
    ImageView iv5;
    @BindView(R.id.iv_6)
    ImageView iv6;
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
    @BindView(R.id.lav_snow_man)
    LottieAnimationView lavSnowMan;
    @BindView(R.id.snow_weather_1)
    SnowWeather snowWeather1;
    @BindView(R.id.snow_weather_2)
    SnowWeather snowWeather2;
    private ParticleSystem mParticleSystem;
    private boolean isHoverExit1 = true;
    private boolean isHoverExit2 = true;
    private boolean isHoverExit3 = true;
    private boolean isHoverExit4 = true;
    private boolean isHoverExit5 = true;
    private boolean isHoverExit6 = true;
    private static final String TAG = "MainActivity";
    private static final String URL = "http://172.162.2.153:88/";
    private StatusFragment mStatusFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        lavSnowMan.setImageAssetsFolder("images");
        lavSnowMan.setAnimation("snow_man_json.json");
        lavSnowMan.playAnimation();
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
                startAnim();
            }
        });
        setOnHoverListener1(iv1);
        setOnHoverListener2(iv2);
        setOnHoverListener3(iv3);
        setOnHoverListener4(iv4);
        setOnHoverListener5(iv5);
        setOnHoverListener6(iv6);

        ScreenUtils.setNavigationListener(rlMain, this);

        rv1.startSpread();
        rv2.startSpread();
        rv3.startSpread();

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


    @OnClick({R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4, R.id.iv_5, R.id.iv_6, R.id.fl_back})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_1:
                startActivity(new Intent(this, FloorMapActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/Floor"));
                break;
            case R.id.iv_2:
                startActivity(new Intent(this, ParkingActivity.class).putExtra("url", URL + "park"));
                break;
            case R.id.iv_3:
                startActivity(new Intent(this, MemberShipActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/VipCard"));
                break;
            case R.id.iv_4:
                startActivity(new Intent(this, PromotionActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/Promotion"));
                break;
            case R.id.iv_5:
                startActivity(new Intent(this, LotteryActivity.class).putExtra("url", URL + "app/markting/index.html"));
                break;
            case R.id.iv_6:
//                startActivity(new Intent(this, JsonActivity.class).putExtra("url", URL + "app"));
                break;
            case R.id.fl_back:
                onBackPressed();
                break;
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

    private void startAnim() {
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
            startAnim();
        }
    }

    @Override
    public void hide() {
        Log.d(TAG, "navigation hide");
        rlMain.postInvalidate();
        if (mParticleSystem != null) {
            mParticleSystem.cancel();
            mParticleSystem = null;
            startAnim();
        }
    }
}
