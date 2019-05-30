package com.imprexion.adplayer.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.bean.EventBusMessage;
import com.imprexion.adplayer.net.NetPresenter;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

public class ADBroadcastReceiver extends BroadcastReceiver {

    private final static String TAG = "ADBroadcastReceiver";

    private static final String AD_CURRENT = "ADContentCurrent";
    private static final String AD_NEXT = "ADContentNext";
    private NetPresenter mNetPresenter;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.imprexion.push.MESSAGE")) {
            YxLog.i(TAG, "receive advertisement data");
            String content = intent.getExtras().getString("data");
//        YxLog.d(TAG, "ContentInfo=" + content);
            Log.d(TAG, "ContentInfo=" + content);
//        Toast.makeText(context, "MyService received Msg: " + content, Toast.LENGTH_LONG).show();
            ADContentPlay adContentPlay = JSON.parseObject(content, ADContentPlay.class);
            if (mNetPresenter == null) {
                mNetPresenter = new NetPresenter();
            }
            mNetPresenter.onADCallback(adContentPlay);
            if (mSharedPreferences == null) {
                mSharedPreferences = context.getSharedPreferences("AIScreenSP", Context.MODE_PRIVATE);
                mEditor = mSharedPreferences.edit();
            }
            if (adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
                mEditor.putString(AD_CURRENT, content);
                mEditor.commit();
                if (adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
                    EventBus.getDefault().post(new EventBusMessage(EventBusMessage.AD_PLAY_CONTENT_UPDATE_BY_BROADCAST, null));
                }
            } else {
                mEditor.putString(AD_NEXT, content);
                mEditor.commit();
            }
        }
        if (intent.getAction().equals("com.imprexion.action.EVENT_TOUCH") || intent.getAction().equals("com.imprexion.action.EVENT_GESTURE")) {
            YxLog.d(TAG, "onReceive InteractionInfo");
            EventBus.getDefault().post(new EventBusMessage(EventBusMessage.RECEIVE_INTERACTION_EVENT, null));
//            ToastUtils.show("轮播接收到了，有人在交互！");
        }
    }

}
