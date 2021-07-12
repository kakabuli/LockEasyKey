package com.philips.easykey.lock.activity.addDevice;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class PhilipsAddDeviceHelpActivity extends BaseAddToApplicationActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.philips_add_device_help_dialog);
        findViewById(R.id.iv_close).setOnClickListener(v -> {
            finish();
        });
    }
}
