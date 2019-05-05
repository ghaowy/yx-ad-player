package com.imprexion.adplayer.bean;

public class EventBusMessage {
    public static final int ACTIVE_TIP = 200;
    public static final int AD_PLAY_CONTENT_UPDATE_BY_BROADCAST = 201;
    public static final int AD_RECEIVE_CALLBACK = 202;
    public static final int REMOVE_GESTURE_ACTIVE = 203;

    private int type;
    private Object mObject;

    public EventBusMessage(int type, Object object) {
        this.type = type;
        mObject = object;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Object getObject() {
        return mObject;
    }

    public void setObject(Object object) {
        mObject = object;
    }
}
