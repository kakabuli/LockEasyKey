package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDuressBean;
import com.philips.easykey.lock.publiclibrary.ota.ble.p6.OTAFirmwareUpdate.OTAFlashRowModel_v1;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PhilipsWifiVideoLockSettingDuressAlarmAvtivity extends AppCompatActivity {


    private ImageView mBack;
    private ImageView mIvDuressAlarmHelp;
    private RelativeLayout mRlDuressAlarmAppReceiver;
    private ImageView mIvDuressSelect;
    private TextView mTvNum;
    private TextView mTvDuressDate;
    private String wifiSn = "";
    private String duressPhone = "";
    private int duressToggle;
    private int position;
    private PhilipsDuressBean data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_setting_duress_alarm);

        initView();
        initListener();
        initData();
    }

    private void initData() {
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        position = getIntent().getIntExtra("key_position",-1);
        data = (PhilipsDuressBean) getIntent().getSerializableExtra("duress_alarm");
        if(data != null){
            mTvNum.setText(data.getNickName().isEmpty() ? data.getNum() : data.getNickName());
            mTvDuressDate.setText(DateUtils.getDayTimeFromMillisecond(data.getCreateTime() * 1000));
        }
    }

    private void initListener() {

        mBack.setOnClickListener(v -> {
            Intent intent = new Intent(this,PhilipsWifiVideoLockDuressAlarmAvtivity.class);
            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            intent.putExtra("key_position",position);
            intent.putExtra("duress_alarm_toggle", mIvDuressSelect.isSelected() ? 1 : 0);
            intent.putExtra("duress_alarm_phone",duressPhone);
            setResult(RESULT_OK,intent);
            finish();
        });
        mIvDuressAlarmHelp.setOnClickListener(v -> {
            showDuressAlarmDialog();
        });
        mRlDuressAlarmAppReceiver.setOnClickListener(v -> {
            Intent intent = new Intent(this,PhilipsWifiVideoLockSettingDuressAlarmReceiverAvtivity.class);
            startActivityForResult(intent,1003);
        });
        mIvDuressSelect.setOnClickListener(v -> {
            if(mIvDuressSelect.isSelected()){
                mIvDuressSelect.setSelected(false);
                duressToggle = 0;
            }else{
                mIvDuressSelect.setSelected(true);
                duressToggle = 1;
            }
        });
    }

    private void showDuressAlarmDialog() {

    }

    private void initView() {
        mBack = findViewById(R.id.back);
        mIvDuressSelect = findViewById(R.id.iv_duress_select);
        mIvDuressAlarmHelp = findViewById(R.id.iv_duress_alarm_help);
        mRlDuressAlarmAppReceiver = findViewById(R.id.rl_duress_alarm_app_recevice);
        mTvNum = findViewById(R.id.tv_num);
        mTvDuressDate = findViewById(R.id.tv_duress_alarm_date);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1003){
                duressPhone = data.getStringExtra("duress_alarm_phone");
            }
        }
    }
}