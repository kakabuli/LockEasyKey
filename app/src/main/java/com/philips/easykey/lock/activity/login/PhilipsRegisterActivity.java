package com.philips.easykey.lock.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
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
import com.philips.easykey.lock.activity.my.PersonalUserAgreementActivity;
import com.philips.easykey.lock.activity.my.PrivacyActivity;
import com.philips.easykey.lock.normal.NormalBaseActivity;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.RegisterResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LinkClickableSpan;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.MMKVUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.PhoneUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.Disposable;

/**
 * author : Jack
 * time   : 2021/5/20
 * E-mail : wengmaowei@kaadas.com
 * desc   : 注册
 */
public class PhilipsRegisterActivity extends NormalBaseActivity {

    private TextView mTvSelectCountry;
    private EditText mEtPhoneOrMail;
    private EditText mEtVerificationCode;
    private TextView mTvGetCode;
    private EditText mEtPwd;
    private ImageView mIvAgreement;
    private TextView mTvAgreement;
    private Button mBtnRegister;
    private ImageView mIvShowOrHide;

    private boolean isCountdown = false;
    private boolean isShowPwd = false;
    private boolean isAgreed = false;

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
        return R.layout.philips_activity_register;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mTvSelectCountry = findViewById(R.id.tvSelectCountry);
        mEtPhoneOrMail = findViewById(R.id.etPhoneOrMail);
        mEtVerificationCode = findViewById(R.id.etVerificationCode);
        mTvGetCode = findViewById(R.id.tvGetCode);
        mEtPwd = findViewById(R.id.etPwd);
        mIvAgreement = findViewById(R.id.ivAgreement);
        mTvAgreement = findViewById(R.id.tvAgreement);
        mBtnRegister = findViewById(R.id.btnRegister);
        mIvShowOrHide = findViewById(R.id.ivShowOrHide);
        initTextChangeListener();
        initTerms();

