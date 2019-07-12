package com.ccnu.nrcci.hbnmhmap.HttpService;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class HttpHandle {
    Context context;
    public HttpHandle(Context context){
        this.context=context;
    }
    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
//获取项目信息
    public Map<String,Object> getCautionByFilterWithPage(String name, String type, String rank, String batch, int i, int number) {
        Map<String,Object> cautionsInfo = new HashMap<>();
        Map<String,String> params = new HashMap<>();
        List<Map<String,String>> cautions = new ArrayList<>();

        params.put("ItemName",name);
        params.put("ItemType",type);
        params.put("ItemLevel",rank);
        params.put("Batch",batch);
       /* params.put("pageIndex",i);
        params.put("pageSize",number);*/

        try {

            String result=new HttpUtil().PostRequest(InterfaceConfig.IP_PROJECT+"/"+String.valueOf(i)+"/"+String.valueOf(number),params);
            ObjectMapper mapper=new ObjectMapper();
            Map<String,Object> resultmap=mapper.readValue(result,Map.class);
            int statusCode=Integer.parseInt(resultmap.get("statusCode").toString());
            if (statusCode == 200){
                Map<String,Object> data = (Map<String,Object>) resultmap.get("data");

                //获取总行数与页面总数
                Map<String, Integer> pageInfo = (Map<String, Integer>)data.get("pageInfo");
                cautionsInfo.put("pageInfo",pageInfo);

                List<Object> dataSource = (List<Object>)data.get("dataSource");
                Iterator iterator = dataSource.iterator();
                while (iterator.hasNext()){
                    Map<String,String> cautionItem = new HashMap<>();
                    Map<String,Object> iteminfo = (Map<String,Object>)iterator.next();

                    Object namec= iteminfo.get("ItemName");
                    Object rankc = iteminfo.get("ItemLevel");
                    Object typec = iteminfo.get("Itemtype");
                    Object batchc = iteminfo.get("Batch");
                    Object synopsis = iteminfo.get("ItemIntroduce");
                    Object inheritor = iteminfo.get("Inheritors");
                    Object region = iteminfo.get("Region");
                    Object imageURL = iteminfo.get("Pics");
                    //Object pagecount=pageInfo.get("pageCount");
                    Object itemintroduce = iteminfo.get("ItemIntroduce");
                    Object develop = iteminfo.get("Develop");
                    Object itemcode = iteminfo.get("ItemCode");
                    Object ProjectCode = iteminfo.get("ProjectCode");
                    Object RelatedCharacters = iteminfo.get("Relatedcharacters");
                    Object ProjectFeatures = iteminfo.get("ProjectFeatures");

                    cautionItem.put("NAME",(namec== null)? "":namec.toString());
                    cautionItem.put("RANK",(rankc == null)? "":rankc.toString());
                    cautionItem.put("TYPE",(typec== null)? "":typec.toString());
                    cautionItem.put("BATCH",(batchc == null)? "":batchc.toString());
                    cautionItem.put("SYNOPSIS", ((synopsis == null)? "":synopsis.toString()));
                    cautionItem.put("INHERITOR",((inheritor == null)? "":inheritor.toString()) );
                    cautionItem.put("REGION",(region == null) ? "":region.toString());
                    cautionItem.put("IMAGEURL",(imageURL == null) ? "/荆楚非遗项目图片/nullimage.png":imageURL.toString().trim());
                    //cautionItem.put("PAGECOUNT",(pagecount==null)?null:pagecount.toString());
                    cautionItem.put("ITEMINTRODUCE",(itemintroduce==null)?"":itemintroduce.toString());
                    cautionItem.put("DEVELOP",(develop==null)?"":develop.toString());
                    cautionItem.put("ITEMCODE",(itemcode==null)?"":itemcode.toString());
                    cautionItem.put("PROJECTCODE",(ProjectCode==null)?"":ProjectCode.toString());
                    cautionItem.put("RELATEDCHARACTERS",(RelatedCharacters == null)?"":RelatedCharacters.toString());
                    cautionItem.put("PROJECTFEATURES",(ProjectFeatures == null)?"":ProjectFeatures.toString());
                    cautions.add(cautionItem);
                }
                cautionsInfo.put("cautions",cautions);
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
//          System.out.println("map:"+list.get("statusCode"));
        /*String name="getCautionByFilterWithPage"+page;
        DiskCacheUtil.init(context,cautions,name);*/
        return cautionsInfo;
    }
//获取传承人信息
    public Map<String,Object> getInheritorByFilterWithPage(String name, String rank, int i, int number) {
        Map<String,Object> cautionsInfo = new HashMap<>();
        Map<String,String> params = new HashMap<>();
        List<Map<String,String>> cautions = new ArrayList<>();

        params.put("Name",name);
        //params.put("ItemType",type);
        params.put("Level",rank);
        //params.put("Batch",batch);
       /* params.put("pageIndex",i);
        params.put("pageSize",number);*/

        try {

            String result=new HttpUtil().PostRequest(InterfaceConfig.IP_INHERITOR+"/"+String.valueOf(i)+"/"+String.valueOf(number),params);
            ObjectMapper mapper=new ObjectMapper();
            Map<String,Object> resultmap=mapper.readValue(result,Map.class);
            int statusCode=Integer.parseInt(resultmap.get("statusCode").toString());
            if (statusCode == 200){
                Map<String,Object> data = (Map<String,Object>) resultmap.get("data");

                //获取总行数与页面总数
                Map<String, Integer> pageInfo = (Map<String, Integer>)data.get("pageInfo");
                cautionsInfo.put("pageInfo",pageInfo);

                List<Object> dataSource = (List<Object>)data.get("dataSource");
                Iterator iterator = dataSource.iterator();
                while (iterator.hasNext()){
                    Map<String,String> cautionItem = new HashMap<>();
                    Map<String,Object> iteminfo = (Map<String,Object>)iterator.next();

                    Object namec= iteminfo.get("Name");
                    Object rankc = iteminfo.get("Level");
                    Object addressc = iteminfo.get("Address");
                    Object nationc = iteminfo.get("Nation");
                    Object resume = iteminfo.get("Resume");
                    Object connection = iteminfo.get("Connection");
                    Object experience = iteminfo.get("Experience");
                    Object imageURL = iteminfo.get("Bootstrap");
                    Object itemcode=iteminfo.get("ItemCode");
                    Object project=iteminfo.get("ItemName");
                    Object sex=iteminfo.get("Sex");
                    Object birthday=iteminfo.get("birthday");
                    Object occupation=iteminfo.get("Occupation");
                    Object influence= iteminfo.get("Influence");


                    cautionItem.put("NAME",(namec== null)? "":namec.toString());
                    cautionItem.put("RANK",(rankc == null)? "":rankc.toString());
                    cautionItem.put("ADDRESS",(addressc== null)? "":addressc.toString());
                    cautionItem.put("NATION",(nationc == null)? "":nationc.toString());
                    cautionItem.put("RESUME", ((resume == null)? "":resume.toString()));
                    cautionItem.put("CONNECTION",((connection == null)? "":connection.toString()) );
                    cautionItem.put("EXPERIENCE",(experience == null) ? "":experience.toString());
                    cautionItem.put("IMAGEURL",(imageURL == null) ? "/荆楚非遗项目图片/nullimage.png":imageURL.toString().trim());
                    cautionItem.put("ITEMCODE",(itemcode==null)?"":itemcode.toString());
                    cautionItem.put("PROJECT",(project==null)?"":project.toString());
                    cautionItem.put("SEX",(sex==null)?"":sex.toString());
                    cautionItem.put("BIRTHDAY",(birthday==null)?"":birthday.toString());
                    cautionItem.put("OCCUPATION",(occupation==null)?"":occupation.toString());
                    cautionItem.put("INFLUENCE",(influence==null)?"":influence.toString());

                    cautions.add(cautionItem);
                }
                cautionsInfo.put("cautions",cautions);
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
//          System.out.println("map:"+list.get("statusCode"));
        /*String name="getCautionByFilterWithPage"+page;
        DiskCacheUtil.init(context,cautions,name);*/
        return cautionsInfo;
    }
}
