package com.philips.easykey.lock.activity.my;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import java.util.Locale;

public class PersonalLanguageSettingActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    TextView zhText;
    CheckBox zhImg;
    RelativeLayout zhLayout;
    CheckBox cbCt;
    RelativeLayout rlTranitionalChinese;
    TextView enText;
    CheckBox enImg;
    RelativeLayout enLayout;
    TextView thaiText;
    CheckBox thaiImg;
    RelativeLayout thaiLayout;
    Button languageConfirm;
    private Context context;
    private String languageCurrent = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_language_setting);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        zhText = findViewById(R.id.zh_text);
        zhImg = findViewById(R.id.zh_img);
        zhLayout = findViewById(R.id.zh_layout);
        cbCt = findViewById(R.id.cb_ct);
        rlTranitionalChinese = findViewById(R.id.rl_tranitional_chinese);
        enText = findViewById(R.id.en_text);
        enImg = findViewById(R.id.en_img);
        enLayout = findViewById(R.id.en_layout);
        thaiText = findViewById(R.id.thai_text);
        thaiImg = findViewById        (R.id.thai_img);
        thaiLayout = findViewById(R.id.thai_layout);
        languageConfirm = findViewById(R.id.language_confirm);

        zhLayout.setOnClickListener(v -> {
            zhImg.setChecked(true);
            enImg.setChecked(false);
            thaiImg.setChecked(false);
            cbCt.setChecked(false);
            languageCurrent = "zh";
            SPUtils.putProtect( "lag", "zh");
        });
        rlTranitionalChinese.setOnClickListener(v -> {
            //繁体中文
            cbCt.setChecked(true);
            zhImg.setChecked(false);
            enImg.setChecked(false);
            thaiImg.setChecked(false);
            languageCurrent = "tw";
            SPUtils.putProtect( "lag", "tw");
        });
        enLayout.setOnClickListener(v -> {
            zhImg.setChecked(false);
            enImg.setChecked(true);
            thaiImg.setChecked(false);
            cbCt.setChecked(false);
            languageCurrent = "en";
            SPUtils.putProtect( "lag", "en");
        });
        thaiLayout.setOnClickListener(v -> {
            zhImg.setChecked(false);
            enImg.setChecked(false);
            thaiImg.setChecked(true);
            cbCt.setChecked(false);
            languageCurrent = "th";
            SPUtils.putProtect( "lag", "th");
        });
        languageConfirm.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(languageCurrent)) {
//                    SPUtils.putProtect( "checklag", true);
                switchLanguage(languageCurrent);
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        });

        context = MyApplication.getInstance();
        initData();
        tvContent.setText(R.string.language_setting);
        ivBack.setOnClickListener(this);

    }

    private void initData() {
        checkLag();
    }

    /**
     * android语言切换
     *
     * @param language
     */
    protected void switchLanguage(String language) {
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("zh")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals("tw")) {
            config.locale = Locale.TAIWAN;
        } else if (language.equals("th")) {
            config.locale = new Locale("th");
        } else {
            config.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(config, dm);
        //保存设置语言的类型
        SPUtils.putProtect("language", language);
    }

 /*   protected void switchLanguage(String language) {
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("zh")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else if (language.equals("tw")) {
            config.locale = Locale.TAIWAN;
        }  else if (language.equals("tai")) {
            config.locale = new Locale("th");
        } else {
            config.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(config, dm);
        //保存设置语言的类型
        CacheFloder.writeLanguage(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "language", language);
    }*/


  /*  private void checkLag() {
        //是否设置过语言
        String language = CacheFloder.readLanguage(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "language");
        if (TextUtils.isEmpty(language)) {
            Locale locale = getResources().getConfiguration().locale;
            String localeLanguage = locale.getLanguage();//获取本地语言
            if (locale.equals(Locale.SIMPLIFIED_CHINESE)) { //简体中文
                zhImg.setChecked(true);
                enImg.setChecked(false);
                thaiImg.setChecked(false);
                cbCt.setChecked(false);
                CacheFloder.writeLanguage(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "language", "zh");
            } else if (localeLanguage.endsWith("tai")) {
                zhImg.setChecked(false);
                enImg.setChecked(false);
                thaiImg.setChecked(true);
                cbCt.setChecked(false);
                CacheFloder.writeLanguage(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "language", "tai");
            } else {
                zhImg.setChecked(false);
                enImg.setChecked(true);
                thaiImg.setChecked(false);
                cbCt.setChecked(false);
                CacheFloder.writeLanguage(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "language", "en");
            }
        } else {
            if (language.equals("zh")) {
                zhImg.setChecked(true);
                enImg.setChecked(false);
                thaiImg.setChecked(false);
                cbCt.setChecked(false);
            } else if (language.equals("tai")) {
                zhImg.setChecked(false);
                enImg.setChecked(false);
                thaiImg.setChecked(true);
                cbCt.setChecked(false);
            } else {
                zhImg.setChecked(false);
                enImg.setChecked(true);
                thaiImg.setChecked(false);
                cbCt.setChecked(false);
            }
        }
    }*/

    private void checkLag() {
        //是否设置过语言
        boolean checklag = (Boolean) SPUtils.getProtect( "checklag", false);
        if (!checklag) {
            Locale locale = getResources().getConfiguration().locale;
            String language = locale.getLanguage();
            LogUtils.d(" local  "+locale.toLanguageTag());
            if (locale.equals( Locale.SIMPLIFIED_CHINESE)) { //简体中文
                zhImg.setChecked(true);
                enImg.setChecked(false);
                thaiImg.setChecked(false);
                cbCt.setChecked(false);
                SPUtils.putProtect("lag", "zh");
            } else if (locale.equals( Locale.TAIWAN)) { //繁体中文
                zhImg.setChecked(false);
                enImg.setChecked(false);
                thaiImg.setChecked(false);
                cbCt.setChecked(true);
                SPUtils.putProtect( "lag", "tw");
            } else if (language.endsWith("th")) {
                zhImg.setChecked(false);
                enImg.setChecked(false);
                thaiImg.setChecked(true);
                cbCt.setChecked(false);
                SPUtils.putProtect( "lag", "th");
            } else {
                zhImg.setChecked(false);
                enImg.setChecked(true);
                thaiImg.setChecked(false);
                cbCt.setChecked(false);
                SPUtils.putProtect( "lag", "en");
            }
        } else {
            String lag = (String) SPUtils.getProtect( "lag", "");
            if (lag.equals("zh")) {
                zhImg.setChecked(true);
                enImg.setChecked(false);
                thaiImg.setChecked(false);
                cbCt.setChecked(false);
                SPUtils.putProtect( "lag", "zh");
            } else if (lag.equals("en")) {
                zhImg.setChecked(false);
                enImg.setChecked(true);
                thaiImg.setChecked(false);
                cbCt.setChecked(false);
                SPUtils.putProtect( "lag", "en");
            } else if (lag.equals("tw")) {
                zhImg.setChecked(false);
                enImg.setChecked(false);
                thaiImg.setChecked(false);
                cbCt.setChecked(true);
                SPUtils.putProtect( "lag", "tw");
            } else if (lag.equals("th")) {
                zhImg.setChecked(false);
                enImg.setChecked(false);
                thaiImg.setChecked(true);
                cbCt.setChecked(false);
                SPUtils.putProtect("lag", "th");
            }
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
