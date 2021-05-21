package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.utils.KeyConstants;

import androidx.appcompat.app.AppCompatActivity;

public class PhilipsWifiVideoLockSettingDuressAlarmReceiverAvtivity extends AppCompatActivity {


    private ImageView mBack;
    private EditText mEtReceiver;
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
            Intent intent = new Intent(this,PhilipsWifiVideoLockSettingDuressAlarmReceiverAvtivity.class);
            intent.putExtra("duress_alarm_phone",mEtReceiver.getText().toString().trim());
            setResult(RESULT_OK,intent);
            finish();
        });
    }

    private void showDuressAlarmDialog() {

    }

    private void initView() {
        mBack = findViewById(R.id.back);
        mEtReceiver = findViewById(R.id.et_receiver);
    }

}