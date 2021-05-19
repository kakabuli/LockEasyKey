package com.philips.easykey.lock.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

/**
 * author :
 * time   : 2021/4/22
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsRvHomeDeviceAdapter extends BaseQuickAdapter<PhilipsDeviceBean, BaseViewHolder> {
    public PhilipsRvHomeDeviceAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, PhilipsDeviceBean bean) {
        if(bean == null) return;

        TextView tvLastRecord = holder.findView(R.id.tvLastRecord);
        holder.setText(R.id.tvDeviceName, StringUtil.processEmptyString(bean.getDeviceName()));

        if(bean.getLastRecordDetail() != null){
            BleUtil.setTextViewOperationRecordByType(tvLastRecord,bean.getLastRecordDetail());
            tvLastRecord.setText(DateUtils.secondToDate2(bean.getLastRecordDetail().getCreateTime())
                    + " " + tvLastRecord.getText().toString().trim());
        }

        if(bean.getPower() <= 30){
            holder.setImageResource(R.id.ivPower,R.drawable.philips_home_icon_battery_low);
        }else if(bean.getPower() > 30 && bean.getPower() <= 60){
            holder.setImageResource(R.id.ivPower,R.drawable.philips_home_icon_battery_low2);
        }else if(bean.getPower() > 60 && bean.getPower() <= 90){
            holder.setImageResource(R.id.ivPower,R.drawable.philips_home_icon_battery_low1);
        }else {
            holder.setImageResource(R.id.ivPower,R.drawable.philips_home_icon_battery_full);
        }
    }
}
