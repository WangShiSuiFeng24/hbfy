package com.ccnu.nrcci.hbnmhmap.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ccnu.nrcci.hbnmhmap.Activity.DetailActivity;
import com.ccnu.nrcci.hbnmhmap.Adapter.FilterListAdapter;
import com.ccnu.nrcci.hbnmhmap.Adapter.InheritorListViewAdapter;
import com.ccnu.nrcci.hbnmhmap.Adapter.ProjectListViewAdapter;
import com.ccnu.nrcci.hbnmhmap.HttpService.HttpHandle;
import com.ccnu.nrcci.hbnmhmap.R;
import com.ccnu.nrcci.hbnmhmap.Util.FilterItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InheritorFragment extends android.app.Fragment {
    private ImageView searchCancle;
    private EditText Name;
    private LinearLayout search;
    private TextView filterReset;
    private TextView filterSure;
    private View footer,alert_progressBar;
    private TextView blank;
    private ListView listView;
    private ListView filterlist;
    private List<Map<String,String >> alertMsgs = new ArrayList<Map<String,String>>();
    private List<String> rankList= Arrays.asList("国家级","省级","县级","州级");
    /*private List<String> typeList=Arrays.asList("民间传说","传统舞蹈","传统戏剧","曲艺","传统体育","传统美术","传统音乐","民俗","传统技艺");
    private List<String> batchList=Arrays.asList("一","二","三","四","一扩","二扩","三扩");*/
    private List<FilterItem> filterSelection=new ArrayList<>();
    private DrawerLayout drawerLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout filter;
    private InheritorListViewAdapter inheritorListViewAdapter;
    private FilterListAdapter filterListAdapter;


    private final String TAG = "DialogDate";
    private int number = 10; // 每次获取多少条数据
    private int totalPage = 0; // 总共有多少页
    private int maxPage =20;//最多浏览页数
    private int totalItem = 0; // 共有多少项
    private int searchMode=0;
    private boolean[] loadfinish = {true}; // 指示数据是否加载完成
    private Toast mToast = null;

    String name = "";
    String rank = "";
    String type = "";
    String batch = "";
    String querysql;

    public InheritorFragment()
    {
    }
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState)
    {
        View view=inflater.inflate(R.layout.fragemnt_inheritor, container, false);
        footer=inflater.inflate(R.layout.footer,null);

        searchCancle = (ImageView) view.findViewById(R.id.cancle_fragment_inheritorname);
        search=(LinearLayout) view.findViewById(R.id.lav_inheritor_search);
        filterlist=(ListView)view.findViewById(R.id.lv_inheritor_filterlist);
        Name =(EditText) view.findViewById(R.id.edtTxt_fragment_inheritorname);
        drawerLayout=(DrawerLayout) view.findViewById(R.id.drawer_layout_inheritor);
        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.srl_inheritor_pulltorefresh);
        filter=(LinearLayout)view.findViewById(R.id.lav_inheritor_filter);
        filterListAdapter=new FilterListAdapter(getActivity(),filterSelection);
        filterReset=(TextView)view.findViewById(R.id.tv_fragment_inheritor_reset);
        filterSure=(TextView)view.findViewById(R.id.tv_fragment_inheritor_sure) ;

        //UserIOUtil.setEditTextInhibitSpace(carNo);

        alert_progressBar=(ProgressBar)view.findViewById(R.id.inheritor_progressBar);
        blank = (TextView)view.findViewById(R.id.inheritor_blank);

        //下拉刷新
        Name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchCancle.setImageResource(0);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(count>0){
                    searchCancle.setImageResource(R.drawable.searchcancle);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh(){
                searchMode=0;
                Map<String,Object> cautionsInfo = new HttpHandle(getActivity()).getInheritorByFilterWithPage("","",1,number);
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
                    alertMsgs.clear();

                    alertMsgs.addAll((List<Map<String, String>>) cautionsInfo.get("cautions"));
                    if (alertMsgs.size() != 0) {
                        myHandler.sendEmptyMessage(101);

                    } else {
                        Message msg = new Message();
                        msg.what = 102;
                        Bundle bundle = new Bundle();
                        bundle.putString("toastText","未查询到数据");
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);

                    }

                    name=type=rank=batch="";
                    swipeRefreshLayout.setRefreshing(false);
                }else{
                    swipeRefreshLayout.setRefreshing(false);
                    if (mToast == null) {
                        mToast = Toast.makeText(getActivity(), "请检查网络连接", Toast.LENGTH_SHORT);
                    }else mToast.setText("请检查网络连接");
                    mToast.show();
                }

            }
        });


        filterReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < filterSelection.size(); i++) {
                    for (int j = 0; j < filterSelection.get(i).getFilterContent().size(); j++) {
                        filterSelection.get(i).getFilterContent().get(j).setChecked(false);
                    }
                }
                try{
                    filterListAdapter.notifyDataSetChanged();
                    filterlist.setAdapter(filterListAdapter);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }
            }
        });

        filterSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    search.performClick();
                    drawerLayout.closeDrawer(GravityCompat.END);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!drawerLayout.isDrawerOpen(GravityCompat.END)){
                    drawerLayout.openDrawer(GravityCompat.END);
                }
            }
        });

        Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if(!Name.getText().toString().trim().equals("")){
                        searchCancle.setImageResource(R.drawable.searchcancle);
                        searchCancle.setClickable(true);
                    }

                }else {
                    searchCancle.setImageResource(0);
                    searchCancle.setClickable(false);
                }
            }
        });

        searchCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name.setText("");

                search.performClick();
                //TODO 返回全部查询结果
                /*searchMode=3;
                String name= "getCautionWithPage"+1;
                List<Map<String,String>> curData=DiskCacheUtil.getDataFromDiskLruCache(name,getActivity());

                //if(!curData.equals(null)&&curData.size()>0){
                if(!curData.isEmpty() && curData.size()>0){
                    totalPage=Integer.parseInt(curData.get(1).get("PAGECOUNT"));
                    alertMsgs.clear();
                    alertMsgs.addAll(curData);
                    myHandler.sendEmptyMessage(101);
                }else{
                    Message msg = new Message();
                    msg.what = 102;
                    Bundle bundle = new Bundle();
                    bundle.putString("toastText","本地无缓存");
                    msg.setData(bundle);
                    myHandler.sendMessage(msg);
                }
*/
            }
        });



        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceStace){
        super.onActivityCreated(savedInstanceStace);

        //加载中的初始页面
        listView=(ListView) getView().findViewById(R.id.lv_fragment_inheritor_alertList);
        listView.addFooterView(footer);


        try{
            filterSelection.add(new FilterItem("非遗级别",rankList));

           /* filterSelection.add(new FilterItem("批次",batchList));
            filterSelection.add(new FilterItem("类型",typeList));*/
            filterlist.setAdapter(filterListAdapter);
        }catch (NullPointerException e){
            e.printStackTrace();
        }


        Thread listinit = new Thread(){
            @Override
            public void run() {
                Looper.prepare();

                //有网络条件下
                try{
                    Map<String,Object> cautionsInfo = new HttpHandle(getActivity()).getInheritorByFilterWithPage("","",1,number);
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
                            myHandler.sendEmptyMessage(101);

                        } else {
                            Message msg = new Message();
                            msg.what = 102;
                            Bundle bundle = new Bundle();
                            bundle.putString("toastText","未查询到数据");
                            msg.setData(bundle);
                            myHandler.sendMessage(msg);

                        }
                    }else {
                        Message msg = new Message();
                        msg.what = 102;
                        Bundle bundle = new Bundle();
                        bundle.putString("toastText","服务器出错");
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);
                    }
                }
                catch (Exception e){e.printStackTrace();}


            }


        };
        listinit.start();


        //按钮点击执行按条件查询
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HttpHandle.isNetworkAvailable(getActivity())) {
                    querysql = null;
                    name = Name.getText().toString().trim();
                    if(drawerLayout.isDrawerOpen(GravityCompat.END))
                    {
                        rank=((TextView) filterlist.getChildAt(0).findViewById(R.id.attr_list_result)).getText().toString();
                        /*type=((TextView) filterlist.getChildAt(1).findViewById(R.id.attr_list_result)).getText().toString();
                        batch=((TextView) filterlist.getChildAt(2).findViewById(R.id.attr_list_result)).getText().toString();*/

                    }else{
                        rank="";
                    }
                    if(name.equals("")&&rank.equals("")){
                        searchMode=0;
                    }else if(!name.equals("")&&rank.equals("")){
                        searchMode=1;
                    }else {
                        searchMode=2;
                    }

                    Map<String, Object> queryResult = new HttpHandle(getActivity()).getInheritorByFilterWithPage(name,rank, 1, number);
                    //List<Map<String,String>> cautions = (List<Map<String,String>>)queryResult.get("cautions");
                    if (queryResult.size() != 0) {
                        alertMsgs.clear();
                        alertMsgs.addAll((List<Map<String, String>>) queryResult.get("cautions"));
                        //System.out.println("size:"+queryResult.size()+"   "+queryResult.toString());
                        if (alertMsgs.size() == 0) {
                            if (mToast == null) {
                                mToast = Toast.makeText(getActivity(), "未查找到相应结果", Toast.LENGTH_SHORT);
                            }else mToast.setText("未查找到相应结果");
                            mToast.show();
                            blank.setVisibility(View.VISIBLE);
//                            setListView();
                            listView.setVisibility(View.GONE);
                        } else {
                            Map<String, Integer> pageInfo = (Map<String, Integer>) queryResult.get("pageInfo");
                            totalPage = pageInfo.get("pageCount");
                            totalItem = pageInfo.get("totalCount");
                            //设置页面list页面内容，显示查询结果
                            //alertListViewAdapter.setData(cautions);
                            setListView();
                        }
                    } else {
                        if (mToast == null) {
                            mToast = Toast.makeText(getActivity(), "服务器出错", Toast.LENGTH_SHORT);
                        }else mToast.setText("服务器出错");
                        mToast.show();
                        blank.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                }else {
                    if (mToast == null) {
                        mToast = Toast.makeText(getActivity(), "当前无网络连接", Toast.LENGTH_SHORT);
                    }else mToast.setText("当前无网络连接");
                    mToast.show();
                    blank.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
                if(searchMode==1||searchMode==2){
                    searchCancle.setImageResource(R.drawable.searchcancle);
                    Name.requestFocus();
                }else{
                    searchCancle.setImageResource(0);
                }
            }
        });

        //跳转至详细页面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(), DetailActivity.class);

                intent.putExtra("alertMsg",(Serializable) alertMsgs.get(position));
                System.out.println(alertMsgs.get(position));
                getActivity().startActivity(intent);
            }
        });
    }

    public void setListView(){

        final class ScrollListener implements AbsListView.OnScrollListener {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                List<Map<String,String>> cautions=new ArrayList<>();
                final int loadtotal = totalItemCount;
                int lastItemid = listView.getLastVisiblePosition(); // 获取当前屏幕最后Item的ID
                if ((lastItemid + 1) == totalItemCount) { // 达到数据的最后一条记录
                    if (totalItemCount > 0) {
                        // 当前页
                        int currentpage = totalItemCount % number == 0 ? totalItemCount / number : totalItemCount / number + 1;
                        final int nextpage = currentpage + 1; // 下一页
                        if (nextpage <= totalPage && loadfinish[0]) {
                            loadfinish[0] = false;
                            listView.addFooterView(footer);
                            Map<String,Object> data = new HashMap<>();
                            if(HttpHandle.isNetworkAvailable(getActivity())){
                                switch (searchMode){
                                    case 0:
                                        data =new HttpHandle(getActivity()).getInheritorByFilterWithPage("","",nextpage,number);
                                        cautions = (List<Map<String,String>>) data.get("cautions");break;
                                    case 1:
                                        data=new HttpHandle(getActivity()).getInheritorByFilterWithPage(name,"",nextpage,number);
                                        cautions = (List<Map<String,String>>) data.get("cautions");break;
                                    case 2:
                                        data=new HttpHandle(getActivity()).getInheritorByFilterWithPage(name,rank,nextpage,number);
                                        cautions = (List<Map<String,String>>) data.get("cautions");break;
                                    /*case 3:
                                        String name="getCautionWithPage"+nextpage;
                                        cautions=DiskCacheUtil.getDataFromDiskLruCache(name,getActivity());*/
                                }
                            }else{
                               /* String name= "getCautionWithPage"+nextpage;
                                List<Map<String,String>> curData=DiskCacheUtil.getDataFromDiskLruCache(name,getActivity());
                                if(!curData.equals(null)){
                                    cautions = curData;
                                }*/
                                if (mToast == null) {
                                    mToast = Toast.makeText(getActivity(), "当前无网络连接", Toast.LENGTH_SHORT);
                                }else mToast.setText("当前无网络连接");
                                mToast.show();
                                cautions=null;

                            }

                            // 发送消息
                            if (inheritorListViewAdapter != null) {
                                myHandler.sendMessage(myHandler.obtainMessage(100, cautions));
                            }else myHandler.sendMessage(myHandler.obtainMessage(101,cautions));
                        }
                    }
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        }
        listView.setOnScrollListener(new ScrollListener());

        listView.removeFooterView(footer);
        inheritorListViewAdapter.notifyDataSetChanged();
        listView.setVisibility(View.VISIBLE);
        blank.setVisibility(View.GONE);

    }

    Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 100:
                    //页面下拉加载列表数据
                    listView.setVisibility(View.VISIBLE);
                    blank.setVisibility(View.GONE);
                    InheritorFragment.this.alertMsgs.addAll((List<Map<String, String>>) msg.obj);
                    // 告诉ListView数据已经发生改变，要求ListView更新界面显示
                    //alertListViewAdapter.setData(AlertFragment.this.alertMsgs);
                    inheritorListViewAdapter.notifyDataSetChanged();
                    if (listView.getFooterViewsCount() > 0) { // 如果有底部视图
                        listView.removeFooterView(footer);
                    }
                    loadfinish[0] = true; // 加载完成
                    break;
                case 101:
                    //页面初始化加载列表数据完成修改UI
                    try {
                        inheritorListViewAdapter= new InheritorListViewAdapter(alertMsgs, getActivity());
                        listView.setAdapter(inheritorListViewAdapter);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }

                    setListView();

                    alert_progressBar.setVisibility(View.GONE);
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    blank.setVisibility(View.GONE);
                    break;
                case 102:
                    alert_progressBar.setVisibility(View.GONE);
                    blank.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setVisibility(View.GONE);
                    //结果为空时toast提示
                    if (mToast == null) {
                        mToast = Toast.makeText(getActivity(), msg.getData().getString("toastText") , Toast.LENGTH_SHORT);
                    }else mToast.setText(msg.getData().getString("toastText") );
                    mToast.show();
                    break;
            }
        }
    };



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mToast!= null) mToast.cancel();
    }
}
