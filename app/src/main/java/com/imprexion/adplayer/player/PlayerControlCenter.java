package com.imprexion.adplayer.player;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.bean.SpecialLoopDataInfo;
import com.imprexion.adplayer.report.AdPlayerReport;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.adplayer.utils.ActivityLaunchUtil;
import com.imprexion.adplayer.utils.ActivityStackUtil;
import com.imprexion.adplayer.utils.TimeUtil;
import com.imprexion.adplayer.utils.Util;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.ContextUtils;
import com.imprexion.library.util.SharedPreferenceUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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
public class PlayerControlCenter implements IControl {
    private static final String TAG = "PlayerControlCenter";
    private static final int DEFAULT_PLAY_TIME = 10;
    private static int NO_OPERATION_SCHEDULE_TIME = 60;
    private static final int MSG_PLAY_NEXT = 1;
    private static final int MSG_SPECIAL_NEXT = 2;

    private long mEndL;
    private long mStartL;
    private int mPlaySize;
    private Handler mHandler;
    private Context mContext;
    private int mCurrentIndex;
    // 是否有用户操作
    private boolean mIsUserUse;
    private PlayerModel mPlayerModel;
    private boolean mNewUpdateDataFlag;
    private ADContentPlay mAdContentPlay;
    private WindowControl mViewControl;
    private volatile boolean mIsDataPrepared;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean isNetConnect = Util.isNetworkConnected(context);
                YxLog.i(TAG, "isNetConnect= " + isNetConnect);
                if (!isNetConnect) {
                    return;
                }
                loadData();
            }
        }
    };
    private ArrayList<ADContentInfo> mPreData;

    // 静态内部类实现单例
    public static class Holder {
        public static PlayerControlCenter instance = new PlayerControlCenter();
    }

    public PlayerControlCenter() {
        mContext = ContextUtils.get();
        mPlayerModel = new PlayerModel();
        mViewControl = new WindowControl(mContext);
        mHandler = new PlayerHandler(this);
        IntentFilter intentFilter = new IntentFilter();
        mContext.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void handleEvent(@NonNull String messageType, String data) {
        YxLog.i(TAG, "handleEvent() receive event, messageType=" + messageType);
        switch (messageType) {
            case Constants.TYPE_COUNT_DOWN:
                if (Integer.parseInt(data) > 0) {
                    NO_OPERATION_SCHEDULE_TIME = Integer.parseInt(data);
                } else {
                    NO_OPERATION_SCHEDULE_TIME = 60;
                }
                reset(NO_OPERATION_SCHEDULE_TIME);
                break;
            case Constants.TYPE_GETURE:
                //如果是手势操作事件，检测当前是否有体感应用在前台运行，是，则重置定时器，调度playNext()；
                if (!PackageUtil.isGestureAppRunning(mContext)) {
                    return;
                }
                reset(NO_OPERATION_SCHEDULE_TIME);
                break;
            case Constants.TYPE_PUSH_DATA:
                //收到push消息，更新广告数据,如果是需要马上播放，则停止当前计时，启动新计时；
                ADContentPlay adContentPlay = PlayerModel.parseObject(data);
                mPlayerModel.onADCallback(adContentPlay);
                if (updateAdData(adContentPlay)) {
                    startScheduler(DEFAULT_PLAY_TIME);
                } else {
                    stopScheduler();
                }
                break;
            case Constants.TYPE_TOUCH:
                //如果是点击屏幕操作事件，重置倒计时，调度playNext()；
                reset(NO_OPERATION_SCHEDULE_TIME);
                updateUseFlag(true);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化本地的广告轮播。
     *
     * @return boolean 判断今天是否有有效的轮播。有返回true,否则返回false；
     */
    private boolean getLocalData() {
        ADContentPlay adContentPlay = mPlayerModel.getLocalAds();
        if (adContentPlay != null && adContentPlay.getContentPlayVOList() != null) {
            mAdContentPlay = adContentPlay;
            mPlaySize = mAdContentPlay.getContentPlayVOList().size();
            mCurrentIndex = SharedPreferenceUtils.getInt(Constants.AD_INDEX, 0);
            // 处理特别轮播 延时消息
            dealDelaySpecialLoop();
            return true;
        }
        return false;
    }

    private void dealDelaySpecialLoop() {
        // 当存在特别轮播,则发送消息执行特别轮播逻辑
        if (mAdContentPlay == null || mAdContentPlay.getSpecialVOList() == null || mAdContentPlay.getSpecialVOList().size() == 0 || mHandler == null) {
            return;
        }
        mHandler.removeMessages(MSG_SPECIAL_NEXT);
        List<SpecialLoopDataInfo> specialVOList = mAdContentPlay.getSpecialVOList();
        for (SpecialLoopDataInfo dataInfo : specialVOList) {
            mStartL = TimeUtil.parserDateTime(dataInfo.getStartTime(), null);
            mEndL = TimeUtil.parserDateTime(dataInfo.getEndTime(), null);
            if (mStartL < System.currentTimeMillis() && System.currentTimeMillis() < mEndL) {
                sendSpecialNextMessage(0);
            }

            if (mStartL >= System.currentTimeMillis()) {
                sendSpecialNextMessage(mStartL - System.currentTimeMillis());
            }

            if (mEndL >= System.currentTimeMillis()) {
                sendSpecialNextMessage(mEndL - System.currentTimeMillis());
            }
        }
    }

    public void setNextPlayerIndex(int index) {
        mCurrentIndex = index;
        if (mCurrentIndex > mPlaySize - 1) {
            mCurrentIndex = 0;
        }
        SharedPreferenceUtils.putInt(Constants.AD_INDEX, mCurrentIndex);
    }

    /**
     * 更新广告数据。如果被更新的数据今天需要播放，则返回true；如果今天不需要播放，则取消轮播，并返回false；
     *
     * @param adContentPlay
     * @return
     */
    private synchronized boolean updateAdData(ADContentPlay adContentPlay) {
        if (adContentPlay == null || adContentPlay.getContentPlayVOList().size() == 0) {
            //如果新收到的轮播数据无效或者今天已取消轮播计划，那么将马上停止广告轮播,并且关闭所有的Activity
            stopScheduler();
            ActivityStackUtil.Holder.instance.exit();
            return false;
        }
        // 保存相关数据到sp
        saveDataToSp(adContentPlay);
        if (Tools.getCurrentDate("yyyy-MM-dd").equals(adContentPlay.getPlayDate())) {
            resetData(adContentPlay);
            dealDelaySpecialLoop();
            return true;
        }
        return false;
    }


    private void resetData(ADContentPlay adContentPlay) {
        mAdContentPlay = adContentPlay;
        mNewUpdateDataFlag = true;
        mCurrentIndex = -1;
        mPlaySize = adContentPlay.getContentPlayVOList().size();
    }

    private void saveDataToSp(ADContentPlay adContentPlay) {
        SharedPreferenceUtils.putString(Constants.AD_CURRENT, new Gson().toJson(adContentPlay));
        SharedPreferenceUtils.putInt(Constants.AD_INDEX, 0);
    }


    private void updateUseFlag(boolean isUse) {
        if (mViewControl == null) {
            return;
        }
        mViewControl.setUserUse(isUse);
        mIsUserUse = isUse;
    }

    private void reset(int noOperationScheduleTime) {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(MSG_PLAY_NEXT);
        startScheduler(noOperationScheduleTime);
    }


    @Override
    public void loadData() {
        mPlayerModel.getAdDataServerAds(new PlayerModel.onPlayerDataListener<ADContentPlay>() {
            @Override
            public void onDataLoadSuccess(ADContentPlay data) {
                boolean isNeedPlay = updateAdData(data);
                YxLog.i(TAG, "onDataLoadSuccess isNeedPlay = " + isNeedPlay);
                if (isNeedPlay) {
                    playNext();
                } else {
                    stopScheduler();
                }
            }

            @Override
            public void onDataLoadFailed(int code, String msg) {
                YxLog.e(TAG, "onDataLoadFailed() code = " + code + " ,msg= " + msg);
                boolean isValidData = getLocalData();
                YxLog.i(TAG, "getLocalData  isValidData = " + isValidData);
                if (isValidData) {
                    playNext();
                } else {
                    stopScheduler();
                }
            }
        });
    }

    // 处理特别轮播消息
    private void playSpecialNext() {
        if (PackageUtil.isGestureAppRunning(mContext) || mIsUserUse) {
            sendSpecialNextMessage(NO_OPERATION_SCHEDULE_TIME * 1000);
            return;
        }
        playNext();
    }

    // 处理 播放下一个逻辑
    @Override
    public synchronized void playNext() {
        NO_OPERATION_SCHEDULE_TIME = 60;
        // 当为第一次启动则不轮播
//        if (SharedPreferenceUtils.getBoolean(Constants.Key.KEY_IS_FIRST, false)) {
//            SharedPreferenceUtils.putBoolean(Constants.Key.KEY_IS_FIRST, false);
//            startScheduler(NO_OPERATION_SCHEDULE_TIME);
//            ActivityStackUtil.Holder.instance.finishAllActivity();
//            return;
//        }
        // 1、判断当霸屏轮播时 不能再轮播应用
        if ((SharedPreferenceUtils.getBoolean(Constants.Key.KEY_IS_START, false) && PackageUtil.isScreenApp(mContext)) || PackageUtil.isGestureAppRunning(mContext)) {
            YxLog.i(TAG, "不轮播, 霸屏 或者体感应用 在运行 !!!");
            startScheduler(NO_OPERATION_SCHEDULE_TIME);
            return;
        }
        // 2、当前时间内是否存在特别轮播，存在特别轮播则按照特别轮播的方式处理
        if (isSpecialLoop()) {
            mViewControl.setIsSpecialLoop(true);
            return;
        }
        mViewControl.setIsSpecialLoop(false);
        //mCurrentIndex自增1
        mCurrentIndex++;
        setNextPlayerIndex(mCurrentIndex);
        // 更新是否用户操作过,操作过下次需要弹出倒计时弹框
        updateUseFlag(false);
        if (mAdContentPlay == null || mAdContentPlay.getContentPlayVOList() == null || mAdContentPlay.getContentPlayVOList().size() == 0 || mAdContentPlay.getContentPlayVOList().get(mCurrentIndex) == null) {
            YxLog.e(TAG, "data is null");
            return;
        }
        mIsDataPrepared = true;
        ADContentInfo adContentInfo = mAdContentPlay.getContentPlayVOList().get(mCurrentIndex);
        // 2.根据广告的类型，调不同的轮播方法
        int contentType = adContentInfo.getContentType();
        //图片类型广告或应用类型广告
        if (contentType == ADContentInfo.CONTENT_TYPE_AD) {
            playNextPicture(adContentInfo);
        } else if (contentType == ADContentInfo.CONTENT_TYPE_APP) {
            playNextApp(adContentInfo);
        }
        //3.当前播放时间，即切换到下一个广告是需要等待的时间
        int playTime = adContentInfo.getPlayTime();
        if (playTime < 0) {
            playTime = DEFAULT_PLAY_TIME;
        }
        startScheduler(playTime);
    }


    // 处理特别轮播逻辑 ,当有特别轮播,则启动对应的应用
    private boolean isSpecialLoop() {
        if (mAdContentPlay == null || mAdContentPlay.getSpecialVOList() == null || mAdContentPlay.getSpecialVOList().size() == 0) {
            return false;
        }
        YxLog.i(TAG, "isSpecialLoop --> " + mAdContentPlay.getSpecialVOList());
        List<SpecialLoopDataInfo> specialVOList = mAdContentPlay.getSpecialVOList();
        for (SpecialLoopDataInfo specialData : specialVOList) {
            if (specialData == null) {
                continue;
            }
            mStartL = TimeUtil.parserDateTime(specialData.getStartTime(), null);
            mEndL = TimeUtil.parserDateTime(specialData.getEndTime(), null);
            // 在当前时间范围内
            if (System.currentTimeMillis() >= mStartL && System.currentTimeMillis() <= mEndL) {
                YxLog.i(TAG, "startApp --> " + specialData.getAppCode());
                // 直接启动
                if (Util.startApp(mContext, specialData.getAppCode())) {
                    sendBroadcast(ADContentInfo.CONTENT_TYPE_APP, specialData.getAppCode());
                }
                // 特别轮播启动时 不需要倒计时
                if (mViewControl != null) {
                    mViewControl.removeOverLayWindow(mContext);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 轮播下一个图片类型广告。
     */
    private void playNextPicture(ADContentInfo adContentInfo) {
        YxLog.i(TAG, "playNextPicture()--> play next picture adContentInfo = " + adContentInfo);
        if (adContentInfo == null) {
            return;
        }
        ArrayList<ADContentInfo> dataList = (ArrayList<ADContentInfo>) filterAds(mAdContentPlay, new Predicate<ADContentInfo>() {
            @Override
            public boolean test(ADContentInfo adContentInfo) {
                return ADContentInfo.CONTENT_TYPE_AD == adContentInfo.getContentType();
            }
        });

        // 当不是只有一个视频 并且数据和之前数据不一样
        if (isNotLastVideo(dataList)) {
            ActivityLaunchUtil.launchMainActivity(mContext, dataList, mNewUpdateDataFlag, adContentInfo.getFileType() == ADContentInfo.TYPE_VIDEO);
            mNewUpdateDataFlag = false;
            //发送广播出来，
            sendBroadcast(adContentInfo.getContentType(), adContentInfo.getAppCode());
        }
        mPreData = dataList;
    }

    // 当不是只有一个视频 并且数据和之前数据不一样
    private boolean isNotLastVideo(ArrayList<ADContentInfo> dataList) {
        if (mPreData == null || dataList == null || !ActivityStackUtil.Holder.instance.isMainActivityVisible()) {
            return true;
        }
        if (mPreData.size() != 1 || dataList.size() != 1) {
            return true;
        }
        ADContentInfo adContentInfo = dataList.get(0);
        boolean isSame = adContentInfo.isSame(mPreData.get(0));
        YxLog.i(TAG, "isNotLastVideo  isSame--> " + isSame);
        return !isSame;
    }

    /**
     * 轮播下一个应用。直接调用startApp()
     *
     * @param adContentInfo
     */
    private void playNextApp(ADContentInfo adContentInfo) {
        //启动应用.appCode会配置为包名
        String packageName = adContentInfo.getAppCode();
        YxLog.i(TAG, "playNextApp()--> packageName= " + packageName);
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        //如果成功需要发出一个广播；提供给导航栏高亮选中当前应用。
        if (Util.startApp(mContext, packageName)) {
            AdPlayerReport.onLoopApp(packageName);
            sendBroadcast(adContentInfo.getContentType(), adContentInfo.getAppCode());
        }
    }

    /**
     * 轮播控制事件，发送广播通知其他进程。
     */
    private void sendBroadcast(int contentType, String packageName) {
        Intent it = new Intent();
        it.putExtra(Constants.Key.KEY_CONTENT_TYPE, contentType);
        it.setAction(Constants.Action.BROADCAST_PLAY_NEXT_APP);
        if (!TextUtils.isEmpty(packageName) && contentType == ADContentInfo.CONTENT_TYPE_APP) {
            it.putExtra(Constants.Key.kEY_PACKAGE_NAME, packageName);
        }
        mContext.sendBroadcast(it);
    }

    /**
     * 初始化主线程的Handler。
     */

    private void sendSpecialNextMessage(long delayTime) {
        if (mHandler == null) {
            return;
        }
        YxLog.i(TAG, "sendSpecialNextMessage --> delayTime = " + delayTime);
        Message message = Message.obtain();
        message.what = MSG_SPECIAL_NEXT;
        mHandler.sendMessageDelayed(message, delayTime);
    }

    /**
     * 使用Handler发延迟消息模拟计时器，计算下一个广告的切换时间。支持通过mHandler.removeMessages(int)清楚消息。
     * 注意使用SystemClock.elapsedRealtime()表示开机到现在的时间总数，包括睡眠时间，它保证一直计时，
     * 而System.currentMills有可能不准。
     *
     * @param delayed 需要计时的时长，单位是s
     */
    private void startScheduler(int delayed) {
        if (mHandler == null) {
            return;
        }
        if (mViewControl != null && mIsDataPrepared) {
            mViewControl.setPlayTime(delayed);
        }

        mHandler.removeMessages(MSG_PLAY_NEXT);
        Message msg = Message.obtain(mHandler, MSG_PLAY_NEXT);
        msg.arg1 = delayed;
        YxLog.i(TAG, "startScheduler --> time" + delayed * 1000);
        mHandler.sendMessageDelayed(msg, delayed * 1000);
    }

    /**
     * 停止计时器。
     */
    private void stopScheduler() {
        if (mHandler == null) {
            return;
        }
        mHandler.removeMessages(MSG_PLAY_NEXT);
    }


    /**
     * 通过contentType广告类型获取子列表。
     *
     * @param predicate 对应的条件
     * @return
     */
    private List<ADContentInfo> filterAds(ADContentPlay adContentPlay, Predicate<ADContentInfo> predicate) {
        if (adContentPlay == null || adContentPlay.getContentPlayVOList() == null || adContentPlay.getContentPlayVOList().size() == 0) {
            return null;
        }
        List<ADContentInfo> list = new ArrayList<>();
        int size = adContentPlay.getContentPlayVOList().size();
        for (int i = 0; i < size; i++) {
            ADContentInfo adContentInfo = adContentPlay.getContentPlayVOList().get(i);
            if (adContentInfo == null) {
                continue;
            }
            if (predicate.test(adContentInfo)) {
                list.add(adContentInfo);
            }
        }
        return list;
    }


    static class PlayerHandler extends Handler {

        private WeakReference<PlayerControlCenter> mWfControl;

        public PlayerHandler(PlayerControlCenter playerControlCenter) {
            mWfControl = new WeakReference<PlayerControlCenter>(playerControlCenter);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mWfControl == null || mWfControl.get() == null) {
                return;
            }
            switch (msg.what) {
                case MSG_PLAY_NEXT:
                    mWfControl.get().playNext();
                    break;
                case MSG_SPECIAL_NEXT:
                    mWfControl.get().playSpecialNext();
                    break;
                default:
                    break;
            }
        }
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
}
