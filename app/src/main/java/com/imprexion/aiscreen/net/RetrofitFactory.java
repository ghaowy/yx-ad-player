package com.imprexion.aiscreen.net;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {

    private static Retrofit mRetrofit;
    private String baseurl = "https://free-api.heweather.com/s6/weather/";

    public RetrofitFactory() {
        mRetrofit = new Retrofit.Builder().
                baseUrl(baseurl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit getInstance(){
        if(mRetrofit == null){
            synchronized (RetrofitFactory.class){
                if (mRetrofit == null){
                    new RetrofitFactory();
                }
            }
        }
        return mRetrofit;
    }

}
