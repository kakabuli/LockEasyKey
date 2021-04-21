package com.philips.easykey.lock.activity.addDevice.zigbeelocknew;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.gateway.AddGatewaySuccessActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.deviceaddpresenter.GatewayBindPresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.DeviceGatewayBindListView;
import com.philips.easykey.lock.mvp.view.deviceaddview.GatewayBindView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.BindGatewayResultBean;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDeviceZigbeeLockNewZeroActivity  extends BaseActivity<GatewayBindView, GatewayBindPresenter<GatewayBindView>> implements GatewayBindView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.cancel_bind)
    Button cancelBind;
    private String deviceSN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_gateway_add_three);
        ButterKnife.bind(this);
        Intent scanIntent = getIntent();
        deviceSN = scanIntent.getStringExtra("deviceSN");
        initView();
    }

    private void initView() {
        if (TextUtils.isEmpty(deviceSN)) {
            ToastUtils.showShort(getString(R.string.unbind_not_have_devicesn));
            return;
        }
        if (NetUtil.isNetworkAvailable()) {
            LogUtils.d("deviceSN    " + deviceSN);
            mPresenter.bindGateway(deviceSN);
        } else {
            ToastUtils.showShort(getString(R.string.network_exception));
        }

    }

    @Override
    protected GatewayBindPresenter<GatewayBindView> createPresent() {
        return new GatewayBindPresenter();
    }

    @OnClick({R.id.back, R.id.cancel_bind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.cancel_bind:
//                Intent cancelBind = new Intent(this, DeviceGatewayBindListView.class);
//                startActivity(cancelBind);
//                finish();
                break;
        }
    }


    @Override
    public void bindGatewaySuccess(String deviceSN) {
        Intent successIntent = new Intent(this, AddGatewaySuccessActivity.class);
        startActivity(successIntent);
        finish();


    }

    @Override
    public void bindGatewaySuitSuccess(String deviceSN, List<BindGatewayResultBean.DataBean.DeviceListBean> mDeviceList,boolean isbindMeMe) {
        //绑定套装成功
        Intent intent = new Intent(this, AddDeviceZigbeeLockNewFirstActivity.class);
        intent.putExtra(KeyConstants.GATEWAY_ID, deviceSN);
        startActivity(intent);
    }

    @Override
    public void bindGatewayFail(String code, String msg) {
        if ("812".equals(code)) { //通知管理员确认
            ToastUtils.showLong(R.string.already_notify_admin);
            Intent cancelBind = new Intent(this, DeviceGatewayBindListView.class);
            startActivity(cancelBind);
            finish();
        } else if ("813".equals(code)) { //您已绑定该网关
            ToastUtils.showLong(R.string.already_bind_gatway);
            Intent cancelBind = new Intent(this, DeviceGatewayBindListView.class);
            startActivity(cancelBind);
            finish();
        } else {
            Intent failIntent = new Intent(this, AddDeviceZigbeeLockNewScanFailActivity.class);
            failIntent.putExtra("code", code);
            failIntent.putExtra("msg", msg);
            startActivity(failIntent);
            finish();
        }
    }

    @Override
    public void bindGatewaySuitFail(String code, String msg) {
        //绑定套装失败
        Intent intent = new Intent(this, AddDeviceZigbeeLockNewFirstActivity.class);
        intent.putExtra(KeyConstants.GATEWAY_ID, "");
        startActivity(intent);
    }

    @Override
    public void bindGatewayThrowable(Throwable throwable) {
        LogUtils.d("绑定网关异常" + throwable);
        Intent failIntent = new Intent(this, AddDeviceZigbeeLockNewScanFailActivity.class);
        startActivity(failIntent);
        finish();
    }

}