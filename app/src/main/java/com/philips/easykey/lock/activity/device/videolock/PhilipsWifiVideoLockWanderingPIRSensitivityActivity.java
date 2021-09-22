package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockMorePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockMoreView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.result.MultiCheckOTAResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;

import java.util.List;


public class PhilipsWifiVideoLockWanderingPIRSensitivityActivity extends BaseActivity<IWifiLockMoreView, WifiLockMorePresenter<IWifiLockMoreView>>
        implements IWifiLockMoreView   {

    ImageView back;
    CheckBox ivSensitivity1;
    CheckBox ivSensitivity2;
    CheckBox ivSensitivity3;

    private String wifiSn;
    private WifiLockInfo wifiLockInfo;

    private int pir;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_wandering_pir_sensitivity);

        back = findViewById(R.id.back);
        ivSensitivity1 = findViewById(R.id.iv_sensitivity_1);
        ivSensitivity2 = findViewById(R.id.iv_sensitivity_2);
        ivSensitivity3 = findViewById(R.id.iv_sensitivity_3);

        back.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(KeyConstants.WIFI_VIDEO_WANDERING_SENSITIVITY,pir);
            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            setResult(RESULT_OK,intent);
            finish();
        });
        findViewById(R.id.rl_sensitivity_1).setOnClickListener(v -> {
            if(wifiLockInfo.getPowerSave() == 0){
                if(!ivSensitivity1.isChecked()){
                    ivSensitivity1.setChecked(true);
                    ivSensitivity2.setChecked(false);
                    ivSensitivity3.setChecked(false);
                    pir = KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_3;
                }
            }else{
                powerStatusDialog();
            }
        });
        findViewById(R.id.rl_sensitivity_2).setOnClickListener(v -> {
            if(wifiLockInfo.getPowerSave() == 0){

                if(!ivSensitivity2.isChecked()){
                    ivSensitivity2.setChecked(true);
                    ivSensitivity1.setChecked(false);
                    ivSensitivity3.setChecked(false);
                    pir = KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_2;
                }
            }else{
                powerStatusDialog();
            }
        });
        findViewById(R.id.rl_sensitivity_3).setOnClickListener(v -> {
            if(wifiLockInfo.getPowerSave() == 0){
                if(!ivSensitivity3.isChecked()){
                    ivSensitivity3.setChecked(true);
                    ivSensitivity2.setChecked(false);
                    ivSensitivity1.setChecked(false);
                    pir = KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_1;
                }

            }else{
                powerStatusDialog();
            }
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        pir = getIntent().getIntExtra(KeyConstants.WIFI_VIDEO_WANDERING_SENSITIVITY,35);
        initData();
    }

    private void initData() {
        /*if(wifiLockInfo != null){
            if(wifiLockInfo.getSetPir() != null){*/
//                pir = wifiLockInfo.getSetPir().getPir_sen();

                if(pir <= KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_1){
                    ivSensitivity1.setChecked(false);
                    ivSensitivity2.setChecked(false);
                    ivSensitivity3.setChecked(true);
                }else if(pir <= KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_2 && pir > KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_1){
                    ivSensitivity1.setChecked(false);
                    ivSensitivity2.setChecked(true);
                    ivSensitivity3.setChecked(false);
                }else if(pir <= KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_3 && pir > KeyConstants.WIFI_VIDEO_LOCK_PIR_SEN_2) {
                    ivSensitivity1.setChecked(true);
                    ivSensitivity2.setChecked(false);
                    ivSensitivity3.setChecked(false);
                }
//            }
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra(KeyConstants.WIFI_VIDEO_WANDERING_SENSITIVITY,pir);
            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    @Override
    protected WifiLockMorePresenter<IWifiLockMoreView> createPresent() {
        return new WifiLockMorePresenter();
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
    public void needMultiUpdate(List<MultiCheckOTAResult.UpgradeTask> upgradeTasks) {

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
    public void showLoading(String content) {

    }

    @Override
    public void showLoadingNoCancel(String content) {

    }

    @Override
    public void hiddenLoading() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
