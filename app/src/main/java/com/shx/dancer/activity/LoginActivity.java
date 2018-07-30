package com.shx.dancer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shx.dancer.R;
import com.shx.dancer.base.BaseActivity;
import com.shx.dancer.dialog.ToastUtil;
import com.shx.dancer.http.RequestCenter;
import com.shx.dancer.http.ZCResponse;
import com.shx.dancer.utils.CountDownTimerUtils;

/**
 * Created by zhou on 2018/2/1.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText phoneEt;
    private EditText passwordEt;
    private Button loginBtn;
    private CountDownTimerUtils mCountDownTimerUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isPermissionOK();
        initView();
    }

    private void initView() {
        phoneEt = (EditText) findViewById(R.id.et_phone);
        passwordEt = (EditText) findViewById(R.id.et_password);
        loginBtn = (Button) findViewById(R.id.btn_login);
        getTopbar().setTitle("登录");
        getTopbar().setRightText("注册");
        getTopbar().setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        loginBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                startActivity(new Intent(this, MainActivity.class));
                if (TextUtils.isEmpty(phoneEt.getText().toString()) || TextUtils.isEmpty(passwordEt.getText().toString())) {
                    ToastUtil.getInstance().toastInCenter(this, "请将信息填写完整！");
                } else {
                    RequestCenter.login(phoneEt.getText().toString(), passwordEt.getText().toString(), this);
                }

                break;

        }
    }

    @Override
    public boolean doSuccess(ZCResponse respose, String requestUrl) {
        return super.doSuccess(respose, requestUrl);
    }
}
