package com.philips.easykey.lock.activity.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.activity.choosecountry.CountryActivity;
import com.philips.easykey.lock.normal.NormalBaseActivity;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWeChatOpenIdResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWeChatUserPhoneResult;
import com.philips.easykey.lock.publiclibrary.http.result.RegisterWeChatAndBindPhoneResult;
import com.philips.easykey.lock.publiclibrary.http.result.UserNickResult;
import com.philips.easykey.lock.publiclibrary.http.result.WeChatLoginResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.MMKVUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.PhoneUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.philips.easykey.lock.wxapi.WXEntryActivity;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.jetbrains.annotations.NotNull;

import io.reactivex.disposables.Disposable;

public class PhilipsWeChatLoginActivity extends NormalBaseActivity {

    private Button mBtnLogin;
    private TextView mTvSelectCountry;
    private EditText mEtVerificationCode,mEtPhone;
    private TextView mTvAgreement;
    private TextView mTvGetCode;
    private ImageView mIvPhone;

    private final int mCountryReqCode = 1233;
    private String mCountryCode = "86";
    private String mWXopenId ;

    private boolean isShowDialog = false;

    private boolean isShowPwd = false;
    private boolean isCountdown = false;

    private final long mCountDownTotalTime = 60000;

    private final String phoneLogin = "phone";
    private final String codeType = "code";
    private final String wxLogin = "wx";
    private String loginType = phoneLogin;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.philips_activity_we_chat_login;
    }


    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {

        intent = getIntent();
        mWXopenId = intent.getStringExtra(KeyConstants.WECHAT_OPENID);

        mEtPhone = findViewById(R.id.etPhone);
        mBtnLogin = findViewById(R.id.btnLogin);
        mTvSelectCountry = findViewById(R.id.tvSelectCountry);
        mEtVerificationCode = findViewById(R.id.etVerificationCode);
        mTvGetCode = findViewById(R.id.tvGetCode);

        changeLoginBtnStyle(false);

        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String pwd = mEtVerificationCode.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)) {
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
                String account = mEtPhone.getText().toString().trim();
                if(TextUtils.isEmpty(account)) {
                    changeLoginBtnStyle(false);
                } else {
                    changeLoginBtnStyle(!TextUtils.isEmpty(s.toString()));
                }
            }
        });

        applyDebouncingClickListener(mBtnLogin, mIvPhone, findViewById(R.id.ivWechat),
                mTvSelectCountry, mTvGetCode,findViewById(R.id.ivBack));

    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onDebouncingClick(@NonNull @NotNull View view) {
        if(view.getId() == R.id.btnLogin) {
            registerWeChatAndBindPhone();
        } else if(view.getId() == R.id.ivBack){
            finish();
        } else if(view.getId() == R.id.tvSelectCountry) {
            Intent intent = new Intent(this, CountryActivity.class);
            startActivityForResult(intent, mCountryReqCode);
        }  else if(view.getId() == R.id.tvGetCode) {
            if(isCountdown) return;
            getVerification();
        }
    }

    private void changeLoginBtnStyle(boolean isCanLogin) {
        mBtnLogin.setEnabled(isCanLogin);
        mBtnLogin.setTextColor(isCanLogin? Color.parseColor("#FFFFFF"):Color.parseColor("#0066A1"));
        mBtnLogin.setBackgroundResource(isCanLogin?R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_login_bg);
    }

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

    //获取验证码
    private void getVerification() {
        if (NetUtil.isNetworkAvailable()) {
            String account = StringUtil.getEdittextContent(mEtPhone);
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
            final String account = StringUtil.getEdittextContent(mEtPhone);
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
    public void onLoginSuccess() {
        hiddenLoading();
        LogUtils.d("登陆成功");
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constants.ISFROMLOGIN,true);
        startActivity(intent);
        finish();
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



}