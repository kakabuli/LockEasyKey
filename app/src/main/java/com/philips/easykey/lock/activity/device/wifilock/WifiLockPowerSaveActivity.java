package com.philips.easykey.lock.activity.device.wifilock;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockSafeModePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockSafeModeView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.utils.KeyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiLockPowerSaveActivity extends BaseActivity<IWifiLockSafeModeView, WifiLockSafeModePresenter<IWifiLockSafeModeView>>
          {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.head_title)
    TextView headTitle;

    private String wifiSn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_power_save_model);
        ButterKnife.bind(this);

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        mPresenter.init(wifiSn);

    }

    @Override
    protected WifiLockSafeModePresenter<IWifiLockSafeModeView> createPresent() {
        return new WifiLockSafeModePresenter<>();
    }


    @OnClick(R.id.back)
    public void onViewClicked() {
        finish();
    }
}
