package com.imprexion.adplayer.player;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.net.http.HttpADManager;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.util.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Xianquan Feng
 * Email: xianquan.feng@imprexion.cn
 * Date: 2019/6/28
 * Desc: 从网络或者Local SP或者文件中，获取广告轮播数据；同时提供SP的缓存接口。
 */
public class PlayerModel {
    private static final String TAG = "PlayerModel";
    private final static String AD_DEFAULT = "adDefalt";

    public PlayerModel() {
    }

    public void getAdDataServerAds(onPlayerDataListener listener) {
        HttpADManager.getInstance().getAdData(Tools.getCurrentDate("yyyy-MM-dd"), listener);
    }

    /**
     * 从SharedPreference中获取缓存的广告数据。
     */
    public ADContentPlay getLocalAds() {
        ADContentPlay adCurrent = parseObject(SharedPreferenceUtils.getString(Constants.AD_CURRENT, null));
        //判断今天是否有有效的播放计划
        if (adCurrent == null || !Tools.getCurrentDate("yyyy-MM-dd").equals(adCurrent.getPlayDate())) {
            return null;
        }
        return adCurrent;
    }

    /**
     * 获取轮播默认图。
     *
     * @return
     */
    public ADContentPlay getDefaultADContentPlay() {
        ADContentPlay adContentPlay = new ADContentPlay();
        adContentPlay.setLocalDefault(true);
        ADContentInfo adContentInfo = new ADContentInfo();
        adContentInfo.setFileUrl(AD_DEFAULT);
        adContentInfo.setContentType(1);
        adContentInfo.setPlayTime(10);
        List<ADContentInfo> adContentInfoList = new ArrayList<>();
        adContentInfoList.add(adContentInfo);
        adContentPlay.setContentPlayVOList(adContentInfoList);
        return adContentPlay;
    }

    public static ADContentPlay parseObject(String adStr) {
        if (TextUtils.isEmpty(adStr)) {
            return null;
        }
        ADContentPlay adContentPlay = null;
        try {
            adContentPlay = JSON.parseObject(adStr, ADContentPlay.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adContentPlay;
    }

    public void release() {
        HttpADManager.getInstance().release();
    }


    public interface onPlayerDataListener<T> {
        /**
         * 主动向Presenter更新需要轮播的数据
         *
         * @param data
         */
        void onDataLoadSuccess(T data);

        void onDataLoadFailed(int code, String msg);
    }
}
