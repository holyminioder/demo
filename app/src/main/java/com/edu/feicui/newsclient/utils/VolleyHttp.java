package com.edu.feicui.newsclient.utils;

import android.content.Context;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edu.feicui.newsclient.R;

/**
 * Created by 13413449041 on 2016.11.28.
 */

public class VolleyHttp {

    private Context context;
    private static RequestQueue requestQueue;

    public VolleyHttp(Context context){
        this.context = context;
        if(this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(context);
        }
    }
    //发送请求，获取字符串
    public void sendStringRequset(String url, Response.Listener<String> listener,Response.ErrorListener errorListener){
        StringRequest request = new StringRequest(url,listener,errorListener);
        requestQueue.add(request);
    }

    //发送请求，获取图片
    public void sendImageRequest(String url, ImageLoader.ImageCache imageCache, ImageView imageView){
        ImageLoader imageLoader = new ImageLoader(requestQueue, imageCache);
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.drawable.defaultpic, android.R.drawable.ic_delete);
        imageLoader.get(url, imageListener);
    }
}
