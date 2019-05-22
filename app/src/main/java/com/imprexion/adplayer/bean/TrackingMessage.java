package com.imprexion.adplayer.bean;

public class TrackingMessage {
    /**
     * usrsex :0:表示无人；!0：表示有人
     */
    private int usrsex;
    /**
     * isStandHere,false:站对位置了；true:表示没有站对位置
     */
    private boolean isStandHere;
    /**
     * isActived,true:表示激活屏幕；false:表示屏幕未激活
     */
    private boolean isActived;

    public int getUsrsex() {
        return usrsex;
    }

    public void setUsrsex(int usrsex) {
        this.usrsex = usrsex;
    }

    public boolean isStandHere() {
        return isStandHere;
    }

    public void setStandHere(boolean standHere) {
        isStandHere = standHere;
    }

    public boolean isActived() {
        return isActived;
    }

    public void setActived(boolean actived) {
        isActived = actived;
    }
}
