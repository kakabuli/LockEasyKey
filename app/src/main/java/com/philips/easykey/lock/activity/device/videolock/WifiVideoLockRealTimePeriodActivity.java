package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockMorePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockMoreView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.result.MultiCheckOTAResult;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.widget.TimePickerDialog;

import java.util.List;

public class WifiVideoLockRealTimePeriodActivity extends BaseActivity<IWifiLockMoreView, WifiLockMorePresenter<IWifiLockMoreView>>
        implements IWifiLockMoreView{

    ImageView back;
    RelativeLayout rlRealTimeSetting;
    RelativeLayout rlRealTimeRuleRepet;
    TextView tvVideoTimeSetting;
    TextView tvPeriodConnect;

    private TimePickerDialog mTimePickerDialog;
    private String wifiSn;
    private int[] snoozeStartTime;
    private int startTime;
    private int endTime;

    private String time;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_real_time_period);

        back = findViewById(R.id.back);
        rlRealTimeSetting = findViewById(R.id.rl_real_time_setting);
        rlRealTimeRuleRepet = findViewById(R.id.rl_real_time_rule_repet);
        tvVideoTimeSetting = findViewById(R.id.tv_video_time_setting);
        tvPeriodConnect = findViewById(R.id.tv_period_connect);

        back.setOnClickListener(v -> setRealTimePeriod());
        rlRealTimeSetting.setOnClickListener(v -> showTimePickDialog());
        rlRealTimeRuleRepet.setOnClickListener(v -> {
            Intent intent = new Intent(this, WifiVideoLockRealTimeWeekPeriodActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_PERIOD, snoozeStartTime);
            startActivityForResult(intent,KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_PERIOD_CODE);
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        snoozeStartTime = getIntent().getIntArrayExtra(KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_PERIOD);
        startTime = getIntent().getIntExtra(KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_SETTING_START,0);
        endTime = getIntent().getIntExtra(KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_SETTING_END,0);
        if(endTime == 86400){
            endTime = endTime -1;
        }

        initData();
    }

    private void initData() {
        time = DateUtils.getStringTime2(startTime) + "-" + DateUtils.getStringTime2(endTime);
        tvVideoTimeSetting.setText(DateUtils.getStringTime2(startTime) + "-" + DateUtils.getStringTime2(endTime));
        setWeekPeriod(snoozeStartTime);
    }

    private void setWeekPeriod(int[] snoozeStartTime) {
        if(snoozeStartTime != null && snoozeStartTime.length >0){

            String str = "";
            int sum = 0;
            for(int i = 0 ; i<snoozeStartTime.length;i++){

                sum += snoozeStartTime[i];
                if(snoozeStartTime[i] == 1){
                    str += getString(R.string.monday_1);
                }else if(snoozeStartTime[i] == 2){
                    str += getString(R.string.tuesday_1);
                }else if(snoozeStartTime[i] == 3){
                    str += getString(R.string.wedensday_1);
                }else if(snoozeStartTime[i] == 4){
                    str += getString(R.string.thursday_1);
                }else if(snoozeStartTime[i] == 5){
                    str += getString(R.string.friday_1);
                }else if(snoozeStartTime[i] == 6){
                    str += getString(R.string.saturday_1);
                }else if(snoozeStartTime[i] == 7){
                    str += getString(R.string.sunday_1);
                }
            }
            if(sum == 28){
                tvPeriodConnect.setText(getString(R.string.week_day_1));
            }else{
                tvPeriodConnect.setText(str + "");
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setRealTimePeriod();
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void setRealTimePeriod() {
        String startStr = "";
        String endStr = "";
        try{
            startStr = DateUtils.getRegEx(time,"\\d+:\\d+").get(0);
            endStr = DateUtils.getRegEx(time,"\\d+:\\d+").get(1);
        }catch (Exception e){
            e.printStackTrace();
        }

        startTime = DateUtils.getSecondTime(startStr);
        endTime = DateUtils.getSecondTime(endStr);
        if(endStr.equals("23:59")){
            endTime = 86400;
        }
        Intent intent = new Intent();
        intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_SETTING_START, startTime);
        intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_SETTING_END, endTime);
        intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_PERIOD, snoozeStartTime);
        intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void showTimePickDialog() {
        if(mTimePickerDialog == null){
            mTimePickerDialog = new TimePickerDialog(WifiVideoLockRealTimePeriodActivity.this, tvVideoTimeSetting.getText().toString(), new TimePickerDialog.ConfirmAction() {
                @Override
                public void onClick(String startAndEndTime) {
                    tvVideoTimeSetting.setText(startAndEndTime + "");
                    time = startAndEndTime;
                }
            });
        }
        mTimePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            switch (requestCode){
                case KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_PERIOD_CODE:
                    snoozeStartTime = data.getIntArrayExtra(KeyConstants.WIFI_VIDEO_LOCK_REAL_TIME_PERIOD);
                    wifiSn = data.getStringExtra(KeyConstants.WIFI_SN);
                    setWeekPeriod(snoozeStartTime);
                    break;
            }
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
