package com.philips.easykey.lock.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * author :
 * time   : 2021/4/22
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsVpHomeDevicesAdapter extends PhilipsBaseVPAdapter<PhilipsDeviceBean> {

    private OnClickMoreListener mOnClickMoreListener;

    public PhilipsVpHomeDevicesAdapter(Context context, int convertId, List<PhilipsDeviceBean> dataList) {
        super(context, convertId, dataList);
    }

    @Override
    public void bindView(View view, PhilipsDeviceBean data) {
        if(data == null) return;
        TextView tvDeviceName = view.findViewById(R.id.tvDeviceName);
        TextView tvLastRecord = view.findViewById(R.id.tvLastRecord);

        view.findViewById(R.id.ivMore).setOnClickListener(v -> {
            if(mOnClickMoreListener != null) {
                mOnClickMoreListener.onClick(v, data);
            }
        });
        tvDeviceName.setText(StringUtil.processEmptyString(data.getDeviceName()));
        tvLastRecord.setText(StringUtils
                .getString(R.string.philips_last_record,
                        TimeUtils.millis2String(data.getLastRecordTime(), TimeUtils.getSafeDateFormat("yyyy-MM-dd HH:mm")),
                        StringUtil.processEmptyString(data.getLastRecordDetail())));

    }

    public void setOnClickMoreListener(OnClickMoreListener onClickMoreListener) {
        mOnClickMoreListener = onClickMoreListener;
    }

    public interface OnClickMoreListener {
        void onClick(View v, @NotNull PhilipsDeviceBean data);
    }

}
