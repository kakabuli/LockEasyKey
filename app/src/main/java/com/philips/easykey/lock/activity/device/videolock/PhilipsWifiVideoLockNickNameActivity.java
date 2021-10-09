package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockMorePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IWifiVideoLockMoreView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.utils.KeyConstants;

public class PhilipsWifiVideoLockNickNameActivity extends BaseActivity<IWifiVideoLockMoreView, WifiVideoLockMorePresenter<IWifiVideoLockMoreView>>
        implements IWifiVideoLockMoreView {


    private Button confirm;
    private EditText mEtNickName;
    private ImageView back;
    private String wifiSn = "";
    private String mNickName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_nick_name);


        initView();
        initData();
        initListener();
    }

    private void initData() {
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        mNickName = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_NEW_NAME) + "";
        mEtNickName.setText(mNickName);
        if(TextUtils.isEmpty(mEtNickName.getText().toString().trim())){
            changeConfirmBtnStyle(false);
        }else {
            changeConfirmBtnStyle(true);
        }
    }

    private void changeConfirmBtnStyle(boolean isCanLogin) {
        confirm.setSelected(isCanLogin);
    }

    private void initListener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mNickName.equals(mEtNickName.getText().toString().trim())){
                    mPresenter.setNickName(wifiSn,mEtNickName.getText().toString().trim());
                }
            }
        });

        mEtNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String account = mEtNickName.getText().toString().trim();
                if(TextUtils.isEmpty(account)) {
                    changeConfirmBtnStyle(false);
                } else {
                    changeConfirmBtnStyle(!TextUtils.isEmpty(s.toString()));
                }
            }
        });
    }

    private void initView() {
        confirm = findViewById(R.id.confirm);
        mEtNickName = findViewById(R.id.et_nickname);
        back = findViewById(R.id.back);
    }

    @Override
    protected WifiVideoLockMorePresenter<IWifiVideoLockMoreView> createPresent() {
        return new WifiVideoLockMorePresenter<>();
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
        ToastUtils.showShort(R.string.device_nick_name_update_success);
        Intent intent = new Intent();
        intent.putExtra(KeyConstants.WIFI_LOCK_NEW_NAME, mEtNickName.getText().toString().trim());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void modifyDeviceNicknameError(Throwable throwable) {
        ToastUtils.showShort(R.string.philips_device_nick_name_update_failed);
        finish();
    }

    @Override
    public void modifyDeviceNicknameFail(BaseResult baseResult) {
        ToastUtils.showShort(R.string.philips_device_nick_name_update_failed);
        finish();
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
    public void onSettingCallBack(boolean flag, int code) {

    }
}