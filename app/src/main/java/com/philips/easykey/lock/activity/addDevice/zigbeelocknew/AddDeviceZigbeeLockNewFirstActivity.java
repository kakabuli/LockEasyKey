package com.philips.easykey.lock.activity.addDevice.zigbeelocknew;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;


public class AddDeviceZigbeeLockNewFirstActivity extends BaseAddToApplicationActivity {

    ImageView back;
    RelativeLayout headLayout;
    Button cancelBind;
    private String gatewayId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_cateye_suit_first);

        back = findViewById(R.id.back);
        headLayout = findViewById(R.id.head_layout);
        cancelBind = findViewById(R.id.cancel_bind);
        back.setOnClickListener(v -> finish());

        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        gatewayId=intent.getStringExtra(KeyConstants.GATEWAY_ID);

      if (!TextUtils.isEmpty(gatewayId)){
          Intent successIntent = new Intent(this, AddDeviceZigbeeLockNewSuccessActivity.class);
          startActivity(successIntent);
          finish();

      }else{
            //绑定套装失败
          Intent successIntent = new Intent(this, AddDeviceZigbeeLockNewFailActivity.class);
          startActivity(successIntent);
          finish();
      }
    }

}
