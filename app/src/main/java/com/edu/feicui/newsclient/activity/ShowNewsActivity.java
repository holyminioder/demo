package com.edu.feicui.newsclient.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.base.BaseActivity;
import com.edu.feicui.newsclient.biz.CommentManager;
import com.edu.feicui.newsclient.db.NewsDBManager;
import com.edu.feicui.newsclient.entity.BaseEntity;
import com.edu.feicui.newsclient.entity.News;
import com.edu.feicui.newsclient.utils.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mac on 2016/12/1.
 */

public class ShowNewsActivity extends BaseActivity{
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_comment_num)
    TextView tvCommentNum;

    private News news;
    private PopupWindow popupWindow;
    private NewsDBManager newsDBManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CommonUtils.isNetConnect(this)){
            setContentView(R.layout.loading_fail);
        }else {
            setContentView(R.layout.activity_show_news);
            ButterKnife.bind(this);
            newsDBManager = new NewsDBManager(this);
            news = (News) getIntent().getSerializableExtra("news");
            //设置启用Javascript
            webView.getSettings().setJavaScriptEnabled(true);
            //设置缓存模式
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.getSettings().setAllowContentAccess(true);
            webView.setWebChromeClient(webChromeClient);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    view.loadUrl(news.getLink());
                    return super.shouldOverrideUrlLoading(view, request);
                }
            });
            webView.loadUrl(news.getLink());
            CommentManager.getCommentNum(this, news.getNid(), listener, errorListener);

            initPopupWindow();
        }
    }

    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if (newProgress >= 100){
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    //初始化PopupWindow
    public void initPopupWindow(){
        View view = getLayoutInflater().inflate(R.layout.popup_window, null);
        TextView tvCollectNews = (TextView) view.findViewById(R.id.tv_collect_news);
        tvCollectNews.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (!newsDBManager.isCollectNews(news)){
                    newsDBManager.save(news);
                    CommonUtils.showShortToast(ShowNewsActivity.this, "收藏新闻成功");
                }else {
                    CommonUtils.showShortToast(ShowNewsActivity.this, "已收藏，可以从收藏选项中查看");
                }
            }
        });
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //只要不为空，则点击PopupWindow最外层布局以及点击返回键PopupWindow消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    //显示菜单
    @OnClick(R.id.iv_menu) public void showMenu(){
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }else if (popupWindow != null){
            popupWindow.showAsDropDown(ivMenu, 0, 20);
        }
    }

    @OnClick(R.id.iv_back) public void exit(){
        finish();
    }

    @OnClick(R.id.tv_comment_num) public void gotoComment(){
        Bundle bundle = new Bundle();
        bundle.putInt("nid", news.getNid());
        startActivity(CommentActivity.class, bundle);
    }

    private Response.Listener<String> listener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            Gson gson = new Gson();
            BaseEntity<Integer> baseEntity = gson.fromJson(s, new TypeToken<BaseEntity<Integer>>(){}.getType());
            int commentNum = baseEntity.getData();
            tvCommentNum.setText(commentNum + "跟帖");
        }
    };
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            CommonUtils.showShortToast(ShowNewsActivity.this, "加载评论失败");
        }
    };
}
