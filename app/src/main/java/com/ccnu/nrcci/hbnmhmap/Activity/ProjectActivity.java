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

    private TextView txt_itemName;
    private TextView txt_itemType;
    private TextView txt_itemLevel;
    private TextView txt_itemRegion;
    private TextView txt_itemBatch;
    private TextView txt_itemInheritors;
    private TextView txt_itemIntroduce;
    private TextView txt_itemDevelop;
    private TextView txt_itemRelatedCharacters;
    private TextView txt_itemProjectFeatures;

//    private ViewFlipper VF_itemPics;
//    private ImageView itemPic1;
//    private ImageView itemPic2;
//    private ImageView itemPic3;

    private ImageView back;

    private TextView txtView_ImageClick;
    private TextView txt_itemVideoClick;

    String projectCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_project);

        txt_itemName = findViewById(R.id.txtView_ItemName);
        txt_itemType = findViewById(R.id.txtView_itemType);
        txt_itemLevel = findViewById(R.id.txtView_itemLevel);
        txt_itemRegion = findViewById(R.id.txtView_itemRegion);
        txt_itemBatch = findViewById(R.id.txtView_itemBatch);
        txt_itemInheritors = findViewById(R.id.txtView_itemInheritors);
        txt_itemIntroduce = findViewById(R.id.txtView_itemIntroduce);
        txt_itemDevelop = findViewById(R.id.txtView_itemDevelop);
        txt_itemRelatedCharacters = findViewById(R.id.txtView_itemRelatedCharacters);
        txt_itemProjectFeatures = findViewById(R.id.txtView_itemProjectFeatures);

        /*VF_itemPics =(ViewFlipper) findViewById(R.id.ViewFlip_itempics);
        itemPic1 = (ImageView)findViewById(R.id.img_itemPic1);
        itemPic1 = (ImageView)findViewById(R.id.img_itemPic1);
        itemPic1 = (ImageView)findViewById(R.id.img_itemPic1);*/

        back =  findViewById(R.id.back);

        txtView_ImageClick =findViewById(R.id.txtView_ImageClick);
        txt_itemVideoClick = findViewById(R.id.txtView_VideoClick);


        Intent intent = getIntent();
        final Map<String,String> map = (Map<String,String>)intent.getSerializableExtra("alertMsg");

        projectCode = map.get("PROJECTCODE").toString();

        txt_itemName.setText(map.get("NAME").toString());

        txt_itemType.setText((map.get("TYPE").equals(""))?"不详":map.get("TYPE"));

        txt_itemLevel.setText((map.get("RANK").equals(""))?"不详":map.get("RANK"));

        txt_itemRegion.setText((map.get("REGION").equals(""))?"不详":map.get("REGION"));

        txt_itemBatch.setText((map.get("BATCH").equals(""))?"不详":map.get("BATCH"));

        txt_itemInheritors.setText((map.get("INHERITOR").equals(""))?"不详":map.get("INHERITOR"),TextView.BufferType.SPANNABLE);
        //跳转到传承人详情页面
        if (!(map.get("INHERITOR").equals(""))){
            Intent i = new Intent(ProjectActivity.this,DetailActivity.class);
        }
        //txt_itemintroduce.setText(map.get("ITEMINTRODUCE").toString());
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            Spanned result1 = Html.fromHtml(map.get("ITEMINTRODUCE").toString(),Html.FROM_HTML_MODE_COMPACT);
            txt_itemIntroduce.setText(result1);
            Spanned result2 = Html.fromHtml(map.get("DEVELOP").toString(),Html.FROM_HTML_MODE_COMPACT);
            txt_itemDevelop.setText(result2);
            if (map.get("RELATEDCHARACTERS").equals("")){
                LinearLayout layout = findViewById(R.id.itemRelatedCharacters);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result3 = Html.fromHtml(map.get("RELATEDCHARACTERS").toString(),Html.FROM_HTML_MODE_COMPACT);
                txt_itemRelatedCharacters.setText(result3);
            }

            Spanned result4 = Html.fromHtml(map.get("PROJECTFEATURES").toString(),Html.FROM_HTML_MODE_COMPACT);
            txt_itemProjectFeatures.setText(result4);

        }
        else {
            //txt_itemintroduce.setText(Html.fromHtml(map.get("ITEMINTRODUCE")));
            if (map.get("ITEMINTRODUCE").equals("")){
                LinearLayout layout = findViewById(R.id.itemIntroduce);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result1 = Html.fromHtml(map.get("ITEMINTRODUCE").toString());
                txt_itemIntroduce.setText(result1);
            }
            if (map.get("DEVELOP").equals("")){
                LinearLayout layout = findViewById(R.id.itemDevelop);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result2 = Html.fromHtml(map.get("DEVELOP").toString());
                txt_itemDevelop.setText(result2);
            }

            if (map.get("RELATEDCHARACTERS").equals("")){
                LinearLayout layout = findViewById(R.id.itemRelatedCharacters);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result3 = Html.fromHtml(map.get("RELATEDCHARACTERS").toString());
                txt_itemRelatedCharacters.setText(result3);
            }
            if (map.get("PROJECTFEATURES").equals("")){
                LinearLayout layout = (LinearLayout)findViewById(R.id.itemProjectFeatures);
                layout.setVisibility(View.GONE);
            }
            else {
                Spanned result4 = Html.fromHtml(map.get("PROJECTFEATURES").toString());
                txt_itemProjectFeatures.setText(result4);
            }

        }

        txtView_ImageClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageActivity.actionStart(ProjectActivity.this, projectCode, map.get("NAME"));
            }
        });

        //点击back,返回到上一个界面
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //跳转到传承人界面
        getEachParagraph(txt_itemInheritors);
        txt_itemInheritors.setMovementMethod(LinkMovementMethod.getInstance());

        //跳到视频页面
        txt_itemVideoClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoDisplay.actionStart(ProjectActivity.this, projectCode);
            }
        });
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
                /**set textColor*/
                //ds.setColor(ds.linkColor);
                ds.setColor(Color.parseColor("#3399FF"));
                /**Remove the underline*/
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
}
