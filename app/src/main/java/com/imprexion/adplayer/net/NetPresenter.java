package com.imprexion.adplayer.net;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.library.YxLog;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetPresenter {
    private static final String TAG = "NetPresenter";

    public NetPresenter() {
    }

    public void onADCallback(ADContentPlay adContent) {
        Log.d(TAG, "onADCallback");
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(adContent));
        RetrofitFactory.getInstanceForAD()
                .create(NetService.class)
                .onAdcontentCallback(requestBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        YxLog.d(TAG, "adcallback=" + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        YxLog.d(TAG, "onFailure  " + t.getMessage());
                    }
                });
    }

}
