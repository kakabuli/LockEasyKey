package com.philips.easykey.lock.activity.addDevice.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class AddBluetoothPairActivity extends BaseAddToApplicationActivity {

    ImageView cancel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_bluetooth_pair);
        cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(v -> finish());

    }

    //配对结果,成功直接跳转到相应的页面，失败也跳转到不同的页面
    private void pairResult(Boolean flag) {
        if (flag) {
            //成功
            Intent sucIntent = new Intent(this, AddBluetoothSuccessActivity.class);
            startActivity(sucIntent);
        } else {
            Intent failIntent = new Intent(this, AddBluetoothPairFailActivity.class);
            startActivity(failIntent);
        }

    }

}
