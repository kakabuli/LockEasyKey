package com.philips.easykey.lock.activity.device.wifilock.password;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.WiFiLockCardAndFingerShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockNickNamePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockNickNameView;
import com.philips.easykey.lock.publiclibrary.bean.FacePassword;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


public class PhilipsWifiLockPasswordDetailActivity extends BaseActivity<IWifiLockNickNameView, WifiLockNickNamePresenter<IWifiLockNickNameView>>
        implements IWifiLockNickNameView {

    private Button confirm;
    private EditText mEtNickName;
    private ImageView back;
    private int pwdType; //	密钥类型：1密码 2指纹密码 3卡片密码 4面容识别
    private String wifiSn;
    private WiFiLockCardAndFingerShowBean wiFiLockCardAndFingerShowBean;
    private ForeverPassword foreverPassword;
    private FacePassword facePassword;
    private String nickName;
    private int num;
    private String adminNickName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_password_detail);

        initView();
        initData();
        initListener();
    }

    private void initData() {
        pwdType = getIntent().getIntExtra(KeyConstants.PASSWORD_TYPE, 1);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);

        if (pwdType == 1) {
            foreverPassword = (ForeverPassword) getIntent().getSerializableExtra(KeyConstants.TO_PWD_DETAIL);
            if(foreverPassword.getNickName() != null)
                nickName = foreverPassword.getNickName();
            num = Integer.parseInt(foreverPassword.getNum());
        } else if (pwdType == 2 || pwdType == 3) {
            wiFiLockCardAndFingerShowBean = (WiFiLockCardAndFingerShowBean) getIntent().getSerializableExtra(KeyConstants.TO_PWD_DETAIL);
            nickName = wiFiLockCardAndFingerShowBean.getNickName();
            this.num = wiFiLockCardAndFingerShowBean.getNum();
        }else if(pwdType == 4){
            facePassword = (FacePassword) getIntent().getSerializableExtra(KeyConstants.TO_PWD_DETAIL);
            nickName = facePassword.getNickName();
            num = Integer.parseInt(facePassword.getNum());

        }
        mEtNickName.setHint(nickName);
        adminNickName = (String) SPUtils.get(SPUtils.USERNAME, "");
        if (TextUtils.isEmpty(adminNickName)) {
            adminNickName = (String) SPUtils.get(SPUtils.PHONEN, "");
        }
    }

    private void initView() {
        confirm = findViewById(R.id.confirm);
        mEtNickName = findViewById(R.id.et_nickname);
        back = findViewById(R.id.back);
    }

    private void initListener() {
        mEtNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = mEtNickName.getText().toString().trim();
                if(TextUtils.isEmpty(phone)) {
                    confirm.setPressed(false);
                } else {
                    confirm.setPressed(!TextUtils.isEmpty(s.toString()));
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.judgeNicknameWhetherSame(nickName, mEtNickName.getText().toString().trim())) {
                    ToastUtils.showShort(R.string.nickname_not_modify);
                    return;
                }
                if(!nickName.equals(mEtNickName.getText().toString().trim())){
                    mPresenter.updateNickName(wifiSn, pwdType, num, mEtNickName.getText().toString().trim(), adminNickName);
                }
            }
        });
    }

    @Override
    protected WifiLockNickNamePresenter<IWifiLockNickNameView> createPresent() {
        return new WifiLockNickNamePresenter<>();
    }

    @Override
    public void onUpdateNickSuccess() {
        hiddenLoading();
        ToastUtils.showShort(R.string.modify_success);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onUpdateNickFailed(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(R.string.modify_failed);
    }

    @Override
    public void onUpdateNickFailedServer(BaseResult result) {
        hiddenLoading();
        ToastUtils.showShort(R.string.modify_failed);
    }
}
