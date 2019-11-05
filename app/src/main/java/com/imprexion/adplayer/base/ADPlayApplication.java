package com.imprexion.adplayer.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.danikula.videocache.HttpProxyCacheServer;
import com.imprexion.adplayer.BuildConfig;
import com.imprexion.adplayer.service.AdPlayService;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.YxLog;
import com.imprexion.library.base.BaseApplication;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

public class ADPlayApplication extends BaseApplication {

    private final static String TAG = "ADPlayApplication";
    private static ADPlayApplication adPlayApplication;
    private HttpProxyCacheServer proxy;

    private List<Activity> activities = new ArrayList<>();

    public Activity pictureActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        adPlayApplication = this;
        YxLog.i(TAG, "Application onCreate: version name is " + Tools.getVersionName(this));
        startADService();
        if (!BuildConfig.DEBUG) {
            CrashReport.initCrashReport(getApplicationContext(), "4e152a32a2", true);
        }
    }


    public static HttpProxyCacheServer getProxy(Context context) {
        return adPlayApplication.proxy == null ? (adPlayApplication.proxy = adPlayApplication.newProxy()) : adPlayApplication.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer(this);
    }

    public static ADPlayApplication getInstance() {
        return adPlayApplication;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void finishActivity(Activity activity) {
        for (int i = 0; i < activities.size(); i++) {
            if (activity != null && activity.equals(activities.get(0))) {
                activity.finish();
            }
        }
    }

    private void startADService() {
        Intent it = new Intent(this, AdPlayService.class);
        startService(it);
    }
}
