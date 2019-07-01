package com.imprexion.adplayer.bean;

import java.util.List;

public class ADContentPlay {

    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 播放日期
     * (yyyy-MM-dd)
     */
    private String playDate;
    /**
     * 业务id 一个队列的唯一标识
     */
    private String businessId;
    /**
     * 变更模式(1:新增, 2:更新, 3:删除)
     */
    private Integer changeType;
    /**
     * 轮播对象
     */
    private List<ADContentInfo> contentPlayVOList;

    private transient boolean isLocalDefault;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPlayDate() {
        return playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public List<ADContentInfo> getContentPlayVOList() {
        return contentPlayVOList;
    }

    public void setContentPlayVOList(List<ADContentInfo> contentPlayVOList) {
        this.contentPlayVOList = contentPlayVOList;
    }

    public boolean isLocalDefault() {
        return isLocalDefault;
    }

    public void setLocalDefault(boolean localDefault) {
        isLocalDefault = localDefault;
    }
}
