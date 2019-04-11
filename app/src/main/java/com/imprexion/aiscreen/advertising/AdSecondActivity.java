package com.imprexion.aiscreen.advertising;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.advertising.activation.GestureActivationFragment;
import com.imprexion.aiscreen.advertising.content.AdContentImageFragment;
import com.imprexion.aiscreen.advertising.content.CameraRainFragment;
import com.imprexion.aiscreen.bean.ADContentInfo;
import com.imprexion.aiscreen.bean.ADContentPlay;
import com.imprexion.aiscreen.bean.EventBusMessage;
import com.imprexion.aiscreen.bean.TrackingMessage;
import com.imprexion.aiscreen.main.MainActivity;
import com.imprexion.aiscreen.tools.ALog;
import com.imprexion.aiscreen.tools.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdSecondActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.tv_usersex)
    TextView tvUsersex;
    @BindView(R.id.tv_standhere)
    TextView tvStandhere;
    @BindView(R.id.tv_isactived)
    TextView tvIsactived;

    private String picUrl = "https://cn.bing.com/th?id=OHR.SakuraFes_ZH-CN1341601988_1920x1080.jpg&rf=NorthMale_1920x1080.jpg";
    private String picUrl1 = "https://cn.bing.com/sa/simg/hpb/NorthMale_EN-US8782628354_1366x768.jpg";
    private String picUrl2 = "https://cn.bing.com/th?id=OHR.AthensNight_ZH-CN1280970241_1920x1080.jpg&rf=NorthMale_1920x1080.jpg";

    private String[] pics = {picUrl, picUrl1, picUrl2, picUrl};

    private List<Fragment> mFragmentList;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mPagerPage;
    private ExecutorService mExecutorService;
    private final static String TAG = "AdSecondActivity";
    private final static int PLAY_NEXT = 1;
    private final static int SHOW_ACTIVE_TIP_FROM_FOOT = 2;
    private final static int SHOW_ACTIVE_TIP_FROM_WAVE_HAND = 3;
    private boolean isPlay = true;
    private int mCurrentPosition;
    private int mSize;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private int mCurrentPage;
    private boolean isShowGestureActive;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PLAY_NEXT:
                    mCurrentPage = mPagerPage++ % mSize;
                    viewPager.setCurrentItem(mCurrentPage);
                    break;
                case SHOW_ACTIVE_TIP_FROM_FOOT:
                    if (!mTrackingMessage.isActived()) {
                        showGestureActiveView();
                    }
                    break;
                case SHOW_ACTIVE_TIP_FROM_WAVE_HAND:
                    if (!mTrackingMessage.isActived()) {
                        showGestureActiveView();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private TrackingMessage mTrackingMessage;

    private void showGestureActiveView() {
        ALog.d(TAG, "showGestureActiveView");
        EventBus.getDefault().post(new EventBusMessage(EventBusMessage.IS_SHOW_ACTIVE_TIP, null));
        getSupportFragmentManager().beginTransaction().add(R.id.fl_gestureActive, new GestureActivationFragment()).commitAllowingStateLoss();
    }

    private FragmentStatePagerAdapter mFragmentStatePagerAdapter;
    private List<ADContentInfo> mAdContentInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_second);
        ButterKnife.bind(this);
        getPermission();
        EventBus.getDefault().register(this);
        initData();
    }

    private void initData() {
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences("AIScreenSP", Context.MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
        }
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
        }
        mFragmentList.clear();
        String adContentPlayString = mSharedPreferences.getString(MainActivity.AD_CURRENT, null);
        ADContentPlay adContentPlay;
        if (adContentPlayString != null) {
            adContentPlay = JSON.parseObject(adContentPlayString, ADContentPlay.class);
        } else {
            ALog.d(TAG, "adContentPlayString current is null");
            return;
        }
        if (!adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
            adContentPlayString = mSharedPreferences.getString(MainActivity.AD_NEXT, null);
            if (adContentPlayString != null) {
                adContentPlay = JSON.parseObject(adContentPlayString, ADContentPlay.class);
            } else {
                ALog.d(TAG, "adContentPlayString next is null");
                return;
            }
            if (!adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
                ALog.d(TAG, "there is not today adContent");
                return;
            } else {
                mEditor.putString(MainActivity.AD_CURRENT, adContentPlayString);
                mEditor.commit();
            }
        }
