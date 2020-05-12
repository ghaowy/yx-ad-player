package com.imprexion.adplayer.net.http;

import android.os.Build;

import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.bean.ADLaunchPicData;
import com.imprexion.adplayer.player.PlayerModel;
import com.imprexion.library.YxHttp;
import com.imprexion.library.YxLog;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @author : yan
 * @date : 2019/7/22 10:54
 * @desc : TODO
 */
public class HttpADManager {
    private static final String TAG = "HttpADManager";
    private static HttpADManager mHttpManager;
    private PlayerModel.onPlayerDataListener<ADContentPlay> mAdListener;
    private PlayerModel.onPlayerDataListener<ADLaunchPicData> mAdPicListener;
    private Disposable mDisposable;

    private HttpADManager() {
    }

    public static HttpADManager getInstance() {
        if (mHttpManager == null) {
            synchronized (HttpADManager.class) {
                if (mHttpManager == null) {
                    mHttpManager = new HttpADManager();
                }
            }
        }
        return mHttpManager;
    }

    public void getAdData(String playDate, PlayerModel.onPlayerDataListener<ADContentPlay> listener) {
        mAdListener = listener;
        mDisposable = YxHttp.getDefaultInstance()
                .service(IAdRequest.class)
                .getAdDatas(Build.SERIAL, playDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<ADContentPlay>>() {
                               @Override
                               public void accept(BaseResult<ADContentPlay> adContentPlayBaseResult) throws Exception {
                                   YxLog.i(TAG, "get ad data success. result= " + adContentPlayBaseResult);
                                   if (adContentPlayBaseResult != null && adContentPlayBaseResult.isSuccess()) {
                                       if (mAdListener != null) {
                                           mAdListener.onDataLoadSuccess(adContentPlayBaseResult.getData());
                                       }
                                   }
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                YxLog.e(TAG, "get ad data error. e= " + throwable.getMessage());
                                mAdListener.onDataLoadFailed(-1, throwable.getMessage());
                            }
                        }
                );
    }


    public void getLaunchPic(PlayerModel.onPlayerDataListener<ADLaunchPicData> listener) {
        mAdPicListener = listener;
        mDisposable = YxHttp.getDefaultInstance()
                .service(IAdRequest.class)
                .getLaunchPic()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResult<ADLaunchPicData>>() {
                               @Override
                               public void accept(BaseResult<ADLaunchPicData> adContentPlayBaseResult) throws Exception {
                                   YxLog.i(TAG, "get ad data success. result= " + adContentPlayBaseResult);
                                   if (adContentPlayBaseResult != null && adContentPlayBaseResult.isSuccess()) {
                                       if (mAdListener != null) {
                                           mAdPicListener.onDataLoadSuccess(adContentPlayBaseResult.getData());
                                       }
                                   } else if (adContentPlayBaseResult != null) {
                                       if (mAdPicListener != null) {
                                           mAdPicListener.onDataLoadFailed(adContentPlayBaseResult.getCode(), adContentPlayBaseResult.getMsg());
                                       }
                                   }
                               }
                           }
                        , new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                YxLog.e(TAG, "get ad data error. e= " + throwable.getMessage());
                                mAdPicListener.onDataLoadFailed(-1, throwable.getMessage());
                            }
                        }
                );
    }


    public void release() {
        mAdListener = null;
        mAdPicListener = null;
        mDisposable.dispose();
    }


}
