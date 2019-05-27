package com.imprexion.adplayer.main;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
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
import com.imprexion.adplayer.service.AdPlayService;
import com.imprexion.adplayer.service.TcpClientConnector;
import com.imprexion.adplayer.tools.Tools;
import com.imprexion.adplayer.tools.VoicePlay;
import com.imprexion.library.YxLog;
import com.imprexion.library.YxStatistics;
import com.imprexion.library.util.ContextUtils;
import com.imprexion.service.tracking.bean.aiscreen;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

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
    SVGAImageView btEnter;
    @BindView(R.id.tv_hands_active_text)
    TextView tvHandsActiveText;

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
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    // 广告总数（包括应用和图片）
    private int mADContentSize;
    // 图片广告fragment数
    private int mFragmentSize;
    // 当前广告页码（包括应用和图片）
    private int mCurrentPage;
    // 当前viewPage页码
    private int mCurrentViewPageIndex;
    // 当前广告数据
    private ADContentInfo mCurrentADContentInfo;

    public SoundPool getSoundPool() {
        return mSoundPool;
    }

    public int getSoundIdStandFootprint() {
        return mSoundIdStandFootprint;
    }

//    public int getSoundIdPutUpYourHand() {
//        return mSoundIdPutUpYourHand;
//    }

    private SoundPool mSoundPool;
    private int mSoundIdStandFootprint;
