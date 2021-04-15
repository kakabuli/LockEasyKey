package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.deviceAdd.AddZigbeeDetailItemBean;

import java.util.List;

public class ZigbeeDetailAdapter extends BaseQuickAdapter<AddZigbeeDetailItemBean, BaseViewHolder> {
    public ZigbeeDetailAdapter( @Nullable List<AddZigbeeDetailItemBean> data) {
        super(R.layout.device_zigbee_detail_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddZigbeeDetailItemBean item) {
        helper.setImageResource(R.id.zigbee_image,item.getImageId());
        helper.setText(R.id.zigbee_text,item.getText());
    }
}
