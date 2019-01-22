package com.imprexion.aiscreen.base;

import android.app.Application;
import android.app.smdt.SmdtManager;
import android.content.Context;

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
    }

    public static AISApplication getInstance(){
        return aisApplication;
    }

    public static SmdtManager getSmdtManager(){
        return mSmdtManager;
    }

}
