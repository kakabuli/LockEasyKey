package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.espressif.iot.esptouch.util.TouchNetUtil;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.videolock.WifiVideoLockFourthActivity;
import com.philips.easykey.lock.activity.device.videolock.WifiVideoLockHelpActivity;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;

public class WifiLockAddNewThirdActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    TextView notice;
    ImageView ivAnim;
    TextView alreadyModify;
    TextView notModify;
    TextView notModify1;

    private String wifiModelType;

    private boolean distributionAgain;

    private boolean distribution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_third);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);
        ivAnim = findViewById(R.id.iv_anim);
        alreadyModify = findViewById(R.id.already_modify);
        notModify = findViewById(R.id.not_modify);
        notModify1 = findViewById(R.id.not_modify_1);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> {
            if(wifiModelType.contains("VIDEO")){
                startActivity(new Intent(this, WifiVideoLockHelpActivity.class));
            }else{
                startActivity(new Intent(this,WifiLockHelpActivity.class));
            }
        });
        alreadyModify.setOnClickListener(v -> {
            if(wifiModelType.contains("VIDEO")){
                saveWifiName();
                Intent wifiVideoIntent = new Intent(this, WifiVideoLockFourthActivity.class);
                wifiVideoIntent.putExtra("wifiModelType", wifiModelType);
                wifiVideoIntent.putExtra("distribution_again",distributionAgain);
                wifiVideoIntent.putExtra("distribution",distribution);
                startActivity(wifiVideoIntent);
            }else{
                //startActivity(new Intent(this,WifiLockAddNewfourthActivity.class));
                Intent wifiIntent = new Intent(this, WifiLockAddNewfourthActivity.class);
                wifiIntent.putExtra("wifiModelType", wifiModelType);
                startActivity(wifiIntent);
            }
        });
        notModify.setOnClickListener(v -> {
            //                startActivity(new Intent(this,WifiLockAddNewThird2Activity.class));
            Intent UnModifyWifiIntent = new Intent(this, WifiLockAddNewThird2Activity.class);
            UnModifyWifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(UnModifyWifiIntent);
        });
        notModify1.setOnClickListener(v -> {
            //                startActivity(new Intent(this,WifiLockAddNewThird2Activity.class));
            Intent UnModifyWifiIntent = new Intent(this, WifiLockAddNewThird2Activity.class);
            UnModifyWifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(UnModifyWifiIntent);
        });

        Intent intent = getIntent();
        wifiModelType = intent.getStringExtra("wifiModelType");
        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();
        distributionAgain = intent.getBooleanExtra("distribution_again",false);
        distribution = intent.getBooleanExtra("distribution",false);
        if(wifiModelType.contains("VIDEO")){
            if(distributionAgain){
                head.setText(R.string.wifi_lock_add_new_third_notice1_2);
            }else{
                if(distribution){
                    head.setText(R.string.wifi_lock_add_new_third_notice1_3);
                }else{
                    head.setText(R.string.wifi_lock_add_new_third_notice1_1);
                }

            }
            notice.setText(R.string.dkjsfkjhad_2);
            ivAnim.setVisibility(View.GONE);
            notModify1.setVisibility(View.VISIBLE);
            notModify.setVisibility(View.GONE);
        }else {
            head.setText(R.string.wifi_lock_add_new_third_notice1);
            ivAnim.setVisibility(View.VISIBLE);
            notice.setText(R.string.dkjsfkjhad);
            notModify1.setVisibility(View.GONE);
            notModify.setVisibility(View.VISIBLE);
        }
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
            SPUtils.put(KeyConstants.WIFI_LOCK_CONNECT_ORIGINAL_DATA, Rsa.bytesToHexString(ssidOriginalData));
        }
        else if(ssid.equals("kaadas_AP")){

        }
        else {
            SPUtils.put(KeyConstants.WIFI_LOCK_CONNECT_NAME, "");
        }

    }
}
