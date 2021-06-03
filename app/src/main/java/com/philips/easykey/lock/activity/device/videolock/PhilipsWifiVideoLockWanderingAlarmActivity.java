package com.philips.easykey.lock.activity.device.videolock;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockWanderingAlarmPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IWifiVideoLockWanderingAlarmView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;


public class PhilipsWifiVideoLockWanderingAlarmActivity extends BaseActivity<IWifiVideoLockWanderingAlarmView, WifiVideoLockWanderingAlarmPresenter<IWifiVideoLockWanderingAlarmView>>
        implements IWifiVideoLockWanderingAlarmView  {

    ImageView back;
    RelativeLayout rlWanderingPIRSensitivity;
    RelativeLayout rlWanderingJudgeTime;
    ImageView ivWanderingAlarm;
    TextView tvWanderingPirSensitivityRight;
    TextView tvWanderingJudgeTimeRight;
    RelativeLayout rlWanderingAlarm;
    Button mBtnRestore;
    AVLoadingIndicatorView avi;
    TextView tvTips;

    private String wifiSn;
    private WifiLockInfo wifiLockInfo;

    private InnerRecevier mInnerRecevier = null;

    private Dialog dialog;

    private int pirSen;
    private int stayTime;

    private int stayStatus = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_wandering_alarm);

        back = findViewById(R.id.back);
        rlWanderingPIRSensitivity = findViewById(R.id.rl_wandering_pir_sensitivity);
        rlWanderingJudgeTime = findViewById(R.id.rl_wandering_judge_time);
        ivWanderingAlarm = findViewById(R.id.iv_wandering_alarm);
        tvWanderingPirSensitivityRight = findViewById(R.id.tv_wandering_pir_sensitivity_right);
        tvWanderingJudgeTimeRight = findViewById(R.id.tv_wandering_judge_time_right);
        rlWanderingAlarm = findViewById(R.id.rl_wandering_alarm);
        mBtnRestore = findViewById(R.id.btn_restore);
        avi = findViewById(R.id.avi);
        tvTips = findViewById(R.id.tv_tips);

        back.setOnClickListener(v -> {
            if (wifiLockInfo.getPowerSave() == 0){
                if(avi.isShow()) setWanderingAlarm();
            } else {
                finish();
            }
        });
        rlWanderingPIRSensitivity.setOnClickListener(v -> {
            if(avi.isShow()){
                Intent intent = new Intent(this, PhilipsWifiVideoLockWanderingPIRSensitivityActivity.class);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                intent.putExtra(KeyConstants.WIFI_VIDEO_WANDERING_SENSITIVITY,pirSen);
                startActivityForResult(intent,KeyConstants.WIFI_VIDEO_LOCK_WANDERING_SENSITIVITY_CODE);
                mPresenter.release();
            }
        });
        rlWanderingJudgeTime.setOnClickListener(v -> {
            if(avi.isShow()){
                Intent wanderingJudeTimeIntent = new Intent(this, PhilipsWifiVideoLockWanderingJudgeTimeActivity.class);
                wanderingJudeTimeIntent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                wanderingJudeTimeIntent.putExtra(KeyConstants.WIFI_VIDEO_WANDERING_TIME,stayTime);
                startActivityForResult(wanderingJudeTimeIntent,KeyConstants.WIFI_VIDEO_LOCK_WANDERING_STAY_TIME_CODE);
                mPresenter.release();
            }
        });
        rlWanderingAlarm.setOnClickListener(v -> {
            if(avi.isShow()){
                if(wifiLockInfo.getPowerSave() == 1){
                    if(wifiLockInfo.getPower() < 30){
                        powerSaveModeStatus();
                        return;
                    }
                    powerStatusDialog();
                    return;
                }
                if(ivWanderingAlarm.isSelected()){
                    ivWanderingAlarm.setSelected(false);
                    rlWanderingPIRSensitivity.setVisibility(View.GONE);
                    rlWanderingJudgeTime.setVisibility(View.GONE);
                    mBtnRestore.setVisibility(View.GONE);
                    stayStatus = 0;
                }else{
                    ivWanderingAlarm.setSelected(true);
                    rlWanderingPIRSensitivity.setVisibility(View.VISIBLE);
                    rlWanderingJudgeTime.setVisibility(View.VISIBLE);
                    mBtnRestore.setVisibility(View.VISIBLE);
                    stayStatus = 1;
                }
            }
        });
        ivWanderingAlarm.setOnClickListener(v -> {
            if(avi.isShow()){
                if(wifiLockInfo.getPowerSave() == 1){
                    if(wifiLockInfo.getPower() < 30){
                        powerSaveModeStatus();
                        return;
                    }
                    powerStatusDialog();
                    return;
                }
                if(ivWanderingAlarm.isSelected()){
                    ivWanderingAlarm.setSelected(false);
                    rlWanderingPIRSensitivity.setVisibility(View.GONE);
                    rlWanderingJudgeTime.setVisibility(View.GONE);
                    mBtnRestore.setVisibility(View.GONE);
                    stayStatus = 0;
                }else{
                    ivWanderingAlarm.setSelected(true);
                    rlWanderingPIRSensitivity.setVisibility(View.VISIBLE);
                    rlWanderingJudgeTime.setVisibility(View.VISIBLE);
                    mBtnRestore.setVisibility(View.VISIBLE);
                    stayStatus = 1;
                }
            }
        });
        mBtnRestore.setOnClickListener(v -> {
            tvWanderingPirSensitivityRight.setText(R.string.activity_wifi_video_wamdering_midd);
            pirSen = KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_2;
            stayTime = 10;
            tvWanderingJudgeTimeRight.setText(stayTime + getString(R.string.activity_wifi_video_wamdering_sceond));
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);


        initData();
    }

    private void initData() {
        if(wifiLockInfo != null){
            mPresenter.settingDevice(wifiLockInfo);
            stayStatus = wifiLockInfo.getStay_status();
            if(wifiLockInfo.getStay_status() == 1){
                ivWanderingAlarm.setSelected(true);
                rlWanderingPIRSensitivity.setVisibility(View.VISIBLE);
                rlWanderingJudgeTime.setVisibility(View.VISIBLE);
                mBtnRestore.setVisibility(View.VISIBLE);
            }else if(wifiLockInfo.getStay_status() ==0){
                ivWanderingAlarm.setSelected(false);
                rlWanderingPIRSensitivity.setVisibility(View.GONE);
                rlWanderingJudgeTime.setVisibility(View.GONE);
                mBtnRestore.setVisibility(View.GONE);
            }

            if(wifiLockInfo.getSetPir() != null){
                stayTime = wifiLockInfo.getSetPir().getStay_time();
                if(wifiLockInfo.getSetPir().getStay_time() < 10 || wifiLockInfo.getSetPir().getStay_time() > 60){
                    tvWanderingJudgeTimeRight.setText("30" + getString(R.string.activity_wifi_video_wamdering_sceond));
                }else {
                    tvWanderingJudgeTimeRight.setText(wifiLockInfo.getSetPir().getStay_time() + getString(R.string.activity_wifi_video_wamdering_sceond));
                }

                pirSen = wifiLockInfo.getSetPir().getPir_sen();
                if(pirSen <= KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_1 ){
                    tvWanderingPirSensitivityRight.setText(getString(R.string.activity_wifi_video_wamdering_low));
                }else if(pirSen <= KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_2){
                    tvWanderingPirSensitivityRight.setText(getString(R.string.activity_wifi_video_wamdering_midd));
                }else if(pirSen <= KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_3){
                    tvWanderingPirSensitivityRight.setText(getString(R.string.activity_wifi_video_wamdering_high));
                }

            }

        }
    }

    private void setWanderingAlarm() {
        if(ivWanderingAlarm.isSelected()){
            stayStatus = 1;
        }else{
            stayStatus = 0;
        }

        if(wifiLockInfo.getStay_status() == 0 && stayStatus == 0){
            finish();
            return;
        }

        try {
            if(stayTime!= wifiLockInfo.getSetPir().getStay_time() || stayStatus != wifiLockInfo.getStay_status() || pirSen != wifiLockInfo.getSetPir().getPir_sen()){
                tvTips.setVisibility(View.VISIBLE);
                avi.setVisibility(View.VISIBLE);
                avi.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.setConnectWanderingAlarm(wifiSn,stayStatus,stayTime,pirSen);
                    }
                }).start();
            }else {
                finish();
            }
        }catch (Exception e){

        }


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(wifiLockInfo.getPowerSave() == 0){
                if(avi.isShow())
                    setWanderingAlarm();
            }else {

                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
        if(avi != null){
            avi.hide();
            tvTips.setVisibility(View.GONE);
        }
        registerBroadcast();
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

    @Override
    public void finish() {
        super.finish();
        mPresenter.release();
    }


    @Override
    protected WifiVideoLockWanderingAlarmPresenter<IWifiVideoLockWanderingAlarmView> createPresent() {
        return new WifiVideoLockWanderingAlarmPresenter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
            wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
            switch (requestCode){
                case KeyConstants.WIFI_VIDEO_LOCK_WANDERING_SENSITIVITY_CODE:
                    if(data.getIntExtra(KeyConstants.WIFI_VIDEO_WANDERING_SENSITIVITY,-1) == -1){

                    }else{
                        pirSen = data.getIntExtra(KeyConstants.WIFI_VIDEO_WANDERING_SENSITIVITY,-1);
                        if(pirSen <= KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_1 ){
                            tvWanderingPirSensitivityRight.setText(getString(R.string.activity_wifi_video_wamdering_low));
                        }else if(pirSen <= KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_2){
                            tvWanderingPirSensitivityRight.setText(getString(R.string.activity_wifi_video_wamdering_midd));
                        }else if(pirSen <= KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_3){
                            tvWanderingPirSensitivityRight.setText(getString(R.string.activity_wifi_video_wamdering_high));
                        }
                    }

                    break;
                case KeyConstants.WIFI_VIDEO_LOCK_WANDERING_STAY_TIME_CODE:
                    if(data.getIntExtra(KeyConstants.WIFI_VIDEO_WANDERING_TIME,-1) == -1){

                    }else{
                        stayTime = data.getIntExtra(KeyConstants.WIFI_VIDEO_WANDERING_TIME,-1);
                        tvWanderingJudgeTimeRight.setText(stayTime + getString(R.string.activity_wifi_video_wamdering_sceond));
                    }

                    break;
            }
        }
    }

    private void registerBroadcast(){
        if(mInnerRecevier == null){
            mInnerRecevier = new InnerRecevier();
        }
        IntentFilter homeFilter = new IntentFilter();
        homeFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        homeFilter.addAction(Intent.ACTION_SCREEN_ON);
        homeFilter.addAction(Intent.ACTION_SCREEN_OFF);
        homeFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mInnerRecevier, homeFilter);
    }

    private void unRegisterBroadcast(){
        if(mInnerRecevier != null){
            unregisterReceiver(mInnerRecevier);
        }
    }

    private class InnerRecevier extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        // home键
                        mPresenter.release();
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        //多任务
                        mPresenter.release();
                    }
                }
            }else if(action.equals(Intent.ACTION_SCREEN_ON)){
            }else if(action.equals(Intent.ACTION_SCREEN_OFF)){
                mPresenter.release();
            }else if(action.equals(Intent.ACTION_USER_PRESENT)){// 解锁

            }

        }
    }

    public void powerStatusDialog(){
        AlertDialogUtil.getInstance().PhilipsSingleButtonDialog(this, getString(R.string.philips_deviceinfo__power_save_mode),"",
                getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
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

    @Override
    public void onDeleteDeviceSuccess() {

    }

    @Override
    public void onDeleteDeviceFailed(Throwable throwable) {

    }

    @Override
    public void onDeleteDeviceFailedServer(BaseResult result) {

    }

    @Override
    public void modifyDeviceNicknameSuccess() {

    }

    @Override
    public void modifyDeviceNicknameError(Throwable throwable) {

    }

    @Override
    public void modifyDeviceNicknameFail(BaseResult baseResult) {

    }

    @Override
    public void onUpdatePushStatusSuccess(int status) {

    }

    @Override
    public void onUpdatePushStatusFailed(BaseResult result) {

    }

    @Override
    public void onUpdatePushStatusThrowable(Throwable throwable) {

    }

    @Override
    public void onWifiLockActionUpdate() {

    }

    @Override
    public void noNeedUpdate() {

    }

    @Override
    public void snError() {

    }

    @Override
    public void dataError() {

    }

    @Override
    public void needUpdate(CheckOTAResult.UpdateFileInfo appInfo, String SN, int type) {

    }

    @Override
    public void readInfoFailed(Throwable throwable) {

    }

    @Override
    public void unknowError(String errorCode) {

    }

    @Override
    public void uploadSuccess(int type) {

    }

    @Override
    public void uploadFailed() {

    }

    @Override
    public void onSettingCallBack(boolean flag) {
        if(!PhilipsWifiVideoLockWanderingAlarmActivity.this.isFinishing()){
            mPresenter.setMqttCtrl(0);
            mPresenter.handler.post(new Runnable() {
                @Override
                public void run() {
                    if(flag){
                        ToastUtils.showLong(getString(R.string.modify_success));
                        Intent intent = new Intent();
                        intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_WANDERING_ALARM,stayStatus);
                        setResult(RESULT_OK,intent);
                    }else{
                        ToastUtils.showLong(getString(R.string.modify_failed));
                    }
                    finish();
                }
            });
        }
    }

    public void powerSaveModeStatus(){
        AlertDialogUtil.getInstance().titleTwoButtonPhilipsDialog(this, getString(R.string.philips_videolock_power_save_mode_title),
                getString(R.string.philips_videolock_power_save_mode_content),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm),
                "#0066A1","#FFFFFF",new AlertDialogUtil.ClickListener() {
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
}
