package com.imprexion.adplay.advertising;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.imprexion.adplay.R;
import com.imprexion.adplay.advertising.activation.GestureActiveOneStepFragment;
import com.imprexion.adplay.advertising.activation.GestureActiveTwoStepFragment;
import com.imprexion.adplay.advertising.content.AdContentImageFragment;
import com.imprexion.adplay.advertising.content.CameraRainFragment;
import com.imprexion.adplay.bean.ADContentInfo;
import com.imprexion.adplay.bean.ADContentPlay;
import com.imprexion.adplay.bean.EventBusMessage;
import com.imprexion.adplay.bean.TrackingMessage;
import com.imprexion.adplay.net.NetPresenter;
import com.imprexion.adplay.service.AdPlayService;
import com.imprexion.adplay.service.TcpClientConnector;
import com.imprexion.adplay.tools.ALog;
import com.imprexion.adplay.tools.Tools;
import com.imprexion.service.tracking.bean.aiscreen;

import org.greenrobot.eventbus.EventBus;

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
    @BindView(R.id.fl_gestureActive)
    FrameLayout flGestureActive;

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
    private static final int ACTIVED = 5;
    private static final int REMOVE_GESTURE_ACTIVE = 6;
    private static final int START_AD_FIRST = 7;
    private static final int OTHER_PAGE = 100;
    private static final int AD_PAGE = 101;
    private int currentPage = 100;//100:MainActivity;101:Ad;
    private static final String AD_CURRENT = "ADContentCurrent";
    private static final String AD_NEXT = "ADContentNext";
    public final static String AD_DEFAULT = "adDefalt";
    private boolean isPlay = true;
    private int mCurrentPosition;
    private int mSize;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private int mCurrentPage;
    private boolean isShowGestureActive;
    private ServiceConnection mConnection;
    private NetPresenter mNetPresenter;
    //    private boolean isShowActiveTip;
    private TcpClientConnector mTcpClientConnector = TcpClientConnector.getInstance();

    private GestureActiveTwoStepFragment mGestureActiveTwoStepFragment;
    private GestureActiveOneStepFragment mGestureActiveOneStepFragment;
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
                    if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isActived()) {
                        isShowGestureActive = true;
                        showGestureActiveTowStepView();
                    }
                    break;
                case SHOW_ACTIVE_TIP_FROM_WAVE_HAND:
                    if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isActived()) {
                        isShowGestureActive = true;
                        showGestureActiveOneStepView();
                    }
                    break;
                case ACTIVED:
                    if (mTrackingMessage.getUsrsex() != 0) {
                        currentPage = OTHER_PAGE;
                        startAIScreenApp();
                    } else {
                        ALog.d(TAG, "have no person in 2s");
                    }
                    break;
                case REMOVE_GESTURE_ACTIVE:
                    if (mTrackingMessage.getUsrsex() == 0) {
                        if (flGestureActive.getChildCount() != 0) {
                            flGestureActive.removeAllViews();
                            isShowGestureActive = false;
                            mGestureActiveTwoStepFragment = null;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void startAIScreenApp() {
        ComponentName componentName = new ComponentName("com.imprexion.aiscreen", "com.imprexion.aiscreen.main.MainActivity");
        try {
            startActivity(new Intent().setComponent(componentName));
        } catch (Exception e) {
            ALog.d(TAG, "start AIScreen fail");
            e.printStackTrace();
        }
    }

    private TrackingMessage mTrackingMessage;

    private void showGestureActiveTowStepView() {
        ALog.d(TAG, "showGestureActiveTowStepView");
        if (mGestureActiveTwoStepFragment == null && flGestureActive.getChildCount() == 0) {
            ALog.d(TAG, "add gestureFragment");
            mGestureActiveTwoStepFragment = new GestureActiveTwoStepFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fl_gestureActive, mGestureActiveTwoStepFragment).commitAllowingStateLoss();
        }
    }

    private void showGestureActiveOneStepView() {
        ALog.d(TAG, "showGestureActiveTowStepView");
        if (mGestureActiveOneStepFragment == null && flGestureActive.getChildCount() == 0) {
            ALog.d(TAG, "add gestureFragment");
            mGestureActiveOneStepFragment = new GestureActiveOneStepFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fl_gestureActive, mGestureActiveOneStepFragment).commitAllowingStateLoss();
        }
    }

    private FragmentStatePagerAdapter mFragmentStatePagerAdapter;
    private List<ADContentInfo> mAdContentInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ALog.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_second);
        ButterKnife.bind(this);
        getPermission();
        bindAISService();
        setSocketListener();
        initData();

        //test
        tvUsersex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGestureActiveOneStepView();
            }
        });
        tvIsactived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTrackingMessage == null) {
                    mTrackingMessage = new TrackingMessage();
                    mTrackingMessage.setActived(true);
                    mTrackingMessage.setUsrsex(1);
                    mTrackingMessage.setStandHere(false);
                }
                EventBus.getDefault().post(new EventBusMessage(EventBusMessage.ACTIVE_TIP, mTrackingMessage));
            }
        });
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
        String adContentPlayString = mSharedPreferences.getString(AD_CURRENT, null);
        ADContentPlay adContentPlay;
        if (adContentPlayString != null) {
            adContentPlay = JSON.parseObject(adContentPlayString, ADContentPlay.class);
            if (!adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
                adContentPlayString = mSharedPreferences.getString(AD_NEXT, null);
                if (adContentPlayString != null) {
                    adContentPlay = JSON.parseObject(adContentPlayString, ADContentPlay.class);
                    if (!adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
                        ALog.d(TAG, "there is not today adContent");
                        adContentPlay = getDefaultADContentPlay();
                    } else {
                        mEditor.putString(AD_CURRENT, adContentPlayString);
                        mEditor.commit();
                    }
                } else {
                    ALog.d(TAG, "adContentPlayString next is null");
                    adContentPlay = getDefaultADContentPlay();
                }

            }
        } else {
            ALog.d(TAG, "adContentPlayString current is null");
            adContentPlay = getDefaultADContentPlay();
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

    @NonNull
    private ADContentPlay getDefaultADContentPlay() {
        ADContentPlay adContentPlay;
        adContentPlay = new ADContentPlay();
        ADContentInfo adContentInfo = new ADContentInfo();
        adContentInfo.setFileUrl(AD_DEFAULT);
        adContentInfo.setContentType(1);
        adContentInfo.setPlayTime(10);
        List<ADContentInfo> adContentInfoList = new ArrayList<>();
        adContentInfoList.add(adContentInfo);
        adContentPlay.setContentPlayVOList(adContentInfoList);
        return adContentPlay;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowGestureActive = false;
        currentPage = AD_PAGE;
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
                    if (mCurrentPage >= mSize) {
                        mCurrentPage = 0;
                    }
//                    ALog.d(TAG, "runnable next,playTime = " + mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getPlayTime());
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
        currentPage = OTHER_PAGE;
        mEditor.putInt("mCurrentPage", mCurrentPage);
        mEditor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
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

    private void bindAISService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ALog.d(TAG, "onServiceConnected");
                AdPlayService.AISBinder aisBinder = (AdPlayService.AISBinder) service;
                aisBinder.getService().setContentInfoToActivity(new AdPlayService.IContentInfoCallBack() {
                    @Override
                    public void setContentInfo(String content) {
                        pushMessage(content);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this, AdPlayService.class);
        intent.putExtra("src", 0);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        startService(intent);

    }

    private void pushMessage(String content) {
        ALog.d(TAG, "ContentInfo=" + content);
        ADContentPlay adContentPlay = JSON.parseObject(content, ADContentPlay.class);
        if (mNetPresenter == null) {
            mNetPresenter = new NetPresenter();
        }
        mNetPresenter.onADCallback(adContentPlay);
        if (mSharedPreferences == null) {
            mSharedPreferences = getSharedPreferences("AIScreenSP", Context.MODE_PRIVATE);
            mEditor = mSharedPreferences.edit();
        }

        ALog.d(TAG, "adContentPlay.getPlayDate()=" + adContentPlay.getPlayDate());
        ALog.d(TAG, "getCurrentDate()=" + Tools.getCurrentDate("yyyy-MM-dd"));
        ALog.d(TAG, "adContentPlay=" + JSON.toJSONString(adContentPlay));
        if (adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
            mEditor.putString(AD_CURRENT, content);
            mEditor.commit();
            if (adContentPlay.getPlayDate().equals(Tools.getCurrentDate("yyyy-MM-dd"))) {
                initData();
//                viewPager.notifyAll();
                mFragmentStatePagerAdapter.notifyDataSetChanged();
            }
        } else {
            mEditor.putString(AD_NEXT, content);
            mEditor.commit();
        }
    }

    private void setSocketListener() {
//        mTcpClientConnector.createConnect("10.2.26.7", 20002);
        mTcpClientConnector.createConnect("127.0.0.1", 20002);
        mTcpClientConnector.setOnConnectListener(new TcpClientConnector.ConnectListener() {
            @Override
            public void onReceiveData(aiscreen messageForAIScreen) {
                String mData = "sex=" + messageForAIScreen.getUsrSex() + " ;HavePerson=" + messageForAIScreen.getStandHere() + " ;IsActived=" + messageForAIScreen.getIsActived();
//                ALog.d(TAG, "sex=" + messageForAIScreen.getUsrsex());
//                ALog.d(TAG, "HavePerson=" + messageForAIScreen.getStandHere());
//                ALog.d(TAG, "IsActived=" + messageForAIScreen.getIsActived());
                dispatchMessage(messageForAIScreen);
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private void dispatchMessage(aiscreen messageForAIScreen) {
        if (mTrackingMessage == null) {
            mTrackingMessage = new TrackingMessage();
        }
        mTrackingMessage.setUsrsex(messageForAIScreen.getUsrSex());
        mTrackingMessage.setActived(messageForAIScreen.getIsActived());
        mTrackingMessage.setStandHere(messageForAIScreen.getStandHere());
        tvUsersex.setText("" + mTrackingMessage.getUsrsex());
        tvStandhere.setText(mTrackingMessage.isStandHere() == true ? "true" : "false");
        tvIsactived.setText(mTrackingMessage.isActived() == true ? "true" : "false");
        ALog.d(TAG, "usrsex=" + mTrackingMessage.getUsrsex() + " ;standHere=" + mTrackingMessage.isStandHere()
                + " ;isActived=" + mTrackingMessage.isActived());
//        if (mTrackingMessage.getUsrsex() == 0 && currentPage != AD_PAGE) {
//            Message message = mHandler.obtainMessage();
//            message.what = NO_PERSON;
//            mHandler.sendMessageDelayed(message, 2000);
//        }
        ALog.d(TAG, "isShowActiveTip=" + isShowGestureActive);

        //激活屏幕进入主界面
        if (mTrackingMessage.getUsrsex() != 0 && currentPage == AD_PAGE && mTrackingMessage.isActived() && !isShowGestureActive) {
            Message message = mHandler.obtainMessage();
            message.what = ACTIVED;
            mHandler.sendMessageDelayed(message, 500);
        }

        //没人了3s,消除小象动画
        if (mTrackingMessage.getUsrsex() == 0 && isShowGestureActive) {
            Message message = mHandler.obtainMessage();
            message.what = REMOVE_GESTURE_ACTIVE;
            mHandler.sendMessageDelayed(message, 3000);
        }

        //识别到有人但没有激活屏幕。2s后显示小象动画
        if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isActived() && !isShowGestureActive) {
            if (mTrackingMessage.isStandHere()) {
                Message message = mHandler.obtainMessage();
                message.what = SHOW_ACTIVE_TIP_FROM_FOOT;
                mHandler.sendMessageDelayed(message, 2000);
            } else {
                Message message = mHandler.obtainMessage();
                message.what = SHOW_ACTIVE_TIP_FROM_WAVE_HAND;
                mHandler.sendMessageDelayed(message, 2000);
            }
        }
        //发送tracking消息给小象。小象根据消息刺激执行下一步动画
        EventBus.getDefault().post(new EventBusMessage(EventBusMessage.ACTIVE_TIP, mTrackingMessage));

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
