package com.philips.easykey.lock.activity.device.videolock;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.password.PhilipsWiFiLockPasswordManagerActivity;
import com.philips.easykey.lock.activity.device.wifilock.password.PhilipsWifiLockPasswordShareActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;

public class PhilipsWifiVideoLockPasswordTypeActivity extends BaseAddToApplicationActivity {

    private RelativeLayout mRlTypePassword;
    private RelativeLayout mRlTypeFingeprint;
    private RelativeLayout mRlTypeCard;
    private RelativeLayout mRlTypeOfflinePassword;
    private ImageView mBack;
    private String wifiSn = "";
    private WifiLockInfo mWifiLockInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_password_type);

        initView();
        initLinstener();
        initData();

    }

    private void initLinstener() {
        mBack.setOnClickListener(v -> {
            finish();
        });
        mRlTypePassword.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockPasswordTypeActivity.this, PhilipsWiFiLockPasswordManagerActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            intent.putExtra(KeyConstants.KEY_TYPE, 1);
            startActivity(intent);
        });

        mRlTypeFingeprint.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockPasswordTypeActivity.this, PhilipsWiFiLockPasswordManagerActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            intent.putExtra(KeyConstants.KEY_TYPE, 2);
            startActivity(intent);
        });

        mRlTypeCard.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockPasswordTypeActivity.this, PhilipsWiFiLockPasswordManagerActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            intent.putExtra(KeyConstants.KEY_TYPE, 3);
            startActivity(intent);
        });

        mRlTypeOfflinePassword.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockPasswordTypeActivity.this, PhilipsWifiLockPasswordShareActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            startActivity(intent);
        });
    }

    private void initView() {
        mBack = findViewById(R.id.back);
        mRlTypePassword = findViewById(R.id.rl_type_password);
        mRlTypeFingeprint = findViewById(R.id.rl_type_fingeprint);
        mRlTypeCard = findViewById(R.id.rl_type_card);
        mRlTypeOfflinePassword = findViewById(R.id.rl_offline_password);
    }

    private void initData() {
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        if(mWifiLockInfo == null) return;
        mWifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

        if(BleLockUtils.isSupportPassword(mWifiLockInfo.getFunctionSet() + "")){
            mRlTypePassword.setVisibility(View.VISIBLE);
        }else{
            mRlTypePassword.setVisibility(View.GONE);
        }

        if(BleLockUtils.isSupportCard(mWifiLockInfo.getFunctionSet() + "")){
            mRlTypeCard.setVisibility(View.VISIBLE);
        }else{
            mRlTypeCard.setVisibility(View.GONE);
        }

        if(BleLockUtils.isSupportFinger(mWifiLockInfo.getFunctionSet() + "")) {
            mRlTypeFingeprint.setVisibility(View.VISIBLE);
        }else {
            mRlTypeFingeprint.setVisibility(View.GONE);
        }

        if(BleLockUtils.isNotSupportOfflinePassword(mWifiLockInfo.getFunctionSet() + "")){
            mRlTypeOfflinePassword.setVisibility(View.GONE);
        }else{
            mRlTypeOfflinePassword.setVisibility(View.VISIBLE);
        }

    }
}