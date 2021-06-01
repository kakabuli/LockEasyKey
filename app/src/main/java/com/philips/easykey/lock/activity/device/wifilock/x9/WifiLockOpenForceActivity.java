package com.philips.easykey.lock.activity.device.wifilock.x9;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.WifiLockMoreActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.x9.WifiLockOpenForcePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.x9.IWifiLockOpenForceView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.ToastUtils;

public class WifiLockOpenForceActivity extends BaseActivity<IWifiLockOpenForceView, WifiLockOpenForcePresenter<IWifiLockOpenForceView>>
        implements IWifiLockOpenForceView{

    RelativeLayout rlLowLayout;
    RelativeLayout rlHighLayout;
    CheckBox ckLow;
    CheckBox ckHigh;

    private String wifiSn = "";
    private WifiLockInfo wifiLockInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_open_force);

        rlLowLayout = findViewById(R.id.low_layout);
        rlHighLayout = findViewById(R.id.high_layout);
        ckLow = findViewById(R.id.ck_low);
        ckHigh = findViewById(R.id.ck_high);

        findViewById(R.id.back).setOnClickListener(v -> setOpenForce());
        findViewById(R.id.low_layout).setOnClickListener(v -> {
            ckLow.setChecked(true);
            ckHigh.setChecked(false);
        });
        findViewById(R.id.high_layout).setOnClickListener(v -> {
            ckLow.setChecked(false);
            ckHigh.setChecked(true);
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

        if(wifiLockInfo != null){
            if(wifiLockInfo.getOpenForce() == 1){
                ckLow.setChecked(true);
                ckHigh.setChecked(false);
            }else{
                ckLow.setChecked(false);
                ckHigh.setChecked(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setOpenForce();
    }

    private void setOpenForce() {
        int openForce = getOpenForce();
        if(wifiLockInfo.getOpenForce() == openForce){
            finish();
        }else{
            showLoading(getString(R.string.wifi_video_lock_waiting));
            mPresenter.setOpenForce(wifiSn,openForce);
        }
    }

    private int getOpenForce() {
        if (ckLow.isChecked()) {
            return 1;
        }
        if (ckHigh.isChecked()) {
            return 2;
        }
        return 1;
    }

    @Override
    protected WifiLockOpenForcePresenter<IWifiLockOpenForceView> createPresent() {
        return new WifiLockOpenForcePresenter<>();
    }

    @Override
    public void settingThrowable(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.modify_failed));
        finish();
    }

    @Override
    public void settingFailed() {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.modify_failed));
        finish();
    }

    @Override
    public void settingSuccess(int openForce) {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.modify_success));
        Intent intent = new Intent(this, WifiLockMoreActivity.class);
        intent.putExtra(MqttConstant.SET_OPEN_FORCE,openForce);
        setResult(RESULT_OK,intent);
        finish();
    }
}
