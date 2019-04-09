package com.imprexion.aiscreen.main;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.imprexion.aiscreen.net.NetPresenter;
import com.imprexion.aiscreen.tools.ALog;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.alibaba.fastjson.JSON;
import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.advertising.AdSecondActivity;
import com.imprexion.aiscreen.bean.ADContentPlay;
import com.imprexion.aiscreen.bean.EventBusMessage;
import com.imprexion.aiscreen.bean.MessageForAIScreenPB;
import com.imprexion.aiscreen.functionPart.WebViewActivity;
import com.imprexion.aiscreen.service.AISService;
import com.imprexion.aiscreen.service.TcpClientConnector;
import com.imprexion.aiscreen.status.StatusFragment;
import com.imprexion.aiscreen.tools.ScreenUtils;
import com.imprexion.aiscreen.tools.Tools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ScreenUtils.NavigationListener {

    @BindView(R.id.rl_status)
    RelativeLayout rlStatus;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.lav)
    LottieAnimationView lav;
    @BindView(R.id.iv_navigation)
    ImageView ivNavigation;
    @BindView(R.id.iv_park)
    ImageView ivPark;
    @BindView(R.id.iv_membership)
    ImageView ivMembership;
    @BindView(R.id.iv_promotion)
    ImageView ivPromotion;
    @BindView(R.id.iv_lottery)
    ImageView ivLottery;
    @BindView(R.id.iv_emojidancer)
    ImageView ivEmojidancer;
    private static final String TAG = "MainActivity";
    private static final String URL = "http://172.16.2.207:5000/";
    private static final int NO_PERSON = 0;
    private static final int OTHER_PAGE = 100;
    private static final int AD_PAGE = 101;
    public static final String AD_SUFFIX = "ADContent";
    private int currentPage = 100;//100:MainActivity;101:Ad;
    @BindView(R.id.tv_wavehands)
    TextView tvWavehands;
    private StatusFragment mStatusFragment;
    private int ZOOM_WIDTH;
    private int ZOOM_HEIGHT;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private NetPresenter mNetPresenter;
    private ServiceConnection mConnection;
    private TcpClientConnector mTcpClientConnector = TcpClientConnector.getInstance();
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NO_PERSON:
                    startAdActivity();
                    break;
                default:
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setMainBg();
        initStatus();
        ZOOM_WIDTH = (int) getResources().getDimension(R.dimen.title_width);
        ZOOM_HEIGHT = (int) getResources().getDimension(R.dimen.title_height);
        setOnHoverListener(ivNavigation);
        setOnHoverListener(ivPark);
        setOnHoverListener(ivMembership);
        setOnHoverListener(ivPromotion);
        setOnHoverListener(ivLottery);
        setOnHoverListener(ivEmojidancer);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Tools.hideNavigationBarStatusBar(this, hasFocus);
    }

    private void setMainBg() {
        Date date = new Date();
    }

    private void initStatus() {

        if (mStatusFragment == null) {
            mStatusFragment = new StatusFragment();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.rl_status, mStatusFragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    @Override
    protected void onResume() {
        super.onResume();
        currentPage = OTHER_PAGE;
        Tools.hideNavigationBarStatusBar(this, true);
        bindAISService();
        setSocketListener();
        lav.setImageAssetsFolder("imagesGohomeDay");
        lav.setAnimation("gohome_day_json.json");
        lav.playAnimation();
        //test
        tvWavehands.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAdActivity();
            }
        });
    }

    private void startAdActivity() {
        startActivity(new Intent(MainActivity.this, AdSecondActivity.class));
    }

    private void setSocketListener() {
        mTcpClientConnector.createConnect("10.2.26.185", 23333);
        mTcpClientConnector.setOnConnectListener(new TcpClientConnector.ConnectListener() {
            @Override
            public void onReceiveData(MessageForAIScreenPB.MessageForAIScreen messageForAIScreen) {
                String mData = "sex=" + messageForAIScreen.getUsrsex() + " ;HavePerson=" + messageForAIScreen.getStandHere() + " ;IsActived=" + messageForAIScreen.getIsActived();
                ALog.d(TAG, "sex=" + messageForAIScreen.getUsrsex());
                ALog.d(TAG, "HavePerson=" + messageForAIScreen.getStandHere());
                ALog.d(TAG, "IsActived=" + messageForAIScreen.getIsActived());
                dispatchMessage(messageForAIScreen);
            }
        });
    }

    private void dispatchMessage(MessageForAIScreenPB.MessageForAIScreen messageForAIScreen) {
        int usrsex = messageForAIScreen.getUsrsex();
        boolean standHere = messageForAIScreen.getStandHere();
        boolean isActived = messageForAIScreen.getIsActived();
        ALog.d(TAG, "usrsex=" + usrsex + " ;standHere=" + standHere + " ;isActived=" + isActived);
        if (usrsex == 0 && currentPage != AD_PAGE) {
            currentPage = AD_PAGE;
            Message message = mHandler.obtainMessage();
            message.what = NO_PERSON;
            mHandler.sendMessageDelayed(message, 200);
        }
//        EventBus.getDefault().post(new EventBusMessage(EventBusMessage.STANDHERE, standHere));
    }

    private void bindAISService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                AISService.AISBinder aisBinder = (AISService.AISBinder) service;
                aisBinder.getService().setContentInfoToActivity(new AISService.IContentInfoCallBack() {
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
        Intent intent = new Intent(this, AISService.class);
        intent.putExtra("src", 0);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

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
        mEditor.putString(adContentPlay.getPlayDate() + AD_SUFFIX, content);
        ALog.d(TAG, "adContentPlay.getPlayDate()=" + adContentPlay.getPlayDate());
        ALog.d(TAG, "getCurrentDate()=" + Tools.getCurrentDate("yyyy-MM-dd"));
        ALog.d(TAG, "adContentPlay=" + JSON.toJSONString(adContentPlay));
        mEditor.commit();
        EventBusMessage eventBusMessage = new EventBusMessage(EventBusMessage.AD_PLAY_CONTENT, adContentPlay);
        EventBus.getDefault().post(eventBusMessage);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if (rv1 != null) {
//            rv1.stopSpread();
//        }
//        if (rv2 != null) {
//            rv2.stopSpread();
//        }
//        if (rv3 != null) {
//            rv3.stopSpread();
//        }
    }


    @OnClick({R.id.iv_navigation, R.id.iv_park, R.id.iv_membership, R.id.iv_promotion, R.id.iv_lottery, R.id.iv_emojidancer, R.id.rl_main})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_navigation:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/Floor"));
                break;
            case R.id.iv_park:
//                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", URL + "app/park"));
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", "http://120.79.245.58:88/#/app/park"));
                break;
            case R.id.iv_membership:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/VipCard"));
                break;
            case R.id.iv_promotion:
                ALog.d(TAG, "back button click");
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/Promotion"));
                break;
            case R.id.iv_lottery:
                startActivity(new Intent(this, WebViewActivity.class).putExtra("url", "http://oss.imprexion.cn/application/lottery/index.html"));
                break;
            case R.id.iv_emojidancer:
                if (mNetPresenter == null) {
                    mNetPresenter = new NetPresenter();
                }
//                mNetPresenter.onADCallback("广告推送成功~~~~");
                if (mSharedPreferences == null) {
                    mSharedPreferences = getSharedPreferences("AIScreenSP", Context.MODE_PRIVATE);
                    mEditor = mSharedPreferences.edit();
                }
                String adContentPlayString = mSharedPreferences.getString(Tools.getCurrentDate("yyyy-MM-dd") + MainActivity.AD_SUFFIX, null);
                if (adContentPlayString == null) {
                    ALog.d(TAG, "adContentPlayString is null");
                    return;
                } else {
                    ALog.d(TAG, "adContentPlayString=" + adContentPlayString);
                }
                ADContentPlay adContentPlay = JSON.parseObject(adContentPlayString, ADContentPlay.class);
                mNetPresenter.onADCallback(adContentPlay);
//                startActivity(new Intent(this, JsonActivity.class).putExtra("url", URL + "app"));
//                AISApplication.getSmdtManager().setMouseIcon(i++%5);
                break;
//            case R.id.fl_back:
//                onBackPressed();
//                break;
//            case R.id.rl_main:
//                bgFlag = !bgFlag;
//                setMainBg();
//                break;
            default:
                break;
        }

    }

    private void setOnHoverListener(final View view) {
        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch (what) {
                    case MotionEvent.ACTION_HOVER_ENTER: //鼠标进入view
                        Drawable drawable1 = getResources().getDrawable(R.drawable.hover);
                        Drawable zoomDrawable1 = Tools.zoomDrawable(drawable1, ZOOM_WIDTH, ZOOM_HEIGHT);
                        ((ImageView) view).setScaleType(ImageView.ScaleType.CENTER);
                        ((ImageView) view).setImageDrawable(zoomDrawable1);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT: //鼠标离开view
                        ((ImageView) view).setImageDrawable(null);
                        break;
                }
                return false;
            }

        });
    }

    @Override
    public void show() {
        ALog.d(TAG, "navigation show");
        rlMain.postInvalidate();
        rlMain.invalidate();
    }

    @Override
    public void hide() {
        ALog.d(TAG, "navigation hide");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBgByTime(EventBusMessage eventBusMessage) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
