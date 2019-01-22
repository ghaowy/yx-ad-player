package com.imprexion.aiscreen.ad;

import com.imprexion.aiscreen.bean.WeatherInfo;

public class AdContract {

    public interface AdView{
        void updateWeather(WeatherInfo weatherInfo);
    }
}
