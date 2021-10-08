package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockOperationRecord;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.List;

public class PhilipsWifiLockRecordIAdapter extends BaseQuickAdapter<WifiLockOperationRecord, BaseViewHolder> {

    public PhilipsWifiLockRecordIAdapter(@Nullable List<WifiLockOperationRecord> data) {
        super(R.layout.philips_item_wifi_lock_record_layout,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiLockOperationRecord record) {
        boolean first = record.isFirst();

        TextView tvTime = helper.getView(R.id.tv_time);
        TextView tvDayTime = helper.getView(R.id.tv_day_time);
        ImageView ivIcon = helper.getView(R.id.iv);
        long time = record.getTime();
        String s = DateUtils.currentLong2HourMinSecond(time * 1000);
        tvTime.setText(TextUtils.isEmpty(s) ? "" : s);
        tvDayTime.setVisibility( first? View.VISIBLE : View.GONE);
        //设置天时间
        String dayTime = record.getDayTime();
        if (!TextUtils.isEmpty(dayTime)) {
            if (dayTime.equals(DateUtils.getCurrentYMD())) {
                dayTime = tvDayTime.getContext().getString(R.string.today);
            }
            tvDayTime.setText(dayTime+"");
        }

        BleUtil.setTextViewOperationRecordByType(ivIcon,helper.getView(R.id.tv_content),record);

    }
}
