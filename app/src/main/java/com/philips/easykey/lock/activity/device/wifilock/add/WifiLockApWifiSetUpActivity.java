package com.philips.easykey.lock.activity.device.wifilock.add;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.PhilipsWifiLockRecordActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiApWifiSetUpPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockAPWifiSetUpView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.widget.DropEditText;

public class WifiLockApWifiSetUpActivity extends BaseActivity<IWifiLockAPWifiSetUpView, WifiApWifiSetUpPresenter<WifiApWifiSetUpPresenter>>
        implements IWifiLockAPWifiSetUpView {
    private static final String TAG = WifiLockApWifiSetUpActivity.class.getSimpleName();

    private static final int REQUEST_PERMISSION = 0x01;

    ImageView back;
    TextView headTitle;
    EditText apSsidText;
    EditText apPasswordEdit;
    ImageView help;
    ImageView ivEye;
    Button confirmBtn;
    TextView tvSupportList;
    private DropEditText mApSsidTV;
    private EditText mApPasswordET;
    private String wifiBssid;
    private boolean passwordHide = true;
    public String adminPassword;
    public String sSsid;

    private String wifiSn;
    private String randomCode;
    private int func;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_ap_wifi_set_up);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        apSsidText = findViewById(R.id.ap_ssid_text);
        apPasswordEdit = findViewById(R.id.ap_password_edit);
        help = findViewById(R.id.help);
        ivEye = findViewById(R.id.iv_eye);
        confirmBtn = findViewById(R.id.confirm_btn);
        tvSupportList = findViewById(R.id.tv_support_list);

        help.setOnClickListener(v -> startActivity(new Intent(this,WifiLockHelpActivity.class)));
        confirmBtn.setOnClickListener(v -> {
            sSsid = mApSsidTV.getText().toString();
            String sPassword = mApPasswordET.getText().toString();
            if (TextUtils.isEmpty(sSsid)) { //WiFi名为空
                ToastUtils.showShort(getString(R.string.philips_wifi_name_disable_empty));
                return;
            }
            if (TextUtils.isEmpty(sPassword)) { //WiFi密码为空
                AlertDialogUtil.getInstance().noEditSingleButtonDialog(WifiLockApWifiSetUpActivity.this, "", getString(R.string.no_support_no_pwd_wifi), getString(R.string.ok_wifi_lock), null);
                return;
            }
            Intent intent = new Intent(this,WifiLockApConnectDeviceActivity.class);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID, sSsid);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_PASSWORD, sPassword);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, randomCode);
            intent.putExtra(KeyConstants.WIFI_LOCK_FUNC, func);
            startActivity(intent);
        });
        tvSupportList.setOnClickListener(v -> {
            //跳转查看支持WiFi列表
            startActivity(new Intent(WifiLockApWifiSetUpActivity.this, WifiLcokSupportWifiActivity.class));
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
        back.setOnClickListener(v -> finish());

        mApSsidTV = findViewById(R.id.ap_ssid_text);
        mApPasswordET = findViewById(R.id.ap_password_edit);
        adminPassword = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD);
        mApPasswordET.setSelection(0);

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        randomCode = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE);
        func = getIntent().getIntExtra(KeyConstants.WIFI_LOCK_FUNC,0);

        String wifiName = (String) SPUtils.get(KeyConstants.WIFI_LOCK_CONNECT_NAME, "");
        apSsidText.setText(wifiName.trim());
        mApSsidTV.setOnOpenPopWindowListener(new DropEditText.OnOpenPopWindowListener() {
            @Override
            public void onOpenPopWindowListener(View view) {
                mApSsidTV.setText("");
                mApSsidTV.setSelection(0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected WifiApWifiSetUpPresenter<WifiApWifiSetUpPresenter> createPresent() {
        return new WifiApWifiSetUpPresenter<>();
    }

    @Override
    public void onBindSuccess(String wifiSn) {

    }

    @Override
    public void onBindFailed(BaseResult baseResult) {

    }

    @Override
    public void onBindThrowable(Throwable throwable) {

    }

    @Override
    public void onUpdateSuccess(String wifiSn) {

    }

    @Override
    public void onUpdateFailed(BaseResult baseResult) {

    }

    @Override
    public void onUpdateThrowable(Throwable throwable) {

    }

    @Override
    public void onSendSuccess() {

    }

    @Override
    public void onSendFailed() {

    }

    @Override
    public void onReceiverFailed() {

    }

    @Override
    public void onReceiverSuccess() {

    }

}


