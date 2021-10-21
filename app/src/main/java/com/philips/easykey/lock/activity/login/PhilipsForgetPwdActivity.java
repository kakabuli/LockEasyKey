package com.philips.easykey.lock.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.choosecountry.CountryActivity;
import com.philips.easykey.lock.normal.NormalBaseActivity;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.PhoneUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StatusBarUtils;
import com.philips.easykey.lock.utils.StringUtil;

import io.reactivex.disposables.Disposable;

/**
 * author : Jack
 * time   : 2021/5/12
 * E-mail : wengmaowei@kaadas.com
 * desc   : 忘记密码
 */
public class PhilipsForgetPwdActivity extends NormalBaseActivity {

    private EditText mEtPhoneOrMail;
    private EditText mEtVerificationCode;
    private EditText mEtPwd;
    private TextView mTvGetCode;
    private Button mBtnComplete;
    private ImageView mIvShowOrHide;
    private TextView mTvSelectCountry;

    private boolean isCountdown = false;
    private boolean isShowPwd = false;

    private String mCountryCode = "86";

    private final int mCountryReqCode = 1233;
    private final long mCountDownTotalTime = 60000;

    private final CountDownTimer mCountDownTimer = new CountDownTimer(mCountDownTotalTime, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String value = String.valueOf((int) (millisUntilFinished / 1000));
            mTvGetCode.setText(value);
        }

