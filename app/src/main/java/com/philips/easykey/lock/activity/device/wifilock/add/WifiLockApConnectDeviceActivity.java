package com.philips.easykey.lock.activity.device.wifilock.add;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiApWifiSetUpPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockAPWifiSetUpView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.SocketManager;
import com.philips.easykey.lock.utils.WifiUtils;


public class WifiLockApConnectDeviceActivity extends BaseActivity<IWifiLockAPWifiSetUpView, WifiApWifiSetUpPresenter<WifiApWifiSetUpPresenter>>
        implements IWifiLockAPWifiSetUpView {


    ImageView back;
    ImageView help;
    ImageView ivAnim;
    CheckBox cbSendWifiAccountPassword;
    CheckBox cbSuccess;
    CheckBox bindSuccess;
    EditText tvSupportList;
    private SocketManager socketManager = SocketManager.getInstance();
    private int func;
    private String randomCode;
    private String wifiSn;
    private String sPassword;
    private String sSsid;
    private boolean isSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_connect_device);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        ivAnim = findViewById(R.id.iv_anim);
        cbSendWifiAccountPassword = findViewById(R.id.cb_send_wifi_account_password);
        cbSuccess = findViewById(R.id.cb_success);
        bindSuccess = findViewById(R.id.bind_success);
        tvSupportList = findViewById(R.id.tv_support_list);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> startActivity(new Intent(this, WifiLockHelpActivity.class)));

        sSsid = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_SSID);
        sPassword = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_PASSWORD);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        randomCode = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE);
        func = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_FUNC, 0);
        Log.e("凯迪仕", "onCreate: sSsid " + sSsid + "   sPassword " + sPassword);
        thread.start();
        LogUtils.d("数据是 2 randomCode " + randomCode);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
        if (socketManager != null) {
            socketManager.destroy();
        }
    }

    @Override
    protected WifiApWifiSetUpPresenter<WifiApWifiSetUpPresenter> createPresent() {
        return new WifiApWifiSetUpPresenter<>();
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
                ToastUtils.showShort(R.string.bind_failed);
                Intent intent = new Intent(WifiLockApConnectDeviceActivity.this, WifiLockAPAddFailedActivity.class);
                startActivity(intent);
//                if (errorCode == -1) {
//                    Toast.makeText(WifiLockApConnectDeviceActivity.this, "读取失败", Toast.LENGTH_SHORT).show();
//                } else if (errorCode == -2) {
//                    Toast.makeText(WifiLockApConnectDeviceActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
//                } else if (errorCode == -3) {
//                    Toast.makeText(WifiLockApConnectDeviceActivity.this, "管理员密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
//                } else if (errorCode == -4) {
//                    Toast.makeText(WifiLockApConnectDeviceActivity.this, "写数据错误", Toast.LENGTH_SHORT).show();
//                } else if (errorCode == -5) {
//                    Toast.makeText(WifiLockApConnectDeviceActivity.this, "返回错误", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }


    private void onSuccess() {
        WifiUtils.getInstance(MyApplication.getInstance()).disableWiFi();
        mPresenter.onReadSuccess(wifiSn, randomCode, sSsid, func);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cbSuccess.setVisibility(View.VISIBLE);
            }
        });
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
                    SocketManager.ReadResult readResult = socketManager.readWifiData();
                    if (readResult.resultCode >= 0) { //读取成功
                        String sResult = new String(readResult.data);
                        LogUtils.d("读取成功   "  + sResult );
                        if (!TextUtils.isEmpty(sResult) && sResult.startsWith("APSuccess")) {
                            onSuccess();
                            isSuccess = true;
                            socketManager.destroy();
                        } else {
                            onError(socketManager, -5);
                            LogUtils.d("读数据失败   " + sResult);
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


    @Override
    public void onBindSuccess(String wifiSn) {
        Intent intent = new Intent(this, WifiLockAddSuccessActivity.class);
        intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
        startActivity(intent);
    }

    @Override
    public void onBindFailed(BaseResult baseResult) {
        ToastUtils.showShort(R.string.bind_failed);
        Intent intent = new Intent(this, WifiLockAPAddFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onBindThrowable(Throwable throwable) {
        ToastUtils.showShort(R.string.bind_failed);
        Intent intent = new Intent(this, WifiLockAPAddFailedActivity.class);
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
        ToastUtils.showShort(R.string.bind_failed);
        Intent intent = new Intent(this, WifiLockAPAddFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onUpdateThrowable(Throwable throwable) {
        ToastUtils.showShort(R.string.bind_failed);
        Intent intent = new Intent(this, WifiLockAPAddFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onSendSuccess() {

    }

    @Override
    public void onSendFailed() {
        ToastUtils.showShort(R.string.bind_failed);
        Intent intent = new Intent(this, WifiLockAPAddFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onReceiverFailed() {
        ToastUtils.showShort(R.string.bind_failed);
        Intent intent = new Intent(this, WifiLockAPAddFailedActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, true);
        startActivity(intent);
    }

    @Override
    public void onReceiverSuccess() {

    }

}
