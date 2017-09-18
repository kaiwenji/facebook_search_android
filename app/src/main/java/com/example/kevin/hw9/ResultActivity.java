package com.example.kevin.hw9;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

/**
 * Created by kevin on 2017/4/13.
 */
public class ResultActivity extends AppCompatActivity{
    private TabLayout layout;
    private List<HashMap<String,String>> mapList;
    private List<Cat> category;
    private List<user> list;
    private String result;
    private String addr;
    private Intent RTD;
    private String[] title=new String[]{"Users","Pages","Events","Places","Groups"};
    private int[] icon=new int[]{R.drawable.users,R.drawable.pages,R.drawable.events,R.drawable.places,R.drawable.groups};
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar mToolBar=(Toolbar)findViewById(R.id.resultToolBar);
        setSupportActionBar(mToolBar);
        //mToolBar.setNavigationIcon();
        //ActionBar.LayoutParams al=new ActionBar.LayoutParams(20,20);
        //mToolBar.setLayoutParams(al);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        category = new ArrayList<>();
        Intent tmp = getIntent();
        result = tmp.getStringExtra("result");
        Bean example = new Gson().fromJson(result, Bean.class);
        ResultData datalist = new ResultData(example);
        layout = (TabLayout) findViewById(R.id.tabLayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this, example,false));
        layout.setupWithViewPager(viewPager);
        for (int i = 0; i < 5; i++)
        {
            layout.getTabAt(i).setIcon(icon[i]).setText(title[i]);
        }




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
            Cat example=new Gson().fromJson(r,Cat.class);


        }
    }
}
