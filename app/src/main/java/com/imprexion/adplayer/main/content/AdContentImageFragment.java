package com.imprexion.adplayer.main.content;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.arialyy.aria.core.Aria;
import com.google.android.exoplayer2.ui.PlayerView;
import com.imprexion.adplayer.R;
import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.main.control.VideoController;
import com.imprexion.adplayer.report.AdPlayerReport;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.adplayer.utils.Util;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.ContextUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdContentImageFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = "AdContentImageFragment";
    @BindView(R.id.iv_ad_fragment)
    ImageView ivAdFragment;
    private String mFileName;
    private String mUrl;
    private boolean mIsVideo;
    private String mStartPackageName;
    private boolean isInit;
    private boolean mIsDownLoading;
    public boolean mIsVisible;
    private boolean mIsLoop;
    private PlayerView mPlayerView;
    private VideoController mVideoController;
    private String mAdFileName;
    private VideoView mVideoView;

    public AdContentImageFragment() {
    }

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
        mIsVisible = true;
        // 当只有一条记录, 在onResume中播放, 不会走onVisible
        if (mIsLoop) {
            return;
        }

        if (mIsVideo) {
            YxLog.i(TAG, "onVisible " + mIsVideo);
            loadVideo();
        }
    }

    public void setUrl(String fileName, String url, boolean isVideo, int size, String startPackageName) {
        this.mAdFileName = fileName;
        mIsLoop = size == 1;
        mUrl = url;
        mIsVideo = isVideo;
        mStartPackageName = startPackageName;
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
        mPlayerView = view.findViewById(R.id.video_view);
        mVideoView = view.findViewById(R.id.video_view_1);
        view.setOnClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoController != null) {
            mVideoController.onPause();
        }
        if (mVideoView.isPlaying()) {
            mVideoView.stopPlayback();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsVideo) {
            YxLog.i(TAG, " playPic --> url= " + mUrl);
            Tools.showPicWithGlide(ivAdFragment, mUrl);
            mPlayerView.setVisibility(View.GONE);
            mVideoView.setVisibility(View.GONE);
            ivAdFragment.setVisibility(View.VISIBLE);
        }

        // 当只有一条视频记录时 在此时播放
        if (mIsLoop) {
            loadVideo();
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
        if (mVideoController != null) {
            mVideoController.releasePlayer();
        }
        mVideoView.stopPlayback();
    }


    public void loadVideo() {
        YxLog.i(TAG, "loadVideo mUrl= " + mUrl);
        if (mIsVideo) {
            if (mIsLoop) {
                mPlayerView.setVisibility(View.VISIBLE);
                if (mVideoController == null) {
                    mVideoController = new VideoController(mPlayerView);
                }
                mVideoController.playVideo(mUrl, mIsLoop, mFileName, ContextUtils.get());
                return;
            } else {
                mPlayerView.setVisibility(View.GONE);
            }
            ivAdFragment.setVisibility(View.GONE);
            File file = new File(mFileName);
            YxLog.i(TAG, "fileExist :  " + file.exists() + " filePath= " + file.getAbsolutePath());
            if (file.exists()) {
                mVideoView.setVideoPath(file.getAbsolutePath());
            } else {
                mVideoView.setVideoPath(mUrl);
            }
            mVideoView.setOnPreparedListener(this);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Aria.download(this).unRegister();
    }

    @Override
    public void onClick(View v) {
        YxLog.i(TAG, "packageName= " + mStartPackageName);
        if (TextUtils.isEmpty(mStartPackageName)) {
            return;
        }
        // 当包名是AIBar的包名时, 做个特殊处理,打开指定界面
        if (mStartPackageName.equals(Constants.PACKAGE_NAME_AI_BAR)) {
            Util.startAppWithActivity(getContext(), mStartPackageName, "com.imprexion.aibar.game_service.activity.MagicSalad2Activity");
            return;
        }

        if (!Util.startApp(getContext(), mStartPackageName)) {
            YxLog.e(TAG, "startAppError packageName = " + mStartPackageName);
        }
        AdPlayerReport.onClickAdPlayer(mAdFileName, mStartPackageName, mUrl);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            mp.start();
        }
    }
}
