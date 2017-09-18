package com.example.kevin.hw9;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.widget.BaseAdapter;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class MyDetailAdapter extends BaseAdapter{
    private List<HashMap<String,Object>> data;
    private LayoutInflater mInflater;
    private Layout layout;
    private Context context;
    public MyDetailAdapter(Context context,List<HashMap<String,Object>> data)
    {
        this.data=data;
        this.context=context;
        this.mInflater = LayoutInflater.from(context);
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
        /*
        if(position==0)
        {
            if (convertView == null) {
                //根据布局文件获取其view返回值
                convertView = mInflater.inflate(R.layout.pictureview, null);
            }
        }
        */
        //if(position==1) {

            if (convertView == null) {
                //根据布局文件获取其view返回值
                convertView = mInflater.inflate(R.layout.postview, null);
            }
            //获取listview中每个Item布局文件中的的子组件的ID
            ImageView image = (ImageView) convertView.findViewById(R.id.post_row_image);
            TextView name = (TextView) convertView.findViewById(R.id.post_name);
            TextView time = (TextView) convertView.findViewById(R.id.created_time);
            TextView containing = (TextView) convertView.findViewById(R.id.post_containing);
            //TextView price_number = (TextView)convertView.findViewById(R.id.price_number);
        String strDate=(String)data.get(position).get("created_time");
        strDate=strDate.substring(0,19);
        strDate=strDate.replace('T',' ');

            //为对应的Item中的各个组件设置资源，从而配置其显示
            Glide.with(context).load((String) data.get(position).get("picture")).into(image);
            name.setText((String) data.get(position).get("name"));
            time.setText(strDate);
            containing.setText((String) data.get(position).get("message"));
       // }
        return convertView;
    }
}
