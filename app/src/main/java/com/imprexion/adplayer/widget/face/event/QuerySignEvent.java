package com.imprexion.adplayer.widget.face.event;


import com.imprexion.adplayer.widget.face.bean.SignInfoBean;

/**
 * @author : gongh
 * @date : 2020/4/14 15:40
 * @desc : TODO
 */
public class QuerySignEvent {
    public SignInfoBean bean;

    public QuerySignEvent(SignInfoBean bean) {
        this.bean = bean;
    }

    public SignInfoBean getBean() {
        return bean;
    }
}
