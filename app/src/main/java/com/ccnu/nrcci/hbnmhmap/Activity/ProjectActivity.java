package com.ccnu.nrcci.hbnmhmap.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.ccnu.nrcci.hbnmhmap.HttpService.HttpHandle;
import com.ccnu.nrcci.hbnmhmap.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProjectActivity extends Activity {

    private TextView txt_itemname;
    private TextView txt_itemtype;
    private TextView txt_itemlevel;
    private TextView txt_itemregion;
    private TextView txt_itembatch;
    private TextView txt_iteminheritors;
    private TextView txt_itemintroduce;
    private TextView txt_itemdevelop;
    private TextView txt_itemrelatedcharacters;
    private TextView txt_itemprojectfeatures;

//    private ViewFlipper VF_itemPics;
//    private ImageView itemPic1;
//    private ImageView itemPic2;
//    private ImageView itemPic3;

    private ImageView back;

    private TextView txtView_ImageClick;
    private TextView txt_itemVideoClick;

    String projectcode;

    static String url = "http://202.114.41.165:8080";
    StringBuilder resource_video;
    List<String> videoname = new ArrayList<>();
    List<String> videourl = new ArrayList<>();
    //LinearLayout ll = (LinearLayout)findViewById(R.id.video);
/*
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);

            if (msg.what == 1){
                for (int i = 0;i<videoname.size();i++){
                    TextView tx = new TextView(getApplicationContext());
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(80,20,20,20);
                    tx.setLayoutParams(lp);
                    tx.getPaint().setFakeBoldText(true);
                    tx.setText(videoname.get(i));
                    tx.setTextColor(Color.parseColor("#3399FF"));
                    tx.setTextSize(20);
                    final int j = i;
                    tx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("url",videourl.get(j));
                            bundle.putString("videoname",videoname.get(j));
                            Intent intent= new Intent(ProjectActivity.this,ProjectVideo.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                    LinearLayout ll = (LinearLayout)findViewById(R.id.video);
                    ll.addView(tx);
                }
            }

        }
    };
*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project);

        txt_itemname = (TextView) findViewById(R.id.txtView_ItemName);
        txt_itemtype = (TextView) findViewById(R.id.txtView_itemtype);
        txt_itemlevel = (TextView) findViewById(R.id.txtView_itemlevel);
        txt_itemregion = (TextView) findViewById(R.id.txtView_itemregion);
        txt_itembatch = (TextView) findViewById(R.id.txtView_itembatch);
        txt_iteminheritors = (TextView) findViewById(R.id.txtView_itemInheritors);
        txt_itemintroduce = (TextView) findViewById(R.id.txtView_itemIntroduce);
        txt_itemdevelop = (TextView) findViewById(R.id.txtView_itemDevelop);
        txt_itemrelatedcharacters = (TextView) findViewById(R.id.txtView_itemRelatedcharacters);
        txt_itemprojectfeatures = (TextView) findViewById(R.id.txtView_itemProjectFeatures);

        /*VF_itemPics =(ViewFlipper) findViewById(R.id.ViewFlip_itempics);
        itemPic1 = (ImageView)findViewById(R.id.img_itemPic1);
        itemPic1 = (ImageView)findViewById(R.id.img_itemPic1);
        itemPic1 = (ImageView)findViewById(R.id.img_itemPic1);*/

        back =  findViewById(R.id.back);

        txtView_ImageClick = (TextView) findViewById(R.id.txtView_ImageClick);
        txt_itemVideoClick = (TextView) findViewById(R.id.txtView_VideoClick);


        Intent intent = getIntent();
        final Map<String,String> map = (Map<String,String>)intent.getSerializableExtra("alertMsg");

        projectcode = map.get("PROJECTCODE").toString();
        //Log.i("TAG",map.get("PROJECTCODE").toString());
        txt_itemname.setText(map.get("NAME").toString());
        //txt_itemtype.setText(map.get("TYPE").toString());
        txt_itemtype.setText((map.get("TYPE").equals(""))?"不详":map.get("TYPE"));
        //txt_itemlevel.setText(map.get("RANK").toString());
        txt_itemlevel.setText((map.get("RANK").equals(""))?"不详":map.get("RANK"));
        //txt_itemregion.setText(map.get("REGION").toString());
        txt_itemregion.setText((map.get("REGION").equals(""))?"不详":map.get("REGION"));
        //txt_itembatch.setText(map.get("BATCH").toString());
        txt_itembatch.setText((map.get("BATCH").equals(""))?"不详":map.get("BATCH"));
        txt_iteminheritors.setText((map.get("INHERITOR").equals(""))?"不详":map.get("INHERITOR"),TextView.BufferType.SPANNABLE);
        //跳转到传承人详情页面
        if (!(map.get("INHERITOR").equals(""))){
            Intent i = new Intent(ProjectActivity.this,DetailActivity.class);
        }
        //txt_itemintroduce.setText(map.get("ITEMINTRODUCE").toString());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //txt_itemintroduce.setText(Html.fromHtml(map.get("ITEMINTRODUCE"),Html.FROM_HTML_MODE_COMPACT));
            Spanned result1 = Html.fromHtml(map.get("ITEMINTRODUCE").toString(),Html.FROM_HTML_MODE_COMPACT);
            txt_itemintroduce.setText(result1);
            Spanned result2 = Html.fromHtml(map.get("DEVELOP").toString(),Html.FROM_HTML_MODE_COMPACT);
            txt_itemdevelop.setText(result2);
            if (map.get("RELATEDCHARACTERS").equals("")){
                LinearLayout layout = (LinearLayout)findViewById(R.id.itemRelatedcharacters);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result3 = Html.fromHtml(map.get("RELATEDCHARACTERS").toString(),Html.FROM_HTML_MODE_COMPACT);
                txt_itemrelatedcharacters.setText(result3);
            }

            Spanned result4 = Html.fromHtml(map.get("PROJECTFEATURES").toString(),Html.FROM_HTML_MODE_COMPACT);
            txt_itemprojectfeatures.setText(result4);

        }
        else {
            //txt_itemintroduce.setText(Html.fromHtml(map.get("ITEMINTRODUCE")));
            if (map.get("ITEMINTRODUCE").equals("")){
                LinearLayout layout = (LinearLayout)findViewById(R.id.itemIntroduce);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result1 = Html.fromHtml(map.get("ITEMINTRODUCE").toString());
                txt_itemintroduce.setText(result1);
            }
            if (map.get("DEVELOP").equals("")){
                LinearLayout layout = (LinearLayout)findViewById(R.id.itemDevelop);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result2 = Html.fromHtml(map.get("DEVELOP").toString());
                txt_itemdevelop.setText(result2);
            }

            if (map.get("RELATEDCHARACTERS").equals("")){
                LinearLayout layout = (LinearLayout)findViewById(R.id.itemRelatedcharacters);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result3 = Html.fromHtml(map.get("RELATEDCHARACTERS").toString());
                txt_itemrelatedcharacters.setText(result3);
            }
            if (map.get("PROJECTFEATURES").equals("")){
                LinearLayout layout = (LinearLayout)findViewById(R.id.itemProjectFeatures);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result4 = Html.fromHtml(map.get("PROJECTFEATURES").toString());
                txt_itemprojectfeatures.setText(result4);
            }

        }


        /*txt_itemdevelop.setText(map.get("DEVELOP").toString());
        txt_itemrelatedcharacters.setText(map.get("RELATEDCHARACTERS").toString());
        txt_itemprojectfeatures.setText(map.get("PROJECTFEATURES".toString()));*/


        txtView_ImageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageActivity.actionStart(ProjectActivity.this, projectcode, map.get("NAME"));


