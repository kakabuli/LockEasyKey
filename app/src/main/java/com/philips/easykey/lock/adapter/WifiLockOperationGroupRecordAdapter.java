package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.WifiLockOperationRecordGroup;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockOperationRecord;
import com.philips.easykey.lock.utils.DateUtils;

import java.util.List;

/**
 * Created by David on 2019/2/16
 */


public class WifiLockOperationGroupRecordAdapter extends BaseQuickAdapter<WifiLockOperationRecordGroup, BaseViewHolder> {

    public interface onDataMoreListener{
        void onClickMore();
    }

    private onDataMoreListener mOnDataMoreListener = null;

    public void setOnDataMoreListener(onDataMoreListener listener){
        this.mOnDataMoreListener = listener;
    }

    public WifiLockOperationGroupRecordAdapter(@Nullable List<WifiLockOperationRecordGroup> data) {
        super(R.layout.item_bluetooth_operation_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiLockOperationRecordGroup bean) {
        TextView tvTitle = helper.getView(R.id.tv_title);
        TextView textView = helper.getView(R.id.tv_more);
        String time = bean.getTime();
        if (!TextUtils.isEmpty(time)) {
            if (time.equals(DateUtils.getCurrentYMD())){
                time = getContext().getString(R.string.today);
            }
            tvTitle.setText(time);
            tvTitle.setVisibility(View.VISIBLE);
            if(helper.getLayoutPosition() == 0)
                textView.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
        }
        RecyclerView recyclerView = helper.getView(R.id.item_recycleview);
        List<WifiLockOperationRecord> list = bean.getList();
        WifiLockOperationItemRecordAdapter bluetoothItemRecordAdapter = new WifiLockOperationItemRecordAdapter(R.layout.item_wifi_lock_operation_record,list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(bluetoothItemRecordAdapter);
        helper.getView(R.id.view_line).setVisibility(helper.getPosition() == getData().size()-1 ? View.INVISIBLE : View.VISIBLE);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnDataMoreListener != null){
                    mOnDataMoreListener.onClickMore();
                }
            }
        });
    }


}
