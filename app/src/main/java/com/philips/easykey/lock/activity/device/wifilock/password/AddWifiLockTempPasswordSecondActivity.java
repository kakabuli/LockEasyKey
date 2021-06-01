package com.philips.easykey.lock.activity.device.wifilock.password;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.AddWifiLockTempPasswordPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IAddWifiTempPasswordView;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


public class AddWifiLockTempPasswordSecondActivity extends BaseActivity<IAddWifiTempPasswordView,
        AddWifiLockTempPasswordPresenter<IAddWifiTempPasswordView>> implements IAddWifiTempPasswordView {

    ImageView back;
    TextView headTitle;
    EditText etPassword;
    TextView tvRandom;
    TextView confirmBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_temp_password_second);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        etPassword = findViewById(R.id.et_password);
        tvRandom = findViewById(R.id.tv_random);
        confirmBtn = findViewById(R.id.confirm_btn);

        back.setOnClickListener(v -> finish());
        tvRandom.setOnClickListener(v -> {
            String randomPassword = StringUtil.makeRandomPassword();
            etPassword.setText(randomPassword);
            etPassword.setSelection(randomPassword.length());
        });
        confirmBtn.setOnClickListener(v -> {
            String password = etPassword.getText().toString().trim();
            if (!StringUtil.randomJudge(password)) {
                ToastUtils.showShort(R.string.philips_random_verify_error);
                return;
            }
            if (StringUtil.checkSimplePassword(password)) {
                AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.hint), getString(R.string.password_simple_please_reset), getString(R.string.go_on), getString(R.string.reinstall), new AlertDialogUtil.ClickListener() {

                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        etPassword.setText("");
                        return;
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
                return;
            }
            //添加临时密码
        });

    }

    @Override
    protected AddWifiLockTempPasswordPresenter<IAddWifiTempPasswordView> createPresent() {
        return new AddWifiLockTempPasswordPresenter<>();
    }

}
