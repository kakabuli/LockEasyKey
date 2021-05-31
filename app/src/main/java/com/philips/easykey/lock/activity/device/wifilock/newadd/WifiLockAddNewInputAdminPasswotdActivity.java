package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.add.WifiLockHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


public class WifiLockAddNewInputAdminPasswotdActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView head;
    EditText apPasswordEdit;
    TextView buttonNext;
    ImageView ivPasswordStatus;//密码状态图标
    boolean passwordHide = true;//密码图标
    int times = 1;
    byte[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_add_new_input_admin_passwotd);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        head = findViewById(R.id.head);
        apPasswordEdit = findViewById(R.id.ap_password_edit);
        buttonNext = findViewById(R.id.button_next);
        ivPasswordStatus = findViewById(R.id.iv_password_status);

        back.setOnClickListener(v -> showWarring());
        help.setOnClickListener(v -> startActivity(new Intent(this, WifiLockHelpActivity.class)));
        buttonNext.setOnClickListener(v -> {
            //输入管理员密码  下一步
            String adminPassword = apPasswordEdit.getText().toString().trim();
            if (!StringUtil.randomJudge(adminPassword)) {
                ToastUtils.showShort(R.string.philips_random_verify_error);
                return;
            }

            LogUtils.d(getLocalClassName()+"次数是   " + times + "  data 是否为空 " + (data == null));
            Intent intent = new Intent(this, WifiLockAddNewCheckAdminPasswordActivity.class);
            intent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD, adminPassword);
            intent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES, times);
            intent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_DATA, data);
            startActivity(intent);
            finish();
        });
        ivPasswordStatus.setOnClickListener(v -> {
            passwordHide = !passwordHide;
            if (passwordHide) {
                apPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                /* etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);*/
                apPasswordEdit.setSelection(apPasswordEdit.getText().toString().length());//将光标移至文字末尾
                ivPasswordStatus.setImageResource(R.mipmap.eye_close_has_color);

            } else {
                //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                //etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                apPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                apPasswordEdit.setSelection(apPasswordEdit.getText().toString().length());//将光标移至文字末尾
                ivPasswordStatus.setImageResource(R.mipmap.eye_open_has_color);
            }
        });

        data =  getIntent().getByteArrayExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_DATA);
        times =  getIntent().getIntExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES,1);
    }

    @Override
    public void onBackPressed() {
        showWarring();
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
                        Intent intent = new Intent(WifiLockAddNewInputAdminPasswotdActivity.this, WifiLockAddNewFirstActivity.class);
                        intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_TIMES, times);
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
}
