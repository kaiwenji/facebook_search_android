package com.example.kevin.hw9;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telecom.Call;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by kevin on 2017/4/13.
 */

public class DetailsActivity extends AppCompatActivity{
    private String id;
    private String name;
    private String url;
    private static final String ADDR="http://cs-server.usc.edu:35614/index.php?callback=OMG&id=";
    private ArrayList<HashMap<String,String>> mapList;
    private String code;
    private SharedPreferences localStorage;
    private String[] title=new String[]{"Albums","Posts"};
    private int[] icon=new int[]{R.drawable.albums,R.drawable.posts};

    private CallbackManager callback;
    private FacebookCallback facebookCallback = new FacebookCallback() {

        @Override
        public void onSuccess(Object o) {
            Toast.makeText(getApplicationContext(),"You shared this post",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
        }
        public void onError(FacebookException e)
        {
            e.printStackTrace();
        }

    };

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_layout);
        Toolbar mToolBar=(Toolbar)findViewById(R.id.detailToolBar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent legacy=getIntent();
        id=legacy.getStringExtra("id");
        code=legacy.getStringExtra("code");
        int type=legacy.getIntExtra("type",0);
        localStorage=getSharedPreferences("data",MODE_MULTI_PROCESS);
        new Mytask().execute(ADDR+id+"&type="+type);
        mapList=new ArrayList<>();
/*

        */

    }
    private void test(DetailData result)
    {
        TabLayout layout=(TabLayout)findViewById(R.id.detail_tab);
        ViewPager viewPager=(ViewPager)findViewById(R.id.detail_viewpager);
        viewPager.setAdapter(new DetailAdapter(getSupportFragmentManager(),this,result));
        layout.setupWithViewPager(viewPager);
        for(int i=0;i<2;i++) {
            layout.getTabAt(i).setIcon(icon[i]).setText(title[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item=menu.findItem(R.id.action_settings);
        SharedPreferences localStorage=getSharedPreferences("data",MODE_MULTI_PROCESS);
        if(localStorage.contains(id))
        {
            item.setTitle("remove from Favorites");
        }
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


            callback.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences.Editor editor = localStorage.edit();
            if(localStorage.contains(this.id))
            {
                editor.remove(this.id);
                editor.commit();
                Toast.makeText(DetailsActivity.this,"Removed from Favorites!",Toast.LENGTH_SHORT).show();
            }
            else {
                editor.putString(this.id, code);
                editor.commit();
                Toast.makeText(DetailsActivity.this, "Added to Favorites!", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        else
        {
            String[] list=code.split("#");
            callback=new CallbackManager.Factory().create();
            ShareDialog sd=new ShareDialog(this);
            ShareLinkContent content=new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://cs-server.usc.edu:45678"))
                    .setContentTitle(list[0]).setImageUrl(Uri.parse(list[1]))
                    .setContentDescription("FB SEARCH FROM USC CSCI571")
                    .build();
            sd.registerCallback(callback,facebookCallback);
            Toast.makeText(this,"Sharing "+list[0]+"!!",Toast.LENGTH_SHORT).show();

            sd.show(content, ShareDialog.Mode.FEED);

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_settings);
        SharedPreferences localStorage=getSharedPreferences("data",MODE_MULTI_PROCESS);
        if(localStorage.contains(id))
        {
            item.setTitle("Remove from Favorites");
        }

        if(!localStorage.contains(id))
        {
            item.setTitle("Add to Favorites");
        }
        return super.onPrepareOptionsMenu(menu);
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
            Gson json=new Gson();
            DetailData result=json.fromJson(r,DetailData.class);
            test(result);

        }
    }
}
