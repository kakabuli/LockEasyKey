package com.philips.easykey.lock.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.PersonalUserAgreementBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.ConstantConfig;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.List;
import java.util.Locale;

/**
 * 用户注销提示页面
 */
public class SignOutTipsActivity extends BaseAddToApplicationActivity implements View.OnClickListener {


    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    WebView webView;


    //总数据
    private List<PersonalUserAgreementBean> personalUserAgreementBeansList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privacy_agreement);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        webView = findViewById(R.id.webView);

        initView();
        ivBack.setOnClickListener(this);
        tvContent.setText(R.string.acc_sign_out);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        String spLanguage = (String) SPUtils.getProtect(KeyConstants.LANGUAGE_SET, "");
        if(TextUtils.isEmpty(spLanguage)){
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if(TextUtils.equals(language,"zh")){
                webView.loadUrl(ConstantConfig.PHILIPS_ACC_SIGN_OUT_TIPS_POLICY_ZH);
            }else if(TextUtils.equals(language,"en")){
                webView.loadUrl(ConstantConfig.PHILIPS_ACC_SIGN_OUT_TIPS_POLICY_EN);
            }else {
                webView.loadUrl(ConstantConfig.PHILIPS_ACC_SIGN_OUT_TIPS_POLICY);
            }
        }else {
            if(TextUtils.equals(spLanguage,"zh")){
                webView.loadUrl(ConstantConfig.PHILIPS_ACC_SIGN_OUT_TIPS_POLICY_ZH);
            }else {
                webView.loadUrl(ConstantConfig.PHILIPS_ACC_SIGN_OUT_TIPS_POLICY_EN);
            }
        }
    }


    private void initView() {
        //getData();
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
