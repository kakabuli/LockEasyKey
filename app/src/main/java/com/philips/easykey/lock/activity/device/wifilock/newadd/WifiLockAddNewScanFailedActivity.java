package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class WifiLockAddNewScanFailedActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    ImageView ivAnim;
    TextView notice;
    TextView reScan;
    TextView tvOtherMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_scan_failed);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        ivAnim = findViewById(R.id.iv_anim);
        notice = findViewById(R.id.notice);
        reScan = findViewById(R.id.re_scan);
        tvOtherMethod = findViewById(R.id.tv_other_method);

        back.setOnClickListener(v -> {
            startActivity(new Intent(this,WifiLockAddNewScanActivity.class));
            finish();
        });
        help.setOnClickListener(v ->  startActivity(new Intent(this,WifiLockHelpActivity.class)));
        reScan.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this,WifiLockAddNewScanActivity.class));
        });
        tvOtherMethod.setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this,WifiLockOldUserFirstActivity.class));
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,WifiLockAddNewScanActivity.class));
        super.onBackPressed();
    }
}
