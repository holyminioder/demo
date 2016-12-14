package com.edu.feicui.newsclient.parser;

import com.edu.feicui.newsclient.entity.BaseEntity;
import com.edu.feicui.newsclient.entity.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by mac on 2016/12/7.
 */

public class UserParser {
    public static BaseEntity<User> parseUser(String json){
        Gson gson = new Gson();
        BaseEntity<User> baseEntity = gson.fromJson(json, new TypeToken<BaseEntity<User>>(){}.getType());
        return baseEntity;
    }
}
