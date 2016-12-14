package com.edu.feicui.newsclient.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.entity.SubType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mac on 2016/11/28.
 */

public class NewsTypeAdapter extends BaseAdapter{
    private List<SubType> list = new ArrayList<SubType>();
    private Context context;
    private LayoutInflater inflater;
    private int position;

    public NewsTypeAdapter(Context context){
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCurrentPosition(int position){
        this.position = position;
    }

    public void appendDataToAdapter(List<SubType> data, boolean isClear){
        if (data == null || data.size() == 0){
            return;
        }

        if (isClear){
            list.clear();
        }
        list.addAll(data);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = inflater.inflate(R.layout.news_type_list_item, null);
            holder = new ViewHolder();
            ButterKnife.bind(holder, view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        SubType subType = list.get(i);
        holder.tvNewsType.setText(subType.getSubgroup());
        if (i == position){
            holder.tvNewsType.setTextColor(Color.RED);
        }else {
            holder.tvNewsType.setTextColor(Color.BLACK);
        }
        return view;
    }

    class ViewHolder{
        @BindView(R.id.news_type)
        TextView tvNewsType;
    }

}
