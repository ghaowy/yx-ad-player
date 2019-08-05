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
import android.widget.TextView;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.main.activation.ViewUtils;
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
    private LoopTimerTask mTimerTask;
    private TimerHandler mHandler;
    private static final int MSG_UPDATE_VIEW = 0X111;
    private Context mContext;
    private View mWindowView;
    private volatile boolean isAddWindow = false;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

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

    private void addOverLayWindow(Context context) {
        if (isAddWindow || PackageUtil.isGestureAppRunning(context)) {
            return;
        }
        YxLog.i(TAG, "addOverLayWindow --> isAddWindow" + isAddWindow);
        YxPermission.checkAndRequestPermission(Manifest.permission.SYSTEM_ALERT_WINDOW);
        initWindowView(context);
        getWindowManager(context).addView(mWindowView, getParams());
        isAddWindow = true;
    }

    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    private void initWindowView(Context context) {
        if (mWindowView == null) {
            YxLog.i(TAG ,"initWindowView --> ");
            mWindowView = getWindowView(context);
            mTvShowTime = mWindowView.findViewById(R.id.tv_show_time);
        }
    }

    private ViewGroup.LayoutParams getParams() {
        if (mParams == null) {
            int width = ViewUtils.dip2px(180);
            int height = ViewUtils.dip2px(180);
            int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            int type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            mParams = new WindowManager.LayoutParams(width, height, type, flags, PixelFormat.TRANSLUCENT);
            mParams.gravity = Gravity.END | Gravity.TOP;
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
        if (playTime <= TIME_LIMIT) {
            addOverLayWindow(mContext);
        } else {
            removeOverLayWindow(mContext);
        }
    }

    private void reset() {
        if (mHandler != null) {
            mHandler.removeMessages(MSG_UPDATE_VIEW);
        }
    }

    private void removeOverLayWindow(Context context) {
        if (mWindowView != null && isAddWindow) {
            YxLog.i(TAG, "removeOverLayWindow --> isAddWindow" + isAddWindow);
            getWindowManager(context).removeView(mWindowView);
            isAddWindow = false;
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

    private void updateTimeShow(Message msg) {
        if (msg.what == MSG_UPDATE_VIEW) {
            if (mPlayTime <= TIME_LIMIT && !isAddWindow) {
                addOverLayWindow(mContext);
            }
            if (mTvShowTime == null) {
                return;
            }
            if (mPlayTime <= 0) {
                mPlayTime = 0;
            }
            mTvShowTime.setText(String.valueOf(mPlayTime));
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
