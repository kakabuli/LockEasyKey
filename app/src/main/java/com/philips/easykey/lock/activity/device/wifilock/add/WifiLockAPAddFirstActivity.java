package com.philips.easykey.lock.activity.device.wifilock.add;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.ImageView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.GpsUtil;
import com.philips.easykey.lock.utils.WifiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

public class WifiLockAPAddFirstActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    Button buttonNext;
    final RxPermissions rxPermissions = new RxPermissions(this);
    private Disposable permissionDisposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_lock_add_device_first);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        buttonNext = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> {
            Intent intent = new Intent(this, WifiLockHelpActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.tohelp).setOnClickListener(v -> {
            Intent intent = new Intent(this, WifiLockHelpActivity.class);
            startActivity(intent);
        });
        buttonNext.setOnClickListener(v -> {
            Intent searchIntent = new Intent(this, WifiLockApAddSecondActivity.class);
            startActivity(searchIntent);
        });

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (permissionDisposable != null) {
            permissionDisposable.dispose();
        }
    }


}
