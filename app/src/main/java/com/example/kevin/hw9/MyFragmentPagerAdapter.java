package com.example.kevin.hw9;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/4/17.
 */

class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    public final int COUNT = 5;
    private String[] titles = new String[]{"user", "page", "event", "place", "group"};
    private Context context;
    private Bean bean;
    private FragmentManager fm;
    private List<PageFragment> fragments;
    private boolean isFav;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context,Bean bean,boolean isFav) {
        super(fm);
        fragments=new ArrayList<>();
        this.context = context;
        this.bean=bean;
        this.fm=fm;
        this.isFav=isFav;
    }

    @Override
    public Fragment getItem(int position) {
        PageFragment tmp=PageFragment.newInstance(position,bean.name.get(position),isFav);
        fragments.add(tmp);
        return tmp;
    }

    @Override
    public int getCount() {
        return COUNT;
    }
    //@Override
    //public CharSequence getPageTitle(int position) {
        //return titles[position];
    //}
}
