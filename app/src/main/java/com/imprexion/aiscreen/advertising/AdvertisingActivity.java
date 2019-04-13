package com.imprexion.aiscreen.advertising;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.imprexion.aiscreen.advertising.activation.GestureActiveFragment;
import com.imprexion.aiscreen.tools.ALog;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.bean.ContentPlay;
import com.imprexion.aiscreen.bean.EventBusMessage;
import com.imprexion.aiscreen.tools.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvertisingActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_ad)
    ImageView ivAd;
    @BindView(R.id.iv_ad2)
    ImageView ivAd2;
    @BindView(R.id.rl_advertising)
    RelativeLayout rlAdvertising;
    @BindView(R.id.fl_fragment)
    FrameLayout flFragment;

    private List<String> mImges = new ArrayList<>();
    private String mAdpath = Environment.getExternalStorageDirectory() + "/imprexion_ad";
    private final static String TAG = "AdvertisingActivity";
    private Message mMessage;
    private Thread mThread;
    private final static int NEXT_AD = 1;
    private final static int NEXT_AD_2 = 2;
    private final static int REMOVE_FRAGMENT = 3;
    private ObjectAnimator mAdEnterObjAnimator;
    private ObjectAnimator mAdExitObjAnimator;
    private ObjectAnimator mAdEnterObjAnimator_1;
    private ObjectAnimator mAdExitObjAnimator_1;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private AnimatorSet mAnimatorSet_2 = new AnimatorSet();
    private int i;
    private ContentPlay mContentPlay;
    private List<ContentPlay> mContentPlayList;
    private boolean isTimeToShowAd;
    private long startTime;
    private String[] picUrl = {"https://cn.bing.com/th?id=OHR.SakuraFes_ZH-CN1341601988_1920x1080.jpg&rf=NorthMale_1920x1080.jpg"};
    private String[] picUrl1 = {"https://cn.bing.com/sa/simg/hpb/NorthMale_EN-US8782628354_1366x768.jpg"};
    private String[] picUrl2 = {"https://cn.bing.com/th?id=OHR.AthensNight_ZH-CN1280970241_1920x1080.jpg&rf=NorthMale_1920x1080.jpg"};
    private List<String[]> mList = new ArrayList<>();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NEXT_AD:
                    mPicUrl = ((ContentPlay) msg.obj).getContentInfo().getContentUrl();
                    Glide.with(AdvertisingActivity.this)
//                            .load(mAdpath + "/" + mImges.get(i++ % mImges.size()))
                            .load(mPicUrl[0])
                            .into(ivAd);
                    mAnimatorSet.play(mAdEnterObjAnimator).with(mAdExitObjAnimator_1);
                    mAnimatorSet.setDuration(300);
                    mAnimatorSet.setInterpolator(new DecelerateInterpolator());
                    mAnimatorSet.start();
                    break;
                case NEXT_AD_2:
                    mPicUrl = ((ContentPlay) msg.obj).getContentInfo().getContentUrl();
                    Glide.with(AdvertisingActivity.this)
