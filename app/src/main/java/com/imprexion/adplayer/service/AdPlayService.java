package com.imprexion.adplayer.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.player.PlayerControlCenter;
import com.imprexion.library.YxConfig;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.SharedPreferenceUtils;

public class AdPlayService extends Service {

    private static final String TAG = "AdPlayService";

    public AdPlayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YxLog.i(TAG, "onCreate()");
        SharedPreferenceUtils.putBoolean(Constants.Key.KEY_IS_FIRST, true);
        PlayerControlCenter.Holder.instance.loadData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = getStartNotification();
        startForeground(115, notification);// 开始前台服务
        /**
         * 由广播接收者 {@code ADBroadcastReceiver} 回调过来的消息,或者首次启动Service的消息。
         */
        if (intent == null) {
            return START_STICKY;
        }
        String messageType = intent.getStringExtra(Constants.Key.KEY_MESSAGE_TYPE);
        if (!TextUtils.isEmpty(messageType)) {
            String data = intent.getStringExtra(Constants.Key.KEY_DATA);
            PlayerControlCenter.Holder.instance.handleEvent(messageType, data);
        }
        return START_STICKY;
    }

    @TargetApi(16)
    private Notification getStartNotification() {
        com.imprexion.library.config.HttpConfig config = YxConfig.getPublic(com.imprexion.library.config.HttpConfig.class);
        String env = "";
        if (config != null) {
            env = config.env.trim().toLowerCase();
        }
        //获取一个Notification构造器
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
        Intent nfIntent = new Intent(this, this.getClass());
        /*设置PendingIntent*/
        builder.setContentIntent(PendingIntent.getService(this, 0, nfIntent, 0))
                // 设置下拉列表中的图标(大图标)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), android.R.mipmap.sym_def_app_icon))
                // 设置下拉列表里的标题
                .setContentTitle(this.getClass().getSimpleName())
                // 设置状态栏内的小图标
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                // 设置上下文内容
                .setContentText("ENV=" + env + " 正在运行。")
                // 设置该通知发生的时间
                .setWhen(System.currentTimeMillis());
        return builder.build();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PlayerControlCenter.Holder.instance.release();
    }
}
