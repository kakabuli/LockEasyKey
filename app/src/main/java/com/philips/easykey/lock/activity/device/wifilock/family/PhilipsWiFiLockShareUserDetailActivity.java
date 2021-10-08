package com.philips.easykey.lock.activity.device.wifilock.family;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ViewUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WiFiLockShareUserDetailPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWiFiLockShareUserDetailView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


/**
 * 分享用户详情界面
 */
public class PhilipsWiFiLockShareUserDetailActivity extends BaseActivity<IWiFiLockShareUserDetailView, WiFiLockShareUserDetailPresenter
        <IWiFiLockShareUserDetailView>>  implements IWiFiLockShareUserDetailView, View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    Button confirm;
    EditText tvNumber;
    TextView tvName;
    TextView tvTime;
    TextView btnDelete;
    private String nickname;
    String data;
    WifiLockShareResult.WifiLockShareUser shareUser;
    private String adminNickname;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_family_member_detail);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        confirm = findViewById(R.id.confirm);
        tvNumber = findViewById(R.id.tv_number);
        tvName = findViewById(R.id.tv_name);
        tvTime = findViewById(R.id.tv_time);
        btnDelete = findViewById(R.id.btn_delete);

        ivBack.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        confirm.setOnClickListener(this);
        tvContent.setText(getString(R.string.philips_user_detail));
        Intent intent = getIntent();
        shareUser = (WifiLockShareResult.WifiLockShareUser) intent.getSerializableExtra(KeyConstants.SHARE_USER_INFO);
        tvNumber.setText(shareUser.getUserNickname());
        String nickname = tvNumber.getText().toString().trim();
        if(!TextUtils.isEmpty(nickname)) {
            changeConfirmBtnStyle(true);
        }
        tvName.setText(shareUser.getUname());
        long createTime = shareUser.getCreateTime();
        if (createTime == 0) {
            getAuthorizationTime();
            createTime = System.currentTimeMillis() / 1000;
        }
        String time = DateUtils.secondToDate(createTime);
        tvTime.setText(time);

        adminNickname = (String) SPUtils.get(SPUtils.USERNAME, "");
        if (TextUtils.isEmpty(adminNickname)) {
            adminNickname = (String) SPUtils.get(SPUtils.PHONEN, "");
        }

        tvNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = tvNumber.getText().toString().trim();
                if(TextUtils.isEmpty(phone)) {
                    changeConfirmBtnStyle(false);
                } else {
                    changeConfirmBtnStyle(!TextUtils.isEmpty(s.toString()));
                }
            }
        });
    }

    private void changeConfirmBtnStyle(boolean isSelect) {
        confirm.setEnabled(isSelect);
        confirm.setBackgroundResource(isSelect ? R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_login_bg);
        confirm.setTextColor(isSelect ? getResources().getColor(R.color.white) : getResources().getColor(R.color.colorPrimary));
    }


    private void getAuthorizationTime() {
    }

    @Override
    protected WiFiLockShareUserDetailPresenter<IWiFiLockShareUserDetailView> createPresent() {
        return new WiFiLockShareUserDetailPresenter<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_delete:
                //删除
                if (NetUtil.isNetworkAvailable()) {
                    AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(this, getString(R.string.sure_delete_user_permission),
                            getString(R.string.philips_cancel), getString(R.string.delete), "#0066A1", "#FFFFFF", new AlertDialogUtil.ClickListener() {
                                @Override
                                public void left() {

                                }

                                @Override
                                public void right() {
                                    showLoading(getString(R.string.is_deleting));
                                    mPresenter.deleteUserList(shareUser.get_id(),adminNickname);
                                }

                                @Override
                                public void onTextChanged(CharSequence s, int start, int before, int count) {

                                }

                                @Override
                                public void afterTextChanged(String toString) {

                                }
                            });
                } else {
                    ToastUtils.showLong(R.string.network_exception);
                }
                break;
            case R.id.confirm:
                if(shareUser.getUserNickname().equals(tvNumber.getText().toString().trim())) {
                    ToastUtils.showShort(getString(R.string.user_nickname_no_update));
                    return;
                }

                data = tvNumber.getText().toString().trim();
                if (!StringUtil.nicknameJudge(data)) {
                    ToastUtils.showShort(R.string.nickname_verify_error);
                    return;
                }
                showLoading(getString(R.string.is_modifing));

                mPresenter.modifyCommonUserNickname(shareUser.get_id(), data);

                break;
        }
    }

    @Override
    public void deleteCommonUserListSuccess(BaseResult baseResult) {
        ToastUtils.showShort(R.string.delete_common_user_success);
        finish();
    }

    @Override
    public void deleteCommonUserListFail(BaseResult baseResult) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, baseResult.getCode()));
        hiddenLoading();
    }

    @Override
    public void deleteCommonUserListError(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
        hiddenLoading();
    }

    @Override
    public void modifyCommonUserNicknameSuccess(BaseResult baseResult) {
        nickname = data;
        tvNumber.setText(nickname);
        shareUser.setUserNickname(nickname);
        ToastUtils.showShort(R.string.modify_user_nickname_success);
        hiddenLoading();
    }

    @Override
    public void modifyCommonUserNicknameFail(BaseResult baseResult) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, baseResult.getCode()));
        hiddenLoading();
    }

    @Override
    public void modifyCommonUserNicknameError(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
        hiddenLoading();
    }
}
