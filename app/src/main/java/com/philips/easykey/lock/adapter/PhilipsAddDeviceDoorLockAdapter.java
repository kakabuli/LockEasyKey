package com.philips.easykey.lock.adapter;


import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;



public class PhilipsAddDeviceDoorLockAdapter extends BaseQuickAdapter<HashMap<String,Object>, BaseViewHolder> {

    private OnClickListener mOnClickListener;
    public PhilipsAddDeviceDoorLockAdapter(int convertId, ArrayList<HashMap<String,Object>> dataList) {
        super(convertId, dataList);
    }


    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HashMap<String,Object> data) {
        ImageView ivPhone = baseViewHolder.findView(R.id.ivPhone);
        TextView tvName = baseViewHolder.findView(R.id.tvName);
        tvName.setText((String) data.get("DoorlockName"));
        ivPhone.setImageDrawable((Drawable) data.get("DoorlockImage"));
        ivPhone.setOnClickListener(v -> {
            mOnClickListener.onClick(v,data);
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(View v, @NotNull HashMap<String,Object> data);
    }
}
