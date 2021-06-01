package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLcokSupportWifiActivity;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.SocketManager;
import com.philips.easykey.lock.widget.DropEditText;

public class WifiLockAddNewInputWifiActivity extends BaseAddToApplicationActivity {

    ImageView back;
    TextView headTitle;
    ImageView help;
    DropEditText apSsidText;
    EditText apPasswordEdit;
    ImageView ivEye;
    TextView confirmBtn;
    TextView tvSupportList;


    private boolean passwordHide = true;
    public String adminPassword;
    public String sSsid;
    private SocketManager socketManager = SocketManager.getInstance();
    private String wifiSn;
    private String randomCode;
    private int func;
    private int times = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_input_wifi);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        help = findViewById(R.id.help);
        apSsidText = findViewById(R.id.ap_ssid_text);
        apPasswordEdit = findViewById(R.id.ap_password_edit);
        ivEye = findViewById(R.id.iv_eye);
        confirmBtn = findViewById(R.id.confirm_btn);
        tvSupportList = findViewById(R.id.tv_support_list);

        back.setOnClickListener(v -> showWarring());
        help.setOnClickListener(v -> startActivity(new Intent(this,WifiLockHelpActivity.class)));
        confirmBtn.setOnClickListener(v -> {
            sSsid = apSsidText.getText().toString();
            String sPassword = apPasswordEdit.getText().toString();
            if (TextUtils.isEmpty(sSsid)) { //WiFi名为空
                ToastUtils.showShort(R.string.philips_wifi_name_disable_empty);
                return;
            }
            if (TextUtils.isEmpty(sPassword) ) { //WiFi密码为空
//                    AlertDialogUtil.getInstance().noEditSingleButtonDialog(this, "", getString(R.string.no_support_no_pwd_wifi), getString(R.string.ok_wifi_lock), null);
                Intent intent = new Intent(this,WifiLockAddNewCheckWifiActivity.class);
                intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID, sSsid);
                intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_PASSWORD, sPassword);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, randomCode);
                intent.putExtra(KeyConstants.WIFI_LOCK_FUNC, func);
                startActivity(intent);
                return;

            }
            if (sPassword.length()<8){
                ToastUtils.showShort(getString(R.string.philips_password_len_not_less_8));
                return;
            }
            Intent intent = new Intent(this,WifiLockAddNewCheckWifiActivity.class);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID, sSsid);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_PASSWORD, sPassword);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, randomCode);
            intent.putExtra(KeyConstants.WIFI_LOCK_FUNC, func);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, times);
            startActivity(intent);
        });
        ivEye.setOnClickListener(v -> {
            passwordHide = !passwordHide;
            if (passwordHide) {
                apPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                apPasswordEdit.setSelection(apPasswordEdit.getText().toString().length());//将光标移至文字末尾
                ivEye.setImageResource(R.mipmap.eye_close_has_color);
            } else {
                apPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                apPasswordEdit.setSelection(apPasswordEdit.getText().toString().length());//将光标移至文字末尾
                ivEye.setImageResource(R.mipmap.eye_open_has_color);
            }
        });
        tvSupportList.setOnClickListener(v -> startActivity(new Intent(this,WifiLcokSupportWifiActivity.class)));

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        randomCode = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE);
        func = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_FUNC,0);
        times = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, 1);

        String wifiName = (String) SPUtils.get(KeyConstants.WIFI_LOCK_CONNECT_NAME, "");
        apSsidText.setText(wifiName.trim());
    }


    private void showWarring(){
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(
                WifiLockAddNewInputWifiActivity.this
                , getString(R.string.activity_wifi_video_fifth_network),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#A4A4A4", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        thread.start();
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private Thread thread = new Thread() {
        @Override
        public void run() {
            super.run();
            LogUtils.d("--Kaadas--发送************************************************************************************************APClose");
            socketManager.writeData("************************************************************************************************APClose".getBytes());

            onError(socketManager, -1);

        }
    };
    public void onError(SocketManager socketManager, int errorCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
                //退出当前界面
                Intent intent = new Intent(WifiLockAddNewInputWifiActivity.this, WifiLockAddNewFirstActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        showWarring();
    }
}
