package com.imprexion.aiscreen.advertising;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.imprexion.aiscreen.R;
import com.imprexion.aiscreen.main.MainActivity;
import com.imprexion.aiscreen.tools.Tools;

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

    private List<String> mImges = new ArrayList<>();
    private String mAdpath = Environment.getExternalStorageDirectory() + "/imprexion_ad";
    private final static String TAG = "AdvertisingActivity";
    private Message mMessage;
    private Thread mThread;
    private final static int NEXT_AD = 1;
    private final static int NEXT_AD_2 = 2;
    private ObjectAnimator mAdEnterObjAnimator;
    private ObjectAnimator mAdExitObjAnimator;
    private ObjectAnimator mAdEnterObjAnimator_1;
    private ObjectAnimator mAdExitObjAnimator_1;
    private AnimatorSet mAnimatorSet = new AnimatorSet();
    private AnimatorSet mAnimatorSet_2 = new AnimatorSet();
    private int i;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NEXT_AD:
                    Glide.with(AdvertisingActivity.this)
                            .load(mAdpath + "/" + mImges.get(i++ % mImges.size()))
                            .into(ivAd);
                    mAnimatorSet.play(mAdEnterObjAnimator).with(mAdExitObjAnimator_1);
                    mAnimatorSet.setDuration(300);
                    mAnimatorSet.setInterpolator(new DecelerateInterpolator());
                    mAnimatorSet.start();
                    break;
                case NEXT_AD_2:
                    Glide.with(AdvertisingActivity.this)
                            .load(mAdpath + "/" + mImges.get(i++ % mImges.size()))
                            .into(ivAd2);
                    mAnimatorSet_2.play(mAdEnterObjAnimator_1).with(mAdExitObjAnimator);
                    mAnimatorSet_2.setDuration(300);
                    mAnimatorSet_2.setInterpolator(new DecelerateInterpolator());
                    mAnimatorSet_2.start();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising);
        ButterKnife.bind(this);
        mAdEnterObjAnimator = ObjectAnimator.ofFloat(ivAd, "translationX", 1080, 0);
        mAdExitObjAnimator = ObjectAnimator.ofFloat(ivAd, "translationX", 0, -1080);
        mAdEnterObjAnimator_1 = ObjectAnimator.ofFloat(ivAd2, "translationX", 1080, 0);
        mAdExitObjAnimator_1 = ObjectAnimator.ofFloat(ivAd2, "translationX", 0, -1080);
//        getSupportFragmentManager().beginTransaction().add(R.id.fl_fragment, new GestureActivationFragment()).commitAllowingStateLoss();
        getImges();
        rlAdvertising.setOnClickListener(this);

    }

    private void getImges() {
        mImges = Tools.getFilesAllName(mAdpath);
        Log.d(TAG, "mImges size = " + mImges.size());

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
                    do {
                        mMessage = mHandler.obtainMessage();
                        i = i == 1 ? 0 : 1;
                        mMessage.what = i + 1;
                        mHandler.sendMessage(mMessage);
                        try {
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } while (true);
                }
            });
            mThread.start();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (mThread.isAlive()) {
//            mThread.destroy();
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_advertising:
                startActivity(new Intent(this, MainActivity.class));
                break;
            default:
                break;
        }
    }
}




