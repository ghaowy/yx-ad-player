package com.imprexion.adplayer.player;

/**
 * @author : yan
 * @date : 2019/11/27 14:58
 * @desc : TODO 控制类接口
 */
public interface IControl {
    // 拉取数据
    void loadData();

    // 处理services消息
    void handleEvent(String messageType, String data);

    // 播放下一个
    void playNext();
}
