package com.imprexion.adplayer.player;

import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.bean.ADContentPlay;

import java.util.List;

/**
 * Author: Xianquan Feng
 * Email: xianquan.feng@imprexion.cn
 * Date: 2019/6/28
 * Desc:
 */
public interface IPlayerView {

    void playerNext();

    void updateAds(List<ADContentInfo> adContentPlays);
}
