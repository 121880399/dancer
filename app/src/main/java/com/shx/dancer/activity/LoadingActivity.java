package com.shx.dancer.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.shx.dancer.R;
import com.shx.dancer.base.BaseActivity;
import com.shx.dancer.common.CommonValues;
import com.shx.dancer.utils.SharedPreferencesUtil;

/**
 * Loading界面
 * */
public class LoadingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            return;
        }
        initView();
//        //判断SP中是否存有用户的信息
//        if (UserInfo.getUserInfoInstance() != null) {
//            mViewModel.getUserInfo(this);
//        }else{
            handler.sendEmptyMessageDelayed(1, 2000);
//        }
    }

    private void initView() {
        setContentView(R.layout.activity_loading);
    }

    @NonNull
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            //如果SP中有用户信息，则直接跳转到MainActivity中，如果SP中没有用户信息说明用户是第一次登录，需要跳转到用户引导页面
            if (msg.what == 0) {
                gotoMainActivity();
            }
            if (msg.what == 1) {
                boolean GuidShow = SharedPreferencesUtil.getBooleanValue(
                        getApplication(), CommonValues.SHOWGUIDE, true);
                if (GuidShow) {
//                    SharedPreferencesUtil.saveValue(LoadingActivity.this, CommonValues.SHOWGUIDE, false);
//                    startActivity(new Intent(LoadingActivity.this, UserGuideActivity.class));
//                    finish();
                    gotoMainActivity();
                    return;
                }
                gotoMainActivity();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
