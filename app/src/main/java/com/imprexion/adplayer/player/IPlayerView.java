package com.imprexion.adplayer.player;

import com.imprexion.adplayer.bean.ADContentInfo;

import java.util.List;

/**
 * Author: Xianquan Feng
 * Email: xianquan.feng@imprexion.cn
 * Date: 2019/6/28
 * Desc:
 */
public interface IPlayerView {

    // 播放下一个
    void playerNext();

    // 更新广告数据
    void updateAds(List<ADContentInfo> adContentPlays);
}
