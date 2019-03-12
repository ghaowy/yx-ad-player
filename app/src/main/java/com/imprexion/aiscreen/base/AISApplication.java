package com.imprexion.aiscreen.base;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

public class AISApplication extends Application {


    private static AISApplication aisApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        aisApplication = this;
        CrashReport.initCrashReport(getApplicationContext(), "8e26a11a85", false);
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
            }
            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);

    }

    public static AISApplication getInstance() {
        return aisApplication;
    }
}
