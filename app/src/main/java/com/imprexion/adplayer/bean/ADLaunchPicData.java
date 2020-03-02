package com.imprexion.adplayer.bean;

public class ADLaunchPicData {

    // 文件路径
    private String fileUrl;

    // id
    private int id;

    // 商场编码
    private String mallCode;

    // 状态(VALID=有效，INVALID=失效) ,
    private String state;


    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String pFileUrlp) {
        fileUrl = pFileUrlp;
    }

    public int getId() {
        return id;
    }

    public void setId(int pIdp) {
        id = pIdp;
    }

    public String getMallCode() {
        return mallCode;
    }

    public void setMallCode(String pMallCodep) {
        mallCode = pMallCodep;
    }

    public String getState() {
        return state;
    }

    public void setState(String pStatep) {
        state = pStatep;
    }


    @Override
    public String toString() {
        return "ADLaunchPicData{" +
                "fileUrl='" + fileUrl + '\'' +
                ", id=" + id +
                ", mallCode='" + mallCode + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
