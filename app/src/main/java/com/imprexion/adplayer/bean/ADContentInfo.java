package com.imprexion.adplayer.bean;

import java.io.Serializable;

public class ADContentInfo implements Serializable {

    public static final int CONTENT_TYPE_PICTURE = 1;
    public static final int CONTENT_TYPE_APP = 2;

    /**
     * 内容类型 (1:广告, 2:应用)
     */
    private int contentType;
    /**
     * 排序
     */
    private int sort;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件url
     */
    private String fileUrl;
    /**
     * 播放时长 (单位秒)
     */
    private int playTime;
    /**
     * 是否互动
     */
    private int isInteraction;
    // --------  ad 广告相关 start

    /**
     * 广告播放计划ID
     */
    private long adPlanId;
    /**
     * 广告用途类型 (1:商用,2:内用)
     */
    private int useType;
    // --------  ad 广告相关 end

    // --------  app 应用相关 start
    /**
     * 应用播放计划ID
     */
    private long appPlanId;
    /**
     * 应用编码 具有唯一性
     */
    private String appCode;
    /**
     * 是否后台运行
     */
    private int isRun;
    // --------  app 应用相关 end

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public int getIsInteraction() {
        return isInteraction;
    }

    public void setIsInteraction(int isInteraction) {
        this.isInteraction = isInteraction;
    }

    public long getAdPlanId() {
        return adPlanId;
    }

    public void setAdPlanId(long adPlanId) {
        this.adPlanId = adPlanId;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }

    public long getAppPlanId() {
        return appPlanId;
    }

    public void setAppPlanId(long appPlanId) {
        this.appPlanId = appPlanId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public int getIsRun() {
        return isRun;
    }

    public void setIsRun(int isRun) {
        this.isRun = isRun;
    }
}
