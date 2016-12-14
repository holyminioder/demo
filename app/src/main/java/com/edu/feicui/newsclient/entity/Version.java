package com.edu.feicui.newsclient.entity;

/**
 * Created by mac on 2016/12/9.
 */

public class Version {
    private String packaName;
    private String version;
    private String link;
    private String md5;

    public String getPackaName() {
        return packaName;
    }

    public void setPackaName(String packaName) {
        this.packaName = packaName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
