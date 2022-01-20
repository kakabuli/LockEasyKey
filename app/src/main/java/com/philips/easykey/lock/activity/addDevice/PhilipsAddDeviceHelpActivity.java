package com.philips.easykey.lock.activity.addDevice;

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
import com.philips.easykey.lock.activity.my.PersonalUserAgreementActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.ConstantConfig;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.Locale;

public class PhilipsAddDeviceHelpActivity extends BaseAddToApplicationActivity {

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
        tvContent.setText(R.string.philips_how_to_add_devices);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
        String spLanguage = (String) SPUtils.getProtect(KeyConstants.LANGUAGE_SET, "");
        if(TextUtils.isEmpty(spLanguage)){
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            if(TextUtils.equals(language,"zh")){
                webView.loadUrl(ConstantConfig.PHILIPS_ADD_SCAN_DEVICE_ZH);
            }else if(TextUtils.equals(language,"en")){
                webView.loadUrl(ConstantConfig.PHILIPS_ADD_SCAN_DEVICE_EN);
            }else {
                webView.loadUrl(ConstantConfig.PHILIPS_ADD_SCAN_DEVICE);
            }
        }else {
            if(TextUtils.equals(spLanguage,"zh")){
                webView.loadUrl(ConstantConfig.PHILIPS_ADD_SCAN_DEVICE_ZH);
            }else {
                webView.loadUrl(ConstantConfig.PHILIPS_ADD_SCAN_DEVICE_EN);
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
