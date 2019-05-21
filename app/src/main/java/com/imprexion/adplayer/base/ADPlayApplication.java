package com.imprexion.adplayer.base;

import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.YxLog;
import com.imprexion.library.base.BaseApplication;
import com.tencent.bugly.crashreport.CrashReport;

public class ADPlayApplication extends BaseApplication {

    private final static String TAG = "ADPlayApplication";
    private static ADPlayApplication adPlayApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        adPlayApplication = this;
        CrashReport.initCrashReport(getApplicationContext(), "8e26a11a85", false);
        YxLog.i(TAG, "Application onCreate: version name is " + Tools.getVersionName(this));
    }

    public static ADPlayApplication getInstance() {
        return adPlayApplication;
    }
}
