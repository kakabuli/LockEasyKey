package com.philips.easykey.lock.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.inf.OnBindClickListener;
import com.philips.easykey.lock.bean.BluetoothLockBroadcastBean;
import com.philips.easykey.lock.bean.BluetoothLockBroadcastListBean;
import com.philips.easykey.lock.utils.Rsa;

import java.util.ArrayList;
import java.util.List;

public class DeviceSearchAdapter extends BaseQuickAdapter<BluetoothDevice, BaseViewHolder> {

    private OnBindClickListener bindClickListener;
    BluetoothLockBroadcastBean mBroadcastBean = new BluetoothLockBroadcastBean();
    List<BluetoothLockBroadcastListBean> mBroadcastList = new ArrayList<>();
    List<BluetoothLockBroadcastBean> mItemList = new ArrayList<>();

    public void setBluetoothLockBroadcast(List<BluetoothLockBroadcastListBean> broadcastList) {
        this.mBroadcastList = broadcastList;
    }

    public void setBindClickListener(OnBindClickListener bindClickListener) {
        this.bindClickListener = bindClickListener;
    }

    public DeviceSearchAdapter(@Nullable List<BluetoothDevice> data) {
        super(R.layout.device_bluetooth_search_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BluetoothDevice item) {
        helper.setText(R.id.device_bluetooth_name,item.getName());
        helper.setVisible(R.id.device_bluetooth_sn,false);

        helper.setOnClickListener(R.id.go_bind, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 bindClickListener.onItemClickListener(v,helper.getPosition(),item);
            }
        });
    }
}
