package com.philips.easykey.lock.activity.addDevice.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAddHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class AddBluetoothPairFailActivity extends BaseAddToApplicationActivity {

    ImageView back;
    Button repair;
    ImageView help;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_add_fail);
        back = findViewById(R.id.back);
        repair = findViewById(R.id.repair);
        help = findViewById(R.id.help);

        back.setOnClickListener(v -> finish());
        repair.setOnClickListener(v -> {
            //重新配对
            Intent intent = new Intent(this, AddBluetoothPairActivity.class);
            startActivity(intent);
        });
        help.setOnClickListener(v -> {
            Intent helpIntent = new Intent(this, DeviceAddHelpActivity.class);
            startActivity(helpIntent);
        });
    }

}
