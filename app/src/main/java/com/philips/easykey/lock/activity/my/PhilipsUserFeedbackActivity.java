package com.philips.easykey.lock.activity.my;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.UserFeedbackPresenter;
import com.philips.easykey.lock.mvp.view.IUserFeedbackView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.blankj.utilcode.util.LogUtils;


/**
 * Created by David on 2019/4/4
 */
public class PhilipsUserFeedbackActivity extends BaseActivity<IUserFeedbackView, UserFeedbackPresenter<IUserFeedbackView>> implements IUserFeedbackView, RadioGroup.OnCheckedChangeListener, TextWatcher {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    TextView tvRight;
    RadioButton rbOne;
    RadioButton rbTwo;
    RadioButton rbThree;
    Button btnSubmit;
    RadioGroup rg;
    EditText et;
    TextView tvNumber;
    int messageType = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_user_feedback);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        tvRight = findViewById(R.id.tv_right);
        rbOne = findViewById(R.id.rb_one);
        rbTwo = findViewById(R.id.rb_two);
        rbThree = findViewById(R.id.rb_three);
        btnSubmit = findViewById(R.id.btn_submit);
        rg = findViewById(R.id.rg);
        et = findViewById(R.id.et);
        tvNumber = findViewById(R.id.tv_number);

        ivBack.setOnClickListener(v -> finish());
        btnSubmit.setOnClickListener(v -> {
            //todo 后续等待服务器更改服务器接口
//                if (messageType != 0) {
            String text = et.getText().toString().trim();
            if (text.length() >= 8) {
                mPresenter.userFeedback(MyApplication.getInstance().getUid(), text);
            } else if (text.length() > 0) {
                ToastUtils.showShort(R.string.feedback_little);
            } else {
                ToastUtils.showShort(R.string.enter_feedback);
            }
        });

        tvContent.setText(getString(R.string.user_feedback));
        ivRight.setVisibility(View.GONE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(getString(R.string.philips_feedback_record));
        rg.setOnCheckedChangeListener(this);
        et.addTextChangedListener(this);

    }

    @Override
    protected UserFeedbackPresenter<IUserFeedbackView> createPresent() {
        return new UserFeedbackPresenter<>();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_one:
                messageType = 1;
                break;
            case R.id.rb_two:
                messageType = 2;
                break;
            case R.id.rb_three:
                messageType = 3;
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        LogUtils.d("davi s" + s.toString());
        tvNumber.setText(s.toString().length() + "/300");
    }

    @Override
    public void userFeedbackSubmitSuccess() {
        ToastUtils.showShort(R.string.submit_success);
        finish();
    }

    @Override
    public void userFeedbackSubmitFailed(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void userFeedbackSubmitFailedServer(BaseResult result) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
    }

}
