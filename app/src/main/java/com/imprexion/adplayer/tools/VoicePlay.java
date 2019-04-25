package com.imprexion.adplayer.tools;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

import com.imprexion.adplayer.R;
import com.imprexion.library.logger.YxLogger;

import java.io.IOException;

public class VoicePlay {

    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private final static String TAG = "VoicePlay";
    private int mRainVoiceId = R.raw.rainvoice_backgroud;
    private SoundPool mSoundPool;
    private int mSoundId;

    public VoicePlay(Context context) {
        mContext = context;
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);
    }

    public void playVoice(int voiceSrcId) {//播放一次声音
        YxLogger.d(TAG, "playVoice");
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

        mMediaPlayer.setLooping(false);
        mMediaPlayer.setVolume(1f, 1f);
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                YxLogger.d(TAG, "playVoice mMediaPlayer onCompletion");
            }
        });
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
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        if (mSoundPool != null) {
            mSoundPool.release();
        }
    }

    public void playVoiceBySoundpool() {//循环播放声音
        if (mSoundPool != null && mSoundId != 0) {
            mSoundPool.resume(mSoundId);
            return;
        }
        mSoundId = mSoundPool.load(mContext, mRainVoiceId, 1);
        if (mSoundId == 0) {
            YxLogger.d(TAG, "soundpool 加载失败");
            return;
        } else {
            YxLogger.d(TAG, "soundId =" + mSoundId);
        }
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                YxLogger.d(TAG, "soundPool load complete");
                mSoundPool.play(mSoundId, 0.5f, 0.5f, 0, -1, 0.8f);
            }
        });
    }
}
