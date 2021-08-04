package com.philips.easykey.lock.activity.my;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.PersonalUserAgreementAdapter;
import com.philips.easykey.lock.bean.PersonalUserAgreementBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

import java.util.ArrayList;
import java.util.List;


public class PersonalUserAgreementActivity extends BaseAddToApplicationActivity implements View.OnClickListener {


    ImageView ivBack;
    TextView tvContent;
    TextView tvRight;
    WebView webView;


    //总数据
    private List<PersonalUserAgreementBean> personalUserAgreementBeansList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_user_agreement);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        tvRight = findViewById(R.id.tv_right);
        webView = findViewById(R.id.webView);

        initView();
        ivBack.setOnClickListener(this);
        tvContent.setText(R.string.philips_terms_of_use);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText(R.string.philips_agree);
        webView.loadUrl("http://app.kaadas.cc/pageFile/Philips_Terms_of_Use/index.html");
    }


    private void initView() {
//        getData();
    }


    private void getData() {
        personalUserAgreementBeansList = new ArrayList<>();
        PersonalUserAgreementBean p0 = new PersonalUserAgreementBean();
        p0.setContent(getResources().getString(R.string.user_agreement_title));
        p0.setFlag(true);
        personalUserAgreementBeansList.add(p0);

        PersonalUserAgreementBean p1 = new PersonalUserAgreementBean();
        p1.setTitle(getResources().getString(R.string.user_agreement_one_title));
        p1.setContent(getResources().getString(R.string.user_agreement_one_content));
        p1.setFlag(false);
        personalUserAgreementBeansList.add(p1);
        PersonalUserAgreementBean p2 = new PersonalUserAgreementBean();
        p2.setTitle(getResources().getString(R.string.user_agreement_two_title));
        p2.setContent(getResources().getString(R.string.user_agreement_two_content));
        p2.setFlag(false);
        personalUserAgreementBeansList.add(p2);
        PersonalUserAgreementBean p3 = new PersonalUserAgreementBean();
        p3.setTitle(getResources().getString(R.string.user_agreement_three_title));
        p3.setContent(getResources().getString(R.string.user_agreement_three_content));
        p3.setFlag(false);
        personalUserAgreementBeansList.add(p3);
        PersonalUserAgreementBean p4 = new PersonalUserAgreementBean();
        p4.setTitle(getResources().getString(R.string.user_agreement_four_title));
        p4.setContent(getResources().getString(R.string.user_agreement_four_content));
        p4.setFlag(false);
        personalUserAgreementBeansList.add(p4);
        PersonalUserAgreementBean p5 = new PersonalUserAgreementBean();
        p5.setTitle(getResources().getString(R.string.user_agreement_five_title));
        p5.setContent(getResources().getString(R.string.user_agreement_five_content));
        p5.setFlag(false);
        personalUserAgreementBeansList.add(p5);
        PersonalUserAgreementBean p6 = new PersonalUserAgreementBean();
        p6.setTitle(getResources().getString(R.string.user_agreement_six_title));
        p6.setContent(getResources().getString(R.string.user_agreement_six_content));
        p6.setFlag(false);
        personalUserAgreementBeansList.add(p6);
        PersonalUserAgreementBean p7 = new PersonalUserAgreementBean();
        p7.setTitle(getResources().getString(R.string.user_agreement_seven_title));
        p7.setContent(getResources().getString(R.string.user_agreement_seven_content));
        p7.setFlag(false);
        personalUserAgreementBeansList.add(p7);

        PersonalUserAgreementBean p8 = new PersonalUserAgreementBean();
        p8.setTitle(getResources().getString(R.string.user_agreement_eight_title));
        p8.setContent(getResources().getString(R.string.user_agreement_eight_content));
        p8.setFlag(false);
        personalUserAgreementBeansList.add(p8);


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
