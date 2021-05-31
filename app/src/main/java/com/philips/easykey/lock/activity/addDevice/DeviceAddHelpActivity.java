package com.philips.easykey.lock.activity.addDevice;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class DeviceAddHelpActivity extends BaseAddToApplicationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_help);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

    }
}
