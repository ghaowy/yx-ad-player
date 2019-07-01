package com.imprexion.adplayer.net.http;

/**
 * Created by Fandy on 2017/12/26.
 */

public final class ErrCode {

    public static final int PAY_FAILED = 2001;

    public static final int REQUEST_TIMEOUT = 1004;

    //server error code
    public static final int SUCCESS = 1;
    public static final int UNKNOWN_ERROR = -1;
    public static final int INVALID_VOUCHER = 100;
    public static final int INVALID_PARAMS = 101;
    public static final int INVALID_SIGNATURE = 102;
    public static final int INVALID_USER_ID = 103;
    public static final int INVALID_ACCESS_TOKEN = 104;
    public static final int USER_NOT_SEE = 210;
    public static final int NOT_EXIST_OBJECT = 211;
    public static final int DUPLICATE_SUMMIT = 230;
    public static final int SERVER_ERROR = 400;

    public static String getMessage(int code){
        switch (code){
            case UNKNOWN_ERROR:
                return "Unknown error.";
            case INVALID_VOUCHER:
                return "Invalid voucher(无效凭证).";
            case INVALID_PARAMS:
                return "Invalid params(无效参数).";
            case INVALID_SIGNATURE:
                return "Invalid signature(无效签名).";
            case INVALID_USER_ID:
                return "Invalid user id(无效的user id).";
            case INVALID_ACCESS_TOKEN:
                return "Invalid access token.";
            case USER_NOT_SEE:
                return "用户不可见.";
            case NOT_EXIST_OBJECT:
                return "对象不存在.";
            case DUPLICATE_SUMMIT:
                return "重复提交.";
            case SERVER_ERROR:
                return "请求服务出错.";
            default:
                return "Unknown error.";
        }
    }
}
