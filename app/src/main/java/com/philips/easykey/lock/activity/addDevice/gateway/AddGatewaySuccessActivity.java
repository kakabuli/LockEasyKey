package com.philips.easykey.lock.activity.addDevice.gateway;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAdd2Activity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class AddGatewaySuccessActivity extends BaseAddToApplicationActivity {

    ImageView back;
    Button buttonAddZigbee;
    Button buttonStop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_gateway_add_success);
        back = findViewById(R.id.back);
        buttonAddZigbee = findViewById(R.id.button_add_zigbee);
        buttonStop = findViewById(R.id.button_stop);

        back.setOnClickListener(v -> {
            Intent deviceAddIntent=new Intent(this, DeviceAdd2Activity.class);
            startActivity(deviceAddIntent);
            finish();
        });
        buttonAddZigbee.setOnClickListener(v -> {
            Intent deviceAddZigbee=new Intent(this, DeviceAdd2Activity.class);
            startActivity(deviceAddZigbee);
            finish();
        });
        buttonStop.setOnClickListener(v -> {
            Intent deviceDetail=new Intent(this, MainActivity.class);
            startActivity(deviceDetail);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DeviceAdd2Activity.class));
        finish();
    }
}
