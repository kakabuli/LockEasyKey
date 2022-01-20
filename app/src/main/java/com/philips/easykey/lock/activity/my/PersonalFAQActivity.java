package com.philips.easykey.lock.activity.my;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.fragment.help.PersonalFAQHangerHelpFragment;
import com.philips.easykey.lock.fragment.help.PersonalFAQLockHelpFragment;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.ConstantConfig;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.Locale;


public class PersonalFAQActivity extends BaseAddToApplicationActivity {

    ImageView ivBack;
    WebView webView;
    TextView tvContent;
    private String mUrl = ConstantConfig.PHILIPS_FQA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_faq);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        webView = findViewById(R.id.webView);
        String spLanguage = (String) SPUtils.getProtect(KeyConstants.LANGUAGE_SET, "");
        if(TextUtils.isEmpty(spLanguage)){
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if(TextUtils.equals(language,"zh")){
                mUrl = ConstantConfig.PHILIPS_FQA_ZH;
            }else if(TextUtils.equals(language,"en")){
                mUrl = ConstantConfig.PHILIPS_FQA_EN;
            }else {
                mUrl = ConstantConfig.PHILIPS_FQA;
            }
        }else {
            if(TextUtils.equals(spLanguage,"zh")){
                mUrl = ConstantConfig.PHILIPS_FQA_ZH;
            }else {
                mUrl = ConstantConfig.PHILIPS_FQA_EN;
            }
        }
        webView.loadUrl(mUrl);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient(){
            @Override
            public void  onPageStarted(WebView view, String url, Bitmap favicon) {
                //设定加载开始的操作
                if(TextUtils.equals(mUrl,webView.getUrl())){
                    tvContent.setText(R.string.philips_common_problem);
                }else {
                    tvContent.setText(R.string.philips_common_problem_of_lock);
                }

            }
            @Override
            public void onPageFinished(WebView view, String url) {
                //设定加载结束的操作

            }
        });
        ivBack.setOnClickListener(v -> back());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }
    }

    private void back() {
        Log.d("zdx", "back: " + mUrl);
        Log.d("zdx", "back: " + webView.getUrl());

        if (webView == null) finish();
        if(TextUtils.equals(mUrl,webView.getUrl())){
            finish();
        }else {
            webView.loadUrl(mUrl);
        }
    }

}
