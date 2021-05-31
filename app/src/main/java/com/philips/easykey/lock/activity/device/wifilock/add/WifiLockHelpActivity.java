package com.philips.easykey.lock.activity.device.wifilock.add;

import android.os.Bundle;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class WifiLockHelpActivity extends BaseAddToApplicationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_help);

        findViewById(R.id.back).setOnClickListener(v -> finish());
    }

}
