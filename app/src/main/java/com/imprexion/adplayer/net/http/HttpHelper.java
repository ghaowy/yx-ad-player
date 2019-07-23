package com.imprexion.adplayer.net.http;


import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Fandy on 2017/11/30.
 */

public class HttpHelper {

    Retrofit retrofit;
    String baseUrl;


    public HttpHelper(String baseUrl) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public HttpHelper(String baseUrl, Map<String, String> headers) {
        this.baseUrl = baseUrl;
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(genericClient(headers))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public void setHeader(Map<String, String> headers) {
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(genericClient(headers))
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    public <T> T create(Class<T> tClass){
        return retrofit.create(tClass);
    }

    private static OkHttpClient genericClient(final Map<String, String> headers) {

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder request = chain.request()
                                .newBuilder()
                                .addHeader("Content-Type", "application/json; charset=UTF-8");
                        //添加请求头
                        if (headers != null) {
                            for (String key : headers.keySet()) {
                                request.addHeader(key, headers.get(key).toString());
                            }
                        }
                        return chain.proceed(request.build());
                    }
                })
                .build();
        return httpClient;
    }

    public interface IRequest {

    }
}
