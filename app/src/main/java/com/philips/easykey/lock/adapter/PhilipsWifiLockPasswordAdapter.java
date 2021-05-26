package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;
import com.philips.easykey.lock.utils.DateUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by David on
 */


public class PhilipsWifiLockPasswordAdapter extends BaseQuickAdapter<ForeverPassword, BaseViewHolder> {

    public PhilipsWifiLockPasswordAdapter(@Nullable List<ForeverPassword> data, int layoutId) {
        super(layoutId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ForeverPassword bean) {

        int num = Integer.parseInt(bean.getNum());
        helper.setText(R.id.tv_num, num > 9 ? getContext().getString(R.string.password) + num : getContext().getString(R.string.password) + "0" + num);
        helper.setText(R.id.tv_nick, bean.getNickName());
        helper.setText(R.id.tv_time, DateUtils.getDayTimeFromMillisecond(bean.getCreateTime() * 1000));
    }


}
