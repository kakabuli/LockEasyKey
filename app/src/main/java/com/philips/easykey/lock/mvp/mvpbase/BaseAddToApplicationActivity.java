package com.philips.easykey.lock.mvp.mvpbase;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.AdaptScreenUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.shulan.KeepAliveManager;
import com.philips.easykey.lock.shulan.config.ForegroundNotification;
import com.philips.easykey.lock.shulan.config.ForegroundNotificationClickListener;
import com.philips.easykey.lock.shulan.config.RunMode;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.StatusBarUtils;

public class BaseAddToApplicationActivity extends AppCompatActivity {

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

//        startKeepAlive();
    }

    /**
     * 启动保活
     */
    private void startKeepAlive() {
        //启动保活服务
        KeepAliveManager.toKeepAlive(
                getApplication()
                , RunMode.HIGH_POWER_CONSUMPTION, getString(R.string.app_name_notificatoin_title),
                getString(R.string.app_name_notificatoin_content),
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
