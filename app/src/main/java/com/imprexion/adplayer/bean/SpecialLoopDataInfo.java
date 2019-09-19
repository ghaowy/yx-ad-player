package com.imprexion.adplayer.bean;

/**
 * @author : yan
 * @date : 2019/9/17 15:22
 * @desc : TODO 特别轮播对象
 */
public class SpecialLoopDataInfo {
    // 应用包名
    private String appCode;//应用编码

    private String name;//名称

    private String startTime;//开始时间

    private String endTime;//结束时间

    private String state;//状态 有效VALID 失效INVALID

    private String stateName;//状态名称 有效VALID 失效INVALID

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return "SpecialLoopDataInfo{" +
                "appCode='" + appCode + '\'' +
                ", name='" + name + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", state='" + state + '\'' +
                ", stateName='" + stateName + '\'' +
                '}';
    }
}
