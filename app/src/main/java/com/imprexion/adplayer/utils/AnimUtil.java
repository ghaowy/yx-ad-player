package com.imprexion.adplayer.utils;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;

/**
 * @author : yan
 * @date : 2019/8/5 17:26
 * @desc : TODO
 */
public class AnimUtil {

    public static void playObjectAnim(View view, String property, float start, float end, int duration, boolean loop) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, property, start, end);
        animator.setDuration(duration);
        if (loop) {
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setRepeatCount(ValueAnimator.INFINITE);
        }
        animator.start(); //启动
    }

    public static void clearAnim(View view) {
        if (view == null) {
            return;
        }
        view.clearAnimation();
    }

}
