package com.imprexion.aiscreen.ad;

import com.imprexion.aiscreen.bean.WeatherInfo;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AdService {

    @GET("now")
    Observable<WeatherInfo> updateWeather(@Query("key") String key, @Query(encoded = true, value = "location") String location);
}
