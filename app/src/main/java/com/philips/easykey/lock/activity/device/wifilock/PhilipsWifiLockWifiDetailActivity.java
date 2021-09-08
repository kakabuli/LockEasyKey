package com.philips.easykey.lock.activity.device.wifilock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
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
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;


public class PhilipsWifiLockWifiDetailActivity extends BaseActivity<IPhilipsWifiDetailView, PhilipsWifiVideoLockWifiDetailPresenter<IPhilipsWifiDetailView>>
        implements IPhilipsWifiDetailView {

    ImageView back;
    RelativeLayout rlReplaceWifi;
    TextView tvWifiName;
    TextView tvWifiStrength;
    TextView tvRssid;
    TextView tvMAC;

    private String wifiSn;
    private WifiLockInfo wifiLockInfo;

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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
}
