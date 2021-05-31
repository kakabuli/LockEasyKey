package com.philips.easykey.lock.activity.device.gatewaylock;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class GatewayAuthorizationDeviceInformationActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    TextView tvSerialNumber;
    TextView tvDeviceModel;
    TextView tvLockFirmwareVersion;
    TextView tvSoftwareVersion;
    RelativeLayout rlSoftwareVersion;
    TextView tvBluetoothVersion;
    RelativeLayout rlBluetoothVersion;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization_gateway_device_information);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        tvSerialNumber = findViewById(R.id.tv_serial_number);
        tvDeviceModel = findViewById(R.id.tv_device_model);
        tvLockFirmwareVersion = findViewById(R.id.tv_lock_firmware_version);
        tvSoftwareVersion = findViewById(R.id.tv_software_version);
        rlSoftwareVersion = findViewById(R.id.rl_software_version);
        tvBluetoothVersion = findViewById(R.id.tv_bluetooth_version);
        rlBluetoothVersion = findViewById(R.id.rl_bluetooth_version);

        ivBack.setOnClickListener(this);
        tvContent.setText(R.string.device_info);
        rlBluetoothVersion.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_bluetooth_version:
                //蓝牙版本
                break;
        }
    }
}
