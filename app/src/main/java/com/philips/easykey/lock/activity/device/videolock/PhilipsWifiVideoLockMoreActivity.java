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
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.widget.AVLoadingIndicatorView;


public class PhilipsWifiVideoLockMoreActivity extends BaseActivity<IWifiVideoLockMoreView, WifiVideoLockMorePresenter<IWifiVideoLockMoreView>>
        implements IWifiVideoLockMoreView, View.OnClickListener {

    RelativeLayout rlDeviceName;
    RelativeLayout rlSafeMode;
    TextView ivSafeMode;
    RelativeLayout rlAm;
    TextView ivAm;
    RelativeLayout rlDoorLockLanguageSwitch;
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
        avi = findViewById(R.id.avi);
        tvTips = findViewById(R.id.tv_tips);

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

        if(wifiLockInfo != null){
            if(MyApplication.getInstance().getWifiVideoLockTypeBySn(wifiSn) == HomeShowBean.TYPE_WIFI_VIDEO_LOCK){
                isWifiVideoLockType = true;
            }
        }
//        rlAm.setVisibility(View.GONE);
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
                ivSafeMode.setText(getString(R.string.safe_mode));
            }else{
                ivSafeMode.setText(getString(R.string.normal_mode));
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
                ivAm.setText(amMode == 1 ? getString(R.string.hand) + getString(R.string.activity_wifi_video_more_lock)
                        : getString(R.string.auto) + getString(R.string.activity_wifi_video_more_lock));
            } else {
                rlAm.setVisibility(View.GONE);
            }

            if(wifiLockInfo.getStay_status() == 0){
                tvWanderingAlarmRight.setText(getString(R.string.wandering_alarm_close));
            }else if(wifiLockInfo.getStay_status() ==1){
                tvWanderingAlarmRight.setText(getString(R.string.activity_wifi_video_more_open));
            }


            if(BleLockUtils.isSupportRealTimeVideo(func)){
                rlRealTimeVideo.setVisibility(View.VISIBLE);
            }else{
                rlRealTimeVideo.setVisibility(View.GONE);
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

                    case R.id.rl_real_time_video:
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
                            intent = new Intent(this,PhilipsWifiVideoLockDuressAlarmAvtivity.class);
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
        mPresenter.attachView(this);
        if(avi!=null){
            avi.hide();
            tvTips.setVisibility(View.GONE);
        }
        registerBroadcast();
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
                        ivSafeMode.setText(getString(R.string.safe_mode));
                    }else{
                        ivSafeMode.setText(getString(R.string.normal_mode));
                    }
                    break;
                case KeyConstants.WIFI_VIDEO_LOCK_AM_MODE_CODE:
                    int amMode = data.getIntExtra(KeyConstants.WIFI_VIDEO_LOCK_AM_MODE,0);
                    ivAm.setText(amMode == 1 ? getString(R.string.hand) + getString(R.string.activity_wifi_video_more_lock)
                            : getString(R.string.auto) + getString(R.string.activity_wifi_video_more_lock));
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