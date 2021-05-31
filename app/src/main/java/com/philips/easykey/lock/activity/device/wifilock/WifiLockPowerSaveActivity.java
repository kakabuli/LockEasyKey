package com.philips.easykey.lock.activity.device.wifilock;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockSafeModePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockSafeModeView;
import com.philips.easykey.lock.utils.KeyConstants;


public class WifiLockPowerSaveActivity extends BaseActivity<IWifiLockSafeModeView, WifiLockSafeModePresenter<IWifiLockSafeModeView>>
          {

    ImageView back;
    TextView headTitle;

    private String wifiSn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_power_save_model);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);

        back.setOnClickListener(v -> finish());

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        mPresenter.init(wifiSn);

    }

    @Override
    protected WifiLockSafeModePresenter<IWifiLockSafeModeView> createPresent() {
        return new WifiLockSafeModePresenter<>();
    }

}
