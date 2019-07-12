package com.ccnu.nrcci.hbnmhmap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccnu.nrcci.hbnmhmap.HttpService.InterfaceConfig;
import com.ccnu.nrcci.hbnmhmap.R;
import com.ccnu.nrcci.hbnmhmap.Util.ImageLoader;

import java.util.List;
import java.util.Map;

public class InheritorListViewAdapter extends BaseAdapter {
    List<Map<String,String>> alertList;
    private Context context;
    private ImageLoader imageLoader;
    public InheritorListViewAdapter(List<Map<String,String>> list, Context context)
    {
        this.context=context;
        this.alertList = list;
        this.imageLoader=new ImageLoader();
    }
    @Override
    public int getCount(){
        return alertList.size();
    }
    @Override
    public Object getItem(int position){
        return alertList.get(position);
    }
    @Override
    public long getItemId(int position){
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ViewHolder vh;
        if(convertView==null){
            vh=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.item_inheritor,parent,false);
            vh.detailImag=(ImageView) convertView.findViewById(R.id.inheritor_image) ;
            vh.projectName=(TextView) convertView.findViewById(R.id.tv_inheritor_project) ;
            vh.inheritorType=(TextView) convertView.findViewById(R.id.tv_inheritor_type) ;
            vh.inheritorRank=(TextView) convertView.findViewById(R.id.tv_inheritor_rank) ;
            vh.inheritorName=(TextView) convertView.findViewById(R.id.tv_inheritor_name) ;
            convertView.setTag(vh);
        }else{
            vh=(ViewHolder) convertView.getTag();
        }

        vh.inheritorName.setText(alertList.get(position).get("NAME"));
        vh.projectName.setText(alertList.get(position).get("PROJECT"));
        vh.inheritorType.setText(alertList.get(position).get("NATION"));
        vh.inheritorRank.setText(alertList.get(position).get("RANK"));
        vh.detailImag.setTag(InterfaceConfig.IP+alertList.get(position).get("IMAGEURL"));
        imageLoader.showImageByAsyncTask(vh.detailImag,InterfaceConfig.IP+alertList.get(position).get("IMAGEURL"));



        return convertView;

    }
    private class ViewHolder{
        ImageView detailImag;
        TextView inheritorName;
        TextView projectName;
        TextView inheritorType;
        TextView inheritorRank;
        ImageView naviegateImag;

    }
}
