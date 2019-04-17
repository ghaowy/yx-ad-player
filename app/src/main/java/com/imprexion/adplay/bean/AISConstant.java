package com.imprexion.adplay.bean;

public class AISConstant {
    /**
     * 推荐信息的优先级CONTENT_PRIORITY
     */
    public final static int APP_PREEMPT = 0;    //霸屏类app
    public final static int APP_GUIDE = 1;    //引导类app
    public final static int APP_RECOMM = 2;    //推荐类app
    public final static int APP_DEFAULT = 9;  //默认类app
    public final static int ADS_PRIORITY = 110;      // 紧急插播(优先级别最高)
    public final static int ADS_BA_PING = 115;       // 霸屏
    public final static int ADS_SCHEDULED = 120;      // 定时播放
    public final static int ADS_ACCURATE = 130;      // 精准推荐
    public final static int ADS_CAROUSEL = 140;      // 普通轮播
    public final static int ADS_DEFAULT = 190;        // 广告默认级别

    /**
     * 推荐信息类型ContentType
     */
    public final static int PIC = 0;      // 图片
    public final static int MP4 = 1;      // 视频
    public final static int TEXT = 2;     // 文字
    public final static int APP_T = 3;    // 应用
}
