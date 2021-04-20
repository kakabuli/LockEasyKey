package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;

import java.util.List;

/**
 * Created by David on
 */


public class GatewayStressPasswordAdapter extends BaseQuickAdapter<ForeverPassword, BaseViewHolder> {

    public GatewayStressPasswordAdapter(@Nullable List<ForeverPassword> data, int layoutId) {
        super(layoutId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ForeverPassword bean) {
        List<ForeverPassword> data = getData();
        int itemCount=data.size();
//        int itemCount = getItemCount();
        int pos=helper.getPosition();
        if (pos==itemCount-1){
            View view= helper.getView(R.id.my_view);
            view.setVisibility(View.GONE);
        }else {
            View view= helper.getView(R.id.my_view);
            view.setVisibility(View.VISIBLE);
        }
        helper.setText(R.id.tv_nick, bean.getNickName());
    }
}
