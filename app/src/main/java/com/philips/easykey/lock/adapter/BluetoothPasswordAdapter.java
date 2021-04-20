package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.LogUtils;


import java.util.Arrays;
import java.util.List;

/**
 * Created by David on
 */


public class BluetoothPasswordAdapter extends BaseQuickAdapter<ForeverPassword, BaseViewHolder> {

    private String[] weekdays;

    public BluetoothPasswordAdapter(@Nullable List<ForeverPassword> data, int layoutId) {
        super(layoutId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ForeverPassword bean) {
        List<ForeverPassword> data = getData();
        int itemCount=data.size();
        int pos=helper.getPosition();
        LogUtils.d(" itemCount "+itemCount+"----------pos "+pos);
        View view= helper.getView(R.id.my_view);
        if (pos==itemCount-1){
            view.setVisibility(View.GONE);
        }else {
            view.setVisibility(View.VISIBLE);
        }

        if (bean.getItems() != null) {
            LogUtils.d("周计划是     " + Arrays.toString(bean.getItems().toArray()));
        }

        weekdays = view.getContext().getResources().getStringArray(R.array.bluetooth_password_adapter_week);
        Context mContext = view.getContext();

        helper.setText(R.id.tv_num, bean.getNum());
        helper.setText(R.id.tv_nick, bean.getNickName());
        //1永久 2时间段 3周期 4 24小时 5 周期
        if (bean.getType() == 1) {
            helper.setText(R.id.tv_time, MyApplication.getInstance().getString(R.string.foever_able));
        } else if (bean.getType() == 2) {
            helper.setText(R.id.tv_time, DateUtils.getDateTimeFromMillisecond(bean.getStartTime()) + "-" + DateUtils.getDateTimeFromMillisecond(bean.getEndTime()));
        } else if (bean.getType() == 3) {  //周期密码
            String weeks = "";
            boolean isFirst = false;
            for (int i = 0; i < bean.getItems().size(); i++) {
                if ("1".equals(bean.getItems().get(i))) {
                    if (isFirst) {
                        weeks += "、" + weekdays[i];
                    } else {
                        weeks += weekdays[i];
                        isFirst = true;
                    }

                }
            }
            String startTime = getStartTime(bean);
            String endTime = getEndTime(bean);
            weeks = mContext.getString(R.string.pwd_will) + weeks + startTime + "-" + endTime + mContext.getString(R.string.force);
            helper.setText(R.id.tv_time, weeks);
        } else if (bean.getType() == 4) {
            helper.setText(R.id.tv_time, DateUtils.getDateTimeFromMillisecond(bean.getStartTime()) + "-" + DateUtils.getDateTimeFromMillisecond(bean.getEndTime()));
        }else if (bean.getType()==5){
            helper.setText(R.id.tv_time, MyApplication.getInstance().getString(R.string.temporary_password_used_once));
        }
        if ("09".equals(bean.getNum())){
            helper.setText(R.id.tv_time, MyApplication.getInstance().getString(R.string.stress_password));
        }

    }

    private String getEndTime(ForeverPassword bean) {
        int endHour = (int) bean.getEndTime() / 60 / 60 / 1000;
        int endMinute = (int) bean.getEndTime() % (60 * 60 * 1000) / 60 / 1000;
        String endTime = (endHour > 9 ? "" + endHour : "0" + endHour) + ":" + (endMinute > 9 ? "" + endMinute : "0" + endMinute);
        return endTime;
    }

    private String getStartTime(ForeverPassword bean) {
        int startHour = (int) bean.getStartTime() / 60 / 60 / 1000;
        int startMinute = (int) bean.getStartTime() % (60 * 60 * 1000) / 60 / 1000;
        String startTime = (startHour > 9 ? "" + startHour : "0" + startHour) + ":" + (startMinute > 9 ? "" + startMinute : "0" + startMinute);
        return startTime;
    }
}
