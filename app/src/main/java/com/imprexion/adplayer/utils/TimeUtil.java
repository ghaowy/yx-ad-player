package com.imprexion.adplayer.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;

/**
 * @author : yan
 * @date : 2019/9/17 15:56
 * @desc : TODO 时间工具类
 */
public class TimeUtil {
    public static final String DEFAULT_FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

    // 将日期格式转换为 字符串格式
    public static long parserDateTime(String endTime, String format) {
        if (TextUtils.isEmpty(format)) {
            format = DEFAULT_FORMAT_DATE;
        }
        try {
            if (!TextUtils.isEmpty(endTime)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                return dateFormat.parse(endTime).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
