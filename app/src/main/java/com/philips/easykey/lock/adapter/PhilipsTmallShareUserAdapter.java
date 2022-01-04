package com.philips.easykey.lock.adapter;

import android.util.Log;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.http.result.TmallQueryDeviceListResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;

import java.util.List;

/**
 * Created by David on 2019/2/16
 */


public class PhilipsTmallShareUserAdapter extends BaseQuickAdapter<TmallQueryDeviceListResult.TmallQueryDeviceList, BaseViewHolder> {


    public PhilipsTmallShareUserAdapter(@Nullable List<TmallQueryDeviceListResult.TmallQueryDeviceList> data, int layoutId) {
        super(layoutId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, TmallQueryDeviceListResult.TmallQueryDeviceList bean) {
        helper.setText(R.id.tv_num, bean.getLockNickname());
    }


}
