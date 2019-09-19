package com.imprexion.adplayer.player;

import android.Manifest;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.imprexion.adplayer.R;
import com.imprexion.adplayer.utils.AnimUtil;
import com.imprexion.library.YxLog;
import com.imprexion.library.YxPermission;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author : yan
 * @date : 2019/8/1 19:19
 * @desc : TODO
 */
public class WindowControl {
    private static final String TAG = "WindowControl";

    private static final int TIME_LIMIT = 10;
    private TextView mTvShowTime;
    private Timer mTimer;
    private int mPlayTime;
    private boolean mIsUserUse;
    private LoopTimerTask mTimerTask;
    private TimerHandler mHandler;
    private static final int MSG_GONE_TEXT_VIEW = 0x222;
    private static final int MSG_UPDATE_VIEW = 0X111;
    private Context mContext;
    private View mWindowView;
    private volatile boolean isAddWindow = false;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;
    private LottieAnimationView mRabbitAnimView;
    private RelativeLayout mRlContainer;
    private LottieAnimationView mCircleAnimView;
    private TextView mTvShowNum;
    private StringBuilder mBuilder;

    WindowControl(Context context) {
        mContext = context;
        initTimer();
        initHandler();
    }

    private void initHandler() {
        mHandler = new TimerHandler(this, Looper.getMainLooper());
    }

    private void initTimer() {
        mTimer = new Timer();
    }

    private View getWindowView(Context context) {
        return View.inflate(context, R.layout.view_overlay_window, null);
    }


    public void setUserUse(boolean userUse) {
        mIsUserUse = userUse;
    }


    private void addOverLayWindow(Context context) {
        if (isAddWindow || PackageUtil.isGestureAppRunning(context)) {
            return;
        }
        YxLog.i(TAG, "addOverLayWindow --> isAddWindow" + isAddWindow);
        YxPermission.checkAndRequestPermission(Manifest.permission.SYSTEM_ALERT_WINDOW);
        initWindowView(context);
        getWindowManager(context).addView(mWindowView, getParams());
        isAddWindow = true;
        if (mRabbitAnimView != null) {
            mRabbitAnimView.playAnimation();
        }
        if (mCircleAnimView != null) {
            mCircleAnimView.playAnimation();
        }
        AnimUtil.playObjectAnim(mRlContainer, "translationY", 0, -10, 1000, true, null);
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
            mTvShowTime = mWindowView.findViewById(R.id.tv_show_time);
            mRabbitAnimView = mWindowView.findViewById(R.id.iv_rabbit_anim);
            mRlContainer = mWindowView.findViewById(R.id.ll_container);
            mCircleAnimView = mWindowView.findViewById(R.id.anim_circle_lottie);
            mTvShowNum = mWindowView.findViewById(R.id.tv_show_num);
            playLottieAnim("rabbit_anim", "rabbit_anim.json", mRabbitAnimView);
            playLottieAnim("circle", "circle_anim.json", mCircleAnimView);
        }

        mTvShowNum.setVisibility(View.VISIBLE);
        mCircleAnimView.setVisibility(View.VISIBLE);
    }
