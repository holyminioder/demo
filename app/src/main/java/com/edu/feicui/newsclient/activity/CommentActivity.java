package com.edu.feicui.newsclient.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.XListView.XListView;
import com.edu.feicui.newsclient.adapter.CommentAdapter;
import com.edu.feicui.newsclient.base.BaseActivity;
import com.edu.feicui.newsclient.biz.NewsManager;
import com.edu.feicui.newsclient.entity.BaseEntity;
import com.edu.feicui.newsclient.entity.Comment;
import com.edu.feicui.newsclient.entity.UserResponse;
import com.edu.feicui.newsclient.listener.FailListener;
import com.edu.feicui.newsclient.listener.SuccessListener;
import com.edu.feicui.newsclient.utils.CommonUtils;
import com.edu.feicui.newsclient.utils.OkHttpUtils;
import com.edu.feicui.newsclient.utils.SharedPreferencesUtils;
import com.edu.feicui.newsclient.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mac on 2016/12/1.
 */

public class CommentActivity extends BaseActivity{
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.listview)
    XListView listView;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.iv_send)
    ImageView ivSend;

    private CommentAdapter commentAdapter;

    private int refreshMode = 2;
    private int nid;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        nid = getIntent().getIntExtra("nid", 1);

        commentAdapter = new CommentAdapter(this);
        //启用上拉加载
        listView.setPullLoadEnable(true);
        //启动下拉刷新
        listView.setPullRefreshEnable(true);
        listView.setXListViewListener(listViewListener);
        refreshMode = NewsManager.MODE_PULL_REFRESH;
        listView.setAdapter(commentAdapter);
        loadNextComment(true);
    }

    @OnClick(R.id.btn_back)
    public void exit(){
        finish();
    }

    public void getCommentList(int cid, int dir, SuccessListener successListener, FailListener failListener){
        String tamp = CommonUtils.getCurrentDate();
        String url = Url.COMMENT + "?ver=0&nid=" + nid + "&type=1&stamp=" + tamp + "&cid=" + cid + "&dir=" +  dir + "&cnt=20";
        OkHttpUtils.doGet(url,successListener,failListener);
    }

    @OnClick(R.id.iv_send)
    public void sendMessage(){
        sendComment(sendSuccessListener,sendFailListener);
        loadNextComment(true);
    }

    public void sendComment(SuccessListener sendSuccessListener, FailListener sendFailListener){
        String ctx = etContent.getText().toString();
        BaseEntity<UserResponse> baseEntity = SharedPreferencesUtils.readBaseEntity(CommentActivity.this);
        String token = baseEntity.getData().getToken();
        String imei = CommonUtils.getIMEI(CommentActivity.this);
        String url = Url.CMT_COMMENT + "?ver=0&nid=" + nid + "&token=" + token + "&imei=" + imei + "&ctx=" + ctx;
        OkHttpUtils.doGet(url, sendSuccessListener, sendFailListener);
    }

    private XListView.IXListViewListener listViewListener = new XListView.IXListViewListener() {
        @Override
        public void onRefresh() {
            refreshMode = NewsManager.MODE_PULL_REFRESH;
            loadNextComment(false);
        }

        @Override
        public void onLoadMore() {
            refreshMode = NewsManager.MODE_LOAD_MORE;
            loadPrevComment();
        }
    };

    private void loadNextComment(boolean isNewType){
        int cid = 1;
        if (!isNewType){
            if (commentAdapter.getData().size() > 0){
                cid = commentAdapter.getData().get(0).getCid();

            }
        }
            getCommentList(cid,refreshMode,successListener,failListener);

    }

    private void loadPrevComment(){
        if (commentAdapter.getData().size() == 0){
            return;
        }

        int lastIndex = commentAdapter.getData().size() - 1;
        int cid = commentAdapter.getData().get(lastIndex).getCid();
        getCommentList(cid,refreshMode,successListener,failListener);

    }

    private SuccessListener successListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            Gson gson = new Gson();
            BaseEntity<List<Comment>> baseEntity = gson.fromJson(json, new TypeToken<BaseEntity<List<Comment>>>(){}.getType());
            listView.stopLoadMore();
            listView.stopRefresh();
            List<Comment> list = baseEntity.getData();
            boolean isClear = refreshMode == NewsManager.MODE_PULL_REFRESH ? true : false;
            commentAdapter.appendDataToAdapter(list, isClear);
            commentAdapter.notifyDataSetChanged();
        }
    };

    private FailListener failListener = new FailListener() {
        @Override
        public void onFail(String error) {
            listView.stopLoadMore();
            listView.stopRefresh();
            Toast.makeText(CommentActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
        }
    };

    private SuccessListener sendSuccessListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            Gson gson = new Gson();
            BaseEntity<UserResponse> baseEntity = gson.fromJson(json, new TypeToken<BaseEntity<UserResponse>>(){}.getType());
            System.out.println("=============================" + json);
            CommonUtils.showShortToast(CommentActivity.this, "发送成功");
        }
    };

    private FailListener sendFailListener = new FailListener() {
        @Override
        public void onFail(String error) {
            CommonUtils.showShortToast(CommentActivity.this, "发送失败");
        }
    };
}
