package com.philips.easykey.lock.fragment.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.zxing.Result;
import com.king.zxing.CameraScan;
import com.king.zxing.DefaultCameraScan;
import com.king.zxing.util.CodeUtils;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.tmall.PhilipsTmallSelectDeviceActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.PhilipsAddVideoLockActivity;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.dialog.MessageDialog;
import com.philips.easykey.lock.widget.image.GlideEngine;

import java.util.List;


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
        mCameraScan = new DefaultCameraScan(this, previewView);
        mCameraScan.setOnScanResultCallback(this).setVibrate(true);

        ImageView ivFlashLight = root.findViewById(R.id.ivFlashLight);
        ivFlashLight.setOnClickListener(v -> {
            if(mCameraScan != null) {
                mCameraScan.enableTorch(!mCameraScan.isTorchEnabled());
            }
        });
        ImageView ivAlbum = root.findViewById(R.id.ivAlbum);
        ivAlbum.setOnClickListener(v -> createAlbum());

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mCameraScan != null) {
            mCameraScan.startCamera();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mCameraScan != null) {
            mCameraScan.enableTorch(false);
            mCameraScan.release();
        }
    }

    public void startCamera(){
        if(mCameraScan != null) {
            mCameraScan.startCamera();
        }
    }

    public void stopCamera(){
        if(mCameraScan != null) {
            mCameraScan.stopCamera();
        }
    }


    @Override
    public boolean onScanResultCallback(Result result) {
        String code = result.getText();
        LogUtils.d(result, code);
        return processScanResult(code);
    }

    private boolean processScanResult(String code) {
        if(TextUtils.isEmpty(code))return false;
        if(code.contains("WiFi&VIDEO") || code.contains("PHILIPS_WiFi_camera")){
            //视频WIFI锁
            Intent wifiIntent = new Intent(getContext(), PhilipsAddVideoLockActivity.class);
            String wifiModelType = "WiFi&VIDEO";
            wifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(wifiIntent);
            if(getActivity() != null) {
                getActivity().finish();
            }
        } else if(code.contains("authUserCode")){
            Intent wifiIntent = new Intent(getContext(), PhilipsTmallSelectDeviceActivity.class);
            wifiIntent.putExtra("code", code);
            startActivity(wifiIntent);
            if(getActivity() != null) {
                getActivity().finish();
            }
        }else {
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

    private MessageDialog mMessageDialog;

    private void unKnowQr(){
        // 信息
        mMessageDialog = new MessageDialog.Builder(getContext())
                .setMessage(R.string.unknow_qr)
                .create();
        mMessageDialog.show();

        new Handler().postDelayed(() -> {
            if(mMessageDialog != null){
                mMessageDialog.dismiss();
                if (getActivity()!=null) getActivity().finish();
            }
        }, 3000); // 延迟3秒消失
    }

    private void createAlbum() {
        int language = -1;
        String spLanguage = (String) SPUtils.getProtect(KeyConstants.LANGUAGE_SET, "");
        if(!TextUtils.isEmpty(spLanguage)){
            if(!TextUtils.equals(spLanguage,"zh")){
                language = 2;
            }
        }
        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .isCamera(false)
                .setLanguage(language)
                .imageEngine(GlideEngine.createGlideEngine())// 外部传入图片加载引擎，必传项
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(4)// 每行显示个数
                .selectionMode(PictureConfig.SINGLE)
                .isPreviewImage(true)
                .isZoomAnim(true)
                .isEnableCrop(false)// 是否裁剪
                .forResult(mOnResultCallbackListener);
    }

    private final OnResultCallbackListener<LocalMedia> mOnResultCallbackListener = new OnResultCallbackListener<LocalMedia>() {
        @Override
        public void onResult(List<LocalMedia> result) {
            if(result.size() > 0){
                String path = result.get(0).getRealPath();
                if(TextUtils.isEmpty(path)) {
                    return;
                }
                LogUtils.d("bitmap path: " + path);
                String code = CodeUtils.parseCode(path);
                processScanResult(code);
            }else {
                ToastUtils.showShort(getString(R.string.no_data));
            }
        }

        @Override
        public void onCancel() {

        }
    };

}
