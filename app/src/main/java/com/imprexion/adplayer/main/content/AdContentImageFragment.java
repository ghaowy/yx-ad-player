package com.imprexion.adplayer.main.content;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTask;
import com.dou361.ijkplayer.widget.IjkVideoView;
import com.imprexion.adplayer.R;
import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.YxLog;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import butterknife.BindView;
import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdContentImageFragment extends Fragment implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener {


    private static final String TAG = "AdContentImageFragment";
    @BindView(R.id.iv_ad_fragment)
    ImageView ivAdFragment;

    private String mFileName;
    private String mUrl;
    private boolean mIsVideo;
    private boolean isInit;
    private IjkVideoView mIjkPlayView;
    private boolean mIsDownLoading;
    public boolean mIsVisible;


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
        mIsVisible = false;
    }

    private void onVisible() {
        if (mIsVideo) {
            YxLog.i(TAG, "onVisible " + mIsVideo);
            loadVideo();
        }
        mIsVisible = true;
    }

    public AdContentImageFragment() {
        // Required empty public constructor
    }

    public void setUrl(String url, boolean isVideo) {
        mUrl = url;
        mIsVideo = isVideo;
    }

    // 更新皮肤数据
    private void downLoadData() {
        if (TextUtils.isEmpty(mUrl) || mIsDownLoading || !mIsVideo) {
            return;
        }
        if (mUrl.contains("?")) {
            mFileName = mUrl.substring(0, mUrl.indexOf("?"));
        }
        if (mFileName.contains("/")) {
            mFileName = mFileName.substring(mFileName.lastIndexOf("/"));
        }

        mFileName = Constants.FILE_BASE_DIR + mFileName;
        File file = new File(mFileName);
        YxLog.i(TAG, " filePath = " + mFileName);
        if (file.exists()) {
            YxLog.i(TAG, " file is exist");
            return;
        }
        Aria.download(this)
                .load(mUrl)     //读取下载地址
                .setFilePath(mFileName) //设置文件保存的完整路径
                .start();   //启动下载
        mIsDownLoading = true;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_content, container, false);
        initView(view);
        isInit = true;
        isCanLoadData();
        Aria.download(getContext()).register();
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
        downLoadData();
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
            mIjkPlayView.setOnPreparedListener(this);
            mIjkPlayView.setOnErrorListener(this);
            File file = new File(mFileName);
            YxLog.i(TAG, "fileExist :  " + file.exists() + " filePath= " + file.getAbsolutePath());
            if (file.exists()) {
                mIjkPlayView.setVideoPath(file.getAbsolutePath());
            } else {
                mIjkPlayView.setVideoPath(mUrl);
            }
        }
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        YxLog.i(TAG, "onPrepared = iMediaPlayer= " + iMediaPlayer);
        if (iMediaPlayer != null) {
            iMediaPlayer.start();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        YxLog.i(TAG, "onError i= " + i + " i1= " + i1);
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Aria.download(this).unRegister();
    }

    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    public void running(DownloadTask task) {
        int percent = task.getPercent();
        mIsDownLoading = false;
        YxLog.d(TAG, "downLoad Presenter --> " + percent);
    }


    @Download.onTaskComplete
    public void taskComplete(DownloadTask task) {
        mIsDownLoading = false;
        //在这里处理任务完成的状态
        YxLog.i(TAG, "downLoad complete filePath--> " + (task == null ? "is null" : task.getFilePath()));
        if (mIsVisible) {
            loadVideo();
        }
    }

    @Download.onTaskFail
    public void taskFailed(DownloadTask task) {
        //在这里处理任务完成的状态
        mIsDownLoading = false;
        YxLog.i(TAG, "downLoad taskFailed filePath--> " + (task == null ? "is null" : task.getFilePath()));
    }
}
