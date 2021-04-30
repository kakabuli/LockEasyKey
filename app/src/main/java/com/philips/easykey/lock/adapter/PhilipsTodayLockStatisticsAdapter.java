package com.philips.easykey.lock.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.TodayLockStatisticsBean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PhilipsTodayLockStatisticsAdapter extends BaseQuickAdapter<TodayLockStatisticsBean, BaseViewHolder> {

    public PhilipsTodayLockStatisticsAdapter(@Nullable List<TodayLockStatisticsBean> data) {
        super(R.layout.philips_item_lock_statistics, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, TodayLockStatisticsBean todayLockStatisticsBean) {
        ImageView ivType = baseViewHolder.getView(R.id.iv_type);
        TextView tvType = baseViewHolder.getView(R.id.tv_type);
        TextView tvBar = baseViewHolder.getView(R.id.tv_bar);
        switch (todayLockStatisticsBean.getStatisticsType()){
            case 1:
                tvType.setText("访客记录");
                ivType.setImageDrawable(getContext().getDrawable(R.drawable.philips_message_icon_visitors));
                break;
            case 2:
                tvType.setText("指纹开门");
                ivType.setImageDrawable(getContext().getDrawable(R.drawable.philips_message_icon_fingerprint));
                break;
            case 3:
                tvType.setText("密码开门");
                ivType.setImageDrawable(getContext().getDrawable(R.drawable.philips_message_icon_password));
                break;
            case 4:
                tvType.setText("开片");
                ivType.setImageDrawable(getContext().getDrawable(R.drawable.philips_message_icon_card));
                break;
        }
        tvBar.setText(todayLockStatisticsBean.getStatisticsCount()+"");
    }
}
