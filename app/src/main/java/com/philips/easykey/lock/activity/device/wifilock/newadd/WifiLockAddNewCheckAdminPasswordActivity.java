package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SocketManager;
import com.philips.easykey.lock.widget.WifiCircleProgress;

public class WifiLockAddNewCheckAdminPasswordActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    WifiCircleProgress circleProgressBar;
    TextView tvStateSendPwd;
    ImageView ivStateSendPwd;
    TextView tvStateLockCheckPwd;
    ImageView ivStateLockCheckPwd;
    TextView tvStateLockChecking;
    ImageView ivStateLockChecking;
    private Animation animation;
    private String adminPassword;
    private int times = 1; //次数从1开始


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_check_admin_password);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        circleProgressBar = findViewById(R.id.circle_progress_bar);
        tvStateSendPwd = findViewById(R.id.tv_state_send_pwd);
        ivStateSendPwd = findViewById(R.id.iv_state_send_pwd);
        tvStateLockCheckPwd = findViewById(R.id.tv_state_lock_check_pwd);
        ivStateLockCheckPwd = findViewById(R.id.iv_state_lock_check_pwd);
        tvStateLockChecking = findViewById(R.id.tv_state_lock_checking);
        ivStateLockChecking = findViewById(R.id.iv_state_lock_checking);

        back.setOnClickListener(v -> showWarring());
        help.setOnClickListener(v -> startActivity(new Intent(this, WifiLockHelpActivity.class)));

        adminPassword = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD);
        data = getIntent().getByteArrayExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_DATA);
        times = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES, 1);
        initView();
        circleProgressBar.setValue(0);
        changeState(1); //初始状态
        LogUtils.d("--Kaadas--"+getLocalClassName()+"次数是   " + times + "  data 是否为空 " + (data == null));
        if (times > 1) {
            if (socketManager.isConnected()) { //如果不是第一进来而且Socket是连接的   那么解析密码
                if (data != null) { //
                    LogUtils.d("--Kaadas--如果不是第一进来而且Socket是连接的   那么解析密码");
                    secondThread.start();
                } else { //修改过管理员密码进来的
                    LogUtils.d("--Kaadas--改过管理员密码进来的");
                    thirdThread.start();
                }
            } else {
                LogUtils.d("--Kaadas--socketManager断开连接");
                LogUtils.d("--Kaadas--"+getLocalClassName()+"次数是   " + times + "  data 是否为空 " + (data == null));
                AlertDialogUtil.getInstance().noEditSingleCanNotDismissButtonDialog(
                        WifiLockAddNewCheckAdminPasswordActivity.this, "", getString(R.string.philips_activity_wifi_lock_checkadmin_password),
                        getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                            @Override
                            public void left() {
                                startActivity(new Intent(WifiLockAddNewCheckAdminPasswordActivity.this, WifiLockAddNewScanFailedActivity.class));
                                finish();
                            }

                            @Override
                            public void right() {
                                startActivity(new Intent(WifiLockAddNewCheckAdminPasswordActivity.this, WifiLockAddNewScanFailedActivity.class));
                                finish();
                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(String toString) {

                            }
                        });
            }
        } else {
            LogUtils.d("--Kaadas--管理员密码输入次数<=1");
            firstThread.start();
        }
    }

    public void initView() {
        animation = new RotateAnimation(0, 359, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        animation.setRepeatCount(-1);//动画的反复次数
        animation.setFillAfter(true);//设置为true，动画转化结束后被应用
    }

    /**
     * @param status 1 初始状态   2 发送密码完成  3 密码验证成功
     */
    private void changeState(int status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                switch (status) {
                    case 1:
                        ivStateSendPwd.setImageResource(R.mipmap.wifi_lock_add_state_refresh);
                        ivStateLockCheckPwd.setImageResource(R.mipmap.wifi_lock_add_state_wait);
                        ivStateLockChecking.setImageResource(R.mipmap.wifi_lock_add_state_wait);

                        tvStateSendPwd.setTextColor(getResources().getColor(R.color.color_333));
                        tvStateLockCheckPwd.setTextColor(getResources().getColor(R.color.color_cdcdcd));
                        tvStateLockChecking.setTextColor(getResources().getColor(R.color.color_cdcdcd));

                        ivStateSendPwd.startAnimation(animation);//開始动画
                        ivStateLockCheckPwd.clearAnimation();
                        ivStateLockChecking.clearAnimation();

                        circleProgressBar.setValue(10);
                        break;
                    case 2:
                        ivStateSendPwd.setImageResource(R.mipmap.wifi_lock_add_state_complete);
                        ivStateLockCheckPwd.setImageResource(R.mipmap.wifi_lock_add_state_refresh);
                        ivStateLockChecking.setImageResource(R.mipmap.wifi_lock_add_state_wait);

                        tvStateSendPwd.setTextColor(getResources().getColor(R.color.color_333));
                        tvStateLockCheckPwd.setTextColor(getResources().getColor(R.color.color_333));
                        tvStateLockChecking.setTextColor(getResources().getColor(R.color.color_cdcdcd));

                        ivStateSendPwd.clearAnimation();//開始动画
                        ivStateLockCheckPwd.startAnimation(animation);
                        ivStateLockChecking.clearAnimation();
                        circleProgressBar.setValue(40);
                        break;
                    case 3:
                        ivStateSendPwd.setImageResource(R.mipmap.wifi_lock_add_state_complete);
                        ivStateLockCheckPwd.setImageResource(R.mipmap.wifi_lock_add_state_complete);
                        ivStateLockChecking.setImageResource(R.mipmap.wifi_lock_add_state_refresh);

                        tvStateSendPwd.setTextColor(getResources().getColor(R.color.color_333));
                        tvStateLockCheckPwd.setTextColor(getResources().getColor(R.color.color_333));
                        tvStateLockChecking.setTextColor(getResources().getColor(R.color.color_333));

                        ivStateSendPwd.clearAnimation();//開始动画
                        ivStateLockCheckPwd.clearAnimation();
                        ivStateLockChecking.startAnimation(animation);
                        circleProgressBar.setValue(80);
                        break;
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        showWarring();
    }

    /**
     * @param socketManager
     * @param errorCode     -1 读取失败  -2 连接失败  -3 校验失败
     */
    public void onError(SocketManager socketManager, int errorCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
                ToastUtils.showShort(R.string.bind_failed);
                startActivity(new Intent(WifiLockAddNewCheckAdminPasswordActivity.this, WifiLockAddNewScanFailedActivity.class));
                socketManager.destroy();
            }
        });
    }

    private void onSuccess(SocketManager.WifiResult result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WifiLockAddNewCheckAdminPasswordActivity.this, WifiLockAddNewInputWifiActivity.class);
                intent.putExtra(KeyConstants.WIFI_SN, new String(result.wifiSn));
                intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, Rsa.bytesToHexString(result.password));
                intent.putExtra(KeyConstants.WIFI_LOCK_FUNC, result.func);
                startActivity(intent);
                finish();
            }
        });
    }


    private SocketManager socketManager = SocketManager.getInstance();
    private byte[] data;
    //第一次进来时 启动的 Thread
    private Thread firstThread = new Thread() {
        @Override
        public void run() {
            super.run();
            int result = socketManager.startServer();
            LogUtils.d("端口打开结果   " + result + "   管理员密码是   " + adminPassword);
            if (result == 0) { //端口打开成功
                LogUtils.d("--Kaadas--端口打开成功，并且Wi-Fi热点连接上");
                SocketManager.ReadResult readResult = socketManager.readWifiData();
                data = readResult.data;
                LogUtils.d("读取结果2   " + readResult.toString());
                LogUtils.d("--Kaadas--resultCode："+readResult.resultCode+"，数据长度："+readResult.dataLen+"，读取结果："+readResult.toString());
                if (readResult.resultCode >= 0) {
                    if (readResult.dataLen < 46) {  //读取数据字数不够
                        onError(socketManager, -1);
                    } else { //读取到数据  而且数据够46
                        changeState(2);
                        if ("12345678".equals(adminPassword)) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showModifyPasswordDialog();
                                }
                            });
                            return;
                        }
                        SocketManager.WifiResult wifiResult = socketManager.parseWifiData(adminPassword, data);
                        LogUtils.d("--Kaadas--wifiResult："+wifiResult.result);

                        //解析数据   解析数据成功
                        if (wifiResult.result == 0) {
                            //发送CRCSuccess 通知锁端
                            int writeResult = socketManager.writeData(("CRCSuccess\r").getBytes());
                            //写入 CRCsuccess 成功
                            if (writeResult == 0) {
                                LogUtils.d("--Kaadas--写入 CRCsuccess 成功");
                                SocketManager.ReadResult readResult2 = socketManager.readWifiData();
                                if (readResult2.resultCode >= 0 && (new String(readResult2.data)).startsWith("APContinue")) {
                                    LogUtils.d("--Kaadas--收到APContinue");
                                    onSuccess(wifiResult);
                                    changeState(3);
                                } else {
                                    LogUtils.d("--Kaadas--readResult2.resultCode："+readResult2.resultCode);
                                    onError(socketManager, -5);
                                }
                            } else { //写入CRC失败
                                LogUtils.d("--Kaadas--写入CRC失败");
                                onError(socketManager, -4);
                            }
                        } else {
                            //解析数据失败  通知锁端蓄电？
                            LogUtils.d("--Kaadas--解析数据失败");
                            int writeResult = socketManager.writeData(("APError\r").getBytes());
                            if (writeResult == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onAdminPasswordError();
                                    }
                                });
                            } else { //写入数据失败
                                onError(socketManager, -4);
                            }
                        }
                    }
                } else { //读取失败
                    LogUtils.d("--Kaadas--读取失败");
                    socketManager.writeData(("TimeOut").getBytes());
                    onError(socketManager, -1);
                }
            } else {  //连接失败
                LogUtils.d("--Kaadas--端口打开失败");
                onError(socketManager, -2);
            }
        }
    };
    //非第一次进来开启的线程1
    private Thread secondThread = new Thread() {
        @Override
        public void run() {

            if ("12345678".equals(adminPassword)) {
                LogUtils.d("--Kaadas--12345678");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showModifyPasswordDialog();
                    }
                });
                return;
            }
            SocketManager.WifiResult wifiResult = socketManager.parseWifiData(adminPassword, data);
            if (wifiResult.result == 0) {
                int writeResult = socketManager.writeData(("CRCSuccess\r").getBytes());
                //写入 CRCsuccess 成功
                if (writeResult == 0) {
                    SocketManager.ReadResult readResult2 = socketManager.readWifiData();
                    if (readResult2.resultCode >= 0 && (new String(readResult2.data)).startsWith("APContinue")) {
                        onSuccess(wifiResult);
                        changeState(3);
                    } else {
                        onError(socketManager, -5);
                    }
                } else { //写入CRC失败
                    onError(socketManager, -4);
                }
            } else {
                //解析数据失败  通知锁端蓄电？
                int writeResult = socketManager.writeData(("APError\r").getBytes());
                if (writeResult == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onAdminPasswordError();
                        }
                    });
                } else { //写入数据失败
                    onError(socketManager, -4);
                }
            }
        }
    };
    //修改过管理员密码了
    private Thread thirdThread = new Thread() {
        @Override
        public void run() {
            int writeResult = socketManager.writeData(("ApFactorResend\r").getBytes());  //要求锁端重新发送密码因子
            if (writeResult == 0) {
                SocketManager.ReadResult readResult = socketManager.readWifiData();
                data = readResult.data;
                //写入 CRCsuccess 成功
                if (readResult.resultCode == 0) {
                    if (readResult.dataLen < 46) {  //读取数据字数不够
                        onError(socketManager, -1);
                        return;
                    } else { //读取到数据  而且数据够46
                        changeState(2);
                        if ("12345678".equals(adminPassword)) {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showModifyPasswordDialog();
                                }
                            });
                            return;
                        }
                        SocketManager.WifiResult wifiResult = socketManager.parseWifiData(adminPassword, data);
                        //解析数据   解析数据成功
                        if (wifiResult.result == 0) {
                            //发送CRCSuccess 通知锁端
                            writeResult = socketManager.writeData(("CRCSuccess\r").getBytes());
                            //写入 CRCsuccess 成功
                            if (writeResult == 0) {
                                SocketManager.ReadResult readResult2 = socketManager.readWifiData();
                                if (readResult2.resultCode >= 0 && (new String(readResult2.data)).startsWith("APContinue")) {
                                    onSuccess(wifiResult);
                                    changeState(3);
                                } else {
                                    onError(socketManager, -5);
                                }
                            } else { //写入CRC失败
                                onError(socketManager, -4);
                            }
                        } else {
                            //解析数据失败  通知锁端蓄电？
                            writeResult = socketManager.writeData(("APError\r").getBytes());
                            if (writeResult == 0) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        onAdminPasswordError();
                                    }
                                });
                            } else { //写入数据失败
                                onError(socketManager, -4);
                            }
                        }
                    }
                } else { //写入CRC失败
                    onError(socketManager, -4);
                }
            } else {
                //解析数据失败  通知锁端蓄电？
                writeResult = socketManager.writeData(("APError\r").getBytes());
                if (writeResult == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onAdminPasswordError();
                        }
                    });
                } else { //写入数据失败
                    onError(socketManager, -4);
                }
            }
        }
    };

    private void onAdminPasswordError() {
        if (times < 5) {
            if (times == 3) { //提示三次错误
                AlertDialogUtil.getInstance().noEditSingleCanNotDismissButtonDialog(
                        WifiLockAddNewCheckAdminPasswordActivity.this, "", getString(R.string.philips_activity_wifi_lock_check_password_dialog_1),
                        getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                            @Override
                            public void left() {
                                toInputPasswordActivity();
                            }

                            @Override
                            public void right() {
                                toInputPasswordActivity();
                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(String toString) {

                            }
                        });
            } else { // 正常提示
                AlertDialogUtil.getInstance().noEditSingleCanNotDismissButtonDialog(
                        WifiLockAddNewCheckAdminPasswordActivity.this, "", getString(R.string.admin_error_reinput), getString(R.string.hao_de), new AlertDialogUtil.ClickListener() {
                            @Override
                            public void left() {
                                toInputPasswordActivity();
                            }

                            @Override
                            public void right() {
                                toInputPasswordActivity();
                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(String toString) {

                            }
                        });
            }
        } else { //都五次输入错误提示   退出
            AlertDialogUtil.getInstance().noEditSingleCanNotDismissButtonDialog(
                    WifiLockAddNewCheckAdminPasswordActivity.this, "", getString(R.string.activity_wifi_video_sixth_fail_3) + getString(R.string.activity_wifi_video_sixth_fail_4), getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                        @Override
                        public void left() {
                            startActivity(new Intent(WifiLockAddNewCheckAdminPasswordActivity.this, WifiLockAddNewBindFailedActivity.class));
                            finish();
                        }

                        @Override
                        public void right() {
                            startActivity(new Intent(WifiLockAddNewCheckAdminPasswordActivity.this, WifiLockAddNewBindFailedActivity.class));
                            finish();
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

    private void showModifyPasswordDialog() {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(
                WifiLockAddNewCheckAdminPasswordActivity.this,
                getString(R.string.philips_activity_wifi_lock_ble_wifi_checkadmin_password),
                getString(R.string.philips_re_input), getString(R.string.modify_password), "#A4A4A4", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {
                        //重新输入
                        toInputPasswordActivity();
                    }

                    @Override
                    public void right() {
                        //修改密码
                        Intent intent = new Intent(WifiLockAddNewCheckAdminPasswordActivity.this, WifiLockAddNewModifyPasswordActivity.class);
                        intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, times+1);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void toInputPasswordActivity() {
        Intent intent = new Intent(this, WifiLockAddNewInputAdminPasswotdActivity.class);
        intent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES, times + 1);
        intent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_DATA, data);
        startActivity(intent);
        finish();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        firstThread.interrupt();
        secondThread.interrupt();
        thirdThread.interrupt();
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
                        Intent intent = new Intent(WifiLockAddNewCheckAdminPasswordActivity.this, WifiLockAddNewFirstActivity.class);
                        intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, times);
                        startActivity(intent);
                        socketManager.destroy();
                        finish();
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