        @Override
        public void onFinish() {
            isCountdown = false;
            if(mTvGetCode != null) {
                mTvGetCode.setText(getString(R.string.philips_get_verification));
            }
        }
    };

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.philips_activity_forget_pwd;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {

        mTvGetCode = findViewById(R.id.tvGetCode);
        mEtPhoneOrMail = findViewById(R.id.etPhoneOrMail);
        mEtVerificationCode = findViewById(R.id.etVerificationCode);
        mEtPwd = findViewById(R.id.etPwd);
        mBtnComplete = findViewById(R.id.btnComplete);
        mIvShowOrHide = findViewById(R.id.ivShowOrHide);
        mTvSelectCountry = findViewById(R.id.tvSelectCountry);
        initTextChangeListener();
        initAccountFromLocal();

        enableComplete(false);
        applyDebouncingClickListener(findViewById(R.id.ivBack), mTvGetCode, mIvShowOrHide,
                mBtnComplete, mTvSelectCountry);

    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onDebouncingClick(@NonNull View view) {
        if(view.getId() == R.id.ivBack) {
            finish();
        } else if(view.getId() == R.id.tvGetCode) {
            if(isCountdown) return;
            getVerification();
        } else if(view.getId() == R.id.ivShowOrHide) {
            changePasswordStatus();
        } else if(view.getId() == R.id.btnComplete) {
            resetPwd();
        } else if(view.getId() == R.id.tvSelectCountry) {
            Intent intent = new Intent(this, CountryActivity.class);
            startActivityForResult(intent, mCountryReqCode);
        }
    }

    @Override
    protected void onDestroy() {
        if(mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mCountryReqCode) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String countryName = bundle.getString("countryName");
                mCountryCode = bundle.getString("countryNumber");
                LogUtils.d("davi 选择的国家==" + countryName + " 区号==" + mCountryCode);
                mTvSelectCountry.setText(countryName);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initAccountFromLocal() {
        String phone = (String) SPUtils.get(SPUtils.PHONEN, "");
        if (phone != null && phone.length() != 0) {
            mEtPhoneOrMail.setText(phone);
            mEtPhoneOrMail.setSelection(phone.length());
        }
    }

    private void initTextChangeListener() {
        mEtPhoneOrMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableComplete(isCanEnableComplete());
            }
        });
        mEtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableComplete(isCanEnableComplete());
            }
        });
        mEtVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                enableComplete(isCanEnableComplete());
            }
        });
    }

    private boolean isCanEnableComplete() {
        String pwd = mEtPwd.getText().toString();
        String code = mEtVerificationCode.getText().toString();
        String account = mEtPhoneOrMail.getText().toString();
        if(TextUtils.isEmpty(pwd)) {
            return false;
        }
        if(TextUtils.isEmpty(code)) {
            return false;
        }
        return !TextUtils.isEmpty(account);
    }

    private void changePasswordStatus() {
        isShowPwd = !isShowPwd;
        mIvShowOrHide.setImageResource(isShowPwd?R.drawable.philips_dms_icon_display:R.drawable.philips_dms_icon_hidden);
        mEtPwd.setInputType(isShowPwd?
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                :(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD));
    }

    private void enableComplete(boolean enable) {
        mBtnComplete.setEnabled(enable);
        mBtnComplete.setTextColor(enable?Color.parseColor("#FFFFFF"):Color.parseColor("#0066A1"));
        mBtnComplete.setBackgroundResource(enable?R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_login_bg);
    }

    //获取验证码
    private void getVerification() {
        if (NetUtil.isNetworkAvailable()) {
            String account = StringUtil.getEdittextContent(mEtPhoneOrMail);
            if (TextUtils.isEmpty(account)) {
                AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_account_message_not_empty));
                return;
            }
            if (StringUtil.isNumeric(account)) {
                if (!PhoneUtil.isMobileNO(account)) {
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone));
                    return;
                } else {
                    String countryCode = mCountryCode.trim().replace("+", "");
                    sendRandomByPhone(account, countryCode);
                }
            } else {
                //邮箱
                if (RegexUtils.isEmail(account)) {
                    sendRandomByEmail(account);
                } else {
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                    return;
                }
            }
            if(mCountDownTimer != null) {
                isCountdown = true;
                mCountDownTimer.start();
            }
        } else {
            ToastUtils.showShort(R.string.philips_noNet);
        }
    }

    private void resetPwd() {
        if (NetUtil.isNetworkAvailable()) {
            final String account = StringUtil.getEdittextContent(mEtPhoneOrMail);
            if (TextUtils.isEmpty(account)) {
                AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_account_message_not_empty));
                return;
            }
            String code = StringUtil.getEdittextContent(mEtVerificationCode);
            if (TextUtils.isEmpty(code) || code.length() != 6) {
                AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_correct_verification_code));
                return;
            }

            String pwd = StringUtil.getEdittextContent(mEtPwd);
            if (StringUtil.judgeSpecialCharacter(pwd)) {
                ToastUtils.showShort(R.string.philips_password_judgment);
                return;
            }
            if (!StringUtil.passwordJudge(pwd)) {
                ToastUtils.showShort(R.string.philips_password_judgment);
                return;
            }

            if (StringUtil.isNumeric(account)) {
                if (!PhoneUtil.isMobileNO(account)) {
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                    return;
                }
                showLoading("");
                String countryCode = mCountryCode.trim().replace("+", "");
                resetPassword(countryCode + account, pwd,1, code);
            } else {
                LogUtils.d("邮箱注册：" + RegexUtils.isEmail(account));
                if (RegexUtils.isEmail(account)) {
                    showLoading("");
                    resetPassword(account, pwd,2, code);
                } else {
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                }
            }


        } else {
            ToastUtils.showShort(R.string.philips_noNet);
        }
    }

    public void senRandomSuccess() {
        LogUtils.d("验证码发送成功");
    }

    public void resetPasswordSuccess() {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.philips_pwd_resetting_success));
        finish();
        //MyApplication.getInstance().tokenInvalid(false);
    }

    public void sendRandomFailed(Throwable e) {
        LogUtils.d("验证码发送失败");
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, e));
    }

    public void resetPasswordFailed(Throwable e) {
        hiddenLoading();
        LogUtils.d("密码重置失败");
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, e));
    }

    public void sendRandomFailedServer(BaseResult result) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
    }

    public void resetPasswordFailedServer(BaseResult result) {
        hiddenLoading();
        if ("445".equals(result.getCode())){
            AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_correct_verification_code));
        }else {
            ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
        }
    }


    public void sendRandomByPhone(String phone, String code) {
        XiaokaiNewServiceImp.sendMessage(phone, code)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        senRandomSuccess();
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        sendRandomFailedServer(baseResult);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        sendRandomFailed(throwable);
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });


    }

    /**
     * 发送邮箱验证码
     */
    public void sendRandomByEmail(String email) {
        XiaokaiNewServiceImp.sendEmailYZM(email)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        senRandomSuccess();
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        sendRandomFailedServer(baseResult);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        sendRandomFailed(throwable);
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });

    }

    public void resetPassword(String user_name, String pwd, int type, String token) {
        XiaokaiNewServiceImp.forgetPassword(user_name, pwd, type, token)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        LogUtils.d("发送验证码成功  " + result.toString());
                        //todo 需要缓存返回来的token,用户id，和手机号码，服务器还未处理
                        resetPasswordSuccess();
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        resetPasswordFailedServer(baseResult);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        resetPasswordFailed(throwable);
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

}
