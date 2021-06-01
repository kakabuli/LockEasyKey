package com.philips.easykey.lock.activity.device.wifilock.add;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.espressif.iot.esptouch.util.TouchNetUtil;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.GpsUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.WifiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

public class WifiLockApAddThirdActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView tvNotice;
    final RxPermissions rxPermissions = new RxPermissions(this);
    TextView head;
    Button btAp;
    Button btSmartConfig;
    private Disposable permissionDisposable;
    private boolean isAp = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_device_wifi_lock_third);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        tvNotice = findViewById(R.id.tv_notice);
        head = findViewById(R.id.head);
        btAp = findViewById(R.id.bt_ap);
        btSmartConfig = findViewById(R.id.bt_smart_config);

        back.setOnClickListener(v -> finish());
        btAp.setOnClickListener(v -> {
            saveWifiName();
            Intent intent = new Intent(WifiLockApAddThirdActivity.this, WifiLockApAutoConnectWifiActivity.class);
            startActivity(intent);
        });
        help.setOnClickListener(v -> startActivity(new Intent(this, WifiLockHelpActivity.class)));

        isAp = getIntent().getBooleanExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        //获取权限  定位权限
        permissionDisposable = rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION)
                .subscribe(granted -> {
                    if (granted) {

                    } else {
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void saveWifiName() {
        WifiManager wifiMgr = (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String ssid = info != null ? info.getSSID() : null;
        if (TextUtils.isEmpty(ssid)) {
            SPUtils.put(KeyConstants.WIFI_LOCK_CONNECT_NAME, "");
            return;
        }
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        if (!ssid.equals("kaadas_AP") && !"<unknown ssid>".equals(ssid)) {
            SPUtils.put(KeyConstants.WIFI_LOCK_CONNECT_NAME, ssid);
        }
        byte[] ssidOriginalData = TouchNetUtil.getOriginalSsidBytes(info);
        LogUtils.d("获取到的   byte数据是    " + Rsa.bytesToHexString(ssidOriginalData));
        SPUtils.put(KeyConstants.WIFI_LOCK_CONNECT_ORIGINAL_DATA, Rsa.bytesToHexString(ssidOriginalData));
    }
}