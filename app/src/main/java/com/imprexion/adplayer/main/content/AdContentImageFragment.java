package com.imprexion.adplayer.main.content;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.danikula.videocache.HttpProxyCacheServer;
import com.imprexion.adplayer.R;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.adplayer.video.VideoPlayerPresenter;
import com.imprexion.library.YxLog;

import butterknife.BindView;

import static com.imprexion.adplayer.base.ADPlayApplication.getProxy;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdContentImageFragment extends Fragment {


    private static final String TAG = "AdContentImageFragment";
    @BindView(R.id.iv_ad_fragment)
    ImageView ivAdFragment;

    private String mUrl;
    private boolean mIsVideo;
    private RelativeLayout mRlContainer;
    private VideoPlayerPresenter mVideoPlayerPresenter;
    private HttpProxyCacheServer mProxy;

    public AdContentImageFragment() {
        // Required empty public constructor
    }

    public void setUrl(String url, boolean isVideo) {
        mUrl = url;
        mIsVideo = isVideo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ad_content, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        ivAdFragment = view.findViewById(R.id.iv_ad_fragment);
        mRlContainer = view.findViewById(R.id.rl_container);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoPlayerPresenter == null) {
            return;
        }
        mVideoPlayerPresenter.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsVideo) {
//            mUrl = "http://9890.vod.myqcloud.com/9890_9c1fa3e2aea011e59fc841df10c92278.f20.mp4";
            YxLog.i(TAG ," playVideo --> url= " + mUrl);
            if (mVideoPlayerPresenter == null) {
                mVideoPlayerPresenter = new VideoPlayerPresenter(getContext(), mRlContainer);
            }
            mProxy = getProxy(getContext());
//            String proxyUrl = mProxy.getProxyUrl(mUrl);
            mVideoPlayerPresenter.setVideoPath(mUrl);
        } else {
            Tools.showPicWithGlide(ivAdFragment, mUrl);
        }
    }
}
