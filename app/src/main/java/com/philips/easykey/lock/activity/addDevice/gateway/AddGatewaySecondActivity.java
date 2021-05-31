package com.philips.easykey.lock.activity.addDevice.gateway;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.zigbeelocknew.QrCodeScanActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.PermissionUtil;


public class AddGatewaySecondActivity extends BaseAddToApplicationActivity {

    ImageView back;
    LinearLayout scanGateway;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_gateway_add_second);
        back = findViewById(R.id.back);
        scanGateway = findViewById(R.id.scan_gateway);

        back.setOnClickListener(v -> finish());
        scanGateway.setOnClickListener(v -> {
            String[] strings = PermissionUtil.getInstance().checkPermission(new String[]{  Manifest.permission.CAMERA});
            if (strings.length>0){
                ToastUtils.showShort(R.string.philips_activity_deviceadd2);
                PermissionUtil.getInstance().requestPermission(new String[]{  Manifest.permission.CAMERA}, this);
            }else {
                Intent scanIntent=new Intent(this,QrCodeScanActivity.class);
                startActivityForResult(scanIntent, KeyConstants.SCANGATEWAY_REQUEST_CODE);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data!=null){
            switch (requestCode){
                case KeyConstants.SCANGATEWAY_REQUEST_CODE:
                    String result = data.getStringExtra(Constants.SCAN_QR_CODE_RESULT);
                    LogUtils.d("扫描结果是   " + result);
                    if (result.contains("SN-GW")&&result.contains("MAC-")&&result.contains(" ")){
                        String[] strs=result.split(" ");
                        String deviceSN=strs[0].replace("SN-","");
                        Intent scanSuccessIntent=new Intent(AddGatewaySecondActivity.this,AddGatewayThirdActivity.class);
                        scanSuccessIntent.putExtra("deviceSN",deviceSN);
                        LogUtils.d("设备SN是   " + deviceSN);
                        startActivity(scanSuccessIntent);
                        finish();
                    }else{
                        ToastUtils.showShort(getString(R.string.please_use_gateway_qr_code));
                    }
                    break;
            }
        }
    }
}
