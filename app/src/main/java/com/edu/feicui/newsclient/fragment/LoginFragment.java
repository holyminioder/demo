package com.edu.feicui.newsclient.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.activity.UserActivity;
import com.edu.feicui.newsclient.entity.BaseEntity;
import com.edu.feicui.newsclient.entity.MessageEvent;
import com.edu.feicui.newsclient.entity.UserResponse;
import com.edu.feicui.newsclient.listener.FailListener;
import com.edu.feicui.newsclient.listener.SuccessListener;
import com.edu.feicui.newsclient.utils.CommonUtils;
import com.edu.feicui.newsclient.utils.OkHttpUtils;
import com.edu.feicui.newsclient.utils.SharedPreferencesUtils;
import com.edu.feicui.newsclient.utils.Url;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by mac on 2016/12/2.
 */

public class LoginFragment extends Fragment{
    @BindView(R.id.et_nikename)
    EditText etNikename;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.btn_forgot_password)
    Button btnForgotPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_register)
    public void publishAddRegisterFragmentEvent(){
        MessageEvent event = new MessageEvent();
        event.setType(MessageEvent.TYPE_REGISTER_FRAGMENT);
        event.setFragmentFullName(RegisterFragment.class.getName());
        EventBus.getDefault().post(event);
    }

    @OnClick(R.id.btn_forgot_password)
    public void publishAddForgotPasswordFragmentEvent(){
        MessageEvent event = new MessageEvent();
        event.setType(MessageEvent.TYPE_FORGOT_PASSWORD_FRAGMENT);
        event.setFragmentFullName(ForgetPasswordFragment.class.getName());
        EventBus.getDefault().post(event);
    }

    @OnClick(R.id.btn_login)
    public void login(){
        String nikename = etNikename.getText().toString();
        String password = etPassword.getText().toString();
        if (!CommonUtils.verifyName(nikename)){
            CommonUtils.showShortToast(getActivity(), "你输入的用户名不规范");
        }
        if (!CommonUtils.verifyPassword(password)){
            CommonUtils.showShortToast(getActivity(), "你输入的密码格式不正确");
        }

        OkHttpUtils.doGet(Url.LOGIN + "?ver=0&uid=" + nikename + "&pwd=" + password + "&device=0",successListener,failListener);
    }

    private SuccessListener successListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            Gson gson = new Gson();
            BaseEntity<UserResponse> baseEntity = gson.fromJson(json, new TypeToken<BaseEntity<UserResponse>>(){}.getType());
            if (baseEntity.getStatus().equals("0")){
                if (baseEntity.getData().getResult() == 0){
                    SharedPreferencesUtils.saveBaseEntity(getActivity(), baseEntity);
//                    MessageEvent messageEvent = new MessageEvent();
//                    messageEvent.setType(MessageEvent.TYPE_MAIN_FRAGMENT);
//                    EventBus.getDefault().post(messageEvent);
                    startActivity(new Intent(getActivity(), UserActivity.class));
                    getActivity().overridePendingTransition(R.anim.guideanim_translate,R.anim.guide_scale);

                    CommonUtils.showShortToast(getActivity(), "登录成功");
                }else {
                    CommonUtils.showShortToast(getActivity(), baseEntity.getData().getExplain());
                }
            }else {
                CommonUtils.showShortToast(getActivity(), "登录失败，请重试");
            }
        }
    };

    private FailListener failListener = new FailListener() {
        @Override
        public void onFail(String error) {
            CommonUtils.showShortToast(getActivity(), "网络异常，请重试");
        }
    };
}
