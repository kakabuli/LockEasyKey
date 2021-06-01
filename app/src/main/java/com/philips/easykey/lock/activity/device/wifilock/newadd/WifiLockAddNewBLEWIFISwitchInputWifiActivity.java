package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLcokSupportWifiActivity;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.deviceaddpresenter.BindBleWiFiSwitchPresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.IBindBleView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.OfflinePasswordFactorManager;
import com.philips.easykey.lock.utils.SPUtils;


public class WifiLockAddNewBLEWIFISwitchInputWifiActivity extends BaseActivity<IBindBleView, BindBleWiFiSwitchPresenter<IBindBleView>> implements IBindBleView  {

    ImageView back;
    TextView headTitle;
    ImageView help;
//    (R.id.ap_ssid_text)
//    DropEditText apSsidText;
    EditText apSsidText;
    EditText apPasswordEdit;
    ImageView ivEye;
    TextView confirmBtn;
    TextView tvSupportList;
    TextView tv_to_change_wifi;


    private boolean passwordHide = true;
    public String adminPassword;
    public String sSsid;
    private String wifiSn;
    private String randomCode;
    private int func;
    private int times = 1;
    private static final int TO_CHECK_WIFI_PASSWORD = 10105;
    private static final int TO_CHOOSE_WIFI_PASSWORD = 10106;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_ble_input_wifi);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        help = findViewById(R.id.help);
        apSsidText = findViewById(R.id.ap_ssid_text);
        apPasswordEdit = findViewById(R.id.ap_password_edit);
        ivEye = findViewById(R.id.iv_eye);
        confirmBtn = findViewById(R.id.confirm_btn);
        tvSupportList = findViewById(R.id.tv_support_list);
        tv_to_change_wifi = findViewById(R.id.tv_to_change_wifi);

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
                Intent intent = new Intent(this,WifiLockAddNewBLEWIFICSwitchCheckWifiActivity.class);
                intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID, sSsid);
                intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_PASSWORD, sPassword);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, randomCode);
                intent.putExtra(KeyConstants.WIFI_LOCK_FUNC, func);
                intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, times);
//                    startActivity(intent);
                startActivityForResult(intent,TO_CHECK_WIFI_PASSWORD);

                return;
            }
            if (sPassword.length()<8){
                ToastUtils.showShort(getString(R.string.philips_password_len_not_less_8));
                return;
            }
            Intent intent = new Intent(this,WifiLockAddNewBLEWIFICSwitchCheckWifiActivity.class);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID, sSsid);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_PASSWORD, sPassword);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, randomCode);
            intent.putExtra(KeyConstants.WIFI_LOCK_FUNC, func);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, times);
//                startActivity(intent);
            startActivityForResult(intent,TO_CHECK_WIFI_PASSWORD);
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
        findViewById(R.id.tv_to_change_wifi).setOnClickListener(v -> {
            Intent scanIntent = new Intent(this,WifiLockScanWifiListActivity.class);
            startActivityForResult(scanIntent,TO_CHOOSE_WIFI_PASSWORD);
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        randomCode = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE);
        func = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_FUNC,0);
        times = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, 1);

        if (func == 0){
            //BLE&WIFI模组 读取功能集
            mPresenter.readFeatureSet();
        }

        String wifiName = (String) SPUtils.get(KeyConstants.WIFI_LOCK_CONNECT_NAME, "");
        apSsidText.setText(wifiName.trim());
    }

    @Override
    protected BindBleWiFiSwitchPresenter<IBindBleView> createPresent() {
        return new BindBleWiFiSwitchPresenter<>();
    }

    private void showWarring(){
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(
                WifiLockAddNewBLEWIFISwitchInputWifiActivity.this
                , getString(R.string.activity_wifi_video_fifth_network),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#A4A4A4", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        //退出当前界面
                        if (MyApplication.getInstance().getBleService() == null) {
                            return;
                        } else {
                            MyApplication.getInstance().getBleService().release();
                        }

                        Intent intent = new Intent(WifiLockAddNewBLEWIFISwitchInputWifiActivity.this, WifiLockAddNewFirstActivity.class);
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

    @Override
    public void onBackPressed() {
        showWarring();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_CHECK_WIFI_PASSWORD ) {

            if (resultCode == RESULT_OK && data != null) {
                times = data.getIntExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, 1);

                LogUtils.d("--Kaadas--onDecodeResult wifi和密码输入次数==" + times);
            }
        }
        if (requestCode == TO_CHOOSE_WIFI_PASSWORD ) {

            if (resultCode == RESULT_OK && data != null) {
                apSsidText.setText(data.getStringExtra(KeyConstants.CHOOSE_WIFI_NAME));
                LogUtils.d("--Kaadas--更换的wifi名称==" + data.getStringExtra(KeyConstants.CHOOSE_WIFI_NAME));
            }
        }

    }

    @Override
    public void onBindSuccess(String deviceName) {

    }

    @Override
    public void onBindFailed(Throwable throwable) {

    }

    @Override
    public void onBindFailedServer(BaseResult result) {

    }

    @Override
    public void onReceiveInNetInfo() {

    }

    @Override
    public void onReceiveUnbind() {

    }

    @Override
    public void onUnbindSuccess() {

    }

    @Override
    public void onUnbindFailed(Throwable throwable) {

    }

    @Override
    public void onUnbindFailedServer(BaseResult result) {

    }

    @Override
    public void readLockTypeFailed(Throwable throwable) {

    }

    @Override
    public void readLockTypeSucces() {

    }

    @Override
    public void onDeviceStateChange(boolean isConnected) {

    }

    @Override
    public void unknownFunctionSet(int functionSet) {

    }

    @Override
    public void readFunctionSetSuccess(int functionSet) {
        func = functionSet;
    }

    @Override
    public void readFunctionSetFailed(Throwable throwable) {

    }

    @Override
    public void onlistenerLastNum(int lastNum) {

    }

    @Override
    public void onlistenerPasswordFactor(byte[] originalData, int pswLen, int index) {

    }

    @Override
    public void onDecodeResult(int index, OfflinePasswordFactorManager.OfflinePasswordFactorResult result) {

    }
}
