package com.imprexion.adplayer.main.activation;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Message;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.bean.TrackingMessage;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.adplayer.tools.VoicePlay;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActiveTipView extends FloatView {


    @BindView(R.id.tv_hands_active_text_app)
    TextView tvHandsActiveTextApp;
    private Message mMessage;
    private ObjectAnimator mHandsActiveAnimator;
    private Unbinder mUnbinder;
    private static final String TAG = "ActiveFootPrintView";
    private TrackingMessage mTrackingMessage;


    public ActiveTipView(Context context) {
        View view = setFloatViewLayout(R.layout.active_tip);
        mUnbinder = ButterKnife.bind(this, view);
        mHandsActiveAnimator = ObjectAnimator.ofFloat(tvHandsActiveTextApp, "translationX", 0, -40);

    }

    public void showActiveTip() {
        Tools.fadeIn(tvHandsActiveTextApp, 500);
//        EventBus.getDefault().register(this);
        startActiveTipAnimation();
        showFloatView();
    }

    public void dismissActiveTip() {
//        EventBus.getDefault().unregister(this);
//        mUnbinder.unbind();
        if (mHandsActiveAnimator.isRunning()) {
            mHandsActiveAnimator.cancel();
        }
        dismissWindow();
    }

    private void startActiveTipAnimation() {
        if (!mHandsActiveAnimator.isRunning()) {
            mHandsActiveAnimator.setDuration(800);
            mHandsActiveAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mHandsActiveAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mHandsActiveAnimator.start();
        }
    }

}
