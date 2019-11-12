package com.imprexion.adplayer.app;

import android.os.Environment;

import java.io.File;

/**
 * @author : yan
 * @date : 2019/9/2 16:35
 * @desc : TODO
 */
public interface Constants {

    String FILE_BASE_DIR = Environment.getExternalStorageDirectory() + File.separator + "AdPlayer" + File.separator;


    interface Key {
        String KEY_FROM = "key_from";
        String KEY_IS_START = "is_start";
        String KEY_MESSAGE_TYPE = "messageType";
        String KEY_DATA = "data";
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
    }
}
