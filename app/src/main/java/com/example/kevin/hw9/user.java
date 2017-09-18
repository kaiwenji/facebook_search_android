package com.example.kevin.hw9;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kevin on 2017/4/21.
 */

public class user {
    String id;
    String name;
    @SerializedName("picture")
    user.picture pic;
    public user(String id,String name,String picture)
    {
        this.id=id;
        this.name=name;
        pic=new user.picture(picture);
    }

    public class picture {
        @SerializedName("data")
        public user.picture.data data2;
        public picture(String url)
        {
            data2=new user.picture.data();
            data2.url=url;
        }
        public class data {
            String height;
            boolean is_silhouette;
            String url;
            String width;
        }
    }
}