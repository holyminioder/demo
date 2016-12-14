package com.edu.feicui.newsclient.entity;

import java.io.Serializable;

/**
 * Created by mac on 2016/11/29.
 */

public class News implements Serializable{
    //新闻类型，1为列表新闻，2为大图新闻
    private int type;
    //新闻编号
    private int nid;
    //时间戳
    private String stamp;
    //图片
    private String icon;
    //新闻标题
    private String title;
    //新闻摘要
    private String summary;
    //新闻链接
    private String link;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public int getNid() {
        return nid;
    }

    public void setNid(int nid) {
        this.nid = nid;
    }

    public News(int type, String link, String summary, String title, String icon, String stamp, int nid) {
        this.type = type;
        this.link = link;
        this.summary = summary;
        this.title = title;
        this.icon = icon;
        this.stamp = stamp;
        this.nid = nid;
    }

    public News() {

    }
}
