package com.imprexion.adplayer.player;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.imprexion.adplayer.base.ADPlayApplication;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.net.http.BaseResult;
import com.imprexion.adplayer.net.http.HttpHelper;
import com.imprexion.adplayer.net.http.IAdRequest;
import com.imprexion.adplayer.net.http.PublicParams;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.YxLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Author: Xianquan Feng
 * Email: xianquan.feng@imprexion.cn
 * Date: 2019/6/28
 * Desc: 从网络或者Local SP或者文件中，获取广告轮播数据；同时提供SP的缓存接口。
 */
public class PlayerModel {

    private static final String TAG = "PlayerModel";

    public static final String AD_CURRENT = "ADContentCurrent";
    private static final String AD_NEXT = "ADContentNext";
    private final static String AD_DEFAULT = "adDefalt";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private Context mContext;

    private onPlayerDataListener mAdListener;


    public PlayerModel() {
        mContext = ADPlayApplication.getInstance().getApplicationContext();
        if (mSharedPreferences == null) {
            mSharedPreferences = mContext.getSharedPreferences("AIScreenSP", Context.MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
        }
    }

    public interface onPlayerDataListener {
        /**
         * 主动向Presenter更新需要轮播的数据
         *
         * @param data
         */
        void onGetAdDatas(ADContentPlay data);

        void onGetAdError(int code, String msg);
    }

    public void setonPlayerDataListener(onPlayerDataListener listener) {
        mAdListener = listener;
    }

    public void getAdDataServerAds() {
        String deviceId = Build.SERIAL;
        String playDate = Tools.getCurrentDate("yyyy-MM-dd");

        HttpHelper httpHelper = new HttpHelper(PublicParams.SERVER_BASE_URL, PublicParams.getHeaders());
        IAdRequest adRequest = httpHelper.create(IAdRequest.class);
        adRequest.getAdDatas(deviceId, playDate, PublicParams.token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseResult<ADContentPlay>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        YxLog.e(TAG, "get ad data error. e=" + e.getMessage());
                        mAdListener.onGetAdError(-1, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResult<ADContentPlay> result) {
                        YxLog.i(TAG, "get ad data success. result=" + new Gson().toJson(result));
                        if (result != null && result.isSuccess()) {
                            if (mAdListener != null) {
                                mAdListener.onGetAdDatas(result.getData());
                            }
                        } else if (result != null) {
                            if (mAdListener != null) {
                                mAdListener.onGetAdError(result.getCode(), result.getMsg());
                            }
                        }
                    }
                });

    }

    /**
     * 从SharedPreference中获取缓存的广告数据。
     */
    public ADContentPlay getLocalAds() {
        String adContentCurrent = mSharedPreferences.getString(AD_CURRENT, null);
        String adContentNext = mSharedPreferences.getString(AD_NEXT, null);
        ADContentPlay adCurrent = parseObject(adContentCurrent);
        ADContentPlay adNext = parseObject(adContentNext);
        //判断今天是否有有效的播放计划
        if (adCurrent != null && adCurrent.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
            YxLog.i(TAG, "local SP current adContentPlay String is not valid.");
            return adCurrent;
        } else if (adNext != null && adNext.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
            YxLog.i(TAG, "local SP next adContentPlay String is valid,should return next and set it to current.");
            saveAdsToSP(AD_CURRENT, adContentNext);
            return adNext;
        } else {
            //今天没有有效的播放计划，返回默认的图片。
            YxLog.i(TAG, "local SP has no valid ad data,should return default data.");
            return getDefaultADContentPlay();
        }
    }

    /**
     * 把广告内容存储到SharedPreference
     *
     * @param ADContentPlayStr
     */
    public void saveAdsToSP(String curOrNext, String ADContentPlayStr) {
        if (mEditor != null) {
            mEditor.putString(curOrNext, ADContentPlayStr);
            mEditor.commit();
        }
    }

    public int getCurPageIndexFromSP() {
        return mSharedPreferences.getInt("mCurrentPage", 0);
    }

    public void saveCurPageIndexToSP(int pageIndex) {
        if (mEditor != null) {
            mEditor.putInt("mCurrentPage", pageIndex);
            mEditor.commit();
        }
    }

    /**
     * 获取轮播默认图。
     *
     * @return
     */
    public ADContentPlay getDefaultADContentPlay() {
        ADContentPlay adContentPlay;
        adContentPlay = new ADContentPlay();
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
            YxLog.e(TAG, "error ad json string.");
        } finally {
            return adContentPlay;
        }
    }
}
