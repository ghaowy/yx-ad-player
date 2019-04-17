package com.imprexion.adplay.base;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

public class ADPlayApplication extends Application {


    private static ADPlayApplication aisApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        aisApplication = this;
        CrashReport.initCrashReport(getApplicationContext(), "8e26a11a85", false);
    }

    public static ADPlayApplication getInstance() {
        return aisApplication;
    }
}
