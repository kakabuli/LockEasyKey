package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.WiFiLockCardAndFingerShowBean;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.DateUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.List;

/**
 * Created by David
 */


public class PhilipsWifiLockCardAndFingerAdapter extends BaseQuickAdapter<WiFiLockCardAndFingerShowBean, BaseViewHolder> {


    public PhilipsWifiLockCardAndFingerAdapter(@Nullable List<WiFiLockCardAndFingerShowBean> data, int layoutId) {
        super(layoutId, data);
        LogUtils.d("初始化了    "+data.size());
    }

    @Override
    protected void convert(BaseViewHolder helper, WiFiLockCardAndFingerShowBean bean) {

        int num = bean.getNum();
        if(bean.getTpye() == BleLockUtils.TYPE_FINGER){

            helper.setText(R.id.tv_num, num > 9 ? getContext().getResources().getString(R.string.finger) + num : getContext().getResources().getString(R.string.finger) + "0" + num);
        }else if(bean.getTpye() == BleLockUtils.TYPE_CARD){
            helper.setText(R.id.tv_num, num > 9 ? getContext().getResources().getString(R.string.card) + num : getContext().getResources().getString(R.string.card) + "0" + num);
        }
        helper.setText(R.id.tv_nick, bean.getNickName());
        helper.setText(R.id.tv_time, DateUtils.getDayTimeFromMillisecond(bean.getCreateTime() * 1000));
    }


}
