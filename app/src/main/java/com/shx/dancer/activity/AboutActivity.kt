package com.shx.dancer.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.shx.dancer.BaseApplication
import com.shx.dancer.R
import com.shx.dancer.base.BaseActivity

class AboutActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        topbar.setTitle("关于我们")
    }
}
