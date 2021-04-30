package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FragmentUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.normal.NormalBaseActivity;

import java.util.ArrayList;

/**
 * author : Jack
 * time   : 2021/4/30
 * E-mail : wengmaowei@kaadas.com
 * desc   : 添加设备配网流程
 */
public class PhilipsAddVideoLockActivity extends NormalBaseActivity {

    private View mVTask1, mVTask2, mVTask3, mVTask4, mVTask5;
    private View mVTask1N2Line, mVTask2N3Line, mVTask3N4Line, mVTask4N5Line;

    private final ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.philips_activity_add_video_lock;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        initTaskShowUI();
        mFragments.add(PhilipsAddVideoLockTask1Fragment.getInstance());
        FragmentUtils.add(getSupportFragmentManager(), mFragments, R.id.fcvAddVideoLock, 0);
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onDebouncingClick(@NonNull View view) {

    }

    private void initTaskShowUI() {
        mVTask1 = findViewById(R.id.vTask1);
        mVTask2 = findViewById(R.id.vTask2);
        mVTask3 = findViewById(R.id.vTask3);
        mVTask4 = findViewById(R.id.vTask4);
        mVTask5 = findViewById(R.id.vTask5);
        mVTask1N2Line = findViewById(R.id.vTask1N2Line);
        mVTask2N3Line = findViewById(R.id.vTask2N3Line);
        mVTask3N4Line = findViewById(R.id.vTask3N4Line);
        mVTask4N5Line = findViewById(R.id.vTask4N5Line);
    }



}
