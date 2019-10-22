package com.imprexion.adplayer.main.content;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.adplayer.video.IjkPlayView;
import com.imprexion.library.YxLog;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdContentImageFragment extends Fragment implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener {


    private static final String TAG = "AdContentImageFragment";
    @BindView(R.id.iv_ad_fragment)
    ImageView ivAdFragment;

    private String mUrl;
    private boolean mIsVideo;
    private boolean isInit;
    private IjkPlayView mIjkPlayView;


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
        mIjkPlayView = view.findViewById(R.id.ijk_video_view);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mIjkPlayView == null) {
            return;
        }
        mIjkPlayView.stopPlayback();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsVideo) {
            YxLog.i(TAG, " playPic --> url= " + mUrl);
            Tools.showPicWithGlide(ivAdFragment, mUrl);
            mIjkPlayView.setVisibility(View.GONE);
            ivAdFragment.setVisibility(View.VISIBLE);
        }
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
        if (mIjkPlayView != null) {
            mIjkPlayView.release(true);
        }
    }

    public void loadVideo() {
        YxLog.i(TAG, "loadVideo mUrl= " + mUrl);
        if (mIsVideo) {
            ivAdFragment.setVisibility(View.GONE);
            mIjkPlayView.setVisibility(View.VISIBLE);
            mIjkPlayView.setVideoPath(mUrl);
            mIjkPlayView.setOnPreparedListener(this);
            mIjkPlayView.setOnErrorListener(this);
        }
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        YxLog.i(TAG ,"onPrepared = iMediaPlayer= " + iMediaPlayer);
        if (iMediaPlayer != null) {
            iMediaPlayer.start();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        YxLog.i(TAG, "onError i= " + i + " i1= " + i1);
        return false;
    }
}
