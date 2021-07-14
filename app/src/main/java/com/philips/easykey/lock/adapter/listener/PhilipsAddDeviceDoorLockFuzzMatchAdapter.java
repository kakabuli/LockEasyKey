package com.philips.easykey.lock.adapter.listener;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.deviceAdd.PhilipsAddManuallyDeviceBean;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PhilipsAddDeviceDoorLockFuzzMatchAdapter extends BaseQuickAdapter<PhilipsAddManuallyDeviceBean, BaseViewHolder> {

    private OnClickListener mOnClickListener;
    public PhilipsAddDeviceDoorLockFuzzMatchAdapter(int convertId) {
        super(convertId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PhilipsAddManuallyDeviceBean data) {
        TextView tvName = baseViewHolder.findView(R.id.tvDeviceName);
        tvName.setText(data.getName());

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(View v, @NotNull HashMap<String,Object> data);
    }
}
