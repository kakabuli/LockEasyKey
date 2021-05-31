package com.philips.easykey.lock.activity.device.videolock;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.utils.KeyConstants;


public class WifiVideoLockFirwareNumberActivity extends BaseAddToApplicationActivity {

    TextView tvHardwareVersion;
    TextView tvHardVersion;
    ImageView ivHardVersion;

    private String wifiSN;
    private WifiLockInfo wifiLockInfoBySn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_video_firware_number);

        tvHardwareVersion = findViewById(R.id.tv_hardware_version);
        tvHardVersion = findViewById(R.id.tv_hard_version);
        ivHardVersion = findViewById(R.id.iv_hard_version);

        findViewById(R.id.back).setOnClickListener(v -> finish());

        wifiSN = getIntent().getStringExtra(KeyConstants.WIFI_SN);


        wifiLockInfoBySn = MyApplication.getInstance().getWifiLockInfoBySn(wifiSN);

        if(wifiLockInfoBySn != null){
            if(wifiLockInfoBySn.getLockFirmwareVersion() != null){
                tvHardVersion.setText(wifiLockInfoBySn.getLockFirmwareVersion());
            }

            if(wifiLockInfoBySn.getWifiVersion() != null){
                tvHardwareVersion.setText(wifiLockInfoBySn.getWifiVersion());
            }

            if(wifiLockInfoBySn.getIsAdmin() == 1){
                ivHardVersion.setVisibility(View.VISIBLE);
            }else{
                ivHardVersion.setVisibility(View.GONE);
            }
        }

    }

}
