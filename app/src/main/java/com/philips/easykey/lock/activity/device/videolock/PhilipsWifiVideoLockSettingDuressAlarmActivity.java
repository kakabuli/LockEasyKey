package com.philips.easykey.lock.activity.device.videolock;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class PhilipsWifiVideoLockSettingDuressAlarmActivity extends BaseActivity<IPhilipsSettingDuressAlarm,
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

    private Dialog duressAlarmHelpDialog;


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
            showDuressAlarmBackDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initListener() {

        mBack.setOnClickListener(v -> {
            showDuressAlarmBackDialog();
        });

        mRlDuressAlarmAppReceiver.setOnClickListener(v -> {
            mPhilipsDuressBean.setPwdDuressSwitch(duressSwitch);
            Intent intent = new Intent(this, PhilipsWifiVideoLockSettingDuressAlarmReceiverActivity.class);
            intent.putExtra(KeyConstants.DURESS_PASSWORD_INfO, mPhilipsDuressBean);
            intent.putExtra(KeyConstants.DURESS_PASSWORD_POSITION_INfO,position);
            startActivityForResult(intent,KeyConstants.WIFI_LOCK_SETTING_DURESS_ACCOUNT);
            finish();
        });

        mIvDuressSelect.setOnClickListener(v -> {
            setDuressAlarm();
        });

        mIvDuressAlarmHelp.setOnClickListener(v -> {
            showDuressAlarmHelpDialog();
        });
    }

    private void setDuressAlarm() {

        if(mIvDuressSelect.isSelected()){
            duressSwitch = 0;
        }else{
            duressSwitch = 1;
        }
        mPresenter.setDuressPwdAlarm(mPhilipsDuressBean.getWifiSN(),mPhilipsDuressBean.getPwdType(),mPhilipsDuressBean.getNum(),duressSwitch);

    }

    private void showDuressAlarmBackDialog(){
        AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(this, getString(R.string.philips_duress_alarm_setting_exit),
                "#333333", getString(R.string.philips_cancel), getString(R.string.query),
                "#0066A1", "#ffffff", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        Intent intent = new Intent(PhilipsWifiVideoLockSettingDuressAlarmActivity.this, PhilipsWifiVideoLockDuressAlarmActivity.class);
                        intent.putExtra(KeyConstants.DURESS_PASSWORD_INfO, mPhilipsDuressBean);
                        intent.putExtra(KeyConstants.WIFI_SN, mPhilipsDuressBean.getWifiSN());
                        intent.putExtra(KeyConstants.DURESS_PASSWORD_POSITION_INfO, position);
                        startActivity(intent);
                        mPresenter.getPasswordList(wifiSn);
                        finish();
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void showDuressAlarmHelpDialog() {
        if(duressAlarmHelpDialog == null){
            duressAlarmHelpDialog = new Dialog(this, R.style.MyDialog);
        }
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_duress_alarm_help, null);
        ImageView duressClose = mView.findViewById(R.id.duress_close);
        duressAlarmHelpDialog.setContentView(mView);

        duressClose.setOnClickListener(v -> {
            duressAlarmHelpDialog.dismiss();
        });

        Window window = duressAlarmHelpDialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams params = window.getAttributes();
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        params.width = width;
        params.height = height;
        window.setAttributes(params);

        if (!PhilipsWifiVideoLockSettingDuressAlarmActivity.this.isFinishing()) {
            duressAlarmHelpDialog.show();
        }
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
        if("200".equals(baseResult.getCode() + "")){
            if(mIvDuressSelect.isSelected()){
                mIvDuressSelect.setSelected(false);
                rlDuressAlarmShow.setVisibility(View.GONE);
            }else{
                mIvDuressSelect.setSelected(true);
                rlDuressAlarmShow.setVisibility(View.VISIBLE);
            }
            mPhilipsDuressBean.setPwdDuressSwitch(duressSwitch);
//            ToastUtils.showShort(R.string.set_success);
        }else if("303".equals(baseResult.getCode())){
            AlertDialogUtil.getInstance().PhilipsSingleButtonDialog(this, "",
                    getString(R.string.philips_code_security_key_full),getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener(){
                @Override
                public void left() {

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
        else{
            ToastUtils.showShort(R.string.set_failed);
            Intent intent = new Intent(this, PhilipsWifiVideoLockDuressAlarmActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, mPhilipsDuressBean.getWifiSN());
            startActivity(intent);
            finish();
        }
    }
}