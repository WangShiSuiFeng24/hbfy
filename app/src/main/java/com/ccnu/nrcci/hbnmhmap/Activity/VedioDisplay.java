package com.ccnu.nrcci.hbnmhmap.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.nrcci.hbnmhmap.JavaBean.VedioListBean;
import com.ccnu.nrcci.hbnmhmap.R;
import com.ccnu.nrcci.hbnmhmap.Adapter.VedioListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.zip.Inflater;

/**
 * @project_name HBNMPepositories
 * @author:Jh
 * @time: 2019/8/1 17:40
 * @version:V1.0
 */

//新写的Activity一定要记得在manifest中注册！
public class VedioDisplay extends Activity{

    Map<String,String> Vedio_Name = new HashMap<>();
    Map<String,String> Vedio_Url = new HashMap<>();
    Map<String,String> Vedio_Place = new HashMap<>();
    Map<String,String> Vedio_Introduce = new HashMap<>();
    Map<String,String> Vedio_ProjectCover = new HashMap<>();

    static String url = "http://202.114.41.165:8080";
    StringBuilder response_vedio;
    List<VedioListBean> itemVedioList;
    private Context context;
    private String projectcode;
    private VedioListAdapter vedioListAdapter;


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                vedioListAdapter=new VedioListAdapter(context,itemVedioList);
                listView.setAdapter(vedioListAdapter);
            }
        }
    };

    private View vedioBack;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vedio_list);


        context = this;
        listView = (ListView)findViewById(R.id.vediolist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VedioListBean mListBean = (VedioListBean)vedioListAdapter.getItem(position);
                if (mListBean.getVedio_url().equals("")){
                    Toast.makeText(getApplicationContext(),"暂无视频资源",Toast.LENGTH_SHORT).show();
                }
                else {

//                    String url = mListBean.getVedio_url();
//                    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
//                    String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
//                    Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
//                    mediaIntent.setDataAndType(Uri.parse(url),mimeType);
//                    startActivity(mediaIntent);


                    Intent i = new Intent();
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.setAction(Intent.ACTION_VIEW);
                    Uri uri= Uri.parse(mListBean.getVedio_url());
                    i.setDataAndType(uri,"video/*");
                    startActivity(i);
//                    Log.i("RRR","tyu");
//                    Bundle bundle = new Bundle();
//                    bundle.putString("url",mListBean.getVedio_url());
//                    bundle.putString("name",mListBean.getVedio_name());
//                    bundle.putString("projectcover",mListBean.getVedio_projectcover());
                    //Intent intent = new Intent(VedioDisplay.this,ProjectVideo.class);
                    //intent.putExtras(bundle);
                    //Toast.makeText(getApplicationContext(),"你大爷的！",Toast.LENGTH_SHORT).show();
                    //Log.i("RRR","tyu");
                    //startActivity(intent);

                }
            }
        });
        Bundle bundle = getIntent().getExtras();
        projectcode = bundle.getString("projectcode");
        requestUsingHttpURLConnectionGetVedio();
        vedioBack = (LinearLayout)findViewById(R.id.vedio_back);
        vedioBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
    }

    public void requestUsingHttpURLConnectionGetVedio(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                VedioListBean bean;
                HttpURLConnection connection = null;
                StringBuilder response = null;
                itemVedioList = new ArrayList<>();
                try {
                    URL url = new URL("http://202.114.41.165:8080/FYProject/servlet/GetVideosByProjectCode"); // 声明一个URL,注意——如果用百度首页实验，请使用https
                    connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                    connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                    connection.setConnectTimeout(8000); // 设置连接建立的超时时间
                    connection.setReadTimeout(8000); // 设置网络报文收发超时时间
                    InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    response_vedio = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        response_vedio.append(line);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray a = new JSONArray(response_vedio.toString());
                    for (int i = 0;i < a.length();i++) {
                        JSONObject b = a.getJSONObject(i);
                        if (b.getString("LinkCode").equals(projectcode)) {
                            bean = new VedioListBean();
                            bean.setVedio_name(b.getString("Name").equals("null")?"":b.getString("Name"));
                            bean.setVedio_intro(b.getString("Introduce").equals("null")?"暂无简介":b.getString("Introduce"));
                            bean.setVedio_projectcover(url + b.getString("PorjectCover"));
                            bean.setVedio_place(b.getString("Place").equals("null")?"地区不详":b.getString("Place"));
                            bean.setVedio_url(b.getString("ResourceUrl").equals("null")?"":"http://202.114.41.165:8080"+b.getString("ResourceUrl"));
                            itemVedioList.add(bean);
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
}