//    private int mSoundIdPutUpYourHand;

    private boolean isShowGestureActive;
    private boolean isSendShowGestureActive;
    private boolean isLaunchFromUserDetect;//backPressed,userDetect.
    private TcpClientConnector mTcpClientConnector = TcpClientConnector.getInstance();

    private GestureActiveTwoStepFragment mGestureActiveTwoStepFragment;
    private GestureActiveOneStepFragment mGestureActiveOneStepFragment;
    private GestureActiveFootPrintFragment mGestureActiveFootPrintFragment;
    private ObjectAnimator mHandsActiveAnimator;
    private TrackingMessage mTrackingMessage;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_NEXT:
                    YxLog.d(TAG, "handleMessage --- mCurrentPage = " + mCurrentPage);

                    ADContentInfo adContentInfo = mAdContentInfoList.get(mCurrentPage);
                    // 图片广告
                    if (adContentInfo.getContentType() == 1) {
                        YxLog.d(TAG, "handleMessage --- mCurrentViewPageIndex = " + mCurrentViewPageIndex);

//                        // 图片循环播放， 第0个不播，否则会反向跳转
//                        if (mCurrentViewPageIndex >= mFragmentSize - 1) {
//                            mCurrentViewPageIndex = 0;
//                        }

                        viewPager.setCurrentItem(mCurrentViewPageIndex++);

                        // 从其他应用切换回图片轮播
                        if ((mCurrentADContentInfo != null) && (mCurrentADContentInfo.getContentType() == 2)) {
                            Intent serviceIntent = new Intent(AdActivity.this, AdPlayService.class);
                            serviceIntent.putExtra("start_app", ContextUtils.get().getPackageName());
                            startService(serviceIntent);
                        }

                        // APP广告
                    } else if (adContentInfo.getContentType() == 2) {
                        Intent serviceIntent = new Intent(AdActivity.this, AdPlayService.class);
                        serviceIntent.putExtra("start_app", adContentInfo.getAppCode());
                        startService(serviceIntent);
                    }
                    mCurrentADContentInfo = adContentInfo;

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
                    if (mTrackingMessage.getUsrsex() != 0 && mTrackingMessage.isStandHere() && !isShowGestureActive) {
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
                        Tools.fadeOut(tvHandsActiveText, 500);
                    } else if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isStandHere()) {
                        removeActivationFragment();
                        Tools.fadeIn(tvHandsActiveText, 500);
//                        VoicePlay.getInstance(AdActivity.this, VoicePlay.SOUNDPOOL).playVoiceBySoundpoolOnce(R.raw.please_put_up_your_hands);
                        startHandsActiveTextAnimation();
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
    private SVGAParser mParser = new SVGAParser(this);
    private FragmentStatePagerAdapter mFragmentStatePagerAdapter;
    private List<ADContentInfo> mAdContentInfoList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        YxLog.i(TAG, "MainActivity onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_second);
        ButterKnife.bind(this);
        Tools.getPermission(this, this);
        EventBus.getDefault().register(this);
        setSocketListener();
        mHandsActiveAnimator = ObjectAnimator.ofFloat(tvHandsActiveText, "translationX", 0, -40);
        if (getIntent() != null && getIntent().getExtras() != null && "userDetect".equals(getIntent().getExtras().getString("launchType"))) {
            isLaunchFromUserDetect = true;
        }
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 5);
        mSoundIdStandFootprint = mSoundPool.load(this, R.raw.please_stand_footprint, 1);
//        mSoundIdPutUpYourHand = mSoundPool.load(this, R.raw.please_put_up_your_hands, 1);
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
        btEnter.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_HOVER_ENTER:
                        mParser.parse("hands_active_ing2.svga", new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                                SVGADrawable svgaDrawable = new SVGADrawable(svgaVideoEntity);
                                btEnter.setImageDrawable(svgaDrawable);
                                btEnter.startAnimation();
                            }

                            @Override
                            public void onError() {

                            }
                        });
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        mParser.parse("hands_active_start.svga", new SVGAParser.ParseCompletion() {
                            @Override
                            public void onComplete(@NotNull SVGAVideoEntity svgaVideoEntity) {
                                SVGADrawable svgaDrawable = new SVGADrawable(svgaVideoEntity);
                                btEnter.setImageDrawable(svgaDrawable);
                                btEnter.setLoops(-1);
                                btEnter.startAnimation();
                            }

                            @Override
                            public void onError() {

                            }
                        });
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
        //test
        tvUsersex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGestureActiveFootPrintView();
            }
        });
        tvStandhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeActivationFragment();
            }
        });
        tvIsactived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        mADContentSize = mAdContentInfoList.size();
        YxLog.d(TAG, "mADContentSize = " + mADContentSize);
        int firstPicIndex = 0;
        for (int i = 0; i < mADContentSize; i++) {
            ADContentInfo adContentInfo = mAdContentInfoList.get(i);
            if (adContentInfo.getContentType() == 1) {//ContentType==1表示广告图片
                if (mFragmentList.isEmpty()) {
                    firstPicIndex = i;
                }
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

        ADContentInfo adContentInfo = mAdContentInfoList.get(firstPicIndex);
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
        mFragmentSize = mFragmentList.size();
        if (mExecutorService == null) {
            mExecutorService = Executors.newSingleThreadExecutor();
        }
        initViewPager();

// 初始化移到这里
        currentPage = AD_PAGE;
//        mPagerPage = mSharedPreferences.getInt("mCurrentPage", 0);
//        mCurrentViewPageIndex = mSharedPreferences.getInt("mCurrentViewPageIndex", 0);
        isPlay = true;
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
                YxLog.i(TAG, "slide ad content: " + i + "/" + (mFragmentSize - 1));
                YxStatistics.version(1).param("cur", i).param("total", mFragmentSize - 1).report("slide_ad_content");
                mCurrentPosition = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if (i != ViewPager.SCROLL_STATE_IDLE) {
                    return;
                }
                if (mCurrentPosition == mFragmentSize - 1) {
//                    YxLog.d(TAG, "last->first");
                    viewPager.setCurrentItem(0, false);
//                    mPagerPage++;
                    mCurrentViewPageIndex++;
                }

            }
        };
        viewPager.setAdapter(mFragmentStatePagerAdapter);
        viewPager.setOnPageChangeListener(mOnPageChangeListener);
        viewPager.setOffscreenPageLimit(2);
    }

    private ADContentPlay getDefaultADContentPlay() {
        ADContentPlay adContentPlay;
        adContentPlay = new ADContentPlay();
        ADContentInfo adContentInfo = new ADContentInfo();
        adContentInfo.setFileUrl(AD_DEFAULT);
        adContentInfo.setContentType(1);
        adContentInfo.setPlayTime(5);
        List<ADContentInfo> adContentInfoList = new ArrayList<>();
        adContentInfoList.add(adContentInfo);
        adContentPlay.setContentPlayVOList(adContentInfoList);
        return adContentPlay;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        YxLog.d(TAG, "--- onNewIntent ---");

        isPlay = true;

    }

    @Override
    protected void onResume() {
        super.onResume();
        isShowGestureActive = false;
//        currentPage = AD_PAGE;
        Tools.hideNavigationBarStatusBar(this, true);
//        mPagerPage = mSharedPreferences.getInt("mCurrentPage", 0);
//        mCurrentViewPageIndex = mSharedPreferences.getInt("mCurrentViewPageIndex", 0);
//        YxLog.d(TAG, "mCurrentPage=" + mPagerPage);
//        isPlay = true;
        Runnable runnable = new Runnable() {
            private JSONObject mJsonObject;

            @Override
            public void run() {
                do {
                    Message message = mHandler.obtainMessage();
                    message.what = PLAY_NEXT;
                    mHandler.sendMessage(message);
                    mCurrentPage = mPagerPage++ % mADContentSize;
                    mCurrentViewPageIndex = mCurrentViewPageIndex % mFragmentSize;
                    YxLog.d(TAG, "Runnable --- mPagerPage = " + mPagerPage + ", mCurrentPage = " + mCurrentPage);
//                    YxLog.d(TAG, "mAdContentInfoList size = " + mAdContentInfoList.size());
//                    YxLog.d(TAG, "mFragmentSize = " + mFragmentSize);
//                    YxLog.d(TAG, "mCurrentPage = " + mCurrentPage);
                    if (mCurrentPage >= mADContentSize) {
                        mCurrentPage = 0;
                    }
                    reportAdPlayPoint();
                    try {
                        Thread.sleep(mAdContentInfoList.get(mCurrentPage == mADContentSize ? 0 : mCurrentPage).getPlayTime() * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (isPlay);
            }

            private void reportAdPlayPoint() {
                if (mJsonObject == null) {
                    mJsonObject = new JSONObject();
                }
                long appPlanId;
                if (mAdContentInfoList.get(mCurrentPage == mADContentSize ? 0 : mCurrentPage).getContentType() == 2) {
                    appPlanId = mAdContentInfoList.get(mCurrentPage == mADContentSize ? 0 : mCurrentPage).getAppPlanId();
                } else {
                    appPlanId = mAdContentInfoList.get(mCurrentPage == mADContentSize ? 0 : mCurrentPage).getAdPlanId();
                }
                mJsonObject.put("type", "1");
                YxStatistics.version(1).param("adplanId", appPlanId).report("aiscreen_ad_play");
            }
        };
        if (mExecutorService != null) {
            mExecutorService.execute(runnable);
        }
    }


    @Override
    protected void onPause() {
        YxLog.i(TAG, "MainActivity onPause");
        super.onPause();
//        isPlay = false;
//        currentPage = OTHER_PAGE;
//        mEditor.putInt("mCurrentPage", mCurrentPage);
//        mEditor.putInt("mCurrentViewPageIndex", mCurrentViewPageIndex);
//        mEditor.commit();
//        removeActivationFragment();
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

        isPlay = false;
        currentPage = OTHER_PAGE;
        mEditor.putInt("mCurrentPage", mCurrentPage);
        mEditor.putInt("mCurrentViewPageIndex", mCurrentViewPageIndex);
        mEditor.commit();
        removeActivationFragment();

        if (mSoundPool != null) {
            mSoundPool.stop(mSoundIdStandFootprint);
            mSoundPool.release();
        }
        if (mExecutorService != null) {
            mExecutorService.shutdown();
        }
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

//        //激活屏幕进入主界面
//        if (mTrackingMessage.getUsrsex() != 0 && currentPage == AD_PAGE && mTrackingMessage.isActived() &&
//                !isShowGestureActive && isLaunchFromUserDetect) {
//            Message message = mHandler.obtainMessage();
//            message.what = ACTIVED;
//            mHandler.sendMessageDelayed(message, 500);
//        }

        //有人且站对位置,显示“请把手移到这里试试”
        if (mTrackingMessage.getUsrsex() != 0 && !mTrackingMessage.isStandHere() && !isShowGestureActive) {
            Tools.fadeIn(tvHandsActiveText, 500);
//            VoicePlay.getInstance(AdActivity.this, VoicePlay.SOUNDPOOL).playVoiceBySoundpoolOnce(R.raw.please_put_up_your_hands);
            startHandsActiveTextAnimation();
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
            mHandler.sendMessageDelayed(message, 600);
//            mHandler.sendMessage(message);
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
            Tools.fadeOut(tvHandsActiveText, 500);
            if (!isSendShowGestureActive) {
                isSendShowGestureActive = true;
                Message message = mHandler.obtainMessage();
                message.what = SHOW_ACTIVE_TIP_FOOTPRINT;
                mHandler.sendMessageDelayed(message, SHOW_ELEPHANT_ACTIVE_GESTURE_DELAY);
            }
        }
        //发送tracking消息给小象。小象根据消息刺激执行下一步动画
        EventBus.getDefault().post(new EventBusMessage(EventBusMessage.ACTIVE_TIP, mTrackingMessage));

    }

    public void startAIScreenApp() {
        YxLog.i(TAG, "goto main page");
        YxStatistics.version(1).param("way", "wave hand").report("goto_main_page");

        // 添加启动主界面参数，为了配合主界面显示引导动画逻辑 2019-5-20 hardy
        Intent homeIntent = new Intent(Intent.ACTION_MAIN, null);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(ContextUtils.get(),
                R.anim.right_in, R.anim.left_out);
        homeIntent.putExtra("message_from", "Ad_Player");
        startActivity(homeIntent, options.toBundle());

        finish();
    }


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

    @Subscribe
    public void onEventBusMessageShow(EventBusMessage eventBusMessage) {
        YxLog.d(TAG, "onEventBusMessageShow");
        if (eventBusMessage.getType() == EventBusMessage.AD_PLAY_CONTENT_UPDATE_BY_BROADCAST) {
            YxLog.d(TAG, "update() mADBroadcastReceiver");
            initData();
            mFragmentStatePagerAdapter.notifyDataSetChanged();
        }
    }

    private void startHandsActiveTextAnimation() {
        if (!mHandsActiveAnimator.isRunning()) {
            mHandsActiveAnimator.setDuration(800);
            mHandsActiveAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mHandsActiveAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mHandsActiveAnimator.start();
        }
    }

}
