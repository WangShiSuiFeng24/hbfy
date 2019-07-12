package com.ccnu.nrcci.hbnmhmap.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.nrcci.hbnmhmap.HttpService.HttpHandle;
import com.ccnu.nrcci.hbnmhmap.HttpService.InterfaceConfig;
import com.ccnu.nrcci.hbnmhmap.R;
import com.ccnu.nrcci.hbnmhmap.Util.ImageLoader;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailActivity extends Activity  {

    private  ImageView img_inheritor;
    private TextView txt_name;
    private TextView txt_sex;
    private TextView txt_nation;
    private TextView txt_birthday;
    private TextView txt_occupation;
    private TextView txt_itemname;
    private TextView txt_Level;
    private TextView txt_address;
    private TextView txt_connection;
    private TextView txt_information;
    private TextView txt_experience;
    private TextView txt_influence;
    private ImageLoader imageLoader=new ImageLoader();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.detail_activity);

        img_inheritor = (ImageView) findViewById(R.id.imageView_inheritor);
        txt_name = (TextView) findViewById(R.id.txtView_InheritorName);
        txt_sex = (TextView) findViewById(R.id.text1);
        txt_nation = (TextView) findViewById(R.id.text2);
        txt_birthday = (TextView) findViewById(R.id.text3);
        txt_occupation = (TextView) findViewById(R.id.text4);
        txt_itemname = (TextView) findViewById(R.id.text5);
        txt_Level = (TextView) findViewById(R.id.text6);
        txt_address = (TextView) findViewById(R.id.text7);
        txt_connection = (TextView) findViewById(R.id.text8);
        txt_information = (TextView) findViewById(R.id.txtView_InheritorInfo);
        txt_experience = (TextView) findViewById(R.id.txtView_InheritorExper);
        txt_influence = (TextView) findViewById(R.id.txtView_InheritorInflu);

        Intent intent = getIntent();
        Map<String,String> map = (Map<String,String>)intent.getSerializableExtra("alertMsg");
        String path = map.get("IMAGEURL").toString();
        if(!path.equals("")){
            img_inheritor.setTag(InterfaceConfig.IP+path);
            imageLoader.showImageByAsyncTask(img_inheritor, InterfaceConfig.IP+path);
        }else{

        }

        txt_name.setText(map.get("NAME").toString());
        //txt_sex.setText(map.get("SEX").toString());
        txt_sex.setText((map.get("SEX").equals(""))?"不详":map.get("SEX"));
        txt_nation.setText((map.get("NATION").equals(""))?"不详":map.get("NATION"));
        //txt_nation.setText(map.get("NATION").toString());
        txt_birthday.setText((map.get("BIRTHDAY").equals(""))?"不详":map.get("BIRTHDAY"));
        //txt_birthday.setText(map.get("BIRTHDAY").toString());
        txt_occupation.setText((map.get("OCCUPATION").equals(""))?"不详":map.get("OCCUPATION"));
        //txt_occupation.setText(map.get("OCCUPATION").toString());
        txt_itemname.setText((map.get("PROJECT").equals(""))?"不详":map.get("PROJECT"));
        //txt_itemname.setText(map.get("PROJECT").toString());
        txt_Level.setText((map.get("RANK").equals(""))?"不详":map.get("RANK"));
        //txt_Level.setText(map.get("RANK").toString());
        txt_address.setText((map.get("ADDRESS").equals(""))?"不详":map.get("ADDRESS"));
        //txt_address.setText(map.get("ADDRESS").toString());
        txt_connection.setText((map.get("CONNECTION").equals(""))?"不详":map.get("CONNECTION"));
        //txt_connection.setText(map.get("CONNECTION").toString());
        if (map.get("RESUME").equals("")){
            LinearLayout layout = (LinearLayout)findViewById(R.id.InheritorInfo);
            layout.setVisibility(View.GONE);
        }
        else {
            txt_information.setText(map.get("RESUME").toString());
        }
        if (map.get("EXPERIENCE").equals("")){
            LinearLayout layout = (LinearLayout)findViewById(R.id.InheritorExper);
            layout.setVisibility(View.GONE);
        }
        else {
            txt_experience.setText(map.get("EXPERIENCE").toString());
        }
        if (map.get("INFLUENCE").equals("")){
            LinearLayout layout = (LinearLayout)findViewById(R.id.InheritorInflu);
            layout.setVisibility(View.GONE);
        }
        else {
            txt_influence.setText(map.get("INFLUENCE").toString());
        }
        txt_itemname.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this,ProjectActivity.class);
                int number = 10; // 每次获取多少条数据
                int totalPage = 0; // 总共有多少页
                int totalItem = 0; // 共有多少项
                int maxPage =20;//最多浏览页数
                boolean flag = false;
                List<Map<String,String >> alertMsgs = new ArrayList<Map<String,String>>();
                String name = txt_itemname.getText().toString().trim();
                try{
                    Map<String,Object> cautionsInfo = new HttpHandle(getApplicationContext()).getCautionByFilterWithPage(name,"","","",1,number);
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
                            /*
                            myHandler.sendEmptyMessage(101);
                            */
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
                    }else {
                        /*
                        Message msg = new Message();
                        msg.what = 102;
                        Bundle bundle = new Bundle();
                        bundle.putString("toastText","服务器出错");
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);
                        */
                    }
                }
                catch (Exception e){e.printStackTrace();}
                for(int i=0;i<alertMsgs.size();i++){
                    if(alertMsgs.get(i).get("INHERITOR").toString().trim().equals(txt_name.getText().toString().trim())){
                        intent.putExtra("alertMsg", (Serializable) alertMsgs.get(i));
                        startActivity(intent);
                        flag = true;
                        break;
                    }
                }
                if(flag==false) {
                    Toast toast = Toast.makeText(DetailActivity.this, "暂无相关信息", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
