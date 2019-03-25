package com.imprexion.aiscreen.functionPart;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    private ObjectAnimator mScaleXAnimator;
    private ObjectAnimator mScaleYAnimator;
    private ObjectAnimator mScaleXAnimator2;
    private ObjectAnimator mScaleYAnimator2;
    private AnimatorSet mAnimatorSet;

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
        return view;
    }

    private void initAnimator() {
        mAnimatorSet = new AnimatorSet();
        mScaleXAnimator = ObjectAnimator.ofFloat(ivCircle1, "ScaleX", 0.01f, 1);
        mScaleYAnimator = ObjectAnimator.ofFloat(ivCircle1, "ScaleY", 0.01f, 1);
        mScaleXAnimator2 = ObjectAnimator.ofFloat(ivCircle2, "ScaleX", 0.01f, 1);
        mScaleYAnimator2 = ObjectAnimator.ofFloat(ivCircle2, "ScaleY", 0.01f, 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        startAnimation();
    }

    private void startAnimation() {
        mScaleXAnimator.setDuration(2000);
        mScaleYAnimator.setDuration(2000);
        mScaleXAnimator2.setDuration(2000);
        mScaleYAnimator2.setDuration(2000);
        mScaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mScaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
        mScaleXAnimator2.setRepeatMode(ObjectAnimator.RESTART);
        mScaleYAnimator2.setRepeatMode(ObjectAnimator.RESTART);
        mScaleXAnimator2.setStartDelay(1000);
        mScaleYAnimator2.setStartDelay(1000);
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
