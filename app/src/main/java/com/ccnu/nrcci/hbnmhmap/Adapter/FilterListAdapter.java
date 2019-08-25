package com.ccnu.nrcci.hbnmhmap.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ccnu.nrcci.hbnmhmap.R;
import com.ccnu.nrcci.hbnmhmap.Util.FilterItem;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27,0027.
 */

public class FilterListAdapter extends BaseAdapter {
    private Context context;
    private List<FilterItem> data;

    public FilterListAdapter(Context context, List<FilterItem> data) {
        this.context = context;
        this.data = data;
    }

    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final MyView myView;
        if (v == null) {
            myView = new MyView();
            v = View.inflate(context, R.layout.item_filter_select_attr_list, null);
            myView.name = (TextView) v.findViewById(R.id.attr_list_name);
            myView.img = (ImageView) v.findViewById(R.id.attr_list_img);
            myView.grid = (GridView) v.findViewById(R.id.attr_list_grid);
            myView.grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
            myView.result=(TextView) v.findViewById(R.id.attr_list_result);
            v.setTag(myView);
        } else {
            myView = (MyView) v.getTag();
        }
        myView.name.setText(data.get(position).getName());
        final GoodsAttrsAdapter adapter = new GoodsAttrsAdapter(context);
        myView.grid.setAdapter(adapter);
        //myView.result.setText("");

        adapter.notifyDataSetChanged(data.get(position).isNameIsChecked(), data.get(position).getFilterContent());
        myView.img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (data.get(position).isNameIsChecked()) {
                    ((ImageView) v).setImageResource(R.drawable.sort_common_up);
                } else {
                    ((ImageView) v).setImageResource(R.drawable.sort_common_down);
                }
                adapter.notifyDataSetChanged(data.get(position).isNameIsChecked(), data.get(position).getFilterContent());
                data.get(position).setNameIsChecked(!data.get(position).isNameIsChecked());
            }
        });
        myView.grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                //设置当前选中的位置的状态为非。
                data.get(position).getFilterContent().get(arg2).setChecked(!data.get(position).getFilterContent().get(arg2).getIsChecked());

                for (int i = 0; i < data.get(position).getFilterContent().size(); i++) {
                    //跳过已设置的选中的位置的状态
                    if (i == arg2) {
                        myView.result.setText(data.get(position).getFilterContent().get(i).getIsChecked()
                                ?data.get(position).getFilterContent().get(i).getName():"");
                        continue;
                    }
                    data.get(position).getFilterContent().get(i).setChecked(false);
                }
                /*for (int i = 0; i < data.get(position).getFilterContent().size(); i++) {
                    if(data.get(position).getFilterContent().get(i).getIsChecked()){
                        myView.result.setText(data.get(position).getFilterContent().get(i).getName());
                        break;}
                    if()


                }*/
                if (data.get(position).isNameIsChecked()) {
                    myView.img.setImageResource(R.drawable.sort_common_up);
                } else {
                    myView.img.setImageResource(R.drawable.sort_common_down);
                }
                adapter.notifyDataSetChanged(data.get(position).isNameIsChecked(), data.get(position).getFilterContent());
            }
        });
        return v;
    }

    static class MyView {
        public TextView name;
        public ImageView img;
        public GridView grid;
        public TextView result;
    }

}
