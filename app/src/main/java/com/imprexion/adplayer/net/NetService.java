package com.imprexion.adplayer.net;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NetService {

    @POST("adCarouselCallback")
//    @Headers({"Content-Type:application/json", "token:30EFKHab7C5C56K9"})//dev
    @Headers({"Content-Type:application/json", "token:30EFKHab7C5C56K8"})//official
    Call<String> onAdcontentCallback(@Body RequestBody requestBody);

}
