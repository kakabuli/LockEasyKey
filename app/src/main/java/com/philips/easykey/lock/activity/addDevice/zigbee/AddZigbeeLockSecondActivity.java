package com.philips.easykey.lock.activity.addDevice.zigbee;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAddGatewayHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class AddZigbeeLockSecondActivity extends BaseAddToApplicationActivity {

    ImageView back;
    Button buttonNext;
    ImageView help;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_bluetooth_second);

        back = findViewById(R.id.back);
        buttonNext = findViewById(R.id.button_next);
        help = findViewById(R.id.help);

        back.setOnClickListener(v -> finish());
        buttonNext.setOnClickListener(v -> {
            Intent nextIntent = new Intent(this, AddZigbeeLockThirdActivity.class);
            startActivity(nextIntent);
        });
        help.setOnClickListener(v -> {
            Intent intent=new Intent(this, DeviceAddGatewayHelpActivity.class);
            startActivity(intent);
        });
    }

}
