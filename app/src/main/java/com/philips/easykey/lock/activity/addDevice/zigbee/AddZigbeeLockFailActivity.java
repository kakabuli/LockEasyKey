package com.philips.easykey.lock.activity.addDevice.zigbee;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAdd2Activity;
import com.philips.easykey.lock.activity.addDevice.DeviceBindGatewayListActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class AddZigbeeLockFailActivity extends BaseAddToApplicationActivity {
    ImageView back;
    Button buttonReconnection;
    Button buttonOut;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_zigbeelock_add_fail);
        back = findViewById(R.id.back);
        buttonReconnection = findViewById(R.id.button_reconnection);
        buttonOut = findViewById(R.id.button_out);

        back.setOnClickListener(v -> {
            Intent backIntent=new Intent(this, DeviceBindGatewayListActivity.class);
            startActivity(backIntent);
            finish();
        });
        buttonOut.setOnClickListener(v -> {
            Intent outIntent=new Intent(this, DeviceAdd2Activity.class);
            startActivity(outIntent);
            finish();
        });
        buttonReconnection.setOnClickListener(v -> {
            Intent reconnectionIntent=new Intent(this, DeviceBindGatewayListActivity.class);
            startActivity(reconnectionIntent);
        });

    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isCosumenBackKey();
        }
        return false;
    }

    private boolean isCosumenBackKey() {
        Intent backIntent=new Intent(this, DeviceBindGatewayListActivity.class);
        startActivity(backIntent);
        finish();
        return true;
    }
}
