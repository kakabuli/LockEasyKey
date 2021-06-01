package com.philips.easykey.lock.activity.addDevice.gateway;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class AddGatewayLightActivity extends BaseAddToApplicationActivity {

    ImageView back;
    Button confirmLightButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_gateway_light);
        back = findViewById(R.id.back);
        confirmLightButton = findViewById(R.id.confirm_light_button);
        back.setOnClickListener(v -> finish());
        confirmLightButton.setOnClickListener(v -> finish());

    }

}
