package com.imprexion.adplayer.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.imprexion.adplayer.R;
import com.imprexion.adplayer.TimerStub;
import com.imprexion.adplayer.Util;
import com.imprexion.adplayer.base.ADPlayApplication;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.bean.EventBusMessage;
import com.imprexion.adplayer.main.MainActivity;
import com.imprexion.adplayer.net.NetPresenter;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.YxLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Xianquan Feng
 * Email: xianquan.feng@imprexion.cn
 * Date: 2019/6/28
 * Desc: 轮播核心的控制器。控制广告轮播的流程、数据等；实现思路有几点：
 * 1.通过类型把图片广告数据和APP广告数据拆分；但播放索引是混合式全局的，图片广告发送到Activity去轮播，控制启动切换APP；
 * 2.通过定时器计算下一个广告的切换时间，该时间也是当前广告的播放时长；
 * 3.每播放一次广告，需要缓存一下当前播放广告的索引index到SP文件，以便下一次启动读取续播；
 * 4.BroadcastReceiver接收push消息，通过EventBus分发到此控制中心；实时更新轮播广告数据；
 */
public class PlayerControlCenter {

    private static final String TAG = "PlayerControlCenter";
    /**
     * 每个广告的默认播放时长，如果拉取到的哪个广告的playTime没有设置大于0，会默认使用此常量替换；
     */
    private static final int DEFAULT_PLAY_TIME = 10;

    PlayerModel mPlayerModel;
    private int mCurrentIndex;
    private int mPlaySize = 0;
    private Context mContext;
    private boolean mNewUpdateDataFlag;

    private ADContentPlay mAdContentPlay;

    public PlayerControlCenter(Context context) {
        mContext = context;
        mPlayerModel = new PlayerModel();
        mPlayerModel.setonPlayerDataListener(mAdListener);
        EventBus.getDefault().register(this);

        IntentFilter intentFilter = new IntentFilter();
        context.registerReceiver(mReceiver, intentFilter);
    }

    /**
     * 初始化本地的广告轮播。
     *
     * @return boolean 判断今天是否有有效的轮播。有返回true,否则返回false；
     */
    private boolean initLocalData() {
        ADContentPlay adContentPlay = mPlayerModel.getLocalAds();
        //local default 数据，不轮播。
        if (adContentPlay != null && adContentPlay.isLocalDefault()) {
            YxLog.i(TAG, "get local default ad contents.");
            return false;
        } else if (adContentPlay != null && adContentPlay.getContentPlayVOList() != null) {
            mAdContentPlay = adContentPlay;
            mPlaySize = mAdContentPlay.getContentPlayVOList().size();
            mCurrentIndex = mPlayerModel.getCurPageIndexFromSP();
            return true;
        }
        return false;
    }

    public void setNextPlayerIndex(int index) {
        mCurrentIndex = index;
        if (mCurrentIndex > mPlaySize - 1) {
            mCurrentIndex = 0;
        }
        mPlayerModel.saveCurPageIndexToSP(mCurrentIndex);
    }

    public void start() {
        //启动主动刷新一次数据。
        // 1.拉取成功则使用新的数据轮播，拉取失败则初始化本地的数据轮播；
        // 2.如果今天没有有效的轮播数据（后台或者local的不包含今天），则不轮播；
        mPlayerModel.getAdDataServerAds();
    }