//                Bundle bundle = new Bundle();
//                bundle.putString("projectcode",projectcode);
//                Log.i("TA",projectcode);
//                Intent intent= new Intent(ProjectActivity.this,ImageActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });

        //点击back,返回到上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getEachParagraph(txt_iteminheritors);
        txt_iteminheritors.setMovementMethod(LinkMovementMethod.getInstance());

        /*
        txt_iteminheritors.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(ProjectActivity.this, DetailActivity.class);
                List<Map<String, String>> alertMsgs = new ArrayList<Map<String, String>>();
                int number = 10; // 每次获取多少条数据
                int totalPage = 0; // 总共有多少页
                int totalItem = 0; // 共有多少项
                int maxPage = 20;//最多浏览页数
                String name = txt_iteminheritors.getText().toString().trim();
                if (!name.equals("不详")) {
                    try {
                        Map<String, Object> cautionsInfo = new HttpHandle(getApplicationContext()).getInheritorByFilterWithPage(name, "", 1, number);
                        System.out.println("cautionsInfo:" + cautionsInfo);
                        if (cautionsInfo.size() != 0) {
                            Map<String, Integer> pageInfo = (Map<String, Integer>) cautionsInfo.get("pageInfo");
                            totalPage = pageInfo.get("pageCount");
                            totalItem = pageInfo.get("totalCount");

                            //避免加载过多内存溢出
                            if (totalPage >= maxPage) {
                                totalPage = maxPage;
                                System.out.println("totalpage:" + totalPage);
                            }

                            alertMsgs.addAll((List<Map<String, String>>) cautionsInfo.get("cautions"));
                            if (alertMsgs.size() != 0) {
                                //myHandler.sendEmptyMessage(101);

                            } else {

                            Message msg = new Message();
                            msg.what = 102;
                            Bundle bundle = new Bundle();
                            bundle.putString("toastText","未查询到数据");
                            msg.setData(bundle);
                            myHandler.sendMessage(msg);


                            }
                        } else {

                        Message msg = new Message();
                        msg.what = 102;
                        Bundle bundle = new Bundle();
                        bundle.putString("toastText","服务器出错");
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    intent1.putExtra("alertMsg", (Serializable) alertMsgs.get(0));
                    startActivity(intent1);
                    finish();
                }
            }
            });*/


        //final LinearLayout ll = (LinearLayout)findViewById(R.id.video);
