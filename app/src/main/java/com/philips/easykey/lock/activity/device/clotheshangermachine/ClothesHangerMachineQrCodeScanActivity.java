package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.Manifest;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAdd2Activity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.clothesHangerMachineUtil.ClothesHangerMachineUtil;
import com.philips.easykey.lock.utils.dialog.MessageDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;


public class ClothesHangerMachineQrCodeScanActivity extends BaseAddToApplicationActivity implements QRCodeView.Delegate {
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.touch_light_layout)
    LinearLayout touchLightLayout;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    private ZBarView mZBarView;
    private boolean isOpenLight = false;
    int scan = 0;
    private static final int REQUEST_CODE = 101;

    private MessageDialog messageDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_scan_qrcode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        MyApplication.getInstance().addActivity(this);
        ButterKnife.bind(this);
        checkVersion();
        scan = getIntent().getIntExtra(KeyConstants.SCAN_TYPE, 0);
        mZBarView = findViewById(R.id.zbarview);
        mZBarView.setDelegate(this);
        //动态设置状态栏高度
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(titleBar.getLayoutParams());
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        titleBar.setLayoutParams(lp);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void checkVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = checkSelfPermission(Manifest.permission.CAMERA);
            LogUtils.d("权限是允许还是开启还是禁止" + i);
            if (i == -1) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    //禁止该权限
                    ToastUtils.showShort(getString(R.string.ban_camera_permission));
                    finish();
                    return;
                } else {
                    //询问该权限
                    ToastUtils.showShort(getString(R.string.inquire_camera_permission));
                    finish();
                    return;
                }
            }
        }
        //版本为22 5.1
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (!isCameraCanUse()) {
                ToastUtils.showShort(getString(R.string.ban_camera_permission));
                finish();
                return;
            }

        }

    }

    //Android6.0以下的摄像头权限处理：
    public static boolean isCameraCanUse() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            // setParameters 是针对魅族MX5 做的。MX5 通过Camera.open() 拿到的Camera
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return canUse;
    }

    @OnClick({R.id.back, R.id.touch_light_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.touch_light_layout:
//                mZBarView.openFlashlight(); // 打开闪光灯
                if (!isOpenLight){
                    isOpenLight = true;
                    mZBarView.openFlashlight(); // 打开闪光灯
                }else {
                    isOpenLight = false;
                    mZBarView.closeFlashlight(); // 打开闪光灯
                }
                break;
        }
    }

    private String result = "";
    @Override
    public void onScanQRCodeSuccess(String result) {
        this.result = result;
        LogUtils.d("shulan -->" + result);
        String[] str = result.split("_");
        if(str.length > 0){
            if(str.length >= 4){
                if(ClothesHangerMachineUtil.pairMode(str[1]).equals(str[2])){
                    showClothesMachineDialog(getString(R.string.philips_activity_clothes_hanger_machine_qrcode_scan,str[1]));
                    return;
                }
            }
        }
        showErrorDialog();

    }

    private void showErrorDialog() {
        //信息
        messageDialog = new MessageDialog.Builder(this)
                .setMessage(R.string.input_right_clothes_machine_or_scan)
                .create();
        messageDialog.show();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                if(messageDialog != null){
                    messageDialog.dismiss();
                    finish();
                }
            }
        }, 3000); //延迟3秒消失
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        LogUtils.d("打开相机出错");
        finish();
        ToastUtils.showShort(R.string.open_camera_failed);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String result2 = data.getStringExtra(KeyConstants.URL_RESULT);

                Intent intent = new Intent();
                intent.putExtra(Constants.SCAN_QR_CODE_RESULT, result2);
                setResult(RESULT_OK, intent);
                finish();
            }else {
                Intent intent = new Intent();
                intent.putExtra(Constants.SCAN_QR_CODE_RESULT, result);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void showClothesMachineDialog(String content) {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(this, content, getString(R.string.philips__no), getString(R.string.philips__yes),
                "#9A9A9A", "#1F96F7", new AlertDialogUtil.ClickListener() {
            @Override
            public void left() {
                Intent deviceAdd = new Intent(ClothesHangerMachineQrCodeScanActivity.this, DeviceAdd2Activity.class);
                startActivity(deviceAdd);
            }

            @Override
            public void right() {
                //首页过来的
                Intent intent = new Intent();
                intent.putExtra(Constants.SCAN_QR_CODE_RESULT, result);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(String toString) {

            }
        });
    }
}
