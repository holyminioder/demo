package com.edu.feicui.newsclient.biz;

import android.content.Context;
import android.content.pm.PackageManager;

import com.edu.feicui.newsclient.listener.FailListener;
import com.edu.feicui.newsclient.listener.SuccessListener;
import com.edu.feicui.newsclient.utils.CommonUtils;
import com.edu.feicui.newsclient.utils.OkHttpUtils;
import com.edu.feicui.newsclient.utils.Url;

/**
 * Created by mac on 2016/12/8.
 */

public class VersionManager {
    public static void getUpdateVersion(Context context, SuccessListener successListener, FailListener failListener){
        String imei = CommonUtils.getIMEI(context);
        String packageName = context.getPackageName();
        OkHttpUtils.doGet(Url.UPDATE_VERSION + "?ver=0&imei=" + imei + "&pkg=" + packageName, successListener, failListener);
    }
}
