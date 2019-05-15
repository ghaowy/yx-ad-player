package com.imprexion.adplayer.tools;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.imprexion.adplayer.R;
import com.imprexion.library.YxLog;

import java.io.IOException;

public class VoicePlay {

    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private final static String TAG = "VoicePlay";
    public final static int SOUNDPOOL = 1;
    public final static int MEDIAPLAYER = 2;
    private int mRainVoiceId = R.raw.rainvoice_backgroud;
    private SoundPool mSoundPool;
    private int mSoundId;

    public VoicePlay(Context context, int type) {
        mContext = context;
        if (type == SOUNDPOOL) {
            mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);
        }
    }

    public void playVoice(int voiceSrcId) {//播放一次声音
        YxLog.d(TAG, "playVoice");
        if (mSoundPool != null) {
            mSoundPool.pause(mSoundId);
        }
        mMediaPlayer = MediaPlayer.create(mContext, voiceSrcId);

        try {
            mMediaPlayer.stop();
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
    }

    public void pause() {
        if (mMediaPlayer != null) {
            mMediaPlayer.pause();
        }
        if (mSoundPool != null && mSoundId != 0) {
            mSoundPool.pause(mSoundId);
        }
    }

    public void stop() {
        YxLog.d(TAG, "voicePlay stop and release");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
//            mMediaPlayer = null;
            YxLog.d(TAG, "mMediaPlayer release");
        }
        if (mSoundPool != null) {
            mSoundPool.stop(mSoundId);
            mSoundPool.release();
//            mSoundPool = null;
            YxLog.d(TAG, "mSoundPool release");
        }
    }

    public void playVoiceBySoundpool() {//循环播放声音
        if (mSoundPool != null && mSoundId != 0) {
            mSoundPool.resume(mSoundId);
            return;
        }
        mSoundId = mSoundPool.load(mContext, mRainVoiceId, 1);
        if (mSoundId == 0) {
            YxLog.d(TAG, "soundpool 加载失败");
            return;
        } else {
            YxLog.d(TAG, "soundId =" + mSoundId);
        }
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                YxLog.d(TAG, "soundPool load complete");
                mSoundPool.play(mSoundId, 0.5f, 0.5f, 0, -1, 0.8f);
            }
        });
    }

    public void playVoiceBySoundpoolOnce(int soundId) {//播放声音一次  不循环
        mSoundId = mSoundPool.load(mContext, soundId, 1);
        if (mSoundId == 0) {
            YxLog.d(TAG, "soundpool 加载失败");
            return;
        } else {
            YxLog.d(TAG, "soundId =" + mSoundId);
        }
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                YxLog.d(TAG, "soundPool load complete");
                mSoundPool.play(mSoundId, 1f, 1f, 0, 0, 1);
            }
        });
    }
}
