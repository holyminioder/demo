package com.edu.feicui.newsclient.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.entity.BaseEntity;
import com.edu.feicui.newsclient.entity.MessageEvent;
import com.edu.feicui.newsclient.entity.UserResponse;
import com.edu.feicui.newsclient.listener.FailListener;
import com.edu.feicui.newsclient.listener.SuccessListener;
import com.edu.feicui.newsclient.utils.CommonUtils;
import com.edu.feicui.newsclient.utils.OkHttpUtils;
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

public class ForgetPasswordFragment extends Fragment{
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.btn_confirm)
    public void findPassword(){
        String email = etEmail.getText().toString();
        if (!CommonUtils.verifyEmail(email)){
            CommonUtils.showShortToast(getActivity(), "邮箱格式错误，请重新输入");
            return;
        }

        OkHttpUtils.doGet(Url.FORGET_PASSWORD + "?ver=0&email=" + email,successListener,failListener);
    }

    private SuccessListener successListener = new SuccessListener() {
        @Override
        public void onSuccess(String json) {
            Gson gson = new Gson();
            BaseEntity<UserResponse> baseEntity = gson.fromJson(json, new TypeToken<BaseEntity<UserResponse>>(){}.getType());
            if (baseEntity.getStatus().equals("0")){
                if (baseEntity.getData().getResult() == 0){
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setType(MessageEvent.TYPE_LOGIN_FRAGMENT);
                    EventBus.getDefault().post(messageEvent);
                    CommonUtils.showShortToast(getActivity(), "成功发送至邮箱");
                    return;
                }else {
                    CommonUtils.showShortToast(getActivity(), baseEntity.getData().getExplain());
                }
            }else {
                CommonUtils.showShortToast(getActivity(), "发送失败，请重试");
            }
        }
    };

    private FailListener failListener = new FailListener() {
        @Override
        public void onFail(String error) {
            CommonUtils.showShortToast(getActivity(), "发送失败，请重试");
        }
    };

}
