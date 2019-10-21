package com.imprexion.adplayer.view;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author : yan
 * @date : 2019/8/22 19:13
 * @desc : TODO
 */
public class NoTouchLayout extends ConstraintLayout {
    public NoTouchLayout(Context context) {
        this(context , null);
    }

    public NoTouchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
