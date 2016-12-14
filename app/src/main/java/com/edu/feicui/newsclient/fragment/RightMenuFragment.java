package com.edu.feicui.newsclient.fragment;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.activity.UserActivity;
import com.edu.feicui.newsclient.base.BaseActivity;
import com.edu.feicui.newsclient.biz.VersionManager;
import com.edu.feicui.newsclient.entity.MessageEvent;
import com.edu.feicui.newsclient.entity.Version;
import com.edu.feicui.newsclient.listener.FailListener;
import com.edu.feicui.newsclient.listener.SuccessListener;
import com.edu.feicui.newsclient.utils.CommonUtils;
import com.edu.feicui.newsclient.utils.LoadImage;
import com.edu.feicui.newsclient.utils.SharedPreferencesUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by mac on 2016/11/24.
 */

public class RightMenuFragment extends Fragment {
    @BindView(R.id.ll_unlogin)
    LinearLayout llUnLogin;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.iv_unlogin)
    ImageView ivUnlogin;
    @BindView(R.id.tv_unlogin)
    TextView tvUnlogin;
    @BindView(R.id.iv_login)
    ImageView ivLogin;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.tv_update_version)
    TextView tvUpdateVersion;

    @BindView(R.id.iv_share_weixin)
    ImageView ivWeixin;
    @BindView(R.id.iv_share_QQ)
    ImageView ivQQ;
    @BindView(R.id.iv_share_friend)
    ImageView ivFriend;
    @BindView(R.id.iv_share_weibo)
    ImageView ivWeibo;

    private static final String path = "http://118.244.212.82:9092/Images/test.apk";
    private long downloadId = -1;
    private DownloadManager downloadManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShareSDK.initSDK(getActivity(), "androidv1101");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_right_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void showShare(String platform) {
        ShareSDK.initSDK(getActivity());
        OnekeyShare oks = new OnekeyShare();
//关闭sso授权
        oks.disableSSOWhenAuthorize();

// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
// titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
// text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
// url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
// comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
// site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
// siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        oks.setPlatform(platform);
// 启动分享GUI
        oks.show(getActivity());
    }

    @OnClick(R.id.iv_share_weixin)
    public void sharedWeixin(){
        Platform platform = ShareSDK.getPlatform(getActivity(), Wechat.NAME);
        showShare(platform.getName());
    }

    @OnClick(R.id.iv_share_QQ)
    public void sharedQQ(){
        Platform platform = ShareSDK.getPlatform(getActivity(), QQ.NAME);
        showShare(platform.getName());
    }

    @OnClick(R.id.iv_share_weibo)
    public void sharedWeibo(){
        Platform platform = ShareSDK.getPlatform(getActivity(), SinaWeibo.NAME);
        showShare(platform.getName());
    }

    @OnClick(R.id.iv_share_friend)
    public void sharedFriend(){
        Platform platform = ShareSDK.getPlatform(getActivity(), WechatMoments.NAME);
        showShare(platform.getName());
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(receiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SharedPreferencesUtils.isLogin(getActivity())) {
            llUnLogin.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);

            SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            String username = sp.getString("username", "");
            String headImage = sp.getString("headImage", "");
            String localHeadImage = sp.getString("localHeadImage", "");

            tvLogin.setText(username);
            File file = new File(localHeadImage);
            if (file.exists()) {
                ivLogin.setImageURI(Uri.fromFile(file));
            } else {
                LoadImage loadImage = new LoadImage(getActivity());
                loadImage.displayBitmap(headImage, ivLogin);
            }
        } else {
            llLogin.setVisibility(View.GONE);
            llUnLogin.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.iv_unlogin)
    public void publishAddLoginFragmentEvent() {
        MessageEvent event = new MessageEvent();
        event.setType(MessageEvent.TYPE_LOGIN_FRAGMENT);
        event.setFragmentFullName(LoginFragment.class.getName());
        //发送事件
        EventBus.getDefault().post(event);
    }

    @OnClick(R.id.tv_unlogin)
    public void publishAddLoginFragmentEvent_2() {
        publishAddLoginFragmentEvent();
    }

    @OnClick(R.id.tv_login)
    public void gotoUserCenter() {
        BaseActivity activity = (BaseActivity) getActivity();
        activity.startActivity(UserActivity.class);
    }

    @OnClick(R.id.iv_login)
    public void gotoUserCenter2() {
        gotoUserCenter();
    }

    @OnClick(R.id.tv_update_version)
    public void getUpdateVersion() {
        VersionManager.getUpdateVersion(getActivity(), new SuccessListener() {
            @Override
            public void onSuccess(String json) {
                Gson gson = new Gson();
                Version version = gson.fromJson(json, Version.class);
                String versionCode = getVersionCode(getActivity());
                if (versionCode == version.getVersion()) {
                    CommonUtils.showShortToast(getActivity(), "已是最新版本，无需更新");
                } else {
                    download();
                }

            }
        }, new FailListener() {
            @Override
            public void onFail(String error) {

            }
        });
    }

    public String getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            return "0";
        }
    }

    public void download() {
        //获取DownloadManager实例
        downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        //生成一个指向资源的Uri
        Uri uri = Uri.parse(path);
        //创建一个下载请求
        DownloadManager.Request request = new DownloadManager.Request(uri);
        //为该下载请求设置一些对应的信息
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setVisibleInDownloadsUi(true);
        //下载时可见，下载完成之后移除
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setTitle("下载newsClient");
        request.setDescription("使用newsClient使用案例说明");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        request.setDestinationInExternalFilesDir(getActivity(), Environment.DIRECTORY_DOWNLOADS, "newsClient-" + date + ".apk");
        downloadId = downloadManager.enqueue(request);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (id == downloadId) {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(id);

                Cursor cursor = downloadManager.query(query);
                if (cursor.moveToFirst()) {
                    String localFileName = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                    //提示安装应用
                    Intent other = new Intent(Intent.ACTION_VIEW);
                    other.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    other.setDataAndType(Uri.parse("file://" + localFileName), "application/vnd.android.package-archive");
                    context.startActivity(other);
                } else {
                    System.out.println("==========================没有");
                }
            }
        }
    };
}
