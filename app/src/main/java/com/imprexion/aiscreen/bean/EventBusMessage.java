package com.imprexion.aiscreen.bean;

public class EventBusMessage {
    public static final int STANDHERE = 200;
    public static final int AD_PLAY_CONTENT = 201;

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
