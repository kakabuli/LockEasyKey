package com.philips.easykey.lock.activity.device.gatewaylock.stress.old;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter.GatewayLockStressAddPresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.IGatewayLockStressAddView;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;

public class GatewayLockStressAddActivity extends BaseActivity<IGatewayLockStressAddView, GatewayLockStressAddPresenter<IGatewayLockStressAddView>> implements IGatewayLockStressAddView {

    ImageView back;
    TextView headTitle;
    ImageView pwdManagerIcon;
    EditText etPassword;
    TextView btnRandomGeneration;
    Button btnConfirmGeneration;

    private String gatewayId;
    private String deviceId;
    private AlertDialog takeEffect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gateway_lock_stress_password_add);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        pwdManagerIcon = findViewById(R.id.pwd_manager_icon);
        etPassword = findViewById(R.id.et_password);
        btnRandomGeneration = findViewById(R.id.btn_random_generation);
        btnConfirmGeneration = findViewById(R.id.btn_confirm_generation);

        back.setOnClickListener(v -> finish());
        btnRandomGeneration.setOnClickListener(v -> {
            String password = StringUtil.makeRandomPassword();
            if (etPassword!=null){
                etPassword.setText(password);
                etPassword.setSelection(password.length());
            }
        });
        btnConfirmGeneration.setOnClickListener(v -> {
            if (!NetUtil.isNetworkAvailable()) {
                AlertDialogUtil.getInstance().singleButtonNoTitleDialog(this, getString(R.string.no_find_network), getString(R.string.philips_confirm), "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(String toString) {
                    }
                });
                return;
            }
            String strForeverPassword = etPassword.getText().toString().trim();
            if (!StringUtil.randomJudge(strForeverPassword)) {
                ToastUtils.showShort(R.string.philips_random_verify_error);
                return;
            }
            if (StringUtil.checkSimplePassword(strForeverPassword)) {
                AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(this, getString(R.string.password_simple_please_reset), getString(R.string.go_on), getString(R.string.reinstall),"#1F96F7","#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }
                    @Override
                    public void right() {
                        etPassword.setText("");
                        return;
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(String toString) {
                    }
                });
                return;
            }
            if (!TextUtils.isEmpty(gatewayId)&&!TextUtils.isEmpty(deviceId)){
                mPresenter.addLockPwd(gatewayId,deviceId,"09",strForeverPassword);
            }
            takeEffect=AlertDialogUtil.getInstance().noButtonDialog(this,getString(R.string.take_effect_be_being));
            takeEffect.setCancelable(false);
        });

        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        gatewayId=intent.getStringExtra(KeyConstants.GATEWAY_ID);
        deviceId=intent.getStringExtra(KeyConstants.DEVICE_ID);
    }

    @Override
    protected GatewayLockStressAddPresenter<IGatewayLockStressAddView> createPresent() {
        return new GatewayLockStressAddPresenter<>();
    }


    @Override
    public void addStressSuccess(String pwdValue) {

        //跳转到分享页面
        Intent intent=new Intent(this,GatewayLockStressShareActivity.class);
        intent.putExtra(KeyConstants.GATEWAY_ID,gatewayId);
        intent.putExtra(KeyConstants.DEVICE_ID,deviceId);
        intent.putExtra(KeyConstants.PWD_VALUE,pwdValue);
        intent.putExtra(KeyConstants.PWD_ID,"09");
        startActivity(intent);

    }

    @Override
    public void addStressFail() {
        //密码添加失败
        LogUtils.d("添加密码失败");
        if (takeEffect!=null){
            takeEffect.dismiss();
        }
        AlertDialogUtil.getInstance().singleButtonNoTitleDialog(this, getString(R.string.add_lock_pwd_fail), getString(R.string.philips_confirm), "#1F96F7", new AlertDialogUtil.ClickListener() {
                @Override
                public void left() {

                }

                @Override
                public void right() {

                }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(String toString) {
            }
            });

    }

    @Override
    public void addStressThrowable(Throwable throwable) {
        //密码添加异常
        LogUtils.d("添加密码异常    ");
        if (takeEffect!=null){
            takeEffect.dismiss();
        }
            AlertDialogUtil.getInstance().singleButtonNoTitleDialog(this, getString(R.string.add_lock_pwd_fail), getString(R.string.philips_confirm), "#1F96F7", new AlertDialogUtil.ClickListener() {
                @Override
                public void left() {

                }

                @Override
                public void right() {

                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }
                @Override
                public void afterTextChanged(String toString) {
                }
            });
        }

    }

