package com.imprexion.aiscreen.main.rainControl;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.imprexion.aiscreen.tools.ALog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import com.imprexion.aiscreen.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RainControlFragment extends Fragment {


    @BindView(R.id.rainView_1)
    RainView rainView1;
    @BindView(R.id.rainView_2)
    RainView rainView2;
    Unbinder unbinder;
    private final static int mDuration = 1600;
    @BindView(R.id.rainView_3)
    RainView rainView3;
    @BindView(R.id.rainView_4)
    RainView rainView4;
    @BindView(R.id.fl_rainControl)
    FrameLayout flRainControl;
    @BindView(R.id.rainDropView)
    RaindropView rainDropView;
    @BindView(R.id.rainView_5)
    RainView rainView5;
    @BindView(R.id.rainView_6)
    RainView rainView6;
    private int lastX;
    private int lastY;
    private final static String TAG = "RainControlFragment";
    private boolean isStop;
    private RainVoice mRainVoice;
    private ObjectAnimator mObjectAnimator1;
    private ObjectAnimator mObjectAnimator2;
    private ObjectAnimator mObjectAnimator3;
    private ObjectAnimator mObjectAnimator4;
    private ObjectAnimator mObjectAnimator5;
    private ObjectAnimator mObjectAnimator6;


    public RainControlFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rain_control, container, false);
        unbinder = ButterKnife.bind(this, view);
        mRainVoice = new RainVoice(getContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startRain();
        setControl();
        mRainVoice.playRainVoiceBySoundpool();
        if (mObjectAnimator1 != null) {
            mObjectAnimator1.resume();
            mObjectAnimator2.resume();
            mObjectAnimator3.resume();
            mObjectAnimator4.resume();
            mObjectAnimator5.resume();
            mObjectAnimator6.resume();
        }
    }

    private void setControl() {
        flRainControl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = x;
                        lastY = y;
                        break;
                    case MotionEvent.ACTION_UP:
                        if (x - lastX > 120) {
                            //右移
                            ALog.d(TAG, "右移");
                            if (isStop) {
                                rainDropView.remove(false, true, false, false);
                            }
                        } else if (lastX - x > 120) {
                            //左移
                            ALog.d(TAG, "左移");
                            if (isStop) {
                                rainDropView.remove(true, false, false, false);
                            }
                        } else if (y - lastY > 120) {
                            //下移
                            if (isStop) {
                                rainDropView.remove(false, false, false, true);

                            }
                        } else if (lastY - y > 120) {
                            //上移
                            if (isStop) {
                                rainDropView.remove(false, false, true, false);

                            }
                        } else {
                            //点击
                            ALog.d(TAG, "点击");
                            if (!isStop) {
                                mRainVoice.playRainDropVoice();
                                rainView1.setVisibility(View.INVISIBLE);
                                rainView2.setVisibility(View.INVISIBLE);
                                rainView3.setVisibility(View.INVISIBLE);
                                rainView4.setVisibility(View.INVISIBLE);
                                rainView5.setVisibility(View.INVISIBLE);
                                rainView6.setVisibility(View.INVISIBLE);
                                rainDropView.setVisibility(View.VISIBLE);
                                isStop = true;
                            } else {
                                mRainVoice.playRainVoiceBySoundpool();
                                rainView1.setVisibility(View.VISIBLE);
                                rainView2.setVisibility(View.VISIBLE);
                                rainView3.setVisibility(View.VISIBLE);
                                rainView4.setVisibility(View.VISIBLE);
                                rainView5.setVisibility(View.VISIBLE);
                                rainView6.setVisibility(View.VISIBLE);
                                rainDropView.invalide();
                                rainDropView.setVisibility(View.INVISIBLE);
                                isStop = false;
                            }

                        }
                        break;
                }
                return true;//返回true表示处理事件
            }
        });
    }

    private void startRain() {
        rainDropView.setVisibility(View.INVISIBLE);
        rainView1.post(new Runnable() {
            @Override
            public void run() {
                rainView1.setVisibility(View.INVISIBLE);
                float height = rainView1.getHeight();
                rainView1.setScale(true);
                mObjectAnimator1 = ObjectAnimator.ofFloat(rainView1, "translationY", -height, height);
                mObjectAnimator1.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimator1.setDuration(mDuration + 300);
                mObjectAnimator1.setInterpolator(new LinearInterpolator());
                mObjectAnimator1.setStartDelay(100);
                mObjectAnimator1.start();
                mObjectAnimator1.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        rainView1.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        rainView1.initData();
                    }
                });
            }
        });
        rainView2.post(new Runnable() {
            @Override
            public void run() {
                rainView2.setVisibility(View.INVISIBLE);
                float height = rainView2.getHeight();
                rainView2.setBitmap(getScaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rain_2), 2.2f, 1.8f));
                rainView2.setRainSize(6);
                mObjectAnimator2 = ObjectAnimator.ofFloat(rainView2, "translationY", -height, height);
                mObjectAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimator2.setDuration(mDuration - 900);
                mObjectAnimator2.setStartDelay(200);
                mObjectAnimator2.setInterpolator(new LinearInterpolator());
                mObjectAnimator2.start();
                mObjectAnimator2.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        rainView2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        rainView2.initData();
                    }
                });
            }
        });
        rainView3.post(new Runnable() {
            @Override
            public void run() {
                rainView3.setVisibility(View.INVISIBLE);
                float height = rainView3.getHeight();
                rainView3.setBitmap(getScaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rain_1), 1f, 1.3f));
                rainView3.setRainSize(30);
                mObjectAnimator3 = ObjectAnimator.ofFloat(rainView3, "translationY", -height, height);
                mObjectAnimator3.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimator3.setDuration(mDuration - 500);
                mObjectAnimator3.setStartDelay(400);
                mObjectAnimator3.setInterpolator(new LinearInterpolator());
                mObjectAnimator3.start();
                mObjectAnimator3.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        rainView3.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        rainView3.initData();
                    }
                });
            }
        });

        rainView4.post(new Runnable() {
            @Override
            public void run() {
                rainView4.setVisibility(View.INVISIBLE);
                float height = rainView4.getHeight();
                rainView4.setScale(true);
                mObjectAnimator4 = ObjectAnimator.ofFloat(rainView4, "translationY", -height, height);
                mObjectAnimator4.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimator4.setDuration(mDuration + 300);
                mObjectAnimator4.setStartDelay(mDuration / 2);
                mObjectAnimator4.setInterpolator(new LinearInterpolator());
                mObjectAnimator4.start();
                mObjectAnimator4.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        rainView4.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        rainView4.initData();
                    }
                });
            }
        });
        rainView5.post(new Runnable() {
            @Override
            public void run() {
                rainView5.setVisibility(View.INVISIBLE);
                rainView5.setBitmap(getScaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rain_2), 2.2f, 1.8f));
                rainView5.setRainSize(6);
                float height = rainView4.getHeight();
                mObjectAnimator5 = ObjectAnimator.ofFloat(rainView5, "translationY", -height, height);
                mObjectAnimator5.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimator5.setDuration(mDuration - 900);
                mObjectAnimator5.setStartDelay(mDuration / 2 + 200);
                mObjectAnimator5.setInterpolator(new LinearInterpolator());
                mObjectAnimator5.start();
                mObjectAnimator5.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        rainView5.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        rainView5.initData();
                    }
                });
            }
        });
        rainView6.post(new Runnable() {
            @Override
            public void run() {
                rainView6.setVisibility(View.INVISIBLE);
                float height = rainView6.getHeight();
                rainView6.setBitmap(getScaleBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.rain_1), 1f, 1.3f));
                rainView6.setRainSize(30);
                mObjectAnimator6 = ObjectAnimator.ofFloat(rainView6, "translationY", -height, height);
                mObjectAnimator6.setRepeatCount(ObjectAnimator.INFINITE);
                mObjectAnimator6.setDuration(mDuration - 500);
                mObjectAnimator6.setStartDelay(mDuration / 2 + 400);
                mObjectAnimator6.setInterpolator(new LinearInterpolator());
                mObjectAnimator6.start();
                mObjectAnimator6.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        rainView6.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        super.onAnimationRepeat(animation);
                        rainView6.initData();
                    }
                });
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mObjectAnimator1.cancel();
        mObjectAnimator2.cancel();
        mObjectAnimator3.cancel();
        mObjectAnimator4.cancel();
        mObjectAnimator5.cancel();
        mObjectAnimator6.cancel();
        mRainVoice.pause();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        mRainVoice.stop();
    }

    private Bitmap getScaleBitmap(Bitmap bitmap, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
