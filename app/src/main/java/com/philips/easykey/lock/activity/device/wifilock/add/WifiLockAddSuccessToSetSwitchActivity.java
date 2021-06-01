package com.philips.easykey.lock.activity.device.wifilock.add;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;

import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.activity.addDevice.singleswitch.SwipchLinkActivity;
import com.philips.easykey.lock.activity.addDevice.singleswitch.SwipchLinkNo;

import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockAddSuccessPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockAddSuccessView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;

public class WifiLockAddSuccessToSetSwitchActivity extends BaseActivity<IWifiLockAddSuccessView
        , WifiLockAddSuccessPresenter<IWifiLockAddSuccessView>> implements IWifiLockAddSuccessView {

    TextView right_now_set;
    ImageView close;


    private String wifiSn;
    private WifiLockInfo wifiLockInfo;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_set_single_switch);

        right_now_set = findViewById(R.id.tv_right_now_set);
        close = findViewById(R.id.close);

        close.setOnClickListener(v -> {
            MyApplication.getInstance().getAllDevicesByMqtt(true);
            handler.postDelayed(runnable1, 1000);
        });
        right_now_set.setOnClickListener(v -> {
            MyApplication.getInstance().getAllDevicesByMqtt(true);
            handler.postDelayed(runnable, 1000);
        });

        initData();
        initView();

    }

    @Override
    protected WifiLockAddSuccessPresenter<IWifiLockAddSuccessView> createPresent() {
        return new WifiLockAddSuccessPresenter<>();
    }

    private void initData() {
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);

    }
    public void initView(){
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    }


    Runnable runnable1 = new Runnable() {
        @Override
        public void run() {

//            finish();
//                Intent intent = new Intent(this, WifiLockAddSuccessActivity.class);
//                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
//                startActivity(intent);
            Intent backIntent=new Intent(WifiLockAddSuccessToSetSwitchActivity.this, MainActivity.class);
            startActivity(backIntent);
            overridePendingTransition(R.anim.page_centerzoom_enter_quick, R.anim.page_centerzoom_exit);

            handler.removeCallbacks(runnable1);

        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            setSwitch();

            handler.removeCallbacks(runnable);

        }
    };
    public void setSwitch(){
        Intent intent;

        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        finish();
        LogUtils.d("--kaadas--wifiLockInfo=="+wifiLockInfo);

        if (wifiLockInfo != null){
            if (wifiLockInfo.getSingleFireSwitchInfo() != null) {

                int SwitchNumber = wifiLockInfo.getSingleFireSwitchInfo().getSwitchNumber().size();

                if (SwitchNumber > 0) {
                    intent = new Intent(WifiLockAddSuccessToSetSwitchActivity.this, SwipchLinkActivity.class);
                    intent.putExtra(KeyConstants.SWITCH_NUMBER, SwitchNumber);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    startActivity(intent);
                } else {
                    intent = new Intent(WifiLockAddSuccessToSetSwitchActivity.this, SwipchLinkNo.class);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    startActivity(intent);
                }
            }else {
                intent = new Intent(WifiLockAddSuccessToSetSwitchActivity.this, SwipchLinkNo.class);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                startActivity(intent);
            }
        }else {
            intent = new Intent(WifiLockAddSuccessToSetSwitchActivity.this, SwipchLinkNo.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            startActivity(intent);
        }
    }

    @Override
    public void onSetNameSuccess() {

    }

    @Override
    public void onSetNameFailedNet(Throwable throwable) {

    }

    @Override
    public void onSetNameFailedServer(BaseResult baseResult) {

    }
}
