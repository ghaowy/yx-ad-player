package com.imprexion.adplayer.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyStatePagerAdapter extends FragmentStatePagerAdapter {

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