package com.philips.easykey.lock.activity.device.videolock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.PhilipsWifiVideoLockOTAPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IWifiVideoLockOTAView;
import com.philips.easykey.lock.publiclibrary.bean.ProductInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

public class PhilipsWifiVideoLockDeviceInfoActivity extends BaseActivity<IWifiVideoLockOTAView, PhilipsWifiVideoLockOTAPresenter<IWifiVideoLockOTAView>>
        implements IWifiVideoLockOTAView  {


    ImageView back;
    TextView headTitle;
    TextView mTvDevicename;
    TextView tvDeviceModel;
    TextView tvSerialNumber;
    TextView tvLockFirmwareVersion;
    ImageView ivWifilock;
    ImageView mImgLockCheckFirmwareVersion;
    TextView mTvVideoSerialNumer;
    ImageView ivChildSystemFirwareNumber;
    ImageView ivLockWifiFirwareNumber;
    ImageView ivLockFirwareNimner;
    TextView tvLockFirwareNumber;
    TextView tvLockWifiFirwareNumber;
    TextView tvChildSystemFirwareNumber;
    ImageView mImgLockCheckFirwareNumner;
    ImageView mImgLockCheckWifiFirwareNumber;
    ImageView mImgChildCheckSystemFirwareNumber;
    ImageView mIvForntHardVersion;
    ImageView mIvBackHardVersion;
    AVLoadingIndicatorView avi;
    TextView mTvTips;
    ConstraintLayout rlVoiceModelFirmwareVersion;
    TextView tvVoiceModelFirmWareVersion;
    String voiceModelFirmWareVersion;
    ConstraintLayout rlBackHardVersion;
    ConstraintLayout rlFrontHardVersion;
    TextView tvFrontHardVersion;
    TextView tvBackHardVersion;

    private WifiLockInfo wifiLockInfo;
    private String wifiSN;
    private List<ProductInfo> productList = new ArrayList<>();
    private String videoMcuVersion = "";
    private String videoMoudleVersion = "";
    private String videoWifiMoudleVersion = "";
    private String lockFirwareVersion = "";
    private String frontHardFirwareVersion = "";
    private String backHardFirwareVersion = "";

    private CheckOTAResult.UpdateFileInfo videoMcuInfo;
    private CheckOTAResult.UpdateFileInfo videoMoudleInfo;
    private CheckOTAResult.UpdateFileInfo videoWifiMoudleInfo;
    private CheckOTAResult.UpdateFileInfo lockFirwareInfo;
    private CheckOTAResult.UpdateFileInfo frontHardFirwareInfo;
    private CheckOTAResult.UpdateFileInfo backHardFirwareInfo;

    private InnerRecevier mInnerRecevier = null;
    private boolean updataSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_device_info);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        mTvDevicename = findViewById(R.id.tv_device_name);
        tvDeviceModel = findViewById(R.id.tv_device_model);
        tvSerialNumber = findViewById(R.id.tv_serial_number);
        tvLockFirmwareVersion = findViewById(R.id.tv_lock_firmware_version);
        ivWifilock = findViewById(R.id.iv_wifilock);
        mImgLockCheckFirmwareVersion = findViewById(R.id.img_lock_check_firmware_version);
        mTvVideoSerialNumer = findViewById(R.id.tv_video_serial_number);
        ivChildSystemFirwareNumber = findViewById(R.id.iv_child_system_firware_number);
        ivLockWifiFirwareNumber = findViewById(R.id.iv_lock_wifi_firware_number);
        ivLockFirwareNimner = findViewById(R.id.iv_lock_firware_number);
        tvLockFirwareNumber = findViewById(R.id.tv_lock_firware_number);
        tvLockWifiFirwareNumber = findViewById(R.id.tv_lock_wifi_firware_number);
        tvChildSystemFirwareNumber = findViewById(R.id.tv_child_system_firware_number);
        mImgLockCheckFirwareNumner = findViewById(R.id.img_lock_check_firware_number);
        mImgLockCheckWifiFirwareNumber = findViewById(R.id.img_lock_check_wifi_firware_number);
        mImgChildCheckSystemFirwareNumber = findViewById(R.id.img_child_check_system_firware_number);
        rlVoiceModelFirmwareVersion = findViewById(R.id.rl_voice_model_firmware_version);
        tvVoiceModelFirmWareVersion = findViewById(R.id.tv_voice_model_firmware_version);
        rlFrontHardVersion = findViewById(R.id.rl_fornt_hard_version);
        rlBackHardVersion = findViewById(R.id.rl_back_hard_version);
        tvFrontHardVersion = findViewById(R.id.tv_fornt_hard_version);
        tvBackHardVersion = findViewById(R.id.tv_back_hard_version);
        mIvForntHardVersion = findViewById(R.id.iv_fornt_hard_version);
        mIvBackHardVersion = findViewById(R.id.iv_back_hard_version);
        avi = findViewById(R.id.avi);
        mTvTips = findViewById(R.id.tv_tips);

        back.setOnClickListener(v -> finish());
        findViewById(R.id.rl_lock_model_firmware_version).setOnClickListener(v -> {
            //门锁固件版本
            if(wifiLockInfo.getIsAdmin() != 1) return;

            if(wifiLockInfo.getPower() < 30){
                showLowBattery();
                return;
            }

            if(wifiLockInfo.getPowerSave() == 1){
                powerStatusDialog();
                return;
            }
            if(avi.isShow() && System.currentTimeMillis() - time > 2500)
                if(lockFirwareInfo == null){
                    Toast.makeText(this, getString(R.string.info_error), Toast.LENGTH_SHORT).show();
                }else {
                    updateDialog(lockFirwareInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                }
        });

        rlFrontHardVersion.setOnClickListener(v -> {

                //前面板
                if(wifiLockInfo.getIsAdmin() != 1) return;

                if(wifiLockInfo.getPower() < 30){
                    showLowBattery();
                    return;
                }

                if(wifiLockInfo.getPowerSave() == 1){
                    powerStatusDialog();
                    return;
                }
                if(avi.isShow() && System.currentTimeMillis() - time > 2500){
                    if(frontHardFirwareInfo == null){
                        Toast.makeText(this, getString(R.string.info_error), Toast.LENGTH_SHORT).show();
                    }else {
                        updateDialog(frontHardFirwareInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                    }
                }
        });

        rlBackHardVersion.setOnClickListener(v -> {

                //后面板
                if(wifiLockInfo.getIsAdmin() != 1) return;

                if(wifiLockInfo.getPower() < 30){
                    showLowBattery();
                    return;
                }

                if(wifiLockInfo.getPowerSave() == 1){
                    powerStatusDialog();
                    return;
                }
                if(avi.isShow() && System.currentTimeMillis() - time > 2500){
                    if(backHardFirwareInfo == null){
                        Toast.makeText(this, getString(R.string.info_error), Toast.LENGTH_SHORT).show();
                    }else {
                        updateDialog(backHardFirwareInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                    }
                }
        });

        findViewById(R.id.rl_tv_lock_firware_number).setOnClickListener(v -> {
            //视频模组版本
            if(wifiLockInfo.getIsAdmin() != 1) return;

            if(wifiLockInfo.getPower() < 30){
                showLowBattery();
                return;
            }

            if(wifiLockInfo.getPowerSave() == 1){
                powerStatusDialog();
                return;
            }
            if(avi.isShow() && System.currentTimeMillis() - time > 2500){
                if(videoMoudleInfo == null){
                    Toast.makeText(this, getString(R.string.info_error), Toast.LENGTH_SHORT).show();
                }else {
                    updateDialog(videoMoudleInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                }
            }

        });
        findViewById(R.id.rl_lock_wifi_firware_number).setOnClickListener(v -> {
            //视频模组微控制器版本
            if(wifiLockInfo.getIsAdmin() != 1) return;

            if(wifiLockInfo.getPower() < 30){
                showLowBattery();
                return;
            }

            if(wifiLockInfo.getPowerSave() == 1){
                powerStatusDialog();
                return;
            }
            if(avi.isShow() && System.currentTimeMillis() - time > 2500){
                if(videoMcuInfo == null){
                    Toast.makeText(this, getString(R.string.info_error), Toast.LENGTH_SHORT).show();
                }else {
                    updateDialog(videoMcuInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                }
            }

        });
        findViewById(R.id.rl_child_system_firware_number).setOnClickListener(v -> {
            //wifi固件版本
            if(wifiLockInfo.getIsAdmin() != 1) return;

            if(wifiLockInfo.getPower() < 30){
                showLowBattery();
                return;
            }

            if(wifiLockInfo.getPowerSave() == 1){
                powerStatusDialog();
                return;
            }
            if(avi.isShow() && System.currentTimeMillis() - time > 2500){
                if(videoWifiMoudleInfo == null){
                    Toast.makeText(this, getString(R.string.info_error), Toast.LENGTH_SHORT).show();
                }else {
                    updateDialog(videoWifiMoudleInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                }
            }

        });

        wifiSN = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSN);
        productList = MyApplication.getInstance().getProductInfos();

        initData();
        checkUpdate();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(avi != null){
            avi.hide();
            mTvTips.setVisibility(View.GONE);
        }
        registerBroadcast();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

    @Override
    public void finish() {
        super.finish();
        if(!updataSuccess){
            mPresenter.release();
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

    private void checkUpdate() {
        if(wifiLockInfo.getIsAdmin() == 1){
            mPresenter.checkOtaInfo(wifiSN,wifiLockInfo.getWifiVersion() + "",1);
            mPresenter.checkOtaInfo(wifiSN,wifiLockInfo.getMcu_version() + "",5);
            mPresenter.checkOtaInfo(wifiSN,wifiLockInfo.getCamera_version() + "",4);
            if(BleLockUtils.isSupportSinglePanelOTA(wifiLockInfo.getFunctionSet())){
                mPresenter.checkOtaInfo(wifiSN, wifiLockInfo.getBackPanelVersion() + "", 7);
                mPresenter.checkOtaInfo(wifiSN, wifiLockInfo.getFrontPanelVersion() + "", 6);
            }else if(BleLockUtils.isSupportFrontPanelOnlyShow(wifiLockInfo.getFunctionSet())){
                mPresenter.checkOtaInfo(wifiSN, wifiLockInfo.getFrontPanelVersion() + "", 6);
            }
        }
    }

    @Override
    protected PhilipsWifiVideoLockOTAPresenter<IWifiVideoLockOTAView> createPresent() {
        return new PhilipsWifiVideoLockOTAPresenter<>();
    }

    private void initData() {
        if (wifiLockInfo != null) {
            String productModel = wifiLockInfo.getProductModel() + "";
            tvDeviceModel.setText(TextUtils.isEmpty(productModel + "") ? "" : productModel.contentEquals("K13") ? getString(R.string.lan_bo_ji_ni) : productModel);
            //适配服务器上的产品型号，适配不上则显示锁本地的研发型号
            for (ProductInfo productInfo:productList) {

                try {
                    if (productInfo.getSnHead().equals(wifiSN.substring(0,3))) {
                        tvDeviceModel.setText(productInfo.getProductModel());
                    }
                } catch (Exception e) {
                    LogUtils.d("--kaadas--:" + e.getMessage());
                }
            }

            if(wifiLockInfo.getIsAdmin() == 1){
                ivChildSystemFirwareNumber.setVisibility(View.VISIBLE);
                ivLockWifiFirwareNumber.setVisibility(View.VISIBLE);
                ivLockFirwareNimner.setVisibility(View.VISIBLE);
                ivWifilock.setVisibility(View.VISIBLE);

                mImgLockCheckFirwareNumner.setVisibility(View.GONE);
                mImgLockCheckWifiFirwareNumber.setVisibility(View.GONE);
                mImgChildCheckSystemFirwareNumber.setVisibility(View.GONE);
                mImgLockCheckFirmwareVersion.setVisibility(View.GONE);
            }else{
                ivChildSystemFirwareNumber.setVisibility(View.GONE);
                ivLockWifiFirwareNumber.setVisibility(View.GONE);
                ivLockFirwareNimner.setVisibility(View.GONE);
                ivWifilock.setVisibility(View.GONE);

            }

            tvSerialNumber.setText(TextUtils.isEmpty(wifiLockInfo.getWifiSN()) ? "" : wifiLockInfo.getWifiSN());
            tvLockFirmwareVersion.setText(wifiLockInfo.getLockFirmwareVersion() + "");

            mTvDevicename.setText(wifiLockInfo.getLockNickname() + "");

            if(wifiLockInfo.getCamera_version() != null){
                tvLockFirwareNumber.setText(wifiLockInfo.getCamera_version() + "");
            }

            if(wifiLockInfo.getMcu_version() != null){
                tvLockWifiFirwareNumber.setText(wifiLockInfo.getMcu_version()+ "");
            }

            if(wifiLockInfo.getWifiVersion() != null){
                tvChildSystemFirwareNumber.setText(wifiLockInfo.getWifiVersion()+ "");
            }

            if(wifiLockInfo.getDevice_sn() != null){
                mTvVideoSerialNumer.setText(wifiLockInfo.getDevice_sn() + "");
            }

            if(BleLockUtils.isSupportSinglePanelOTA(wifiLockInfo.getFunctionSet())) {
                rlBackHardVersion.setVisibility(View.VISIBLE);
                rlFrontHardVersion.setVisibility(View.VISIBLE);
                if (wifiLockInfo.getFrontPanelVersion() != null)
                    tvFrontHardVersion.setText(wifiLockInfo.getFrontPanelVersion());
                if (wifiLockInfo.getBackPanelVersion() != null)
                    tvBackHardVersion.setText(wifiLockInfo.getBackPanelVersion());
            }else if(BleLockUtils.isSupportFrontPanelOnlyShow(wifiLockInfo.getFunctionSet())){
                rlBackHardVersion.setVisibility(View.GONE);
                rlFrontHardVersion.setVisibility(View.VISIBLE);
                if (wifiLockInfo.getFrontPanelVersion() != null)
                    tvFrontHardVersion.setText(wifiLockInfo.getFrontPanelVersion());
            }

            if(BleLockUtils.isSupportVoiceModel(wifiLockInfo.getFunctionSet())){
                rlVoiceModelFirmwareVersion.setVisibility(View.VISIBLE);
                voiceModelFirmWareVersion = wifiLockInfo.getVoiceVersion();
                tvVoiceModelFirmWareVersion.setText(voiceModelFirmWareVersion + "");
            }else{
                rlVoiceModelFirmwareVersion.setVisibility(View.GONE);
            }
        }
    }

    private long time = 0;

    private void showLowBattery() {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(this,getString(R.string.philips_deviceinfo_update_ota_battery_low),
                "#333333",getString(R.string.philips_cancel), getString(R.string.query),
                "#0066A1", "#FFFFFF",new AlertDialogUtil.ClickListener() {
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
        hiddenLoading();
    }

    @Override
    public void needUpdate(CheckOTAResult.UpdateFileInfo appInfo, String SN, String version, int type) {
        hiddenLoading();
        switch (type){
            case 1:
                mImgChildCheckSystemFirwareNumber.setVisibility(View.VISIBLE);
                videoWifiMoudleVersion = version;
                videoWifiMoudleInfo = appInfo;
                break;
            case 2:
                mImgLockCheckFirmwareVersion.setVisibility(View.VISIBLE);
                lockFirwareVersion = version;
                lockFirwareInfo = appInfo;
                break;
            case 4:
                mImgLockCheckFirwareNumner.setVisibility(View.VISIBLE);
                videoMoudleVersion = version;
                videoMoudleInfo = appInfo;
                break;
            case 5:
                mImgLockCheckWifiFirwareNumber.setVisibility(View.VISIBLE);
                videoMcuVersion = version;
                videoMcuInfo = appInfo;
                break;
            case 6:
                mIvForntHardVersion.setVisibility(View.VISIBLE);
                frontHardFirwareVersion = version;
                frontHardFirwareInfo = appInfo;
                break;
            case 7:
                mIvBackHardVersion.setVisibility(View.VISIBLE);
                backHardFirwareVersion = version;
                backHardFirwareInfo = appInfo;
                break;
        }
    }


    @Override
    public void readInfoFailed(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.check_update_failed));
        hiddenLoading();
    }

    @Override
    public void unknowError(String errorCode) {
        //TODO :未知错误没有必要toast
//        ToastUtils.showShort(R.string.unknown_error);
        hiddenLoading();
    }

    @Override
    public void uploadSuccess(int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                updataSuccess = true;
                //升级
                mPresenter.connectNotifyGateWayNewVersion();
            }
        }).start();
    }

    @Override
    public void uploadFailed() {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.notice_lock_update_uploadFailed));
    }


    @Override
    public void onMqttCtrl(boolean flag) {
        if(!PhilipsWifiVideoLockDeviceInfoActivity.this.isFinishing()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(avi != null){
                        avi.hide();
                        mTvTips.setVisibility(View.GONE);
                    }
                }
            });

        }
    }

    @Override
    public void onSettingCallBack(boolean flag) {
        if (!PhilipsWifiVideoLockDeviceInfoActivity.this.isFinishing()) {
            mPresenter.handler.post(new Runnable() {
                @Override
                public void run() {
                    if (flag) {

                    } else {
                        ToastUtils.showLong(getString(R.string.ota_fail));
                    }
                    if (avi != null) {
                        avi.hide();
                        mTvTips.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    public void updateDialog(CheckOTAResult.UpdateFileInfo appInfo,String content,String wifiSN){
        AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(this,content,
                "#333333",getString(R.string.philips_cancel),getString(R.string.philips_confirm),
                "#0066A1","#FFFFFF",new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        mPresenter.uploadOta(appInfo,wifiSN);
                        avi.show();
                        mTvTips.setVisibility(View.VISIBLE);
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
}
