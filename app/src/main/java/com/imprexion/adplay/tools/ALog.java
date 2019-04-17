package com.imprexion.adplay.tools;

import android.util.Log;

public class ALog {
    private final static String TAG = "ADPlay_";

    public static void v(String Tag, String info) {
        Log.v(TAG + Tag, info);
    }

    public static void d(String Tag, String info) {
        Log.d(TAG + Tag, info);
    }

    public static void i(String Tag, String info) {
        Log.i(TAG + Tag, info);
    }

    public static void w(String Tag, String info) {
        Log.w(TAG + Tag, info);
    }

    public static void e(String Tag, String info) {
        Log.e(TAG + Tag, info);
    }

}
