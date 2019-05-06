package com.imprexion.adplayer.base;

import android.app.Application;
import android.os.Build;

import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.logger.YxLogger;
import com.tencent.bugly.crashreport.CrashReport;

public class ADPlayApplication extends Application {

    private final static String TAG = "ADPlayApplication";
    private static ADPlayApplication aisApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        aisApplication = this;
        CrashReport.initCrashReport(getApplicationContext(), "8e26a11a85", false);
        YxLogger.i(TAG, "Application onCreate: version name is " + Tools.getVersionName(this));
    }

    public static ADPlayApplication getInstance() {
        return aisApplication;
    }
}
