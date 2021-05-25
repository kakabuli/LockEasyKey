package com.philips.easykey.lock.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.bean.PhilipsDuressBean;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

/**
 * author :
 * time   : 2021/4/22
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsDuressAlarmAdapter extends BaseQuickAdapter<PhilipsDuressBean, BaseViewHolder> {

    private OnClickDuressNotificationListener mOnClickDuressNotificationListener;

    public PhilipsDuressAlarmAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, PhilipsDuressBean bean) {
        if(bean == null) return;

        if(bean.isHead()){
            if(bean.getType() == 1){
                holder.setText(R.id.tv_password,R.string.password);
            }else if(bean.getType() == 2){
                holder.setText(R.id.tv_password,R.string.fingerprint);
            }
            holder.findView(R.id.tv_password).setVisibility(View.VISIBLE);
            holder.findView(R.id.tv_password_num).setVisibility(View.GONE);
            holder.findView(R.id.rl_duress_alarm_toggle).setVisibility(View.GONE);
            holder.findView(R.id.rl_duress_alarm_password_notification).setVisibility(View.GONE);
            return;
        }else{
            holder.findView(R.id.tv_password).setVisibility(View.GONE);
        }

        if(bean.getDuressToggle() == 0){
            holder.setText(R.id.tv_duress_alarm_toggle,R.string.close);
            holder.findView(R.id.rl_duress_alarm_password_notification).setVisibility(View.GONE);
        }else if(bean.getDuressToggle() == 1){
            holder.setText(R.id.tv_duress_alarm_toggle,R.string.open);
            holder.setText(R.id.tv_duress_alarm_phone,bean.getDuressPhone());
            holder.findView(R.id.rl_duress_alarm_password_notification).setVisibility(View.VISIBLE);
        }

        if(bean.getNickName() != null && !bean.getNickName().isEmpty()){
            holder.setText(R.id.tv_password_num,bean.getNickName().isEmpty() ? bean.getNum() : bean.getNickName());
        }else{
            holder.setText(R.id.tv_password_num,bean.getNum());
        }

        holder.findView(R.id.rl_duress_alarm_toggle).setOnClickListener(v -> {
            if(mOnClickDuressNotificationListener != null){
                mOnClickDuressNotificationListener.onClick(v,holder.getLayoutPosition(),bean);
            }
        });
    }

    public void setOnClickDuressNotificationListener(OnClickDuressNotificationListener onClickDuressNotificationListener) {
        mOnClickDuressNotificationListener = onClickDuressNotificationListener;
    }

    public interface OnClickDuressNotificationListener {
        void onClick(View v, int position, @NotNull PhilipsDuressBean data);
    }
}
