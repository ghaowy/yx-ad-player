package com.imprexion.adplayer.net;

import com.alibaba.fastjson.JSON;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.bean.WeatherInfo;
import com.imprexion.adplayer.tools.ALog;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetPresenter {
    private NetContract.StatusView mAdView;
    private String key = "89e172514f5f4ba79cc2655153834a19";
    private String location = "%E6%B7%B1%E5%9C%B3";
    private static final String TAG = "NetPresenter";

    public NetPresenter(NetContract.StatusView adView) {
        mAdView = adView;
    }

    public NetPresenter() {
    }

    public void updateWeather(String city) {
        RetrofitFactory.getInstance()
                .create(NetService.class)
                .updateWeather(key, city)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherInfo>() {
                    @Override
                    public void accept(WeatherInfo weatherInfo) throws Exception {
                        mAdView.updateWeather(weatherInfo);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public void onADCallback(ADContentPlay adContent) {
//        JSONObject jsonObject = new JSONObject();
//        jsonObject.put("token", "30EFKHab7C5C56K9");
//        jsonObject.put("adcontent", adContent);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), JSON.toJSONString(adContent));

        RetrofitFactory.getInstanceForAD()
                .create(NetService.class)
                .onAdcontentCallback(requestBody)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        ALog.d(TAG, "adcallback=" + response.body());
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {

                    }
                });
    }

}
