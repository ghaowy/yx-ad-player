package com.imprexion.adplayer.main.content;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.imprexion.library.YxImage;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.ContextUtils;
import com.sprylab.android.widget.TextureVideoView;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdContentImageFragment extends Fragment implements View.OnClickListener, MediaPlayer.OnPreparedListener {
    private static final String TAG = "AdContentImageFragment";
    private int MSG_SET_FIRST_FRAME = 1;
    private int MSG_SET_STOP_PLAY = 2;
    private int MSG_SET_START_PLAY = 3;
    private String mFileName;
    private String mUrl;
    private boolean mIsVideo;
    private String mStartPackageName;
    private boolean isInit;
    private boolean mIsDownLoading;
    public boolean mIsVisible;
    private boolean mIsLoop;
    private VideoController mVideoController;
    private String mAdFileName;
    private View mDisplayView;
    private ImageView mIvImg;
    private Bitmap mBitmap;

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
        View view = null;
        if (mIsVideo && mIsLoop) {
            view = inflater.inflate(R.layout.fragment_ad_content, container, false);
        } else if (mIsVideo) {
            view = inflater.inflate(R.layout.fragment_ad_content2, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_ad_content1, container, false);
        }
        initView(view);
        isInit = true;
        isCanLoadData();
        Aria.download(getContext()).register();
        return view;
    }

    private void initView(View view) {
        view.setOnClickListener(this);
        mDisplayView = view.findViewById(R.id.root_view);
        if (mIsVideo) {
            mIvImg = view.findViewById(R.id.iv_first_frame);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mVideoController != null) {
            mVideoController.onPause();
        }
        mHandler.sendEmptyMessage(MSG_SET_STOP_PLAY);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mIsVideo) {
            YxLog.i(TAG, " playPic --> url= " + mUrl);
            Tools.showPicWithGlide(((ImageView) mDisplayView), mUrl);
        }


        // 当只有一条视频记录时 在此时播放
        if (mIsLoop) {
            loadVideo();
        }

        if (mIsVideo && mBitmap == null) {
            YxLog.i(TAG, "mBitmap= " + mBitmap);
            File file = new File(mFileName);
            if (!file.exists()) {
                return;
            }
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(file.getAbsolutePath());

            //获取第一帧图片
            mBitmap = mmr.getFrameAtTime(2);
            mmr.release();//释放资源
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
        if (mDisplayView instanceof VideoView) {
            mHandler.sendEmptyMessage(MSG_SET_STOP_PLAY);
        }
    }


    public void loadVideo() {
        YxLog.i(TAG, "loadVideo mUrl= " + mUrl);
        if (mIsVideo && mIsLoop) {
            if (mVideoController == null) {
                mVideoController = new VideoController(((PlayerView) mDisplayView));
            }
            mVideoController.playVideo(mUrl, true, mFileName, ContextUtils.get());
            return;
        }

        mHandler.sendEmptyMessageDelayed(MSG_SET_START_PLAY, 200);

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
        try {
            // 当包名是AIBar的包名时, 做个特殊处理,打开指定界面
            if (mStartPackageName.equals(Constants.PACKAGE_NAME_AI_BAR)) {
                Util.startAppWithActivity(getContext(), mStartPackageName, "com.imprexion.aibar.game_service.activity.MagicSalad2Activity");
                return;
            }

            if (!Util.startApp(getContext(), mStartPackageName)) {
                YxLog.e(TAG, "startAppError packageName = " + mStartPackageName);
            }
            AdPlayerReport.onClickAdPlayer(mAdFileName, mStartPackageName, mUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            PlaybackParams params = mp.getPlaybackParams();
            params.setSpeed(1.0f);
            mp.start();
        }
    }


    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_SET_STOP_PLAY) {
                if (mDisplayView instanceof VideoView) {
                    ((TextureVideoView) mDisplayView).stopPlayback();
                }
            }

            if (msg.what == MSG_SET_START_PLAY) {
                mIvImg.setVisibility(View.GONE);
                File file = new File(mFileName);
                YxLog.i(TAG, "fileExist :  " + file.exists() + " filePath= " + file.getAbsolutePath());
                if (file.exists()) {
                    ((TextureVideoView) mDisplayView).setVideoPath(file.getAbsolutePath());
                } else {
                    ((TextureVideoView) mDisplayView).setVideoPath(mUrl);
                }
                ((TextureVideoView) mDisplayView).setOnPreparedListener(AdContentImageFragment.this);
            }

            if (msg.what == MSG_SET_FIRST_FRAME) {
                if (mIvImg == null) {
                    return;
                }
                mIvImg.setVisibility(View.VISIBLE);
                if (mBitmap == null) {
                    YxImage.load(R.drawable.ad_default, mIvImg);
                } else {
                    mIvImg.setImageBitmap(mBitmap);
                }
            }
        }
    };


}
