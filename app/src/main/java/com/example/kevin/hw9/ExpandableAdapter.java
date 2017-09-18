package com.example.kevin.hw9;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kevin on 2017/4/19.
 */

public class ExpandableAdapter extends BaseExpandableListAdapter{
    private HashMap<String,List<String>> dataset;
    private List<String> parentList;
    private Context context;
    private LayoutInflater mInflater;

    public ExpandableAdapter(Context context, HashMap<String,List<String>> data,List<String> parentList)
    {
        this.dataset=data;
        this.parentList=parentList;
        this.context=context;
        mInflater=LayoutInflater.from(context);
    }
        //  获得某个父项的某个子项
        @Override
        public Object getChild(int parentPos, int childPos) {
            return dataset.get(parentList.get(parentPos)).get(childPos);
        }

        //  获得父项的数量
        @Override
        public int getGroupCount() {
            return dataset.size();
        }

        //  获得某个父项的子项数目
        @Override
        public int getChildrenCount(int parentPos) {
            return 1;
        }

        //  获得某个父项
        @Override
        public Object getGroup(int parentPos) {
            return dataset.get(parentList.get(parentPos));
        }

        //  获得某个父项的id
        @Override
        public long getGroupId(int parentPos) {
            return parentPos;
        }

        //  获得某个父项的某个子项的id
        @Override
        public long getChildId(int parentPos, int childPos) {
            return childPos;
        }

        //  按函数的名字来理解应该是是否具有稳定的id，这个方法目前一直都是返回false，没有去改动过
        @Override
        public boolean hasStableIds() {
            return false;
        }

        //  获得父项显示的view
        @Override
        public View getGroupView(int parentPos, boolean b, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view=mInflater.inflate(R.layout.pictureview,null);
            }
            TextView name=(TextView)view.findViewById(R.id.picListTitle);
            name.setText(parentList.get(parentPos));

            return view;
        }

        //  获得子项显示的view
        @Override
        public View getChildView(int parentPos, int childPos, boolean b, View view, ViewGroup viewGroup) {
            if(view==null)
            {
                view=mInflater.inflate(R.layout.child_layout,null);
            }
            List<String> picList=dataset.get(parentList.get(parentPos));
            LinearLayout tmpLayout=(LinearLayout)view.findViewById(R.id.picture_layout);
            tmpLayout.removeAllViews();
            for(int i=0;i<picList.size();i++)
            {
                ImageView newImage=new ImageView(context);
                Glide.with(context).load((String)picList.get(i)).into(newImage);
                LinearLayout.LayoutParams ll=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                ll.setMargins(0,0,0,50);
                newImage.setLayoutParams(ll);
                tmpLayout.addView(newImage);

            }
            return view;
        }

        //  子项是否可选中，如果需要设置子项的点击事件，需要返回true
        @Override
        public boolean isChildSelectable(int i, int i1) {
            return false;
        }


    }
