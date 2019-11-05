package com.imprexion.adplayer.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.base.ADPlayApplication;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.player.PlayerViewImp;
import com.imprexion.library.YxLog;

import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private PlayerViewImp mPlayerView;
    public boolean isResumed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        YxLog.i(TAG, "onCreate() savedInstanceState=" + savedInstanceState);
        mPlayerView = new PlayerViewImp(this);
        handleStartIntent(getIntent());
        //缓存一下当前activity的引用，可以被外部停止。
        ADPlayApplication.getInstance().pictureActivity = this;

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        YxLog.i(TAG, "onNewIntent() intent=" + intent);
        handleStartIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YxLog.i(TAG, "onResume()");
        isResumed = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        YxLog.i(TAG, "onStop()");
        isResumed = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        YxLog.i(TAG, "onPause()");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //如果页面被系统回收调用此方法缓存一下数据，这样从onRestoreInstanceState()中恢复数据
        YxLog.i(TAG, "onSaveInstanceState() savedInstanceState=" + outState);
        outState.putInt("currentPageIndex", mPlayerView.getCurPageIndex());
        outState.putSerializable("data", (Serializable) mPlayerView.getADContents());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //在此回调中恢复轮播的index和List数据。
        YxLog.i(TAG, "onRestoreInstanceState() savedInstanceState=" + savedInstanceState);
        int currentPageIndex = savedInstanceState.getInt("currentPageIndex", 0);
        List<ADContentInfo> adContents = (List<ADContentInfo>) savedInstanceState.getSerializable("data");
        mPlayerView.setCurPageIndex(currentPageIndex);
        refreshAdDatas(adContents);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        YxLog.i(TAG, "onDestroy()");
        ADPlayApplication.getInstance().pictureActivity = null;
    }

    private void handleStartIntent(Intent intent) {
        if (intent == null) {
            YxLog.i(TAG, "handleStartIntent() intent=null");
            return;
        }
        String messageType = intent.getStringExtra("messageType");
        if ("playNext".equals(messageType)) {
            List<ADContentInfo> data = intent.getParcelableArrayListExtra("data");
            boolean isNewData = intent.getBooleanExtra("isNewData", true);
            //如果需要刷新数据播放，则先替换数据，然后从index = 0，播放第一个页面；否则直接播放下一个。
            if (isNewData) {
                YxLog.d(TAG, "handleStartIntent() messageType=playNext, isNewData=" + isNewData + ",need to refresh data.");
                refreshAdDatas(data);
            } else if (mPlayerView.getADContents() == null || mPlayerView.getADContents().size() == 0) {
                YxLog.d(TAG, "handleStartIntent() messageType=playNext, isNewData=" + isNewData
                        + ",mPlayerView data is empty,need to refresh data.");
                refreshAdDatas(data);
            } else {
                YxLog.d(TAG, "handleStartIntent() messageType=playNext, newData=null,call playNext() directly.");
                playNext();
            }
        } else {
            YxLog.d(TAG, "handleStartIntent() messageType=" + messageType);
        }
    }

    private void playNext() {
        if (mPlayerView != null) {
            mPlayerView.playerNext();
        }
    }

    private void refreshAdDatas(List<ADContentInfo> newData) {
        try {
            mPlayerView.updateAds(newData);
            playNext();
        } catch (Exception e) {
            e.printStackTrace();
            YxLog.i(TAG, "refreshAdDatas() refresh ad data error. e=" + e.getCause());
        }
    }
}
