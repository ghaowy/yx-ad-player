package com.imprexion.adplayer.video;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.dou361.ijkplayer.widget.IjkVideoView;
import com.imprexion.library.YxLog;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * @author : yan
 * @date : 2019/10/12 17:30
 * @desc : TODO
 */
public class VideoPlayerPresenter implements IMediaPlayer.OnPreparedListener, IMediaPlayer.OnErrorListener {

    private static final String TAG = "VideoPlayerPresenter";
    private IjkVideoView mIjkVideoView;

    public VideoPlayerPresenter(Context context, RelativeLayout rootView) {
        IjkVideoView ijkVideoView = initIjkView(context);
        rootView.addView(ijkVideoView);

    }

    @NonNull
    private IjkVideoView initIjkView(Context context) {
        mIjkVideoView = new IjkVideoView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mIjkVideoView.setLayoutParams(layoutParams);
        mIjkVideoView.setOnPreparedListener(this);
        mIjkVideoView.setOnErrorListener(this);
        return mIjkVideoView;
    }

    public void setVideoPath(String path) {
        if (TextUtils.isEmpty(path)) {
            Log.e(TAG, "VideoPath is Null");
        }

        mIjkVideoView.setVideoPath(path);
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        if (iMediaPlayer != null) {
            iMediaPlayer.start();
        }
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
        YxLog.i(TAG ,"VideoPlayerPresenter --> onError i= " + i + " i1= " + i1);
        return false;
    }

    public void pause() {
        if (mIjkVideoView == null) {
            return;
        }
        mIjkVideoView.pause();
    }
}
