package com.imprexion.adplayer.widget;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.imprexion.adplayer.widget.face.WidgetConstants;
import com.imprexion.adplayer.widget.face.FaceModuleUtil;
import com.imprexion.adplayer.widget.face.event.FaceIdDetectUserEvent;
import com.imprexion.adplayer.widget.face.event.FaceSignInEvent;
import com.imprexion.adplayer.widget.face.event.RetryBindEvent;
import com.imprexion.library.YxLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author hardy
 * @name yx-ad-player
 * @class name：com.imprexion.adplayer.widget
 * @class describe: 在轮播广告过程中，接收人脸插件传来检测到人脸信息，弹出动画
 * 人脸插件逻辑：后台定时监测频率是1s一次，监测到用户就发送消息给通过Messenger方式绑定的所有应用，如果发生触屏操作，5s内FaceID不再进行后台定时监测
 * @time 2020/5/13 15:02
 * @change
 * @chang time
 * @class describe
 */
public class UserAnimWidgetService extends Service {
    private static final String TAG = UserAnimWidgetService.class.getSimpleName();


    /**
     * 判断无用户时间
     */
    private static final int DETECT_NOBODY_TIME = 10 * 1000;

    private boolean mDetectEnable = false;
    private AnimWindowControl mAnimWindowControl = null;

    private long mLastDetectUserTime = 0;

    public UserAnimWidgetService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YxLog.i(TAG, "onCreate()");
        mAnimWindowControl = new AnimWindowControl(this);
        FaceModuleUtil.getInstance().registerAndBindServices(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent == null) {
            return START_STICKY;
        }
        mDetectEnable = intent.getBooleanExtra(WidgetConstants.KEY_ADPLAY_CONTROL_SCREEN, false);
        YxLog.i(TAG, "onStartCommand --- mDetectEnable = " + mDetectEnable);

        if (mDetectEnable == false) {
            mAnimWindowControl.removeOverLayWindow();
        }


        if (mDetectEnable == true) {
            // for test
//            mAnimWindowControl.addOverLayWindow();

            if( System.currentTimeMillis() - mLastDetectUserTime > DETECT_NOBODY_TIME) {
                mAnimWindowControl.removeOverLayWindow();
            }
        }

        return START_STICKY;
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        mAnimWindowControl.release();
        FaceModuleUtil.getInstance().unRegister(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRetryBindEvent(RetryBindEvent event) {
        FaceModuleUtil.getInstance().registerAndBindServices(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onFaceIdDetectUser(FaceIdDetectUserEvent event) {
        YxLog.i(TAG, "--- onFaceIdDetectUser ---");
        mLastDetectUserTime = System.currentTimeMillis();

        if (mDetectEnable == true) {
            mAnimWindowControl.addOverLayWindow();
        }

    }
}
