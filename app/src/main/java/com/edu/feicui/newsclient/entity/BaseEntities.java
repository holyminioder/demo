package com.edu.feicui.newsclient.entity;

import java.util.List;

/**
 * Created by 13413449041 on 2016.11.28.
 */

public class BaseEntities<T> {

    private String status;
    private String message;
    private List<T> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public BaseEntities(String status, String message, List<T> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public BaseEntities() {
    }
}
