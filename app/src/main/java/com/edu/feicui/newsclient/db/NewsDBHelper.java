package com.edu.feicui.newsclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mac on 2016/11/28.
 */

public class NewsDBHelper extends SQLiteOpenHelper{
    public NewsDBHelper(Context context){
        super(context, "news.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //用于保存新闻分类的数据
        db.execSQL("create table news_type(_id integer primary key autoIncrement,subid text,subgroup text)");
        db.execSQL("create table news(_id integer primary key autoIncrement,type text,nid text,stamp text,icon text,title text,summary text,link text)");
        db.execSQL("create table lovenews(_id integer primary key autoIncrement,type text,nid text,stamp text,icon text,title text,summary text,link text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
