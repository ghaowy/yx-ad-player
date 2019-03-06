package com.imprexion.aiscreen.base;

import android.app.Application;
import android.app.smdt.SmdtManager;
import android.content.Context;

import com.tencent.smtt.sdk.QbSdk;

public class AISApplication extends Application {


    private static AISApplication aisApplication;
    private static SmdtManager mSmdtManager;

    @Override
    public void onCreate() {
        super.onCreate();
        aisApplication = this;
        mSmdtManager = SmdtManager.create(this);
//        mSmdtManager.setMouseAutoHide(false);
//        mSmdtManager.setMousePosition(400,600);
//        mSmdtManager.setMouseSize(9.5f);
//        mSmdtManager.setMouseIcon(1);

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

    public static AISApplication getInstance(){
        return aisApplication;
    }

    public static SmdtManager getSmdtManager(){
        return mSmdtManager;
    }

}
