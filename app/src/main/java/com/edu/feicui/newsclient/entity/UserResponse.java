package com.edu.feicui.newsclient.entity;

/**
 * Created by mac on 2016/12/5.
 */

public class UserResponse {
    private int result;
    private String explain;
    private String token;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
