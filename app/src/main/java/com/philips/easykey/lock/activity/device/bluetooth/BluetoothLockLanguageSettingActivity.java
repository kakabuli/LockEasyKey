package com.philips.easykey.lock.activity.device.bluetooth;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleActivity;
import com.philips.easykey.lock.mvp.presenter.LanguageSetPresenter;
import com.philips.easykey.lock.mvp.view.ILanguageSetView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.blankj.utilcode.util.ToastUtils;


public class BluetoothLockLanguageSettingActivity extends BaseBleActivity<ILanguageSetView, LanguageSetPresenter<ILanguageSetView>>
        implements ILanguageSetView, View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    CheckBox zhImg;
    RelativeLayout zhLayout;
    CheckBox enImg;
    RelativeLayout enLayout;
    Button btnSave;
    private Context context;
    private String languageCurrent = "";
    private BleLockInfo bleLockInfo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_lock_language_setting);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        zhImg = findViewById(R.id.zh_img);
        zhLayout = findViewById(R.id.zh_layout);
        enImg = findViewById(R.id.en_img);
        enLayout = findViewById(R.id.en_layout);
        btnSave = findViewById(R.id.btn_save);
        bleLockInfo = mPresenter.getBleLockInfo();
        if (mPresenter.isAuth(bleLockInfo, false)) {
            mPresenter.getDeviceInfo();
        }else{
            ToastUtils.showLong(R.string.please_connect_lock);
        }
        context = MyApplication.getInstance();
        initData();
        tvContent.setText(getString(R.string.lock_language));
        ivBack.setOnClickListener(this);

        zhLayout.setOnClickListener(v -> {
            zhImg.setChecked(true);
            enImg.setChecked(false);
            languageCurrent = "zh";
        });
        enLayout.setOnClickListener(v -> {
            zhImg.setChecked(false);
            enImg.setChecked(true);
            languageCurrent = "en";
        });
        btnSave.setOnClickListener(v -> {
            if (mPresenter.isAuth(bleLockInfo, true)){
                if ("zh".equals(languageCurrent)){
                    mPresenter.setLanguage("zh");
                }else if ("en".equals(languageCurrent)){
                    mPresenter.setLanguage("en");
                }
            }
            showLoading(getString(R.string.is_setting));
        });

    }

    @Override
    protected LanguageSetPresenter<ILanguageSetView> createPresent() {
        return new LanguageSetPresenter<>();
    }

    private void initData() {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onGetLanguageSuccess(String lang) {
        if ("zh".equals(lang)) {  //中文
            zhImg.setChecked(true);
            enImg.setChecked(false);
            languageCurrent = "zh";
        } else {  //英文
            zhImg.setChecked(false);
            enImg.setChecked(true);
            languageCurrent = "en";
        }
    }

    @Override
    public void onGetLanguageFailed(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.read_device_language_fail));
    }

    @Override
    public void onSetLangSuccess(String language) {
        if ("zh".equals(language)) {  //中文
            languageCurrent = "zh";
        } else {  //英文
            languageCurrent = "en";
        }
        ToastUtils.showShort(R.string.set_success);
        hiddenLoading();
    }

    @Override
    public void onSetLangFailed(Throwable throwable) {
        ToastUtils.showShort(R.string.set_failed);
        hiddenLoading();
    }
}
