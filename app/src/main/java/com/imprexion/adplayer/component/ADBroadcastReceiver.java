package com.imprexion.adplayer.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.service.AdPlayService;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.SharedPreferenceUtils;

public class ADBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = "ADBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }

        YxLog.i(TAG, "onReceive action= " + intent.getAction());
        Intent it = new Intent(context, AdPlayService.class);
        switch (intent.getAction()) {
            case Constants.Action.LOOP_EVENT:
                saveValue(intent);
                break;
            case Intent.ACTION_BOOT_COMPLETED:
                context.startService(it);
                break;
            case Constants.Action.EVENT_GESTURE:
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, "geture");
                context.startService(it);
                break;
            case Constants.Action.EVENT_NO_OPERATION:
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, "no_operation");
                context.startService(it);
                break;
            case Constants.Action.EVENT_TOUCH:
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, "touch");
                context.startService(it);
                break;
            case Constants.Action.PUSH_MESSAGE:
                if (intent.getExtras() == null) {
                    return;
                }
                String content = intent.getExtras().getString("data");
                YxLog.i(TAG, "receive push advertisement data. data = " + content);
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, "get_push_data");
                it.putExtra(Constants.Key.KEY_DATA, content);
                context.startService(it);
                break;
            default:
                break;
        }
    }

    private void saveValue(Intent intent) {
        if (intent == null) {
            return;
        }
        SharedPreferenceUtils.putBoolean(Constants.Key.KEY_IS_START, intent.getBooleanExtra(Constants.Key.KEY_IS_START, false));
        YxLog.i(TAG, " getDominateMessage... " + (intent.getBooleanExtra(Constants.Key.KEY_IS_START, false)));
    }
}
