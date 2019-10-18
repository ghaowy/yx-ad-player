package com.imprexion.adplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class ADContentInfo implements Parcelable {

    public static final int CONTENT_TYPE_AD = 1;
    public static final int CONTENT_TYPE_APP = 2;
    public static final int TYPE_VIDEO = 3;
    public static final int TYPE_PICTURE = 2;
    public static final int TYPE_TEXT = 1;

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

    /**
     * 1:文字,2:图片,3:视频 ,
     */
    private int fileType;

    public ADContentInfo() {
    }

    protected ADContentInfo(Parcel in) {
        contentType = in.readInt();
        sort = in.readInt();
        fileName = in.readString();
        fileUrl = in.readString();
        playTime = in.readInt();
        isInteraction = in.readInt();
        adPlanId = in.readLong();
        useType = in.readInt();
        appPlanId = in.readLong();
        appCode = in.readString();
        isRun = in.readInt();
        fileType = in.readInt();
    }

    public static final Creator<ADContentInfo> CREATOR = new Creator<ADContentInfo>() {
        @Override
        public ADContentInfo createFromParcel(Parcel in) {
            return new ADContentInfo(in);
        }

        @Override
        public ADContentInfo[] newArray(int size) {
            return new ADContentInfo[size];
        }
    };

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }


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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(contentType);
        dest.writeInt(sort);
        dest.writeString(fileName);
        dest.writeString(fileUrl);
        dest.writeInt(playTime);
        dest.writeInt(isInteraction);
        dest.writeLong(adPlanId);
        dest.writeInt(useType);
        dest.writeLong(appPlanId);
        dest.writeString(appCode);
        dest.writeInt(isRun);
        dest.writeInt(fileType);
    }

    @Override
    public String toString() {
        return "ADContentInfo{" +
                "contentType=" + contentType +
                ", sort=" + sort +
                ", fileName='" + fileName + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", playTime=" + playTime +
                ", isInteraction=" + isInteraction +
                ", adPlanId=" + adPlanId +
                ", useType=" + useType +
                ", appPlanId=" + appPlanId +
                ", appCode='" + appCode + '\'' +
                ", isRun=" + isRun +
                ", fileType=" + fileType +
                '}';
    }
}
