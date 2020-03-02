package com.imprexion.adplayer.net.http;

import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.bean.ADLaunchPicData;

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
    String INTERFACE_GET_LAUNCH_PIC = "/android/gateway/androidStartImg/getByDeviceId";
    String INTERFACE_CALLBACK = "/android/gateway/imprexion/recommend/callback";


    @GET(INTERFACE_GET_AD_CONTENT)
    Flowable<BaseResult<ADContentPlay>> getAdDatas(@Query("deviceId") String deviceId,
                                                   @Query("playDate") String playDate);


    @GET(INTERFACE_GET_LAUNCH_PIC)
    Flowable<BaseResult<ADLaunchPicData>> getLaunchPic();
}
