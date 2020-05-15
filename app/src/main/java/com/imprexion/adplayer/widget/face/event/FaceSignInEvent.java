package com.imprexion.adplayer.widget.face.event;


import com.imprexion.adplayer.widget.face.bean.SignInfoBean;

/**
 * @author : gongh
 * @date : 2020/4/14 15:43
 * @desc : TODO
 */
public class FaceSignInEvent {
    public SignInfoBean bean;

    public FaceSignInEvent(SignInfoBean bean) {
        this.bean = bean;
    }

    public SignInfoBean getBean() {
        return bean;
    }
}
