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


public class PersonalUpdateNickNameActivity extends BaseActivity<IPersonalUpdateNickNameView, PersonalUpdateNickNamePresenter<IPersonalUpdateNickNameView>> implements IPersonalUpdateNickNameView, View.OnClickListener {


    ImageView ivBack;
    TextView tvContent;
    EditText etNickName;
    ImageView delete;
    Button btOk;
    private String userName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_update_nickname);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        etNickName = findViewById(R.id.et_nickName);
        delete = findViewById(R.id.delete);
        btOk = findViewById(R.id.bt_ok);

        delete.setOnClickListener(v -> etNickName.setText(""));
        btOk.setOnClickListener(v -> {
            String editText = etNickName.getText().toString().trim();
            if (NetUtil.isNetworkAvailable()) {
                if (TextUtils.isEmpty(editText)) {
                    ToastUtils.showShort(R.string.nickName_not_empty);
                    return;
                }
                if (!StringUtil.nicknameJudge(editText, 10)) {

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
        });

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
