package com.philips.easykey.lock.fragment.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.google.zxing.Result;
import com.king.zxing.CameraScan;
import com.king.zxing.DefaultCameraScan;
import com.king.zxing.ViewfinderView;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewFirstActivity;
import com.philips.easykey.lock.utils.dialog.MessageDialog;


/**
 * author : Jack
 * time   : 2021/4/26
 * E-mail : wengmaowei@kaadas.com
 * desc   : 扫描二维码添加设备
 */
public class PhilipsQrCodeScanFragment extends Fragment implements CameraScan.OnScanResultCallback {

    private CameraScan mCameraScan;

    public static PhilipsQrCodeScanFragment newInstance() {
        return new PhilipsQrCodeScanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.philips_fragment_qr_code_scan, container, false);

        PreviewView previewView = root.findViewById(R.id.previewView);
        ViewfinderView viewfinderView = root.findViewById(R.id.viewfinderView);
        mCameraScan = new DefaultCameraScan(this, previewView);
        mCameraScan.setOnScanResultCallback(this)
                .setVibrate(true)
                .startCamera();

        ImageView ivFlashLight = root.findViewById(R.id.ivFlashLight);
        ivFlashLight.setOnClickListener(v -> {
            if(mCameraScan != null) {
                mCameraScan.enableTorch(!mCameraScan.isTorchEnabled());
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        if(mCameraScan != null) {
            mCameraScan.enableTorch(false);
            mCameraScan.release();
        }
        super.onDestroy();
    }

    @Override
    public boolean onScanResultCallback(Result result) {
        LogUtils.d(result, result.getText());
        if(result.getText().contains("WiFi&VIDEO") || result.getText().contains("kaadas_WiFi_camera")){
            //视频WIFI锁
            Intent wifiIntent = new Intent(getContext(), WifiLockAddNewFirstActivity.class);
            String wifiModelType = "WiFi&VIDEO";
            wifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(wifiIntent);
            getActivity().finish();
        } else {
            unKnowQr();
            // 停止扫描了
            if(mCameraScan != null) {
                mCameraScan.stopCamera();
            }
            // 返回true 不销毁当前的activity
            return true;
        }
        return false;
    }

    @Override
    public void onScanResultFailure() {

    }

    private MessageDialog messageDialog;

    public void unKnowQr(){
        //信息
        messageDialog = new MessageDialog.Builder(getContext())
                .setMessage(R.string.unknow_qr)
                .create();
        messageDialog.show();

        new Handler().postDelayed(() -> {
            if(messageDialog != null){
                messageDialog.dismiss();
                getActivity().finish();
            }
        }, 3000); //延迟3秒消失
    }

}
