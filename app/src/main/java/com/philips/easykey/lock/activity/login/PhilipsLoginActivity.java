package com.philips.easykey.lock.activity.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.activity.choosecountry.CountryActivity;
import com.philips.easykey.lock.activity.my.PersonalUserAgreementActivity;
import com.philips.easykey.lock.activity.my.PrivacyActivity;
import com.philips.easykey.lock.normal.NormalBaseActivity;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWeChatOpenIdResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWeChatUserPhoneResult;
import com.philips.easykey.lock.publiclibrary.http.result.LoginResult;
import com.philips.easykey.lock.publiclibrary.http.result.RegisterWeChatAndBindPhoneResult;
import com.philips.easykey.lock.publiclibrary.http.result.UserNickResult;
import com.philips.easykey.lock.publiclibrary.http.result.WeChatLoginResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.publiclibrary.http.util.LoginObserver;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.DetectionEmailPhone;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.LinkClickableSpan;
import com.philips.easykey.lock.utils.MD5Utils;
import com.philips.easykey.lock.utils.MMKVUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.PhoneUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.philips.easykey.lock.wxapi.NetworkUtil;
import com.philips.easykey.lock.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.Disposable;

/**
 * author : Jack
 * time   : 2021/5/11
 * E-mail : wengmaowei@kaadas.com
 * desc   : 登录
 */
public class PhilipsLoginActivity extends NormalBaseActivity{

    private EditText mEtPhoneOrMail, mEtPwd;
    private Button mBtnLogin;
    private TextView mTvSelectCountry;
    private ImageView mIvShowOrHide;
    private EditText mEtVerificationCode;
    private TextView mTvAgreement;
    private TextView mTvGetCode;
    private ImageView mIvPhone;
    private TextView mTvPhone;
    private ImageView mIvVerification;
    private TextView mTvCode;
    private TextView mTvForgotPwd;
    private TextView mTvRegister;

    private final int mCountryReqCode = 1233;
    private String mCountryCode = "86";
    private String mWXopenId = "";

    private boolean isShowDialog = false;

    private boolean isShowPwd = false;
    private boolean isCountdown = false;

    private final long mCountDownTotalTime = 60000;

    private final String phoneLogin = "phone";
    private final String codeType = "code";
    private final String wxLogin = "wx";
    private String loginType = phoneLogin;

    private final CountDownTimer mCountDownTimer = new CountDownTimer(mCountDownTotalTime, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String value = String.valueOf((int) (millisUntilFinished / 1000));
            mTvGetCode.setText(value);
        }

