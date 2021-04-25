package com.philips.easykey.lock.adapter;

import android.util.TypedValue;
import android.widget.TextView;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDeviceTypeBean;

import org.jetbrains.annotations.NotNull;

/**
 * author :
 * time   : 2021/4/21
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsDeviceTypeAdapter extends BaseQuickAdapter<PhilipsDeviceTypeBean, BaseViewHolder> {

    public PhilipsDeviceTypeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, PhilipsDeviceTypeBean s) {
        if(s == null) return;
        holder.setText(R.id.tvDeviceTypeName, s.getTypeName());
        holder.setTextColor(R.id.tvDeviceTypeName, s.isSelected()
                ?getContext().getColor(R.color.color_333333)
                :getContext().getColor(R.color.c666666));
        ((TextView) holder.getView(R.id.tvDeviceTypeName)).setTextSize(TypedValue.COMPLEX_UNIT_PT, s.isSelected()? 16:14);
        holder.setVisible(R.id.vSelected, s.isSelected());
    }
}
