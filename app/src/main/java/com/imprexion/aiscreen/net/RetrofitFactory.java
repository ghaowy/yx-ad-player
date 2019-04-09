package com.imprexion.aiscreen.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static Retrofit mRetrofit;
    private static Retrofit mRetrofitAD;
    private static final String baseurl = "https://free-api.heweather.com/s6/weather/";
//    private static final String baseurlAdCallback = "http:/10.2.26.163:9010/android/gateway/adcarousel/";//local
    private static final String baseurlAdCallback = "http://120.77.177.30:9010/android/gateway/adcarousel/";//dev

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
