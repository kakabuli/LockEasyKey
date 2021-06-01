package com.philips.easykey.lock.activity.addDevice.zigbee;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceBindGatewayListActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.deviceaddpresenter.AddZigbeeLockPresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.IAddZigbeeLockView;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.DeviceOnLineBean;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.handPwdUtil.Constants;


public class AddZigbeeLockFourthActivity extends BaseActivity<IAddZigbeeLockView,AddZigbeeLockPresenter<IAddZigbeeLockView>> implements IAddZigbeeLockView {

    ImageView back;
    ImageView addCateyeAwating;

    private Animation operatingAnim;
    private String gatewayId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_zigbeelock_add_scan);

        back = findViewById(R.id.back);
        addCateyeAwating = findViewById(R.id.add_cateye_awating);

        back.setOnClickListener(v -> {
            Intent backIntent=new Intent(this, DeviceBindGatewayListActivity.class);
            startActivity(backIntent);
            finish();
        });

        initAnimation();
        startAnimation();


    }

    private void initData() {
       gatewayId= (String) SPUtils.getProtect(Constants.GATEWAYID,"");
        if (!TextUtils.isEmpty(gatewayId)){
            LogUtils.d("允许设备入网的网关id",gatewayId);
            mPresenter.openJoinAllow(gatewayId);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();

    }

    @Override
    protected AddZigbeeLockPresenter<IAddZigbeeLockView> createPresent() {
        return new AddZigbeeLockPresenter<>();
    }


    /**
     * 初始化动画
     */
    private void initAnimation(){
        operatingAnim = AnimationUtils.loadAnimation(this, R.anim.device_zigbeelock);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAnimation();
    }

    /**
     * 启动搜索图片的动画
     */
    private void startAnimation() {
        if (operatingAnim != null) {
            addCateyeAwating.startAnimation(operatingAnim);
        } else {
            addCateyeAwating.setAnimation(operatingAnim);
            addCateyeAwating.startAnimation(operatingAnim);
        }
    }

    /**
     * 停止动画
     *
     * @param
     */
    private void stopAnimation() {
        addCateyeAwating.clearAnimation();
    }


    @Override
    public void netInSuccess() {
        //入网成功
        mPresenter.deviceZigbeeIsOnLine(gatewayId);

    }

    @Override
    public void netInFail() {
        Intent failIntent=new Intent(this,AddZigbeeLockFailActivity.class);
        startActivity(failIntent);
        finish();
        LogUtils.d("设备入网失败");
    }

    @Override
    public void netInThrowable() {
        Intent failIntent=new Intent(this,AddZigbeeLockFailActivity.class);
        startActivity(failIntent);
        finish();
        LogUtils.d("设备入网异常");
    }

    @Override
    public void addZigbeeSuccess(DeviceOnLineBean deviceOnLineBean) {
        stopAnimation();
        String deviceId=deviceOnLineBean.getDeviceId();
        String gwId=deviceOnLineBean.getGwId();
        Intent successIntent=new Intent(this,AddZigbeeLockSuccessActivity.class);
        successIntent.putExtra(KeyConstants.GATEWAY_ID,gwId);
        successIntent.putExtra(KeyConstants.DEVICE_ID,deviceId);
        //清除锁上的密码
        startActivity(successIntent);
        finish();
        LogUtils.d("设备添加成功");
    }

    @Override
    public void addZigbeeFail() {
        Intent failIntent=new Intent(this,AddZigbeeLockFailActivity.class);
        startActivity(failIntent);
        finish();
        LogUtils.d("设备添加异常");
    }

    @Override
    public void addZigbeeThrowable() {
        Intent failIntent=new Intent(this,AddZigbeeLockFailActivity.class);
        startActivity(failIntent);
        finish();
        LogUtils.d("设备添加异常");
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAnimation();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isCosumenBackKey();
        }
        return false;
    }

    private boolean isCosumenBackKey() {
        Intent backIntent=new Intent(this, DeviceBindGatewayListActivity.class);
        startActivity(backIntent);
        finish();
        return true;
    }

}
