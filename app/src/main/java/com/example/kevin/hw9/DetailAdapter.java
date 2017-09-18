package com.example.kevin.hw9;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/4/17.
 */

class DetailAdapter extends FragmentPagerAdapter {
    public final int COUNT = 2;
    private String[] titles = new String[]{"albums", "posts"};
    private Context context;
    private DetailData result;
    private FragmentManager fm;
    private List<DetailFragment> fragments;

    public DetailAdapter(FragmentManager fm, Context context,DetailData result) {
        super(fm);
        this.fm=fm;
        this.context = context;
        this.result=result;
        fragments=new ArrayList<>();

    }

    @Override
    public Fragment getItem(int position) {
        DetailFragment tmp=DetailFragment.newInstance(position,result);
        fragments.add(tmp);
        return tmp;
    }
    public void setFragments(ArrayList fragments) {
        if(this.fragments != null){
            FragmentTransaction ft = fm.beginTransaction();
            for(Fragment f:this.fragments){
                ft.remove(f);
            }
            ft.commit();
            ft=null;
            fm.executePendingTransactions();
        }
        this.fragments = fragments;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return COUNT;
    }

}
