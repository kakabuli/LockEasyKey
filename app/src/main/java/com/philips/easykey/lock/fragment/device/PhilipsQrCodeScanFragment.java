package com.philips.easykey.lock.fragment.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.philips.easykey.lock.R;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;


/**
 * author : Jack
 * time   : 2021/4/26
 * E-mail : wengmaowei@kaadas.com
 * desc   : 扫描二维码添加设备
 */
public class PhilipsQrCodeScanFragment extends Fragment implements QRCodeView.Delegate {

    private ZBarView mZBarView;

    public static PhilipsQrCodeScanFragment newInstance() {
        return new PhilipsQrCodeScanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.philips_fragment_qr_code_scan, container, false);
        mZBarView = root.findViewById(R.id.zbarview);
        mZBarView.setDelegate(this);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mZBarView != null) {
            // 打开后置摄像头开始预览，但是并未开始识别
            mZBarView.startCamera();
            // 显示扫描框，并开始识别
            mZBarView.startSpotAndShowRect();
        }
    }

    @Override
    public void onStop() {
        if(mZBarView != null) {
            mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
            mZBarView.closeFlashlight();
        }
        super.onStop();
    }

    @Override
    public void onDetach() {
        if(mZBarView != null) {
            mZBarView.onDestroy(); // 销毁二维码扫描控件
            mZBarView.closeFlashlight();
        }
        super.onDetach();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {

    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }


}
