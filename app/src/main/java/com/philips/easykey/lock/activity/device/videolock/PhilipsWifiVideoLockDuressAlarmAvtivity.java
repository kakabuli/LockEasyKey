package com.philips.easykey.lock.activity.device.videolock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.utils.KeyConstants;

public class PhilipsWifiVideoLockDuressAlarmAvtivity extends AppCompatActivity {

    private TextView mTvPassword;
    private TextView mTvPasswordNum;
    private RelativeLayout mRlDuressAlarmPasswordToggle;
    private TextView mTvDuressAlarmPasswordToggle;
    private RelativeLayout mRlDuressAlarmPasswordNotification;
    private TextView mTvDuressAlarmPasswordPhone;

    private TextView mTvFingeprint;
    private TextView mTvFingeprintNum;
    private RelativeLayout mRlDuressAlarmFingeprintToggle;
    private TextView mTvDuressAlarmFingeprintToggle;
    private RelativeLayout mRlDuressAlarmFingeprintNotification;
    private TextView mTvDuressAlarmFingeprintPhone;


    private ImageView mIvDuressSelect;
    private ImageView mBack;
    private String wifiSn = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_duress_alarm);

        initView();
        initListener();
        initData();
    }

    private void initData() {
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);

        if(mIvDuressSelect.isSelected()){
            setPasswordVisibility(View.GONE);
            setFingeprintVisibility(View.GONE);
            mIvDuressSelect.setSelected(false);
        }else{
            setPasswordVisibility(View.VISIBLE);
            setFingeprintVisibility(View.VISIBLE);
            mIvDuressSelect.setSelected(true);
        }
    }

    private void initListener() {
        mIvDuressSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected()){
                    setPasswordVisibility(View.GONE);
                    setFingeprintVisibility(View.GONE);
                    mIvDuressSelect.setSelected(false);
                }else{
                    setPasswordVisibility(View.VISIBLE);
                    setFingeprintVisibility(View.VISIBLE);
                    mIvDuressSelect.setSelected(true);
                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRlDuressAlarmPasswordToggle.setOnClickListener(v -> {
            Intent intent = new Intent(this,PhilipsWifiVideoLockSettingDuressAlarmAvtivity.class);
            startActivity(intent);
        });

        mRlDuressAlarmFingeprintToggle.setOnClickListener(v -> {
            Intent intent = new Intent(this,PhilipsWifiVideoLockSettingDuressAlarmAvtivity.class);
            startActivity(intent);
        });
    }

    private void initView() {
        mIvDuressSelect = findViewById(R.id.iv_duress_select);
        mBack = findViewById(R.id.back);
        mTvPassword = findViewById(R.id.tv_password);
        mTvPasswordNum = findViewById(R.id.tv_password_num);
        mRlDuressAlarmPasswordToggle = findViewById(R.id.rl_duress_alarm_toggle);
        mTvDuressAlarmPasswordToggle = findViewById(R.id.tv_duress_alarm_toggle);
        mRlDuressAlarmPasswordNotification = findViewById(R.id.rl_duress_alarm_password_notification);
        mTvDuressAlarmPasswordPhone = findViewById(R.id.tv_duress_alarm_phone);
        mTvFingeprint = findViewById(R.id.tv_fingeprint);
        mTvFingeprintNum = findViewById(R.id.tv_fingeprint_num);
        mRlDuressAlarmFingeprintToggle = findViewById(R.id.rl_duress_alarm_fingeprint_toggle);
        mTvDuressAlarmFingeprintToggle = findViewById(R.id.tv_duress_alarm_fingeprint_toggle);
        mRlDuressAlarmFingeprintNotification = findViewById(R.id.rl_duress_alarm_fingeprint_notification);
        mTvDuressAlarmFingeprintPhone = findViewById(R.id.tv_duress_alarm_fingeprint_phone);

    }

    private void setFingeprintVisibility(int visible){
        if(visible == View.GONE){
            mTvFingeprint.setVisibility(View.GONE);
            mTvFingeprintNum.setVisibility(View.GONE);
            mRlDuressAlarmFingeprintToggle.setVisibility(View.GONE);
            mRlDuressAlarmFingeprintNotification.setVisibility(View.GONE);
        }else if(visible == View.VISIBLE){
            mTvFingeprint.setVisibility(View.VISIBLE);
            mTvFingeprintNum.setVisibility(View.VISIBLE);
            mRlDuressAlarmFingeprintToggle.setVisibility(View.VISIBLE);
            mRlDuressAlarmFingeprintNotification.setVisibility(View.VISIBLE);
        }
    }

    private void setPasswordVisibility(int visible){
        if(visible == View.GONE){
            mTvPassword.setVisibility(View.GONE);
            mTvPasswordNum.setVisibility(View.GONE);
            mRlDuressAlarmPasswordToggle.setVisibility(View.GONE);
            mRlDuressAlarmPasswordNotification.setVisibility(View.GONE);
        }else if(visible == View.VISIBLE){
            mTvPassword.setVisibility(View.VISIBLE);
            mTvPasswordNum.setVisibility(View.VISIBLE);
            mRlDuressAlarmPasswordToggle.setVisibility(View.VISIBLE);
            mRlDuressAlarmPasswordNotification.setVisibility(View.VISIBLE);
        }
    }
}