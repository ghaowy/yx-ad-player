package com.imprexion.adplayer.widget.face.bean;

/**
 * @author hardy
 * @name yx-ad-player
 * @class name：com.imprexion.adplayer.widget.face.bean
 * @class describe:
 * @time 2020/5/13 16:30
 * @change
 * @chang time
 * @class describe
 */
public class SignInfoBean {
   int code;
    String userAvatarUrl;
    String accountToken;
    String phone;
    String userID;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getUserAvatarUrl() {
        return userAvatarUrl;
    }

    public void setUserAvatarUrl(String userAvatarUrl) {
        this.userAvatarUrl = userAvatarUrl;
    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
