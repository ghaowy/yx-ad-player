package com.imprexion.adplayer.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.ActivityOptionsCompat;

import com.imprexion.adplayer.R;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.ContextUtils;

public class AdPlayService extends Service {

    private static final String TAG = "AdPlayService";

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
        YxLog.d(TAG, "onStartCommand");
        if (intent != null) {
            //获取包名
            String packageName = intent.getStringExtra("start_app");
            if (packageName != null) {
                YxLog.d(TAG, "onStartCommand --- packageName = " + packageName);
                switchApp(packageName);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    public class AISBinder extends Binder {
        public AdPlayService getService() {
            return AdPlayService.this;
        }
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
    }

    /**
     * 通过包名启动应用
     *
     * @param packageName
     */
    private void switchApp(String packageName) {
        YxLog.d(TAG, "switchApp --- packageName = " + packageName);
        Intent intent = ContextUtils.get().getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            YxLog.d(TAG, "switchApp --- getLaunchIntentForPackage, intent is null!");
            return;
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(ContextUtils.get(),
                R.anim.right_in, R.anim.left_out);
        startActivity(intent, options.toBundle());
    }
}