        @Override
        public void onFinish() {
            isCountdown = false;
            mTvGetCode.setText(getString(R.string.philips_get_verification));
        }
    };

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.philips_activity_login;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        mEtPhoneOrMail = findViewById(R.id.etPhoneOrMail);
        mEtPwd = findViewById(R.id.etPwd);
        mBtnLogin = findViewById(R.id.btnLogin);
        mTvSelectCountry = findViewById(R.id.tvSelectCountry);
        mIvShowOrHide = findViewById(R.id.ivShowOrHide);
        mTvGetCode = findViewById(R.id.tvGetCode);
        mEtVerificationCode = findViewById(R.id.etVerificationCode);
        mIvPhone = findViewById(R.id.ivPhone);
        mTvPhone = findViewById(R.id.tvPhone);
        mIvVerification = findViewById(R.id.ivVerification);
        mTvCode = findViewById(R.id.tvCode);
        mTvForgotPwd = findViewById(R.id.tvForgotPwd);
        mTvRegister = findViewById(R.id.tvRegister);
        mTvAgreement = findViewById(R.id.tvAgreement);

        changeLoginBtnStyle(false);
        mEtPhoneOrMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = mEtPwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)) {
                    changeLoginBtnStyle(false);
                } else {
                    changeLoginBtnStyle(!TextUtils.isEmpty(s.toString()));
                }

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
                String account = mEtPhoneOrMail.getText().toString().trim();
                if(TextUtils.isEmpty(account)) {
                    changeLoginBtnStyle(false);
                } else {
                    changeLoginBtnStyle(!TextUtils.isEmpty(s.toString()));
                }
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
                String account = mEtPhoneOrMail.getText().toString().trim();
                if(TextUtils.isEmpty(account)) {
                    changeLoginBtnStyle(false);
                } else {
                    changeLoginBtnStyle(!TextUtils.isEmpty(s.toString()));
                }
            }
        });

        applyDebouncingClickListener(mTvForgotPwd, mTvRegister,
                mBtnLogin, mIvPhone,mIvVerification, findViewById(R.id.ivWechat),
                mTvSelectCountry, mIvShowOrHide, mTvGetCode);
        setStatusBarColor(R.color.white);

        initTerms();
        initStatement();
    }

    @Override
    public void doBusiness() {
        initLoginData();
        checkVersion();
        initAccountFromLocal();
    }

    private void initStatement() {
        boolean showStatementAndTerms = (boolean) SPUtils.getProtect(KeyConstants.SHOW_STATEMENT_AND_TERMS, true);
        if(!showStatementAndTerms)return;
        AlertDialogUtil.getInstance().statementAndTermsDialog(
                this
                , getString(R.string.philips_statements_and_terms),
                getString(R.string.philips_statements_and_terms_content),
                getString(R.string.philips_no_agree),getString(R.string.philips_agree),new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {
                        finish();
                    }

                    @Override
                    public void right() {
                        SPUtils.putProtect(KeyConstants.SHOW_STATEMENT_AND_TERMS, false);
                        MyApplication.getInstance().initSDK();
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void initTerms() {
        String termsOfUseStr = getString(R.string.philips_terms_of_use2);
        SpannableString termsOfUseSpannable = new SpannableString(termsOfUseStr);
        LinkClickableSpan termsOfUseSpan = new LinkClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent agreementIntent = new Intent(PhilipsLoginActivity.this, PersonalUserAgreementActivity.class);
                startActivity(agreementIntent);
            }
        };
        termsOfUseSpannable.setSpan(termsOfUseSpan, 0, termsOfUseStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        String privacyPolicyStr = getString(R.string.philips_privacy_policy);
        SpannableString privacyPolicySpannable = new SpannableString(privacyPolicyStr);
        LinkClickableSpan privacyPolicySpan = new LinkClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent privacyIntent = new Intent(PhilipsLoginActivity.this, PrivacyActivity.class);
                startActivity(privacyIntent);
            }
        };
        privacyPolicySpannable.setSpan(privacyPolicySpan, 0, termsOfUseStr.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTvAgreement.append(termsOfUseSpannable);
        mTvAgreement.append(getString(R.string.philips_and));
        mTvAgreement.append(privacyPolicySpannable);
        mTvAgreement.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initAccountFromLocal() {
        String phone = (String) SPUtils.get(SPUtils.PHONEN, "");
        if (phone != null && phone.length() != 0) {
            mEtPhoneOrMail.setText(phone);
            mEtPhoneOrMail.setSelection(phone.length());
        }
    }

    @Override
    public void onDebouncingClick(@NonNull @NotNull View view) {
        if(view.getId() == R.id.tvForgotPwd) {
            Intent intent = new Intent(this, PhilipsForgetPwdActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.tvRegister) {
            Intent intent = new Intent(this, PhilipsRegisterActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.btnLogin) {
            if(TextUtils.equals(loginType,phoneLogin)){
                login();
            }else if(TextUtils.equals(loginType,codeType)){
                codeLogin();
            }else if(TextUtils.equals(loginType,wxLogin)){
                registerWeChatAndBindPhone();
            }
        } else if(view.getId() == R.id.ivWechat) {
            wechatLogin();
        } else if(view.getId() == R.id.ivVerification) {
            // TODO: 2021/5/20 临时屏蔽，等提供接口后再恢复
            loginType = codeType;
            changeToVCodeLogin();
        } else if(view.getId() == R.id.ivPhone){
            loginType = phoneLogin;
            changeToAccountLogin();
        } else if(view.getId() == R.id.tvSelectCountry) {
            Intent intent = new Intent(this, CountryActivity.class);
            startActivityForResult(intent, mCountryReqCode);
        } else if(view.getId() == R.id.ivShowOrHide) {
            changePasswordStatus();
        } else if(view.getId() == R.id.tvGetCode) {
            if(isCountdown) return;
            getVerification();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == mCountryReqCode) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String countryName = bundle.getString("countryName");
                String countryNumber = bundle.getString("countryNumber");
                LogUtils.d("davi 选择的国家==" + countryName + " 区号==" + countryNumber);
                mCountryCode = countryNumber;
                mTvSelectCountry.setText(countryName);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void changeToVCodeLogin() {
        mEtVerificationCode.setVisibility(View.VISIBLE);
        mTvGetCode.setVisibility(View.VISIBLE);
        mEtPwd.setVisibility(View.INVISIBLE);
        mIvShowOrHide.setVisibility(View.INVISIBLE);
        mIvPhone.setVisibility(View.VISIBLE);
        mTvPhone.setVisibility(View.VISIBLE);
        mIvVerification.setVisibility(View.GONE);
        mTvCode.setVisibility(View.GONE);
        mTvForgotPwd.setVisibility(View.GONE);
        mTvRegister.setVisibility(View.GONE);
    }

    private void wechatLogin() {
        if (!MyApplication.getInstance().getApi().isWXAppInstalled()) {
            ToastUtils.showShort(R.string.you_have_not_installed_wechat);
            return;
        }
        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        // 用于保持请求和回调的状态，授权请求后原样带回给第三方。
        // 该参数可用于防止 csrf 攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加 session 进行校验
        req.state = "wechat_sdk_demo_test";
        MyApplication.getInstance().getApi().sendReq(req);
        WXEntryActivity.setWXdata(new WXEntryActivity.onWXDataListener() {
            @Override
            public void data(String code) {
                loginType = wxLogin;
                getWeChatOpenId(code);
            }
        });
    }

    private void changeToAccountLogin() {
        mEtVerificationCode.setVisibility(View.GONE);
        mTvGetCode.setVisibility(View.GONE);
        mEtPwd.setVisibility(View.VISIBLE);
        mIvShowOrHide.setVisibility(View.VISIBLE);
        mIvPhone.setVisibility(View.GONE);
        mTvPhone.setVisibility(View.GONE);
        mIvVerification.setVisibility(View.VISIBLE);
        mTvCode.setVisibility(View.VISIBLE);
        mTvForgotPwd.setVisibility(View.VISIBLE);
        mTvRegister.setVisibility(View.VISIBLE);
    }

    private void changeLoginBtnStyle(boolean isCanLogin) {
        mBtnLogin.setEnabled(isCanLogin);
        mBtnLogin.setBackgroundResource(isCanLogin?R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_login_bg);
    }

    private void changePasswordStatus() {
        isShowPwd = !isShowPwd;
        mIvShowOrHide.setImageResource(isShowPwd?R.drawable.philips_dms_icon_display:R.drawable.philips_dms_icon_hidden);
        mEtPwd.setInputType(isShowPwd?
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                :(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD));
        mEtPwd.setSelection(mEtPwd.getText().toString().trim().length());
    }

    public void checkVersion() {
        Boolean updateFlag = (Boolean) SPUtils.get(SPUtils.APPUPDATE, false);
        if (updateFlag) {
            appUpdateDialog();
        }
    }

    private void toMarkApp() {
        Uri uri = Uri.parse("market://details?id=" + "com.philips.easykey.lock");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void appUpdateDialog() {
        AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.hint), getString(R.string.find_newAPP), getString(R.string.philips_cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
            @Override
            public void left() {
            }

            @Override
            public void right() {
                toMarkApp();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(String toString) {

            }
        });
    }

    protected void initLoginData() {
        Intent intent = getIntent();
        if (intent != null) {
            if(intent.hasExtra(KeyConstants.AREA_CODE)) {
                mCountryCode = intent.getStringExtra(KeyConstants.AREA_CODE);
            }
            String country = intent.getStringExtra(KeyConstants.COUNTRY);
            String account = intent.getStringExtra(KeyConstants.ACCOUNT);
            String password = intent.getStringExtra(KeyConstants.PASSWORD);
            if (!TextUtils.isEmpty(country)){
                mTvSelectCountry.setText(country);
            }
            if (!TextUtils.isEmpty(account)){
                mEtPhoneOrMail.setText(account);
                mEtPhoneOrMail.setSelection(account.length());
            }
            if (!TextUtils.isEmpty(password)){
                mEtPwd.setText(password);
                mEtPwd.setSelection(password.length());
            }
            isShowDialog = intent.getBooleanExtra(KeyConstants.IS_SHOW_DIALOG, false);
        }

        if (isShowDialog) { //如果需要显示提示token过期弹窗，那么提示、
            //AlertDialogUtil.getInstance().singleButtonSingleHintDialog(this,getString(R.string.token_out_date));
            //存在适配问题修改弹窗
            tokenDialog(getString(R.string.token_out_date));
        }
    }

    private void tokenDialog(String content) {
        AlertDialogUtil.getInstance().noEditSingleButtonDialog(this, getString(R.string.hint), content, getString(R.string.dialog_confirm), new AlertDialogUtil.ClickListener() {
            @Override
            public void left() {

            }

            @Override
            public void right() {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(String toString) {

            }
        });

    }


    private void login() {
        if (NetUtil.isNetworkAvailable()) {
            String phoneOrMail = getEdittextContent(mEtPhoneOrMail);
            String pwd = getEdittextContent(mEtPwd);
            if (TextUtils.isEmpty(phoneOrMail)) {
                AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_account_message_not_empty));
                return;
            }
            if (StringUtil.judgeSpecialCharacter(pwd)) {
                ToastUtils.showShort(R.string.not_input_special_symbol);
                return;
            }
            if (StringUtil.isNumeric(phoneOrMail)) {
                if (!PhoneUtil.isMobileNO(phoneOrMail)) {
                    // 账户密码错误 请输入正确验证码 调用这个方法传入对应的内容就可以
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                } else {
                    //密码错误
                    if (!StringUtil.passwordJudge(pwd)) {
                        AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.account_password_error));
                        return;
                    }
                    if(TextUtils.isEmpty(mCountryCode)) {
                        return;
                    }
                    mCountryCode = mCountryCode.replace("+", "");
                    showLoading(getString(R.string.login_in));
                    loginByPhone(mCountryCode + phoneOrMail, pwd, phoneOrMail);
                }
            } else {
                if (!DetectionEmailPhone.isEmail(phoneOrMail)) {
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                } else {
                    //密码错误
                    if (!StringUtil.passwordJudge(pwd)) {
                        AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.account_password_error));
                        return;
                    }
                    showLoading(getString(R.string.login_in));
                    loginByEmail(phoneOrMail, pwd);
                }
            }

        } else {
            ToastUtils.showShort(R.string.philips_noNet);
        }
    }


    @NonNull
    private String getEdittextContent(EditText et) {
        return et.getText().toString().trim();
    }

    public void onLoginSuccess() {
        hiddenLoading();
        LogUtils.d("登陆成功");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.ISFROMLOGIN,true);
        startActivity(intent);
        finish();
    }

    public void onLoginFailed(Throwable e) {
        mBtnLogin.setBackgroundResource(R.drawable.login_button_shape);
        hiddenLoading();
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, e));
    }

    public void onLoginFailedServer(LoginResult result) {
        mBtnLogin.setBackgroundResource(R.drawable.login_button_shape);
        hiddenLoading();
        if ("101".equals(result.getCode() + "")){
            if(result.getData() != null){
                if(result.getData().getRestrictCount() >= 5){
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_login_password_restrict,
                            result.getData().getRestrictCount() + "" , getString(StringUtil.getTimeToString(result.getData().getRestrictTime()))));
                }else{
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.account_password_error));
                }
            }else{
                AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.account_password_error));
            }
        }else {
            ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
        }
    }

    public void sendRandomByPhone(String phone, String code) {
        XiaokaiNewServiceImp.sendMessage(phone, code)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
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

    public void sendRandomFailedServer(BaseResult result) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
    }

    public void sendRandomFailed(Throwable e) {
        LogUtils.d("验证码发送失败");
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, e));
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
                    sendRandomByPhone(account,countryCode);
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

    public void registerWeChatAndBindPhone() {
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
            String countryCode = mCountryCode.trim().replace("+", "");
            XiaokaiNewServiceImp.registerWeChatAndBindPhone(mWXopenId,countryCode + account, code)
                    .subscribe(new BaseObserver<RegisterWeChatAndBindPhoneResult>() {
                        @Override
                        public void onSuccess(RegisterWeChatAndBindPhoneResult registerWeChatAndBindPhoneResult) {
                            loginSuccess(registerWeChatAndBindPhoneResult.getData().getToken(),registerWeChatAndBindPhoneResult.getData().getUid(),account);
                        }

                        @Override
                        public void onAckErrorCode(BaseResult baseResult) {
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                        }

                        @Override
                        public void onSubscribe1(Disposable d) {

                        }
                    });
        } else {
            ToastUtils.showShort(R.string.philips_noNet);
        }
    }

    private void getWeChatOpenId(String code ){
        XiaokaiNewServiceImp.getWeChatOpenId(code)
                .subscribe(new BaseObserver<GetWeChatOpenIdResult>() {
                    @Override
                    public void onSuccess(GetWeChatOpenIdResult getWeChatOpenIdResult) {
                        mWXopenId = getWeChatOpenIdResult.getData().getOpenId();
                        if(TextUtils.isEmpty(mWXopenId))return;
                        getTelByOpenId(mWXopenId);
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {

                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

    //根据微信openId获取手机号，如果返回code = 448,就进入手机获取验证码界面，绑定手机号
    private void getTelByOpenId(String openId){
        XiaokaiNewServiceImp.getWeChatUserPhone(openId)
                .subscribe(new BaseObserver<GetWeChatUserPhoneResult>() {
                    @Override
                    public void onSuccess(GetWeChatUserPhoneResult getWeChatUserPhoneResult) {
                        String tel = getWeChatUserPhoneResult.getData().getTel();
                        if(TextUtils.isEmpty(tel))return;
                        weChatLogin(mWXopenId,tel);
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if(baseResult.getCode().equals("448")){
                            changeToVCodeLogin();
                        }
                    }



                    @Override
                    public void onFailed(Throwable throwable) {
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

    private void weChatLogin(String openId,String tel){
        XiaokaiNewServiceImp.weChatLogin(openId,tel)
                .subscribe(new BaseObserver<WeChatLoginResult>() {
                    @Override
                    public void onSuccess(WeChatLoginResult weChatLoginResult) {
                        loginSuccess(weChatLoginResult.getData().getToken(),weChatLoginResult.getData().getUid(), tel);
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        LogUtils.d("微信登陆失败   " + baseResult.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

    public void loginByPhone(String phone, String pwd, String noCountryPhone) {
        XiaokaiNewServiceImp.loginByPhone(phone, pwd)
                .subscribe(new LoginObserver<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginSuccess(loginResult, noCountryPhone, pwd);
                        LogUtils.d(loginResult.toString());
                    }

                    @Override
                    public void onAckErrorCode(LoginResult result) {
                        onLoginFailedServer(result);
                        LogUtils.d("登陆失败   " + result.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        onLoginFailed(throwable);
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

    public void loginByEmail(String Email, String pwd) {
        XiaokaiNewServiceImp.loginByEmail(Email, pwd)
                .subscribe(new LoginObserver<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        loginSuccess(loginResult, Email, pwd);
                    }

                    @Override
                    public void onAckErrorCode(LoginResult result) {
                        onLoginFailedServer(result);
                        LogUtils.d("登陆失败   " + result.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        onLoginFailed(throwable);
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

    private void codeLogin(){
        if (NetUtil.isNetworkAvailable()) {
             String account = StringUtil.getEdittextContent(mEtPhoneOrMail);
            mCountryCode = mCountryCode.replace("+", "");
            if (TextUtils.isEmpty(account)) {
                AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_account_message_not_empty));
                return;
            }
            String code = StringUtil.getEdittextContent(mEtVerificationCode);
            if (TextUtils.isEmpty(code) || code.length() != 6) {
                AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_correct_verification_code));
                return;
            }
            XiaokaiNewServiceImp.codeLogin(code,mCountryCode + account).subscribe(new BaseObserver<WeChatLoginResult>() {
                @Override
                public void onSuccess(WeChatLoginResult weChatLoginResult) {
                    loginSuccess(weChatLoginResult.getData().getToken(),weChatLoginResult.getData().getUid(),account);
                }

                @Override
                public void onAckErrorCode(BaseResult baseResult) {
                    LogUtils.d("手机验证码登录失败   " + baseResult.toString());
                }

                @Override
                public void onFailed(Throwable throwable) {

                }

                @Override
                public void onSubscribe1(Disposable d) {

                }
            });
        }else {
            ToastUtils.showShort(R.string.philips_noNet);
        }
    }

    //获取用户名称
    private void getUserName(String uid) {
        XiaokaiNewServiceImp.getUserNick(uid).subscribe(new BaseObserver<UserNickResult>() {
            @Override
            public void onSuccess(UserNickResult userNickResult) {
                if ("200".equals(userNickResult.getCode())) {
                    setUserName(userNickResult.getData().getNickName());
                }
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {

            }

            @Override
            public void onFailed(Throwable throwable) {

            }

            @Override
            public void onSubscribe1(Disposable d) {

            }
        });
    }

    private void setUserName(String userName) {
        SPUtils.put(SPUtils.USERNAME, userName);
    }

    private void loginSuccess(String token , String uid ,String phone) {
        //请求用户名称，由于服务器返回过来的用户名称为空，因此需要重新获取
        onLoginSuccess();
        LogUtils.d("登陆成功  数据是  token" + token);
        //保存数据到本地  以及 内存
//        SPUtils.put(SPUtils.TOKEN, loginResult.getData().getToken());
//        SPUtils.put(SPUtils.UID, loginResult.getData().getUid());
        MMKVUtils.setMMKV(SPUtils.TOKEN,token);
        MMKVUtils.setMMKV(SPUtils.UID,uid);
        SPUtils.put(SPUtils.PHONEN, phone);

        MyApplication.getInstance().setToken(token);
        MyApplication.getInstance().setUid(uid);
        getUserName(uid);
    }

    private void loginSuccess(LoginResult loginResult, String phone, String pwd) {
        //请求用户名称，由于服务器返回过来的用户名称为空，因此需要重新获取
        onLoginSuccess();
        LogUtils.d("登陆成功  数据是  " + loginResult.toString());
        //保存数据到本地  以及 内存
//        SPUtils.put(SPUtils.TOKEN, loginResult.getData().getToken());
//        SPUtils.put(SPUtils.UID, loginResult.getData().getUid());
        MMKVUtils.setMMKV(SPUtils.TOKEN,loginResult.getData().getToken());
        MMKVUtils.setMMKV(SPUtils.UID,loginResult.getData().getUid());
        SPUtils.put(SPUtils.PHONEN, phone);
        SPUtils.put(SPUtils.PASSWORD, MD5Utils.encode(pwd));


        MyApplication.getInstance().setToken(loginResult.getData().getToken());
        MyApplication.getInstance().setUid(loginResult.getData().getUid());
        getUserName(loginResult.getData().getUid());

        MMKVUtils.setMMKV(SPUtils.STORE_TOKEN,loginResult.getData().getStoreToken());
    }
}
