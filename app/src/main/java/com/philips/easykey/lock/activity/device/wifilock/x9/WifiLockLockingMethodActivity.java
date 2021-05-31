package com.philips.easykey.lock.activity.device.wifilock.x9;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.WifiLockMoreActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.x9.WifiLockLockingMethodPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.x9.IWifiLockLockingMethodView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.ToastUtils;


public class WifiLockLockingMethodActivity extends BaseActivity<IWifiLockLockingMethodView, WifiLockLockingMethodPresenter<IWifiLockLockingMethodView>>
        implements IWifiLockLockingMethodView {

    RelativeLayout rlAutoLayout;
    RelativeLayout rlSecond5Layout;
    RelativeLayout rlSecond10Layout;
    RelativeLayout rlSecond15Layout;
    RelativeLayout rlCloseLayout;
    CheckBox ckAuto;
    CheckBox ckSecond5;
    CheckBox ckSecond10;
    CheckBox ckSecond15;
    CheckBox ckClose;

    private String wifiSn = "";
    private WifiLockInfo wifiLockInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_locking_method);

        rlAutoLayout = findViewById(R.id.auto_layout);
        rlSecond5Layout = findViewById(R.id.second_5_layout);
        rlSecond10Layout = findViewById(R.id.second_10_layout);
        rlSecond15Layout = findViewById(R.id.second_15_layout);
        rlCloseLayout = findViewById(R.id.close_layout);
        ckAuto = findViewById(R.id.ck_auto);
        ckSecond5 = findViewById(R.id.ck_second_5);
        ckSecond10 = findViewById(R.id.ck_second_10);
        ckSecond15 = findViewById(R.id.ck_second_15);
        ckClose = findViewById(R.id.ck_close);

        findViewById(R.id.back).setOnClickListener(v -> setLockingMethod());
        findViewById(R.id.auto_layout).setOnClickListener(v -> showLockingMethod(1));
        findViewById(R.id.second_5_layout).setOnClickListener(v -> showLockingMethod(2));
        findViewById(R.id.second_10_layout).setOnClickListener(v -> showLockingMethod(3));
        findViewById(R.id.second_15_layout).setOnClickListener(v -> showLockingMethod(4));
        findViewById(R.id.close_layout).setOnClickListener(v -> showLockingMethod(5));

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

        if(wifiLockInfo != null){
            showLockingMethod(wifiLockInfo.getLockingMethod());
        }
    }

    private void showLockingMethod(int type) {
        switch (type){
            case 1:
                ckAuto.setChecked(true);
                ckSecond5.setChecked(false);
                ckSecond10.setChecked(false);
                ckSecond15.setChecked(false);
                ckClose.setChecked(false);
                break;
            case 2:
                ckAuto.setChecked(false);
                ckSecond5.setChecked(true);
                ckSecond10.setChecked(false);
                ckSecond15.setChecked(false);
                ckClose.setChecked(false);
                break;
            case 3:
                ckAuto.setChecked(false);
                ckSecond5.setChecked(false);
                ckSecond10.setChecked(true);
                ckSecond15.setChecked(false);
                ckClose.setChecked(false);
                break;
            case 4:
                ckAuto.setChecked(false);
                ckSecond5.setChecked(false);
                ckSecond10.setChecked(false);
                ckSecond15.setChecked(true);
                ckClose.setChecked(false);
                break;
            case 5:
                ckAuto.setChecked(false);
                ckSecond5.setChecked(false);
                ckSecond10.setChecked(false);
                ckSecond15.setChecked(false);
                ckClose.setChecked(true);
                break;
        }
    }

    private int getLockingMethod(){
        if(ckAuto.isChecked()){
            return 1;
        }
        if(ckSecond5.isChecked()){
            return 2;
        }
        if(ckSecond10.isChecked()){
            return 3;
        }
        if(ckSecond15.isChecked()){
            return 4;
        }
        if(ckClose.isChecked()){
            return 5;
        }
        return 1;
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
        setLockingMethod();
    }

    @Override
    protected WifiLockLockingMethodPresenter<IWifiLockLockingMethodView> createPresent() {
        return new WifiLockLockingMethodPresenter<>();
    }

    private void setLockingMethod(){
        int lockingMethod = getLockingMethod();
        if(wifiLockInfo.getLockingMethod() == lockingMethod){
            finish();
        }else{
            showLoading(getString(R.string.wifi_video_lock_waiting));
            mPresenter.setLockingMethod(wifiSn,lockingMethod);
        }
        Intent intent = new Intent(this, WifiLockMoreActivity.class);
        intent.putExtra(MqttConstant.SET_LOCKING_METHOD,lockingMethod);
        setResult(RESULT_OK,intent);
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
    public void settingSuccess(int lockingMethod) {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.modify_success));
        Intent intent = new Intent(this, WifiLockMoreActivity.class);
        intent.putExtra(MqttConstant.SET_LOCKING_METHOD,lockingMethod);
        setResult(RESULT_OK,intent);
        finish();
    }
}
