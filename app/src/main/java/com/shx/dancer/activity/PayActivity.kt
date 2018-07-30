package com.shx.dancer.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.shx.dancer.R
import com.shx.dancer.base.BaseActivity

class PayActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        topbar.setTitle("支付")
    }
}
