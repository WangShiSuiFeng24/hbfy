package com.ccnu.nrcci.hbnmhmap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ccnu.nrcci.hbnmhmap.JavaBean.Video;
import com.ccnu.nrcci.hbnmhmap.R;

import java.util.List;

/**
 * @project_name HBNMPepositories
 * @author:Jh
 * @time: 2019/8/1 16:15
 * @version:V1.0
 */
public class VideoListAdapter extends BaseAdapter {

    private List<Video> mList;
    private LayoutInflater layoutInflater;
    private Context context;

    public VideoListAdapter(Context context, List<Video> list){
        mList = list;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return mList.size();
    }

    @Override
    //指定的索引对应的数据项
    public Object getItem(int position) {
        return mList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.video_display_layout,null);
            viewHolder.videoName = convertView.findViewById(R.id.video_name);
            viewHolder.videoPlace = convertView.findViewById(R.id.video_region);
            viewHolder.videoIntro = convertView.findViewById(R.id.video_intro);
            viewHolder.videoProjectCover = convertView.findViewById(R.id.ProjectCover_image);
            convertView.setTag(viewHolder);
        }

        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Video video = mList.get(position);
        viewHolder.videoName.setText(video.getVideo_name());
        viewHolder.videoPlace.setText(video.getVideo_place());
        viewHolder.videoIntro.setText(video.getVideo_intro());
        if (video.getVideo_projectCover().equals("http://202.114.41.165:8080null")){
            viewHolder.videoProjectCover.setImageResource(R.drawable.nopicture);
        }
        else {
            Glide.with(context).load(video.getVideo_projectCover()).into(viewHolder.videoProjectCover);
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
