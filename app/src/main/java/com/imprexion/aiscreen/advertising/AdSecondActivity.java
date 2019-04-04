package com.imprexion.aiscreen.advertising;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.advertising.content.AdContentImageFragment;
import com.imprexion.aiscreen.advertising.content.CameraRainFragment;
import com.imprexion.aiscreen.bean.ADContentInfo;
import com.imprexion.aiscreen.bean.ADContentPlay;
import com.imprexion.aiscreen.bean.EventBusMessage;
import com.imprexion.aiscreen.tools.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdSecondActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;

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
    private boolean isPlay = true;
    private int mCurrentPosition;
    private int mSize;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private int mCurrentPage;
    private static final String AD_SUFFIX = "ADContent";

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PLAY_NEXT:
                    mCurrentPage = mPagerPage++ % mSize;
                    viewPager.setCurrentItem(mCurrentPage);
                    break;
                default:
                    break;
            }
        }
    };
    private FragmentStatePagerAdapter mFragmentStatePagerAdapter;
    private List<ADContentInfo> mAdContentInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_second);
        ButterKnife.bind(this);
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
//        ADContentPlay adContentPlay = JSON.parseObject(getString("adJson.text", this), ADContentPlay.class);
        String adContentPlayString = mSharedPreferences.getString(getCurrentDate() + AD_SUFFIX, null);
        if (adContentPlayString == null) {
            Log.d(TAG, "adContentPlayString is null");
            return;
        }
        ADContentPlay adContentPlay = JSON.parseObject(adContentPlayString, ADContentPlay.class);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tools.hideNavigationBarStatusBar(this, true);
        initViewPager();
        mPagerPage = mSharedPreferences.getInt("mCurrentPage", 0);
        Log.d(TAG, "mCurrentPage=" + mPagerPage);
        isPlay = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                do {
//                    viewPager.setCurrentItem(mPagerPage++ % 3);
                    Message message = mHandler.obtainMessage();
                    message.what = PLAY_NEXT;
                    mHandler.sendMessage(message);
                    try {
                        Thread.sleep(mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getPlayTime() * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "runnable next,playTime = " + mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getPlayTime());
                } while (isPlay);
            }
        };
        if (mExecutorService != null) {
            mExecutorService.execute(runnable);
        }
    }

    private void initViewPager() {
//        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
//            @Override
//            public Fragment getItem(int i) {
//                return mFragmentList.get(i);
//            }
//
//            @Override
//            public int getCount() {
//                return mFragmentList.size();
//            }
//        };
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
                Log.d(TAG, "onPageSelected=" + i);
                mCurrentPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i != ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                if (mCurrentPosition == mSize - 1) {
                    Log.d(TAG, "last->first");
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
        Log.d(TAG, "ADjson=" + stringBuilder.toString());
        return stringBuilder.toString();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAdPlayContent(EventBusMessage eventBusMessage) {
        if (eventBusMessage.getType() == EventBusMessage.AD_PLAY_CONTENT) {
            String adPlayContent = (String) eventBusMessage.getObject();
            Log.d(TAG, "adPlayContent=" + adPlayContent);
            ADContentPlay adContentPlay = JSON.parseObject(adPlayContent, ADContentPlay.class);
            if (mSharedPreferences == null) {
                mSharedPreferences = getSharedPreferences("AIScreenSP", Context.MODE_PRIVATE);
                mEditor = mSharedPreferences.edit();
            }
            mEditor.putString(adContentPlay.getPlayDate() + AD_SUFFIX, adPlayContent);
            Log.d(TAG, "adContentPlay.getPlayDate()=" + adContentPlay.getPlayDate());
            Log.d(TAG, "getCurrentDate()=" + getCurrentDate());
            mEditor.commit();
            if (adContentPlay.getPlayDate().equals(getCurrentDate())) {
                initData();
//                viewPager.notifyAll();
                mFragmentStatePagerAdapter.notifyDataSetChanged();
            }
        }
    }

    private String getCurrentDate() {
        long time = System.currentTimeMillis();
        Date date = new Date(time);
        String pattern = "yyyy-MM-dd";//格式yy-MM
        String currentDate = new SimpleDateFormat(pattern, Locale.getDefault()).format(date);
        Log.d(TAG, "currentDate=" + currentDate);
        return currentDate;
    }

}
