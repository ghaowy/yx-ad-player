package com.imprexion.aiscreen.net;

import com.imprexion.aiscreen.bean.WeatherInfo;

public class NetContract {

    public interface StatusView {
        void updateWeather(WeatherInfo weatherInfo);
    }
}
