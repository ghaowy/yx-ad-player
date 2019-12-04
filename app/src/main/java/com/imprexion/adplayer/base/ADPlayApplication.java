package com.imprexion.adplayer.base;

import android.content.Intent;

import com.imprexion.adplayer.BuildConfig;
import com.imprexion.adplayer.service.AdPlayService;
import com.imprexion.adplayer.utils.ActivityStackUtil;
import com.imprexion.library.base.BaseApplication;
import com.tencent.bugly.crashreport.CrashReport;

public class ADPlayApplication extends BaseApplication {
    private final static String TAG = "ADPlayApplication";
    private static ADPlayApplication adPlayApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        adPlayApplication = this;
        startADService();
        ActivityStackUtil.Holder.instance.init(this);
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "4e152a32a2", true);
        }
    }

    public static ADPlayApplication getInstance() {
        return adPlayApplication;
    }

    private void startADService() {
        Intent it = new Intent(this, AdPlayService.class);
        startService(it);
    }
}
