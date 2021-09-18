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
import com.philips.easykey.lock.utils.ConstantConfig;


public class PersonalFAQActivity extends BaseAddToApplicationActivity {

    ImageView ivBack;
    WebView webView;
    TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_faq);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        webView = findViewById(R.id.webView);
        webView.loadUrl(ConstantConfig.PHILIPS_FQA);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        ivBack.setOnClickListener(v -> back());
        tvContent.setText(R.string.faq);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            back();
            return false;
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
        if (webView == null) finish();
        Log.d("lhf","111---------" + ConstantConfig.PHILIPS_FQA);
        Log.d("lhf","222---------" + webView.getUrl());
        if(TextUtils.equals(ConstantConfig.PHILIPS_FQA,webView.getUrl())){
            finish();
        }else {
            webView.loadUrl(ConstantConfig.PHILIPS_FQA);
        }
    }
}
