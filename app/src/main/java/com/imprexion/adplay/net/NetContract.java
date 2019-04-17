package com.imprexion.adplay.net;

import com.imprexion.adplay.bean.WeatherInfo;

public class NetContract {

    public interface StatusView {
        void updateWeather(WeatherInfo weatherInfo);
    }
}
