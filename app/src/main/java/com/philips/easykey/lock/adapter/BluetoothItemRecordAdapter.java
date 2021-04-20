package com.philips.easykey.lock.adapter;

import androidx.annotation.Nullable;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.BluetoothItemRecordBean;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;

import java.util.List;

/**
 * Created by David on 2019/2/16
 */


public class BluetoothItemRecordAdapter extends BaseQuickAdapter<BluetoothItemRecordBean, BaseViewHolder> {
    public BluetoothItemRecordAdapter(int layoutResId, @Nullable List<BluetoothItemRecordBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, BluetoothItemRecordBean bean) {
        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText(bean.getOpen_time());
        helper.getView(R.id.view_top).setVisibility(bean.isFirstData() == true ? View.INVISIBLE : View.VISIBLE);
        helper.getView(R.id.view_bottom).setVisibility(bean.isLastData() == true ? View.INVISIBLE : View.VISIBLE);
        ImageView iv = helper.getView(R.id.iv);
        String imgIcon = bean.getIconImg();
        if (KeyConstants.BLUETOOTH_RECORD_COMMON.equals(imgIcon)) {
            iv.setImageResource(R.mipmap.bluetooth_common_icon);
        } else if (KeyConstants.BLUETOOTH_RECORD_WARN.equals(imgIcon)) {
            iv.setImageResource(R.mipmap.bluetooth_warn_icon);
        }
        TextView tvContent = helper.getView(R.id.tv_content);
        tvContent.setText(bean.getNickName());
        TextView tvRight = helper.getView(R.id.tv_right);
        String strRight = bean.getStrRight();
        LogUtils.d("Adapter 显示的   " + strRight);
        switch (strRight) {
            case BleUtil.PASSWORD:// "密码"  break;
                strRight = tvRight.getContext().getString(R.string.password_open);
                break;
            case BleUtil.RF:// "遥控";  break;
                strRight = tvRight.getContext().getString(R.string.rf_open);
                break;
            case BleUtil.MANUAL:// = "手动";  break;
                strRight = tvRight.getContext().getString(R.string.manual_open);
                break;
            case BleUtil.RFID://= "卡片";  break;
                strRight = tvRight.getContext().getString(R.string.rfid_open);
                break;
            case BleUtil.FINGERPRINT://= "指纹";  break;
                strRight = tvRight.getContext().getString(R.string.fingerprint_open);
                break;
            case BleUtil.VOICE://= "语音";  break;
                strRight = tvRight.getContext().getString(R.string.voice_open);
                break;
            case BleUtil.FINGER_VEIN:// = "静脉";  break;
                strRight = tvRight.getContext().getString(R.string.finger_vein_open);
                break;
            case BleUtil.FACE_RECOGNITION:// = "人脸";  break;
                strRight = tvRight.getContext().getString(R.string.face_recognition_open);
                break;
            case BleUtil.PHONE://= "手机";  break;
                strRight = tvRight.getContext().getString(R.string.app_open);
                break;
            case BleUtil.ONE_KEY_OPEN:
                strRight = tvRight.getContext().getString(R.string.one_key_open);
                break;
            case BleUtil.UNKNOWN_OPEN:
                strRight = tvRight.getContext().getString(R.string.unknown_open);
                break;
        }
        // 机械开锁/APP开锁/自动开锁/密码开锁/门卡开锁/指纹开锁
        if (!TextUtils.isEmpty(strRight)) {
            tvRight.setText(strRight);
        }
    }


}
