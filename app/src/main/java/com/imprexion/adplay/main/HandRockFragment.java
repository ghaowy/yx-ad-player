package com.imprexion.adplay.main;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.imprexion.adplay.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HandRockFragment extends Fragment {


    @BindView(R.id.iv_rock_hand)
    ImageView ivRockHand;
    Unbinder unbinder;

    public HandRockFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hand_rock, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        startAnimation();
    }

    private void startAnimation() {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(ivRockHand, "Rotation", -10, 30, -10);
        ivRockHand.setPivotX(65);
        ivRockHand.setPivotY(130);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setDuration(1500);
        objectAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        objectAnimator.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
