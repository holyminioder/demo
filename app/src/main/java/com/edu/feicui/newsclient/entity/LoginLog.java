package com.edu.feicui.newsclient.entity;

/**
 * Created by mac on 2016/12/7.
 */

public class LoginLog {
    private String time;
    private String address;
    private int device;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }
}
