package com.imprexion.adplayer.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ADBroadcastReceiverForInteraction extends BroadcastReceiver {

    /**
     * 触摸事件广播
     */
    public static final String EVENT_ACTION_TOUCH = "com.imprexion.action.EVENT_TOUCH";
    /**
     * 手势事件广播
     */
    public static final String EVENT_ACTION_GESTURE = "com.imprexion.action.EVENT_GESTURE";

    public void setIInteractionInfo(IInteractionInfo IInteractionInfo) {
        mIInteractionInfo = IInteractionInfo;
    }

    private IInteractionInfo mIInteractionInfo;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (EVENT_ACTION_TOUCH.equals(intent.getAction()) || EVENT_ACTION_GESTURE.equals(intent.getAction())) {
            mIInteractionInfo.noticeIntoInteractionMode();
        }
    }

    public interface IInteractionInfo{
        void noticeIntoInteractionMode();
    }
}