//        requestUsingHttpURLConnectionGetVideoByProjectCode();

        /*txt_itemVideoClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("projectcode",projectcode);
                Intent intent= new Intent(ProjectActivity.this,ProjectVideo.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });*/


    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

    public void getEachParagraph(TextView textView) {
        Spannable spans = (Spannable) textView.getText();
        Integer[] indices = getIndices(textView.getText().toString().trim(),',');
        int start = 0;
        int end = 0;

        for (int i=0; i<=indices.length; i++) {
            ClickableSpan clickableSpan = getClickableSpan();
            //setSpan
            end = (i<indices.length ? indices[i] : spans.length());
            spans.setSpan(clickableSpan,start,end,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 1;
        }
}

    //click
    private ClickableSpan getClickableSpan() {
        return new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                TextView textView = (TextView)view;
                String s = textView.getText().subSequence(textView.getSelectionStart(),textView.getSelectionEnd()).toString();
                Intent intent1 = new Intent(ProjectActivity.this, DetailActivity.class);
                List<Map<String, String>> alertMsgs = new ArrayList<Map<String, String>>();
                int number = 10; // 每次获取多少条数据
                int totalPage = 0; // 总共有多少页
                int totalItem = 0; // 共有多少项
                int maxPage = 20;//最多浏览页数
                String name = s;
                if (!name.equals("不详")) {
                    try {
                        Map<String, Object> cautionsInfo = new HttpHandle(getApplicationContext()).getInheritorByFilterWithPage(name, "", 1, number);
                        System.out.println("cautionsInfo:" + cautionsInfo);
                        if (cautionsInfo.size() != 0) {
                            Map<String, Integer> pageInfo = (Map<String, Integer>) cautionsInfo.get("pageInfo");
                            totalPage = pageInfo.get("pageCount");
                            totalItem = pageInfo.get("totalCount");

                            //避免加载过多内存溢出
                            if (totalPage >= maxPage) {
                                totalPage = maxPage;
                                System.out.println("totalpage:" + totalPage);
                            }

                            alertMsgs.addAll((List<Map<String, String>>) cautionsInfo.get("cautions"));
                            if (alertMsgs.size() != 0) {
                                //myHandler.sendEmptyMessage(101);

                            } else {
                            /*
                            Message msg = new Message();
                            msg.what = 102;
                            Bundle bundle = new Bundle();
                            bundle.putString("toastText","未查询到数据");
                            msg.setData(bundle);
                            myHandler.sendMessage(msg);
                            */

                            }
                        } else {
                        /*
                        Message msg = new Message();
                        msg.what = 102;
                        Bundle bundle = new Bundle();
                        bundle.putString("toastText","服务器出错");
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);
                        */
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    intent1.putExtra("alertMsg", (Serializable) alertMsgs.get(0));
                    startActivity(intent1);
                }
                Log.e("onclick--", s);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                /**set textColor**/
                //ds.setColor(ds.linkColor);
                ds.setColor(Color.parseColor("#3399FF"));
                /**Remove the underline**/
                ds.setUnderlineText(false);
            }
        };
    }

    public static Integer[] getIndices(String s,char c) {
        int pos = s.indexOf(c,0);
        List<Integer> indices = new ArrayList<Integer>();
        while (pos != -1) {
            indices.add(pos);
            pos = s.indexOf(c,pos+1);
        }
        return (Integer[]) indices.toArray(new Integer[0]);
    }
    /*
    public void requestUsingHttpURLConnectionGetVideoByProjectCode(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://202.114.41.165:8080/FYProject/servlet/GetVideosByProjectCode"); // 声明一个URL,注意——如果用百度首页实验，请使用https
                    connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
                    connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
                    connection.setConnectTimeout(8000); // 设置连接建立的超时时间
                    connection.setReadTimeout(8000); // 设置网络报文收发超时时间
                    InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    resource_video = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        resource_video.append(line);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    JSONArray a = new JSONArray(resource_video.toString());
                    for (int i = 0;i < a.length();i++) {
                        JSONObject b = a.getJSONObject(i);

                        if (b.getString("LinkCode").equals(projectcode)) {
                            videourl.add(url + b.getString("ResourceUrl"));
                            videoname.add(b.getString("Name"));
                        }
                    }

                    Message msg = new Message();
                    msg.what = 1;
                    //Log.i("AA","1111111");
                    handler.sendMessage(msg);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }*/
}
