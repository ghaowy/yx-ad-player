package com.imprexion.adplayer.player;

import android.content.Context;

import com.imprexion.adplayer.utils.Util;
import com.imprexion.library.YxLog;

/**
 * @author : yan
 * @date : 2019/8/2 16:01
 * @desc : TODO
 */
public class PackageUtil {
    /**
     * 当前只有一款体感应用。
     */
    private static String[] gestureAppPackageNames = new String[]{"com.Orbbec.MagicSalad2", "com.imprexion.aibar"};
    private static final String PACKAGE_SCREEN_DONIMATE = "com.imprexion.screendominate";
    private static final String PACKAGE_AI_SMILE = "com.imprexion.aismile";
    public static final String TAG = "PackageUtil";


    public static boolean isGestureAppRunning(Context context) {
        for (int i = 0; i < gestureAppPackageNames.length; i++) {
            if (Util.isAppOnForeground(context, gestureAppPackageNames[i])) {
                YxLog.i(TAG, "isGestureAppRunning--> true packageName = " + gestureAppPackageNames[i]);
                return true;
            }
        }
        return false;
    }

    public static boolean isScreenApp(Context context) {
        return Util.isAppOnForeground(context, PACKAGE_SCREEN_DONIMATE) || Util.isAppOnForeground(context, PACKAGE_AI_SMILE);
    }
}
