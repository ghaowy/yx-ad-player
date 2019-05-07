package com.imprexion.adplayer.main.content;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.tools.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdContentImageFragment extends Fragment {


    @BindView(R.id.iv_ad_fragment)
    ImageView ivAdFragment;
    Unbinder unbinder;

    private String mUrl;

    public AdContentImageFragment() {
        // Required empty public constructor
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad_content, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Tools.showPicWithGlide(ivAdFragment, mUrl);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
