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
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, Constants.TYPE_GETURE);
                context.startService(it);
                break;
            case Constants.Action.EVENT_NO_OPERATION:
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, Constants.TYPE_NO_OPERATION);
                context.startService(it);
                break;
            case Constants.Action.EVENT_TOUCH:
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, Constants.TYPE_TOUCH);
                context.startService(it);
                break;
            case Constants.Action.PUSH_MESSAGE:
                if (intent.getExtras() == null) {
                    return;
                }
                String content = intent.getExtras().getString(Constants.Key.KEY_DATA);
                YxLog.i(TAG, "receive push advertisement data. data = " + content);
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, Constants.TYPE_PUSH_DATA);
                it.putExtra(Constants.Key.KEY_DATA, content);
                context.startService(it);
                break;
            case Constants.Action.EVENT_COUNT_DOWN:
                if (intent.getExtras() == null) {
                    return;
                }
                int timeout = intent.getExtras().getInt(Constants.Key.KEY_TIME);
                YxLog.i(TAG, "receive push advertisement data. data = " + timeout);
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, Constants.TYPE_COUNT_DOWN);
                it.putExtra(Constants.Key.KEY_DATA, String.valueOf(timeout));
                context.startService(it);
            case Constants.Action.EVENT_PLAY_NEXT:
                it.putExtra(Constants.Key.KEY_MESSAGE_TYPE, Constants.PLAY_NEXT);
                context.startService(it);
                break;
            default:
                break;
        }
    }

    // 收到霸屏消息,存储变量在本地, 当需要轮播时 取出判断 确定是否需要处理
    private void saveValue(Intent intent) {
        if (intent == null) {
            return;
        }
        SharedPreferenceUtils.putBoolean(Constants.Key.KEY_IS_START, intent.getBooleanExtra(Constants.Key.KEY_IS_START, false));
        YxLog.i(TAG, " getDominateMessage... " + (intent.getBooleanExtra(Constants.Key.KEY_IS_START, false)));
    }
}
