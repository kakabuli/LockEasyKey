package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PhilipsDuressBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.PhilipsSettingDuressAlarmPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IPhilipsSettingDuressAlarm;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PhilipsWifiVideoLockSettingDuressAlarmAvtivity extends BaseActivity<IPhilipsSettingDuressAlarm,
        PhilipsSettingDuressAlarmPresenter<IPhilipsSettingDuressAlarm>> implements IPhilipsSettingDuressAlarm {

    private ConstraintLayout rlDuressAlarmShow;
    private ImageView mBack;
    private ImageView mIvDuressAlarmHelp;
    private RelativeLayout mRlDuressAlarmAppReceiver;
    private ImageView mIvDuressSelect;
    private TextView mTvNum;
    private TextView mTvDuressDate;
    private String wifiSn = "";
    private String duressAccount = "";
    private int duressSwitch;
    private int position;
    private PhilipsDuressBean mPhilipsDuressBean;


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
        position = getIntent().getIntExtra(KeyConstants.DURESS_PASSWORD_POSITION_INfO,-1);
        mPhilipsDuressBean = (PhilipsDuressBean) getIntent().getSerializableExtra(KeyConstants.DURESS_PASSWORD_INfO);
        if(mPhilipsDuressBean != null){
            String sNum = mPhilipsDuressBean.getNum() > 9 ? "" + mPhilipsDuressBean.getNum() : "0" + mPhilipsDuressBean.getNum();
            mTvNum.setText(mPhilipsDuressBean.getNickName().isEmpty() ? getString(R.string.philips_duress_number,sNum) : mPhilipsDuressBean.getNickName());
            mTvDuressDate.setText(DateUtils.getDayTimeFromMillisecond(mPhilipsDuressBean.getCreateTime() * 1000));

            if(mPhilipsDuressBean.getPwdDuressSwitch() == 0){
                rlDuressAlarmShow.setVisibility(View.GONE);
                mIvDuressSelect.setSelected(false);
                duressSwitch = 0;
            }else{
                rlDuressAlarmShow.setVisibility(View.VISIBLE);
                mIvDuressSelect.setSelected(true);
                duressSwitch = 1;
            }
        }
    }

    @Override
    protected PhilipsSettingDuressAlarmPresenter<IPhilipsSettingDuressAlarm> createPresent() {
        return new PhilipsSettingDuressAlarmPresenter<>();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            setDuressAlarm();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initListener() {

        mBack.setOnClickListener(v -> {
            setDuressAlarm();
        });

        mRlDuressAlarmAppReceiver.setOnClickListener(v -> {
            mPhilipsDuressBean.setPwdDuressSwitch(duressSwitch);
            Intent intent = new Intent(this,PhilipsWifiVideoLockSettingDuressAlarmReceiverAvtivity.class);
            intent.putExtra(KeyConstants.DURESS_PASSWORD_INfO, mPhilipsDuressBean);
            intent.putExtra(KeyConstants.DURESS_PASSWORD_POSITION_INfO,position);
            startActivityForResult(intent,KeyConstants.WIFI_LOCK_SETTING_DURESS_ACCOUNT);
            finish();
        });

        mIvDuressSelect.setOnClickListener(v -> {
            if(mIvDuressSelect.isSelected()){
                mIvDuressSelect.setSelected(false);
                rlDuressAlarmShow.setVisibility(View.GONE);
                duressSwitch = 0;
            }else{
                mIvDuressSelect.setSelected(true);
                rlDuressAlarmShow.setVisibility(View.VISIBLE);
                duressSwitch = 1;
            }
        });
    }

    private void setDuressAlarm() {
        if(mPhilipsDuressBean == null) {
//            ToastUtils.showShort(R.string.set_failed);
            finish();
            return;
        }

        if(duressSwitch == 1 && TextUtils.isEmpty(mPhilipsDuressBean.getDuressAlarmAccount())){
            ToastUtils.showShort(R.string.philips_set_duress_account);
            return;
        }

        finish();
        /*if(mDuressBean.getPwdDuressSwitch() == duressSwitch && mDuressBean.getDuressAlarmAccount() == duressAccount){
            ToastUtils.showShort(R.string.set_failed);
            finish();
            return;
        }*/

//        mPresenter.setDuressPwdAlarm(mDuressBean.getWifiSN(),mDuressBean.getPwdType(),mDuressBean.getNum(),duressSwitch);

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
        rlDuressAlarmShow = findViewById(R.id.rl_duress_alarm_show);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == KeyConstants.WIFI_LOCK_SETTING_DURESS_ACCOUNT){
                duressAccount = data.getStringExtra(KeyConstants.DURESS_AUTHORIZATION_TELEPHONE);
            }
        }
    }

    @Override
    public void onSettingDuressAccount(BaseResult baseResult) {

    }

    @Override
    public void onSettingDuress(BaseResult baseResult) {
        Intent intent = new Intent(this,PhilipsWifiVideoLockDuressAlarmAvtivity.class);
        intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
        intent.putExtra(KeyConstants.DURESS_PASSWORD_POSITION_INfO,position);
        intent.putExtra("duress_alarm_toggle", mIvDuressSelect.isSelected() ? 1 : 0);
        intent.putExtra("duress_alarm_phone", duressAccount);
        setResult(RESULT_OK,intent);
        finish();
    }
}