//                            .load(mAdpath + "/" + mImges.get(i++ % mImges.size()))
                            .load(picUrl[0])
                            .into(ivAd2);
                    mAnimatorSet_2.play(mAdEnterObjAnimator_1).with(mAdExitObjAnimator);
                    mAnimatorSet_2.setDuration(300);
                    mAnimatorSet_2.setInterpolator(new DecelerateInterpolator());
                    mAnimatorSet_2.start();
                    break;
                case REMOVE_FRAGMENT:
                    if (flFragment.getChildCount() != 0) {
                        flFragment.removeAllViews();
                        ivAd.setVisibility(View.VISIBLE);
                        ivAd2.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private boolean mIsRoundPlay = true;
    private String[] mPicUrl;
    private GestureActiveFragment mGestureActiveFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        mGestureActiveFragment = new GestureActiveFragment();
        mAdEnterObjAnimator = ObjectAnimator.ofFloat(ivAd, "translationX", 1080, 0);
        mAdExitObjAnimator = ObjectAnimator.ofFloat(ivAd, "translationX", 0, -1080);
        mAdEnterObjAnimator_1 = ObjectAnimator.ofFloat(ivAd2, "translationX", 1080, 0);
        mAdExitObjAnimator_1 = ObjectAnimator.ofFloat(ivAd2, "translationX", 0, -1080);
        rlAdvertising.setOnClickListener(this);
        //test
        initContentPlay(System.currentTimeMillis() / 1000);
        getImges();
        getPermission();
    }

    @Override
    protected void onPause() {
        super.onPause();
        removeGestureFragment();
    }

    private void initContentPlay(long currentTime) {
        mContentPlayList = new ArrayList<>();
        mList.add(picUrl);
        mList.add(picUrl1);
        mList.add(picUrl2);
        for (int j = 0; j < 2000; j++) {
            mContentPlay = new ContentPlay();
            ContentPlay.ContentInfo contentInfo = new ContentPlay.ContentInfo();
            contentInfo.setContent_len(5000);
            contentInfo.setContentName("播放广告");
            contentInfo.setContent_priority(5);
            contentInfo.setContentUrl(mList.get(j % 3));
            mContentPlay.setContentInfo(contentInfo);
            mContentPlay.setStart_time((int) (currentTime + j * 2));
            mContentPlayList.add(mContentPlay);
        }
    }

    private void getImges() {
        mImges = Tools.getFilesAllName(mAdpath);
//        ALog.d(TAG, "mImges size = " + mImges.size());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.hideNavigationBarStatusBar(this, true);
        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    int i = 0;
                    int k = 0;
                    int size = mContentPlayList.size();
                    ALog.d(TAG, "mContentPlayList.size=" + size);
                    do {
                        mContentPlay = mContentPlayList.get(k++ % size);
                        startTime = mContentPlay.getStart_time();
                    } while (System.currentTimeMillis() / 1000 > startTime);
                    do {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (k > size) {
                            k = 0;
                            initContentPlay(System.currentTimeMillis() / 1000);
                            do {
                                mContentPlay = mContentPlayList.get(k++ % size);
                                startTime = mContentPlay.getStart_time();
                            } while (System.currentTimeMillis() / 1000 > startTime);
                        }
//                        ALog.d(TAG, "System.currentTimeMillis()=" + System.currentTimeMillis() / 1000);
//                        ALog.d(TAG, "startTime=" + startTime);
//                        ALog.d(TAG, "k=" + k);
                        while (System.currentTimeMillis() / 1000 > startTime) {
                            ALog.d(TAG, "System.currentTimeMillis()=" + System.currentTimeMillis() / 1000 + "> startTime=" + startTime);
                            startTime++;
                        }
                        isTimeToShowAd = System.currentTimeMillis() / 1000 == startTime ? true : false;
                        if (isTimeToShowAd) {
                            mMessage = mHandler.obtainMessage();
                            i = i == 1 ? 0 : 1;
                            mMessage.what = i + 1;
                            mMessage.obj = mContentPlay;
                            mHandler.sendMessage(mMessage);
//                            try {
//                                Thread.sleep(mContentPlay.getContentInfo().getContent_len());
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
                            mContentPlay = mContentPlayList.get(k++ % size);
                            startTime = mContentPlay.getStart_time();
                            isTimeToShowAd = false;
                        }
                    } while (mIsRoundPlay);
                }
            });
            mThread.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        if (mThread.isAlive()) {
//            mThread.destroy();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_advertising:
                if (flFragment.getChildCount() == 0) {
                    mGestureActiveFragment = new GestureActiveFragment();
                    getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment, mGestureActiveFragment).commitAllowingStateLoss();
                    ivAd.setVisibility(View.INVISIBLE);
                    ivAd2.setVisibility(View.INVISIBLE);
                }
//                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                break;
        }

    }

    public void removeGestureFragment() {
        Message message = mHandler.obtainMessage();
        message.what = REMOVE_FRAGMENT;
        mHandler.sendMessageDelayed(message,100);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowMessageEvent(EventBusMessage message){

    }

    private void getPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }
}




