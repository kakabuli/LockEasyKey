package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.utils.KeyConstants;

import androidx.appcompat.app.AppCompatActivity;

public class PhilipsWifiVideoLockSettingDuressAlarmAvtivity extends AppCompatActivity {


    private ImageView mBack;
    private ImageView mIvDuressAlarmHelp;
    private RelativeLayout mRlDuressAlarmAppReceiver;
    private ImageView mIvDuressSelect;
    private String wifiSn = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_setting_duress_alarm);

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
        mIvDuressAlarmHelp.setOnClickListener(v -> {
            showDuressAlarmDialog();
        });
        mRlDuressAlarmAppReceiver.setOnClickListener(v -> {
            Intent intent = new Intent(this,PhilipsWifiVideoLockSettingDuressAlarmReceiverAvtivity.class);
            startActivity(intent);
        });
        mIvDuressSelect.setOnClickListener(v -> {
            if(mIvDuressSelect.isSelected()){
                mIvDuressSelect.setSelected(false);
            }else{
                mIvDuressSelect.setSelected(true);
            }
        });
    }

    private void showDuressAlarmDialog() {

    }

    private void initView() {
        mBack = findViewById(R.id.back);
        mIvDuressSelect = findViewById(R.id.iv_duress_select);
        mIvDuressAlarmHelp = findViewById(R.id.iv_duress_alarm_help);
        mRlDuressAlarmAppReceiver = findViewById(R.id.rl_duress_alarm_app_recevice);
    }

}