package com.imprexion.adplay.net;

import com.imprexion.adplay.bean.WeatherInfo;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetService {

    @GET("now")
    Observable<WeatherInfo> updateWeather(@Query("key") String key, @Query(encoded = true, value = "location") String location);

    @POST("adCarouselCallback")
    @Headers({"Content-Type:application/json", "token:30EFKHab7C5C56K9"})
    Call<String> onAdcontentCallback(@Body() RequestBody requestBody);

}
