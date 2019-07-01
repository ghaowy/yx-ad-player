package com.imprexion.adplayer.net.http;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Fandy on 2019/03/26.
 */

public class PublicParams {

    //调试环境
//    public static final String SERVER_BASE_URL = "http://debug.imprexion.cn";
//    public static String token = "30EFKHab7C5C56K9";
//    public static final String ENV = "debug";
    //测试环境
//    public static final String SERVER_BASE_URL = "http://test.imprexion.cn";
//    public static String token = "30EFKHab7C5C56K9";
//    public static final String ENV = "test";
//    //正式环境
    public static final String SERVER_BASE_URL = "http://pro.imprexion.cn";
    public static String token = "30EFKHab7C5C56K8";
    public static final String ENV = "pro";
    public static String sysVersion = getSysVersion();
    public static String deviceId = Build.SERIAL;

//    //金沙印象城地图的包名
//    public static String fengMapPackageName = "com.fengmap.yingxiang";
    //济南印象城地图的包名
//    public static String fengMapPackageName = "com.fengmap.jinanyinxiang";
    //印力中心地图包名
    public static String fengMapPackageName = "com.imprexion.fengmap";

    /**
     * 缓存一个当前本地人脸识别的userId.如果重复是同一个人，就不再请求数据，避免过于频繁
     */
    public static long userId = -1;
    public static long localUserId = -1;

    public static void reset() {
        token = "";
    }

    public static Map<String, String> getHeaders() {
        ConcurrentHashMap<String, String> headers = new ConcurrentHashMap<>();
        headers.put("token", token);
        headers.put("deviceId",deviceId);
        return headers;
    }

    public static String getSysVersion() {
        String sysVer = "Product Model: " + Build.MODEL + ","
                + Build.VERSION.SDK + ","
                + Build.VERSION.RELEASE;
        return sysVer;
    }

    public static String getMetaDataFromManifest(Context context, String key) {
        String value = null;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),
                    PackageManager.GET_META_DATA);
            value = (String) appInfo.metaData.get(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getVersionName(Context context) {
        String verName = null;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            verName = packInfo.versionName;
            return verName;
        } catch (Exception e) {
            return verName;
        }
    }

    public static int getVersionCode(Context context) {
        int verCode = 0;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            verCode = packInfo.versionCode;
            return verCode;
        } catch (Exception e) {
            return verCode;
        }
    }

    public static String getPackageName(Context context) {
        String packageName = "";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            packageName = packInfo.packageName;
            return packageName;
        } catch (Exception e) {
            return packageName;
        }
    }
}
