package com.edu.feicui.newsclient.activity;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.adapter.GuideAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac on 2016/11/23.
 */

public class GuideActivity extends Activity{
    private List<View> listView = new ArrayList<View>();
    private GuideAdapter adapter;
    private ViewPager viewPager;
    private ImageView[] imageViews = new ImageView[4];

    private int[] images = {
            R.mipmap.welcome,
            R.mipmap.wy,
            R.mipmap.bd,
            R.mipmap.small
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("guide_config", Context.MODE_PRIVATE);
        boolean isFirstRun = sp.getBoolean("isFirstRun", true);
        if(isFirstRun){
			initSave();
            setContentView(R.layout.activity_guide);
            initView();
            initImageView();
        }else{
            Intent intent = new Intent(GuideActivity.this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void initImageView() {
        for(int i = 0;i < images.length;i++){
            ImageView imageview = (ImageView) getLayoutInflater().inflate(R.layout.guide_imageview, null);
            imageview.setImageResource(images[i]);
            listView.add(imageview);
        }
        adapter = new GuideAdapter(listView);
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                for(int i = 0;i < imageViews.length;i++){
                    if(i == arg0){
                        imageViews[i].setImageAlpha(255);
                    }else{
                        imageViews[i].setImageAlpha(128);
                    }
                }
                if(arg0 == imageViews.length - 1){
                    Intent intent = new Intent(GuideActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.guideanim_translate, R.anim.guide_scale);
                    finish();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }



    private void initSave() {
        SharedPreferences sp = getSharedPreferences("guide_config", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isFirstRun", false);
        editor.commit();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        imageViews[0] = (ImageView) findViewById(R.id.imageView1);
        imageViews[0].setImageAlpha(255);
        imageViews[1] = (ImageView) findViewById(R.id.imageView2);
        imageViews[1].setImageAlpha(128);
        imageViews[2] = (ImageView) findViewById(R.id.imageView3);
        imageViews[2].setImageAlpha(128);
        imageViews[3] = (ImageView) findViewById(R.id.imageView4);
        imageViews[3].setImageAlpha(128);
    }
}
