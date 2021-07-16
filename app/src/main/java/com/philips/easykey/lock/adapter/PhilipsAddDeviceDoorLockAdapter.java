package com.philips.easykey.lock.adapter;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.bean.deviceAdd.PhilipsAddManuallyDeviceBean;
import com.philips.easykey.lock.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;



public class PhilipsAddDeviceDoorLockAdapter extends BaseQuickAdapter<PhilipsAddManuallyDeviceBean, BaseViewHolder> {

    private OnClickListener mOnClickListener;
    public PhilipsAddDeviceDoorLockAdapter(int convertId, ArrayList<PhilipsAddManuallyDeviceBean> dataList) {
        super(convertId, dataList);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, PhilipsAddManuallyDeviceBean data) {
        ImageView ivPhone = baseViewHolder.findView(R.id.ivPhone);
        TextView tvName = baseViewHolder.findView(R.id.tvName);
        tvName.setText(data.getName());
        ivPhone.setImageDrawable(getContext().getDrawable(data.getId()));
        ivPhone.setOnClickListener(v -> {
            mOnClickListener.onClick(v,data);
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(View v, @NotNull PhilipsAddManuallyDeviceBean data);
    }
}
