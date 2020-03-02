package com.imprexion.adplayer.report;

import com.imprexion.adplayer.app.Constants;
import com.imprexion.library.YxStatistics;

/**
 * @author : yan
 * @date : 2019/7/26 16:16
 * @desc : TODO 应用轮播埋点类
 */
public class AdPlayerReport {

    // 轮播到广告埋点
    public static void onLoopAdplayer(String name ,String url, String packageName) {
        YxStatistics.param(Constants.AD_URL, url)
                .param(Constants.PACKAGE, packageName)
                .param(Constants.AD_NAME, name)
                .version(24)
                .report("adplayer_on_loop_ad");
    }


    // 点击广告跳转埋点
    public static void onClickAdPlayer(String name ,String packageName, String url) {
        YxStatistics.param(Constants.AD_URL, url)
                .param(Constants.PACKAGE, packageName)
                .version(24)
                .param(Constants.AD_NAME, name)
                .report("adplayer_on_click_ad");
    }

    // 轮播应用埋点 统计次数
    public static void onLoopApp(String packageName) {
        YxStatistics
                .param(Constants.PACKAGE, packageName)
                .version(26)
                .report("adplayer_on_loop_app");
    }
}
