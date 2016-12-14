package com.edu.feicui.newsclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.entity.Comment;
import com.edu.feicui.newsclient.utils.LoadImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mac on 2016/12/6.
 */

public class CommentAdapter extends BaseAdapter{
    private List<Comment> list = new ArrayList<Comment>();
    private Context context;
    private LayoutInflater inflater;
    private LoadImage loadImage;

    public CommentAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        loadImage = new LoadImage(context);
    }

    public List<Comment> getData(){
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

    public void appendDataToAdapter(List<Comment> data, boolean isClear){
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
        ViewHolder viewHolder = null;
        if (view == null){
            view = inflater.inflate(R.layout.comment_list_item, null);
            viewHolder = new ViewHolder();
            ButterKnife.bind(viewHolder, view);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Comment comment = list.get(i);
        viewHolder.tvName.setText(comment.getUid());
        viewHolder.tvData.setText(comment.getStamp());
        viewHolder.tvContent.setText(comment.getContent());
        loadImage.displayBitmap(comment.getPortrait(),viewHolder.ivIcon);
        return view;
    }

    class ViewHolder{
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_data)
        TextView tvData;
        @BindView(R.id.tv_content)
        TextView tvContent;
    }

}
