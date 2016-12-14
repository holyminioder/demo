package com.edu.feicui.newsclient.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by mac on 2016/11/29.
 */

public class LoadImage {
    private Context context;
    private LruCache<String, Bitmap> cache = new LruCache<>(4 * 1024 * 1024);

    public LoadImage(Context context){
        this.context = context;
    }

    public void displayBitmap(String url, ImageView imageView){
        if (TextUtils.isEmpty(url)){
            return;
        }
        Bitmap bitmap = cache.get(url);
        if (bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }

        //如果内存中没有缓存该图片，则去缓存的文件中查找是否有该图片，如果有，则显示该图片，如果没有，则继续后面的操作
        bitmap = getBitmapFromFileCache(url);
        if (bitmap != null){
            cache.put(url, bitmap);
            imageView.setImageBitmap(bitmap);
            return;
        }

        //如果文件缓存中也没有对应的图片，则从网络进行加载图片，并缓存至文件与内存中
        VolleyHttp volleyHttp = new VolleyHttp(context);
        volleyHttp.sendImageRequest(url, imageCache, imageView);
    }

    private ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache(){

        @Override
        public Bitmap getBitmap(String s) {
            return cache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            cache.put(s, bitmap);
            saveBitmapToFileCache(s, bitmap);
        }
    };

    private void saveBitmapToFileCache(String url, Bitmap bitmap){
        String filename = url.substring(url.lastIndexOf("/") + 1);
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null){
            return;
        }
        if (!cacheDir.exists()){
            cacheDir.mkdirs();
        }

        try {
            OutputStream stream = new FileOutputStream(new File(cacheDir, filename));
            //第一个参数为图片类型，第二个参数为图片的清晰度，第三个参数为写入的文件路径
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getBitmapFromFileCache(String url){
        String filename = url.substring(url.lastIndexOf("/") + 1);

        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null && cacheDir.exists()){
            File bitmapFile = new File(cacheDir, filename);
            if (bitmapFile != null && bitmapFile.exists()){
                return BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
            }
        }
        return null;
    }
}
