package com.imprexion.aiscreen.base;

import android.app.Application;
import android.content.Context;

public class AISApplication extends Application {


    private static AISApplication aisApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        aisApplication = this;
    }

    public static AISApplication getInstance(){
        return aisApplication;
    }

}
