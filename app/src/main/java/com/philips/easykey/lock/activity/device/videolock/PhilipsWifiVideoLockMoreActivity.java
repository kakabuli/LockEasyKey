package com.philips.easykey.lock.activity.device.videolock;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.activity.device.wifilock.PhilipsWifiLockMessagePushActivity;
import com.philips.easykey.lock.activity.device.wifilock.PhilipsWifiLockWifiDetailActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewThirdActivity;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockMorePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IWifiVideoLockMoreView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;


public class PhilipsWifiVideoLockMoreActivity extends BaseActivity<IWifiVideoLockMoreView, WifiVideoLockMorePresenter<IWifiVideoLockMoreView>>
        implements IWifiVideoLockMoreView, View.OnClickListener {

    RelativeLayout rlDeviceName;
    RelativeLayout rlSafeMode;
    TextView ivSafeMode;
    RelativeLayout rlAm;
    TextView ivAm;
    RelativeLayout rlDoorLockLanguageSwitch;
    RelativeLayout rlVoiceQualitySetting;
    TextView tvVoiceQualitySetting;
    RelativeLayout rlDoorDirection;
    TextView tvDoorDirection;
    RelativeLayout rlOpenForce;
    TextView tvOpenForce;
    RelativeLayout rlScreenBrightness;
    TextView tvScreenBrightness;
    RelativeLayout rlScreenDuration;
    TextView tvScreenDuration;
    ImageView ivSilentMode;
    RelativeLayout rlSilentMode;
    RelativeLayout rlDeviceInformation;
    Button btnDelete;
    String name;
    TextView tvDeviceName;
    ImageView back;
    TextView tvLanguage;
    TextView headTitle;
    TextView wifiName;
    RelativeLayout rlWifiName;
    RelativeLayout rlMessagePush;
    RelativeLayout rlWanderingAlarm;
    TextView tvWanderingAlarmRight;
    RelativeLayout rlRealTimeVideo;
    RelativeLayout rlDuressAlarm;
    RelativeLayout rlSensingDoorHandle;
    TextView tvSensingDoorHandle;
    AVLoadingIndicatorView avi;
    TextView tvTips;


    private WifiLockInfo wifiLockInfo;
    private String wifiSn;
    String deviceNickname;//设备名称

    private boolean isWifiVideoLockType = false;

    private InnerRecevier mInnerRecevier = null;

    private Dialog dialog;

    private int setVolume = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_more);

        rlDeviceName = findViewById(R.id.rl_device_name);
        rlSafeMode = findViewById(R.id.rl_safe_mode);
        ivSafeMode = findViewById(R.id.iv_safe_mode);
        rlAm = findViewById(R.id.rl_am);
        ivAm = findViewById(R.id.iv_am);
        rlDoorLockLanguageSwitch = findViewById(R.id.rl_door_lock_language_switch);
        rlVoiceQualitySetting = findViewById(R.id.rl_voice_quality_setting);
        tvVoiceQualitySetting = findViewById(R.id.tv_voice_quality_setting);
        rlDoorDirection = findViewById(R.id.rl_door_direction);
        tvDoorDirection = findViewById(R.id.tv_door_direction);
        rlOpenForce = findViewById(R.id.rl_open_force);
        tvOpenForce = findViewById(R.id.tv_open_force);
        rlScreenBrightness = findViewById(R.id.rl_screen_brightness);
        tvScreenBrightness = findViewById(R.id.tv_screen_brightness);
        rlScreenDuration = findViewById(R.id.rl_screen_duration);
        tvScreenDuration = findViewById(R.id.tv_screen_duration);
        ivSilentMode = findViewById(R.id.iv_silent_mode);
        rlSilentMode = findViewById(R.id.rl_silent_mode);
        rlDeviceInformation = findViewById(R.id.rl_device_information);
        btnDelete = findViewById(R.id.btn_delete);
        tvDeviceName = findViewById(R.id.tv_device_name);
        back = findViewById(R.id.back);
        tvLanguage = findViewById(R.id.tv_language);
        headTitle = findViewById(R.id.head_title);
        wifiName = findViewById(R.id.wifi_name);
        rlWifiName = findViewById(R.id.rl_wifi_name);
        rlMessagePush = findViewById(R.id.rl_message_push);
        rlWanderingAlarm = findViewById(R.id.rl_wandering_alarm);
        tvWanderingAlarmRight = findViewById(R.id.tv_wandering_alarm_right);
        rlRealTimeVideo = findViewById(R.id.rl_real_time_video);
        rlDuressAlarm = findViewById(R.id.rl_duress_alarm);
        rlSensingDoorHandle = findViewById(R.id.rl_sensing_door_handle);
        tvSensingDoorHandle = findViewById(R.id.tv_sensing_door_handle);;

        avi = findViewById(R.id.avi);
        tvTips = findViewById(R.id.tv_tips);

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

        if(wifiLockInfo != null){
            if(MyApplication.getInstance().getWifiVideoLockTypeBySn(wifiSn) == HomeShowBean.TYPE_WIFI_VIDEO_LOCK){
                isWifiVideoLockType = true;
            }
        }
        mPresenter.init(wifiSn);
        initClick();
        initData();
    }

    @Override
    protected WifiVideoLockMorePresenter createPresent() {
        return new WifiVideoLockMorePresenter();
    }

    private void initData() {

        if (wifiLockInfo != null) {
            mPresenter.settingDevice(wifiLockInfo);
            tvDeviceName.setText(wifiLockInfo.getLockNickname());  //昵称
            setVolume = wifiLockInfo.getVolume();
            if(wifiLockInfo.getVolume() == 1){
                ivSilentMode.setSelected(true);
            }else {
                ivSilentMode.setSelected(false);
            }

            if(wifiLockInfo.getSafeMode() == 1){
                ivSafeMode.setText(getString(R.string.activity_wifi_video_safe_mode_double));
            }else{
                ivSafeMode.setText(getString(R.string.activity_wifi_video_lock_safe_mode_single));
            }
            String language = wifiLockInfo.getLanguage();
            if ("zh".equals(language)) {
                tvLanguage.setText(R.string.chinese);
            } else if ("en".equals(language)) {
                tvLanguage.setText(R.string.setting_language_en);
            }else{
                tvLanguage.setText(R.string.chinese);
            }

            String functionSet = wifiLockInfo.getFunctionSet();
            int func = 0;
            try {
                func = Integer.parseInt(functionSet);
            } catch (Exception e) {
                LogUtils.d("" + e.getMessage());
            }

            if (BleLockUtils.isSupportAMModeSet(func)) {
                rlAm.setVisibility(View.VISIBLE);
                int amMode = wifiLockInfo.getAmMode();
                ivAm.setText(amMode == 1 ? getString(R.string.wifi_video_lock_normal_mode)
                        : getString(R.string.wifi_video_lock_auto_mode));
            } else {
                rlAm.setVisibility(View.GONE);
            }

            //17静音模式设置
            if(BleLockUtils.isSupportSilentMode(wifiLockInfo.getFunctionSet())){
                rlSilentMode.setVisibility(View.VISIBLE);
            }else{
                rlSilentMode.setVisibility(View.GONE);
            }

            //88语音设置
            if(BleLockUtils.isSupportVoiceQuality(wifiLockInfo.getFunctionSet())){
                rlVoiceQualitySetting.setVisibility(View.VISIBLE);
                setVoiceQuality(wifiLockInfo.getVolLevel());
            }else{
                rlVoiceQualitySetting.setVisibility(View.GONE);
            }

            //感应把手开关显示
            if(BleLockUtils.isSupportSensingHandleSwitch(wifiLockInfo.getFunctionSet())){
                rlSensingDoorHandle.setVisibility(View.VISIBLE);
                int touchHandleStatus = wifiLockInfo.getTouchHandleStatus();
                tvSensingDoorHandle.setText(touchHandleStatus == 1 ? getString(R.string.open) : getString(R.string.close));
            }else{
                rlSensingDoorHandle.setVisibility(View.GONE);
            }

            //开门力量
            if(BleLockUtils.isSupportOpenDoorPower(func)){
                rlOpenForce.setVisibility(View.VISIBLE);
                try{
                    setOpenForce(wifiLockInfo.getOpenForce());
                }catch (Exception e){}
            }else{
                rlOpenForce.setVisibility(View.GONE);
            }

            //开门方向
            if(BleLockUtils.isSupportDoorDirection(func)){
                rlDoorDirection.setVisibility(View.VISIBLE);
                try{
                    setOpenDirection(wifiLockInfo.getOpenDirection());
                }catch (Exception e){}
            }else{
                rlDoorDirection.setVisibility(View.GONE);
            }

            if(wifiLockInfo.getStay_status() == 0){
                tvWanderingAlarmRight.setText(getString(R.string.wandering_alarm_close));
            }else if(wifiLockInfo.getStay_status() ==1){
                tvWanderingAlarmRight.setText(getString(R.string.activity_wifi_video_more_open));
            }

            //54实时视频设置
            if(BleLockUtils.isSupportRealTimeVideo(Integer.parseInt(wifiLockInfo.getFunctionSet()))){
                ((TextView)findViewById(R.id.tv_real_time_title)).setText(R.string.real_time_video);
                ((TextView)findViewById(R.id.tv_real_time)).setText("");
            }

            //93视频模式显示
            if(BleLockUtils.isSupportVideoModeSwitch(wifiLockInfo.getFunctionSet())){
                ((TextView)findViewById(R.id.tv_real_time_title)).setText(R.string.philips_video_activity_lock_real_time_video);
                ((TextView)findViewById(R.id.tv_real_time)).setCompoundDrawables(null,null,null,null);
                int KeepAliveStatus = wifiLockInfo.getPowerSave();
                ((TextView)findViewById(R.id.tv_real_time)).setText(KeepAliveStatus == 0 ? getString(R.string.open) : getString(R.string.close));
            }

            /*if(BleLockUtils.isSupportRealTimeVideo(func)){
                rlRealTimeVideo.setVisibility(View.VISIBLE);
            }else{
                rlRealTimeVideo.setVisibility(View.GONE);
            }*/

            //显示屏亮度
            if(BleLockUtils.isSupportScreenBrightness(wifiLockInfo.getFunctionSet())){
                rlScreenBrightness.setVisibility(View.VISIBLE);
                setScreenLightLevel(wifiLockInfo.getScreenLightLevel());
            }else{
                rlScreenBrightness.setVisibility(View.GONE);
            }

            //显示屏时长
            if(BleLockUtils.isSupportScreenDuration(wifiLockInfo.getFunctionSet())){
                rlScreenDuration.setVisibility(View.VISIBLE);
                if(wifiLockInfo.getScreenLightSwitch() == 0){
                    setScreenLightTime(0);
                }else {
                    setScreenLightTime(wifiLockInfo.getScreenLightTime());
                }
            }else{
                rlScreenDuration.setVisibility(View.GONE);
            }

            if(BleLockUtils.isSupportPirSetting(func)){
                rlWanderingAlarm.setVisibility(View.VISIBLE);
            }else {
                rlWanderingAlarm.setVisibility(View.GONE);
            }

            if(isWifiVideoLockType){
                rlMessagePush.setVisibility(View.VISIBLE);
            }else{
                rlMessagePush.setVisibility(View.GONE);
            }
            wifiName.setText(wifiLockInfo.getWifiName());
            deviceNickname = wifiLockInfo.getLockNickname();

        }
    }

    private void initClick() {
        back.setOnClickListener(this);
        rlDeviceName.setOnClickListener(this);
        rlSafeMode.setOnClickListener(this);
        rlAm.setOnClickListener(this);
        rlVoiceQualitySetting.setOnClickListener(this);
        rlDoorLockLanguageSwitch.setOnClickListener(this);
        rlSilentMode.setOnClickListener(this);
        rlDeviceInformation.setOnClickListener(this);
        rlMessagePush.setOnClickListener(this);
        rlWifiName.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        ivAm.setOnClickListener(this);
        rlWanderingAlarm.setOnClickListener(this);
        rlRealTimeVideo.setOnClickListener(this);
        ivSilentMode.setOnClickListener(this);
        rlDuressAlarm.setOnClickListener(this);
        rlDoorDirection.setOnClickListener(this);
        rlOpenForce.setOnClickListener(this);
        rlScreenBrightness.setOnClickListener(this);
        rlScreenDuration.setOnClickListener(this);
    }

    private void setVoiceQuality(int voiceQuality) {
        if(voiceQuality == 0){
            tvVoiceQualitySetting.setText(R.string.mute_name);
        }else if(voiceQuality == 1){
            tvVoiceQualitySetting.setText(R.string.philips_voice_quality_low);
        }else if(voiceQuality == 2){
            tvVoiceQualitySetting.setText(R.string.philips_voice_quality_high);
        }
    }

    private void setOpenDirection(int openDirection) {
        if(openDirection == 1){
            tvDoorDirection.setText(getString(R.string.wifi_lock_x9_door_direction_left));
        }else if(openDirection == 2){
            tvDoorDirection.setText(getString(R.string.wifi_lock_x9_door_direction_right));
        }
    }

    private void setOpenForce(int openForce) {
        if(openForce == 1){
            tvOpenForce.setText(getString(R.string.wifi_lock_x9_door_force_low));
        }else if(openForce == 2){
            tvOpenForce.setText(getString(R.string.wifi_lock_x9_door_force_high));
        }
    }

    private void setScreenLightTime(int screenLightTime) {
        if(/*screenLightTime <= 5*/ screenLightTime == 5){
            tvScreenDuration.setText(R.string.more_screen_light_duration_5s);
        }else if(/*screenLightTime > 5 && screenLightTime <= 10*/ screenLightTime == 10){
            tvScreenDuration.setText(R.string.more_screen_light_duration_10s);
        }else if(/*screenLightTime > 10 && screenLightTime <= 15*/ screenLightTime == 15){
            tvScreenDuration.setText(R.string.more_screen_light_duration_15s);
        }else  if(/*screenLightTime > 10 && screenLightTime <= 15*/ screenLightTime == 0){
            tvScreenDuration.setText(R.string.close);
        }
    }

    private void setScreenLightLevel(int screenLightLevel) {
        if(/*screenLightLevel <= 30*/screenLightLevel == 30){
            tvScreenBrightness.setText(R.string.low);
        }else if(/*screenLightLevel > 30 && screenLightLevel <= 50*/ screenLightLevel == 50){
            tvScreenBrightness.setText(R.string.centre);
        }else if(/*screenLightLevel > 50 && screenLightLevel <= 80*/ screenLightLevel == 80){
            tvScreenBrightness.setText(R.string.high);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        if (wifiLockInfo != null) {
            if (!TextUtils.isEmpty(wifiLockInfo.getFunctionSet())) {
                String functionSet = wifiLockInfo.getFunctionSet();
                int func = 0;
                try {
                    func = Integer.parseInt(functionSet);
                } catch (Exception e) {
                    LogUtils.d("" + e.getMessage());
                }
                switch (v.getId()) {
                    case R.id.back:  //返回
                        finish();
                        break;
                    case R.id.rl_device_name:  //设备昵称
                        if(avi.isShow()){
                            intent = new Intent(this,PhilipsWifiVideoLockNickNameActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN,wifiLockInfo.getWifiSN());
                            intent.putExtra(KeyConstants.WIFI_LOCK_NEW_NAME,wifiLockInfo.getLockNickname());
                            startActivityForResult(intent,KeyConstants.WIFI_VIDEO_LOCK_SETTING_NICKNAME);
                        }
                        mPresenter.release();
                        break;
                    case R.id.rl_safe_mode:
                        if(avi.isShow()){

                            intent = new Intent(this, PhilipsWifiVideoLockSafeModeActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiLockInfo.getWifiSN());
                            startActivityForResult(intent,KeyConstants.WIFI_VIDEO_LOCK_SAFE_MODE_CODE);
                            mPresenter.release();
                        }
                        break;


                    case R.id.iv_am:   //手动自动模式
                        if(avi.isShow()){

                            intent = new Intent(this, PhilipsWifiVideoLockAMModeActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiLockInfo.getWifiSN());
                            startActivityForResult(intent,KeyConstants.WIFI_VIDEO_LOCK_AM_MODE_CODE);
                            mPresenter.release();
                        }

                        break;
                    case R.id.rl_door_lock_language_switch:
                        if(avi.isShow()){

                            intent = new Intent(this, PhilipsWifiVideoLockLanguageSettingActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivityForResult(intent,KeyConstants.WIFI_VIDEO_LOCK_LANGUAGE_CODE);
                            mPresenter.release();
                        }
                        break;
                    case R.id.rl_voice_quality_setting:
                        if (avi.isShow()) {
                            intent = new Intent(this, PhilipsWifiVideoLockVoiceQualitySettingActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivityForResult(intent,KeyConstants.WIFI_VICEO_LOCK_VOICE_QUALITY);
                            mPresenter.release();
                        }
                        break;
                    case R.id.iv_silent_mode:  //静音模式
                        if(avi.isShow()) {
                            if(wifiLockInfo.getPowerSave() == 1){
                                if(wifiLockInfo.getPower() < 30) {
                                    powerSaveModeStatus();
                                    return;
                                }
                                powerStatusDialog();
                                return;
                            }
                            tvTips.setVisibility(View.VISIBLE);
                            avi.setVisibility(View.VISIBLE);
                            avi.show();
                            if(ivSilentMode.isSelected()){
                                setVolume = 0;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPresenter.setConnectVolume(wifiSn,0);
                                    }
                                }).start();
                            }else{
                                setVolume = 1;
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mPresenter.setConnectVolume(wifiSn,1);
                                    }
                                }).start();

                            }
                        }
                        break;
                    case R.id.rl_device_information:
                        if(avi.isShow()){

                            intent = new Intent(this, PhilipsWifiVideoLockDeviceInfoActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivity(intent);
                            mPresenter.release();
                        }
                        break;
                    case R.id.btn_delete:  //删除设备
                        AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(this,getString(R.string.device_delete_dialog_head),
                                getString(R.string.philips_cancel), getString(R.string.query),"#0066A1", "#FFFFFF",new AlertDialogUtil.ClickListener() {
                                    @Override
                                    public void left() {

                                    }

                                    @Override
                                    public void right() {
                                        showLoading(getString(R.string.is_deleting));
                                        if(isWifiVideoLockType){
                                            mPresenter.deleteVideoDevice(wifiLockInfo.getWifiSN());
                                        }else{
                                            mPresenter.deleteDevice(wifiLockInfo.getWifiSN());
                                        }

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                    }

                                    @Override
                                    public void afterTextChanged(String toString) {
                                    }
                                });
                        break;
                    case R.id.rl_wifi_name: //WiFi名称
                        if(avi.isShow()){
                            Intent setWifiIntent = new Intent(this, PhilipsWifiLockWifiDetailActivity.class);
                            setWifiIntent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivity(setWifiIntent);
                            mPresenter.release();
                        }
                        break;
                    case R.id.rl_message_push:
                        if(avi.isShow()){

                            intent = new Intent(this, PhilipsWifiLockMessagePushActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivity(intent);
                            mPresenter.release();
                        }
                        break;
                    case R.id.rl_screen_brightness:
                        if (avi.isShow()) {
                            intent = new Intent(this, PhilipsWifiVideoScreenBrightnessActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivityForResult(intent,KeyConstants.WIFI_VIDEO_LOCK_SCREEN_BRIGHTNESS);
                            mPresenter.release();
                        }
                        break;
                    case R.id.rl_screen_duration:
                        if (avi.isShow()) {
                            intent = new Intent(this, PhilipsWifiVideoScreenDurationActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivityForResult(intent,KeyConstants.WIFI_VIDEO_LOCK_SCREEN_DURATION);
                            mPresenter.release();
                        }
                        break;
                    case R.id.rl_door_direction:
                        if (avi.isShow()) {
                            intent = new Intent(this, PhilipsWifiLockOpenDirectionActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivityForResult(intent,KeyConstants.WIFI_LOCK_SET_OPEN_DIRECTION);
                            mPresenter.release();
                        }
                        break;
                    case R.id.rl_open_force:
                        if (avi.isShow()) {
                            intent = new Intent(this, PhilipsWifiLockOpenForceActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivityForResult(intent,KeyConstants.WIFI_LOCK_SET_OPEN_FORCE);
                            mPresenter.release();
                        }
                        break;
                    case R.id.rl_real_time_video:
                        if(BleLockUtils.isSupportVideoModeSwitch(wifiLockInfo.getFunctionSet())) return;
                        if(avi.isShow()){
                            intent = new Intent(this, PhilipsWifiVideoLockRealTimeActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivity(intent);
                            mPresenter.release();
                        }
                        break;
                    case R.id.rl_wandering_alarm:
                        if(avi.isShow()){
                            intent = new Intent(this, PhilipsWifiVideoLockWanderingAlarmActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivityForResult(intent,KeyConstants.WIFI_VIDEO_LOCK_WANDERING_ALARM_CODE);
                            mPresenter.release();
                        }
                        break;
                    case R.id.rl_duress_alarm:
                        if(avi.isShow()){
                            intent = new Intent(this, PhilipsWifiVideoLockDuressAlarmActivity.class);
                            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                            startActivity(intent);
                            mPresenter.release();
                        }
                        break;
                }
            } else {
                LogUtils.d("--kaadas--取功能集为空");

            }
        }
    }

    private void showWifiDialog() {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(PhilipsWifiVideoLockMoreActivity.this, getString(R.string.activity_wifi_video_replace_wifi_again),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#999999", "#1F95F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        Intent wifiIntent = new Intent(PhilipsWifiVideoLockMoreActivity.this, WifiLockAddNewThirdActivity.class);
                        String wifiModelType = "WiFi&VIDEO";
                        wifiIntent.putExtra("wifiModelType", wifiModelType);
                        wifiIntent.putExtra("distribution_again", true);
                        startActivity(wifiIntent);
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
    protected void onResume() {
        super.onResume();
        if(avi!=null){
            avi.hide();
            tvTips.setVisibility(View.GONE);
        }
        registerBroadcast();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
        ToastUtils.showLong(R.string.delete_success);
        hiddenLoading();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeleteDeviceFailed(Throwable throwable) {
        LogUtils.d("删除失败   " + throwable.getMessage());
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
//        ToastUtils.showLong(R.string.delete_fialed);
        hiddenLoading();
    }

    @Override
    public void onDeleteDeviceFailedServer(BaseResult result) {
        LogUtils.d("删除失败   " + result.toString());
        String httpErrorCode = HttpUtils.httpErrorCode(this, result.getCode());
        ToastUtils.showLong(httpErrorCode);
        hiddenLoading();
    }

    @Override
    public void modifyDeviceNicknameSuccess() {
        hiddenLoading();
        tvDeviceName.setText(name);
        wifiLockInfo.setLockNickname(name);
        ToastUtils.showLong(R.string.device_nick_name_update_success);
        Intent intent = new Intent();
        intent.putExtra(KeyConstants.WIFI_LOCK_NEW_NAME, name);
        setResult(RESULT_OK, intent);
    }

    @Override
    public void modifyDeviceNicknameError(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void modifyDeviceNicknameFail(BaseResult baseResult) {
        hiddenLoading();
        ToastUtils.showLong(HttpUtils.httpErrorCode(this, baseResult.getCode()));
    }

    @Override
    public void onUpdatePushStatusSuccess(int status) {
        hiddenLoading();
        wifiLockInfo.setPushSwitch(status);
    }

    @Override
    public void onUpdatePushStatusFailed(BaseResult result) {
        hiddenLoading();
        ToastUtils.showLong(R.string.set_failed);
    }

    @Override
    public void onUpdatePushStatusThrowable(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showLong(R.string.set_failed);
    }

    @Override
    public void onWifiLockActionUpdate() {
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        initData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case KeyConstants.WIFI_VIDEO_LOCK_SAFE_MODE_CODE:
                    if(data.getIntExtra(KeyConstants.WIFI_VIDEO_LOCK_SAFE_MODE,0) == 1){
                        ivSafeMode.setText(getString(R.string.activity_wifi_video_safe_mode_double));
                    }else{
                        ivSafeMode.setText(getString(R.string.activity_wifi_video_lock_safe_mode_single));
                    }
                    break;
                case KeyConstants.WIFI_VIDEO_LOCK_AM_MODE_CODE:
                    int amMode = data.getIntExtra(KeyConstants.WIFI_VIDEO_LOCK_AM_MODE,0);
                    ivAm.setText(amMode == 1 ? getString(R.string.wifi_video_lock_normal_mode)
                            : getString(R.string.wifi_video_lock_auto_mode));
                    break;
                case KeyConstants.WIFI_VIDEO_LOCK_LANGUAGE_CODE:
                    String language = data.getStringExtra(KeyConstants.WIFI_VIDEO_LOCK_LANGUAGE);
                    if ("zh".equals(language)) {
                        tvLanguage.setText(R.string.chinese);
                    } else if ("en".equals(language)) {
                        tvLanguage.setText(R.string.setting_language_en);
                    }else{
                        tvLanguage.setText(R.string.chinese);
                    }
                    break;
                case KeyConstants.WIFI_VIDEO_LOCK_WANDERING_ALARM_CODE:
                    int stayStatus = data.getIntExtra(KeyConstants.WIFI_VIDEO_LOCK_WANDERING_ALARM,0);
                    if(stayStatus == 0){
                        tvWanderingAlarmRight.setText(getString(R.string.wandering_alarm_close));
                    }else if(stayStatus == 1){
                        tvWanderingAlarmRight.setText(getString(R.string.activity_wifi_video_more_open));
                    }
                    break;
                case KeyConstants.WIFI_VIDEO_LOCK_SETTING_NICKNAME:
                    String nickName = data.getStringExtra(KeyConstants.WIFI_LOCK_NEW_NAME) + "";
                    tvDeviceName.setText(nickName);
                    wifiLockInfo.setLockNickname(nickName);
                    Intent intent = new Intent();
                    intent.putExtra(KeyConstants.WIFI_LOCK_NEW_NAME, name);
                    setResult(RESULT_OK, intent);
                    break;
                case KeyConstants.WIFI_VICEO_LOCK_VOICE_QUALITY:
                    int voiceQuality = data.getIntExtra(MqttConstant.SET_VOICE_QUALITY,0);
                    setVoiceQuality(voiceQuality);
                    break;
                case KeyConstants.WIFI_VIDEO_LOCK_SCREEN_BRIGHTNESS:
                    int screenLightLevel = data.getIntExtra(MqttConstant.SET_SCREEN_LIGHT_LEVEL,50);
                    setScreenLightLevel(screenLightLevel);
                    break;
                case KeyConstants.WIFI_VIDEO_LOCK_SCREEN_DURATION:
                    int screenLightTime = data.getIntExtra(MqttConstant.SET_SREEN_LIGHT_TIME,10);
                    setScreenLightTime(screenLightTime);
                    break;
                case KeyConstants.WIFI_LOCK_SET_OPEN_DIRECTION:
                    if(data != null){
                        int openDirection = data.getIntExtra(MqttConstant.SET_OPEN_DIRECTION, 0);
                        setOpenDirection(openDirection);
                    }
                    break;
                case KeyConstants.WIFI_LOCK_SET_OPEN_FORCE:
                    int openForce = data.getIntExtra(MqttConstant.SET_OPEN_FORCE,0);
                    setOpenForce(openForce);
                    break;
            }

        }
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
    public void onSettingCallBack(boolean flag,int code) {
        if(!PhilipsWifiVideoLockMoreActivity.this.isFinishing()){
            mPresenter.setMqttCtrl(0);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(flag){
                        ToastUtils.showShort(getString(R.string.modify_success));
                        if(code == 1){
                            ivSilentMode.setSelected(true);
                        }else{
                            ivSilentMode.setSelected(false);
                        }
                    }else{
                        setVolume = code;
                        ToastUtils.showShort(getString(R.string.modify_failed));
                    }
                    if(avi!=null){
                        tvTips.setVisibility(View.GONE);
                        avi.hide();
                    }
                }
            });
            mPresenter.release();
        }
    }


}