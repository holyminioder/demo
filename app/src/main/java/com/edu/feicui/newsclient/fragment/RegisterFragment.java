package com.edu.feicui.newsclient.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.entity.BaseEntity;
import com.edu.feicui.newsclient.entity.UserResponse;
import com.edu.feicui.newsclient.listener.FailListener;
import com.edu.feicui.newsclient.listener.SuccessListener;
import com.edu.feicui.newsclient.utils.CommonUtils;
import com.edu.feicui.newsclient.utils.OkHttpUtils;
import com.edu.feicui.newsclient.utils.SharedPreferencesUtils;
import com.edu.feicui.newsclient.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mac on 2016/12/2.
 */

public class RegisterFragment extends Fragment{
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_nikename)
    EditText etNikename;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.cb_agree)
    CheckBox cbAgree;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_register)
    public void register(){
        String email = etEmail.getText().toString();
        String username = etNikename.getText().toString();
        String password = etPassword.getText().toString();
        boolean isAgree = cbAgree.isChecked();

        if (!isAgree){
            Toast.makeText(getActivity(), "必需要同意条款才能注册", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!CommonUtils.verifyEmail(email)){
            CommonUtils.showShortToast(getActivity(), "邮箱格式不正确，请重新输入");
            return;
        }

        if (!CommonUtils.verifyName(username)){
            CommonUtils.showShortToast(getActivity(), "用户名不符合规范，请重命名");
            return;
        }

        if (!CommonUtils.verifyPassword(password)){
            CommonUtils.showShortToast(getActivity(), "密码必须在6~24位之间");
            return;
        }

        OkHttpUtils.doGet(Url.REGISTER + "?ver=0&uid=" + username + "&pwd=" + password + "&email=" + email,successListener,failListener);
    }

    private SuccessListener successListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            Gson gson = new Gson();
            BaseEntity<UserResponse> baseEntity = gson.fromJson(json, new TypeToken<BaseEntity<UserResponse>>(){}.getType());
            if (baseEntity.getStatus().equals("0")){
                if (baseEntity.getData().getResult() == 0){
                    SharedPreferencesUtils.saveBaseEntity(getActivity(), baseEntity);
                    CommonUtils.showShortToast(getActivity(),"注册成功");
                    return;
                }
                CommonUtils.showShortToast(getActivity(), baseEntity.getData().getExplain());
            }else {
                CommonUtils.showShortToast(getActivity(), "注册失败，请重试");
            }
        }
    };

    private FailListener failListener = new FailListener() {
        @Override
        public void onFail(String error) {
            CommonUtils.showShortToast(getActivity(), "访问失败，请重试");
        }
    };

}