    public void release() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }
    }

    private PlayerModel.onPlayerDataListener mAdListener = new PlayerModel.onPlayerDataListener() {
        @Override
        public void onGetAdDatas(ADContentPlay data) {
            boolean isNeedPlay = updateAdDatas(data);
            YxLog.i(TAG, "get new data from server success. need to call startScheduler() = " + isNeedPlay);
            if (isNeedPlay) {
                startScheduler(5);
            }
        }

        @Override
        public void onGetAdError(int code, String msg) {
            YxLog.e(TAG, "onGetAdError() code=" + code + ",msg=" + msg);
            boolean isValidData = initLocalData();
            YxLog.i(TAG, "get old data from local. need to call startScheduler() = " + isValidData);
            if (isValidData) {
                startScheduler(5);
            }
        }
    };

    /**
     * 更新广告数据。如果被更新的数据今天需要播放，则返回true；如果今天不需要播放，则取消轮播，并返回false；
     *
     * @param adContentPlay
     * @return
     */
    private synchronized boolean updateAdDatas(ADContentPlay adContentPlay) {
        if (adContentPlay == null || adContentPlay.getContentPlayVOList().size() == 0) {
            YxLog.e(TAG, "get error new update ad data,would not change current!!");
            //如果新收到的轮播数据无效或者今天已取消轮播计划，那么将马上停止广告轮播.
            mHandler.removeMessages(PLAY_NEXT);
            if (ADPlayApplication.getInstance().pictureActivity != null) {
                ADPlayApplication.getInstance().pictureActivity.finish();
            }
            return false;
        } else {
            mPlayerModel.saveAdsToSP(PlayerModel.AD_CURRENT, new Gson().toJson(adContentPlay));
            mPlayerModel.saveCurPageIndexToSP(0);
            if (adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
                mAdContentPlay = adContentPlay;
                mNewUpdateDataFlag = true;
                mCurrentIndex = -1;
                mPlaySize = adContentPlay.getContentPlayVOList().size();
                return true;
            }
        }
        return false;
    }

    /**
     * 控制中心，处理外部过来的事件。
     *
     * @param messageType
     * @param data
     */
    public void handleEvent(String messageType, Object data) {
        if ("get_push_data".equals(messageType)) {
            //收到push消息，更新广告数据
            ADContentPlay adContentPlay = PlayerModel.parseObject(data.toString());
            updateAdDatas(adContentPlay);
            NetPresenter netPresenter = new NetPresenter();
            netPresenter.onADCallback(adContentPlay);
        } else if ("no_operation".equals(messageType)) {
            //如果收到无人操作的事件，那么将启动计时器，30s后开始轮播。
            startScheduler(30);
            YxLog.i(TAG, "handleEvent() receive no_operation event, start scheduler,messageType=" + messageType);
        } else if ("interaction".equals(messageType)) {
            //如果有点击屏幕操作时间，当前播放图片的Activity不在前台，则取消定时器，不会再调度playNext()；
            MainActivity mainActivity = (MainActivity) ADPlayApplication.getInstance().pictureActivity;
            if (mainActivity == null || !mainActivity.isResumed) {
                mHandler.removeMessages(PLAY_NEXT);
                YxLog.i(TAG, "handleEvent() receive interaction event, cancelling scheduler,messageType=" + messageType);
            }
        } else {
            YxLog.i(TAG, "handleEvent() unknown intent Extra,messageType=" + messageType);
        }
    }

    /**
     * 轮播下一个广告。控制器主流程。
     */
    private synchronized void playNext() {
        //mCurrentIndex自增1
        mCurrentIndex++;
        setNextPlayerIndex(mCurrentIndex);
        /*1.取出轮播对象*/
        if (mAdContentPlay == null || mAdContentPlay.getContentPlayVOList() == null ||
                mAdContentPlay.getContentPlayVOList().size() == 0 ||
                mAdContentPlay.getContentPlayVOList().get(mCurrentIndex) == null) {
            YxLog.i(TAG, "the playing ad content data is null, cancel next playing!!");
            return;
        }
        ADContentInfo adContentInfo = mAdContentPlay.getContentPlayVOList().get(mCurrentIndex);
        /*2.根据广告的类型，调不同的轮播方法*/
        int contentType = adContentInfo.getContentType();
        //图片类型广告或应用类型广告
        if (contentType == ADContentInfo.CONTENT_TYPE_PICTURE) {
            playNextPicture(adContentInfo);
        } else if (contentType == ADContentInfo.CONTENT_TYPE_APP) {
            playNextApp(adContentInfo);
        }
        /*3.当前播放时间，即切换到下一个广告是需要等待的时间*/
        int playTime = adContentInfo.getPlayTime();
        if (playTime < 0) {
            playTime = DEFAULT_PLAY_TIME;
        }
        startScheduler(playTime);
    }

    /**
     * 轮播下一个图片类型广告。通过EventBus发送消息给Activity去播放。
     */
    private void playNextPicture(ADContentInfo adContentInfo) {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra("messageType", "playNext");

        List<ADContentInfo> dataList = getAdsByType(mAdContentPlay, ADContentInfo.CONTENT_TYPE_PICTURE);
        intent.putExtra("data", (Serializable) dataList);
        intent.putExtra("isNewData", mNewUpdateDataFlag);
        //如果是新数据，需要轮播的Activity通过isNewData标志，更新数据
        if (mNewUpdateDataFlag) {
            mNewUpdateDataFlag = false;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(mContext,
                R.anim.right_in, R.anim.left_out);
        mContext.startActivity(intent, options.toBundle());
    }

    /**
     * 轮播下一个应用。直接调用startApp()
     *
     * @param adContentInfo
     */
    private void playNextApp(ADContentInfo adContentInfo) {
        //启动应用.appCode会配置为包名
        String packageName = adContentInfo.getAppCode();
        if (!TextUtils.isEmpty(packageName)) {
            YxLog.i(TAG, "playNextApp()--> play next app ad, packageName=" + packageName);
            Util.startApp(mContext, packageName);
        } else {
            YxLog.i(TAG, "playNextApp()--> play next app ad failed,because of invalid packageName:" + packageName);
        }
    }

    /**
     * 初始化主线程的Handler。
     */
    private static final int PLAY_NEXT = 1;
    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == PLAY_NEXT) {
                playNext();
            }
        }
    };

    /**
     * 使用Handler发延迟消息模拟计时器，计算下一个广告的切换时间。支持通过mHandler.removeMessages(int)清楚消息。
     *
     * @param delayed 单位是s
     */
    private void startScheduler(int delayed) {
        Message msg = Message.obtain(mHandler, PLAY_NEXT);
        mHandler.sendMessageDelayed(msg, delayed * 1000);
        YxLog.i(TAG, "startScheduler() may cause play ads, delayed time =" + delayed + "s");
    }

    /**
     * 通过contentType广告类型获取子列表。
     *
     * @param contentType
     * @return
     */
    private List<ADContentInfo> getAdsByType(ADContentPlay adContentPlay, int contentType) {
        List<ADContentInfo> list = new ArrayList<>();
        for (int i = 0; i < adContentPlay.getContentPlayVOList().size(); i++) {
            if (adContentPlay.getContentPlayVOList().get(i).getContentType() == contentType) {
                list.add(adContentPlay.getContentPlayVOList().get(i));
            }
        }
        return list;
    }

    @Subscribe
    public void onEventBusMessageShow(EventBusMessage eventBusMessage) {
        YxLog.d(TAG, "onEventBusMessageShow");
        //新的push消息到达，需要重置广告数据。
//        if (eventBusMessage.getType() == EventBusMessage.INDEX_CHANGED) {
//            long adPlanId = Long.valueOf(eventBusMessage.getObject().toString());
//            YxLog.i(TAG, "get EventBusMessage type=INDEX_CHANGED adPlanId=" + adPlanId);
//            for (int i = 0; i < mAdContentPlay.getContentPlayVOList().size(); i++) {
//                if (mAdContentPlay.getContentPlayVOList().get(i).getAdPlanId() == adPlanId) {
//                    setNextPlayerIndex(i);
//                    YxLog.d(TAG, "update mCurrentIndex=" + i + " success by type=INDEX_CHANGED event.");
//                }
//            }
//        }
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                YxLog.i(TAG, "network state had changed.");
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    String name = info.getTypeName();
                    YxLog.i(TAG, "current network：" + name);
                    mPlayerModel.getAdDataServerAds();
                } else {
                    YxLog.w(TAG, "no network available.");
                }
            }
        }
    };
}
