package com.imprexion.adplayer.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static Retrofit mRetrofit;
    private static Retrofit mRetrofitAD;
    private static final String baseurl = "https://free-api.heweather.com/s6/weather/";
    private static final String baseurlAdCallback = "http://pro.imprexion.cn/android/gateway/adcarousel/";//official
//    private static final String baseurlAdCallback = "http://debug.imprexion.cn/android/gateway/adcarousel/";//debug
//    private static final String baseurlAdCallback = "http://test.imprexion.cn/android/gateway/adcarousel/";//test

    public RetrofitFactory() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit getInstance() {
        if (mRetrofit == null) {
            synchronized (RetrofitFactory.class) {
                if (mRetrofit == null) {
                    new RetrofitFactory();
                }
            }
        }
        return mRetrofit;
    }

    public static Retrofit getInstanceForAD() {
        if (mRetrofitAD == null) {
            synchronized (RetrofitFactory.class) {
                if (mRetrofitAD == null) {
                    mRetrofitAD = new Retrofit.Builder()
                            .baseUrl(baseurlAdCallback)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return mRetrofitAD;
    }

}
