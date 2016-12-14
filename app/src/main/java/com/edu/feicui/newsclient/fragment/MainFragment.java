package com.edu.feicui.newsclient.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.XListView.XListView;
import com.edu.feicui.newsclient.activity.ShowNewsActivity;
import com.edu.feicui.newsclient.adapter.NewsAdapter;
import com.edu.feicui.newsclient.adapter.NewsTypeAdapter;
import com.edu.feicui.newsclient.base.BaseActivity;
import com.edu.feicui.newsclient.biz.NewsManager;
import com.edu.feicui.newsclient.db.NewsDBManager;
import com.edu.feicui.newsclient.entity.News;
import com.edu.feicui.newsclient.entity.SubType;
import com.edu.feicui.newsclient.parser.NewsParser;
import com.edu.feicui.newsclient.utils.CommonUtils;
import com.edu.feicui.newsclient.view.HorizontalListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mac on 2016/11/28.
 */

public class MainFragment extends Fragment{
    @BindView(R.id.hl_type)
    HorizontalListView hlType;
    @BindView(R.id.iv_type_more)
    ImageView ivTypeMore;
    @BindView(R.id.listview)
    XListView listView;

    private NewsDBManager newsDBManager;
    private NewsTypeAdapter newsTypeAdapter;

    private NewsAdapter newsAdapter;
    private int subid = 1;
    private int refreshMode = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        newsDBManager = new NewsDBManager(getActivity());
        newsTypeAdapter = new NewsTypeAdapter(getActivity());
        hlType.setAdapter(newsTypeAdapter);
        hlType.setOnItemClickListener(typeItemClickListener);

        loadNewsType();

        newsAdapter = new NewsAdapter(getActivity());
        //启用上拉加载
        listView.setPullLoadEnable(true);
        //启动下拉刷新
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(listViewListener);
        listView.setOnItemClickListener(newsItemClickListener);
        listView.setAdapter(newsAdapter);
        refreshMode = NewsManager.MODE_PULL_REFRESH;
        loadNextNews(true);
        ((BaseActivity)getActivity()).showDialog(null, false);
        return view;

    }

    private AdapterView.OnItemClickListener typeItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SubType subType = (SubType) adapterView.getItemAtPosition(i);
            subid = subType.getSubid();
            newsTypeAdapter.notifyDataSetChanged();
            newsTypeAdapter.setCurrentPosition(i);
            ((BaseActivity)getActivity()).showDialog(null, false);
            refreshMode = NewsManager.MODE_PULL_REFRESH;
            loadNextNews(true);
        }
    };

    private AdapterView.OnItemClickListener newsItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            News news = (News) adapterView.getItemAtPosition(i);
            Bundle bundle = new Bundle();
            bundle.putSerializable("news", news);
            BaseActivity activity = (BaseActivity) getActivity();
            activity.startActivity(ShowNewsActivity.class, bundle);
        }
    };

    private XListView.IXListViewListener listViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            refreshMode = NewsManager.MODE_PULL_REFRESH;
            loadNextNews(false);
        }

        @Override
        public void onLoadMore() {
            refreshMode = NewsManager.MODE_LOAD_MORE;
            loadPrevNews();
        }
    };
    //加载新数据
    private void loadNextNews(boolean isNewType){
        int nid = 1;
        if (!isNewType){
            if (newsAdapter.getData().size() > 0){
                nid = newsAdapter.getData().get(0).getNid();
            }
        }
        if (CommonUtils.isNetConnect(getActivity())){
            NewsManager.getNewsList(getActivity(), subid, refreshMode, nid, newsListener, errorListener);
        }else {

        }
    }

    private void loadPrevNews(){
        if (newsAdapter.getData().size() == 0){
            return;
        }

        int lastIndex = newsAdapter.getData().size() - 1;
        int nid = newsAdapter.getData().get(lastIndex).getNid();
        if (CommonUtils.isNetConnect(getActivity())){
            NewsManager.getNewsList(getActivity(), subid, refreshMode, nid, newsListener, errorListener);
        }else {
            //从缓存中获取新闻数据
        }
    }

    private void loadNewsType() {
        if (newsDBManager.getNewsSubType().size() == 0){
            if (CommonUtils.isNetConnect(getActivity())){
                NewsManager.getNewsType(getActivity(), newsTypeListener, errorListener);
            }
        }else {
            List<SubType> list = newsDBManager.getNewsSubType();
            newsTypeAdapter.appendDataToAdapter(list, true);
            newsTypeAdapter.notifyDataSetChanged();
        }
    }

    private Response.Listener<String> newsTypeListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String json) {
            List<SubType> list = NewsParser.parseNewsType(json);
            newsTypeAdapter.appendDataToAdapter(list, true);
            newsTypeAdapter.notifyDataSetChanged();

            newsDBManager.saveNewsType(list);
        }
    };

    private Response.Listener<String> newsListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String json) {
            List<News> list = NewsParser.parseNews(json);
            boolean isClear = refreshMode == NewsManager.MODE_PULL_REFRESH ? true : false;
            newsAdapter.appendDataToAdapter(list, isClear);
            newsAdapter.notifyDataSetChanged();
            listView.stopLoadMore();
            listView.stopRefresh();
            ((BaseActivity)getActivity()).cancelDialog();
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            ((BaseActivity)getActivity()).cancelDialog();
            //取消进度对话框
            listView.stopLoadMore();
            listView.stopRefresh();
            Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
        }
    };
}
