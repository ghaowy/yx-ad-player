package com.imprexion.aiscreen.tools;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.imprexion.aiscreen.base.AISApplication;

public class ScreenUtils {

    /**
     * 获取手机屏幕高度
     */
    public static int getHeight() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) AISApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕真实高度（包括虚拟键盘）
     *
     */
    public static int getRealHeight() {
        WindowManager windowManager = (WindowManager) AISApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        return realHeight;
    }

    public interface NavigationListener {
        void show();
        void hide();
    }

    //虚拟导航栏显示/隐藏
    public static void setNavigationListener(final View rootView, final NavigationListener navigationListener){
        if (rootView == null || navigationListener == null) {
            return;
        }
        if (getRealHeight() != getHeight()) {
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                int rootViewHeight;
                @Override
                public void onGlobalLayout() {
                    int viewHeight = rootView.getHeight();
                    if (rootViewHeight != viewHeight) {
                        rootViewHeight = viewHeight;
                        if (viewHeight == getRealHeight()) {
                            //隐藏虚拟按键
                            if (navigationListener != null) {
                                navigationListener.hide();
                            }
                        } else {
                            //显示虚拟按键
                            if (navigationListener != null) {
                                navigationListener.show();
                            }
                        }
                    }
                }
            });
        }
    }
}

