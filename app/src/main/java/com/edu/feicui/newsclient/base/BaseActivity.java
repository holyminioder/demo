package com.edu.feicui.newsclient.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.edu.feicui.newsclient.R;

/**
 * Created by mac on 2016/11/23.
 */

public class BaseActivity extends FragmentActivity{
    private Dialog dialog;

    public void startActivity(Class<? extends Activity> clazz){
        startActivity(clazz, null, null);
    }

    public void startActivity(Class<? extends Activity> clazz, Bundle bundle){
        startActivity(clazz, bundle, null);
    }

    public void startActivity(Class<? extends Activity> clazz, Bundle bundle, Uri uri){
        Intent intent = new Intent(this, clazz);

        if (bundle != null){
            intent.putExtras(bundle);
        }

        if (uri != null){
            intent.setData(uri);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.guideanim_translate, R.anim.guide_scale);
    }

    public void showDialog(String message,boolean isCancel){
        View view = getLayoutInflater().inflate(R.layout.loading, null);
        ImageView ivLoading = (ImageView) view.findViewById(R.id.iv_loading);
        TextView tvLoading = (TextView) view.findViewById(R.id.tv_loading);

        if (!TextUtils.isEmpty(message)){
            tvLoading.setText(message);
        }

        Animation animation = AnimationUtils.loadAnimation(this,R.anim.loading);
        ivLoading.startAnimation(animation);

        dialog = new Dialog(this,R.style.loading_style);
        dialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        dialog.setCancelable(isCancel);
        dialog.show();
    }

    public void cancelDialog(){
        if (dialog != null && dialog.isShowing()){
            dialog.dismiss();
        }
    }
}
