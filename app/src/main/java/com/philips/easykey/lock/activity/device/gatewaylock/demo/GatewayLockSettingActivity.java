package com.philips.easykey.lock.activity.device.gatewaylock.demo;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.widget.EditText;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.ToastUtils;


public class GatewayLockSettingActivity extends BaseActivity<CatwayLockSettingView, GatwayLockSettingPresenter<CatwayLockSettingView>> implements CatwayLockSettingView {

    TextView setArmLocked;
    TextView setAM;
    TextView getArmLocked;
    TextView getAM;
    EditText inputArmlock;
    EditText inputAM;

    private String gatewayId;
    private String deviceId;
    private int armlock;
    private int am;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        setArmLocked = findViewById(R.id.setArmLocked);
        setAM = findViewById(R.id.setAM);
        getArmLocked = findViewById(R.id.getArmLocked);
        getAM = findViewById(R.id.getAM);
        inputArmlock = findViewById(R.id.input_armlock);
        inputAM = findViewById(R.id.input_AM);

        setArmLocked.setOnClickListener(v -> {
            armlock=Integer.parseInt(inputArmlock.getText().toString().trim());
            mPresenter.setArmLocked(MyApplication.getInstance().getUid(), gatewayId, deviceId, armlock);
        });
        setAM.setOnClickListener(v -> {
            am=Integer.parseInt(inputAM.getText().toString().trim());
            mPresenter.setAM(MyApplication.getInstance().getUid(),gatewayId,deviceId,am);
        });
        getArmLocked.setOnClickListener(v -> mPresenter.getArmLocked(MyApplication.getInstance().getUid(),gatewayId,deviceId));
        getAM.setOnClickListener(v -> mPresenter.getAm(MyApplication.getInstance().getUid(),gatewayId,deviceId));

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        gatewayId = intent.getStringExtra(KeyConstants.GATEWAY_ID);
        deviceId = intent.getStringExtra(KeyConstants.DEVICE_ID);
        armlock=Integer.parseInt(inputArmlock.getText().toString().trim());
        am=Integer.parseInt(inputAM.getText().toString().trim());
    }

    @Override
    protected GatwayLockSettingPresenter<CatwayLockSettingView> createPresent() {
        return new GatwayLockSettingPresenter<>();
    }

    @Override
    public void setArmLockedSuccess() {
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_1);
    }

    @Override
    public void setArmLockedFail() {
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_2);
    }

    @Override
    public void setArmLockedThrowable(Throwable throwable) {
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_3);
    }

    @Override
    public void setAMSuccess() {
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_4);
    }

    @Override
    public void setAMFail() {
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_5);
    }

    @Override
    public void setAMThrowable(Throwable throwable) {
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_6);
    }

    @Override
    public void getArmLockedSuccess(int operatingMode) {
        if (getArmLocked!=null){
            getArmLocked.setText(getString(R.string.philips_activity_gatewaylock_setting_7) + operatingMode);
        }
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_8);
    }

    @Override
    public void getArmLockedFail() {
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_9);
    }

    @Override
    public void getArmLockedThrowable(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.philips_activity_gatewaylock_setting_10));
    }

    @Override
    public void getAMSuccess(int autoRelockTime) {
        if (getAM!=null){
            getArmLocked.setText(getString(R.string.philips_activity_gatewaylock_setting_11,autoRelockTime + ""));
        }
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_12);
    }

    @Override
    public void getAMFail() {
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_13);
    }

    @Override
    public void getAMThrowable(Throwable throwable) {
        ToastUtils.showShort(R.string.philips_activity_gatewaylock_setting_14);
    }
}
