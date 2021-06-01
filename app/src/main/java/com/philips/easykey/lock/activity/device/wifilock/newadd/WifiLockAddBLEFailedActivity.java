package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.os.Bundle;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLcokSupportWifiActivity;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class WifiLockAddBLEFailedActivity extends BaseAddToApplicationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi_lock_failed);

        findViewById(R.id.back).setOnClickListener(v -> {
            Intent intent = new Intent(this, WifiLockAddNewFirstActivity.class);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.bt_repair).setOnClickListener(v -> {
            Intent intent = new Intent(this, WifiLockAddNewFirstActivity.class);
            startActivity(intent);
            finish();
        });
        findViewById(R.id.to_look_support_route).setOnClickListener(v -> {
            //跳转查看支持WiFi列表
            startActivity(new Intent(this, WifiLcokSupportWifiActivity.class));
        });
        findViewById(R.id.tv_support_list).setOnClickListener(v -> {
            //跳转查看支持WiFi列表
            startActivity(new Intent(this, WifiLcokSupportWifiActivity.class));
        });
        findViewById(R.id.help).setOnClickListener(v -> {
            //跳转查看支持WiFi列表
            startActivity(new Intent(this, WifiLockHelpActivity.class));
        });
        findViewById(R.id.cancel).setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, WifiLockAddNewFirstActivity.class);
        startActivity(intent);
    }


}
