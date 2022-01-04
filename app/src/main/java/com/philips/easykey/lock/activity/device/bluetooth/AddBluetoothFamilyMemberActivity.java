package com.philips.easykey.lock.activity.device.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DetectionEmailPhone;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.PhoneUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


/**
 * Created by David
 */
public class AddBluetoothFamilyMemberActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    EditText etTelephone;
    Button btnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_add_family_member);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        etTelephone = findViewById(R.id.et_telephone);
        btnConfirm = findViewById(R.id.btn_confirm);
        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.philips_add_user));
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                String phone = etTelephone.getText().toString().trim();
                String myPhone = (String) SPUtils.get(SPUtils.PHONEN, "");

                if (myPhone != null) {
                    if (myPhone.equals(phone)) {
                        ToastUtils.showShort(getString(R.string.no_add_my));
                        return;
                    }
                }
                if (NetUtil.isNetworkAvailable()) {
                    if (TextUtils.isEmpty(phone)) {
//                ToastUtils.showShort(R.string.input_telephone_or_rmail);
                        AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_account_message_not_empty));
                        return;
                    }


                    if (StringUtil.isNumeric(phone)) {
                        if (!PhoneUtil.isMobileNO(phone)) {
                            // 账户密码错误 请输入正确验证码 调用这个方法传入对应的内容就可以
                            AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                            return;
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(KeyConstants.AUTHORIZATION_TELEPHONE, "86" + phone);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    } else {
                        if (!DetectionEmailPhone.isEmail(phone)) {
//                    ToastUtils.showShort(R.string.email_not_right);
                            AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                            return;
                        } else {
                            Intent intent = new Intent();
                            intent.putExtra(KeyConstants.AUTHORIZATION_TELEPHONE, phone);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    }

                } else {
                    ToastUtils.showShort(getString(R.string.philips_noNet));
                }

                break;
        }
    }
}
