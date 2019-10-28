package com.imprexion.adplayer.app;

import android.os.Environment;

import java.io.File;

/**
 * @author : yan
 * @date : 2019/9/2 16:35
 * @desc : TODO
 */
public class Constants {
    public static final String FILE_BASE_DIR = Environment.getExternalStorageDirectory() + File.separator + "AdPlayer" + File.separator;

    public static final String KEY_FROM = "key_from";
    public static final String KEY_IS_START = "is_start";
}
