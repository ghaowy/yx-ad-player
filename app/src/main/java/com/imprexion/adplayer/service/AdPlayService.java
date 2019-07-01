package com.imprexion.adplayer.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityOptionsCompat;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.net.http.PublicParams;
import com.imprexion.adplayer.player.PlayerControlCenter;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.ContextUtils;

public class AdPlayService extends Service {

    private static final String TAG = "AdPlayService";

    private PlayerControlCenter mControlCenter;

    public AdPlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        int src = intent.getIntExtra("src", -1);
        if (src == 0) {
            return new AISBinder();
        }

        return null;
    }

    public class AISBinder extends Binder {
        public AdPlayService getService() {
            return AdPlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        YxLog.i(TAG, "onCreate()");
        mControlCenter = new PlayerControlCenter(this);
        mControlCenter.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        YxLog.i(TAG, "onStartCommand()   本机的序列号，deviceId=" + Build.SERIAL
                + ", 连接Server环境=" + PublicParams.ENV);
        Notification notification = getStartNotification();
        startForeground(115, notification);// 开始前台服务
        /**
         * 由广播接收者 {@code ADBroadcastReceiver} 回调过来的消息,或者首次启动Service的消息。
         */
        if (intent != null) {
            String messageType = intent.getStringExtra("messageType");
            String data = intent.getStringExtra("data");
            YxLog.i(TAG, "onStartCommand() messageType=" + messageType + ",data=" + data);
            mControlCenter.handleEvent(messageType, data);
        }
        return START_STICKY;
    }

    @TargetApi(16)
    private Notification getStartNotification() {
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
                .setContentText("ENV=" + PublicParams.ENV + " 正在运行。")
                // 设置该通知发生的时间
                .setWhen(System.currentTimeMillis());

        // 获取构建好的Notification
        Notification notification = builder.build();
        //设置为默认的声音
//        notification.defaults = Notification.DEFAULT_SOUND;
        return notification;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        YxLog.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        YxLog.d(TAG, "onDestroy()");
        mControlCenter.release();
    }
}
