package com.philips.easykey.lock.activity.addDevice.gateway;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class AddGatewayFirstActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView addDeviceBackground;
    TextView setting;
    Button buttonNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_gateway_add);
        back = findViewById(R.id.back);
        addDeviceBackground = findViewById(R.id.add_device_background);
        setting = findViewById(R.id.setting);
        buttonNext = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        setting.setOnClickListener(v -> {
            Intent settingLightIntent = new Intent(this, AddGatewayLightActivity.class);
            startActivity(settingLightIntent);
        });
        buttonNext.setOnClickListener(v -> {
            Intent nextIntent = new Intent(this, AddGatewaySecondActivity.class);
            startActivity(nextIntent);
        });

        initView();
    }

    private void initView() {
        setting.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG );
    }

}
