package com.imprexion.aiscreen.functionPart;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imprexion.aiscreen.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class BackButtonFragment extends Fragment {


    @BindView(R.id.iv_circle_1)
    ImageView ivCircle1;
    @BindView(R.id.iv_circle_2)
    ImageView ivCircle2;
    @BindView(R.id.tv_back)
    TextView tvBack;
    Unbinder unbinder;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.iv_hover)
    ImageView ivHover;

    private ObjectAnimator mScaleXAnimator;
    private ObjectAnimator mScaleYAnimator;
    private ObjectAnimator mScaleXAnimator2;
    private ObjectAnimator mScaleYAnimator2;
    private AnimatorSet mAnimatorSet;
    private final static int DURATION = 1800;
    private final static int DELAY_TIME = 1000;
    private final static String TAG = "BackButtonFragment";

    public BackButtonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_back_button, container, false);
        unbinder = ButterKnife.bind(this, view);
        initAnimator();
        setListener(ivHover);
        return view;
    }

    private void setListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
                Log.d(TAG, "back button click");
            }
        });
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER: //鼠标进入view
                        hoverEnter();
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: //鼠标离开view
                        hoverExit();
                        break;
                }
                return false;
            }

        });
    }

    private void hoverExit() {
        if (ivHover != null && ivCircle1 != null && ivCircle2 != null && tvBack != null) {
            ivHover.setImageDrawable(null);
            ivCircle1.setVisibility(View.VISIBLE);
            ivCircle2.setVisibility(View.VISIBLE);
            tvBack.setVisibility(View.VISIBLE);
        }
    }

    private void hoverEnter() {
        ivCircle1.setVisibility(View.INVISIBLE);
        ivCircle2.setVisibility(View.INVISIBLE);
        tvBack.setVisibility(View.INVISIBLE);
        Drawable drawable1 = getResources().getDrawable(R.drawable.back_button_hover);
//                        Drawable zoomDrawable1 = Tools.zoomDrawable(drawable1, ZOOM_WIDTH, ZOOM_HEIGHT);
        ivHover.setScaleType(ImageView.ScaleType.CENTER);
        ivHover.setImageDrawable(drawable1);
    }

    private void initAnimator() {
        mAnimatorSet = new AnimatorSet();
        mScaleXAnimator = ObjectAnimator.ofFloat(ivCircle1, "ScaleX", 0.1f, 1);
        mScaleYAnimator = ObjectAnimator.ofFloat(ivCircle1, "ScaleY", 0.1f, 1);
        mScaleXAnimator2 = ObjectAnimator.ofFloat(ivCircle2, "ScaleX", 0.1f, 1);
        mScaleYAnimator2 = ObjectAnimator.ofFloat(ivCircle2, "ScaleY", 0.1f, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAnimation();
    }

    private void startAnimation() {
        mScaleXAnimator.setDuration(DURATION);
        mScaleYAnimator.setDuration(DURATION);
        mScaleXAnimator2.setDuration(DURATION);
        mScaleYAnimator2.setDuration(DURATION);
        mScaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mScaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mScaleXAnimator2.setRepeatMode(ObjectAnimator.RESTART);
        mScaleYAnimator2.setRepeatMode(ObjectAnimator.RESTART);
        mScaleXAnimator2.setStartDelay(DELAY_TIME);
        mScaleYAnimator2.setStartDelay(DELAY_TIME);
        mScaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        mScaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        mScaleXAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        mScaleYAnimator2.setRepeatCount(ObjectAnimator.INFINITE);
        mAnimatorSet.play(mScaleXAnimator).with(mScaleYAnimator);
        mAnimatorSet.start();
        mScaleXAnimator2.start();
        mScaleYAnimator2.start();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
