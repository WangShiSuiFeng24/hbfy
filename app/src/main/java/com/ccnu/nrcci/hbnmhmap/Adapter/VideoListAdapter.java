package com.ccnu.nrcci.hbnmhmap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccnu.nrcci.hbnmhmap.JavaBean.VideoListBean;
import com.ccnu.nrcci.hbnmhmap.R;

import java.util.List;

/**
 * @project_name HBNMPepositories
 * @author:Jh
 * @time: 2019/8/1 16:15
 * @version:V1.0
 */
public class VideoListAdapter extends BaseAdapter {

    private List<VideoListBean> bList;
    private LayoutInflater mfla;
    private Context mcontext;

    public VideoListAdapter(Context context, List<VideoListBean> list){
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
            convertView = mfla.inflate(R.layout.video_display_layout,null);
            viewHolder.videoName = (TextView)convertView.findViewById(R.id.video_name);
            viewHolder.videoPlace = (TextView)convertView.findViewById(R.id.video_region);
            viewHolder.videoIntro = (TextView)convertView.findViewById(R.id.video_intro);
            viewHolder.videoProjectCover = (ImageView)convertView.findViewById(R.id.ProjectCover_image);
            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.videoName.setText(bList.get(position).getVideo_name());
        viewHolder.videoPlace.setText(bList.get(position).getVideo_place());
        viewHolder.videoIntro.setText(bList.get(position).getVideo_intro());
        if (bList.get(position).getVideo_projectcover().equals("http://202.114.41.165:8080null")){
            viewHolder.videoProjectCover.setImageResource(R.drawable.nopicture);
        }
        else {
            Glide.with(mcontext).load(bList.get(position).getVideo_projectcover()).into(viewHolder.videoProjectCover);
        }
        return convertView;
    }

    public class ViewHolder{
        public ImageView videoProjectCover;
        public TextView videoName;
        public TextView videoPlace;
        public TextView videoIntro;
        public TextView videoUrl;
    }
}
