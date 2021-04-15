package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;
import com.philips.easykey.lock.utils.LogUtils;

import java.util.List;

/**
 * Created by David on 2019/2/16
 */


public class WifiLockShareUserAdapter extends BaseQuickAdapter<WifiLockShareResult.WifiLockShareUser, BaseViewHolder> {


    public WifiLockShareUserAdapter(@Nullable List<WifiLockShareResult.WifiLockShareUser> data, int layoutId) {
        super(layoutId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, WifiLockShareResult.WifiLockShareUser bean) {
        int position = helper.getPosition()+1;
        String number="";
        if (position<10){
            number="0"+position;
        }else {
            number=""+position;
        }
        helper.setText(R.id.tv_serial_number, number);
        helper.setText(R.id.tv_num, bean.getUserNickname());
        LogUtils.d("davi getData().size() "+getData().size()+"  position  "+position);
        if( getData().size()==position){
            helper.getView(R.id.my_view).setVisibility(View.GONE);
        }else {
            helper.getView(R.id.my_view).setVisibility(View.VISIBLE);
        }
    }


}
