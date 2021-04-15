package com.philips.easykey.lock.activity.device.videolock;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.utils.KeyConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiVideoLockFirwareNumberActivity extends BaseAddToApplicationActivity {

    @BindView(R.id.tv_hardware_version)
    TextView tvHardwareVersion;
    @BindView(R.id.tv_hard_version)
    TextView tvHardVersion;
    @BindView(R.id.iv_hard_version)
    ImageView ivHardVersion;

    private String wifiSN;
    private WifiLockInfo wifiLockInfoBySn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_video_firware_number);
        ButterKnife.bind(this);

        wifiSN = getIntent().getStringExtra(KeyConstants.WIFI_SN);


        wifiLockInfoBySn = MyApplication.getInstance().getWifiLockInfoBySn(wifiSN);

        if(wifiLockInfoBySn != null){
            if(wifiLockInfoBySn.getLockFirmwareVersion() != null){
                tvHardVersion.setText(wifiLockInfoBySn.getLockFirmwareVersion());
            }

            if(wifiLockInfoBySn.getWifiVersion() != null){
                tvHardwareVersion.setText(wifiLockInfoBySn.getWifiVersion());
            }

            if(wifiLockInfoBySn.getIsAdmin() == 1){
                ivHardVersion.setVisibility(View.VISIBLE);
            }else{
                ivHardVersion.setVisibility(View.GONE);
            }
        }

    }


    @OnClick(R.id.back)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }

    }

}
