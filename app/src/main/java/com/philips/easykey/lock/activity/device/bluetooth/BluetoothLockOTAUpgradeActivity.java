package com.philips.easykey.lock.activity.device.bluetooth;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

/**
 * Created by David on 2019/4/11
 */
public class BluetoothLockOTAUpgradeActivity extends BaseAddToApplicationActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_lock_ota_upgrade);
    }
}
