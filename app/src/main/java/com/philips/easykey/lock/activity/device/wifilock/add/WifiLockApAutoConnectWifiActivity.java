package com.philips.easykey.lock.activity.device.wifilock.add;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.GpsUtil;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.WifiUtil;
import com.philips.easykey.lock.utils.WifiUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class WifiLockApAutoConnectWifiActivity extends BaseAddToApplicationActivity {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.help)
    ImageView help;
    private Handler handler = new Handler();

    private Disposable permissionDisposable;
    final RxPermissions rxPermissions = new RxPermissions(this);
    private Disposable scanDisposable1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_ap_auto_connect_wifi);
        ButterKnife.bind(this);

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
        WifiUtil.getIns().init(getApplicationContext());
          WifiUtil.getIns().changeToWifi("kaadas_AP", "88888888");
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        registerReceiver(mReceiver, filter);
        handler.postDelayed(timeoutRunnable, 15 * 1000);
    }

    private Runnable timeoutRunnable = new Runnable() {
        @Override
        public void run() {
            LogUtils.d("连接失败");
            finish();
            Intent intent = new Intent(WifiLockApAutoConnectWifiActivity.this, WifiLockNoticeUserLinkWifiFirstActivity.class);
            startActivity(intent);
        }
    };

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }
            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    handler.postDelayed(runnable, 2000);
                    break;
            }
        }
    };

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
            assert wifiManager != null;
            onWifiChanged(wifiManager.getConnectionInfo());
        }
    };


    private void onWifiChanged(WifiInfo info) {
        boolean disconnected = info == null
                || info.getNetworkId() == -1
                || "<unknown ssid>".equals(info.getSSID());
        if (disconnected) {
            LogUtils.d("网络切换   断开 ");
        } else {
            String ssid = info.getSSID();
            if (TextUtils.isEmpty(ssid)) {
                return;
            }
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            LogUtils.d("网络切换    " + ssid + "   " + "网络可用   " + NetUtil.isNetworkAvailable());

            if ((ssid.equals("kaadas_AP"))) {
                if (scanDisposable1!=null){
                    scanDisposable1.dispose();
                }
                handler.removeCallbacks(runnable);
                startActivity(new Intent(WifiLockApAutoConnectWifiActivity.this, WifiLockApInputAdminPasswordActivity.class));
                handler.removeCallbacks(timeoutRunnable);
                finish();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        if (permissionDisposable != null) {
            permissionDisposable.dispose();
        }
        if (scanDisposable1!=null){
            scanDisposable1.dispose();
        }
    }

    @Override
    public void onBackPressed() {
        ToastUtils.showShort(getString(R.string.is_connect_wifi_please_wait));
    }

    @OnClick(R.id.help)
    public void onClick() {
        startActivity(new Intent(this, WifiLockHelpActivity.class));
    }


}
