package com.philips.easykey.lock.activity.addDevice.zigbee;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAddGatewayHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class AddZigbeeLockFifthActivity  extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    Button buttonNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_zigbeelock_fifth);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        buttonNext = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> {
            Intent intent=new Intent(this, DeviceAddGatewayHelpActivity.class);
            startActivity(intent);
        });
        buttonNext.setOnClickListener(v -> {
            Intent searchIntent=new Intent(this, AddZigbeeLockSuccessSaveActivity.class);
            startActivity(searchIntent);
        });
    }
}
