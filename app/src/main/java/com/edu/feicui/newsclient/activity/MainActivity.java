package com.edu.feicui.newsclient.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.feicui.newsclient.R;
import com.edu.feicui.newsclient.base.BaseActivity;
import com.edu.feicui.newsclient.entity.Constant;
import com.edu.feicui.newsclient.entity.MessageEvent;
import com.edu.feicui.newsclient.fragment.ForgetPasswordFragment;
import com.edu.feicui.newsclient.fragment.LeftMenuFragment;
import com.edu.feicui.newsclient.fragment.LoginFragment;
import com.edu.feicui.newsclient.fragment.MainFragment;
import com.edu.feicui.newsclient.fragment.RegisterFragment;
import com.edu.feicui.newsclient.fragment.RightMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {
    private ImageView ivMenuLeft;
    private ImageView ivMenuRight;
    private TextView tvTitle;
    private  long prevTime;

    public static SlidingMenu slidingMenu;
    private Fragment leftMenuFragment;
    private Fragment rightMenuFragment;
    private MainFragment mainFragment;
    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;
    private ForgetPasswordFragment forgetPasswordFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initSlidingMenu();
        ivMenuLeft.setOnClickListener(listener);
        ivMenuRight.setOnClickListener(listener);
        initMainFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //注册订阅者
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void initMainFragment() {
        if (mainFragment == null){
            mainFragment = new MainFragment();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.rl_content, mainFragment);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (slidingMenu != null && slidingMenu.isMenuShowing()){
            slidingMenu.showContent();
        }else {
            twiceExit();
        }
    }

    private void twiceExit(){
        long currentTime = System.currentTimeMillis();
        if (currentTime - prevTime > 1500){
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            prevTime = currentTime;
        }else {
            prevTime = currentTime;
            System.exit(0);
        }
    }

    private void initSlidingMenu() {
        leftMenuFragment = new LeftMenuFragment();
        rightMenuFragment = new RightMenuFragment();

        slidingMenu = new SlidingMenu(this);
        //设置侧滑模式，左侧滑，右侧滑，左右侧滑
        slidingMenu.setMode(SlidingMenu.LEFT_RIGHT);
        //设置触摸屏幕可以通过滑动显示侧滑菜单
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        //设置侧滑菜单显示之后activity剩余的空间大小
        slidingMenu.setBehindOffsetRes(R.dimen.sliding_menu_margin);
        //设置该侧滑菜单依附在这个activity内容里面
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

        //为侧滑菜单设置布局
        slidingMenu.setMenu(R.layout.layout_left_menu);
        //设置右边（二级）侧滑菜单
        slidingMenu.setSecondaryMenu(R.layout.layout_right_menu);

        getSupportFragmentManager().beginTransaction().
                replace(R.id.ll_left_container, leftMenuFragment).commit();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.ll_right_container, rightMenuFragment).commit();
    }

    //初始化组件
    private void initView() {
        ivMenuLeft = (ImageView) findViewById(R.id.iv_left_menu_icon);
        ivMenuRight = (ImageView) findViewById(R.id.iv_right_menu_icon);
        tvTitle = (TextView) findViewById(R.id.tv_title);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchFragment(MessageEvent event){
        String title = "";
        switch (event.getType()){
            case MessageEvent.TYPE_FORGOT_PASSWORD_FRAGMENT:
                if (forgetPasswordFragment == null){
                    forgetPasswordFragment = new ForgetPasswordFragment();
                }
                title = "忘记密码";
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, forgetPasswordFragment).commit();
                break;
            case MessageEvent.TYPE_MAIN_FRAGMENT:
                if (mainFragment == null){
                    mainFragment = new MainFragment();
                }
                title = "资讯";
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, mainFragment).commit();
                break;
            case MessageEvent.TYPE_LOGIN_FRAGMENT:
                if (loginFragment == null){
                    loginFragment = new LoginFragment();
                }
                title = "用户登录";
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_content,loginFragment).commit();
                break;
            case MessageEvent.TYPE_REGISTER_FRAGMENT:
                if (registerFragment == null){
                    registerFragment = new RegisterFragment();
                }
                title = "用户注册";
                getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, registerFragment).commit();
                break;
        }
        tvTitle.setText(title);

        if (slidingMenu != null && slidingMenu.isMenuShowing()){
            slidingMenu.showContent();
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_left_menu_icon://当侧滑没有显示，则显示侧滑，当侧滑显示，则收起侧滑
                    if (slidingMenu != null && slidingMenu.isMenuShowing()) {
                        slidingMenu.showContent();
                    } else if (slidingMenu != null) {
                        slidingMenu.showMenu();
                    }
                    break;
                case R.id.iv_right_menu_icon:
                    if (slidingMenu != null && slidingMenu.isMenuShowing()) {
                        slidingMenu.showContent();
                    } else if (slidingMenu != null) {
                        slidingMenu.showSecondaryMenu();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        addMainFragment();
    }
    private void addMainFragment(){
        if (mainFragment == null){
            mainFragment = new MainFragment();
        }
        tvTitle.setText("资讯");
        getSupportFragmentManager().beginTransaction().replace(R.id.rl_content, mainFragment).commit();
    }
}
