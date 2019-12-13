package com.imprexion.adplayer.main.control;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.imprexion.library.YxLog;
import com.imprexion.library.util.ContextUtils;

import java.io.File;

/**
 * @author : yan
 * @date : 2019/11/29 15:50
 * @desc : TODO 视频播放控制类
 */
public class VideoController implements Player.EventListener {

    private static final String TAG = "VideoController";
    private PlayerView mPlayerView;
    private SimpleExoPlayer player;

    public VideoController(PlayerView playerView) {
        mPlayerView = playerView;
    }

    public void onPause() {
        if (mPlayerView == null || mPlayerView.getPlayer() == null) {
            return;
        }
        YxLog.i(TAG, " onPause== ");
        mPlayerView.getPlayer().stop();
    }

    public void releasePlayer() {
        YxLog.i(TAG, "releasePlayer");
        if (player != null) {
            player.release();
            player.removeListener(this);
            player = null;
        }
    }

    public void playVideo(String url, boolean isLoop, String fileName, Context context) {
        YxLog.i(TAG, "url--> " + url + " isLoop--> " + isLoop + " fileName= " + fileName);
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
            player.addListener(this);

            mPlayerView.setPlayer(player);

            player.setPlayWhenReady(true);
            player.setRepeatMode(isLoop ? Player.REPEAT_MODE_ONE : Player.REPEAT_MODE_OFF);
        }
        File file = new File(fileName);
        YxLog.i(TAG, "fileExist :  " + file.exists() + " filePath= " + file.getAbsolutePath());
        Uri mp4VideoUri = null;
        if (file.exists()) {
            mp4VideoUri = Uri.fromFile(file.getAbsoluteFile());
        } else {
            mp4VideoUri = Uri.parse(url);
        }
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                Util.getUserAgent(context, ContextUtils.getPackageName()));
        //这是一个代表将要被播放的媒体的MediaSource
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(mp4VideoUri);
        player.prepare(videoSource);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        String stateString;
        // actually playing media
        if (playWhenReady && playbackState == Player.STATE_READY) {
            YxLog.i(TAG, "onPlayerStateChanged: actually playing media");
        }
        switch (playbackState) {
            case Player.STATE_IDLE:
                stateString = "ExoPlayer.STATE_IDLE      -";
                break;
            case Player.STATE_BUFFERING:
                stateString = "ExoPlayer.STATE_BUFFERING -";
                break;
            case Player.STATE_READY:
                stateString = "ExoPlayer.STATE_READY     -";
                break;
            case Player.STATE_ENDED:
                stateString = "ExoPlayer.STATE_ENDED     -";
                break;
            default:
                stateString = "UNKNOWN_STATE             -";
                break;
        }
        YxLog.i(TAG, "stateChange= stateString + " + stateString);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        YxLog.i(TAG, "ERROR " + error.getLocalizedMessage());
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
