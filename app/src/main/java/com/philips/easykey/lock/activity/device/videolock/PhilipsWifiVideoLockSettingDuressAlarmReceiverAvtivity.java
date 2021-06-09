package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDuressBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.PhilipsSettingDuressAlarmPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IPhilipsSettingDuressAlarm;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DetectionEmailPhone;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.PhoneUtil;
import com.philips.easykey.lock.utils.StringUtil;

public class PhilipsWifiVideoLockSettingDuressAlarmReceiverAvtivity extends BaseActivity<IPhilipsSettingDuressAlarm,
        PhilipsSettingDuressAlarmPresenter<IPhilipsSettingDuressAlarm>> implements IPhilipsSettingDuressAlarm {


    private ImageView mBack;
    private TextView confirm;
    private EditText mEtReceiver;
    private PhilipsDuressBean mDuressBean;
    private String duressAccount;
    private int position;
    private int accountType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_setting_duress_alarm_receiver);

        initView();
        initListener();
        initData();
    }

    @Override
    protected PhilipsSettingDuressAlarmPresenter<IPhilipsSettingDuressAlarm> createPresent() {
        return new PhilipsSettingDuressAlarmPresenter<>();
    }

    private void initData() {
        mDuressBean = (PhilipsDuressBean) getIntent().getSerializableExtra(KeyConstants.DURESS_PASSWORD_INfO);
        position = getIntent().getIntExtra(KeyConstants.DURESS_PASSWORD_POSITION_INfO,-1);
        if(mDuressBean != null && !TextUtils.isEmpty(mDuressBean.getDuressAlarmAccount())){
            mEtReceiver.setText(mDuressBean.getDuressAlarmAccount());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            settingDuressAccount();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void settingDuressAccount() {
        duressAccount = mEtReceiver.getText().toString().trim();

        if(mDuressBean.getDuressAlarmAccount() == duressAccount){
            finish();
            return;
        }

        if(TextUtils.isEmpty(duressAccount)) {
            AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_account_message_not_empty));
            return;
        }

        if(StringUtil.isNumeric(duressAccount)){
            if(!PhoneUtil.isMobileNO(duressAccount)){
                AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                return;
            }

            if(mDuressBean == null){
                finish();
                ToastUtils.showShort(R.string.modify_failed);
                return;
            }
            accountType = 1;
            duressAccount = "86" + duressAccount;
            mPresenter.setDuressPwdAlarm(mDuressBean.getWifiSN(),mDuressBean.getPwdType(),mDuressBean.getNum(),mDuressBean.getPwdDuressSwitch());
//            mPresenter.setDuressPwdAccount(mDuressBean.getWifiSN(), mDuressBean.getPwdType(), mDuressBean.getNum(),1,duressAccount);
            return;
        }else{
            if(!DetectionEmailPhone.isEmail(duressAccount)){
                AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                return;
            }

            if(mDuressBean == null){
                ToastUtils.showShort(R.string.set_failed);
                finish();
                return;
            }
            accountType = 2;
            mPresenter.setDuressPwdAlarm(mDuressBean.getWifiSN(),mDuressBean.getPwdType(),mDuressBean.getNum(),mDuressBean.getPwdDuressSwitch());
//            mPresenter.setDuressPwdAccount(mDuressBean.getWifiSN(), mDuressBean.getPwdType(), mDuressBean.getNum(),2,duressAccount);
            return;
        }

    }

    private void initListener() {
        mBack.setOnClickListener(v -> {
            settingDuressAccount();
        });
        confirm.setOnClickListener(v -> {
            settingDuressAccount();
        });
    }

    private void showDuressAlarmDialog() {

    }

    private void initView() {
        mBack = findViewById(R.id.back);
        mEtReceiver = findViewById(R.id.et_receiver);
        confirm = findViewById(R.id.confirm);
    }

    @Override
    public void onSettingDuressAccount(BaseResult baseResult) {
        if("200".equals(baseResult.getCode() + "")){
            mDuressBean.setDuressAlarmAccount(duressAccount);
            ToastUtils.showShort(R.string.set_success);
            Intent intent = new Intent(this, PhilipsWifiVideoLockDuressAlarmAvtivity.class);
            intent.putExtra(KeyConstants.DURESS_PASSWORD_INfO, mDuressBean);
            intent.putExtra(KeyConstants.WIFI_SN, mDuressBean.getWifiSN());
            intent.putExtra(KeyConstants.DURESS_PASSWORD_POSITION_INfO, position);
            startActivity(intent);
//            setResult(RESULT_OK, intent);
            finish();
        }else if("453".equals(baseResult.getCode() + "")){
            AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.phliips_user_not_registered));
        }else{
            ToastUtils.showShort(R.string.set_failed);
            Intent intent = new Intent(this, PhilipsWifiVideoLockDuressAlarmAvtivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, mDuressBean.getWifiSN());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onSettingDuress(BaseResult baseResult) {
        if("200".equals(baseResult.getCode() + "")) {
            mPresenter.setDuressPwdAccount(mDuressBean.getWifiSN(), mDuressBean.getPwdType(), mDuressBean.getNum(),accountType,duressAccount);
        }else{
            ToastUtils.showShort(R.string.set_failed);
            /*Intent intent = new Intent(this, WifiVideoLockDuressAlarmAvtivity.class);
            startActivity(intent);
            finish();*/
        }
    }

}