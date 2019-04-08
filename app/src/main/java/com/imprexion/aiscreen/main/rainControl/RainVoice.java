package com.imprexion.aiscreen.main.rainControl;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import com.imprexion.aiscreen.tools.ALog;

import com.imprexion.aiscreen.R;

import java.io.IOException;

public class RainVoice {

    private MediaPlayer mMediaPlayer;
    private Context mContext;
    private final static String TAG = "RainVoice";
    private int mRainVoiceId = R.raw.rainvoice_2;
    private SoundPool mSoundPool;
    private int mSoundId;

    public RainVoice(Context context) {
        mContext = context;
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);
    }

    public void playRainDropVoice() {
        ALog.d(TAG, "playRainDropVoice");
        if (mSoundPool != null) {
            mSoundPool.pause(mSoundId);
        }
        mMediaPlayer = MediaPlayer.create(mContext, R.raw.raindropvoice_3);

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
                ALog.d(TAG, "playRainDropVoice mMediaPlayer onCompletion");
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

    public void playRainVoiceBySoundpool() {
        if (mSoundPool != null && mSoundId != 0) {
            mSoundPool.resume(mSoundId);
            return;
        }
        mSoundId = mSoundPool.load(mContext, mRainVoiceId, 1);
        if (mSoundId == 0) {
            ALog.d(TAG, "soundpool 加载失败");
            return;
        } else {
            ALog.d(TAG, "soundId =" + mSoundId);
        }
        mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                ALog.d(TAG, "soundPool load complete");
                mSoundPool.play(mSoundId, 0.5f, 0.5f, 0, -1, 0.8f);
            }
        });
    }
}
