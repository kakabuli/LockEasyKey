package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.SocketManager;


public class WifiLockAddNewModifyPasswordActivity extends BaseAddToApplicationActivity {

    TextView head;
    TextView notice;

    private int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_modify_password);

        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);

        findViewById(R.id.back).setOnClickListener(v -> showWarring());
        findViewById(R.id.help).setOnClickListener(v -> startActivity(new Intent(this,WifiLockHelpActivity.class)));
        findViewById(R.id.continue_check).setOnClickListener(v -> {
            String wifiName = NetUtil.getWifiName();
            LogUtils.d("连接状态  wifiName   " +wifiName+"  isConnected " +SocketManager.getInstance().isConnected());
            if (!(!TextUtils.isEmpty(wifiName ) && wifiName.contains("kaadas_AP")) || !SocketManager.getInstance().isConnected()){
                ToastUtils.showShort(R.string.philips_activity_wifi_lock_add_modify_password);
                startActivity(new Intent(this,WifiLockAddNewModifyPasswordDisconnectActivity.class));
                socketManager.destroy();
                return;
            }
            Intent intent = new Intent(this, WifiLockAddNewInputAdminPasswotdActivity.class);
            LogUtils.d(getLocalClassName()+"次数是   " + times + "  data 是否为空 "  );
            intent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES, times);
            startActivity(intent);
            finish();
        });

        times = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES,1);
        thread.start();
    }


    SocketManager socketManager = SocketManager.getInstance();
    Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
            int ret = 0;
            try {
                ret =socketManager.writeData("PinSimple".getBytes());
                LogUtils.d("写入状态  PinSimple  "+ret);
                Thread.sleep(89 * 1000);
                ret = socketManager.writeData("APError".getBytes());
                LogUtils.d("写入状态  APError1  "+ret);
                Thread.sleep(89 * 1000);
                ret = socketManager.writeData("APError".getBytes());
                LogUtils.d("写入状态  APError2  "+ret);
                Thread.sleep(89 * 1000);
                ret = socketManager.writeData("APError".getBytes());
                LogUtils.d("写入状态   APError3 "+ret);
                Thread.sleep(89 * 1000);
                ret = socketManager.writeData("ApClose".getBytes());
                LogUtils.d("写入状态  APError4·  "+ret);
                Thread.sleep(1000);
                socketManager.destroy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showWarring();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.interrupt();
    }


    private void showWarring(){
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
                        Intent intent = new Intent(WifiLockAddNewModifyPasswordActivity.this, WifiLockAddNewFirstActivity.class);
                        intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, times);
                        startActivity(intent);

                        finish();
                        socketManager.destroy();
                        thread.interrupt();
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }
}
