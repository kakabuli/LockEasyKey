package com.philips.easykey.lock.activity.device.videolock;

import android.os.Bundle;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.utils.KeyConstants;

import androidx.appcompat.app.AppCompatActivity;

public class PhilipsWifiVideoLockSettingDuressAlarmReceiverAvtivity extends AppCompatActivity {


    private ImageView mBack;
    private String wifiSn = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_setting_duress_alarm_receiver);

        initView();
        initListener();
        initData();
    }

    private void initData() {
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
    }

    private void initListener() {

        mBack.setOnClickListener(v -> {
            finish();
        });
    }

    private void showDuressAlarmDialog() {

    }

    private void initView() {
        mBack = findViewById(R.id.back);
    }

}