package com.philips.easykey.lock.activity.device.wifilock.add;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;


public class WifiLockAPAddFailedActivity extends BaseAddToApplicationActivity {

    ImageView back;
    TextView headTitle;
    LinearLayout toLookSupportRoute;
    TextView btRepair;
    EditText tvSupportList;
    ImageView help;
    private boolean isAp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wifi_lock_failed);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        toLookSupportRoute = findViewById(R.id.to_look_support_route);
        btRepair = findViewById(R.id.bt_repair);
        tvSupportList = findViewById(R.id.tv_support_list);
        help = findViewById(R.id.help);

        back.setOnClickListener(v -> {
            Intent intent;
            if (isAp) {
                intent = new Intent(WifiLockAPAddFailedActivity.this, WifiLockAPAddFirstActivity.class);
            } else {
                intent = new Intent(WifiLockAPAddFailedActivity.this, WifiLockAddFirstActivity.class);
                intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, false);
            }
            startActivity(intent);
            finish();
        });
        btRepair.setOnClickListener(v -> {
            Intent intent;
            if (isAp) {
                intent = new Intent(WifiLockAPAddFailedActivity.this, WifiLockAPAddFirstActivity.class);
            } else {
                intent = new Intent(WifiLockAPAddFailedActivity.this, WifiLockAddFirstActivity.class);
                intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, false);
            }
            startActivity(intent);
            finish();
        });
        toLookSupportRoute.setOnClickListener(v -> {
            //跳转查看支持WiFi列表
            startActivity(new Intent(WifiLockAPAddFailedActivity.this, WifiLcokSupportWifiActivity.class));
        });
        tvSupportList.setOnClickListener(v -> {
            //跳转查看支持WiFi列表
            startActivity(new Intent(WifiLockAPAddFailedActivity.this, WifiLcokSupportWifiActivity.class));
        });
        help.setOnClickListener(v -> {
            //跳转查看支持WiFi列表
            startActivity(new Intent(WifiLockAPAddFailedActivity.this, WifiLockHelpActivity.class));
        });
        findViewById(R.id.et_other_method).setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(WifiLockAPAddFailedActivity.this, WifiLockAddFirstActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.cancel).setOnClickListener(v -> {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        });

        isAp = getIntent().getBooleanExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        toLookSupportRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WifiLockAPAddFailedActivity.this, WifiLcokSupportWifiActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent;
        if (isAp) {
            intent = new Intent(WifiLockAPAddFailedActivity.this, WifiLockAPAddFirstActivity.class);
            startActivity(intent);
        } else {
            intent = new Intent(WifiLockAPAddFailedActivity.this, WifiLockAddFirstActivity.class);
            intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, false);
            startActivity(intent);
        }
    }

}
