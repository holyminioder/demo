package com.edu.feicui.newsclient.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.edu.feicui.newsclient.entity.News;
import com.edu.feicui.newsclient.entity.SubType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2016/11/28.
 */

public class NewsDBManager {
    private NewsDBHelper helper;
    private Context context;

    public NewsDBManager(Context context){
        this.context = context;
        this.helper = new NewsDBHelper(context);
    }

    //获取新闻分类的方法
    public List<SubType> getNewsSubType(){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from news_type order by _id desc", null);
        List<SubType> list = new ArrayList<SubType>();
        while (cursor.moveToNext()){
            int subid = cursor.getInt(cursor.getColumnIndex("subid"));
            String subgroup = cursor.getString(cursor.getColumnIndex("subgroup"));
            SubType subType = new SubType(subid, subgroup);
            list.add(subType);
        }
        cursor.close();
        cursor = null;
        return list;
    }

    //保存新闻分类数据
    public void saveNewsType(List<SubType> list){
        if (list == null || list.size() == 0){
            return;
        }
        SQLiteDatabase db = helper.getWritableDatabase();
        for (int i = 0;i < list.size();i++){
            SubType subType = list.get(i);
            Cursor cursor = db.rawQuery("select * from news_type where subid = ?", new String[]{subType.getSubid() +  ""});
            if (cursor.moveToNext()){
                cursor.close();
                return;
            }
            cursor.close();
            ContentValues values = new ContentValues();
            values.put("subid", subType.getSubid());
            values.put("subgroup", subType.getSubgroup());
            db.insert("news_type", null, values);
        }
        db.close();
    }

    //查询指定的新闻是否已收藏
    public boolean isCollectNews(News news){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from lovenews where nid = ?", new String[]{String.valueOf(news.getNid())});
        boolean isExists = false;
        if (cursor.moveToFirst()){
            isExists = true;
        }
        cursor.close();
        return isExists;
    }
    //保存新闻数据
    public void save(News news){
        SQLiteDatabase db = helper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("nid", news.getNid());
        values.put("stamp", news.getStamp());
        values.put("icon", news.getIcon());
        values.put("title", news.getTitle());
        values.put("summary", news.getSummary());
        values.put("link", news.getLink());
        db.insert("lovenews", null, values);
    }
}
