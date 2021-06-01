package com.philips.easykey.lock.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.personalpresenter.PersonalUpdateNumberPresenter;
import com.philips.easykey.lock.mvp.view.personalview.IPersonalUpdateNumberView;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;


public class PhilipsPersonalUpdateNumberActivity extends BaseActivity<IPersonalUpdateNumberView, PersonalUpdateNumberPresenter<IPersonalUpdateNumberView>> implements IPersonalUpdateNumberView {


    TextView tvContent;
    TextView tvNumber;
    TextView tvHint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_personal_update_number);

        tvContent = findViewById(R.id.tv_content);
        tvNumber = findViewById(R.id.tv_number);
        tvHint = findViewById(R.id.tv_hint);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        initView();
    }

    @Override
    protected PersonalUpdateNumberPresenter<IPersonalUpdateNumberView> createPresent() {
        return new PersonalUpdateNumberPresenter<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    private void initView() {
        tvContent.setText(getString(R.string.input_verification_code));
        tvHint.setText(getString(R.string.philips_activity_personal_verification_code_sent_to_the_following_number));
        //获取手机号码
        String phone = (String) SPUtils.get(SPUtils.PHONEN, "");
        if (!TextUtils.isEmpty(phone)) {
            tvNumber.setText(StringUtil.phoneToHide(phone));
        }
    }

}
