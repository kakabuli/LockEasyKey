package com.philips.easykey.lock.activity.my;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.PersonalUserAgreementAdapter;
import com.philips.easykey.lock.bean.PersonalUserAgreementBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.ConstantConfig;

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
//        tvRight = findViewById(R.id.tv_right);
        webView = findViewById(R.id.webView);

        initView();
        ivBack.setOnClickListener(this);
        tvContent.setText(R.string.philips_terms_of_use);
//        tvRight.setVisibility(View.VISIBLE);
//        tvRight.setText(R.string.philips_agree);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl(ConstantConfig.PHILIPS_TERMS_OF_USE);
    }


    private void initView() {
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }
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
