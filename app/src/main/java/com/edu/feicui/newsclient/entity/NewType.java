package com.edu.feicui.newsclient.entity;

import java.util.List;

/**
 * Created by 13413449041 on 2016.11.28.
 */

public class NewType {
    private int gid;
    private List<SubType> subgrp;
    private String group;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public List<SubType> getSubgrp() {
        return subgrp;
    }

    public void setSubgrp(List<SubType> subgrp) {
        this.subgrp = subgrp;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public NewType(int gid, List<SubType> subgrp, String group) {
        this.gid = gid;
        this.subgrp = subgrp;
        this.group = group;
    }

    public NewType() {
    }
}
