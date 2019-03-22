package com.imprexion.aiscreen.main;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.bean.EventBusMessage;
import com.imprexion.aiscreen.functionPart.BrowserActivity;
import com.imprexion.aiscreen.functionPart.WebViewActivity;
import com.imprexion.aiscreen.status.StatusFragment;
import com.imprexion.aiscreen.tools.ScreenUtils;
import com.imprexion.aiscreen.tools.Tools;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ScreenUtils.NavigationListener {

    @BindView(R.id.rl_status)
    RelativeLayout rlStatus;
//    @BindView(R.id.fl_back)
//    FrameLayout flBack;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
//    @BindView(R.id.rv_1)
//    RippleView rv1;
//    @BindView(R.id.rv_2)
//    RippleView rv2;
//    @BindView(R.id.rv_3)
//    RippleView rv3;
//    @BindView(R.id.lav)
//    LottieAnimationView lav;
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
    private StatusFragment mStatusFragment;
    private boolean bgFlag = true;
    private int ZOOM_WIDTH;
    private int ZOOM_HEIGHT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setMainBg();
        initStatus();
        ZOOM_WIDTH = (int) getResources().getDimension(R.dimen.title_width);
        ZOOM_HEIGHT = (int) getResources().getDimension(R.dimen.title_height);
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
        Tools.hideNavigationBarStatusBar(this, true);
        setOnHoverListener(ivNavigation);
        setOnHoverListener(ivPark);
        setOnHoverListener(ivMembership);
        setOnHoverListener(ivPromotion);
        setOnHoverListener(ivLottery);
        setOnHoverListener(ivEmojidancer);
//        ScreenUtils.setNavigationListener(rlMain, this);
//        rv1.startSpread();
//        rv2.startSpread();
//        rv3.startSpread();
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
                startActivity(new Intent(this, BrowserActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/Floor"));
                break;
            case R.id.iv_park:
                startActivity(new Intent(this, BrowserActivity.class).putExtra("url", URL + "app/park"));
                break;
            case R.id.iv_membership:
                startActivity(new Intent(this, BrowserActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/VipCard"));
                break;
            case R.id.iv_promotion:
                startActivity(new Intent(this, BrowserActivity.class).putExtra("url", "http://m.mallcoo.cn/a/custom/10919/SGT/Promotion"));
                break;
            case R.id.iv_lottery:
                startActivity(new Intent(this, BrowserActivity.class).putExtra("url", "http://oss.imprexion.cn/application/lottery/index.html"));
                break;
            case R.id.iv_emojidancer:
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
        Log.d(TAG, "navigation show");
        rlMain.postInvalidate();
        rlMain.invalidate();
    }

    @Override
    public void hide() {
        Log.d(TAG, "navigation hide");

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateBgByTime(EventBusMessage eventBusMessage) {

    }
}
