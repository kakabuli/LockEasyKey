package com.philips.easykey.lock.activity.login;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.activity.choosecountry.CountryActivity;
import com.philips.easykey.lock.normal.NormalBaseActivity;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.LoginResult;
import com.philips.easykey.lock.publiclibrary.http.result.UserNickResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.publiclibrary.http.util.LoginObserver;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.DetectionEmailPhone;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.MD5Utils;
import com.philips.easykey.lock.utils.MMKVUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.PhoneUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.Disposable;

/**
 * author : Jack
 * time   : 2021/5/11
 * E-mail : wengmaowei@kaadas.com
 * desc   : 登录
 */
public class PhilipsLoginActivity extends NormalBaseActivity {

    private EditText mEtPhoneOrMail, mEtPwd;
    private Button mBtnLogin;
    private TextView mTvSelectCountry;
    private ImageView mIvShowOrHide;

    private final int mCountryReqCode = 1233;
    private String mCountryCode = "86";

    private boolean isShowDialog = false;

    private boolean isShowPwd = false;

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
                String pwd = mEtPhoneOrMail.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)) {
                    changeLoginBtnStyle(false);
                } else {
                    changeLoginBtnStyle(!TextUtils.isEmpty(s.toString()));
                }
            }
        });

        applyDebouncingClickListener(findViewById(R.id.tvForgotPwd), findViewById(R.id.tvRegister),
                mBtnLogin, findViewById(R.id.ivVerification), findViewById(R.id.ivWechat),
                mTvSelectCountry, mIvShowOrHide);
        setStatusBarColor(R.color.white);

    }

    @Override
    public void doBusiness() {
        initLoginData();
        checkVersion();
        initAccountFromLocal();
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
            Intent intent = new Intent(this, ForgetPasswordActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.tvRegister) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        } else if(view.getId() == R.id.btnLogin) {
            login();
        } else if(view.getId() == R.id.ivWechat) {
            // TODO: 2021/5/12 微信登录
        } else if(view.getId() == R.id.ivVerification) {
            // TODO: 2021/5/12 验证码登录
        } else if(view.getId() == R.id.tvSelectCountry) {
            Intent intent = new Intent(this, CountryActivity.class);
            startActivityForResult(intent, mCountryReqCode);
        } else if(view.getId() == R.id.ivShowOrHide) {
            changePasswordStatus();
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
            mCountryCode = intent.getStringExtra(KeyConstants.AREA_CODE);
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
                    mCountryCode.replace("+", "");
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
