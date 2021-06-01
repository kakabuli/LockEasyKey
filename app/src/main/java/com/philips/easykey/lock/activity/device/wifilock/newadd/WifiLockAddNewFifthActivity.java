package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.espressif.iot.esptouch.util.TouchNetUtil;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;


public class WifiLockAddNewFifthActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    TextView notice;
    ImageView ivAnim;
    TextView buttonNext;

    private String wifiModelType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_fifth);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);
        ivAnim = findViewById(R.id.iv_anim);
        buttonNext = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> startActivity(new Intent(this,WifiLockHelpActivity.class)));
        buttonNext.setOnClickListener(v -> {
            //在连接前   保存密码
            saveWifiName();
            LogUtils.d("--Kaadas--wifiModelType==：" + wifiModelType);

            if(wifiModelType == null || wifiModelType.equals("WiFi")){
                startActivity(new Intent(this,WifiLockAddNewScanActivity.class));
            }
            else if(wifiModelType.equals("WiFi&BLE")){
                //新流程
                startActivity(new Intent(this,WifiLockAddNewScanBLEActivity.class));
            }
            else {
                ToastUtils.showShort(getString(R.string.philips_activity_wifi_lock_add_new_fifth_1));

            }
        });

        Intent intent = getIntent();
        wifiModelType = intent.getStringExtra("wifiModelType");

        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();
    }


    private void saveWifiName() {
        WifiManager wifiMgr = (WifiManager) MyApplication.getInstance().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifiMgr.getConnectionInfo();
        String ssid = info != null ? info.getSSID() : null;
        LogUtils.d("--Kaadas--获取到的ssid：" + ssid);
        if (TextUtils.isEmpty(ssid)) {
            SPUtils.put(KeyConstants.WIFI_LOCK_CONNECT_NAME, "");
            return;
        }
        if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
            ssid = ssid.substring(1, ssid.length() - 1);
        }
        if (!ssid.equals("kaadas_AP") && !"<unknown ssid>".equals(ssid)) {
            SPUtils.put(KeyConstants.WIFI_LOCK_CONNECT_NAME, ssid);
            byte[] ssidOriginalData = TouchNetUtil.getOriginalSsidBytes(info);
            LogUtils.d("获取到的   byte数据是    " + Rsa.bytesToHexString(ssidOriginalData));
            SPUtils.put(KeyConstants.WIFI_LOCK_CONNECT_ORIGINAL_DATA, Rsa.bytesToHexString(ssidOriginalData) + "");
        }
        else if(ssid.equals("kaadas_AP")){

        }
        else {
            SPUtils.put(KeyConstants.WIFI_LOCK_CONNECT_NAME, "");
        }

    }
}
