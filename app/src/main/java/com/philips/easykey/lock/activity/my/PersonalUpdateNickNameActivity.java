package com.philips.easykey.lock.activity.my;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.personalpresenter.PersonalUpdateNickNamePresenter;
import com.philips.easykey.lock.mvp.view.personalview.IPersonalUpdateNickNameView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalUpdateNickNameActivity extends BaseActivity<IPersonalUpdateNickNameView, PersonalUpdateNickNamePresenter<IPersonalUpdateNickNameView>> implements IPersonalUpdateNickNameView, View.OnClickListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.et_nickName)
    EditText etNickName;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.bt_ok)
    Button btOk;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_update_nickname);
        ButterKnife.bind(this);
        initView();
        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.philips_set_nickname));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected PersonalUpdateNickNamePresenter<IPersonalUpdateNickNameView> createPresent() {
        return new PersonalUpdateNickNamePresenter<>();
    }

    private void initView() {
        //获取昵称
        userName = (String) SPUtils.get(SPUtils.USERNAME, "");
        if (!TextUtils.isEmpty(userName)) {
            etNickName.setText(userName);
            etNickName.setSelection(userName.length());
        }
        etNickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    btOk.setBackgroundResource(R.drawable.philips_button_0066a1_3);
                } else {
                    btOk.setBackgroundResource(R.drawable.philips_button_b3c8e6_3);
                }
            }
        });
    }


    @OnClick({R.id.delete, R.id.bt_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.delete:
                etNickName.setText("");
                break;
            case R.id.bt_ok:
                String editText = etNickName.getText().toString().trim();
                if (NetUtil.isNetworkAvailable()) {
                    if (TextUtils.isEmpty(editText)) {
                        ToastUtils.showShort(R.string.nickName_not_empty);
                        return;
                    }
                    if (!StringUtil.nicknameJudge(editText, 20)) {

                        ToastUtils.showShort(R.string.philips_nickname_verify_error);
                        return;
                    }

                    if (editText.equals(userName)) {
                        ToastUtils.showShort(R.string.nickname_repeat);
                        return;
                    }
                    mPresenter.updateNickName(MyApplication.getInstance().getUid(), editText);

                } else {
                    ToastUtils.showShort(R.string.philips_noNet);
                }
                break;

        }
    }

    @Override
    public void updateNickNameSuccess(String nickName) {
        //更新成功
        SPUtils.put(SPUtils.USERNAME, nickName);
        ToastUtils.showShort(R.string.update_nick_name);
        finish();
    }

    @Override
    public void updateNickNameError(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void updateNickNameFail(BaseResult baseResult) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, baseResult.getCode()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
