package com.edu.feicui.newsclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.entity.News;
import com.edu.feicui.newsclient.utils.LoadImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mac on 2016/11/29.
 */

public class NewsAdapter extends BaseAdapter{
    private List<News> list = new ArrayList<News>();
    private Context context;
    private LayoutInflater inflater;
    private LoadImage loadImage;

    public NewsAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        loadImage = new LoadImage(context);
    }

    public List<News> getData(){
        return list;
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

    public void appendDataToAdapter(List<News> data, boolean isClear){
        if (data == null || data.size() == 0){
            return;
        }
        if (isClear){
            list.clear();
        }
        list.addAll(data);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = inflater.inflate(R.layout.activity_main_item, null);
            holder = new ViewHolder();
            ButterKnife.bind(holder,view);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        News news = list.get(i);
        holder.tvTitle.setText(news.getTitle());
        holder.tvContent.setText(news.getSummary());
        holder.tvDate.setText(news.getStamp());
        loadImage.displayBitmap(news.getIcon(), holder.imageView);
        return view;
    }

    class ViewHolder{
        @BindView(R.id.iv_icon)
        ImageView imageView;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_date)
        TextView tvDate;
    }
}