//
//    private void playRootAlphaAnim(boolean isAdd) {
//        mIsAdd = isAdd;
//        float start = 0.0f;
//        float end = 0.0f;
//        if (isAdd) {
//            start = 1.0f;
//            end = 0.4f;
//        } else {
//            start = 0.4f;
//            end = 1.0f;
//        }
//        AnimUtil.playObjectAnim(mRootView, "alpha", start, end, 1000, false, this);
//    }

    private void playLottieAnim(String imgFolder, String animJson, LottieAnimationView view) {
        view.setImageAssetsFolder(imgFolder);
        view.setAnimation(animJson);
        view.loop(true);
    }

    private ViewGroup.LayoutParams getParams() {
        if (mParams == null) {
            int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            int type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            mParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, type, flags, PixelFormat.TRANSLUCENT);
            mParams.dimAmount = 0.7f;
            mParams.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            mParams.windowAnimations = R.style.window_anim;
        }
        return mParams;
    }

    void setPlayTime(int playTime) {
        mPlayTime = playTime;
        reset();
        if (mTimerTask == null) {
            mTimerTask = new LoopTimerTask();
            mTimer.schedule(mTimerTask, 0, 1000);
        }
        YxLog.i(TAG ,"setPlayTime--> mIsUserUse" + mIsUserUse);
        if (playTime <= TIME_LIMIT && mIsUserUse) {
            addOverLayWindow(mContext);
//            playRootAlphaAnim(true);
        } else {
            removeOverLayWindow(mContext);
        }
    }

    private void reset() {
        if (mHandler != null) {
            mHandler.removeMessages(MSG_UPDATE_VIEW);
        }
    }

    public void removeOverLayWindow(Context context) {
        if (mWindowView != null && isAddWindow) {
            YxLog.i(TAG, "removeOverLayWindow --> isAddWindow" + isAddWindow);
            getWindowManager(context).removeView(mWindowView);
            if (mRabbitAnimView != null) {
                mRabbitAnimView.cancelAnimation();
            }
            isAddWindow = false;
            AnimUtil.clearAnim(mRlContainer);
        }
    }

    class LoopTimerTask extends TimerTask {

        @Override
        public void run() {
            if (mHandler != null) {
                mHandler.sendEmptyMessage(MSG_UPDATE_VIEW);
            }
            mPlayTime--;
        }
    }

    static class TimerHandler extends Handler {

        private WeakReference<WindowControl> mWeakReference;

        TimerHandler(WindowControl control, Looper looper) {
            super(looper);
            mWeakReference = new WeakReference<>(control);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWeakReference == null || mWeakReference.get() == null) {
                return;
            }
            WindowControl control = mWeakReference.get();
            control.updateTimeShow(msg);
        }
    }

    public StringBuilder getStringBuilder() {
        if (mBuilder == null) {
            mBuilder = new StringBuilder();
        }
        mBuilder.setLength(0);
        return mBuilder;
    }

    private void updateTimeShow(Message msg) {
        if (msg.what == MSG_UPDATE_VIEW) {
            if (mPlayTime <= TIME_LIMIT && !isAddWindow && mIsUserUse) {
//                playRootAlphaAnim(true);
                addOverLayWindow(mContext);
            }
            if (mTvShowTime == null) {
                return;
            }
            if (mPlayTime <= 0) {
                mPlayTime = 0;
            }

            if (mTvShowNum != null) {
                mTvShowNum.setVisibility(View.VISIBLE);
                if (mPlayTime >= 10) {
                    mTvShowNum.setText(String.valueOf(mPlayTime));
                } else {
                    mTvShowNum.setText(getStringBuilder().append(0).append(mPlayTime).toString());
                }
            }
            mTvShowTime.setVisibility(View.VISIBLE);
            mTvShowTime.setText(String.valueOf(mPlayTime));

            if (mHandler != null) {
                mHandler.removeMessages(MSG_GONE_TEXT_VIEW);
                mHandler.sendEmptyMessageDelayed(MSG_GONE_TEXT_VIEW, 700);
            }
        } else if (msg.what == MSG_GONE_TEXT_VIEW) {
            if (mTvShowTime == null || mTvShowNum == null) {
                return;
            }
            mTvShowTime.setVisibility(View.INVISIBLE);
            mTvShowNum.setVisibility(View.INVISIBLE);
        }

        if (mPlayTime == 0) {
            mTvShowNum.setVisibility(View.INVISIBLE);
            mCircleAnimView.setVisibility(View.INVISIBLE);
        }
    }

    public void release() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimerTask = null;
            mTimer = null;
        }

        if (mHandler != null) {
            mHandler.removeMessages(MSG_UPDATE_VIEW);
            mHandler = null;
        }

        removeOverLayWindow(mContext);
        mContext = null;
        mWindowManager = null;
    }
}
