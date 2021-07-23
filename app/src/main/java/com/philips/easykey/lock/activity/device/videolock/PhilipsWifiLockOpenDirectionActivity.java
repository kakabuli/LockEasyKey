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
import com.philips.easykey.lock.mvp.presenter.wifilock.x9.WifiLockOpenDirectionPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.x9.IWifiLockOpenDirectionView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;

public class PhilipsWifiLockOpenDirectionActivity extends BaseActivity<IWifiLockOpenDirectionView, WifiLockOpenDirectionPresenter<IWifiLockOpenDirectionView>>
        implements IWifiLockOpenDirectionView , View.OnClickListener {

    ImageView ivBack;
    RelativeLayout rlLeftLayout;
    RelativeLayout rlRightLayout;
    CheckBox ckLeft;
    CheckBox ckRight;
    AVLoadingIndicatorView avi;
    TextView tvTips;

    private String wifiSn = "";
    private WifiLockInfo wifiLockInfo;

    private int openDirection;

    private InnerRecevier mInnerRecevier = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_open_direction);

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        ivBack = findViewById(R.id.back);
        rlLeftLayout = findViewById(R.id.left_layout);
        rlRightLayout = findViewById(R.id.right_layout);
        ckLeft = findViewById(R.id.ck_left);
        ckRight = findViewById(R.id.ck_right);
        avi = findViewById(R.id.avi);
        tvTips = findViewById(R.id.tv_tips);
        ivBack.setOnClickListener(this);
        rlLeftLayout.setOnClickListener(this);
        rlRightLayout.setOnClickListener(this);

        if(wifiLockInfo != null){
            if(wifiLockInfo.getOpenDirection() == 1){
                ckLeft.setChecked(true);
                ckRight.setChecked(false);
            }else{
                ckLeft.setChecked(false);
                ckRight.setChecked(true);
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
            setOpenDirection();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                setOpenDirection();
                break;
            case R.id.left_layout:
                ckLeft.setChecked(true);
                ckRight.setChecked(false);
                break;
            case R.id.right_layout:
                ckLeft.setChecked(false);
                ckRight.setChecked(true);
                break;
        }
    }

    private void setOpenDirection() {
        openDirection = getOpenDirection();
        if(wifiLockInfo.getOpenDirection() == openDirection){
            finish();
        }else{
            if(BleLockUtils.isSupportXMConnect(wifiLockInfo.getFunctionSet())){
                setConnectOpenDirection(wifiSn,openDirection);
            }else{
                showLoading(getString(R.string.wifi_video_lock_waiting));
                mPresenter.setOpenDirection(wifiSn,openDirection, MqttConstant.SET_OPEN_DIRECTION);
            }
        }
    }

    private void setConnectOpenDirection(String wifiSn, int openDirection) {
        if(avi.isShow()) {
            if (wifiLockInfo.getPowerSave() == 0) {
                tvTips.setVisibility(View.VISIBLE);
                avi.setVisibility(View.VISIBLE);
                avi.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.setConnectOpenDirection(wifiSn,openDirection);
                    }
                }).start();
            }else{
                powerStatusDialog();
            }
        }
    }

    private int getOpenDirection() {
        if (ckLeft.isChecked()) {
            return 1;
        }
        if (ckRight.isChecked()) {
            return 2;
        }
        return 1;
    }

    @Override
    protected WifiLockOpenDirectionPresenter<IWifiLockOpenDirectionView> createPresent() {
        return new WifiLockOpenDirectionPresenter<>();
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
        if(!PhilipsWifiLockOpenDirectionActivity.this.isFinishing()){
            mPresenter.setMqttCtrl(0);
            mPresenter.handler.post(new Runnable() {
                @Override
                public void run() {
                    hiddenLoading();
                    if(flag){
                        ToastUtils.showLong(getString(R.string.modify_success));
                        Intent intent;
                        if(BleLockUtils.isSupportXMConnect(wifiLockInfo.getFunctionSet())){
                            intent = new Intent(PhilipsWifiLockOpenDirectionActivity.this, PhilipsWifiVideoLockMoreActivity.class);
                        }else {
                            intent = new Intent(PhilipsWifiLockOpenDirectionActivity.this, WifiLockMoreActivity.class);
                        }

                        intent.putExtra(MqttConstant.SET_OPEN_DIRECTION,openDirection);
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
