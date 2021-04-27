package com.philips.easykey.lock.activity.addDevice;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;

import com.blankj.utilcode.util.FragmentUtils;
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

    private FragmentContainerView mFcvAddDevice;
    private TextView mTvScanAdd, mTvAddManually;
    private View mVScanAddSelected, mVAddManuallySelected;

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.philips_activity_add_device;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {

        mFcvAddDevice = findViewById(R.id.fcvAddDevice);
        mTvScanAdd = findViewById(R.id.tvScanAdd);
        mTvAddManually = findViewById(R.id.tvAddManually);
        mVScanAddSelected = findViewById(R.id.vScanAddSelected);
        mVAddManuallySelected = findViewById(R.id.vAddManuallySelected);
        applyDebouncingClickListener(findViewById(R.id.ivBack), findViewById(R.id.ivHelp), mTvAddManually, mTvScanAdd);


        mFragments.add(PhilipsQrCodeScanFragment.newInstance());
        mFragments.add(PhilipsAddManuallyFragment.newInstance());
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
        } else if(view.getId() == R.id.tvAddManually) {
            showAddDeviceFromManual();
        } else if(view.getId() == R.id.tvScanAdd) {
            showAddDeviceFromQRCode();
        }
    }

    private void showAddDeviceFromQRCode() {
        // TODO: 2021/4/26 通过扫描二维码进入添加模式
        mTvScanAdd.setTextColor(getColor(R.color.color_333333));
        mTvAddManually.setTextColor(getColor(R.color.c999999));
        mVScanAddSelected.setVisibility(View.VISIBLE);
        mVAddManuallySelected.setVisibility(View.GONE);
        FragmentUtils.showHide(0, mFragments);
    }

    private void showAddDeviceFromManual() {
        // TODO: 2021/4/26 选择对应型号并进入对应的添加方式
        mTvAddManually.setTextColor(getColor(R.color.color_333333));
        mTvScanAdd.setTextColor(getColor(R.color.c999999));
        mVAddManuallySelected.setVisibility(View.VISIBLE);
        mVScanAddSelected.setVisibility(View.GONE);
        FragmentUtils.showHide(1, mFragments);
    }

}
