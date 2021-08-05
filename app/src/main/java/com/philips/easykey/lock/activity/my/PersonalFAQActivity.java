package com.philips.easykey.lock.activity.my;

import android.content.Intent;
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


public class PersonalFAQActivity extends BaseAddToApplicationActivity {

    ImageView ivBack;
    WebView webView;
    TextView tvContent;
    private String url = "http://app.kaadas.cc/pageFile/Philips_FQA/index.html";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_faq);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        webView = findViewById(R.id.webView);
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        ivBack.setOnClickListener(v -> back());
        tvContent.setText(R.string.faq);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back();
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
        if (webView == null) finish();
        if(TextUtils.equals(url,webView.getUrl())){
            finish();
        }else {
            webView.loadUrl(url);
            return;
        }
    }
}
