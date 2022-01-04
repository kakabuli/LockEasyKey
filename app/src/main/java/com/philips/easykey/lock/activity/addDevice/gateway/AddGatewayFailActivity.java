package com.philips.easykey.lock.activity.addDevice.gateway;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAdd2Activity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

public class AddGatewayFailActivity extends BaseAddToApplicationActivity {


    ImageView back;
    Button buttonAgain;
    Button buttonUnbind;

    private String code;
    private String msg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_gateway_add_fail);
        back = findViewById(R.id.back);
        buttonAgain = findViewById(R.id.button_again);
        buttonUnbind = findViewById(R.id.button_unbind);

        back.setOnClickListener(v -> {
            Intent scanIntent=new Intent(this,AddGatewaySecondActivity.class);
            startActivity(scanIntent);
            finish();
        });
        buttonAgain.setOnClickListener(v -> {
            //重新连接
            Intent reConnection=new Intent(this,AddGatewayFirstActivity.class);
            startActivity(reConnection);
        });
        buttonUnbind.setOnClickListener(v -> {
            Intent addDeviceIntent=new Intent(this, DeviceAdd2Activity.class);
            startActivity(addDeviceIntent);
            finish();
        });

//        initView();
    }

    private void initView() {
        Intent intent=getIntent();
        code=intent.getStringExtra("code");
        msg=intent.getStringExtra("msg");
        if (!TextUtils.isEmpty(code)){
           if ("813".equals(code)){
               ToastUtils.showShort(getString(R.string.gateway_already_bind));
           } else if ("812".equals(code)){
               ToastUtils.showShort(getString(R.string.already_notify_admin_sure));
           }else{
               ToastUtils.showShort(msg);
           }

            LogUtils.d("网关绑定失败"+msg);
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DeviceAdd2Activity.class));
    }
}
