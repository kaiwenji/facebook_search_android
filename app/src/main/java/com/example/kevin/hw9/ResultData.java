package com.example.kevin.hw9;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2017/4/17.
 */

public class ResultData {
    private List<Cat> datalist;

    public ResultData(Bean bean) {
        datalist=new ArrayList<>();
        for(int i=0;i<bean.name.size();i++)
        {
            datalist.add(bean.name.get(i));
        }
    }

    public Cat get(int i)
    {
        return datalist.get(i);
    }
    public void update(int i,Cat newData)
    {
        datalist.set(i,newData);
    }
}
