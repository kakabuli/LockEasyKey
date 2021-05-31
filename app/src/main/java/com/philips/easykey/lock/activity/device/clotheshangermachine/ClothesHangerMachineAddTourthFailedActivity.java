package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAdd2Activity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class ClothesHangerMachineAddTourthFailedActivity extends BaseAddToApplicationActivity {

    ImageView back;
    TextView button_confirm;
    TextView button_reconnection;

    private String wifiModelType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_add_tourth_failed);

        back = findViewById(R.id.back);
        button_confirm = findViewById(R.id.button_confirm);
        button_reconnection = findViewById(R.id.button_reconnection);

        back.setOnClickListener(v -> {
            Intent firstIntent = new Intent(this, ClothesHangerMachineAddFirstActivity.class);
            firstIntent.putExtra("wifiModelType",wifiModelType);
            startActivity(firstIntent);
            finish();
        });
        button_confirm.setOnClickListener(v -> {
            Intent firstIntent = new Intent(this, ClothesHangerMachineAddFirstActivity.class);
            firstIntent.putExtra("wifiModelType",wifiModelType);
            startActivity(firstIntent);
            finish();
        });
        button_reconnection.setOnClickListener(v -> {
            Intent addIntent = new Intent(this, DeviceAdd2Activity.class);
            startActivity(addIntent);
            finish();
        });

        wifiModelType = getIntent().getStringExtra("wifiModelType");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
