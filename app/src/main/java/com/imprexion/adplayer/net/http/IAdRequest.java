package com.imprexion.adplayer.net.http;

import com.imprexion.adplayer.bean.ADContentPlay;

import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author: Xianquan Feng
 * Email: xianquan.feng@imprexion.cn
 * Date: 2019/4/22
 * Desc:
 */
public interface IAdRequest {

    //消息回复接口
    String INTERFACE_GET_AD_CONTENT = "/android/gateway/adcarousel/getCarouselContentByCondition";
    String INTERFACE_CALLBACK = "/android/gateway/imprexion/recommend/callback";


    @GET(INTERFACE_GET_AD_CONTENT)
    Flowable<BaseResult<ADContentPlay>> getAdDatas(@Query("deviceId") String deviceId,
                                                   @Query("playDate") String playDate,
                                                   @Query("token") String token);

}
