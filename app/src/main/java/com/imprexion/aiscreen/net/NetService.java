package com.imprexion.aiscreen.net;

import com.imprexion.aiscreen.bean.WeatherInfo;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetService {

    @GET("now")
    Observable<WeatherInfo> updateWeather(@Query("key") String key, @Query(encoded = true, value = "location") String location);
}
