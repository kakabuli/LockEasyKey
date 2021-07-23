package com.philips.easykey.lock.activity.device.videolock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.WifiLockMoreActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.x9.WifiLockOpenForcePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.x9.IWifiLockOpenForceView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;


public class PhilipsWifiLockOpenForceActivity extends BaseActivity<IWifiLockOpenForceView, WifiLockOpenForcePresenter<IWifiLockOpenForceView>>
        implements IWifiLockOpenForceView , View.OnClickListener {

    ImageView ivBack;
    RelativeLayout rlLowLayout;
    RelativeLayout rlHighLayout;
    CheckBox ckLow;
    CheckBox ckHigh;
    AVLoadingIndicatorView avi;
    TextView tvTips;


    private String wifiSn = "";
    private WifiLockInfo wifiLockInfo;

    private InnerRecevier mInnerRecevier = null;

    private int openForce;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_open_force);

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        ivBack = findViewById(R.id.back);
        rlLowLayout = findViewById(R.id.low_layout);
        rlHighLayout = findViewById(R.id.high_layout);
        ckLow = findViewById(R.id.ck_low);
        ckHigh = findViewById(R.id.ck_high);
        avi = findViewById(R.id.avi);
        tvTips = findViewById(R.id.tv_tips);
        ivBack.setOnClickListener(this);
        rlLowLayout.setOnClickListener(this);
        rlHighLayout.setOnClickListener(this);

        if(wifiLockInfo != null){
            openForce = wifiLockInfo.getOpenForce();
            if(wifiLockInfo.getOpenForce() == 1){
                ckLow.setChecked(true);
                ckHigh.setChecked(false);
            }else{
                ckLow.setChecked(false);
                ckHigh.setChecked(true);
            }

            if(BleLockUtils.isSupportXMConnect(wifiLockInfo.getFunctionSet())){
                mPresenter.settingDevice(wifiLockInfo);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcast();
        if(avi != null){
            avi.hide();
            tvTips.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setOpenForce();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                setOpenForce();
                break;
            case R.id.low_layout:
                ckLow.setChecked(true);
                ckHigh.setChecked(false);
                break;
            case R.id.high_layout:
                ckLow.setChecked(false);
                ckHigh.setChecked(true);
                break;
        }
    }

    private void setOpenForce() {
        openForce = getOpenForce();
        if(wifiLockInfo.getOpenForce() == openForce){
            finish();
        }else{
            if(BleLockUtils.isSupportXMConnect(wifiLockInfo.getFunctionSet())){
                setConnectOpenForce(wifiSn,openForce);
            }else{
                showLoading(getString(R.string.wifi_video_lock_waiting));
                mPresenter.setOpenForce(wifiSn,openForce, MqttConstant.SET_OPEN_FORCE);
            }
        }
    }

    private void setConnectOpenForce(String wifiSn, int openForce) {
        if(avi.isShow()) {
            if (wifiLockInfo.getPowerSave() == 0) {
                tvTips.setVisibility(View.VISIBLE);
                avi.setVisibility(View.VISIBLE);
                avi.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.setConnectOpenForce(wifiSn,openForce);
                    }
                }).start();
            }else{
                powerStatusDialog();
            }
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

    public void powerStatusDialog(){
        String content = getString(R.string.dialog_wifi_video_power_status);
        AlertDialogUtil.getInstance().noEditSingleButtonDialog(this, getString(R.string.set_failed), "\n"+ content +"\n",
                getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
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

    private void registerBroadcast(){
        if(mInnerRecevier == null){
            mInnerRecevier = new InnerRecevier();
        }
        IntentFilter homeFilter = new IntentFilter();
        homeFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        homeFilter.addAction(Intent.ACTION_SCREEN_ON);
        homeFilter.addAction(Intent.ACTION_SCREEN_OFF);
        homeFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mInnerRecevier, homeFilter);
    }

    private void unRegisterBroadcast(){
        if(mInnerRecevier != null){
            unregisterReceiver(mInnerRecevier);
        }
    }

    private class InnerRecevier extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        // home键
                        mPresenter.release();
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        //多任务
                        mPresenter.release();
                    }
                }
            }else if(action.equals(Intent.ACTION_SCREEN_ON)){
            }else if(action.equals(Intent.ACTION_SCREEN_OFF)){
                mPresenter.release();
            }else if(action.equals(Intent.ACTION_USER_PRESENT)){// 解锁

            }

        }
    }

    @Override
    public void finish() {
        super.finish();
        mPresenter.release();
    }

    @Override
    public void onSettingCallBack(boolean flag) {
        if(!PhilipsWifiLockOpenForceActivity.this.isFinishing()){
            mPresenter.setMqttCtrl(0);
            mPresenter.handler.post(new Runnable() {
                @Override
                public void run() {
                    hiddenLoading();
                    if(flag){
                        ToastUtils.showLong(getString(R.string.modify_success));
                        Intent intent;
                        if(BleLockUtils.isSupportXMConnect(wifiLockInfo.getFunctionSet())){
                            intent = new Intent(PhilipsWifiLockOpenForceActivity.this, PhilipsWifiLockOpenForceActivity.class);
                        }else {
                            intent = new Intent(PhilipsWifiLockOpenForceActivity.this, WifiLockMoreActivity.class);
                        }

                        intent.putExtra(MqttConstant.SET_OPEN_FORCE,openForce);
                        setResult(RESULT_OK,intent);
                    }else{
                        ToastUtils.showLong(getString(R.string.modify_failed));
                    }
                    if(avi != null){
                        tvTips.setVisibility(View.GONE);
                        avi.hide();
                    }
                    finish();
                }
            });
        }
    }
}
