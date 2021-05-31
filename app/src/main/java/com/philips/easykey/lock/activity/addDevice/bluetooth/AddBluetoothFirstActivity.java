package com.philips.easykey.lock.activity.addDevice.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAddHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class AddBluetoothFirstActivity extends BaseAddToApplicationActivity {
    ImageView back;
    ImageView help;
    Button buttonNext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_bluetooth_first);
        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        buttonNext = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> {
            Intent intent=new Intent(this, DeviceAddHelpActivity.class);
            startActivity(intent);
        });
        buttonNext.setOnClickListener(v -> {
            Intent searchIntent=new Intent(this,AddBluetoothSearchActivity.class);
            startActivity(searchIntent);
        });
    }

}
