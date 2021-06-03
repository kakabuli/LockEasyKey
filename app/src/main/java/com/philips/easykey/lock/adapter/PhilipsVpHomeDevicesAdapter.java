package com.philips.easykey.lock.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void bindView(View view, PhilipsDeviceBean data) {
        if (data == null) return;
        TextView tvDeviceName = view.findViewById(R.id.tvDeviceName);
        TextView tvLastRecord = view.findViewById(R.id.tvLastRecord);

        ImageView ivPower = view.findViewById(R.id.ivPower);
        if (data.getPower() <= 30) {
            ivPower.setImageResource(R.drawable.philips_home_icon_battery_low);
        } else if (data.getPower() > 30 && data.getPower() <= 60) {
            ivPower.setImageResource(R.drawable.philips_home_icon_battery_low2);
        } else if (data.getPower() > 60 && data.getPower() <= 90) {
            ivPower.setImageResource(R.drawable.philips_home_icon_battery_low1);
        } else {
            ivPower.setImageResource(R.drawable.philips_home_icon_battery_full);
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

        tvDeviceName.setText(StringUtil.processEmptyString(data.getDeviceName()));
        if (data.getLastRecordDetail() != null) {
            BleUtil.setTextViewOperationRecordByType(tvLastRecord, data.getLastRecordDetail());
            String lastRecord = DateUtils.secondToDate2(data.getLastRecordDetail().getCreateTime())
                    + " " + tvLastRecord.getText().toString().trim();
            tvLastRecord.setText(lastRecord);
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
