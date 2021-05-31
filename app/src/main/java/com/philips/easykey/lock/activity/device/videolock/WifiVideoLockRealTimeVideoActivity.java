package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiLockRealTimeVideoPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockRealTimeVideoView;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PManager;
import com.blankj.utilcode.util.LogUtils;


public class WifiVideoLockRealTimeVideoActivity extends BaseActivity<IWifiLockRealTimeVideoView,
        WifiLockRealTimeVideoPresenter<IWifiLockRealTimeVideoView>> implements IWifiLockRealTimeVideoView {

    ImageView ivSetting;
    ImageView back;
    SurfaceView mSufaceView;
    TextView tvTemporaryPassword;
    LinearLayout llyTemporaryPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_real_time_video);

        ivSetting = findViewById(R.id.iv_setting);
        back = findViewById(R.id.back);
        mSufaceView = findViewById(R.id.surface_view);
        tvTemporaryPassword = findViewById(R.id.tv_temporary_password);
        llyTemporaryPassword = findViewById(R.id.lly_temporary_password);

        back.setOnClickListener(v -> {
            mPresenter.release();
            finish();
        });
        ivSetting.setOnClickListener(v -> startActivity(new Intent(WifiVideoLockRealTimeVideoActivity.this, PhilipsWifiVideoLockRealTimeActivity.class)));
        findViewById(R.id.iv_mute).setOnClickListener(v -> {
            int ret = -999;
            ret= XMP2PManager.getInstance().startAudioStream();
            if(ret>=0){
                if(!XMP2PManager.getInstance().isEnableAudio()){
                    XMP2PManager.getInstance().enableAudio(true);
                }
            }
        });

        LogUtils.d( "WifiLockRealTimeVideoActivity onCreate: ");
        mPresenter.startRealTimeVideo(mSufaceView);
    }

    @Override
    protected WifiLockRealTimeVideoPresenter<IWifiLockRealTimeVideoView> createPresent() {
        return new WifiLockRealTimeVideoPresenter<>();
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

    @Override
    protected void onDestroy() {
        mPresenter.release();
        super.onDestroy();
    }

}
