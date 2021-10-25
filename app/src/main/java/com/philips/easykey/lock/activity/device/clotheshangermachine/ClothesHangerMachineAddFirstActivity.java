package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.espressif.iot.esptouch.util.TouchNetUtil;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.GpsUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.WifiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.disposables.Disposable;

public class ClothesHangerMachineAddFirstActivity extends BaseAddToApplicationActivity {

    TextView button_next;
    ImageView back;

    private String wifiModelType = "";
    private Disposable permissionDisposable;
    final RxPermissions rxPermissions = new RxPermissions(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_add_first);

        button_next = findViewById(R.id.button_next);
        back = findViewById(R.id.back);

        back.setOnClickListener(v -> finish());
        button_next.setOnClickListener(v -> {
            //检查权限，检查是否连接wifi
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
            if (!WifiUtils.getInstance(MyApplication.getInstance()).isWifiEnable()) {
                showWifiDialog();
                WifiUtils.getInstance(MyApplication.getInstance()).openWifi();
                return;
            }
            if (!GpsUtil.isOPen(MyApplication.getInstance())) {
                GpsUtil.openGPS(MyApplication.getInstance());
                showLocationPermission();
                return;
            }
            Intent intent = new Intent(this, ClothesHangerMachineAddSecondActivity.class);
            intent.putExtra("wifiModelType",wifiModelType);
            startActivity(intent);
            finish();
        });

        wifiModelType = getIntent().getStringExtra("wifiModelType") + "";
        saveWifiName();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void showWifiDialog() {
        AlertDialogUtil.getInstance().noEditTitleOneButtonDialog(
                ClothesHangerMachineAddFirstActivity.this,
                getString(R.string.philips_activity_clothes_hanger_machine_add_dialog_1),
                getString(R.string.philips_dialog_go_to_connect), "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {
                    }

                    @Override
                    public void right() {
//                        Intent wifiIntent =  new Intent(Settings.ACTION_WIFI_SETTINGS);
//                        startActivity(wifiIntent);
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void showLocationPermission() {

        String message = String.format(getString(R.string.philips_activity_clothes_hanger_machine_add_dialog_3), getString(R.string.app_name));

        AlertDialogUtil.getInstance().havaNoEditTwoButtonDialog(
                ClothesHangerMachineAddFirstActivity.this
                , getString(R.string.dialog_wifi_video_tip),
                message,
                getString(R.string.philips_confirm), getString(R.string.philips_cancel),"#1F96F7", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                        startActivityForResult(intent,887);
                    }

                    @Override
                    public void right() {

                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
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
