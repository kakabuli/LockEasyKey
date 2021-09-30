package com.philips.easykey.lock.activity.addDevice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.fragment.device.PhilipsAddManuallyFragment;
import com.philips.easykey.lock.fragment.device.PhilipsQrCodeScanFragment;
import com.philips.easykey.lock.normal.NormalBaseActivity;

import java.util.ArrayList;

/**
 * author : Jack
 * time   : 2021/4/26
 * E-mail : wengmaowei@kaadas.com
 * desc   : 添加设备
 */
public class PhilipsAddDeviceActivity extends NormalBaseActivity {

    private TextView mTvScanAdd, mTvAddManually;
    private View mVScanAddSelected, mVAddManuallySelected;

    private final ArrayList<Fragment> mFragments = new ArrayList<>();
    private PhilipsQrCodeScanFragment mPhilipsQrCodeScanFragment;
    private PhilipsAddManuallyFragment mPhilipsAddManuallyFragment;

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.philips_activity_add_device;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {

        mTvScanAdd = findViewById(R.id.tvScanAdd);
        mTvAddManually = findViewById(R.id.tvAddManually);
        mVScanAddSelected = findViewById(R.id.vScanAddSelected);
        mVAddManuallySelected = findViewById(R.id.vAddManuallySelected);
        applyDebouncingClickListener(findViewById(R.id.ivBack), findViewById(R.id.ivHelp), mTvAddManually, mTvScanAdd);

        if(mPhilipsQrCodeScanFragment == null){
            mPhilipsQrCodeScanFragment = new PhilipsQrCodeScanFragment();
        }
        if(mPhilipsAddManuallyFragment == null){
            mPhilipsAddManuallyFragment = new PhilipsAddManuallyFragment();
        }
        mFragments.add(mPhilipsQrCodeScanFragment);
        mFragments.add(mPhilipsAddManuallyFragment);
        FragmentUtils.add(getSupportFragmentManager(), mFragments, R.id.fcvAddDevice, 0);

    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onDebouncingClick(@NonNull View view) {
        if(view.getId() == R.id.ivBack) {
            finish();
        } else if(view.getId() == R.id.ivHelp) {
            // TODO: 2021/4/26 跳转到帮助页面
            startActivity(new Intent(this,PhilipsAddDeviceHelpActivity.class));
        } else if(view.getId() == R.id.tvAddManually) {
            showAddDeviceFromManual();
        } else if(view.getId() == R.id.tvScanAdd) {
            showAddDeviceFromQRCode();
        }
    }

    private void showAddDeviceFromQRCode() {
        // TODO: 2021/4/26 通过扫描二维码进入添加模式
        mTvScanAdd.setTextColor(getColor(R.color.white));
        mTvAddManually.setTextColor(getColor(R.color.afffffff));
        mVScanAddSelected.setVisibility(View.VISIBLE);
        mVAddManuallySelected.setVisibility(View.GONE);
        FragmentUtils.showHide(0, mFragments);
        mPhilipsQrCodeScanFragment.startCamera();
    }

    private void showAddDeviceFromManual() {
        // TODO: 2021/4/26 选择对应型号并进入对应的添加方式
        mTvAddManually.setTextColor(getColor(R.color.white));
        mTvScanAdd.setTextColor(getColor(R.color.afffffff));
        mVAddManuallySelected.setVisibility(View.VISIBLE);
        mVScanAddSelected.setVisibility(View.GONE);
        FragmentUtils.showHide(1, mFragments);
        mPhilipsQrCodeScanFragment.stopCamera();
    }

}
