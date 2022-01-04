package com.philips.easykey.lock.activity.device.videolock;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.activity.device.wifilock.password.PhilipsWiFiLockPasswordManagerActivity;
import com.philips.easykey.lock.activity.device.wifilock.password.PhilipsWifiLockPasswordShareActivity;
import com.philips.easykey.lock.activity.login.PhilipsLoginActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LanguageUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;

import java.util.Locale;

public class PhilipsLanguageSetActivity extends BaseAddToApplicationActivity {

    ImageView back;
    CheckBox zhImg;
    CheckBox enImg;
    private String languageCurrent = "";
    private String initLanguage = "";
    private String activity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_language_set);
        back = findViewById(R.id.back);
        zhImg = findViewById(R.id.zh_img);
        enImg = findViewById(R.id.en_img);
        activity = getIntent().getStringExtra("activity");
        String spLanguage = (String) SPUtils.getProtect(KeyConstants.LANGUAGE_SET, "");
        if(TextUtils.isEmpty(spLanguage)){
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            setImgChecked(language);
        }else {
            setImgChecked(spLanguage);
        }
        findViewById(R.id.en_layout).setOnClickListener(v -> {
                if(!enImg.isChecked()){
                    zhImg.setChecked(false);
                    enImg.setChecked(true);
                    languageCurrent = "en";
                }
            ;
        });
        findViewById(R.id.zh_layout).setOnClickListener(v -> {
                if(!zhImg.isChecked()){
                    zhImg.setChecked(true);
                    enImg.setChecked(false);
                    languageCurrent = "zh";
            }
        });
        back.setOnClickListener(v -> {
            if(TextUtils.isEmpty(languageCurrent)){
                finish();
                return;
            }

            if(TextUtils.equals(languageCurrent,initLanguage)){
                finish();
                return ;
            }
            switchLanguage(languageCurrent);
        });
    }

    private void setImgChecked(String type){
        initLanguage = type;
        if(TextUtils.equals(type,"zh")){
            zhImg.setChecked(true);
            enImg.setChecked(false);
        }else {
            zhImg.setChecked(false);
            enImg.setChecked(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(TextUtils.isEmpty(languageCurrent)){
                finish();
                return true;
            }

            if(TextUtils.equals(languageCurrent,initLanguage)){
                finish();
                return true;
            }
            switchLanguage(languageCurrent);
            return true;
        }
        return super.onKeyDown(keyCode,event);

    }

    /**
     * android语言切换
     *
     * @param language
     */
    protected void switchLanguage(String language) {
        SPUtils.putProtect(KeyConstants.LANGUAGE_SET, language);
        Intent intent = null;
        if(TextUtils.equals(activity,"login")){
            intent = new Intent(this, PhilipsLoginActivity.class);
        }else {
            intent = new Intent(this, MainActivity.class);
        }
        stopKeepAlive();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        startKeepAlive();
        finish();
    }
}