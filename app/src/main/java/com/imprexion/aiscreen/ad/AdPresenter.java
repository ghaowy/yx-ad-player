package com.imprexion.aiscreen.ad;

import com.imprexion.aiscreen.bean.WeatherInfo;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AdPresenter {
    private AdContract.AdView mAdView;
    private String key ="89e172514f5f4ba79cc2655153834a19";
    private String location = "%E6%B7%B1%E5%9C%B3";

    public AdPresenter(AdContract.AdView adView) {
        mAdView = adView;
    }

    public void updateWeather(){
        RetrofitFactory.getInstance()
                .create(AdService.class)
                .updateWeather(key,location)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherInfo>() {
                    @Override
                    public void accept(WeatherInfo weatherInfo) throws Exception {
                        mAdView.updateWeather(weatherInfo);
                    }
                });
    }

}
