package com.philips.easykey.lock.activity.device.videolock;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.fragment.help.WifiVideoLockCommonProblemHelpFragment;
import com.philips.easykey.lock.fragment.help.WifiVideoLockToConfigureHelpFragment;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class WifiVideoLockHelpActivity extends BaseAddToApplicationActivity {

    View vLeft;
    View vRight;
    FrameLayout content;
    TextView tvLeft;
    TextView tvRight;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private WifiVideoLockCommonProblemHelpFragment mWifiVideoLockCommonProblemHelpFragment;
    private WifiVideoLockToConfigureHelpFragment mWifiVideoLockToConfigureHelpFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_video_lock_help);

        vLeft = findViewById(R.id.v_left);
        vRight = findViewById(R.id.v_right);
        content = findViewById(R.id.content);
        tvLeft = findViewById(R.id.tv_left);
        tvRight = findViewById(R.id.tv_right);

        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.rl_left).setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            hideAll(fragmentTransaction);
            vLeft.setVisibility(View.VISIBLE);
            vRight.setVisibility(View.GONE);
            tvLeft.setTextColor(Color.parseColor("#1F96F7"));
            tvRight.setTextColor(Color.parseColor("#333333"));
            if( mWifiVideoLockToConfigureHelpFragment != null){
                fragmentTransaction.show(mWifiVideoLockToConfigureHelpFragment);
            }else{
                mWifiVideoLockToConfigureHelpFragment = new WifiVideoLockToConfigureHelpFragment();
                fragmentTransaction.add(R.id.content,mWifiVideoLockToConfigureHelpFragment);
            }
            fragmentTransaction.commit();
        });
        findViewById(R.id.rl_right).setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = manager.beginTransaction();
            hideAll(fragmentTransaction);
            vLeft.setVisibility(View.GONE);
            vRight.setVisibility(View.VISIBLE);
            tvRight.setTextColor(Color.parseColor("#1F96F7"));
            tvLeft.setTextColor(Color.parseColor("#333333"));
            if( mWifiVideoLockCommonProblemHelpFragment != null){
                fragmentTransaction.show(mWifiVideoLockCommonProblemHelpFragment);
            }else{
                mWifiVideoLockCommonProblemHelpFragment = new WifiVideoLockCommonProblemHelpFragment();
                fragmentTransaction.add(R.id.content,mWifiVideoLockCommonProblemHelpFragment);
            }
            fragmentTransaction.commit();
        });

        initFragment();
    }


    private void hideAll(FragmentTransaction ft) {
        if (ft == null) {
            return;
        }
        if (mWifiVideoLockToConfigureHelpFragment != null) {
            ft.hide(mWifiVideoLockToConfigureHelpFragment);
        }
        if (mWifiVideoLockCommonProblemHelpFragment != null) {
            ft.hide(mWifiVideoLockCommonProblemHelpFragment);
        }
    }

    private void initFragment() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        vLeft.setVisibility(View.VISIBLE);
        vRight.setVisibility(View.GONE);
        tvLeft.setTextColor(Color.parseColor("#1F96F7"));
        tvRight.setTextColor(Color.parseColor("#333333"));
        mWifiVideoLockToConfigureHelpFragment = new WifiVideoLockToConfigureHelpFragment();
        transaction.add(R.id.content, mWifiVideoLockToConfigureHelpFragment);
        transaction.commit();
    }

}
