package com.imprexion.aiscreen.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.imprexion.aiscreen.ContentInfo;
import com.imprexion.aiscreen.IAISAidlInterface;

public class AISService extends Service {

    private IContentInfoCallBack mIContentInfoCallBack;
    private static final String TAG = "AISService";

    public AISService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        int src = intent.getIntExtra("src", -1);
        if (src == 0) {
            return new AISBinder();
        }

        return mAISServer;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        if ("com.imprexion.push.MESSAGE".equals(intent.getAction())) {
            String data = intent.getExtras().getString("data");
            Toast.makeText(this, "MyService received Msg: " + data, Toast.LENGTH_LONG).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    IAISAidlInterface.Stub mAISServer = new IAISAidlInterface.Stub() {
        @Override
        public void receiveContentInfoByServer(ContentInfo contentInfo) throws RemoteException {
            mIContentInfoCallBack.setContentInfo(contentInfo);
        }
    };

    public void setContentInfoToActivity(IContentInfoCallBack contentInfoCallBack) {
        mIContentInfoCallBack = contentInfoCallBack;
    }

    public interface IContentInfoCallBack {
        void setContentInfo(ContentInfo contentInfo);
    }

    public class AISBinder extends Binder {
        public AISService getService() {
            return AISService.this;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }
}
