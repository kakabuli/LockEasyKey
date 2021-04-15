package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.GatewaySettingItemBean;

import java.util.List;

public class GatewaySettingAdapter extends BaseQuickAdapter<GatewaySettingItemBean, BaseViewHolder> {

    public GatewaySettingAdapter(@Nullable List<GatewaySettingItemBean> data) {
        super(R.layout.activity_gateway_setting_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GatewaySettingItemBean item) {
            helper.setText(R.id.title, item.getTitle());
            helper.setText(R.id.content, item.getContent());
            ImageView imageView = helper.getView(R.id.right);
            if (item.isSetting()) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
    }
}
