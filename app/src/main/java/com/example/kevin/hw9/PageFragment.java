package com.example.kevin.hw9;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import static android.content.Context.MODE_MULTI_PROCESS;
import static android.content.Context.MODE_PRIVATE;
import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by kevin on 2017/4/17.
 */

public class PageFragment extends Fragment {
    public static final String ARGS_PAGE = "args_page";
    public static final String NAME="name";
    public static final String PICTURE="picture";
    public static final String NEXT="next";
    public static final String PREVIOUS="previous";
    public static final String ID="id";
    private int mPage;
    private Cat containing;
    private ArrayList<String> nameList;
    private ArrayList<String> picList;
    private ArrayList<String> idList;
    private String next;
    private String previous;
    private View view;
    private int top;
    private int mposition;
    private boolean isFav;

    static public PageFragment newInstance(int page,Cat example,boolean isFav) {
        Bundle args = new Bundle();
        ArrayList<String>nameList=new ArrayList<>();
        ArrayList<String> picList=new ArrayList<>();
        ArrayList<String>idList=new ArrayList<>();
        for(int i=0;i<example.data1.size();i++)
        {
            idList.add(example.data1.get(i).id);
            nameList.add(example.data1.get(i).name);
            picList.add(example.data1.get(i).pic.data2.url);
        }
        args.putInt(ARGS_PAGE, page);
        if(example.page!=null) {
            args.putString(NEXT, example.page.next);
            args.putString(PREVIOUS, example.page.previous);
        }
        args.putBoolean("isFav",isFav);
        args.putStringArrayList(ID, idList);
        args.putStringArrayList(NAME,nameList);
        args.putStringArrayList(PICTURE,picList);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        next=getArguments().getString(NEXT);
        previous=getArguments().getString(PREVIOUS);
        idList=getArguments().getStringArrayList(ID);
        mPage = getArguments().getInt(ARGS_PAGE);
        nameList=getArguments().getStringArrayList(NAME);
        picList=getArguments().getStringArrayList(PICTURE);
        containing=null;
        isFav=getArguments().getBoolean("isFav");


    }
    @Override
    public void onPause()
    {
        super.onPause();
        getArguments().putInt("top",top);
        getArguments().putInt("position",mposition);
    }
    @Override
    public void onResume()
    {
        super.onResume();
        if(isFav)
        {
            SharedPreferences sp=getContext().getSharedPreferences("data",MODE_MULTI_PROCESS);
            Map<String,String> map=(Map<String,String>)sp.getAll();
            nameList=new ArrayList<>();
            picList=new ArrayList<>();
            idList=new ArrayList<>();
            for(Map.Entry<String, String>  entry : map.entrySet()){
                String tmp=entry.getValue();
                String[] list=tmp.split("#");
                String name=list[0];
                String picture=list[1];
                String id=entry.getKey();
                int type=Integer.parseInt(list[2]);
                if(type==mPage)
                {
                    nameList.add(name);
                    picList.add(picture);
                    idList.add(id);
                }
            }
        }
        ListView listview=(ListView)view.findViewById(R.id.listview);
        List<HashMap<String,String>>mapList=new ArrayList<>();

        for(int i=0;i<nameList.size();i++)
        {
            HashMap<String,String> newMap=new HashMap<>();
            newMap.put("name",nameList.get(i));
            newMap.put("picture",picList.get(i));
            newMap.put("id",idList.get(i));
            mapList.add(newMap);
        }

        listview.setAdapter(new MyAdapter(getContext(),mapList,mPage));
        top=getArguments().getInt("top");
        mposition=getArguments().getInt("position");
        listview.setSelectionFromTop(mposition, top);

    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_layout,container,false);
        ListView listview=(ListView)view.findViewById(R.id.listview);
        List<HashMap<String,String>>mapList=new ArrayList<>();

        for(int i=0;i<nameList.size();i++)
        {
            HashMap<String,String> newMap=new HashMap<>();
            newMap.put("name",nameList.get(i));
            newMap.put("picture",picList.get(i));
            newMap.put("id",idList.get(i));
            mapList.add(newMap);
        }

        listview.setAdapter(new MyAdapter(getContext(),mapList,mPage));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String code=new String();
                code= nameList.get(position) +'#'+ picList.get(position) +'#'+mPage;
                Intent RTD=new Intent(getContext(),DetailsActivity.class);
                RTD.putExtra("id",idList.get(position));
                RTD.putExtra("code",code);
                RTD.putExtra("type",mPage);
                //Toast.makeText(getContext(),"hello",LENGTH_SHORT).show();
                startActivity(RTD);
            }
        });
        Button nextButton=(Button)view.findViewById(R.id.next);
        nextButton.setEnabled(false);
        Button prevButton=(Button)view.findViewById(R.id.pre);
        prevButton.setEnabled(false);
        if(isFav)
        {
            nextButton.setVisibility(View.GONE);
            prevButton.setVisibility(View.GONE);
        }
        else {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Mytask().execute(next);
                }
            });
            prevButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new Mytask().execute(previous);
                }
            });
            if (next != null) {
                nextButton.setEnabled(true);
            }
            if (previous != null) {
                prevButton.setEnabled(true);
            }
        }


        return view;
    }

    private class Mytask extends AsyncTask<String,Integer,String>
    {
        protected void onPreExecute() {
            super.onPreExecute();

        }

        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(String... params) {

            StringBuffer buffer=new StringBuffer();

            try {
                //Toast.makeText(MainActivity.this,params[0],Toast.LENGTH_SHORT);
                URL url=new URL(params[0]);
                HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                if(conn.getResponseCode()==200) {
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String s = "";
                    if ((s = reader.readLine()) != null) {
                        buffer.append(s);
                    }
                    is.close();
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return buffer.toString();

            //return null;
        }


        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String r) {
            super.onPostExecute(r);
            containing=new Gson().fromJson(r,Cat.class);
            //category.set(curCat,example);
            List<HashMap<String,String>> mapList=new ArrayList<>();
            for(int i=0;i<containing.data1.size();i++)
            {
                nameList.set(i,containing.data1.get(i).name);
                picList.set(i,containing.data1.get(i).pic.data2.url);
                HashMap<String,String> tmp=new HashMap<>();
                tmp.put("name",containing.data1.get(i).name);
                tmp.put("picture",containing.data1.get(i).pic.data2.url);
                tmp.put("id",containing.data1.get(i).id);
                mapList.add(tmp);
            }
            ListView listview= (ListView)view.findViewById(R.id.listview);
            listview.setAdapter(new MyAdapter(getContext(),mapList,mPage));
            Button nextButton=(Button)view.findViewById(R.id.next);
            nextButton.setEnabled(false);
            Button prevButton=(Button)view.findViewById(R.id.pre);
            prevButton.setEnabled(false);
            if(containing.page.next!=null)
            {
                next=containing.page.next;
                nextButton.setEnabled(true);
            }
            if(containing.page.previous!=null)
            {
                previous=containing.page.previous;
                prevButton.setEnabled(true);
            }
        }
    }

}
