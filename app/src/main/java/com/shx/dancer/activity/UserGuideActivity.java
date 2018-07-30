//package com.shx.dancer.activity;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//
//import com.shx.dancer.R;
//import com.shx.dancer.adapter.UserGuideAdapter;
//import com.shx.dancer.base.BaseActivity;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 用户向导Activity
// * Created by 周正一 on 2017/2/21.
// */
//
//public class UserGuideActivity extends BaseActivity {
//    @Nullable
//    private ViewPager mViewPager;
//    private List<View> views;
//    private UserGuideAdapter mAdapter;
//    //引导图片资源
//    private static final int[] pics = { R.drawable.img_user_guide_00,
//            R.drawable.img_user_guide_01, R.drawable.img_user_guide_02};
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        initView();
//        initData();
//    }
//
//    private void initView() {
//        setContentView(R.layout.activity_user_guide);
//        mViewPager= (ViewPager) findViewById(R.id.vp_userGuide);
//        views=new ArrayList<>();
//    }
//
//    private void initData(){
//        //设置长宽为全屏
//        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT);
//        //初始化引导图片列表
//        for(int i=0; i<pics.length; i++) {
//            ImageView iv = new ImageView(this);
//            iv.setLayoutParams(mParams);
//            iv.setImageResource(pics[i]);
//            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            views.add(iv);
//        }
//        mAdapter=new UserGuideAdapter(views);
//        mViewPager.setAdapter(mAdapter);
//        views.get(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mViewPager.setCurrentItem(1);
//            }
//        });
//        views.get(1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mViewPager.setCurrentItem(2);
//            }
//        });
//        views.get(views.size()-1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                gotoMainActivity();
//            }
//        });
//    }
//}
