package com.example.kevin.hw9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by kevin on 2017/4/17.
 */

public class MyAdapter extends BaseAdapter{
    private List<HashMap<String,String>> data;
    private LayoutInflater mInflater;
    private Layout layout;
    private Context context;
    private int page;
    public MyAdapter(Context context,List<HashMap<String,String>> data,int page)
    {
        this.data=data;
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
        this.page=page;
        this.layout=layout;
    }

    public int getCount()
    {
        return data.size();
    }


    public Object getItem(int arg0) {
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            //根据布局文件获取其view返回值
            convertView = mInflater.inflate(R.layout.mylistview, null);
        }
        //获取listview中每个Item布局文件中的的子组件的ID
        final String id=data.get(position).get("id");
        final String n=data.get(position).get("name");
        final String pic=data.get(position).get("picture");
        final String type=String.valueOf(page);
        ImageView image = (ImageView)convertView.findViewById(R.id.row_image);
        TextView name = (TextView)convertView.findViewById(R.id.row_name);
        ImageView detail=(ImageView)convertView.findViewById(R.id.bay);
        ImageView star=(ImageView)convertView.findViewById(R.id.real);
        SharedPreferences localStorage=context.getSharedPreferences("data",Context.MODE_MULTI_PROCESS);
        if(localStorage.contains(id))
        {
            star.setImageResource(R.drawable.favorites_on);
        }
        //TextView price_number = (TextView)convertView.findViewById(R.id.price_number);

        //为对应的Item中的各个组件设置资源，从而配置其显示
        Glide.with(context).load((String)data.get(position).get("picture")).into(image);
        name.setText((String)data.get(position).get("name"));
        return convertView;
    }
}
