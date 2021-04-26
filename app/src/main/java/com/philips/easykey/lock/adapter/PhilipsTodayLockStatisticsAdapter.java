package com.philips.easykey.lock.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

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
        TextView tvType = baseViewHolder.getView(R.id.tv_type);
        TextView tvBar = baseViewHolder.getView(R.id.tv_bar);
        tvType.setText(todayLockStatisticsBean.getStatisticsType());
        tvBar.setText(todayLockStatisticsBean.getStatisticsCount()+"");
    }
}
