package com.philips.easykey.lock.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.SevendayDataStatisticsBean;
import com.philips.easykey.lock.widget.CurveChart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PhilipsSevenDayDataStatisticsAdapter extends BaseQuickAdapter<SevendayDataStatisticsBean, BaseViewHolder> {

    public PhilipsSevenDayDataStatisticsAdapter() {
        super(R.layout.philips_item_seven_day_data_statistics);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder baseViewHolder, SevendayDataStatisticsBean sevendayDataStatisticsBean) {
        TextView tvType = baseViewHolder.getView(R.id.tv_type);
        CurveChart curveChart = baseViewHolder.getView(R.id.curve_chart);
        tvType.setText(sevendayDataStatisticsBean.getStatisticsTypeName());
        curveChart.setMaxVlaue(max(sevendayDataStatisticsBean.getOrdinateValue()) + 40);
        curveChart.setMinValue(0);
        curveChart.setNumberLine(4);
        curveChart.setOrdinateValue(sevendayDataStatisticsBean.getOrdinateValue());
        curveChart.setTransverseValue(sevendayDataStatisticsBean.getTransverseValue());
        curveChart.setBorderTransverseLineWidth(0.3f);
        curveChart.setBrokenLineLTRB(42,20,15,22);
    }

    public static int max(int[] array) {
        int maximum = Integer.MIN_VALUE;

        for (int i = 0; i < array.length; i++) {
            if (maximum < array[i]) {
                maximum = array[i];
            }
        }
        return maximum;
    }
}
