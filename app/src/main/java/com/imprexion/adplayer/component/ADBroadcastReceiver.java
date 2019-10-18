package com.imprexion.adplayer.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.service.AdPlayService;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.SharedPreferenceUtils;

public class ADBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = "ADBroadcastReceiver";

    private static final String AD_CURRENT = "ADContentCurrent";
    private static final String AD_NEXT = "ADContentNext";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.imprexion.push.MESSAGE")) {
            String content = intent.getExtras().getString("data");
            YxLog.i(TAG, "receive push advertisement data. data = " + content);
//            EventBus.getDefault().post(new EventBusMessage(EventBusMessage.AD_PLAY_CONTENT_UPDATE_BY_BROADCAST, content));
            Intent it = new Intent(context, AdPlayService.class);
            it.putExtra("messageType", "get_push_data");
            it.putExtra("data", content);
            context.startService(it);
        } else if (intent.getAction().equals("com.imprexion.action.EVENT_TOUCH")) {
            YxLog.i(TAG, "onReceive InteractionInfo");
            Intent it = new Intent(context, AdPlayService.class);
            it.putExtra("messageType", "touch");
            context.startService(it);
//            EventBus.getDefault().post(new EventBusMessage(EventBusMessage.RECEIVE_INTERACTION_EVENT, null));
//            ToastUtils.show("轮播接收到了，有人在交互！");
        } else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent it = new Intent(context, AdPlayService.class);
            context.startService(it);
        } else if (intent.getAction().equals("com.imprexion.action.EVENT_NO_OPERATION")) {
            Intent it = new Intent(context, AdPlayService.class);
            it.putExtra("messageType", "no_operation");
            context.startService(it);
        } else if (intent.getAction().equals("com.imprexion.action.EVENT_GESTURE")) {
            Intent it = new Intent(context, AdPlayService.class);
            it.putExtra("messageType", "geture");
            context.startService(it);
        } else if (intent.getAction().equals("com.imprexion.adplayer.LOOP_EVENT")) {
            dealSaveMessage(intent);
        }
    }

    private void dealSaveMessage(Intent intent) {
        if (intent == null) {
            return;
        }
        SharedPreferenceUtils.putBoolean(Constants.KEY_IS_START, intent.getBooleanExtra(Constants.KEY_IS_START, false));
    }
}
