package com.imprexion.adplayer.net;

import com.imprexion.adplayer.bean.WeatherInfo;

public class NetContract {

    public interface StatusView {
        void updateWeather(WeatherInfo weatherInfo);
    }
}
