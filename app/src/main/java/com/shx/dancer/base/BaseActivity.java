package com.shx.dancer.base;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shx.dancer.R;
import com.shx.dancer.activity.MainActivity;
import com.shx.dancer.custom.ActionBarView;
import com.shx.dancer.dialog.ToastUtil;
import com.shx.dancer.http.HttpCallBack;
import com.shx.dancer.http.HttpTrowable;
import com.shx.dancer.http.ZCResponse;
import com.shx.dancer.utils.PermissionChecker;

public class BaseActivity extends AppCompatActivity implements HttpCallBack{
    protected ActionBarView topbarView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        APPActivityManager.getInstance().addActivity(this);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType. E_UM_NORMAL);
//        PushManager.getInstance().initialize(getApplicationContext(),null);
//        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), PushService.class);
    }
    public ActionBarView getTopbar() {
        if (topbarView == null) {
            View view = findViewById(R.id.topbar_view);
            if (view != null) {
                topbarView = new ActionBarView(view);
            }
        }
        return topbarView;
    }

    public void  gotoMainActivity(){
//        startActivity(new Intent(this, AliyunPlayerSkinActivity.class));
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    @Override
    public boolean doSuccess(ZCResponse respose, String requestUrl) {
        return false;
    }

    public boolean doFaild(HttpTrowable error, String url) {
        return false;
    }
    @Override
    public boolean httpCallBackPreFilter(String result, String url) {
        return false;
    }

    protected boolean isPermissionOK() {
        PermissionChecker checker = new PermissionChecker(this);
        boolean isPermissionOK = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || checker.checkPermission();
        if (!isPermissionOK) {
            ToastUtil.getInstance().toastInCenter(this,"Some permissions is not approved !!!");
        }
        return isPermissionOK;
    }
}
