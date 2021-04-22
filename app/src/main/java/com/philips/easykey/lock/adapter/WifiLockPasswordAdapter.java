package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;
import com.philips.easykey.lock.utils.LogUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by David on
 */


public class WifiLockPasswordAdapter extends BaseQuickAdapter<ForeverPassword, BaseViewHolder> {

    public WifiLockPasswordAdapter(@Nullable List<ForeverPassword> data, int layoutId) {
        super(layoutId, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, ForeverPassword bean) {
        List<ForeverPassword> data = getData();
        int itemCount = data.size();
        int pos = helper.getPosition();
        LogUtils.d(" itemCount " + itemCount + "----------pos " + pos);
        View view = helper.getView(R.id.my_view);
        if (pos == itemCount - 1) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }

        if (bean.getItems() != null) {
            LogUtils.d("周计划是     " + Arrays.toString(bean.getItems().toArray()));
        }

        helper.setText(R.id.tv_num, bean.getNum());
        helper.setText(R.id.tv_nick, bean.getNickName());
        //0永久秘钥 1策略秘钥 2胁迫秘钥 3管理员秘钥 4无权限秘钥 254 一次性秘钥 255 无效值
        switch (bean.getType()) {
            case 0:
                helper.setText(R.id.tv_time, view.getContext().getString(R.string.foever_able));
                break;
            case 1:
                helper.setText(R.id.tv_time, view.getContext().getString(R.string.philips_policy_password));
                break;
            case 2:
                helper.setText(R.id.tv_time, view.getContext().getString(R.string.stress_password));
                break;
            case 3:
                helper.setText(R.id.tv_time, view.getContext().getString(R.string.philips_admin_password));
                break;
            case 4:
                helper.setText(R.id.tv_time, view.getContext().getString(R.string.philips_without_permission_password));
                break;
            case 254:
                helper.setText(R.id.tv_time, view.getContext().getString(R.string.wifi_lock_temp_password));
                break;
            case 255:
                helper.setText(R.id.tv_time, view.getContext().getString(R.string.philips_invalid_value_password));
                break;
        }
    }


}
