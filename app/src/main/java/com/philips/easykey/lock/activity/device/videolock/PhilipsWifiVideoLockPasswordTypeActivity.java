package com.philips.easykey.lock.activity.device.videolock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class PhilipsWifiVideoLockPasswordTypeActivity extends BaseAddToApplicationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_password_type);
    }
}