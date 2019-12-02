package com.imprexion.adplayer.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.app.Constants;
import com.imprexion.adplayer.base.BaseActivity;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.main.adapter.MyStatePagerAdapter;
import com.imprexion.adplayer.main.content.AdContentImageFragment;
import com.imprexion.adplayer.report.AdPlayerReport;
import com.imprexion.library.YxLog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private static final String TAG = "MainActivity";
    private ArrayList<Fragment> mFragments;
    private List<ADContentInfo> mADContents;
    private int mCurPageIndex;
    private ViewPager mViewPager;

    @Override
    protected void initView() {
        super.initView();
        initViewPager();
        handleStartIntent(getIntent());
    }

    private void initViewPager() {
        mViewPager = findViewById(R.id.viewPager);
        mViewPager.addOnPageChangeListener(this);
    }

    @Override
    public int attachResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleStartIntent(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //如果页面被系统回收调用此方法缓存一下数据，这样从onRestoreInstanceState()中恢复数据
        outState.putInt(Constants.Key.KEY_CURRENT_INDEX, mCurPageIndex);
        outState.putSerializable(Constants.Key.KEY_DATA, ((Serializable) mADContents));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //在此回调中恢复轮播的index和List数据。
        int currentPageIndex = savedInstanceState.getInt(Constants.Key.KEY_CURRENT_INDEX, 0);
        List<ADContentInfo> adContents = (List<ADContentInfo>) savedInstanceState.getSerializable("data");
        mCurPageIndex = currentPageIndex;
        refreshAdData(adContents);
    }

    private void handleStartIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        String messageType = intent.getStringExtra(Constants.Key.KEY_MESSAGE_TYPE);
        if (!Constants.Key.KEY_PLAY_NEXT.equals(messageType)) {
            return;
        }
        List<ADContentInfo> data = intent.getParcelableArrayListExtra(Constants.Key.KEY_DATA);
        boolean isNewData = intent.getBooleanExtra(Constants.Key.KEY_IS_NEW_DATA, true);
        //如果需要刷新数据播放，则先替换数据，然后从index = 0，播放第一个页面；否则直接播放下一个。
        if (mADContents == null || mADContents.size() == 0 || isNewData) {
            YxLog.i(TAG, "handleStartIntent refreshAdData + ");
            refreshAdData(data);
        } else {
            playNext();
        }
    }

    private void playNext() {
        if (mADContents == null || mCurPageIndex < -1) {
            return;
        }
        mCurPageIndex++;
        if (mCurPageIndex > mADContents.size() - 1) {
            mCurPageIndex = 0;
        }
        if (mViewPager != null) {
            mViewPager.setCurrentItem(mCurPageIndex);
        }
    }

    // 刷新ViewPager 的数据
    private void refreshAdData(List<ADContentInfo> newData) {
        mADContents = newData;
        mCurPageIndex = -1;

        int size = mADContents.size();
        if (mADContents != null && size != 0) {

            if (mFragments == null) {
                mFragments = new ArrayList<>();
            }
            mFragments.clear();

            for (int i = 0; i < size; i++) {
                AdContentImageFragment fragment = new AdContentImageFragment();
                ADContentInfo adContentInfo = mADContents.get(i);
                mFragments.add(fragment);
                fragment.setUrl(adContentInfo.getFileName() ,adContentInfo.getFileUrl(), adContentInfo.getFileType()
                        == ADContentInfo.TYPE_VIDEO, size, adContentInfo.getStartApp());
            }
            MyStatePagerAdapter pagerAdapter = new MyStatePagerAdapter(getSupportFragmentManager());
            pagerAdapter.setFragments(mFragments);
            mViewPager.setAdapter(pagerAdapter);
        }
        playNext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mFragments != null) {
            mFragments.clear();
            mFragments = null;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        if (mADContents == null || i < 0 || i >= mADContents.size()) {
            return;
        }
        AdPlayerReport.onLoopAdplayer(mADContents.get(i).getFileName() ,mADContents.get(i).getFileUrl(), mADContents.get(i).getStartApp());
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
