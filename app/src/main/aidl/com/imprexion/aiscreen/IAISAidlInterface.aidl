// IAISAidlInterface.aidl
package com.imprexion.aiscreen;

import com.imprexion.aiscreen.ContentInfo;

// Declare any non-default types here with import statements

interface IAISAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
//    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
//            double aDouble, String aString);

    //接收轮播广告和霸屏消息ContentInfo
    void receiveContentInfoByServer(in ContentInfo contentInfo);
}
