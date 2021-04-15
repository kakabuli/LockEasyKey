package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.deviceAdd.AddSelectDeviceAddItemBean;

import java.util.List;

public class DeviceAddSelectItemAdapter extends BaseQuickAdapter<AddSelectDeviceAddItemBean, BaseViewHolder> {

    public DeviceAddSelectItemAdapter(@Nullable List<AddSelectDeviceAddItemBean> data) {
        super(R.layout.device_add_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AddSelectDeviceAddItemBean item) {
        helper.setImageResource(R.id.device_type_image,item.getImageId());
        helper.setText(R.id.device_type_text,item.getTitle());
    }
}
