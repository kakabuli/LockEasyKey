package com.philips.easykey.lock.mvp.mvpbase;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.shulan.KeepAliveManager;
import com.philips.easykey.lock.shulan.config.ForegroundNotification;
import com.philips.easykey.lock.shulan.config.ForegroundNotificationClickListener;
import com.philips.easykey.lock.shulan.config.RunMode;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LanguageUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StatusBarUtils;

public class BaseAddToApplicationActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        //获取我们存储的语言环境 比如 "en","zh",等等
        String spLanguage = (String) SPUtils.getProtect(KeyConstants.LANGUAGE_SET, "");
        if(TextUtils.isEmpty(spLanguage)){
            super.attachBaseContext(newBase);
            return;
        }
        //attach 对应语言环境下的context
        super.attachBaseContext(LanguageUtil.attachBaseContext(newBase, spLanguage));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getInstance().addActivity(this);


        //第三种方法
        StatusBarUtils.with(this)
                .init();

        /**
         * 开启电量优化
         */
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            KeepAliveManager.batteryOptimizations(getApplicationContext());
        }*/

        startKeepAlive();
    }



    /**
     * 启动保活
     */
    public void startKeepAlive() {
        String spLanguage = (String) SPUtils.getProtect(KeyConstants.LANGUAGE_SET, "");
        if(!TextUtils.isEmpty(spLanguage)) {
            LanguageUtil.attachBaseContext(this, spLanguage);
        }
        String title = String.format(getString(R.string.app_name_notificatoin_title), getString(R.string.app_name));
        String content = String.format(getString(R.string.app_name_notificatoin_content), getString(R.string.app_name));

        //启动保活服务
        KeepAliveManager.toKeepAlive(
                getApplication()
                , RunMode.HIGH_POWER_CONSUMPTION, title,
                content,
                R.mipmap.ic_launcher,
                new ForegroundNotification(
                        //定义前台服务的通知点击事件
                        new ForegroundNotificationClickListener() {
                            @Override
                            public void foregroundNotificationClick(Context context, Intent intent) {
                                LogUtils.d("JOB-->", " foregroundNotificationClick");
//                                stopKeepAlive();
                            }
                        })
        );
    }

    /**
     * 停止保活
     */
    public void stopKeepAlive() {
        KeepAliveManager.stopWork(getApplication());
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().removeActivity(this);
        super.onDestroy();
    }

    @Override
    public Resources getResources() {
        return AdaptScreenUtils.adaptWidth(super.getResources(), 375);
    }

}