//        ADContentPlay adContentPlay = JSON.parseObject(getString("adJson.text", this), ADContentPlay.class);
        mAdContentInfoList = adContentPlay.getContentPlayVOList();
        int size = mAdContentInfoList.size();
        for (int i = 0; i < size; i++) {
            ADContentInfo adContentInfo = mAdContentInfoList.get(i);
            if (adContentInfo.getContentType() == 1) {
                Fragment fragment = new AdContentImageFragment();
                ((AdContentImageFragment) fragment).setUrl(adContentInfo.getFileUrl());
                mFragmentList.add(fragment);
            } else if (adContentInfo.getContentType() == 2) {
                if (adContentInfo.getAppCode().equals("1001")) {//1001代表雨滴
                    Fragment fragment = new CameraRainFragment();
                    mFragmentList.add(fragment);
                }
            }
        }
        ADContentInfo adContentInfo = mAdContentInfoList.get(0);
        if (adContentInfo.getContentType() == 1) {
            Fragment fragment = new AdContentImageFragment();
            ((AdContentImageFragment) fragment).setUrl(adContentInfo.getFileUrl());
            mFragmentList.add(fragment);
        } else if (adContentInfo.getContentType() == 2) {
            if (adContentInfo.getAppCode().equals("1001")) {
                Fragment fragment = new CameraRainFragment();
                mFragmentList.add(fragment);
            }
        }
        mSize = mFragmentList.size();
        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        initViewPager();

    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowGestureActive = false;
        Tools.hideNavigationBarStatusBar(this, true);
//        initViewPager();
        mPagerPage = mSharedPreferences.getInt("mCurrentPage", 0);
        ALog.d(TAG, "mCurrentPage=" + mPagerPage);
        isPlay = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                do {
//                    viewPager.setCurrentItem(mPagerPage++ % 3);
                    Message message = mHandler.obtainMessage();
                    message.what = PLAY_NEXT;
                    mHandler.sendMessage(message);
//                    ALog.d(TAG, "mAdContentInfoList size = " + mAdContentInfoList.size());
//                    ALog.d(TAG, "mSize = " + mSize);
//                    ALog.d(TAG, "mCurrentPage = " + mCurrentPage);
                    ALog.d(TAG, "runnable next,playTime = " + mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getPlayTime());
                    try {
                        Thread.sleep(mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getPlayTime() * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (isPlay);
            }
        };
        if (mExecutorService != null) {
            mExecutorService.execute(runnable);
        }
    }

    private void initViewPager() {
        mFragmentStatePagerAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Override
            public Fragment getItem(int i) {
                return mFragmentList.get(i);
            }
        };
        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
//                ALog.d(TAG, "onPageSelected=" + i);
                mCurrentPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i != ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                if (mCurrentPosition == mSize - 1) {
//                    ALog.d(TAG, "last->first");
                    viewPager.setCurrentItem(0, false);
                    mPagerPage++;
                }

            }
        };
//        viewPager.setAdapter(mFragmentPagerAdapter);
        viewPager.setAdapter(mFragmentStatePagerAdapter);
        viewPager.setOnPageChangeListener(mOnPageChangeListener);
        viewPager.setOffscreenPageLimit(2);

    }

    @Override
    protected void onPause() {
        super.onPause();
        isPlay = false;
        mEditor.putInt("mCurrentPage", mCurrentPage);
        mEditor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
//        mExecutorService.shutdown();
    }

    public static String getString(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ALog.d(TAG, "ADjson=" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAdPlayContent(EventBusMessage eventBusMessage) {
        if (eventBusMessage.getType() == EventBusMessage.AD_PLAY_CONTENT) {
            ADContentPlay adContentPlay = (ADContentPlay) eventBusMessage.getObject();
            if (adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
                initData();
//                viewPager.notifyAll();
                mFragmentStatePagerAdapter.notifyDataSetChanged();
            }
        }
        if (eventBusMessage.getType() == EventBusMessage.ACTIVE_TIP) {
            mTrackingMessage = (TrackingMessage) eventBusMessage.getObject();
            tvUsersex.setText("" + mTrackingMessage.getUsrsex());
            tvStandhere.setText(mTrackingMessage.isStandHere() == true ? "true" : "false");
            tvIsactived.setText(mTrackingMessage.isActived() == true ? "true" : "false");

            if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isActived() && !isShowGestureActive) {
                if (mTrackingMessage.isStandHere()) {
                    isShowGestureActive = true;
                    Message message = mHandler.obtainMessage();
                    message.what = SHOW_ACTIVE_TIP_FROM_FOOT;
                    mHandler.sendMessageDelayed(message, 3000);
                } else {
                    isShowGestureActive = true;
                    Message message = mHandler.obtainMessage();
                    message.what = SHOW_ACTIVE_TIP_FROM_WAVE_HAND;
                    mHandler.sendMessageDelayed(message, 3000);
                }
            }
        }
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
