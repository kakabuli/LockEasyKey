package com.philips.easykey.lock.activity.device.wifilock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockDetailActivity;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockLanguageSettingActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.PhilipsAddVideoLockActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewFirstActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewThirdActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockOldUserFirstActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.PhilipsWifiVideoLockWifiDetailPresenter;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockSetLanguagePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IPhilipsWifiDetailView;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IWifiVideoLockSetLanguageView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PConnectError;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;


public class PhilipsWifiLockWifiDetailActivity extends BaseActivity<IPhilipsWifiDetailView, PhilipsWifiVideoLockWifiDetailPresenter<IPhilipsWifiDetailView>>
        implements IPhilipsWifiDetailView {

    ImageView back;
    RelativeLayout rlReplaceWifi;
    TextView tvWifiName;
    TextView tvWifiStrength;
    TextView tvRssid;
    TextView tvMAC;
    AVLoadingIndicatorView avi;
    TextView tvTips;

    private String wifiSn;
    private WifiLockInfo wifiLockInfo;

    private InnerRecevier mInnerRecevier = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_wifi_detail);

        back = findViewById(R.id.back);
        rlReplaceWifi = findViewById(R.id.rl_replace_wifi);
        tvWifiName = findViewById(R.id.tv_wifi_name);
        tvWifiStrength = findViewById(R.id.tv_wifi_strength);
        tvRssid = findViewById(R.id.tv_rssid);
        tvMAC = findViewById(R.id.tv_mac);
        avi = findViewById(R.id.avi);
        tvTips = findViewById(R.id.tv_tips);


        back.setOnClickListener(v -> finish());
        rlReplaceWifi.setOnClickListener(v -> {
            //老的wifi锁不存在这个字段，为wifi配网1，wifi&ble为2
            LogUtils.d("--kaadas--老的wifi锁不存在这个字段，为wifi配网1，wifi&ble为2--->" + wifiLockInfo.getDistributionNetwork());
            if (TextUtils.isEmpty(String.valueOf(wifiLockInfo.getDistributionNetwork()))) {
                Intent wifiIntent = new Intent(this, WifiLockOldUserFirstActivity.class);
                String wifiModelType = "WiFi";
                wifiIntent.putExtra("wifiModelType", wifiModelType);
                startActivity(wifiIntent);
                //startActivity(new Intent(this, WifiLockOldUserFirstActivity.class));
            } else if (wifiLockInfo.getDistributionNetwork() == 0 || wifiLockInfo.getDistributionNetwork() == 1) {
                Intent wifiIntent = new Intent(this, WifiLockOldUserFirstActivity.class);
                String wifiModelType = "WiFi";
                wifiIntent.putExtra("wifiModelType", wifiModelType);
                startActivity(wifiIntent);
            } else if (wifiLockInfo.getDistributionNetwork() == 2) {
                Intent wifiIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
                String wifiModelType = "WiFi&BLE";
                wifiIntent.putExtra("wifiModelType", wifiModelType);
                startActivity(wifiIntent);
            } else if(wifiLockInfo.getDistributionNetwork() == 3){
                showWifiDialog();
            }else {
                LogUtils.d("--kaadas--wifiLockInfo.getDistributionNetwork()为" + wifiLockInfo.getDistributionNetwork());

            }
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        initData();
    }

    private void initData() {
        if(wifiLockInfo != null){
            tvWifiName.setText(wifiLockInfo.getWifiName() + "");
            if(wifiLockInfo.getWifiStrength() != 0)
                tvWifiStrength.setText(wifiLockInfo.getWifiStrength() + "%");
            if(wifiLockInfo.getRSSI() != null)
                tvRssid.setText(wifiLockInfo.getRSSI() + "");
            if(wifiLockInfo.getLockMac() != null)
                tvMAC.setText(wifiLockInfo.getLockMac() + "");


            if(wifiLockInfo.getDistributionNetwork() == 3 ||
                    BleLockUtils.isSupportXMConnect(wifiLockInfo.getFunctionSet())){
                mPresenter.settingDevice(wifiLockInfo);
            }else {
                tvTips.setVisibility(View.GONE);
                avi.hide();
            }
        }
    }

    @Override
    protected PhilipsWifiVideoLockWifiDetailPresenter<IPhilipsWifiDetailView> createPresent() {
        return new PhilipsWifiVideoLockWifiDetailPresenter<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(avi != null){
            avi.hide();
            tvTips.setVisibility(View.GONE);
        }
        avi.setVisibility(View.VISIBLE);
        tvTips.setVisibility(View.VISIBLE);
        avi.show();
        if(wifiLockInfo.getDistributionNetwork() == 3 ||
                BleLockUtils.isSupportXMConnect(wifiLockInfo.getFunctionSet())){
            registerBroadcast();
            if(wifiLockInfo.getPowerSave() == 1){
                if(wifiLockInfo.getPower() < 30){
                    powerSaveModeStatus();
                    return;
                }
                createDialog(getString(R.string.philips_closed_video_mode_title),getString(R.string.philips_closed_video_mode_content));
                ToastUtils.showShort(R.string.philips_get_wifi_rssi_error);
                return;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    mPresenter.connectP2P();
                }
            }).start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        if(wifiLockInfo.getDistributionNetwork() == 3 ||
                BleLockUtils.isSupportXMConnect(wifiLockInfo.getFunctionSet()))
            mPresenter.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(wifiLockInfo.getDistributionNetwork() == 3 ||
                BleLockUtils.isSupportXMConnect(wifiLockInfo.getFunctionSet()))
            unRegisterBroadcast();
    }


    private void showWifiDialog() {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(this, getString(R.string.activity_wifi_video_replace_wifi_again),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#999999", "#1F95F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        Intent wifiIntent = new Intent(PhilipsWifiLockWifiDetailActivity.this, PhilipsAddVideoLockActivity.class);
                        String wifiModelType = "WiFi&VIDEO";
                        wifiIntent.putExtra("wifiModelType", wifiModelType);
                        startActivity(wifiIntent);
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

    @Override
    public void getSignalSuccess(String rssi, String strength) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTips.setVisibility(View.GONE);
                avi.hide();
                tvRssid.setText(rssi);
                tvWifiStrength.setText(strength);
            }
        });
    }

    @Override
    public void getSignalFailed(int errorCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*String content = "";
                if(errorCode == 0){
                    content = getString(R.string.video_lock_xm_connect_failed_1);
                }else{
                    content = XMP2PConnectError.checkP2PErrorStringWithCode(PhilipsWifiLockWifiDetailActivity.this,errorCode);
                }*/
                // TODO: 2021/10/22 这里在获取到wifi的rssi后偶尔会回调这个接口，所以在wifi强度，rssi有值时返回
                if(!TextUtils.isEmpty(tvRssid.getText()) && !TextUtils.isEmpty(tvWifiStrength.getText()))return;
                if(errorCode == -3){
                    createDialog(getString(R.string.video_lock_xm_connect_time_out_1) + "","");
                }else{
                    createDialog(getString(R.string.video_lock_xm_connect_failed_1) + "","");
                }

                tvTips.setVisibility(View.GONE);
                avi.hide();
            }
        });

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

    public void createDialog(String title,String content){
        AlertDialogUtil.getInstance().PhilipsSingleButtonDialog(this, title,content,
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

    public void powerSaveModeStatus(){
        AlertDialogUtil.getInstance().titleTwoButtonPhilipsDialog(this, getString(R.string.philips_videolock_power_save_mode_title),
                getString(R.string.philips_videolock_power_save_mode_content),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm),
                "#0066A1","#FFFFFF",new AlertDialogUtil.ClickListener() {
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
