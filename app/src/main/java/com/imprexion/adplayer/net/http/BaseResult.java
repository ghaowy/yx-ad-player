package com.imprexion.adplayer.net.http;

import java.io.Serializable;

/**
 * Created by Fandy on 2017/11/30.
 */

public class BaseResult<T> implements Serializable {

    private int code;
    private String msg;
    private String time;
    private T data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public boolean isSuccess(){
        return code == 1000 || code == 0;
    }
}
