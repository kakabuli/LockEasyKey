package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;
import com.blankj.utilcode.util.LogUtils;

import java.util.List;

/**
 * Created by David on 2019/2/16
 */


public class PhilipsWifiLockShareUserAdapter extends BaseQuickAdapter<WifiLockShareResult.WifiLockShareUser, BaseViewHolder> {


    public PhilipsWifiLockShareUserAdapter(@Nullable List<WifiLockShareResult.WifiLockShareUser> data, int layoutId) {
        super(layoutId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, WifiLockShareResult.WifiLockShareUser bean) {
        helper.setText(R.id.tv_num, bean.getUserNickname());
    }


}
