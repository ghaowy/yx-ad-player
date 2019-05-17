package com.imprexion.adplayer.main;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.imprexion.adplayer.R;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.bean.EventBusMessage;
import com.imprexion.adplayer.bean.TrackingMessage;
import com.imprexion.adplayer.main.activation.GestureActiveFootPrintFragment;
import com.imprexion.adplayer.main.activation.GestureActiveOneStepFragment;
import com.imprexion.adplayer.main.activation.GestureActiveTwoStepFragment;
import com.imprexion.adplayer.main.content.AdContentImageFragment;
import com.imprexion.adplayer.main.content.CameraRainFragment;
import com.imprexion.adplayer.service.TcpClientConnector;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.library.YxLog;
import com.imprexion.library.YxStatistics;
import com.imprexion.service.tracking.bean.aiscreen;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AdActivity extends AppCompatActivity {

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
    @BindView(R.id.bt_enter)
    Button btEnter;

    private List<Fragment> mFragmentList;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int mPagerPage;
    private ExecutorService mExecutorService;
    private final static String TAG = "AdActivity";
    private final static int PLAY_NEXT = 1;
    private final static int SHOW_ACTIVE_TIP_FROM_FOOT = 2;
    private final static int SHOW_ACTIVE_TIP_FROM_WAVE_HAND = 3;
    private final static int SHOW_ACTIVE_TIP_FOOTPRINT = 4;//提示站对脚印
    private final static int SHOW_ELEPHANT_ACTIVE_GESTURE_DELAY = 50;
    private static final int ACTIVED = 5;
    private static final int REMOVE_GESTURE_ACTIVE = 6;
    private static final int START_AD_FROM_USER_DETECT = 7;
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
    private boolean isSendShowGestureActive;
    private boolean isLaunchFromUserDetect;////backPressed,userDetect.
    private TcpClientConnector mTcpClientConnector = TcpClientConnector.getInstance();

    private GestureActiveTwoStepFragment mGestureActiveTwoStepFragment;
    private GestureActiveOneStepFragment mGestureActiveOneStepFragment;
    private GestureActiveFootPrintFragment mGestureActiveFootPrintFragment;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_NEXT:
                    YxLog.d(TAG, "main mCurrentPage=" + mCurrentPage);
                    viewPager.setCurrentItem(mCurrentPage);
                    break;
                case SHOW_ACTIVE_TIP_FROM_FOOT:
                    if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isActived()) {
                        isShowGestureActive = true;
                        if (mTrackingMessage.isStandHere()) {
                            showGestureActiveTowStepView();
                        } else {
                            showGestureActiveOneStepView();
                        }
                    }
                    isSendShowGestureActive = false;
                    break;
                case SHOW_ACTIVE_TIP_FOOTPRINT:
                    if (mTrackingMessage.getUsrsex() != 0 && mTrackingMessage.isStandHere()) {
                        isShowGestureActive = true;
                        showGestureActiveFootPrintView();
                    }
                    isSendShowGestureActive = false;
                    break;
                case ACTIVED:
                    if (mTrackingMessage.getUsrsex() != 0) {
                        currentPage = OTHER_PAGE;
                        startAIScreenApp();
                    } else {
                        YxLog.d(TAG, "have no person in 2s");
                    }
                    break;
                case REMOVE_GESTURE_ACTIVE:
                    if (mTrackingMessage.getUsrsex() == 0) {
                        removeActivationFragment();
                    } else if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isStandHere()) {
                        removeActivationFragment();
                    }
                    break;
                case START_AD_FROM_USER_DETECT:
                    isLaunchFromUserDetect = true;
                    break;
                default:
                    break;

            }
            return false;
        }
    });

    public void startAIScreenApp() {
        YxLog.i(TAG, "goto main page");
        YxStatistics.version(1).param("way", "wave hand").report("goto_main_page");
        finish();
    }

    private TrackingMessage mTrackingMessage;

    private void showGestureActiveTowStepView() {
        YxLog.d(TAG, "showGestureActiveTowStepView");
        if (mGestureActiveTwoStepFragment == null && flGestureActive.getChildCount() == 0) {
            YxLog.d(TAG, "add two gestureFragment");
            mGestureActiveTwoStepFragment = new GestureActiveTwoStepFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fl_gestureActive, mGestureActiveTwoStepFragment).commitAllowingStateLoss();
        }
    }

    private void showGestureActiveFootPrintView() {
        YxLog.d(TAG, "showGestureActiveFootPrintView");
        if (mGestureActiveFootPrintFragment == null && flGestureActive.getChildCount() == 0) {
            YxLog.d(TAG, "add footprint gestureFragment");
            mGestureActiveFootPrintFragment = new GestureActiveFootPrintFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fl_gestureActive, mGestureActiveFootPrintFragment).commitAllowingStateLoss();
        }
    }

    private void showGestureActiveOneStepView() {
        YxLog.d(TAG, "showGestureActiveOneStepView");
        if (mGestureActiveOneStepFragment == null && flGestureActive.getChildCount() == 0) {
            YxLog.d(TAG, "add  one gestureFragment");
            mGestureActiveOneStepFragment = new GestureActiveOneStepFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.fl_gestureActive, mGestureActiveOneStepFragment).commitAllowingStateLoss();
        }
    }

    private FragmentStatePagerAdapter mFragmentStatePagerAdapter;
    private List<ADContentInfo> mAdContentInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        YxLog.i(TAG, "MainActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_second);
        ButterKnife.bind(this);
        getPermission();
        EventBus.getDefault().register(this);
        setSocketListener();
//        YxLog.init(this, true, false);
//        YxLog.setVersion(Tools.getVersionName(this));
//        YxLog.setPrettyFormatEnable(false);
        if (getIntent() != null && getIntent().getExtras() != null && "userDetect".equals(getIntent().getExtras().getString("launchType"))) {
            isLaunchFromUserDetect = true;
        }
        initData();
        setOnClickListener();
    }

    private void setOnClickListener() {
        btEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAIScreenApp();
            }
        });
        //test
        tvUsersex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showGestureActiveOneStepView();
                showGestureActiveTowStepView();
            }
        });
        tvStandhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTrackingMessage == null) {
                    mTrackingMessage = new TrackingMessage();
                }
                mTrackingMessage.setActived(false);
                mTrackingMessage.setUsrsex(1);
                mTrackingMessage.setStandHere(false);
                EventBus.getDefault().post(new EventBusMessage(EventBusMessage.ACTIVE_TIP, mTrackingMessage));
            }
        });
        tvIsactived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTrackingMessage == null) {
                    mTrackingMessage = new TrackingMessage();
                }
                mTrackingMessage.setActived(true);
                mTrackingMessage.setUsrsex(1);
                mTrackingMessage.setStandHere(false);
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
                        YxLog.d(TAG, "there is not today adContent");
                        adContentPlay = getDefaultADContentPlay();
                    } else {
                        mEditor.putString(AD_CURRENT, adContentPlayString);
                        mEditor.commit();
                    }
                } else {
                    YxLog.d(TAG, "adContentPlayString next is null");
                    adContentPlay = getDefaultADContentPlay();
                }
            }
        } else {
            YxLog.d(TAG, "adContentPlayString current is null");
            adContentPlay = getDefaultADContentPlay();
        }

        YxLog.d(TAG, "adContentPlayString=" + adContentPlayString);
        mAdContentInfoList = adContentPlay.getContentPlayVOList();
        int size = mAdContentInfoList.size();
        for (int i = 0; i < size; i++) {
            ADContentInfo adContentInfo = mAdContentInfoList.get(i);
            if (adContentInfo.getContentType() == 1) {//ContentType==1表示广告图片
                Fragment fragment = new AdContentImageFragment();
                ((AdContentImageFragment) fragment).setUrl(adContentInfo.getFileUrl());
                mFragmentList.add(fragment);
            } else if (adContentInfo.getContentType() == 2) {//ContentType==2表示播放应用
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
        mPagerPage = mSharedPreferences.getInt("mCurrentPage", 0);
//        YxLog.d(TAG, "mCurrentPage=" + mPagerPage);
        isPlay = true;
        Runnable runnable = new Runnable() {
            private JSONObject mJsonObject;
            @Override
            public void run() {
                do {
                    Message message = mHandler.obtainMessage();
                    message.what = PLAY_NEXT;
                    mHandler.sendMessage(message);
                    mCurrentPage = mPagerPage++ % mSize;
//                    YxLog.d(TAG, "mAdContentInfoList size = " + mAdContentInfoList.size());
//                    YxLog.d(TAG, "mSize = " + mSize);
//                    YxLog.d(TAG, "mCurrentPage = " + mCurrentPage);
                    if (mCurrentPage >= mSize) {
                        mCurrentPage = 0;
                    }
                    if (mJsonObject == null) {
                        mJsonObject = new JSONObject();
                    }
                    long appPlanId;
                    if (mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getContentType() == 2) {
//                        mJsonObject.put("ad_plan_id", mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getAppPlanId());
                        appPlanId = mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getAppPlanId();
                    } else {
//                        mJsonObject.put("ad_plan_id", mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getAdPlanId());
                        appPlanId = mAdContentInfoList.get(mCurrentPage == mSize - 1 ? 0 : mCurrentPage).getAdPlanId();
                    }
                    mJsonObject.put("type", "1");
                    YxStatistics.version(1).param("adplanId", appPlanId).report("aiscreen_ad_play");
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
                YxLog.i(TAG, "slide ad content: " + i + "/" + (mSize - 1));
                YxStatistics.version(1).param("cur", i).param("total", mSize - 1).report("slide_ad_content");
                mCurrentPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i != ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                if (mCurrentPosition == mSize - 1) {
//                    YxLog.d(TAG, "last->first");
                    viewPager.setCurrentItem(0, false);
                    mPagerPage++;
                }

            }
        };
        viewPager.setAdapter(mFragmentStatePagerAdapter);
        viewPager.setOnPageChangeListener(mOnPageChangeListener);
        viewPager.setOffscreenPageLimit(2);
    }

    @Override
    protected void onPause() {
        YxLog.i(TAG, "MainActivity onPause");
        super.onPause();
        isPlay = false;
        currentPage = OTHER_PAGE;
        mEditor.putInt("mCurrentPage", mCurrentPage);
        mEditor.commit();
        removeActivationFragment();
    }

    private void removeActivationFragment() {
        if (flGestureActive.getChildCount() != 0) {
            FragmentTransaction mTransaction = getSupportFragmentManager().beginTransaction();
            if (mGestureActiveOneStepFragment != null) {
                mTransaction.remove(mGestureActiveOneStepFragment);
            }
            if (mGestureActiveTwoStepFragment != null) {
                mTransaction.remove(mGestureActiveTwoStepFragment);
            }
            if (mGestureActiveFootPrintFragment != null) {
                mTransaction.remove(mGestureActiveFootPrintFragment);
            }
            mTransaction.commitAllowingStateLoss();
            isShowGestureActive = false;
            mGestureActiveTwoStepFragment = null;
            mGestureActiveOneStepFragment = null;
            mGestureActiveFootPrintFragment = null;
        }
    }

    @Override
    protected void onDestroy() {
        YxLog.i(TAG, "MainActivity onDestroy");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void setSocketListener() {
//        mTcpClientConnector.createConnect("10.2.26.7", 20002);
        mTcpClientConnector.createConnect("127.0.0.1", 20002);
        mTcpClientConnector.setOnConnectListener(new TcpClientConnector.ConnectListener() {
            @Override
            public void onReceiveData(aiscreen messageForAIScreen) {
                YxLog.i(TAG, "receive tracking info");
                dispatchMessage(messageForAIScreen);
            }
        });
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
        YxLog.d(TAG, "usrsex=" + mTrackingMessage.getUsrsex() + " ;standHere=" + mTrackingMessage.isStandHere()
                + " ;isActived=" + mTrackingMessage.isActived());
        YxLog.d(TAG, "isShowActiveTip=" + isShowGestureActive);

        //激活屏幕进入主界面
        if (mTrackingMessage.getUsrsex() != 0 && currentPage == AD_PAGE && mTrackingMessage.isActived() &&
                !isShowGestureActive && isLaunchFromUserDetect) {
            Message message = mHandler.obtainMessage();
            message.what = ACTIVED;
            mHandler.sendMessageDelayed(message, 500);
        }

        //backPress启动，没人2秒后 恢复为检测到没有人启动
        if (mTrackingMessage.getUsrsex() == 0 && !isLaunchFromUserDetect) {
            Message message = mHandler.obtainMessage();
            message.what = START_AD_FROM_USER_DETECT;
            mHandler.sendMessageDelayed(message, 2000);
        }

        //没人了2s,消除小象动画
        if (mTrackingMessage.getUsrsex() == 0 && isShowGestureActive) {
            Message message = mHandler.obtainMessage();
            message.what = REMOVE_GESTURE_ACTIVE;
            mHandler.sendMessageDelayed(message, 2000);
        }

        //脚印站对位置了，消除脚印动画
        if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isStandHere() && isShowGestureActive) {
            Message message = mHandler.obtainMessage();
            message.what = REMOVE_GESTURE_ACTIVE;
//            mHandler.sendMessageDelayed(message, 50);
            mHandler.sendMessage(message);
        }

//        //识别到有人但没有激活屏幕。(即时)后显示小象动画
//        if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isActived() && !isShowGestureActive) {
//            if (!isSendShowGestureActive) {
//                isSendShowGestureActive = true;
//                Message message = mHandler.obtainMessage();
//                message.what = SHOW_ACTIVE_TIP_FROM_FOOT;
//                mHandler.sendMessageDelayed(message, SHOW_ELEPHANT_ACTIVE_GESTURE_DELAY);
//            }
//        }

        //识别到有人但没有站对位置。(即时)后显示站对位置提示动画
        if (mTrackingMessage.getUsrsex() != 0 && mTrackingMessage.isStandHere() && !isShowGestureActive) {
            if (!isSendShowGestureActive) {
                isSendShowGestureActive = true;
                Message message = mHandler.obtainMessage();
                message.what = SHOW_ACTIVE_TIP_FOOTPRINT;
                mHandler.sendMessageDelayed(message, SHOW_ELEPHANT_ACTIVE_GESTURE_DELAY);
            }
        }
        //发送tracking消息给小象。小象根据消息刺激执行下一步动画
//        EventBus.getDefault().post(new EventBusMessage(EventBusMessage.ACTIVE_TIP, mTrackingMessage));

    }

    @Subscribe
    public void onEventBusMessageShow(EventBusMessage eventBusMessage) {
        YxLog.d(TAG, "onEventBusMessageShow");
        if (eventBusMessage.getType() == EventBusMessage.AD_PLAY_CONTENT_UPDATE_BY_BROADCAST) {
            YxLog.d(TAG, "update() mADBroadcastReceiver");
            initData();
            mFragmentStatePagerAdapter.notifyDataSetChanged();
        }
    }

    private void getPermission() {
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
    }
}
