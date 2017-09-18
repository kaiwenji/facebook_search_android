package com.example.kevin.hw9;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kevin on 2017/4/17.
 */

public class Cat {
    @SerializedName("data")
    List<user> data1;

    @SerializedName("paging")
    Page page;

    public class Page {
        String next;
        String previous;
    }

}
