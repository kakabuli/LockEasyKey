package com.philips.easykey.lock.activity.device.videolock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.philips.easykey.lock.widget.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhilipsWifiVideoLockDeviceInfoActivity extends BaseActivity<IWifiVideoLockOTAView, PhilipsWifiVideoLockOTAPresenter<IWifiVideoLockOTAView>>
        implements IWifiVideoLockOTAView  {


    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.head_title)
    TextView headTitle;
    @BindView(R.id.tv_device_name)
    TextView mTvDevicename;
    @BindView(R.id.tv_device_model)
    TextView tvDeviceModel;
    @BindView(R.id.tv_serial_number)
    TextView tvSerialNumber;
    @BindView(R.id.tv_lock_firmware_version)
    TextView tvLockFirmwareVersion;
    @BindView(R.id.iv_wifilock)
    ImageView ivWifilock;
    @BindView(R.id.img_lock_check_firmware_version)
    ImageView mImgLockCheckFirmwareVersion;
    @BindView(R.id.tv_video_serial_number)
    TextView mTvVideoSerialNumer;
    @BindView(R.id.iv_child_system_firware_number)
    ImageView ivChildSystemFirwareNumber;
    @BindView(R.id.iv_lock_wifi_firware_number)
    ImageView ivLockWifiFirwareNumber;
    @BindView(R.id.iv_lock_firware_number)
    ImageView ivLockFirwareNimner;
    @BindView(R.id.tv_lock_firware_number)
    TextView tvLockFirwareNumber;
    @BindView(R.id.tv_lock_wifi_firware_number)
    TextView tvLockWifiFirwareNumber;
    @BindView(R.id.tv_child_system_firware_number)
    TextView tvChildSystemFirwareNumber;
    @BindView(R.id.img_lock_check_firware_number)
    ImageView mImgLockCheckFirwareNumner;
    @BindView(R.id.img_lock_check_wifi_firware_number)
    ImageView mImgLockCheckWifiFirwareNumber;
    @BindView(R.id.img_child_check_system_firware_number)
    ImageView mImgChildCheckSystemFirwareNumber;
    @BindView(R.id.avi)
    AVLoadingIndicatorView avi;
    @BindView(R.id.tv_tips)
    TextView mTvTips;

    private WifiLockInfo wifiLockInfo;
    private String wifiSN;
    private List<ProductInfo> productList = new ArrayList<>();
    private String videoMcuVersion = "";
    private String videoMoudleVersion = "";
    private String videoWifiMoudleVersion = "";
    private String lockFirwareVersion = "";

    private CheckOTAResult.UpdateFileInfo videoMcuInfo;
    private CheckOTAResult.UpdateFileInfo videoMoudleInfo;
    private CheckOTAResult.UpdateFileInfo videoWifiMoudleInfo;
    private CheckOTAResult.UpdateFileInfo lockFirwareInfo;

    private InnerRecevier mInnerRecevier = null;
    private boolean updataSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_device_info);
        ButterKnife.bind(this);
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
            if(BleLockUtils.isSupportPanelMultiOTA(wifiLockInfo.getFunctionSet())){
                mPresenter.checkOtaInfo(wifiSN, wifiLockInfo.getBackPanelVersion() + "", 7);
                mPresenter.checkOtaInfo(wifiSN, wifiLockInfo.getFrontPanelVersion() + "", 6);
            }else{
                mPresenter.checkOtaInfo(wifiSN, wifiLockInfo.getLockFirmwareVersion() + "", 2);
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
        }
    }

    private long time = 0;

    @OnClick({ R.id.rl_lock_model_firmware_version,R.id.back,R.id.rl_tv_lock_firware_number,R.id.rl_child_system_firware_number,R.id.rl_lock_wifi_firware_number})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.rl_lock_model_firmware_version://门锁固件版本
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
                    updateDialog(lockFirwareInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                break;
            case R.id.rl_tv_lock_firware_number://视频模组版本
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
                    updateDialog(videoMoudleInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                break;
            case R.id.rl_lock_wifi_firware_number://视频模组微控制器版本
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
                    updateDialog(videoMcuInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                break;
            case R.id.rl_child_system_firware_number://wifi固件版本
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
                    updateDialog(videoWifiMoudleInfo,getString(R.string.philips_videolock_deviceinfo_update_ota),wifiSN);
                break;
        }
    }

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
                break;
            case 7:
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
        ToastUtils.showShort(R.string.unknown_error);
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
