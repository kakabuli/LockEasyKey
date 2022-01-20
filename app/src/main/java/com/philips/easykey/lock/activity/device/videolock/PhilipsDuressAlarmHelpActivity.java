package com.philips.easykey.lock.activity.device.videolock;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.ConstantConfig;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.Locale;

public class PhilipsDuressAlarmHelpActivity extends BaseAddToApplicationActivity {

    ImageView ivBack;
    TextView tvContent;
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.philips_add_device_help_dialog);
        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        webView = findViewById(R.id.webView);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvContent.setText(R.string.what_is_coercion_alarm);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        String spLanguage = (String) SPUtils.getProtect(KeyConstants.LANGUAGE_SET, "");
        if(TextUtils.isEmpty(spLanguage)){
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if(TextUtils.equals(language,"zh")){
                webView.loadUrl(ConstantConfig.PHILPS_DURRESS_HELP_ZH);
            }else if(TextUtils.equals(language,"en")){
                webView.loadUrl(ConstantConfig.PHILPS_DURRESS_HELP_EN);
            }else {
                webView.loadUrl(ConstantConfig.PHILPS_DURRESS_HELP);
            }
        }else {
            if(TextUtils.equals(spLanguage,"zh")){
                webView.loadUrl(ConstantConfig.PHILPS_DURRESS_HELP_ZH);
            }else {
                webView.loadUrl(ConstantConfig.PHILPS_DURRESS_HELP_EN);
            }
        }
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            webView.loadUrl(url);
            return true;
        }
    }
}
