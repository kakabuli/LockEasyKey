package com.philips.easykey.lock.activity.device.wifilock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewFirstActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewThirdActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockOldUserFirstActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhilipsWifiLockWifiDetailActivity extends BaseAddToApplicationActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.rl_replace_wifi)
    RelativeLayout rlReplaceWifi;
    @BindView(R.id.tv_wifi_name)
    TextView tvWifiName;
    @BindView(R.id.tv_wifi_strength)
    TextView tvWifiStrength;
    @BindView(R.id.tv_rssid)
    TextView tvRssid;
    @BindView(R.id.tv_mac)
    TextView tvMAC;

    private String wifiSn;
    private WifiLockInfo wifiLockInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_wifi_detail);
        ButterKnife.bind(this);

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
        }
    }

    @OnClick({R.id.back, R.id.rl_replace_wifi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_replace_wifi:
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
                break;

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

    private void showWifiDialog() {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(this, getString(R.string.activity_wifi_video_replace_wifi_again),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#999999", "#1F95F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        Intent wifiIntent = new Intent(PhilipsWifiLockWifiDetailActivity.this, WifiLockAddNewThirdActivity.class);
                        String wifiModelType = "WiFi&VIDEO";
                        wifiIntent.putExtra("wifiModelType", wifiModelType);
                        wifiIntent.putExtra("distribution_again", true);
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
