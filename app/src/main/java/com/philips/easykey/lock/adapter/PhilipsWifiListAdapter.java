package com.philips.easykey.lock.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;

import org.jetbrains.annotations.NotNull;

/**
 * author : Jack
 * time   : 2021/5/7
 * E-mail : wengmaowei@kaadas.com
 * desc   : wifi列表
 */
public class PhilipsWifiListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public PhilipsWifiListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, String s) {
        holder.setText(R.id.tvWifiName, s);
    }
}
