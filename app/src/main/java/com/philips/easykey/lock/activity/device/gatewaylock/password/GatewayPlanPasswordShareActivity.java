package com.philips.easykey.lock.activity.device.gatewaylock.password;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
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
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayPasswordPlanBean;

import java.util.Arrays;
import java.util.Map;


/**
 * Created by David on 2019/4/17
 */
public class GatewayPlanPasswordShareActivity extends BaseActivity<IGatewayLockPasswordShareView, GatewayLockPasswordSharePresenter<IGatewayLockPasswordShareView>>
        implements View.OnClickListener, IGatewayLockPasswordShareView {

    ImageView ivBack;
    TextView tvContent;
    TextView tvNumber;
    Button btnDelete;
    TextView tvShortMessage;
    TextView tvWeiXin;
    TextView tvCopy;
    ImageView ivEditor;
    TextView tvPassword;
    private String password;
    String shiXiao;
    Intent intent;
    private GatewayPasswordPlanBean gatewayPasswordPlanBean;
    private String[] weekdays;
    public String gatewayId;
    public String deviceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_plan_password_share);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        tvNumber = findViewById(R.id.tv_number);
        btnDelete = findViewById(R.id.btn_delete);
        tvShortMessage = findViewById(R.id.tv_short_message);
        tvWeiXin = findViewById(R.id.tv_wei_xin);
        tvCopy = findViewById(R.id.tv_copy);
        ivEditor = findViewById(R.id.iv_editor);
        tvPassword = findViewById(R.id.tv_password);

        intent = getIntent();
        gatewayPasswordPlanBean = (GatewayPasswordPlanBean) intent.getSerializableExtra(KeyConstants.GATEWAY_PASSWORD_BEAN);
        password = intent.getStringExtra(KeyConstants.TO_DETAIL_PASSWORD);
        gatewayId = getIntent().getStringExtra(KeyConstants.GATEWAY_ID);
        deviceId = getIntent().getStringExtra(KeyConstants.DEVICE_ID);


        String pwd = "";
        for (char c : password.toCharArray()) {
            pwd += " " + c;
        }

        if (TextUtils.isEmpty(password)){

        }

        tvPassword.setText(pwd);
        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.password_detail));


        ivEditor.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        initData();
        tvShortMessage.setOnClickListener(this);
        tvWeiXin.setOnClickListener(this);
        tvCopy.setOnClickListener(this);
    }

    private void initData() {
        String notice = "";
        int num = gatewayPasswordPlanBean.getPasswordNumber();
        if (num > 4 && num < 9) {  //临时密码
            notice =  getString(R.string.permanent_once_validity);
        } else if (num == 9) { //胁迫密码
            notice =  getString(R.string.stress_password);
        } else {
            int userType = gatewayPasswordPlanBean.getUserType(); //userType: 0 永久性密钥   userType: 1 策略密钥   userType: 3 管理员密钥   userType: 4 无权限密钥
            if (userType == 0) {
                notice =  getString(R.string.permanent_validity);
            } else {  //策略密钥
                String planType = gatewayPasswordPlanBean.getPlanType();
                if ("year".equals(planType)) {
                    long startTime = gatewayPasswordPlanBean.getZigBeeLocalStartTime();
                    long endTime = gatewayPasswordPlanBean.getZigBeeLocalEndTime();
                    LogUtils.d("  startTime  "+startTime+"  endTime  "+endTime);
                    long startSeconds = startTime + BleCommandFactory.defineTime;
                    String start = DateUtils.getDateTimeFromMillisecond(startSeconds * 1000);//要上传的开锁时间
                    long endSeconds = endTime + BleCommandFactory.defineTime;
                    String end = DateUtils.getDateTimeFromMillisecond(endSeconds * 1000);//要上传的开锁时间
                    notice =  getString(R.string.password_valid_shi_xiao) + "  " + start + "~" + end;
                    LogUtils.d("显示的内容是   "  +notice );
                    if (endTime - startTime == 24 * 60 * 60) {
                        notice =  getString(R.string.password_one_day_valid);
                    }
                } else if ("week".equals(planType)) {
                    if (weekdays == null) {
                        weekdays = new String[]{ getString(R.string.week_day),
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
                    notice = String.format( getString(R.string.week_hint), weeks,
                            gatewayPasswordPlanBean.getStartHour() + ":" + gatewayPasswordPlanBean.getStartMinute(),
                            gatewayPasswordPlanBean.getEndHour() + ":" + gatewayPasswordPlanBean.getEndMinute());
                    LogUtils.d("重复的天数是   " + Arrays.toString(days));
                } else {
                    notice =  getString(R.string.permanent_validity);
                }
            }
        }
        tvNumber.setText(notice);
    }

    @Override
    protected GatewayLockPasswordSharePresenter<IGatewayLockPasswordShareView> createPresent() {
        return new GatewayLockPasswordSharePresenter<>();
    }

    @Override
    public void onClick(View v) {
        String message = String.format(getString(R.string.share_content), password, shiXiao);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_editor:
                break;
            case R.id.btn_delete:
                AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.hint), getString(R.string.sure_delete_password), getString(R.string.philips_cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }
                    @Override
                    public void right() {
                        showLoading(getString(R.string.is_deleting));
                        mPresenter.deletePassword(gatewayId,deviceId,gatewayPasswordPlanBean.getPasswordNumber());
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(String toString) {
                    }
                });
                break;
            case R.id.tv_short_message:
                SharedUtil.getInstance().sendShortMessage(message, this);
                break;
            case R.id.tv_wei_xin:
                if (SharedUtil.isWeixinAvilible(this)) {
                    SharedUtil.getInstance().sendWeiXin(message);
                } else {
                    ToastUtils.showShort(R.string.telephone_not_install_wechat);
                }
                break;
            case R.id.tv_copy:
                SharedUtil.getInstance().copyTextToSystem(this, message);
                break;
        }
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
    public void setPlanSuccess(String passwordValue,GatewayPasswordPlanBean gatewayPasswordPlanBean) {

    }

    @Override
    public void setPlanFailed(Throwable throwable) {

    }

    @Override
    public void deletePasswordSuccess() {
        hiddenLoading();
        ToastUtils.showLong(getString(R.string.delete_success));
    }

    @Override
    public void deletePasswordFailed(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showLong(getString(R.string.delete_fialed));
    }

    @Override
    public void gatewayPasswordFull() {

    }
}
