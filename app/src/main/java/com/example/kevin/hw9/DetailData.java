package com.example.kevin.hw9;

import java.io.Serializable;
import java.util.List;

/**
 * Created by kevin on 2017/4/18.
 */

public class DetailData implements Serializable{
    String name;
    String id;
    Picture picture;
    public class Picture
    {
        Data data;
        public class Data
        {
            String height;
            boolean is_sihouette;
            String url;
            String width;
        }
    }
    Albums albums;
    public class Albums
    {
        List<Data> data;
        public class Data
        {
            String name;
            Photos photos;
            public class Photos
            {
                List<PhotoData> data;
                public class PhotoData
                {
                    String name;
                    String picture;
                    String id;
                }

            }
        }
    }
    Posts posts;
    public class Posts
    {
        List<PostData> data;
        public class PostData
        {
            String message;
            String created_time;
        }
    }
}
