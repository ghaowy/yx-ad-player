package com.imprexion.adplayer.main.content;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.adplayer.video.VideoPlayerPresenter;
import com.imprexion.library.YxLog;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

import static com.imprexion.adplayer.base.ADPlayApplication.getProxy;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdContentImageFragment extends Fragment implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener {


    private static final String TAG = "AdContentImageFragment";
    @BindView(R.id.iv_ad_fragment)
    ImageView ivAdFragment;

    private String mUrl;
    private boolean mIsVideo;
    private boolean isInit;
    private RelativeLayout mRlContainer;
    private VideoPlayerPresenter mVideoPlayerPresenter;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    private void isCanLoadData() {
        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    private void onInVisible() {

    }

    private void onVisible() {
        if (mIsVideo) {
            YxLog.i(TAG, "onVisible " + mIsVideo);
            loadVideo();
        } else {
            YxLog.i(TAG, " playPic --> url= " + mUrl);
            Tools.showPicWithGlide(ivAdFragment, mUrl);
            mRlContainer.setVisibility(View.GONE);
            ivAdFragment.setVisibility(View.VISIBLE);
        }
    }

    public AdContentImageFragment() {
        // Required empty public constructor
    }

    public void setUrl(String url, boolean isVideo) {
        mUrl = url;
        mIsVideo = isVideo;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_content, container, false);
        initView(view);
        isInit = true;
        isCanLoadData();
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
        mVideoPlayerPresenter.release();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        YxLog.i(TAG, " onAttach");
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        YxLog.i(TAG, " onAttachFragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        YxLog.i(TAG, "onDetach");
        if (mVideoPlayerPresenter != null) {
            mVideoPlayerPresenter.release();
        }
    }

    public void loadVideo() {
        YxLog.i(TAG, "loadVideo mUrl= " + mUrl);
        if (mIsVideo) {
//            mIjkPlayView.setVisibility(View.VISIBLE);
            ivAdFragment.setVisibility(View.GONE);
            mVideoPlayerPresenter = new VideoPlayerPresenter(mRlContainer);
            mVideoPlayerPresenter.setVideoPath(getProxy(getContext()).getProxyUrl(mUrl));
//            mIjkPlayView.setVideoPath(getProxy(getContext()).getProxyUrl(mUrl));
//            mIjkPlayView.setOnPreparedListener(this);
//            mIjkPlayView.setOnCompletionListener(this);
//            mIjkPlayView.setOnErrorListener(this);
        }
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        YxLog.i(TAG, "onPrepared murl= " + mUrl);
        if (iMediaPlayer != null) {
            iMediaPlayer.start();
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        YxLog.i(TAG, "onError--> i= " + i + " i1= " + i1);
        return false;
    }
}
