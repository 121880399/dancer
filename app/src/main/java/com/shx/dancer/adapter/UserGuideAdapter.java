package com.shx.dancer.adapter;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by 周正一 on 2017/2/21.
 */

public class UserGuideAdapter extends PagerAdapter{

    private List<View> views;

    public UserGuideAdapter(List<View> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views==null ? 0:views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view==object);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(views.get(position),0);
        return views.get(position);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
