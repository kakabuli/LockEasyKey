package com.philips.easykey.lock.activity.device.gatewaylock.more;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter.GatewayLockLangPresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.GatewayLockLangView;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LoadingDialog;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;


public class GatewayLockLanguageSettingActivity extends BaseActivity<GatewayLockLangView,GatewayLockLangPresenter<GatewayLockLangView>> implements View.OnClickListener,GatewayLockLangView {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    CheckBox zhImg;
    RelativeLayout zhLayout;
    CheckBox enImg;
    RelativeLayout enLayout;
    Button btnSave;
    private String languageCurrent = "";

    private String  deviceId;
    private String  gatewayId;
    private LoadingDialog loadingDialog;

    private String lockCurrentLang="";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_lock_language_setting);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        zhImg = findViewById(R.id.zh_img);
        zhLayout = findViewById(R.id.zh_layout);
        enImg = findViewById(R.id.en_img);
        enLayout = findViewById(R.id.en_layout);
        btnSave = findViewById(R.id.btn_save);

        zhLayout.setOnClickListener(v -> {
            zhImg.setChecked(true);
            enImg.setChecked(false);
            languageCurrent = "zh";
        });
        enLayout.setOnClickListener(v -> {
            zhImg.setChecked(false);
            enImg.setChecked(true);
            languageCurrent = "en";
        });
        btnSave.setOnClickListener(v -> {
            if (TextUtils.isEmpty(lockCurrentLang)){
                ToastUtils.showShort(getString(R.string.no_get_current_lang));
                return;
            }
            if (lockCurrentLang.equals(languageCurrent)){
                ToastUtils.showShort(getString(R.string.lock_lang_no_change));
                return;
            }
            if (gatewayId!=null&&deviceId!= null){
                ToastUtils.showShort(R.string.is_setting_lock_lang);
                mPresenter.setLang(gatewayId,deviceId,languageCurrent);
            }
        });

        initView();
        initData();
        initListener();


    }

    @Override
    protected GatewayLockLangPresenter<GatewayLockLangView> createPresent() {
        return new GatewayLockLangPresenter<>();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
    }

    private void initView() {
        tvContent.setText(getString(R.string.lock_language));
        loadingDialog=LoadingDialog.getInstance(this);
    }
    private void initData() {
        Intent intent=getIntent();
        deviceId=intent.getStringExtra(KeyConstants.DEVICE_ID);
        gatewayId=intent.getStringExtra(KeyConstants.GATEWAY_ID);
        if (deviceId!=null&&gatewayId!=null){
            loadingDialog.show(getString(R.string.get_lock_lang));
            mPresenter.getLang(gatewayId,deviceId);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void getLockLangSuccess(String lang) {
        LogUtils.d("获取到的语言是"+lang);
        loadingDialog.dismiss();
        if ("zh".equals(lang)){
            zhImg.setChecked(true);
            enImg.setChecked(false);
            lockCurrentLang="zh";
            languageCurrent="zh";
        }else{
            zhImg.setChecked(false);
            enImg.setChecked(true);
            lockCurrentLang="en";
            languageCurrent="en";
        }

    }

    @Override
    public void getLockLangFail() {
        loadingDialog.dismiss();
        ToastUtils.showShort(R.string.get_lock_lang_fail);
    }

    @Override
    public void getLockLangThrowable(Throwable throwable) {
        loadingDialog.dismiss();
        LogUtils.d("获取锁的语言异常      "+throwable.getMessage());
    }

    @Override
    public void setLockLangSuccess(String lang) {
        languageCurrent=lang;
        lockCurrentLang=lang;
        ToastUtils.showShort(getString(R.string.set_success));
    }


    @Override
    public void setLockLangFail() {
        ToastUtils.showShort(getString(R.string.set_failed));
    }

    @Override
    public void setLockLangThrowable(Throwable throwable) {
        LogUtils.d("设置锁的语言异常    "+throwable.getMessage());
    }
}
