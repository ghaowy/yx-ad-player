package com.imprexion.adplayer.widget;

import android.Manifest;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.utils.Util;
import com.imprexion.library.YxLog;
import com.imprexion.library.YxPermission;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

/**
 * @author : yan
 * @date : 2019/8/1 19:19
 * @desc : TODO
 */
public class AnimWindowControl {
    private static final String TAG = "AnimWindowControl";

    /**
     * 领券花花花包名
     */
    private static final String PACKAGE_NAME_COUPON = "com.imprexion.coupon";

    private Context mContext;
    private View mWindowView;
    private volatile boolean isAddWindow = false;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private SVGAImageView mSivButton;
    private SVGAImageView mSivRedEnvelopes;
    private SVGAImageView mSivMainAnimStart;
    private SVGAImageView mSivMainAnim;
    private ImageView mBtnClose;

    private SVGAParser mSVGAParser;

    private boolean isRepeatInit = false;
    private Runnable mPlayButtonAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            playButtonAnimation();
        }
    };

    private Runnable mPlayRedEnvelopestAnimationRunnable = new Runnable() {
        @Override
        public void run() {
            playRedEnvelopestAnimation();
        }
    };


    AnimWindowControl(Context context) {
        mContext = context;

    }

    private View getWindowView(Context context) {
        return View.inflate(context, R.layout.view_anim_window, null);
    }


    public void addOverLayWindow() {
        if (isAddWindow) {
            return;
        }
        YxLog.i(TAG, "addOverLayWindow --> isAddWindow" + isAddWindow);
        YxPermission.checkAndRequestPermission(Manifest.permission.SYSTEM_ALERT_WINDOW);
        initWindowView(mContext);
        getWindowManager(mContext).addView(mWindowView, getParams());
        isAddWindow = true;

        isRepeatInit = false;
        mSVGAParser = new SVGAParser(mContext);
        playStartAnimation();
        playRepeatAnimation();
        mSivButton.postDelayed(mPlayButtonAnimationRunnable, 8000);
        mSivRedEnvelopes.postDelayed(mPlayRedEnvelopestAnimationRunnable, 6000);
    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    private void initWindowView(Context context) {
        if (mWindowView == null) {
            YxLog.i(TAG, "initWindowView --> ");
            mWindowView = getWindowView(context);
            mSivMainAnimStart = mWindowView.findViewById(R.id.siv_main_anim_start);
            mSivMainAnim = mWindowView.findViewById(R.id.siv_main_anim);
            mSivButton = mWindowView.findViewById(R.id.siv_btn);
            mSivRedEnvelopes = mWindowView.findViewById(R.id.siv_red_envelopes);
            mBtnClose = mWindowView.findViewById(R.id.iv_close);

            mSivButton.setVisibility(View.GONE);
            mSivRedEnvelopes.setVisibility(View.GONE);
            mSivMainAnim.setVisibility(View.GONE);
            mBtnClose.setVisibility(View.GONE);

            mSivButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCouponApp();
                    removeOverLayWindow();
                }
            });

            mWindowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeOverLayWindow();
                }
            });
        }
    }

    private ViewGroup.LayoutParams getParams() {
        if (mParams == null) {
            int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            int type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            mParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, type, flags, PixelFormat.TRANSLUCENT);
            mParams.dimAmount = 0.7f;
            mParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
        }
        return mParams;
    }


    // 移除
    public void removeOverLayWindow() {
        if (mWindowView != null && isAddWindow) {
            YxLog.i(TAG, "removeOverLayWindow --> isAddWindow" + isAddWindow);
            getWindowManager(mContext).removeView(mWindowView);

            mSivButton.removeCallbacks(mPlayButtonAnimationRunnable);
            mSivRedEnvelopes.removeCallbacks(mPlayRedEnvelopestAnimationRunnable);

            mSivMainAnimStart.stopAnimation();
            mSivMainAnim.stopAnimation();
            mSivButton.stopAnimation();
            mSivRedEnvelopes.stopAnimation();
            mSivMainAnim = null;
            mSivMainAnimStart = null;
            mSivButton = null;
            mSivRedEnvelopes = null;
            mBtnClose = null;
            mSVGAParser = null;
            mWindowView = null;
            isAddWindow = false;
        }
    }

    public void release() {
        removeOverLayWindow();
        mContext = null;
        mWindowManager = null;

    }

    /**
     * 播放按钮动画
     */
    private void playButtonAnimation() {
        if (mSVGAParser == null) {
            return;
        }
        mSVGAParser.parse("anim_button.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {
                if (mSivButton == null) {
                    return;
                }
                mSivButton.setVisibility(View.VISIBLE);
                mBtnClose.setVisibility(View.VISIBLE);
                SVGADrawable drawable = new SVGADrawable(mSVGAVideoEntity);
                mSivButton.setImageDrawable(drawable);
                mSivButton.startAnimation();

                mSivButton.setAlpha(0f);
                mSivButton.animate()
                        .alpha(1f)
                        .setDuration(1000)
                        .setListener(null);

                mBtnClose.setAlpha(0f);
                mBtnClose.animate()
                        .alpha(1f)
                        .setDuration(1000)
                        .setListener(null);
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 播放红包动画
     */
    private void playRedEnvelopestAnimation() {
        if (mSVGAParser == null) {
            return;
        }
        mSVGAParser.parse("red_envelopes.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {
                if (mSivRedEnvelopes == null) {
                    return;
                }
                mSivRedEnvelopes.setVisibility(View.VISIBLE);
                SVGADrawable drawable = new SVGADrawable(mSVGAVideoEntity);
                mSivRedEnvelopes.setImageDrawable(drawable);
                mSivRedEnvelopes.startAnimation();
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 播放启动动画
     */
    private void playStartAnimation() {
        if (mSVGAParser == null) {
            return;
        }
        mSVGAParser.parse("anim_start.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {
                if (mSivMainAnimStart == null) {
                    return;
                }
                SVGADrawable drawable = new SVGADrawable(mSVGAVideoEntity);
                mSivMainAnim.setVisibility(View.GONE);
                mSivMainAnimStart.setVisibility(View.VISIBLE);
                mSivMainAnimStart.setImageDrawable(drawable);
                mSivMainAnimStart.startAnimation();
                mSivMainAnimStart.setCallback(new SVGACallback() {
                    @Override
                    public void onPause() {

                    }

                    @Override
                    public void onFinished() {
                        YxLog.i(TAG, "--- onFinished ---");

                    }

                    @Override
                    public void onRepeat() {
                        YxLog.i(TAG, "--- onRepeat ---");
                        mSivMainAnimStart.stopAnimation();
                        playRepeatAnimation();

                    }

                    @Override
                    public void onStep(int i, double v) {

                    }
                });
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 播放循环动画
     */
    private void playRepeatAnimation() {
        YxLog.i(TAG, "--- playRepeatAnimation ---");
        if (mSVGAParser == null) {
            return;
        }
        if (isRepeatInit == false) {
            mSVGAParser.parse("anim_repeat.svga", new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {
                    if (mSivMainAnim == null) {
                        return;
                    }
                    SVGADrawable drawable = new SVGADrawable(mSVGAVideoEntity);
                    mSivMainAnim.setImageDrawable(drawable);
                    isRepeatInit = true;
                }

                @Override
                public void onError() {

                }
            });
        } else {
            mSivMainAnim.startAnimation();
            mSivMainAnim.setVisibility(View.VISIBLE);
            mSivMainAnimStart.setVisibility(View.GONE);
        }
    }

    /**
     * 启动领券花花花应用
     */
    private void startCouponApp() {
        YxLog.i(TAG, "--- startCouponApp ---");
        Util.startApp(mContext, PACKAGE_NAME_COUPON, false);
        Util.sendBroadcastToAiBar(mContext, PACKAGE_NAME_COUPON, null);
    }


}
