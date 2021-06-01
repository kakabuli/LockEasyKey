package com.philips.easykey.lock.activity.my;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.fragment.help.PersonalFAQHangerHelpFragment;
import com.philips.easykey.lock.fragment.help.PersonalFAQLockHelpFragment;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class PersonalFAQActivity extends BaseAddToApplicationActivity {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    View vLeft;
    View vRight;
    FrameLayout content;
    TextView tvLeft;
    TextView tvRight;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private PersonalFAQLockHelpFragment mPersonalFAQLockHelpFragment;
    private PersonalFAQHangerHelpFragment mPersonalFAQHangerHelpFragment;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_faq);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        vLeft = findViewById(R.id.v_left);
        vRight = findViewById(R.id.v_right);
        content = findViewById(R.id.content);
        tvLeft = findViewById(R.id.tv_left);
        tvRight = findViewById(R.id.tv_right);

        ivBack.setOnClickListener(v -> finish());
        findViewById(R.id.rl_left).setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = manager.beginTransaction();
            hideAll(fragmentTransaction);
            vLeft.setVisibility(View.VISIBLE);
            vRight.setVisibility(View.GONE);
            tvLeft.setTextColor(Color.parseColor("#1F96F7"));
            tvRight.setTextColor(Color.parseColor("#333333"));
            if( mPersonalFAQLockHelpFragment != null){
                fragmentTransaction.show(mPersonalFAQLockHelpFragment);
            }else{
                mPersonalFAQLockHelpFragment = new PersonalFAQLockHelpFragment();
                fragmentTransaction.add(R.id.content,mPersonalFAQLockHelpFragment);
            }
            fragmentTransaction.commit();
        });
        findViewById(R.id.rl_right).setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction;
            fragmentTransaction = manager.beginTransaction();
            hideAll(fragmentTransaction);
            vLeft.setVisibility(View.GONE);
            vRight.setVisibility(View.VISIBLE);
            tvRight.setTextColor(Color.parseColor("#1F96F7"));
            tvLeft.setTextColor(Color.parseColor("#333333"));
            if( mPersonalFAQHangerHelpFragment != null){
                fragmentTransaction.show(mPersonalFAQHangerHelpFragment);
            }else{
                mPersonalFAQHangerHelpFragment = new PersonalFAQHangerHelpFragment();
                fragmentTransaction.add(R.id.content,mPersonalFAQHangerHelpFragment);
            }
            fragmentTransaction.commit();
        });

        tvContent.setText(R.string.faq);
        initFragment();
    }

    private void hideAll(FragmentTransaction ft) {
        if (ft == null) {
            return;
        }
        if (mPersonalFAQLockHelpFragment != null) {
            ft.hide(mPersonalFAQLockHelpFragment);
        }
        if (mPersonalFAQHangerHelpFragment != null) {
            ft.hide(mPersonalFAQHangerHelpFragment);
        }
    }

    private void initFragment() {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        vLeft.setVisibility(View.VISIBLE);
        vRight.setVisibility(View.GONE);
        tvLeft.setTextColor(Color.parseColor("#1F96F7"));
        tvRight.setTextColor(Color.parseColor("#333333"));
        mPersonalFAQLockHelpFragment = new PersonalFAQLockHelpFragment();
        transaction.add(R.id.content, mPersonalFAQLockHelpFragment);
        transaction.commit();
    }
}
