package com.philips.easykey.lock.activity.device.wifilock.x9;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.WifiLockMoreActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.x9.WifiLockOpenDirectionPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.x9.IWifiLockOpenDirectionView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.ToastUtils;


public class WifiLockOpenDirectionActivity extends BaseActivity<IWifiLockOpenDirectionView, WifiLockOpenDirectionPresenter<IWifiLockOpenDirectionView>>
        implements IWifiLockOpenDirectionView {

    RelativeLayout rlLeftLayout;
    RelativeLayout rlRightLayout;
    CheckBox ckLeft;
    CheckBox ckRight;

    private String wifiSn = "";
    private WifiLockInfo wifiLockInfo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_open_direction);

        rlLeftLayout = findViewById(R.id.left_layout);
        rlRightLayout = findViewById(R.id.right_layout);
        ckLeft = findViewById(R.id.ck_left);
        ckRight = findViewById(R.id.ck_right);

        findViewById(R.id.back).setOnClickListener(v -> setOpenDirection());
        findViewById(R.id.left_layout).setOnClickListener(v -> {
            ckLeft.setChecked(true);
            ckRight.setChecked(false);
        });
        findViewById(R.id.right_layout).setOnClickListener(v -> {
            ckLeft.setChecked(false);
            ckRight.setChecked(true);
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

        if(wifiLockInfo != null){
            if(wifiLockInfo.getOpenDirection() == 1){
                ckLeft.setChecked(true);
                ckRight.setChecked(false);
            }else{
                ckLeft.setChecked(false);
                ckRight.setChecked(true);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setOpenDirection();
    }



    private void setOpenDirection() {
        int openDirection = getOpenDirection();
        if(wifiLockInfo.getOpenDirection() == openDirection){
            finish();
        }else{
            showLoading(getString(R.string.wifi_video_lock_waiting));
            mPresenter.setOpenDirection(wifiSn,openDirection);
        }
    }

    private int getOpenDirection() {
        if (ckLeft.isChecked()) {
            return 1;
        }
        if (ckRight.isChecked()) {
            return 2;
        }
        return 1;
    }

    @Override
    protected WifiLockOpenDirectionPresenter<IWifiLockOpenDirectionView> createPresent() {
        return new WifiLockOpenDirectionPresenter<>();
    }

    @Override
    public void settingThrowable(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.modify_failed));
        finish();
    }

    @Override
    public void settingFailed() {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.modify_failed));
        finish();
    }

    @Override
    public void settingSuccess(int openDirection) {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.modify_success));
        Intent intent = new Intent(this, WifiLockMoreActivity.class);
        intent.putExtra(MqttConstant.SET_OPEN_DIRECTION,openDirection);
        setResult(RESULT_OK,intent);
        finish();
    }
}
