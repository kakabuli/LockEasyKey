package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.singlefireswitchpresenter.SingleFireSwitchSettingPresenter;
import com.philips.easykey.lock.mvp.view.singlefireswitchview.SingleFireSwitchView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.AddSingleFireSwitchBean;
import com.philips.easykey.lock.utils.KeyConstants;

public class WifiLockAddToSetSwitchActivity extends BaseActivity<SingleFireSwitchView, SingleFireSwitchSettingPresenter<SingleFireSwitchView>> implements SingleFireSwitchView {

    ImageView back;
    TextView head;
    ImageView ivAnim;
    TextView buttonNext;

    private WifiLockInfo wifiLockInfoChange;
    private String wifiSn;

    private static final int TO_SET_ALL_REQUEST_CODE = 10103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_scan_for_switch);

        back = findViewById(R.id.back);
        head = findViewById(R.id.head);
        ivAnim = findViewById(R.id.iv_anim);
        buttonNext = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        buttonNext.setOnClickListener(v -> {
            Intent intent = new Intent(this, WifiLockWaitForSwitchActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            intent.putExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE, wifiLockInfoChange);
//                startActivity(intent);
            startActivityForResult(intent,TO_SET_ALL_REQUEST_CODE);
//                finish();
        });

        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();

        initData();

    }

    @Override
    protected SingleFireSwitchSettingPresenter<SingleFireSwitchView> createPresent() {
        return new SingleFireSwitchSettingPresenter<>();
    }

    private void initData() {

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfoChange = (WifiLockInfo) getIntent().getSerializableExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE);
        
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TO_SET_ALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
//                LogUtils.d("--kaadas--11111WIFI_LOCK_INFO_CHANGE_RESULT=="+data.getBooleanExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE_RESULT,false));
//                Intent intent;
//                intent = new Intent(this, SwipchLinkActivity.class);
//                intent.putExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE_RESULT, data.getBooleanExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE_RESULT,false));
                setResult(RESULT_OK, data);
                finish();
            }
        }
    }
    @Override
    public void settingDeviceSuccess() {

    }

    @Override
    public void settingDeviceFail() {

    }

    @Override
    public void settingDeviceThrowable() {

    }

    @Override
    public void gettingDeviceSuccess() {

    }

    @Override
    public void gettingDeviceFail() {

    }

    @Override
    public void gettingDeviceThrowable() {

    }

    @Override
    public void addDeviceSuccess(AddSingleFireSwitchBean addSingleFireSwitchBean) {

    }

    @Override
    public void addDeviceFail() {

    }

    @Override
    public void addDeviceThrowable() {

    }

    @Override
    public void bindingAndModifyDeviceSuccess() {

    }

    @Override
    public void bindingAndModifyDeviceFail() {

    }

    @Override
    public void bindingAndModifyDeviceThrowable() {

    }


}
