package com.philips.easykey.lock.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.BluetoothItemRecordBean;
import com.philips.easykey.lock.bean.BluetoothRecordBean;
import com.philips.easykey.lock.publiclibrary.ble.bean.OpenLockRecord;
import com.philips.easykey.lock.publiclibrary.http.result.FamilyMemberBean;
import com.philips.easykey.lock.utils.ftp.GeTui;


import org.linphone.mediastream.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 2019/2/16
 */


public class BluetoothRecordAdapter extends BaseQuickAdapter<BluetoothRecordBean, BaseViewHolder> {


    public BluetoothRecordAdapter(@Nullable List<BluetoothRecordBean> data) {
        super(R.layout.item_bluetooth_record, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, BluetoothRecordBean bean) {
       TextView tvTitle= helper.getView(R.id.tv_title);
        String time = bean.getTime();
        if (!TextUtils.isEmpty(time)){
            tvTitle.setText(time);
            tvTitle.setVisibility(View.VISIBLE);
        }else {
            tvTitle.setVisibility(View.GONE);
        }
        RecyclerView recyclerView = helper.getView(R.id.item_recycleview);
        List<BluetoothItemRecordBean> data = bean.getList();
       // Log.e(GeTui.VideoLog,"数据是.....:"+data);
//        data.clear();
        if(data != null && data.size() > 0){

            BluetoothItemRecordAdapter bluetoothItemRecordAdapter = new BluetoothItemRecordAdapter(R.layout.item_item_bluetooth_record,data);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(bluetoothItemRecordAdapter);
        }
    /*    if (bluetoothItemRecordAdapter!=null){
            List<BluetoothItemRecordBean> dataList = bean.getList();
            data.addAll(dataList);
            bluetoothItemRecordAdapter.notifyDataSetChanged();
        }*/
        helper.getView(R.id.view_line).setVisibility(bean.isLastData() == true ? View.INVISIBLE : View.VISIBLE);
    }


}
