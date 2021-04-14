package com.philips.easykey.lock.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockOperationRecord;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.LogUtils;

import java.util.List;

/**
 * Created by David on 2019/2/16
 */


public class WifiLockOperationItemRecordAdapter extends BaseQuickAdapter<WifiLockOperationRecord, BaseViewHolder> {
    public WifiLockOperationItemRecordAdapter(int layoutResId, @Nullable List<WifiLockOperationRecord> data) {
        super(layoutResId, data);
    }

    public WifiLockOperationItemRecordAdapter(@Nullable List<WifiLockOperationRecord> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WifiLockOperationRecord record) {
        int size = getData().size();
        int position = helper.getPosition();
        TextView tvTime = helper.getView(R.id.tv_time);
        long time = record.getTime();
        String s = DateUtils.currentLong2HourMin(time * 1000);
        tvTime.setText(TextUtils.isEmpty(s) ? "" : s);
        helper.getView(R.id.view_top).setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        helper.getView(R.id.view_bottom).setVisibility(position == size - 1 ? View.INVISIBLE : View.VISIBLE);


        TextView tvContent = helper.getView(R.id.tv_content);
        String content = BleUtil.getAlarmByType(record.getType(), mContext);
        TextView tvRight = helper.getView(R.id.tv_right);
        int type = record.getType();
        //1开锁 2关锁 3添加密钥 4删除密钥 5修改管理员密码 6自动模式 7手动模式 8安全模式切换 9常用模式切换 10反锁模式 11布防模式
        String left = "";
        String right = "";
        String shareUserNickname;
        int pwdType = record.getPwdType();
        String sNum = mContext.getString(R.string.number)+(record.getPwdNum() > 9 ? ""+ record.getPwdNum() : "0"+ record.getPwdNum());
        switch (type) {
            case 1: //开锁
                left = record.getUserNickname();
                if (TextUtils.isEmpty(left)) {
                    left = sNum;
                }
                switch (pwdType) {
                    //	密码类型：0密码 3卡片 4指纹 7面容 8APP用户 9机械方式开锁 10室内多功能键开锁（open） 11室内感应把手开锁
                    case 0:
                        right = mContext.getString(R.string.password_open);
                        if (record.getPwdNum() == 252) {
                            left = mContext.getString(R.string.temp_password_open_lock);
                            right = "";
                        }else if (record.getPwdNum() == 254){
                            left = mContext.getString(R.string.admin_password);
                            right = "";
                        }else if (record.getPwdNum() == 253){
                            left = mContext.getString(R.string.gust_password);
                            right = "";
                        }else if (record.getPwdNum() == 250){
                            left = mContext.getString(R.string.offline_password_open);
                            right = "";
                        }
                        break;
                    case 4:
                        right = mContext.getString(R.string.fingerprint_open);
                        break;
                    case 3:
                        right = mContext.getString(R.string.rfid_open);
                        break;
                    case 7:
                        right = mContext.getString(R.string.face_open);
                        break;
                    case 8:
                        right = mContext.getString(R.string.app_open);
                        break;
                    case 9:
//                        right = mContext.getString(R.string.machine_key);
//                        if(left.equals("编号00")){
//                            return;
//                        }
                        left = mContext.getString(R.string.machine_key);
                        right = "";
                        break;
                    case 10:
//                        right = mContext.getString(R.string.indoor_open);
//                        if(left.equals("编号00")){
//                            return;
//                        }
                        left = mContext.getString(R.string.indoor_open_new_express);
                        right = "";
                        break;
                    case 11:
//                        right = mContext.getString(R.string.indoor_Induction);
//                        if(left.equals("编号00")){
//                            return;
//                        }
                        left = mContext.getString(R.string.indoor_Induction);
                        right = "";
                        break;
                    default:
                        right = mContext.getString(R.string.unknown_open);
                        break;
                }
                break;
            case 2: //关锁
                left = mContext.getString(R.string.lock_already_lock);
                break;
            case 3: //添加秘钥
                left = mContext.getString(R.string.lock_add) + sNum;
                switch (pwdType) {
                    //	密码类型：0密码 3卡片 4指纹 7面容 8APP用户 9机械方式
                    case 0:
                        left = left + mContext.getString(R.string.password);
                        break;
                    case 3:
                        left = left + mContext.getString(R.string.card);
                        break;
                    case 4:
                        left = left + mContext.getString(R.string.fingerprint);
                        break;
                    case 7:
                        left = left + mContext.getString(R.string.face_open);
                        break;
                    default:
                        left = left + mContext.getString(R.string.unknown_open);
                        break;
                }
                break;
            case 4: //删除秘钥
                int pwdNum = record.getPwdNum();
                left = mContext.getString(R.string.lock_delete) + ((pwdNum == 255) ? mContext.getString(R.string.all) : sNum + "");
                switch (pwdType) {
                    //	密码类型：0密码 3卡片 4指纹 7面容 8APP用户 9机械方式
                    case 0:
                        left = left + mContext.getString(R.string.password);
                        break;
                    case 3:
                        left = left + mContext.getString(R.string.card);
                        break;
                    case 4:
                        left = left + mContext.getString(R.string.fingerprint);
                        break;
                    case 7:
                        left = left + mContext.getString(R.string.face_open);
                        break;
                    default:
                        left = mContext.getString(R.string.unknown_open);
                        break;
                }
                break;
            case 5: //修改管理员密码
                left = mContext.getString(R.string.wifi_lock_modify_admin_password);
                break;
            case 6: //自动模式
                left = mContext.getString(R.string.wifi_lock_auto_model);
                break;
            case 7: //手动模式
                left = mContext.getString(R.string.wifi_lock_hand_model);
                break;
            case 8: //安全模式切换
                left = mContext.getString(R.string.wifi_lock_safe_model);
                break;
            case 9: //常用模式切换
                left = mContext.getString(R.string.wifi_lock_commen_model);
                break;
            case 10: //反锁模式
                left = mContext.getString(R.string.wifi_lock_bacl_lock_model);
                break;
            case 11: //布防模式
                left = mContext.getString(R.string.wifi_lock_bufang_model);
                break;
            // 12修改密码昵称
            case 12:
                left = record.getUserNickname();
                right = mContext.getString(R.string.modify) + sNum;
                switch (pwdType) {
                    //	密码类型：0密码 3卡片 4指纹 7面容 8APP用户 9机械方式
                    case 0:
                        right = right + mContext.getString(R.string.password);
                        break;
                    case 3:
                        right = right + mContext.getString(R.string.card);
                        break;
                    case 4:
                        right = right + mContext.getString(R.string.fingerprint);
                        break;
                    case 7:
                        right = right + mContext.getString(R.string.face_open);
                        break;
                    default:
                        right = right + mContext.getString(R.string.unknown_open);
                        break;
                }
                right = right + mContext.getString(R.string.nick_for) + record.getPwdNickname();
                break;
            //13添加分享用户
            case 13:
                left = record.getUserNickname();
                shareUserNickname = record.getShareUserNickname();
                right = mContext.getString(R.string.wifi_add) + (!TextUtils.isEmpty(shareUserNickname) ? shareUserNickname : record.getShareAccount()) + mContext.getString(R.string.wifi_add2);
                break;
            //14删除分享用户
            case 14:
                left = record.getUserNickname();
                shareUserNickname = record.getShareUserNickname();
                right = mContext.getString(R.string.wifi_delete) + (!TextUtils.isEmpty(shareUserNickname) ? shareUserNickname : record.getShareAccount()) + mContext.getString(R.string.wifi_add2);
                break;
            case 15: //15修改管理指纹
                left = mContext.getString(R.string.lock_modify_admin_finger);
                break;
            case 16: //   16添加管理员指纹
                left = mContext.getString(R.string.lock_add_admin_finger);
                break;
            case 17: //   17开启节能模式
                left = mContext.getString(R.string.face_lock_power_Save_open);
                break;
            case 18: //   18关闭节能模式
                left = mContext.getString(R.string.face_lock_power_Save_close);
                break;
            default:
                left = mContext.getString(R.string.unknow_operation);
                break;
        }
        // 机械开锁/APP开锁/自动开锁/密码开锁/门卡开锁/指纹开锁
        tvContent.setText(left);
        if (TextUtils.isEmpty(right)){
            tvRight.setVisibility(View.GONE);
            tvRight.setText(right+"");
        }else {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(right+"");
        }

    }


}
