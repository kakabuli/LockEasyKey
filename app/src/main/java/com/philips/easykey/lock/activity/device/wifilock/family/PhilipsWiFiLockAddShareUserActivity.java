package com.philips.easykey.lock.activity.device.wifilock.family;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WiFiLockShareAddUserPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWiFiLockShareAddUserView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
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
public class PhilipsWiFiLockAddShareUserActivity extends BaseActivity<IWiFiLockShareAddUserView, WiFiLockShareAddUserPresenter<IWiFiLockShareAddUserView>>
        implements View.OnClickListener, IWiFiLockShareAddUserView {

    ImageView ivBack;
    EditText etTelephone;
    Button btnConfirm;
    TextView mTvNickName;
    EditText mEtNickName;
    private String wifiSn;
    private String nickName;

    private WifiLockInfo mWifiLockInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_add_family_member);

        ivBack = findViewById(R.id.iv_back);
        etTelephone = findViewById(R.id.et_telephone);
        btnConfirm = findViewById(R.id.btn_confirm);
        mTvNickName = findViewById(R.id.tv_nick_name);
        mEtNickName = findViewById(R.id.et_nick_name);

        ivBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);

        mWifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

        if(mWifiLockInfo != null){
            mTvNickName.setText(getString(R.string.philips_share_user_nickname,mWifiLockInfo.getLockNickname()));
        }

        nickName = (String) SPUtils.get(SPUtils.USERNAME, "");
        if (TextUtils.isEmpty(nickName)) {
            nickName = (String) SPUtils.get(SPUtils.PHONEN, "");
        }

        etTelephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = etTelephone.getText().toString().trim();
                if(TextUtils.isEmpty(phone)) {
                    changeConfirmBtnStyle(false);
                } else {
                    changeConfirmBtnStyle(!TextUtils.isEmpty(s.toString()));
                }
            }
        });
    }

    private void changeConfirmBtnStyle(boolean isSelect) {
        btnConfirm.setEnabled(isSelect);
        btnConfirm.setBackgroundResource(isSelect ? R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_login_bg);
        btnConfirm.setTextColor(isSelect ? getResources().getColor(R.color.white) : getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected WiFiLockShareAddUserPresenter<IWiFiLockShareAddUserView> createPresent() {
        return new WiFiLockShareAddUserPresenter<>();
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
                        ToastUtils.showShort(R.string.no_add_my);
                        return;
                    }
                }

                if (!NetUtil.isNetworkAvailable()) {
                    ToastUtils.showShort(R.string.philips_noNet);
                    return;
                }

                if (TextUtils.isEmpty(phone)) {
                    AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_account_message_not_empty));
                    return;
                }


                String userNickName = "86" + phone;
                if(!mEtNickName.getText().toString().trim().isEmpty()){
                    userNickName = mEtNickName.getText().toString().trim();
                }

                if (StringUtil.isNumeric(phone)) {
                    if (!PhoneUtil.isMobileNO(phone)) {
                        // 账户密码错误 请输入正确验证码 调用这个方法传入对应的内容就可以
                        AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                        return;
                    }

                    mPresenter.addUser(wifiSn, "86" + phone, userNickName, nickName);
                    showLoading(getString(R.string.is_adding));
                } else {
                    if (!DetectionEmailPhone.isEmail(phone)) {
                        AlertDialogUtil.getInstance().noButtonSingleLineDialog(this, getString(R.string.philips_input_valid_telephone_or_email));
                        return;
                    }

                    mPresenter.addUser(wifiSn, phone, userNickName, nickName);
                    showLoading(getString(R.string.is_adding));

                }
                break;

        }
    }

    @Override
    public void onAddUserSuccess() {
        hiddenLoading();
        ToastUtils.showLong(R.string.share_success);
        finish();
    }

    @Override
    public void onAddUserFailed(BaseResult result) {
        hiddenLoading();
        ToastUtils.showLong( HttpUtils.httpErrorCode(this, result.getCode()));
    }

    @Override
    public void onAddUserFailedServer(Throwable throwable) {
        hiddenLoading();
//        ToastUtils.showLong(R.string.add_failed);
        ToastUtils.showLong(HttpUtils.httpProtocolErrorCode(this, throwable));
    }
}
