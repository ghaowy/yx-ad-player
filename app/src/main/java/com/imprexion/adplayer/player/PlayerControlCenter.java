package com.imprexion.adplayer.player;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.ActivityOptionsCompat;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.imprexion.adplayer.R;
import com.imprexion.adplayer.TimerStub;
import com.imprexion.adplayer.utils.Util;
import com.imprexion.adplayer.base.ADPlayApplication;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.main.MainActivity;
import com.imprexion.adplayer.net.NetPresenter;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.YxLog;

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

    private static final int NO_OPERATION_SCHEDULE_TIME = 60;

    private PlayerModel mPlayerModel;
    private int mCurrentIndex;
    private int mPlaySize = 0;
    private Context mContext;
    private boolean mNewUpdateDataFlag;

    private TimerStub mTimerStub;

    private ADContentPlay mAdContentPlay;
    private WindowControl mViewControl;
    private volatile boolean mIsDataPrepared;

//    ThreadPoolExecutor mThreadPoolExecutor;

    public PlayerControlCenter(Context context) {
        mContext = context;
        mPlayerModel = new PlayerModel();
        mViewControl = new WindowControl(context);
        mPlayerModel.setonPlayerDataListener(mAdListener);

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
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        if (mPlayerModel != null) {
            mPlayerModel.release();
            mPlayerModel = null;
        }

        if (mViewControl != null) {
            mViewControl.release();
            mViewControl = null;
        }

    }

    /**
     * 接收主动请求网络广告数据的回调。
     */
    private PlayerModel.onPlayerDataListener mAdListener = new PlayerModel.onPlayerDataListener() {
        @Override
        public void onGetAdDatas(ADContentPlay data) {
            boolean isNeedPlay = updateAdDatas(data);
            YxLog.i(TAG, "get new data from server success. need to call startScheduler() = " + isNeedPlay);
            if (isNeedPlay) {
                playNext();
            } else {
                stopScheduler();
            }
        }

        @Override
        public void onGetAdError(int code, String msg) {
            YxLog.e(TAG, "onGetAdError() code=" + code + ",msg=" + msg);
            boolean isValidData = initLocalData();
            YxLog.i(TAG, "get old data from local. need to call startScheduler() = " + isValidData);
            if (isValidData) {
                playNext();
            } else {
                stopScheduler();
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
            stopScheduler();
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
    public synchronized void handleEvent(String messageType, Object data) {
        YxLog.i(TAG, "handleEvent() receive event, messageType=" + messageType);
        if ("get_push_data".equals(messageType)) {
            //收到push消息，更新广告数据,如果是需要马上播放，则停止当前计时，启动新计时；
            ADContentPlay adContentPlay = PlayerModel.parseObject(data.toString());
            boolean isNeedPlay = updateAdDatas(adContentPlay);
            NetPresenter netPresenter = new NetPresenter();
            netPresenter.onADCallback(adContentPlay);
            stopScheduler();
            if (isNeedPlay) {
                startScheduler(DEFAULT_PLAY_TIME);
            }
        } else if ("touch".equals(messageType)) {
            //如果是点击屏幕操作事件，重置倒计时，调度playNext()；
            reset(NO_OPERATION_SCHEDULE_TIME);
            updateUseFlag(true);
//            stopScheduler();
        } else if ("gesture".equals(messageType)) {
            //如果是手势操作事件，检测当前是否有体感应用在前台运行，是，则重置定时器，调度playNext()；
            //否则暂不处理；
            boolean flag = PackageUtil.isGestureAppRunning(mContext);
            YxLog.i(TAG, "isGestureAppRunning=" + flag);
            if (flag) {
                reset(NO_OPERATION_SCHEDULE_TIME);
            }
        } else {
            YxLog.i(TAG, "handleEvent() unknown event of null Extra data");
        }
    }

    private void updateUseFlag(boolean isUse) {
        if (mViewControl == null) {
            return;
        }
        mViewControl.setUserUse(isUse);
    }

    private void reset(int noOperationScheduleTime) {
        if (mHandler != null) {
            YxLog.i(TAG, "reset--> PLAY_NEXT");
            mHandler.removeMessages(PLAY_NEXT);
        }
        startScheduler(noOperationScheduleTime);
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
            updateUseFlag(false);
            return;
        }
        mIsDataPrepared = true;
        ADContentInfo adContentInfo = mAdContentPlay.getContentPlayVOList().get(mCurrentIndex);
        /*2.根据广告的类型，调不同的轮播方法*/
        int contentType = adContentInfo.getContentType();
        //图片类型广告或应用类型广告
        if (contentType == ADContentInfo.CONTENT_TYPE_PICTURE) {
            playNextPicture(adContentInfo);
        } else if (contentType == ADContentInfo.CONTENT_TYPE_APP) {
            playNextApp(adContentInfo);
        }
        /*3 启动avatar和tracking service*/
        startAvatar();
        startTrackingService();
        /*4.当前播放时间，即切换到下一个广告是需要等待的时间*/
        int playTime = adContentInfo.getPlayTime();
        if (playTime < 0) {
            playTime = DEFAULT_PLAY_TIME;
        }
        updateUseFlag(false);
        startScheduler(playTime);
    }

    /**
     * 轮播下一个图片类型广告。
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
        YxLog.i(TAG, "playNextPicture()--> play next picture ad");
        //发送广播出来，
        sendBroadcast(adContentInfo);
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
            boolean isSuccess = Util.startApp(mContext, packageName);
            //如果成功需要发出一个广播；提供给导航栏高亮选中当前应用。
            if (isSuccess) {
                sendBroadcast(adContentInfo);
            }
        } else {
            YxLog.i(TAG, "playNextApp()--> play next app ad failed,because of invalid packageName:" + packageName);
        }
    }

    /**
     * 轮播控制事件，发送广播通知其他进程。
     *
     * @param adContentInfo
     */
    private void sendBroadcast(ADContentInfo adContentInfo) {
        Intent it = new Intent();
        it.putExtra("contentType", adContentInfo.getContentType());
        it.setAction("com.imprexion.action.PLAY_APP");
        if (adContentInfo.getContentType() == ADContentInfo.CONTENT_TYPE_APP) {
            it.putExtra("packageName", adContentInfo.getAppCode());
        }
        mContext.sendBroadcast(it);
        YxLog.i(TAG, "playNext()--> sendBroadcast  contentType=" + adContentInfo.getContentType());
    }

    private void startAvatar() {
        final String packName = "com.imprexion.avatar";
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = Util.startApp(mContext, packName);
                YxLog.i(TAG, "start avatar isSuccess=" + isSuccess);
            }
        }, 500);
    }

    private void startTrackingService() {

        String packName = "com.imprexion.service.tracking";
        String serviceName = "com.imprexion.service.tracking.TrackingService";
        if (!Util.isAppRunning(mContext, packName)) {
            Intent it = new Intent();
            it.setComponent(new ComponentName(packName, serviceName));
            mContext.startService(it);
            YxLog.i(TAG, "start tracking service with className");
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
                if (PackageUtil.isGestureAppRunning(mContext)) {
                    YxLog.i(TAG, "isGestureAppRunning is Running --> reset");
                    startScheduler(NO_OPERATION_SCHEDULE_TIME);
                    return;
                }
                int delayTime = msg.arg1;
                int scheduleTime = (int) ((SystemClock.elapsedRealtime() - (long) msg.obj) / 1000);
                YxLog.i(TAG, "mHandler.handleMessage() msg.what=PLAY_NEXT,delayTime=" + delayTime
                        + ",scheduleTime=" + scheduleTime);
                if (scheduleTime < delayTime) {
                    //如果handler回调太快，清除消息，默认重新计时10s
                    stopScheduler();
                    startScheduler(DEFAULT_PLAY_TIME);
                    YxLog.i(TAG, "mHandler.handleMessage()  callback too soon, reset delayTime,"
                            + ",scheduleTime=" + DEFAULT_PLAY_TIME);
                    return;
                } else {
                    playNext();
                }
            }
        }
    };

    /**
     * 使用Handler发延迟消息模拟计时器，计算下一个广告的切换时间。支持通过mHandler.removeMessages(int)清楚消息。
     * 注意使用SystemClock.elapsedRealtime()表示开机到现在的时间总数，包括睡眠时间，它保证一直计时，
     * 而System.currentMills有可能不准。
     *
     * @param delayed 需要计时的时长，单位是s
     */
    private void startScheduler(int delayed) {
        if (mHandler != null) {
            mHandler.removeMessages(PLAY_NEXT);
        }
        if (mViewControl != null && mIsDataPrepared) {
            mViewControl.setPlayTime(delayed);
        }

        Message msg = Message.obtain(mHandler, PLAY_NEXT);
        msg.arg1 = delayed;
        msg.obj = SystemClock.elapsedRealtime();
        YxLog.i(TAG, "startScheduler --> time" + delayed * 1000);
        mHandler.sendMessageDelayed(msg, delayed * 1000);
    }

    /**
     * 停止计时器。
     */
    private void stopScheduler() {
        mHandler.removeMessages(PLAY_NEXT);
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
                    YxLog.i(TAG, "no network available.");
                }
            }
        }
    };
}
