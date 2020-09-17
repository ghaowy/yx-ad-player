package com.imprexion.adplayer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.player.PlayerControlCenter;
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
        // 发送通知
        startNotification();

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

    private void startNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent nfIntent = new Intent(this, this.getClass());
        Notification.Builder builder = null;
        // 针对8.0通知适配
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            //获取一个Notification构造器
            builder = new Notification.Builder(this.getApplicationContext());
        } else {
            String id = "1";
            String channelName = "adplayer channel";
            NotificationChannel channel = new NotificationChannel(id, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder = new Notification.Builder(getApplicationContext(), id);
        }

        builder.setContentIntent(PendingIntent.getService(this, 0, nfIntent, 0))
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.mipmap.sym_def_app_icon))
                .setContentTitle(this.getClass().getSimpleName())
                .setContentText("正在运行。")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .build();

        startForeground(123, builder.build());
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
