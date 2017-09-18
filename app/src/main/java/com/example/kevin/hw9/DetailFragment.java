package com.example.kevin.hw9;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by kevin on 2017/4/17.
 */

public class DetailFragment extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    public static final String DATA="data";
    private int mPage;
    private View parentview;
    private View childview;
    private List<String> nameList;
    private List<String> picList;
    private DetailData result;
    private int count;

    static public DetailFragment newInstance(int page,DetailData result) {
        Bundle args = new Bundle();
        ArrayList<String> picList;
        args.putInt(ARGS_PAGE, page);
        args.putSerializable(DATA,result);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage=getArguments().getInt("args_page");
        result=(DetailData) getArguments().getSerializable("data");

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View pictureview=inflater.inflate(R.layout.picture_fragment_layout,container,false);
        View postview=inflater.inflate(R.layout.detail_fragment_layout,container,false);
        final ExpandableListView pictureListView=(ExpandableListView)pictureview.findViewById(R.id.pictureListView);
        ListView postListView=(ListView)postview.findViewById(R.id.postListView);

        if(mPage==0) {

            List<String> parentList=new ArrayList<>();
            HashMap<String, List<String>> tmp = new HashMap<>();
            if(result.albums==null)
            {
                return inflater.inflate(R.layout.picture_null_layout,container,false);
            }
            int size = Math.min(result.albums.data.size(), 5);
            for (int i = 0; i < size; i++) {
                String albumsName=result.albums.data.get(i).name;
                List<String> picList = new ArrayList<>();
                tmp.put(albumsName,picList);
                parentList.add(albumsName);
                if(result.albums.data.get(i).photos!=null) {
                    int ps = Math.min(result.albums.data.get(i).photos.data.size(), 2);
                    for (int j = 0; j < ps; j++) {
                        picList.add(new String(result.albums.data.get(i).photos.data.get(j).picture));
                    }
                }
                tmp.put(albumsName, picList);

            }
            ExpandableAdapter adapter= new ExpandableAdapter(getContext(),tmp,parentList);
            pictureListView.setAdapter(adapter);
            count=adapter.getGroupCount();
            pictureListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    for (int i = 0; i < count; i++) {
                        if (groupPosition != i) {
                            pictureListView.collapseGroup(i);
                        }
                    }
                }
            });
            return pictureview;

        }

        else {
            if(result.posts==null)
            {
                return inflater.inflate(R.layout.post_null_layout,container,false);
            }
            int size = Math.min(result.posts.data.size(), 5);
            List<HashMap<String,Object>> mapList=new ArrayList<>();
            for (int i = 0; i < size; i++) {
                HashMap<String, Object> tmp = new HashMap<>();
                tmp.put("message",result.posts.data.get(i).message);
                tmp.put("name",result.name);
                tmp.put("created_time",result.posts.data.get(i).created_time);
                tmp.put("picture",result.picture.data.url);
                mapList.add(tmp);
            }
            postListView.setAdapter(new MyDetailAdapter(getContext(),mapList));
            return postview;
       }

    }
}
