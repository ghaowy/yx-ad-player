package com.imprexion.aiscreen.base;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

public class AISApplication extends Application {


    private static AISApplication aisApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        aisApplication = this;
        CrashReport.initCrashReport(getApplicationContext(), "8e26a11a85", false);
    }

    public static AISApplication getInstance() {
        return aisApplication;
    }
}
