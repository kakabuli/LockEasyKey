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


public class PhilipsWifiVideoLockWanderingJudgeTimeActivity extends BaseActivity<IWifiLockMoreView, WifiLockMorePresenter<IWifiLockMoreView>>
        implements IWifiLockMoreView{

    ImageView back;
    CheckBox ivJudgeTime1;
    CheckBox ivJudgeTime2;
    CheckBox ivJudgeTime3;
    CheckBox ivJudgeTime4;
    CheckBox ivJudgeTime5;
    CheckBox ivJudgeTime6;

    private String wifiSn;
    private WifiLockInfo wifiLockInfo;

    private int stay_time ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_wandering_judge_time);

        back = findViewById(R.id.back);
        ivJudgeTime1 = findViewById(R.id.iv_judge_time_1);
        ivJudgeTime2 = findViewById(R.id.iv_judge_time_2);
        ivJudgeTime3 = findViewById(R.id.iv_judge_time_3);
        ivJudgeTime4 = findViewById(R.id.iv_judge_time_4);
        ivJudgeTime5 = findViewById(R.id.iv_judge_time_5);
        ivJudgeTime6 = findViewById(R.id.iv_judge_time_6);

        back.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra(KeyConstants.WIFI_VIDEO_WANDERING_TIME,stay_time);
            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            setResult(RESULT_OK,intent);
            finish();
        });
        findViewById(R.id.rl_judge_time_6).setOnClickListener(v -> setJudgeTime(R.id.rl_judge_time_6));
        findViewById(R.id.rl_judge_time_5).setOnClickListener(v -> setJudgeTime(R.id.rl_judge_time_5));
        findViewById(R.id.rl_judge_time_4).setOnClickListener(v -> setJudgeTime(R.id.rl_judge_time_4));
        findViewById(R.id.rl_judge_time_3).setOnClickListener(v -> setJudgeTime(R.id.rl_judge_time_3));
        findViewById(R.id.rl_judge_time_2).setOnClickListener(v -> setJudgeTime(R.id.rl_judge_time_2));
        findViewById(R.id.rl_judge_time_1).setOnClickListener(v -> setJudgeTime(R.id.rl_judge_time_1));

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        stay_time = getIntent().getIntExtra(KeyConstants.WIFI_VIDEO_WANDERING_TIME,30);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        initData();
    }

    private void initData() {
        if(stay_time == 10){
            setJudgeTimeInit(R.id.rl_judge_time_1);
        }else if(stay_time == 20){
            setJudgeTimeInit(R.id.rl_judge_time_2);
        }else if(stay_time == 30){
            setJudgeTimeInit(R.id.rl_judge_time_3);
        }else if(stay_time == 40){
            setJudgeTimeInit(R.id.rl_judge_time_4);
        }else if(stay_time == 50){
            setJudgeTimeInit(R.id.rl_judge_time_5);
        }else if(stay_time == 60){
            setJudgeTimeInit(R.id.rl_judge_time_6);
        }
        ivJudgeTime1.setClickable(false);
        ivJudgeTime2.setClickable(false);
        ivJudgeTime3.setClickable(false);
        ivJudgeTime4.setClickable(false);
        ivJudgeTime5.setClickable(false);
        ivJudgeTime6.setClickable(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra(KeyConstants.WIFI_VIDEO_WANDERING_TIME,stay_time);
            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            setResult(RESULT_OK,intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void setJudgeTimeInit(int id){
        switch (id){
            case R.id.rl_judge_time_6:
                    ivJudgeTime6.setChecked(true);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(false);


                break;
            case R.id.rl_judge_time_5:

                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(true);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(false);

                break;
            case R.id.rl_judge_time_4:


                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(true);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(false);

                break;
            case R.id.rl_judge_time_3:

                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(true);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(false);

                break;
            case R.id.rl_judge_time_2:
                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(true);
                    ivJudgeTime1.setChecked(false);
                break;
            case R.id.rl_judge_time_1:

                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(true);
                break;
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

    private void setJudgeTime(int id){
        if(wifiLockInfo.getPowerSave() == 1){
            powerStatusDialog();
            return;
        }
        switch (id){
            case R.id.rl_judge_time_6:
                if(!ivJudgeTime6.isChecked()){
                    ivJudgeTime6.setChecked(true);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(false);
                    stay_time = 60;
                }

                break;
            case R.id.rl_judge_time_5:
                if(!ivJudgeTime5.isChecked()){

                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(true);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(false);
                    stay_time = 50;
                }
                break;
            case R.id.rl_judge_time_4:
                if(!ivJudgeTime4.isChecked()){

                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(true);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(false);
                    stay_time = 40;
                }
                break;
            case R.id.rl_judge_time_3:
                if(!ivJudgeTime3.isChecked()){

                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(true);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(false);
                    stay_time = 30;
                }
                break;
            case R.id.rl_judge_time_2:
                if(!ivJudgeTime2.isChecked()){

                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(true);
                    ivJudgeTime1.setChecked(false);
                    stay_time = 20;
                }
                break;
            case R.id.rl_judge_time_1:
                if(!ivJudgeTime1.isChecked()){

                    ivJudgeTime6.setChecked(false);
                    ivJudgeTime5.setChecked(false);
                    ivJudgeTime4.setChecked(false);
                    ivJudgeTime3.setChecked(false);
                    ivJudgeTime2.setChecked(false);
                    ivJudgeTime1.setChecked(true);
                    stay_time = 10;
                }
                break;
        }
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
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();
    }
}
