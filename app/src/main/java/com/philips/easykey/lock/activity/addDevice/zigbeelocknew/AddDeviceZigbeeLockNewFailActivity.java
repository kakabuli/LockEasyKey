package com.philips.easykey.lock.activity.addDevice.zigbeelocknew;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.blankj.utilcode.util.ToastUtils;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAdd2Activity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.PermissionUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDeviceZigbeeLockNewFailActivity extends BaseAddToApplicationActivity {


    @BindView(R.id.button_again)
    Button buttonAgain;
    @BindView(R.id.hand_bind)
    Button handBind;
    @BindView(R.id.back)
    ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_add_zigbeenewlock_fail);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.button_again, R.id.hand_bind,R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                startActivity(new Intent(this, DeviceAdd2Activity.class));
                break;
            case R.id.button_again:


                String[] strings = PermissionUtil.getInstance().checkPermission(new String[]{  Manifest.permission.CAMERA});
                if (strings.length>0){
                    ToastUtils.showShort(R.string.philips_activity_deviceadd2);
                    PermissionUtil.getInstance().requestPermission(new String[]{  Manifest.permission.CAMERA}, this);
                }else {
                    Intent intent = new Intent(this, QrCodeScanActivity.class);
                    startActivityForResult(intent, KeyConstants.SCANGATEWAYNEW_REQUEST_CODE);
                }
                //再来一次
                break;
            case R.id.hand_bind:
                //退出
                Intent outIntent = new Intent(this, DeviceAdd2Activity.class);
                startActivity(outIntent);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, DeviceAdd2Activity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case KeyConstants.SCANGATEWAYNEW_REQUEST_CODE:
                    String result = data.getStringExtra(Constants.SCAN_QR_CODE_RESULT);
                    LogUtils.d("扫描结果是   " + result);
                    if (result.contains("SN-GW") && result.contains("MAC-") && result.contains(" ")) {
                        String[] strs = result.split(" ");
                        String deviceSN = strs[0].replace("SN-", "");
                        Intent scanSuccessIntent = new Intent(AddDeviceZigbeeLockNewFailActivity.this, AddDeviceZigbeeLockNewZeroActivity.class);
                        scanSuccessIntent.putExtra("deviceSN", deviceSN);
                        LogUtils.d("设备SN是   " + deviceSN);
                        startActivity(scanSuccessIntent);
                        finish();
                    } else {
                        Intent scanSuccessIntent = new Intent(AddDeviceZigbeeLockNewFailActivity.this, AddDeviceZigbeeLockNewScanFailActivity.class);
                        startActivity(scanSuccessIntent);
                        finish();
                    }
                    break;
            }

        }
    }


}