        enableRegister(false);
        applyDebouncingClickListener(mTvSelectCountry, mTvGetCode, mIvAgreement, mBtnRegister, mIvShowOrHide);

    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onDebouncingClick(@NotNull View view) {
        if(view.getId() == R.id.tvSelectCountry) {
            Intent intent = new Intent(this, CountryActivity.class);
            startActivityForResult(intent, mCountryReqCode);
        } else if(view.getId() == R.id.tvGetCode) {
            if(isCountdown) return;
            getVerification();
        } else if(view.getId() == R.id.ivAgreement) {
            changeReadState();
        } else if(view.getId() == R.id.btnRegister) {
            if(!isAgreed){
                ToastUtils.showShort(R.string.philips_activity_register_agreenment);
                return;
            }
            register();
        } else if(view.getId() == R.id.ivShowOrHide) {
            changePasswordStatus();
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

    private void initTerms() {

        String termsOfUseStr = getString(R.string.philips_terms_of_use2);
        SpannableString termsOfUseSpannable = new SpannableString(termsOfUseStr);
        LinkClickableSpan termsOfUseSpan = new LinkClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent agreementIntent = new Intent(PhilipsRegisterActivity.this, PersonalUserAgreementActivity.class);
                startActivity(agreementIntent);
            }
        };
        termsOfUseSpannable.setSpan(termsOfUseSpan, 0, termsOfUseStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        String privacyPolicyStr = getString(R.string.philips_privacy_policy);
        SpannableString privacyPolicySpannable = new SpannableString(privacyPolicyStr);
        LinkClickableSpan privacyPolicySpan = new LinkClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent privacyIntent = new Intent(PhilipsRegisterActivity.this, PrivacyActivity.class);
                startActivity(privacyIntent);
            }
        };
        privacyPolicySpannable.setSpan(privacyPolicySpan, 0, termsOfUseStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvAgreement.append(getString(R.string.philips_register_agreement_tip));
        mTvAgreement.append(termsOfUseSpannable);
        mTvAgreement.append(getString(R.string.philips_and));
        mTvAgreement.append(privacyPolicySpannable);
        mTvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void changeReadState() {
        isAgreed = !isAgreed;
        mIvAgreement.setImageResource(isAgreed?R.drawable.philips_register_icon_selected:R.drawable.philips_register_icon_default);
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
                enableRegister(isCanEnableRegister());
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
                enableRegister(isCanEnableRegister());
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
                enableRegister(isCanEnableRegister());
            }
        });
    }

    private boolean isCanEnableRegister() {
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

    private void enableRegister(boolean enable) {
        mBtnRegister.setEnabled(enable);
        mBtnRegister.setBackgroundResource(enable?R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_login_bg);
    }

    private void changePasswordStatus() {
        isShowPwd = !isShowPwd;
        mIvShowOrHide.setImageResource(isShowPwd?R.drawable.philips_dms_icon_display:R.drawable.philips_dms_icon_hidden);
        mEtPwd.setInputType(isShowPwd?
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                :(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD));
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
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
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

    private void register() {
        if (NetUtil.isNetworkAvailable()) {
            String account = StringUtil.getEdittextContent(mEtPhoneOrMail);
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
                ToastUtils.showShort(R.string.not_input_special_symbol);
                return;
            }
            if (!StringUtil.passwordJudge(pwd)) {
                ToastUtils.showShort(R.string.philips_password_judgment);
                return;
            }

            if (!isAgreed) {
                ToastUtils.showShort(R.string.agree_user_protocol);
                return;
            }
            if (StringUtil.isNumeric(account)) {
                if (!PhoneUtil.isMobileNO(account)) {
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                    return;
                }
                showLoading("");
                String countryCode = mCountryCode.trim().replace("+", "");
                registerByPhone(countryCode + account, pwd, code);
            } else {
                LogUtils.d("邮箱注册：" + RegexUtils.isEmail(account));
                if (RegexUtils.isEmail(account)) {
                    showLoading("");
                    registerByEmail(account, pwd, code);
                } else {
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                }
            }
        } else {
            ToastUtils.showShort(R.string.philips_noNet);
        }
    }

    private void sendRandomSuccess() { //发送验证码成功
        LogUtils.d("发送验证码成功");

    }

    private void registerSuccess() { //注册成功
        hiddenLoading();
        LogUtils.d("注册成功");
        ToastUtils.showLong(R.string.register_success);
        Intent intent = new Intent(this, PhilipsLoginActivity.class);
        intent.putExtra(KeyConstants.AREA_CODE, mCountryCode);
        intent.putExtra(KeyConstants.COUNTRY, mTvSelectCountry.getText().toString().trim());
        intent.putExtra(KeyConstants.ACCOUNT, mEtPhoneOrMail.getText().toString().trim());
        intent.putExtra(KeyConstants.PASSWORD, mEtPwd.getText().toString().trim());
        startActivity(intent);

        finish();
    }

    private void sendRandomFailed(Throwable e) { //发送验证码失败
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, e));
    }

    private void sendRandomFailedServer(BaseResult result) {

        ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
    }

    private void registerFailed(Throwable e) { //注册失败
        hiddenLoading();
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, e));
    }

    private void registerFailedServer(BaseResult result) {
        hiddenLoading();
        if ("445".equals(result.getCode())){
            AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_correct_verification_code));
        }else {
            ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
        }
    }

    private void sendRandomByPhone(String phone, String code) {
        XiaokaiNewServiceImp.sendMessage(phone, code)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        LogUtils.d("发送验证码成功  " + result.getMsg());
                        sendRandomSuccess();
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

    private void sendRandomByEmail(String email) {
        XiaokaiNewServiceImp.sendEmailYZM(email)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        LogUtils.d("发送验证码成功  " + result.getMsg());
                        sendRandomSuccess();
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

    private void registerByPhone(String phone, String pwd, String random) {
        XiaokaiNewServiceImp.registerByPhone(phone, pwd, random)
                .subscribe(
                        new BaseObserver<RegisterResult>() {
                            @Override
                            public void onSuccess(RegisterResult result) {
                                LogUtils.d("注册成功  " + result.getData().toString());
                                MMKVUtils.setMMKV(SPUtils.TOKEN,result.getData().getToken());
                                MMKVUtils.setMMKV(SPUtils.UID,result.getData().getUid());
                                MyApplication.getInstance().setToken(result.getData().getToken());
                                MyApplication.getInstance().setUid(result.getData().getUid());

                                MMKVUtils.setMMKV(SPUtils.STORE_TOKEN,result.getData().getStoreToken());
                                registerSuccess();
                            }

                            @Override
                            public void onAckErrorCode(BaseResult baseResult) {
                                registerFailedServer(baseResult);
                            }

                            @Override
                            public void onFailed(Throwable throwable) {
                                registerFailed(throwable);
                            }

                            @Override
                            public void onSubscribe1(Disposable d) {

                            }
                        }
                );

    }

    private void registerByEmail(String phone, String pwd, String random) {
        XiaokaiNewServiceImp.registerByEmail(phone, pwd, random)
                .subscribe(
                        new BaseObserver<RegisterResult>() {
                            @Override
                            public void onSuccess(RegisterResult result) {
                                LogUtils.d("注册成功  " + result.getData().toString());
                                MMKVUtils.setMMKV(SPUtils.TOKEN,result.getData().getToken());
                                MMKVUtils.setMMKV(SPUtils.UID,result.getData().getUid());
                                MyApplication.getInstance().setToken(result.getData().getToken());
                                MyApplication.getInstance().setUid(result.getData().getUid());
                                MMKVUtils.setMMKV(SPUtils.STORE_TOKEN,result.getData().getStoreToken());
                                registerSuccess();
                            }

                            @Override
                            public void onAckErrorCode(BaseResult baseResult) {
                                registerFailedServer(baseResult);
                            }

                            @Override
                            public void onFailed(Throwable throwable) {
                                registerFailed(throwable);
                            }

                            @Override
                            public void onSubscribe1(Disposable d) {

                            }
                        }
                );

    }

}
