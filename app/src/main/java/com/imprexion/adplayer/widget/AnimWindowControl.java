package com.imprexion.adplayer.widget;

import android.Manifest;
import android.content.Context;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.renderscript.RenderScript;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.google.android.exoplayer2.util.UriUtil;
import com.imprexion.adplayer.R;
import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.player.PackageUtil;
import com.imprexion.adplayer.utils.AnimUtil;
import com.imprexion.adplayer.utils.Util;
import com.imprexion.library.YxLog;
import com.imprexion.library.YxPermission;
import com.imprexion.library.util.SharedPreferenceUtils;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

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
    private SVGAImageView mSivMainAnim;

    private SVGAParser mSVGAParser;
    AnimWindowControl(Context context) {
        mContext = context;
        mSVGAParser = new SVGAParser(mContext);
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

        playStartAnimation();
        mSivButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                playButtonAnimation();
            }
        }, 8000);
        mSivRedEnvelopes.postDelayed(new Runnable() {
            @Override
            public void run() {
                playRedEnvelopestAnimation();
            }
        }, 6000);
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
            mSivMainAnim = mWindowView.findViewById(R.id.siv_main_anim);
            mSivButton = mWindowView.findViewById(R.id.siv_btn);
            mSivRedEnvelopes = mWindowView.findViewById(R.id.siv_red_envelopes);

            mSivButton.setVisibility(View.GONE);
            mSivRedEnvelopes.setVisibility(View.GONE);

            mSivButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCouponApp();
                }
            });
        }
    }

    private ViewGroup.LayoutParams getParams() {
        if (mParams == null) {
            int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            int type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            mParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, type, flags, PixelFormat.TRANSLUCENT);
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
        mSVGAParser.parse("anim_button.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {
                mSivButton.setVisibility(View.VISIBLE);
                SVGADrawable drawable = new SVGADrawable(mSVGAVideoEntity);
                mSivButton.setImageDrawable(drawable);
                mSivButton.startAnimation();

                mSivButton.setAlpha(0f);
                mSivButton.animate()
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
        mSVGAParser.parse("red_envelopes.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {
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
        mSVGAParser.parse("anim_start.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {
                SVGADrawable drawable = new SVGADrawable(mSVGAVideoEntity);
                mSivMainAnim.setImageDrawable(drawable);
                mSivMainAnim.startAnimation();
                mSivMainAnim.setCallback(new SVGACallback() {
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
                        mSivMainAnim.stopAnimation();
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
        mSVGAParser.parse("anim_repeat.svga", new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete(SVGAVideoEntity mSVGAVideoEntity) {
                SVGADrawable drawable = new SVGADrawable(mSVGAVideoEntity);
                mSivMainAnim.setImageDrawable(drawable);
                mSivMainAnim.startAnimation();
                mSivMainAnim.setCallback(null);
            }

            @Override
            public void onError() {

            }
        });
    }

    /**
     * 启动领券花花花应用
     */
    private void startCouponApp() {
        YxLog.i(TAG, "--- startCouponApp ---");
        Util.startApp(mContext, PACKAGE_NAME_COUPON);
        Util.sendBroadcastToAiBar(mContext, PACKAGE_NAME_COUPON, null);
    }


}
