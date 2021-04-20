package com.philips.easykey.lock.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.bean.SevendayDataStatisticsBean;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.widget.CurveChart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DeviceSelectAdapter extends BaseQuickAdapter<HomeShowBean, BaseViewHolder> {

    private DeviceSelectCallBackLinstener mListener;

    public interface DeviceSelectCallBackLinstener{
        void onDeviceSelectCallBackLinstener(HomeShowBean homeShowBean);
    }

    public DeviceSelectAdapter(@Nullable List<HomeShowBean> data,DeviceSelectCallBackLinstener linstener) {
        super(R.layout.item_device_select, data);
        this.mListener = linstener;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, HomeShowBean homeShowBean) {
        TextView tvDeviceName = baseViewHolder.getView(R.id.tv_device_name);
        tvDeviceName.setText(homeShowBean.getDeviceNickName());
        tvDeviceName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDeviceSelectCallBackLinstener(homeShowBean);
            }
        });
    }
}
