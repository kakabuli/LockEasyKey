package com.philips.easykey.lock.activity.device.gatewaylock.password;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;

import com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter.GatewayLockPasswordSharePresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.IGatewayLockPasswordShareView;
import com.philips.easykey.lock.publiclibrary.ble.BleCommandFactory;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SharedUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayPasswordPlanBean;

import java.util.Arrays;
import java.util.Map;


public class GatewayLockPasswordShareActivity extends BaseActivity<IGatewayLockPasswordShareView,
        GatewayLockPasswordSharePresenter<IGatewayLockPasswordShareView>>
        implements IGatewayLockPasswordShareView {

    ImageView back;
    TextView tvNumber;
    Button btnDelete;
    TextView tvShortMessage;
    TextView tvWeiXin;
    TextView tvCopy;
    TextView tvPwdType;

    private String gatewayId;
    private String deviceId;
    private String pwdValue;
    private AlertDialog alertDialog;
    private GatewayPasswordPlanBean gatewayPasswordPlanBean;
    private String[] weekdays;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_lock_password_share);

        back = findViewById(R.id.back);
        tvNumber = findViewById(R.id.tv_number);
        btnDelete = findViewById(R.id.btn_delete);
        tvShortMessage = findViewById(R.id.tv_short_message);
        tvWeiXin = findViewById(R.id.tv_wei_xin);
        tvCopy = findViewById(R.id.tv_copy);
        tvPwdType = findViewById(R.id.tv_pwd_type);

        back.setOnClickListener(v -> {
            Intent managerIntent = new Intent(GatewayLockPasswordShareActivity.this, GatewayPasswordManagerActivity.class);
            startActivity(managerIntent);
        });
        btnDelete.setOnClickListener(v -> {
            mPresenter.deletePassword(gatewayId, deviceId, gatewayPasswordPlanBean.getPasswordNumber());
            alertDialog = AlertDialogUtil.getInstance().noButtonDialog(this, getString(R.string.delete_be_being));
            alertDialog.setCancelable(false);
        });
        tvShortMessage.setOnClickListener(v -> {
            String message = String.format(getString(R.string.share_content), pwdValue, tvPwdType.getText().toString().trim(),getString(R.string.app_name));
            SharedUtil.getInstance().sendShortMessage(message, this);
        });
        tvWeiXin.setOnClickListener(v -> {
            if (SharedUtil.isWeixinAvilible(this)) {
                String message = String.format(getString(R.string.share_content), pwdValue, tvPwdType.getText().toString().trim(),getString(R.string.app_name));
                SharedUtil.getInstance().sendWeiXin(message);
            } else {
                ToastUtils.showShort(R.string.telephone_not_install_wechat);
            }
        });
        tvCopy.setOnClickListener(v -> {
            String message = String.format(getString(R.string.share_content), pwdValue, tvPwdType.getText().toString().trim(),getString(R.string.app_name));
            SharedUtil.getInstance().copyTextToSystem(this, message);
        });

        initData();

    }

    @Override
    protected GatewayLockPasswordSharePresenter<IGatewayLockPasswordShareView> createPresent() {
        return new GatewayLockPasswordSharePresenter<>();
    }


    private void initData() {
        Intent intent = getIntent();
        gatewayId = intent.getStringExtra(KeyConstants.GATEWAY_ID);
        deviceId = intent.getStringExtra(KeyConstants.DEVICE_ID);
        pwdValue = intent.getStringExtra(KeyConstants.PWD_VALUE);
        gatewayPasswordPlanBean = (GatewayPasswordPlanBean) intent.getSerializableExtra(KeyConstants.GATEWAY_PASSWORD_BEAN);

        if (!TextUtils.isEmpty(pwdValue)) {
            String value = StringUtil.getFileAddSpace(pwdValue);
            tvNumber.setText(value);
        }

        String notice = "";
        int num = gatewayPasswordPlanBean.getPasswordNumber();
        if (num > 4 && num < 9) {  //临时密码
            notice = getString(R.string.permanent_once_validity);
        } else if (num == 9) { //胁迫密码
            notice = getString(R.string.stress_password);
        } else {
            int userType = gatewayPasswordPlanBean.getUserType(); //userType: 0 永久性密钥   userType: 1 策略密钥   userType: 3 管理员密钥   userType: 4 无权限密钥
            if (userType == 0) {
                notice = getString(R.string.permanent_validity);
            } else {  //策略密钥

                String planType = gatewayPasswordPlanBean.getPlanType();
                if ("year".equals(planType)) {
                    long startTime = gatewayPasswordPlanBean.getZigBeeLocalStartTime();
                    long endTime = gatewayPasswordPlanBean.getZigBeeLocalEndTime();
                    LogUtils.d("  startTime  " + startTime + "  endTime  " + endTime);
                    long startSeconds = startTime + BleCommandFactory.defineTime;
                    String start = DateUtils.getDateTimeFromMillisecond(startSeconds * 1000);//要上传的开锁时间
                    long endSeconds = endTime + BleCommandFactory.defineTime;
                    String end = DateUtils.getDateTimeFromMillisecond(endSeconds * 1000);//要上传的开锁时间
                    notice = getString(R.string.password_valid_shi_xiao) + "  " + start + "~" + end;
                    LogUtils.d("显示的内容是   " + notice);
//                    if (endTime - startTime == 24 * 60 * 60) {
//                        notice =  getString(R.string.password_one_day_valid);
//                    }
                } else if ("week".equals(planType)) {
                    if (weekdays == null) {
                        weekdays = new String[]{getString(R.string.week_day),
                                getString(R.string.monday),
                                getString(R.string.tuesday),
                                getString(R.string.wedensday),
                                getString(R.string.thursday),
                                getString(R.string.friday),
                                getString(R.string.saturday)};
                    }
                    int mask = gatewayPasswordPlanBean.getDaysMask();
                    int[] days = new int[7];
                    String weeks = BleUtil.getStringByMask(mask, weekdays);
                    notice = String.format(getString(R.string.week_hint), weeks,
                            gatewayPasswordPlanBean.getStartHour() + ":" + gatewayPasswordPlanBean.getStartMinute(),
                            gatewayPasswordPlanBean.getEndHour() + ":" + gatewayPasswordPlanBean.getEndMinute());
                    LogUtils.d("重复的天数是   " + Arrays.toString(days));
                } else {
                    notice = getString(R.string.permanent_validity);
                }
            }
        }
        tvPwdType.setText(notice);
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isCosumenBackKey();
        }
        return false;
    }

    private boolean isCosumenBackKey() {
        Intent managerIntent = new Intent(GatewayLockPasswordShareActivity.this, GatewayPasswordManagerActivity.class);
        startActivity(managerIntent);
        return true;
    }

    @Override
    public void getLockInfoSuccess(int maxPwd) {

    }

    @Override
    public void getLockInfoFail() {

    }

    @Override
    public void getLockInfoThrowable(Throwable throwable) {

    }

    @Override
    public void syncPasswordComplete(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {

    }

    @Override
    public void onLoadPasswordPlan(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {

    }

    @Override
    public void onLoadPasswordPlanFailed(Throwable throwable) {

    }

    @Override
    public void onLoadPasswordPlanComplete(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {

    }

    @Override
    public void syncPasswordFailed(Throwable throwable) {

    }

    @Override
    public void addLockPwdFail(Throwable throwable) {

    }

    @Override
    public void addLockPwdSuccess(GatewayPasswordPlanBean gatewayPasswordPlanBean, String pwdValue) {

    }

    @Override
    public void setUserTypeSuccess(String passwordValue, GatewayPasswordPlanBean gatewayPasswordPlanBean) {

    }


    @Override
    public void setUserTypeFailed(Throwable typeFailed) {

    }

    @Override
    public void setPlanSuccess(String passwordValue, GatewayPasswordPlanBean gatewayPasswordPlanBean) {

    }

    @Override
    public void setPlanFailed(Throwable throwable) {

    }

    @Override
    public void deletePasswordSuccess() {
        //删除锁密码成功
        alertDialog.dismiss();
        Intent intent = new Intent(this, GatewayPasswordManagerActivity.class);
        startActivity(intent);
    }

    @Override
    public void deletePasswordFailed(Throwable throwable) {
        //删除失败
        alertDialog.dismiss();
        AlertDialogUtil.getInstance().noButtonDialog(this, getString(R.string.delete_fialed));
    }

    @Override
    public void gatewayPasswordFull() {

    }
}