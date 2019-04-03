package com.imprexion.aiscreen.advertising.content;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.imprexion.aiscreen.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CameraRainFragment extends Fragment {


    public CameraRainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_camera_rain, container, false);
    }

}
