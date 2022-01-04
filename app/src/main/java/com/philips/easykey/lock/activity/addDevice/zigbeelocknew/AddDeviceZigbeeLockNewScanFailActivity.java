package com.philips.easykey.lock.activity.addDevice.zigbeelocknew;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAdd2Activity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.PermissionUtil;


public class AddDeviceZigbeeLockNewScanFailActivity extends BaseAddToApplicationActivity {

    ImageView back;
    Button rescan;
    Button scanCancel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qcode_no_is_kaadas);

        back = findViewById(R.id.back);
        rescan = findViewById(R.id.rescan);
        scanCancel = findViewById(R.id.scan_cancel);

        back.setOnClickListener(v -> {
            Intent backIntent=new Intent(this, DeviceAdd2Activity.class);
            startActivity(backIntent);
            finish();
        });
        scanCancel.setOnClickListener(v -> {
            Intent backIntent=new Intent(this, DeviceAdd2Activity.class);
            startActivity(backIntent);
            finish();
        });
        rescan.setOnClickListener(v -> {
            String[] strings = PermissionUtil.getInstance().checkPermission(new String[]{  Manifest.permission.CAMERA});
            if (strings.length>0){
                ToastUtils.showShort(getString(R.string.philips_activity_deviceadd2));
                PermissionUtil.getInstance().requestPermission(new String[]{  Manifest.permission.CAMERA}, this);
            }else {
                Intent rescanIntent=new Intent(this,QrCodeScanActivity.class);
                startActivityForResult(rescanIntent,KeyConstants.SCANGATEWAYNEW_REQUEST_CODE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DeviceAdd2Activity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            switch (requestCode){
                case KeyConstants.SCANGATEWAYNEW_REQUEST_CODE:
                    String result = data.getStringExtra(Constants.SCAN_QR_CODE_RESULT);
                    LogUtils.d("扫描结果是   " + result);
                    if (result.contains("SN-GW")&&result.contains("MAC-")&&result.contains(" ")){
                        String[] strs=result.split(" ");
                        String deviceSN=strs[0].replace("SN-","");
                        Intent scanSuccessIntent=new Intent(AddDeviceZigbeeLockNewScanFailActivity.this, AddDeviceZigbeeLockNewZeroActivity.class);
                        scanSuccessIntent.putExtra("deviceSN",deviceSN);
                        LogUtils.d("设备SN是   " + deviceSN);
                        startActivity(scanSuccessIntent);
                        finish();
                    }
                    break;
            }

        }
    }
}
