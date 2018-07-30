package com.shx.dancer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.ViewHolder> {
    @NonNull
    @Override
    public RechargeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RechargeAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mText;
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

}
