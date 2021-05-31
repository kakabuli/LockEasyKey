package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;


public class ClothesHangerMachineAddThirdSuccessActivity extends BaseAddToApplicationActivity {

    ImageView back;

    private String wifiModelType = "";
    private int bleVersion = 0;
    private String deviceSN = "";
    private String deviceMAC = "";
    private String deviceName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_add_third_success);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        wifiModelType = getIntent().getStringExtra("wifiModelType");
        bleVersion = getIntent().getIntExtra(KeyConstants.BLE_VERSION,4);
        deviceSN = getIntent().getStringExtra(KeyConstants.BLE_DEVICE_SN);
        deviceMAC = getIntent().getStringExtra(KeyConstants.BLE_MAC);
        deviceName = getIntent().getStringExtra(KeyConstants.DEVICE_NAME);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(ClothesHangerMachineAddThirdSuccessActivity.this, ClothesHangerMachineAddTourthActivity.class);
            intent.putExtra("wifiModelType",wifiModelType);
            intent.putExtra(KeyConstants.BLE_VERSION, bleVersion);
            intent.putExtra(KeyConstants.BLE_DEVICE_SN, deviceSN);
            intent.putExtra(KeyConstants.BLE_MAC, deviceMAC);
            intent.putExtra(KeyConstants.DEVICE_NAME, deviceName);
            startActivity(intent);
            finish();
        },1500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
