package com.imprexion.adplayer.app;

import android.os.Environment;

import java.io.File;

/**
 * @author : yan
 * @date : 2019/9/2 16:35
 * @desc : TODO
 */
public interface Constants {

    String FILE_BASE_DIR = Environment.getExternalStorageDirectory() + File.separator + "imprexion" + File.separator + "adplayer" + File.separator + "video";
    String TYPE_GETURE = "geture";
    String TYPE_NO_OPERATION = "no_operation";
    String TYPE_TOUCH = "touch";
    String TYPE_PUSH_DATA = "get_push_data";
    String TYPE_COUNT_DOWN = "count_down";
    String AD_CURRENT = "ADContentCurrent";
    String AD_INDEX = "mCurrentPage";
    String AD_URL = "ad_url";
    String PACKAGE = "package";
    String AD_NAME = "ad_name";


    interface Key {
        String KEY_FROM = "key_from";
        String KEY_IS_START = "is_start";
        String KEY_MESSAGE_TYPE = "messageType";
        String KEY_DATA = "data";
        String KEY_TIME = "time";
        String KEY_IS_NEW_DATA = "isNewData";
        String KEY_IS_VIDEO = "isVideo";
        String KEY_CONTENT_TYPE = "contentType";
        String kEY_PACKAGE_NAME = "packageName";
        String KEY_IS_FIRST = "is_first";
        String KEY_PLAY_NEXT = "playNext";
        String KEY_CURRENT_INDEX = "currentPageIndex";
    }


    interface Action {
        // 推送消息Action
        String PUSH_MESSAGE = "com.imprexion.push.MESSAGE";
        // window services 传递的touch Action
        String EVENT_TOUCH = "com.imprexion.action.EVENT_TOUCH";
        String EVENT_GESTURE = "com.imprexion.action.EVENT_GESTURE";
        // 霸屏应用 霸屏时传递消息的Action
        String LOOP_EVENT = "com.imprexion.adplayer.LOOP_EVENT";
        // 外部接口实现修改轮播停留时间
        String EVENT_COUNT_DOWN = "com.imprexion.aibar.action.USER_LOGOUT_COUNT_DOWN";
        String EVENT_NO_OPERATION = "com.imprexion.action.EVENT_NO_OPERATION";
        // 播放下一个应用的action
        String BROADCAST_PLAY_NEXT_APP = "com.imprexion.action.PLAY_APP";
    }


}
