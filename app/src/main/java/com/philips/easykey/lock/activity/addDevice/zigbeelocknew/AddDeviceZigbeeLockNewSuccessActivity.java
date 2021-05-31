package com.philips.easykey.lock.activity.addDevice.zigbeelocknew;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class AddDeviceZigbeeLockNewSuccessActivity extends BaseAddToApplicationActivity {
    Button buttonNext;
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_add_zigbeenewlock_success);
        buttonNext = findViewById(R.id.button_next);
        back = findViewById(R.id.back);

        back.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, MainActivity.class);
            startActivity(backIntent);
        });
        buttonNext.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, MainActivity.class);
            startActivity(backIntent);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent finishIntent = new Intent(this, MainActivity.class);
        startActivity(finishIntent);
    }


}
