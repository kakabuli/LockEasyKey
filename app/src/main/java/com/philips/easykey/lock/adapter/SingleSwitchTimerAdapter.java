package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.SingleSwitchTimerShowBean;
import com.blankj.utilcode.util.LogUtils;

import java.util.List;

/**
 * Created by David on 2019/2/16
 */


public class SingleSwitchTimerAdapter extends BaseQuickAdapter<SingleSwitchTimerShowBean, BaseViewHolder> {


    public SingleSwitchTimerAdapter(@Nullable List<SingleSwitchTimerShowBean> data) {
        super(R.layout.item_single_switch_timer_layout, data);
        LogUtils.d("数据是  " + data.toString());
    }

    @Override
    protected void convert(BaseViewHolder helper, SingleSwitchTimerShowBean bean) {
        ImageView waitIcon =   helper.getView(R.id.iv_wait);
        ImageView switchIcon =   helper.getView(R.id.iv_switch);
        TextView time =   helper.getView(R.id.tv_time);
        TextView repeat =   helper.getView(R.id.tv_repeat);
        TextView action =   helper.getView(R.id.tv_action);
        time.setText(bean.getTime());
        action.setText(bean.getAction());
        repeat.setText(bean.getRepeat());
        if (bean.isOpen()){
            waitIcon.setImageResource(R.mipmap.single_switch_timer_wait_blue );
            switchIcon.setImageResource(R.mipmap.single_switch_timer_switch_blue);
        }else {
            waitIcon.setImageResource(R.mipmap.single_switch_timer_wait_gray );
            switchIcon.setImageResource(R.mipmap.single_switch_timer_switch_gray);
        }
    }
}
