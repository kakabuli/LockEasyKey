package com.philips.easykey.lock.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * author : Jack
 * time   : 2021/4/22
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsVpHomeDevicesAdapter extends PhilipsBaseVPAdapter<PhilipsDeviceBean> {

    private OnClickMoreListener mOnClickMoreListener;
    private OnClickPasswordListener mOnClickPasswordListener;
    private OnClickMessageListener mOnClickMessageListener;
    private OnClickCallingListener mOnClickCallingListener;

    public PhilipsVpHomeDevicesAdapter(Context context, int convertId, List<PhilipsDeviceBean> dataList) {
        super(context, convertId, dataList);
    }

    @Override
    public void bindView(View view, PhilipsDeviceBean data,int position) {
        if (data == null) return;
        TextView tvDeviceName = view.findViewById(R.id.tvDeviceName);
        TextView tvLastRecord = view.findViewById(R.id.tvLastRecord);

        TextView tvCount = view.findViewById(R.id.tvCount);
        TextView tvCurrentPage = view.findViewById(R.id.tvCurrentPage);

        tvCurrentPage.setText(StringUtils.format("%1d", position + 1));
        tvCount.setText(tvCount.getContext().getString(R.string.philips_device_count, getCount()));
        ImageView ivPower = view.findViewById(R.id.ivPower);
        if (data.getPower() <= 20) {
            ivPower.setImageResource(R.drawable.philips_home_icon_battery_low);
        } else if (data.getPower() > 20 && data.getPower() <= 60) {
            ivPower.setImageResource(R.drawable.philips_home_icon_battery_low2);
        } else if (data.getPower() > 60 && data.getPower() <= 90) {
            ivPower.setImageResource(R.drawable.philips_home_icon_battery_low1);
        } else {
            ivPower.setImageResource(R.drawable.philips_home_icon_battery_full);
        }

        if (data.getPurview()==1){
            ///管理员
            view.findViewById(R.id.ivPwd).setVisibility(View.VISIBLE);
            view.findViewById(R.id.tvPwd).setVisibility(View.VISIBLE);
        }else {
            ///分享用户
            view.findViewById(R.id.ivPwd).setVisibility(View.GONE);
            view.findViewById(R.id.tvPwd).setVisibility(View.GONE);

        }
        view.findViewById(R.id.ivMore).setOnClickListener(v -> {
            if (mOnClickMoreListener != null) {
                mOnClickMoreListener.onClick(v, data);
            }
        });
        view.findViewById(R.id.ivMessage).setOnClickListener(v -> {
            if (mOnClickMessageListener != null) {
                mOnClickMessageListener.onClick(v, data);
            }
        });
        view.findViewById(R.id.tvLastRecord).setOnClickListener(v -> {
            if (mOnClickMessageListener != null) {
                mOnClickMessageListener.onClick(v, data);
            }
        });
        view.findViewById(R.id.ivPwd).setOnClickListener(v -> {
            if (mOnClickPasswordListener != null) {
                mOnClickPasswordListener.onClick(v, data);
            }
        });
        view.findViewById(R.id.ivVideo).setOnClickListener(v -> {
            if (mOnClickCallingListener != null) {
                mOnClickCallingListener.onClick(v, data);
            }
        });

        if(!TextUtils.isEmpty(data.getDeviceName())){
            if(data.getDeviceName().length() > 10){
                tvDeviceName.setText(data.getDeviceName().substring(0,10) + "...");
            }else {
                tvDeviceName.setText(data.getDeviceName());
            }
        }
        if (data.getLastRecordDetail() != null) {
            BleUtil.setTextViewOperationRecordByType(null,tvLastRecord, data.getLastRecordDetail());
            if(!TextUtils.isEmpty(tvLastRecord.getText())){
                String lastRecord = DateUtils.secondToDate2(data.getLastRecordDetail().getCreateTime())
                        + " " + tvLastRecord.getText().toString().trim();
                tvLastRecord.setText(lastRecord);
            }
        }

    }

    public void setOnClickMoreListener(OnClickMoreListener onClickMoreListener) {
        mOnClickMoreListener = onClickMoreListener;
    }

    public interface OnClickMoreListener {
        void onClick(View v, @NotNull PhilipsDeviceBean data);
    }

    public void setOnClickPasswordListener(OnClickPasswordListener onClickPasswordListener) {
        mOnClickPasswordListener = onClickPasswordListener;
    }

    public interface OnClickPasswordListener {
        void onClick(View v, @NotNull PhilipsDeviceBean data);
    }

    public void setOnClickMessageListener(OnClickMessageListener onClickMessageListener) {
        mOnClickMessageListener = onClickMessageListener;
    }

    public interface OnClickMessageListener {
        void onClick(View v, @NotNull PhilipsDeviceBean data);
    }

    public void setOnClickCallingListener(OnClickCallingListener onClickCallingListener) {
        mOnClickCallingListener = onClickCallingListener;
    }

    public interface OnClickCallingListener {
        void onClick(View v, @NotNull PhilipsDeviceBean data);
    }
}
