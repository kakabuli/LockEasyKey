package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockAddSuccessActivity;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiApWifiSetUpPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockAPWifiSetUpView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.SocketManager;
import com.philips.easykey.lock.utils.WifiUtils;
import com.philips.easykey.lock.widget.WifiCircleProgress;


public class WifiLockAddNewCheckWifiActivity extends BaseActivity<IWifiLockAPWifiSetUpView, WifiApWifiSetUpPresenter<WifiApWifiSetUpPresenter>>
        implements IWifiLockAPWifiSetUpView {

    ImageView back;
    ImageView help;
    WifiCircleProgress circleProgressBar2;
    TextView tvSendWifiInfo;
    ImageView ivSendWifiInfo;
    TextView tvSetSuccess;
    ImageView ivSetSuccess;
    TextView tvBindSuccess;
    ImageView ivBindSuccess;

    private Animation animation;
    SocketManager socketManager = SocketManager.getInstance();
    private int func;
    private String randomCode;
    private String wifiSn;
    private String sPassword;
    private String sSsid;
    private boolean isSuccess = false;
    private int times = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_check_wifi);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        circleProgressBar2 = findViewById(R.id.circle_progress_bar2);
        tvSendWifiInfo = findViewById(R.id.tv_send_wifi_info);
        ivSendWifiInfo = findViewById(R.id.iv_send_wifi_info);
        tvSetSuccess = findViewById(R.id.tv_set_success);
        ivSetSuccess = findViewById(R.id.iv_set_success);
        tvBindSuccess = findViewById(R.id.tv_bind_success);
        ivBindSuccess = findViewById(R.id.iv_bind_success);

        back.setOnClickListener(v -> showWarring());
        help.setOnClickListener(v -> startActivity(new Intent(this, WifiLockHelpActivity.class)));

        sSsid = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_SSID);
        sPassword = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_PASSWORD);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        randomCode = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE);
        func = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_FUNC, 0);
        times = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, 1);
        thread.start();
        LogUtils.d("数据是 2 randomCode " + randomCode);

        circleProgressBar2.setValue(0);

        animation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(1000);
        animation.setRepeatCount(-1);//动画的反复次数
        animation.setFillAfter(false);//设置为true，动画转化结束后被应用

        changeState(1);
    }

    @Override
    protected WifiApWifiSetUpPresenter<WifiApWifiSetUpPresenter> createPresent() {
        return new WifiApWifiSetUpPresenter<>();
    }

    private void onBindSuccess() {
        startActivity(new Intent(this, WifiLockAddNewBindSuccesssActivity.class));

    }

    private void onBindFailed() {
        startActivity(new Intent(this, WifiLockAddNewBindFailedActivity.class));
    }


    private Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
            if (socketManager.isStart()) { //连接成功
                LogUtils.d("连接成功");
                byte[] bSsid ;
                byte[] bPwd = sPassword.getBytes();
                String wifiName = (String) SPUtils.get(KeyConstants.WIFI_LOCK_CONNECT_NAME, "");
                if (sSsid.equals(wifiName)) {
                    String pwdByteString = (String) SPUtils.get(KeyConstants.WIFI_LOCK_CONNECT_ORIGINAL_DATA, "");
                    bSsid = Rsa.hex2byte2(pwdByteString);
                } else {
                    bSsid = sSsid.getBytes();
                }

                byte[] data = new byte[96];
                System.arraycopy(bSsid, 0, data, 0, bSsid.length);
                System.arraycopy(bPwd, 0, data, 32, bPwd.length);
                int writeResult = socketManager.writeData(data);
                if (writeResult == 0) {
                    LogUtils.d("发送账号密码成功   开始读取数据");
                    SocketManager.ReadResult readResult = socketManager.readWifiDataTimeout(60 * 1000);
                    if (readResult.resultCode >= 0) { //读取成功
                        String sResult = new String(readResult.data);
                        LogUtils.d("读取成功   " + sResult);
                        if (!TextUtils.isEmpty(sResult) && sResult.startsWith("APSuccess")) {
                            changeState(2);
                            onSuccess();
                            isSuccess = true;
                            socketManager.destroy();
                        } else if (!TextUtils.isEmpty(sResult) && sResult.startsWith("APError")) {

                            if (times < 5) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtils.showShort(getString(R.string.recheck_wifi_password));
                                        Intent intent = new Intent(WifiLockAddNewCheckWifiActivity.this, WifiLockAddNewInputWifiActivity.class);
                                        intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                                        intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, randomCode);
                                        intent.putExtra(KeyConstants.WIFI_LOCK_FUNC, func);
                                        intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, times + 1);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            } else { //五次失败
                                socketManager.writeData("************************************************************************************************APClose".getBytes());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onWiFIAndPWDError();
                                    }
                                });

                            }
                        } else {
                            onError(socketManager, -5);
                        }
                    } else {
                        onError(socketManager, -1);
                        LogUtils.d("读数据失败   " + writeResult);
                    }
                } else { //写数据失败
                    LogUtils.d("写数据失败   " + writeResult);
                    onError(socketManager, -4);
                }
            } else {  //连接失败
                LogUtils.d("连接失败");
                onError(socketManager, -2);
            }
        }
    };

    private void onWiFIAndPWDError() {
        AlertDialogUtil.getInstance().noEditSingleCanNotDismissButtonDialog(
                WifiLockAddNewCheckWifiActivity.this, "", getString(R.string.philips_activity_wifi_lock_add_new_checkwifi_dialog_1),
                getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                        onError(socketManager, -5);
                    }

                    @Override
                    public void right() {

                        onError(socketManager, -5);
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    /**
     * @param socketManager
     * @param errorCode     -1 读取失败  -2 连接失败  -3 校验失败
     */
    public void onError(SocketManager socketManager, int errorCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    return;
                }
                finish();
//                Toast.makeText(WifiLockAddNewCheckWifiActivity.this, R.string.bind_failed+"=="+errorCode, Toast.LENGTH_SHORT).show();

                if (errorCode == -1) {
                    ToastUtils.showShort(getString(R.string.philips_activity_wifi_lock_add_new_checkwifi_dialog_2));
                } else if (errorCode == -2) {
                    ToastUtils.showShort(getString(R.string.philips_activity_wifi_lock_add_new_checkwifi_dialog_3));
                } else if (errorCode == -3) {
                    ToastUtils.showShort(getString(R.string.philips_activity_wifi_lock_add_new_checkwifi_dialog_4));
                } else if (errorCode == -4) {
                    ToastUtils.showShort(getString(R.string.philips_activity_wifi_lock_add_new_checkwifi_dialog_5));
                } else if (errorCode == -5) {
                    ToastUtils.showShort(getString(R.string.philips_activity_wifi_lock_add_new_checkwifi_dialog_6));
                }
                Intent intent = new Intent(WifiLockAddNewCheckWifiActivity.this, WifiLockAddNewBindFailedActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * @param status 1初始状态  2 发送wifi信息成功  3设置成功 正在绑定  4
     */
    private void changeState(int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (status) {
                    case 1:
                        ivSendWifiInfo.setImageResource(R.mipmap.wifi_lock_add_state_refresh);
                        ivSetSuccess.setImageResource(R.mipmap.wifi_lock_add_state_wait);
                        ivBindSuccess.setImageResource(R.mipmap.wifi_lock_add_state_wait);

                        tvSendWifiInfo.setTextColor(getResources().getColor(R.color.color_333));
                        tvSetSuccess.setTextColor(getResources().getColor(R.color.color_cdcdcd));
                        tvBindSuccess.setTextColor(getResources().getColor(R.color.color_cdcdcd));

                        ivSendWifiInfo.startAnimation(animation);//開始动画
                        ivSetSuccess.clearAnimation();
                        ivBindSuccess.clearAnimation();

                        circleProgressBar2.setValue(10);

                        break;
                    case 2:
                        if(ivSendWifiInfo != null)
                            ivSendWifiInfo.setImageResource(R.mipmap.wifi_lock_add_state_complete);
                        if(ivSetSuccess != null)
                            ivSetSuccess.setImageResource(R.mipmap.wifi_lock_add_state_refresh);
                        if(ivBindSuccess != null)
                            ivBindSuccess.setImageResource(R.mipmap.wifi_lock_add_state_wait);
                        if(tvSendWifiInfo != null)
                            tvSendWifiInfo.setTextColor(getResources().getColor(R.color.color_333));
                        if(tvSetSuccess != null)
                            tvSetSuccess.setTextColor(getResources().getColor(R.color.color_333));
                        if(tvBindSuccess != null)
                            tvBindSuccess.setTextColor(getResources().getColor(R.color.color_cdcdcd));

                        if(ivSendWifiInfo != null)
                            ivSendWifiInfo.clearAnimation();//開始动画
                        if(ivSetSuccess != null)
                            ivSetSuccess.startAnimation(animation);
                        if(ivBindSuccess != null)
                            ivBindSuccess.clearAnimation();

                        circleProgressBar2.setValue(40);
                        break;
                    case 3:
                        ivSendWifiInfo.setImageResource(R.mipmap.wifi_lock_add_state_complete);
                        ivSetSuccess.setImageResource(R.mipmap.wifi_lock_add_state_complete);
                        ivBindSuccess.setImageResource(R.mipmap.wifi_lock_add_state_refresh);

                        tvSendWifiInfo.setTextColor(getResources().getColor(R.color.color_333));
                        tvSetSuccess.setTextColor(getResources().getColor(R.color.color_333));
                        tvBindSuccess.setTextColor(getResources().getColor(R.color.color_333));

                        ivSendWifiInfo.clearAnimation();//開始动画
                        ivSetSuccess.clearAnimation();
                        ivBindSuccess.startAnimation(animation);

                        circleProgressBar2.setValue(80);
                        break;

                }

            }
        });
    }


    private void onSuccess() {
        WifiUtils.getInstance(MyApplication.getInstance()).disableWiFi();
        mPresenter.onReadSuccess(wifiSn, randomCode, sSsid, func);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                changeState(3);
            }
        });
    }


    @Override
    public void onBindSuccess(String wifiSn) {
        Intent intent = new Intent(this, WifiLockAddSuccessActivity.class);
        intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
        startActivity(intent);
    }

    @Override
    public void onBindFailed(BaseResult baseResult) {
        ToastUtils.showShort(getString(R.string.bind_failed) +"--1");
        Intent intent = new Intent(this, WifiLockAddNewBindFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onBindThrowable(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.bind_failed) + "--2");
        Intent intent = new Intent(this, WifiLockAddNewBindFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onUpdateSuccess(String wifiSn) {
        Intent intent = new Intent(this, WifiLockAddSuccessActivity.class);
        intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
        startActivity(intent);
    }

    @Override
    public void onUpdateFailed(BaseResult baseResult) {
        ToastUtils.showShort(getString(R.string.bind_failed) +"--3");
        Intent intent = new Intent(this, WifiLockAddNewBindFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onUpdateThrowable(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.bind_failed) +"--4");
        Intent intent = new Intent(this, WifiLockAddNewBindFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onSendSuccess() {

    }

    @Override
    public void onSendFailed() {
        ToastUtils.showShort(getString(R.string.bind_failed) +"--5");
        Intent intent = new Intent(this, WifiLockAddNewBindFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onReceiverFailed() {
        ToastUtils.showShort(getString(R.string.bind_failed)+"--6");
        Intent intent = new Intent(this, WifiLockAddNewBindFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onReceiverSuccess() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }

    private void showWarring() {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(
                this
                , getString(R.string.activity_wifi_video_fifth_network),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#A4A4A4", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        //退出当前界面
                        Intent intent = new Intent(WifiLockAddNewCheckWifiActivity.this, WifiLockAddNewFirstActivity.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        showWarring();
    }
}
