package com.philips.easykey.lock.activity.device.wifilock.add;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.GpsUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.WifiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

public class WifiLockApAddSecondActivity extends BaseAddToApplicationActivity {

    ImageView back;
    Button buttonNext;
    ImageView help;
    final RxPermissions rxPermissions = new RxPermissions(this);
    TextView head;
    TextView notice;
    private Disposable permissionDisposable;
    private boolean isAp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_lock_add_device_second);

        back = findViewById(R.id.back);
        buttonNext = findViewById(R.id.button_next);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);

        back.setOnClickListener(v -> finish());
        buttonNext.setOnClickListener(v -> {
            Intent intent = new Intent(WifiLockApAddSecondActivity.this, WifiLockApAddThirdActivity.class);
            startActivity(intent);
        });
        help.setOnClickListener(v -> startActivity(new Intent(this, WifiLockHelpActivity.class)));

        isAp = getIntent().getBooleanExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        if (!isAp) {
            head.setText(R.string.first_step);

            notice.setText(getString(R.string.noticesdkjfh));
        }
        //获取权限  定位权限
        permissionDisposable = rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {
                        // All requested permissions are granted
                    } else {
                        // At least one permission is denied
                        ToastUtils.showShort(getString(R.string.philips_granted_local_please_open_wifi));

                    }
                });
        //打开wifi
        WifiUtils wifiUtils = WifiUtils.getInstance(MyApplication.getInstance());
        if (!wifiUtils.isWifiEnable()) {
            wifiUtils.openWifi();
            ToastUtils.showShort(getString(R.string.philips_wifi_no_open_please_open_wifi));
        }
        if (!GpsUtil.isOPen(MyApplication.getInstance())) {
            GpsUtil.openGPS(MyApplication.getInstance());
            ToastUtils.showShort(getString(R.string.locak_no_open_please_open_local));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        isAp = intent.getBooleanExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        if (!isAp) {
            head.setText(R.string.first_step);
            notice.setText(getString(R.string.noticesdkjfh));
        }
    }

}
