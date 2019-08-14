package com.ccnu.nrcci.hbnmhmap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccnu.nrcci.hbnmhmap.JavaBean.VedioListBean;
import com.ccnu.nrcci.hbnmhmap.R;

import java.util.List;

/**
 * @project_name HBNMPepositories
 * @author:Jh
 * @time: 2019/8/1 16:15
 * @version:V1.0
 */
public class VedioListAdapter extends BaseAdapter {

    private List<VedioListBean> bList;
    private LayoutInflater mfla;
    private Context mcontext;

    public VedioListAdapter(Context context,List<VedioListBean> list){
        bList = list;
        this.mcontext = context;
        mfla = LayoutInflater.from(context);
    }

    public int getCount() {
        return bList.size();
    }

    @Override
    //指定的索引对应的数据项
    public Object getItem(int position) {
        return bList.get(position);
    }

    @Override
    //指定的索引对应的数据项ID
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = mfla.inflate(R.layout.vedio_display_layout,null);
            viewHolder.vedioname = (TextView)convertView.findViewById(R.id.vedio_name);
            viewHolder.vedioplace = (TextView)convertView.findViewById(R.id.vedio_region);
            viewHolder.vediointro = (TextView)convertView.findViewById(R.id.vedio_intro);
            viewHolder.vedioprojectcover = (ImageView)convertView.findViewById(R.id.ProjectCover_image);
            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.vedioname.setText(bList.get(position).getVedio_name());
        viewHolder.vedioplace.setText(bList.get(position).getVedio_place());
        viewHolder.vediointro.setText(bList.get(position).getVedio_intro());
        if (bList.get(position).getVedio_projectcover().equals("http://202.114.41.165:8080null")){
            viewHolder.vedioprojectcover.setImageResource(R.drawable.nopicture);
        }
        else {
            Glide.with(mcontext).load(bList.get(position).getVedio_projectcover()).into(viewHolder.vedioprojectcover);
        }
        return convertView;
    }

    public class ViewHolder{
        public ImageView vedioprojectcover;
        public TextView vedioname;
        public TextView vedioplace;
        public TextView vediointro;
        public TextView vediourl;
    }
}
