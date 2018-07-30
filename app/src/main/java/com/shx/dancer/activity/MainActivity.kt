package com.shx.dancer.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import android.view.View.OnClickListener
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.shx.dancer.R
import com.shx.dancer.base.BaseActivity
import com.shx.dancer.common.CommonWebFragment
import com.shx.dancer.common.SystemConfig

class MainActivity : BaseActivity(), OnClickListener {


    private var mContent: FrameLayout? = null
    private var mMainFragment: Fragment? = null
    private var mPersonCenterFragment: Fragment? = null
    private var mFavoriteFragment: Fragment? = null
    private var mMain: TextView? = null
    private var mLive: TextView? = null
    private var mDiscovery: TextView? = null
    private var mTest: ImageView? = null
    private var mAccountCenter: TextView? = null
    private var mFragmentManager: FragmentManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        isPermissionOK()
    }


    private fun initView() {
        mMain = findViewById(R.id.rb_main) as TextView
        mLive = findViewById(R.id.rb_live) as TextView
        mTest = findViewById(R.id.rb_test) as ImageView
        mDiscovery = findViewById(R.id.rb_discovery) as TextView
        mAccountCenter = findViewById(R.id.rb_my) as TextView
        mContent = findViewById(R.id.content) as FrameLayout
        mMain!!.setOnClickListener(this)
        mTest!!.setOnClickListener(this)
        mLive!!.setOnClickListener(this)
        mDiscovery!!.setOnClickListener(this)
        mAccountCenter!!.setOnClickListener(this)
        setSelected()
        mMain!!.performClick()
    }

    private fun showMainFragment() {
        mFragmentManager = supportFragmentManager
        mMainFragment = CommonWebFragment(SystemConfig.WEBBASEURL+"/layout", "首页")
        val transaction = mFragmentManager!!.beginTransaction()
        transaction.replace(R.id.content, mMainFragment)
        transaction.commitAllowingStateLoss()

    }

    private fun showPersonFragment() {
        mFragmentManager = supportFragmentManager
        mMainFragment = CommonWebFragment(SystemConfig.WEBBASEURL+"/my_profile_home", "个人中心")
        val transaction = mFragmentManager!!.beginTransaction()
        transaction.replace(R.id.content, mMainFragment)
        transaction.commitAllowingStateLoss()

    }

    private fun showDiscoryFragment() {
        mFragmentManager = supportFragmentManager
        mMainFragment = CommonWebFragment(SystemConfig.WEBBASEURL+"/discover_list", "发现")
        val transaction = mFragmentManager!!.beginTransaction()
        transaction.replace(R.id.content, mMainFragment)
        transaction.commitAllowingStateLoss()

    }

    private fun showLiveFragment() {
        mFragmentManager = supportFragmentManager
        mMainFragment = CommonWebFragment(SystemConfig.WEBBASEURL+"/live_home", "直播")
        val transaction = mFragmentManager!!.beginTransaction()
        transaction.replace(R.id.content, mMainFragment)
        transaction.commitAllowingStateLoss()

    }

    //重置所有文本的选中状态
    private fun setSelected() {
        mMain!!.setSelected(false)
        mLive!!.setSelected(false)
        mDiscovery!!.setSelected(false)
        mAccountCenter!!.setSelected(false)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.rb_main -> {
                setSelected()
                mMain!!.setSelected(true)
                showMainFragment()
            }
            R.id.rb_live -> {
                setSelected()
                mLive!!.setSelected(true)
                showLiveFragment()
            }
            R.id.rb_discovery -> {
                setSelected()
                mDiscovery!!.setSelected(true)
                showDiscoryFragment()
            }
            R.id.rb_my -> {
                setSelected()
                mAccountCenter!!.setSelected(true)
                showPersonFragment()
            }
            R.id.rb_test -> {
                val intent = Intent()
                //获取intent对象
                intent.setClass(this, RecordActivity2::class.java)
                // 获取class是使用::反射
                startActivity(intent)
            }
        }

    }
}
