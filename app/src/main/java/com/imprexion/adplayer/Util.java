package com.imprexion.adplayer;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import com.imprexion.library.YxLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.imprexion.library.util.ContextUtils.startService;

/**
 * Author: Xianquan Feng
 * Email: xianquan.feng@imprexion.cn
 * Date: 2019/4/16
 * Desc:
 */
public final class Util {

    public static final int STATUS_BAR_BLACK = 2;
    public static final int STATUS_BAR_WHITE = 1;

    private static final String TAG = "Util";

    public static int getWindowWidth(Context context) {
        // 获取屏幕分辨率宽度
        return getDisplayDetric(context).widthPixels;
    }

    public static int getWindowHeight(Context context) {
        // 获取屏幕分辨率高度
        return getDisplayDetric(context).heightPixels;
    }

    public static float getWindowDensity(Context context) {
        return getDisplayDetric(context).density;
    }

    public static int getWindowDensityDpi(Context context) {
        return getDisplayDetric(context).densityDpi;
    }

    private static DisplayMetrics getDisplayDetric(Context context) {
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static String formatDate(Date date, String format) {
        String str = null;
        if (date != null) {
            DateFormat dateFormat = new DateFormat();
            str = dateFormat.format(format, date).toString();
        }
        return str;
    }

    public static void startAdPlayerActivity(Context context, String lauchType) {
        String pkgName = "com.imprexion.adplayer";
        Bundle bd = new Bundle();
        bd.putString("launchType", lauchType);
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            intent.putExtras(bd);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context,
                    R.anim.right_in, R.anim.left_out);
            context.startActivity(intent, options.toBundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean startApp(Context context, String pkgName) {
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context,
                    R.anim.right_in, R.anim.left_out);
            context.startActivity(intent, options.toBundle());
            YxLog.i(TAG, "start app with packageName success");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            YxLog.e(TAG, "start app with packageName failed error= " + e.getCause());
            return false;
        }
    }

    public static void startApp(Context context, String pkgName, Map<String, String> mapExtras) {
//        Bundle bd = new Bundle();
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
//            intent.putExtras(bd);
            if (mapExtras != null) {
                for (String key : mapExtras.keySet()) {
                    intent.putExtra(key, mapExtras.get(key));
                }
            }
            ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context,
                    R.anim.right_in, R.anim.left_out);
            context.startActivity(intent, options.toBundle());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startAppWithActivity(Context context, String packageName, String clsName,
                                             Bundle bundle) {
        ComponentName componetName = new ComponentName(
                packageName, clsName);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //传递参数
        intent.putExtras(bundle);
        intent.setComponent(componetName);
        context.startActivity(intent);
    }

    private static void startAppWithActivityClass(Context context, Class cls,
                                             Bundle bundle) {
        Intent intent = new Intent(context,cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //传递参数
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(context,
                R.anim.right_in, R.anim.left_out);
        context.startActivity(intent, options.toBundle());
    }

    public static boolean isAppRunning(Context context, String packageName) {
        //通过ActivityManager我们可以获得系统里正在运行的activities
        //包括进程(Process)等、应用程序/包、服务(Service)、任务(Task)信息。
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();

        //获取本App的唯一标识

        //利用一个增强for循环取出手机里的所有进程
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            //通过比较进程的唯一标识和包名判断进程里是否存在该App
            if (info.processName.contains(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 修改系统状态栏的颜色。
     *
     * @param value 1，表示黑色，2表示白色。
     */
    public static void setStatusBarColor(int value) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.imprexion.service.window",
                "com.imprexion.service.window.WindowService"));
        intent.putExtra("status_bar_color", value);
        startService(intent);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    public static void showSystemBackPressed(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.imprexion.service.window",
                "com.imprexion.service.window.WindowService"));
        intent.putExtra("back_button_show", 1);
        context.startService(intent);
    }

    public static void hideSystemBackPressed(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.imprexion.service.window",
                "com.imprexion.service.window.WindowService"));
        intent.putExtra("back_button_show", 2);
        context.startService(intent);
    }

    public static void hideNavigationBarStatusBar(Activity activity, boolean hasFocus) {
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = activity.getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
