package com.philips.easykey.lock.activity.device.wifilock;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockMorePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockMoreView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.result.MultiCheckOTAResult;
import com.philips.easykey.lock.utils.KeyConstants;

import java.util.List;

public class PhilipsWifiLockMessagePushActivity extends BaseActivity<IWifiLockMoreView, WifiLockMorePresenter<IWifiLockMoreView>>
        implements IWifiLockMoreView{

    ImageView back;
    ImageView ivWanderingAlarm;
    RelativeLayout rlWanderingAlarm;
    ImageView ivDoorBell;
    RelativeLayout rlDoorBell;
    RelativeLayout rlLockInfo;
    ImageView ivLockInfo;
    RelativeLayout rlMessageFree;
    ImageView ivMessageFree;

    private String wifiSn;
    private WifiLockInfo wifiLockInfo;

    private int pushSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_message_push);

        back = findViewById(R.id.back);
        ivWanderingAlarm = findViewById(R.id.iv_wandering_alarm);
        rlWanderingAlarm = findViewById(R.id.rl_wandering_alarm);
        ivDoorBell = findViewById(R.id.iv_door_bell);
        rlDoorBell = findViewById(R.id.rl_door_bell);
        rlLockInfo = findViewById(R.id.rl_lock_info);
        ivLockInfo = findViewById(R.id.iv_lock_info);
        rlMessageFree = findViewById(R.id.rl_message_free);
        ivMessageFree = findViewById(R.id.iv_message_free);

        back.setOnClickListener(v -> finish());
        rlMessageFree.setOnClickListener(v -> {
            if(ivMessageFree.isSelected()){
                mPresenter.updateSwitchStatus(1,wifiSn);
            }else{
                mPresenter.updateSwitchStatus(2,wifiSn);
            }
        });
        ivMessageFree.setOnClickListener(v -> {
            if(ivMessageFree.isSelected()){
                mPresenter.updateSwitchStatus(1,wifiSn);
            }else{
                mPresenter.updateSwitchStatus(2,wifiSn);
            }
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        initData();
    }


    private void initData() {
        if(wifiLockInfo != null){
            pushSwitch = wifiLockInfo.getPushSwitch();
            if(pushSwitch == 2){
                ivMessageFree.setSelected(true);
            }else{
                ivMessageFree.setSelected(false);
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
        if(!PhilipsWifiLockMessagePushActivity.this.isFinishing()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    ToastUtils.showShort(getString(R.string.modify_success));
                    if(status == 2){
                        ivMessageFree.setSelected(true);
                    }else {
                        ivMessageFree.setSelected(false);
                    }
                }
            });
        }

    }

    @Override
    public void onUpdatePushStatusFailed(BaseResult result) {
        /*if(!WifiLockMessagePushActivity.this.isFinishing()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(getString(R.string.modify_failed));
                }
            });
        }*/
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
