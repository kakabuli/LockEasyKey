package com.philips.easykey.lock.activity.device.wifilock.add;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


public class WifiLockInputAdminPasswordActivity extends BaseAddToApplicationActivity {


    ImageView back;
    ImageView help;
    EditText etAdminPassword;
    TextView tvNext;
    ImageView ivPasswordStatus;//密码状态图标
    boolean passwordHide = true;//密码图标
    private String wifiBssid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_ap_input_admin_password);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        etAdminPassword = findViewById(R.id.et_admin_password);
        tvNext = findViewById(R.id.tv_next);
        ivPasswordStatus = findViewById(R.id.iv_password_status);

        back.setOnClickListener(v -> finish());
        tvNext.setOnClickListener(v -> {
            String adminPassword = etAdminPassword.getText().toString().trim();
            if (!StringUtil.randomJudge(adminPassword)) {
                ToastUtils.showShort(getString(R.string.philips_random_verify_error));
                return;
            }
            Intent intent = new Intent(WifiLockInputAdminPasswordActivity.this, WifiLockCheckAdminPasswordActivity.class);
            intent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD, adminPassword);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID, wifiBssid);
            startActivity(intent);
        });
        ivPasswordStatus.setOnClickListener(v -> {
            passwordHide = !passwordHide;
            if (passwordHide) {
                etAdminPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                /* etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);*/
                etAdminPassword.setSelection(etAdminPassword.getText().toString().length());//将光标移至文字末尾
                ivPasswordStatus.setImageResource(R.mipmap.eye_close_has_color);

            } else {
                //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                //etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etAdminPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                etAdminPassword.setSelection(etAdminPassword.getText().toString().length());//将光标移至文字末尾
                ivPasswordStatus.setImageResource(R.mipmap.eye_open_has_color);
            }
        });
        help.setOnClickListener(v -> startActivity(new Intent(this,WifiLockHelpActivity.class)));

        wifiBssid = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_SSID);
        etAdminPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!TextUtils.isEmpty(text) && text.length() >= 6) {
                    tvNext.setBackgroundResource(R.drawable.go_and_buy);
                    tvNext.setClickable(true);
                } else {
                    tvNext.setBackgroundResource(R.drawable.button_bg_bfbfbf_);
                    tvNext.setClickable(false);
                }
            }
        });
    }

}
