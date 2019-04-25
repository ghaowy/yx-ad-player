package com.imprexion.adplayer.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.imprexion.adplayer.base.ADPlayApplication;
import com.imprexion.library.logger.YxLogger;

public class AdPlayService extends Service {

    private IContentInfoCallBack mIContentInfoCallBack;
    private static final String TAG = "AdPlayService";
    private Notification mNotification;

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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        YxLogger.d(TAG, "onStartCommand");
        if (mNotification == null) {
            Notification.Builder builder = new Notification.Builder(ADPlayApplication.getInstance().getApplicationContext());
            Intent nIntent = new Intent(this, AdPlayService.class);
            builder.setContentIntent(PendingIntent.getActivity(this, 0, nIntent, 0))
                    .setWhen(System.currentTimeMillis());
            mNotification = builder.build();
            mNotification.defaults = Notification.DEFAULT_SOUND;
            startForeground(110, mNotification);
        }
        if (intent != null && "com.imprexion.push.MESSAGE".equals(intent.getAction())) {
            String data = intent.getExtras().getString("data");
//            Toast.makeText(this, "MyService received Msg: " + data, Toast.LENGTH_LONG).show();
//            YxLogger.d(TAG, "content=" + data);
            if (mIContentInfoCallBack != null) {
                mIContentInfoCallBack.setContentInfo(data);
            } else {
                YxLogger.d(TAG, "mIContentInfoCallBack is null");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    public void setContentInfoToActivity(IContentInfoCallBack contentInfoCallBack) {
        mIContentInfoCallBack = contentInfoCallBack;
    }

    public interface IContentInfoCallBack {
        void setContentInfo(String content);
    }

    public class AISBinder extends Binder {
        public AdPlayService getService() {
            return AdPlayService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        YxLogger.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        YxLogger.d(TAG, "onDestroy()");
    }
}
