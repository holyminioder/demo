package com.edu.feicui.newsclient.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.edu.feicui.newsclient.entity.BaseEntity;
import com.edu.feicui.newsclient.entity.User;
import com.edu.feicui.newsclient.entity.UserResponse;

/**
 * Created by mac on 2016/12/5.
 */

public class SharedPreferencesUtils {

    public static void saveBaseEntity(Context context, BaseEntity<UserResponse> baseEntity){
        SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("message",baseEntity.getMessage());
        editor.putString("status",baseEntity.getStatus());
        editor.putInt("result",baseEntity.getData().getResult());
        editor.putString("explain",baseEntity.getData().getExplain());
        editor.putString("token",baseEntity.getData().getToken());
        editor.commit();
    }

    public static BaseEntity<UserResponse> readBaseEntity(Context context){
        SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String explain = sp.getString("explain", "");
        String token = sp.getString("token", "");
        int result = sp.getInt("result", 0);
        String message = sp.getString("message", "");
        String status = sp.getString("status", "");

        UserResponse response = new UserResponse();
        response.setExplain(explain);
        response.setResult(result);
        response.setToken(token);

        BaseEntity<UserResponse> baseEntity = new BaseEntity<>();
        baseEntity.setData(response);
        baseEntity.setMessage(message);
        baseEntity.setStatus(status);
        return baseEntity;
    }

    public static boolean isLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        return TextUtils.isEmpty(token) ? false : true;
    }

    public static String getToken(Context context){
        SharedPreferences sp = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        return sp.getString("token","");
    }

    public static void saveUser(Context context, User user){
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", user.getUid());
        editor.putString("headImage", user.getPortrait());
        editor.commit();
    }

    public static void clearUser(Context context){
        SharedPreferences sp = context.getSharedPreferences("user_info",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();

        sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.clear();
        editor.commit();
    }

    public static void saveUserHeadImagePath(Context context, String imagePath){
        SharedPreferences sp = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("localHeadImage", imagePath);
        editor.commit();
    }
}
