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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhilipsPersonalUpdateNumberActivity extends BaseActivity<IPersonalUpdateNumberView, PersonalUpdateNumberPresenter<IPersonalUpdateNumberView>> implements IPersonalUpdateNumberView {


    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_hint)
    TextView tvHint;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_personal_update_number);
        ButterKnife.bind(this);
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
        tvHint.setText(getString(R.string.activity_personal_verification_code_sent_to_the_following_number));
        //获取手机号码
        String phone = (String) SPUtils.get(SPUtils.PHONEN, "");
        if (!TextUtils.isEmpty(phone)) {
            tvNumber.setText(StringUtil.phoneToHide(phone));
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_next_or_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_next_or_complete:
                break;
        }
    }
}
