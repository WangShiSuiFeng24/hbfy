package com.ccnu.nrcci.hbnmhmap.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ccnu.nrcci.hbnmhmap.R;
import com.ccnu.nrcci.hbnmhmap.Util.GlideImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageActivity extends Activity {

    static String url = "http://202.114.41.165:8080";
    StringBuilder resource_pics;
    Map<String,String> Pic = new HashMap<>();
    String projectcode;
    List<String> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                //Log.i("QQ","aaaa");
               for (Map.Entry<String, String> entry:Pic.entrySet()){
                   Log.i("QQQ",entry.getKey());
                   Log.i("QQ",entry.getValue());
                   String a = entry.getKey();  //数据库的地址后边有空格，大坑！
                   a = a.trim();
                   images.add(url + a);
                   titles.add(entry.getValue());
               }
                Banner banner = (Banner) findViewById(R.id.banner);
                banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
                //设置图片加载器
                banner.setImageLoader(new GlideImageLoader());
                //设置图片集合
                banner.setImages(images);
                banner.setBannerTitles(titles);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project_image);

        Intent intent = getIntent();
        projectcode = intent.getStringExtra("projectcode");
        String item_name = intent.getStringExtra("item_name");

        TextView txtView_ItemName = findViewById(R.id.txtView_ItemName);
        txtView_ItemName.setText(item_name);

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

//        Bundle bundle = getIntent().getExtras();
//        projectcode = bundle.getString("projectcode");
        Log.i("TT",projectcode);
        //Log.i("111",projectcode);
        requestUsingHttpURLConnectionGetPicByProjectCode();
    }

    public void requestUsingHttpURLConnectionGetPicByProjectCode(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://202.114.41.165:8080/FYProject/GetPicsByProjectCode"); // 声明一个URL,注意——如果用百度首页实验，请使用https
                    connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                    connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                    connection.setConnectTimeout(8000); // 设置连接建立的超时时间
                    connection.setReadTimeout(8000); // 设置网络报文收发超时时间
                    InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    resource_pics = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        resource_pics.append(line);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray a = new JSONArray(resource_pics.toString());
                    for (int i = 0;i < a.length();i++) {
                        JSONObject b = a.getJSONObject(i);
                        Log.i("AA",b.getString("LinkCode"));
                            if (b.getString("LinkCode").equals(projectcode)) {
                                Pic.put(b.getString("ResourceUrl"), b.getString("Introduce"));
                            }
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void actionStart(Context context, String projectcode, String item_name) {
        Intent intent = new Intent(context, ImageActivity.class);
        intent.putExtra("projectcode", projectcode);
        intent.putExtra("item_name", item_name);
        context.startActivity(intent);
    }

}
