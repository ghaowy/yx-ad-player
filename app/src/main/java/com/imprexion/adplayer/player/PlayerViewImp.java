package com.imprexion.adplayer.player;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.imprexion.adplayer.R;
import com.imprexion.adplayer.bean.ADContentInfo;
import com.imprexion.adplayer.bean.ADContentPlay;
import com.imprexion.adplayer.main.content.AdContentImageFragment;
import com.imprexion.library.YxLog;
import com.imprexion.library.YxStatistics;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Xianquan Feng
 * Email: xianquan.feng@imprexion.cn
 * Date: 2019/6/28
 * Desc: 负责播放图片广告轮播的View。当前版本此View已播放图片为主。播放应用，即切换应用。不需要界面做内容切换。
 */
public class PlayerViewImp implements IPlayerView {

    private static final String TAG = "PlayerViewImp";

    private AppCompatActivity mActivity;
    private List<Fragment> mFragmentList;
    /*需要播放的图片类型广告*/
    private List<ADContentInfo> mADContents;
    private int mCurPageIndex = -1;

    private ViewPager mViewPager;

    public PlayerViewImp(AppCompatActivity activity) {
        mActivity = activity;
        initView();
    }

    @Override
    public void playerNext() {
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

    @Override
    public void updateAds(List<ADContentInfo> adContentPlay) {
        mADContents = adContentPlay;
        mFragmentList.clear();
        mCurPageIndex = -1;
        if (mADContents != null) {
            for (int i = 0; i < adContentPlay.size(); i++) {
                AdContentImageFragment fragment = new AdContentImageFragment();
                mFragmentList.add(fragment);
                fragment.setUrl(adContentPlay.get(i).getFileUrl() , adContentPlay.get(i).getFileType() == ADContentInfo.TYPE_VIDEO);
            }
            MyStatePagerAdapter adapter = new MyStatePagerAdapter(mActivity.getSupportFragmentManager());
            adapter.setFragments(mFragmentList);
            mViewPager.setAdapter(adapter);
        }
    }

    public void initData() {
        PlayerModel playerModel = new PlayerModel();
        ADContentPlay adContentPlay = playerModel.getLocalAds();
        if (adContentPlay != null) {
            mADContents = adContentPlay.getContentPlayVOList();
        }
        updateAds(mADContents);
    }

    public List<ADContentInfo> getADContents() {
        return mADContents;
    }

    public int getCurPageIndex() {
        return mCurPageIndex;
    }

    public void setCurPageIndex(int curPageIndex) {
        this.mCurPageIndex = curPageIndex;
    }

    public void setADContents(List<ADContentInfo> adContents) {
        this.mADContents = adContents;
    }

    private void initView() {
        mViewPager = mActivity.findViewById(R.id.viewPager);
        mViewPager.setOnPageChangeListener(mOnPageChangeListener);
        mViewPager.setOffscreenPageLimit(2);
        if (mFragmentList == null) {
            mFragmentList = new ArrayList<>();
        }
        mFragmentList.clear();
    }

    class MyStatePagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments;

        public MyStatePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void setFragments(List<Fragment> fragments) {
            this.fragments = fragments;
        }

        @Override
        public int getCount() {
            if (fragments != null) {
                return fragments.size();
            }
            return 0;
        }

        @Override
        public Fragment getItem(int i) {
            if (fragments != null) {
                return fragments.get(i);
            }
            return null;
        }
    }

    ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            YxLog.i(TAG, "slide ad content: " + (i + 1) + "/" + (mFragmentList.size()));
            YxStatistics.version(1).param("cur", (i + 1)).param("total", mFragmentList.size()).report("slide_ad_content");
        }

        @Override
        public void onPageScrollStateChanged(int i) {
            if (i != ViewPager.SCROLL_STATE_IDLE) {
                return;
            }
        }
    };
}
