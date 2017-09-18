package com.example.kevin.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.Layout;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

/**
 * Created by kevin on 2017/4/17.
 */

public class SidebarAdapter extends BaseAdapter{
    private String[] title=new String[]{"Home","Favorite"};
    private int[] icon=new int[]{R.drawable.home, R.drawable.black};
    private LayoutInflater mInflater;
    private Context context;
    private int tag=0;
    public SidebarAdapter(Context context,String[] title,int[] icon)
    {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.title=title;
        this.icon=icon;
        tag=1;
    }
    public SidebarAdapter(Context context)
    {
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount()
    {
        return title.length;
    }


    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        if( tag==0)
        {
            convertView = mInflater.inflate(R.layout.sidebar_layout, null);
            TextView titleView = (TextView) convertView.findViewById(R.id.textView);
            ImageView iconView = (ImageView) convertView.findViewById(R.id.imageView);
            titleView.setText(title[position]);
            iconView.setImageResource(icon[position]);
        }
        else
        {
            convertView = mInflater.inflate(R.layout.about, null);
            TextView titleView = (TextView) convertView.findViewById(R.id.aboutText);
            titleView.setText(title[position]);
        }
        return convertView;
    }
}
