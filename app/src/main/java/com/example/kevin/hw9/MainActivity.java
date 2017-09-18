package com.example.kevin.hw9;

import android.Manifest;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    private Button search;
    private Button clear;
    private String resText;
    private Mytask task;
    private Intent MTR;
    private DrawerLayout dlayout;
    private View mainView;
    private String provider;
    private String[] title=new String[]{"user","page","event","place","group"};
    private int[] icon=new int[]{R.drawable.users,R.drawable.pages,R.drawable.events,R.drawable.places,R.drawable.groups};
    private LocationManager locationManager;
    private double latitude=34,longitude=-118;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MTR=new Intent(MainActivity.this,ResultActivity.class);
        setContentView(R.layout.activity_main);
        dlayout=(DrawerLayout)findViewById(R.id.drawer);
        ImageView menu=(ImageView)findViewById(R.id.menu);
        //menu.setImageResource(android.R.drawable.ic_menu_sort_by_size);
        mainView=findViewById(R.id.layout_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        search=(Button)findViewById(R.id.search);
        clear=(Button)findViewById(R.id.clear);
        ListView listview=(ListView)findViewById(R.id.sidebarListView);
        listview.setAdapter(new SidebarAdapter(getApplicationContext()));
        ListView aboutme=(ListView)findViewById(R.id.aboutListView);
        aboutme.setAdapter(new SidebarAdapter(getApplicationContext(),new String[]{"About Me"},new int[]{R.drawable.real}));
        aboutme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dlayout.closeDrawer(Gravity.START);
                startActivity(new Intent(MainActivity.this,AboutMeActivity.class));
            }
        });
        gps();
        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlayout.openDrawer(Gravity.START);
            }
        });
        findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText tmp=(EditText)findViewById(R.id.input);
                tmp.setText("");
            }
        });
        findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.this,"helloworld",Toast.LENGTH_SHORT);
                EditText tmp=(EditText)findViewById(R.id.input);
                String s=tmp.getText().toString();
                try {
                    s = URLEncoder.encode(s, "UTF-8");
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

                if(s.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please enter a keyword!",LENGTH_SHORT).show();
                }
                else {
                    task = new Mytask();
                    task.execute("http://cs-server.usc.edu:35614/index.php?input=" + s + "&callback=test&latitude="+latitude+"&longitude="+longitude);
                }
            }
        });
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dlayout.closeDrawer(Gravity.START);
                if(position==1)
                {
                    mainView.setVisibility(View.GONE);
                    generateFav();

                }
                else
                {
                    mainView.setVisibility(View.VISIBLE);
                    returnMain();
                }
            }
        });

    }
    private void returnMain()
    {
        TextView tmp=(TextView)findViewById(R.id.title);
        tmp.setText("Search on FB");
    }
    private void gps() throws SecurityException {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;

        } else {
           // Toast.makeText(this, "check if the gps is opened",Toast.LENGTH_LONG).show();
            return;
        }

        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();


        }
        locationManager.requestLocationUpdates(provider, 2000, 2,
                locationListener);
    }




    LocationListener locationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderEnabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProviderDisabled(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChanged(Location arg0) {
            // TODO Auto-generated method stub
            latitude=arg0.getLatitude();
            longitude=arg0.getLongitude();
        }
    };
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    public void generateFav()
    {
        TextView tmpText=(TextView)findViewById(R.id.title);
        tmpText.setText("Favorites");
        TabLayout layout=(TabLayout)findViewById(R.id.fav_tab);
        ViewPager viewpager=(ViewPager)findViewById(R.id.fav_viewpager);
        Bean example=new Bean();
        List<Cat> category=new ArrayList<>();
        for(int i=0;i<5;i++)
        {
            Cat tmp=new Cat();
            tmp.data1=new ArrayList<>();
            category.add(tmp);
        }
        SharedPreferences sp= getSharedPreferences("data",MODE_MULTI_PROCESS);
        Map<String,String> map=(Map<String,String>)sp.getAll();
        for(Map.Entry<String, String>  entry : map.entrySet()){
            String tmp=entry.getValue();
            String[] list=tmp.split("#");
            String name=list[0];
            String picture=list[1];
            String id=entry.getKey();
            int type=Integer.parseInt(list[2]);
            user p=new user(id,name,picture);
            category.get(type).data1.add(p);
        }
        example.name=category;
        viewpager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), this, example,true));
        layout.setupWithViewPager(viewpager);

        for (int i = 0; i < 5; i++)
        {
            layout.getTabAt(i).setIcon(icon[i]).setText(title[i]);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Mytask extends AsyncTask<String,Integer,String>
    {
        protected void onPreExecute() {
            super.onPreExecute();
            search.setEnabled(false);

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
            resText=new String(r);
            MTR.putExtra("result",resText);
            search.setEnabled(true);
            startActivity(MTR);
            //Toast.makeText(getApplicationContext(),r,Toast.LENGTH_SHORT).show();

        }
    }
}
