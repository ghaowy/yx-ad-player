package com.imprexion.adplayer.main.activation;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.imprexion.library.YxLog;
import com.imprexion.library.util.ContextUtils;

public class FloatView {
    private static final String TAG = FloatView.class.getSimpleName();

    // 布局参数
    private WindowManager.LayoutParams mLayoutParams;
    // 窗口管理
    private WindowManager mWindowManager;
    // 窗口根布局
    private View layoutView;
    // 浮窗是否显示
    private boolean isShow = false;

    public FloatView() {

    }

    public WindowManager.LayoutParams getLayoutParams() {
        return mLayoutParams;
    }

    public View getLayoutView() {
        return layoutView;
    }

    public boolean isShow() {
        return isShow;
    }

    /**
     * 设置布局，默认基本参数设置
     * 设置window type
     * mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
     * mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
     * mLayoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
     * 如果设置为TYPE_PHONE; 那么优先级会降低一些,即拉下通知栏不可见
     *
     * @param layout
     */
    public View setFloatViewLayout(int layout) {

        mWindowManager = (WindowManager) ContextUtils.get().getSystemService(
                Context.WINDOW_SERVICE);
        mLayoutParams = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);


        LayoutInflater inflater = LayoutInflater.from(ContextUtils.get());
        layoutView = inflater.inflate(layout, null, false);
//        mLayoutParams.gravity = Gravity.NO_GRAVITY;
//        mLayoutParams.gravity = Gravity.BOTTOM;

        // 设置Window flag
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        return layoutView;

    }

    /**
     * 显示浮窗
     */
    public void showFloatView() {
        if (isShow == false) {
            mWindowManager.addView(layoutView, mLayoutParams);
            isShow = true;
        } else {
            YxLog.w(TAG, "showFloatView --- view is already show!");
        }
    }

    /**
     * 移除浮窗
     */
    public void dismissWindow() {
        if (isShow == true) {
            mWindowManager.removeView(layoutView);
            isShow = false;
        } else {
            YxLog.w(TAG, "dismissWindow --- view is already dismiss!");
        }
    }

    /**
     * 刷新浮窗
     */
    public void refreshView() {
        if (isShow == true) {
            mWindowManager.updateViewLayout(layoutView, mLayoutParams);
        } else {
            YxLog.w(TAG, "refreshView --- view is not show!");
        }
    }
}
