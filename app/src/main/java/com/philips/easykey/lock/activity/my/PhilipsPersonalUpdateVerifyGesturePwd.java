package com.philips.easykey.lock.activity.my;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.login.PhilipsForgetPwdActivity;
import com.philips.easykey.lock.activity.login.PhilipsLoginActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.MD5Utils;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.cachefloder.ACache;
import com.philips.easykey.lock.utils.cachefloder.CacheFloder;
import com.philips.easykey.lock.utils.handPwdUtil.GestureContentView;
import com.philips.easykey.lock.utils.handPwdUtil.GestureDrawline;


public class PhilipsPersonalUpdateVerifyGesturePwd extends BaseAddToApplicationActivity {

    TextView mTextTip;
    FrameLayout gestureContainer;

    ImageView gesturePwdBack;
    TextView more;

    private GestureContentView mGestureContentView;
    //输入错误剩余次数
    private int residueCount = 5;
    String source;
    private boolean passwordHide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_update_verify_hand_pwd);

        mTextTip = findViewById(R.id.text_tip);
        gestureContainer = findViewById(R.id.gesture_container);
        gesturePwdBack = findViewById(R.id.gesture_pwd_back);
        more = findViewById(R.id.more);

        gesturePwdBack.setOnClickListener(v -> finish());
        more.setOnClickListener(v -> {
            String password = (String) SPUtils.get(SPUtils.PASSWORD, "");
            if (TextUtils.isEmpty(password)) { //如果本地密码保存为空
                MyApplication.getInstance().tokenInvalid(false);
                Intent intent = new Intent(PhilipsPersonalUpdateVerifyGesturePwd.this, PhilipsLoginActivity.class);
                startActivity(intent);
            } else {
                showInputPassword();
            }
        });

        Intent intent = getIntent();
        source = intent.getStringExtra(KeyConstants.SOURCE);
        initView();
    }

    private void initView() {
        String code = CacheFloder.readHandPassword(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "handPassword");
        if (code != null) {
            mTextTip.setText(getResources().getString(R.string.hand_pwd_drraw_the_old_ailure));
            if ("PersonalSecuritySettingActivity_Update".equals(source)) {
                more.setVisibility(View.VISIBLE);
            }else {
                more.setVisibility(View.GONE);
            }
            mGestureContentView = new GestureContentView(this, true, code, new GestureDrawline.GestureCallBack() {
                @Override
                public void onGestureCodeInput(String inputCode) {

                }

                @Override
                public void checkedSuccess() {
                    if ("PhilipsPersonalSecuritySettingActivity".equals(source)) {
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        //设置返回数据
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        finish();
                        Intent intent = new Intent(PhilipsPersonalUpdateVerifyGesturePwd.this, PersonalUpdateGesturePwdActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void checkedFail() {

                    residueCount--;
                    if (residueCount > 0) {
                        mGestureContentView.clearDrawlineState(1300L);
                        String text = getResources().getString(R.string.resume_load) + residueCount + getResources().getString(R.string.second);
                        mTextTip.setText(Html
                                .fromHtml("<font color='#DB392B'>" + text + "</font>"));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(PhilipsPersonalUpdateVerifyGesturePwd.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                    } else {
                        //重新登录
                        //todo 清除数据，未做！
                        //1清除手势密码缓存的数据
                        if (MyApplication.getInstance().getMqttService() != null) {
                            MyApplication.getInstance().getMqttService().httpMqttDisconnect();
                        }
                        MyApplication.getInstance().tokenInvalid(false);
                        Intent intent = new Intent(PhilipsPersonalUpdateVerifyGesturePwd.this, PhilipsLoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
        if (mGestureContentView != null) {
            mGestureContentView.setParentView(gestureContainer);
        }


    }


    public void showInputPassword() {
        View mView = LayoutInflater.from(this).inflate(R.layout.update_hand_password_dialog, null);
        AlertDialog alertDialog = AlertDialogUtil.getInstance().common(this, mView);
        TextView tvTitle = mView.findViewById(R.id.tv_title);
        TextView tvCurrentAccount = mView.findViewById(R.id.tv_current_account);
        EditText etPassword = mView.findViewById(R.id.et_password);
        ImageView ivPasswordStatus = mView.findViewById(R.id.iv_password_status);
        TextView tvCancel = mView.findViewById(R.id.tv_cancel);
        TextView tvConfirm = mView.findViewById(R.id.tv_confirm);
        //取消
        String phone = (String) SPUtils.get(SPUtils.PHONEN, "");

        String content = getString(R.string.current_account) + phone;
        tvCurrentAccount.setText(content);

        if (passwordHide) {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            /* etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);*/
            etPassword.setSelection(etPassword.getText().toString().length());//将光标移至文字末尾
            ivPasswordStatus.setImageResource(R.mipmap.eye_close_no_color);

        } else {
            //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
            //etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPassword.setSelection(etPassword.getText().toString().length());//将光标移至文字末尾
            ivPasswordStatus.setImageResource(R.mipmap.eye_open_no_color);
        }


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //确定
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = etPassword.getText().toString().trim();
                password =   MD5Utils.encode(password);
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.showLong(R.string.please_input_password);
                    return;
                } else {
                    String localPassword = (String) SPUtils.get(SPUtils.PASSWORD, "");
                    if (localPassword.equals(password)){  //验证成功  跳转至设置手势密码界面
                        finish();
                        Intent intent = new Intent(PhilipsPersonalUpdateVerifyGesturePwd.this, PersonalUpdateGesturePwdActivity.class);
                        startActivity(intent);
                    }else {
                        showSelectDialog();
                    }
                }
                alertDialog.dismiss();
            }
        });

        ivPasswordStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordHide = !passwordHide;
                if (passwordHide) {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    /* etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);*/
                    etPassword.setSelection(etPassword.getText().toString().length());//将光标移至文字末尾
                    ivPasswordStatus.setImageResource(R.mipmap.eye_close_no_color);

                } else {
                    //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                    //etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etPassword.setSelection(etPassword.getText().toString().length());//将光标移至文字末尾
                    ivPasswordStatus.setImageResource(R.mipmap.eye_open_no_color);
                }
            }
        });
    }


    public void showSelectDialog(){
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(PhilipsPersonalUpdateVerifyGesturePwd.this, getString(R.string.account_password_error2),
                getString(R.string.modify_password), getString(R.string.philips_re_input), "#333333",
                "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {
                        Intent intent = new Intent();
                        intent.setClass(PhilipsPersonalUpdateVerifyGesturePwd.this, PhilipsForgetPwdActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void right() {
                        showInputPassword();
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
