package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.videolock.WifiVideoLockHelpActivity;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.GpsUtil;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.WifiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class WifiLockAddNewFirstActivity extends BaseAddToApplicationActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.help)
    ImageView help;
    @BindView(R.id.head)
    TextView head;
    @BindView(R.id.notice)
    TextView notice;
    @BindView(R.id.button_next)
    TextView buttonNext;
    @BindView(R.id.tv_reconnect)
    TextView tvReconnect;
    @BindView(R.id.iv_img_lock)
    ImageView ivImgLock;

    private String wifiModelType = "";

    private Disposable permissionDisposable;
    final RxPermissions rxPermissions = new RxPermissions(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_first);
        Intent intent = getIntent();
        wifiModelType = intent.getStringExtra("wifiModelType") + "";
        ButterKnife.bind(this);
        if(wifiModelType.contains("VIDEO")){
            ivImgLock.setImageResource(R.mipmap.wifi_video_lock_img_lock);
            notice.setText(getText(R.string.wifi_lock_new_add_first_notice3));
        }else{
            ivImgLock.setImageResource(R.mipmap.new_add_first);
            notice.setText(getText(R.string.wifi_lock_new_add_first_notice2));
        }

    }

    @OnClick({R.id.back, R.id.help, R.id.button_next, R.id.tv_reconnect})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.help:
                if(wifiModelType.contains("VIDEO")){
                    startActivity(new Intent(this, WifiVideoLockHelpActivity.class));
                }else{
                    startActivity(new Intent(this,WifiLockHelpActivity.class));
                }

                break;
            case R.id.button_next:
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
                if (wifiModelType != null) {
                    if (!(wifiModelType.equals("WiFi&BLE"))) {
                        //打开wifi
                        WifiUtils wifiUtils = WifiUtils.getInstance(MyApplication.getInstance());
                        if (!wifiUtils.isWifiEnable()) {
                            wifiUtils.openWifi();
                            ToastUtils.showShort(getString(R.string.philips_wifi_no_open_please_open_wifi));
                            return;
                        }
                    }
                }
                else {

                    return;

                }
                if (!WifiUtils.getInstance(MyApplication.getInstance()).isWifiEnable()) {
                    showWifiDialog();
                    WifiUtils.getInstance(MyApplication.getInstance()).openWifi();
                    return;
                }
                if (!GpsUtil.isOPen(MyApplication.getInstance())) {
                    GpsUtil.openGPS(MyApplication.getInstance());
                    showLocationPermission();
//                    Toast.makeText(this, getString(R.string.locak_no_open_please_open_local), Toast.LENGTH_SHORT).show();
                    return;
                }
                LogUtils.d("--Kaadas--wifiModelType=="+wifiModelType);
//                startActivity(new Intent(this,WifiLockAddNewSecondActivity.class));
                Intent wifiIntent = new Intent(this, WifiLockAddNewSecondActivity.class);
                wifiIntent.putExtra("wifiModelType", wifiModelType);
                startActivity(wifiIntent);
                break;
            case R.id.tv_reconnect:
                if(wifiModelType.contains("VIDEO")){
                    Intent thirdIntent = new Intent(this, WifiLockAddNewThirdActivity.class);
                    thirdIntent.putExtra("wifiModelType", wifiModelType);
                    thirdIntent.putExtra("distribution", true);
                    startActivity(thirdIntent);
                }else {
                    //startActivity(new Intent(this,WifiLockOldUserFirstActivity.class));
                    Intent reconnectWifiIntent = new Intent(this, WifiLockOldUserFirstActivity.class);
                    reconnectWifiIntent.putExtra("wifiModelType", wifiModelType);
                    startActivity(reconnectWifiIntent);
                }
                break;
        }
    }

    private void showWifiDialog() {
        AlertDialogUtil.getInstance().noEditTitleOneButtonDialog(
                WifiLockAddNewFirstActivity.this,
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
        AlertDialogUtil.getInstance().havaNoEditTwoButtonDialog(
                WifiLockAddNewFirstActivity.this
                , getString(R.string.dialog_wifi_video_tip),
                getString(R.string.philips_activity_wifi_lock_new_add_first),
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
}
