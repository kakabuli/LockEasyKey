package com.philips.easykey.lock.activity.addDevice.zigbee;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;


public class  AddZigbeeLockSuccessActivity extends BaseAddToApplicationActivity {

    ImageView back;
    Button buttonNext;

    private String deviceId;
    private String gatewayId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_zigbeelock_add_success);
        back = findViewById(R.id.back);
        buttonNext = findViewById(R.id.button_next);
        initData();
        
        back.setOnClickListener(v -> {
            Intent intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        });
        buttonNext.setOnClickListener(v -> {
            Intent saveIntent=new Intent(this,AddZigbeeLockSuccessSaveActivity.class);
            saveIntent.putExtra(KeyConstants.DEVICE_ID,deviceId);
            saveIntent.putExtra(KeyConstants.GATEWAY_ID,gatewayId);
            startActivity(saveIntent);
        });
        
    }

    private void initData() {
        Intent intent=getIntent();
        deviceId=intent.getStringExtra(KeyConstants.DEVICE_ID);
        gatewayId=intent.getStringExtra(KeyConstants.GATEWAY_ID);

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isCosumenBackKey();
        }
        return false;
    }

    private boolean isCosumenBackKey() {
        Intent backIntent=new Intent(this, MainActivity.class);
        startActivity(backIntent);
        return true;
    }

}
