package com.imprexion.aiscreen.advertising;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.imprexion.aiscreen.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdSecondActivity extends AppCompatActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.btn_change_vp)
    Button btnChangeVp;

    private String picUrl = "https://cn.bing.com/th?id=OHR.SakuraFes_ZH-CN1341601988_1920x1080.jpg&rf=NorthMale_1920x1080.jpg";
    private String picUrl1 = "https://cn.bing.com/sa/simg/hpb/NorthMale_EN-US8782628354_1366x768.jpg";
    private String picUrl2 = "https://cn.bing.com/th?id=OHR.AthensNight_ZH-CN1280970241_1920x1080.jpg&rf=NorthMale_1920x1080.jpg";

    private String[] pics = {picUrl, picUrl1, picUrl2};

    private List<Fragment> mFragmentList;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private int i;
    private ExecutorService mExecutorService;
    private final static String TAG = "AdSecondActivity";
    private boolean isPlay = true;
    private int mCurrentPosition;
    private int mSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_second);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        mExecutorService = Executors.newSingleThreadExecutor();
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Fragment fragment = new AdContentImageFragment();
            ((AdContentImageFragment) fragment).setUrl(pics[i]);
            mFragmentList.add(fragment);
        }
//        Fragment fragment = new AdContentImageFragment();
//        ((AdContentImageFragment) fragment).setUrl(pics[0]);
//        mFragmentList.add(fragment);
//        mFragmentList.add(mFragmentList.get(0));
//        mSize = mFragmentList.size();


    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewPager();
        btnChangeVp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(i++ % 3);
            }
        });
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                do {
                    viewPager.setCurrentItem(i++ % 3);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "runnable next");
                } while (isPlay);
            }
        };
        mExecutorService.execute(runnable);
    }

    private void initViewPager() {
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragmentList.get(i);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
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
//                if (i == ViewPager.SCROLL_STATE_IDLE) {
//                    return;
//                }
//                if (mCurrentPosition == mSize - 1) {
//                    viewPager.setCurrentItem(0, false);
//                }

            }
        };
        viewPager.setAdapter(mFragmentPagerAdapter);
//        viewPager.setOnPageChangeListener(mOnPageChangeListener);
        viewPager.setOffscreenPageLimit(2);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        isPlay = false;
//        mExecutorService.shutdown();
    }
}
