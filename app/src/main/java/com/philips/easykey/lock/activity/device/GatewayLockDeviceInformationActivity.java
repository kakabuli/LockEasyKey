package com.philips.easykey.lock.activity.device;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


/**
 * Created by David
 */
public class GatewayLockDeviceInformationActivity extends BaseAddToApplicationActivity implements View.OnClickListener {
    ImageView ivBack;
    TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_lock_device_information);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivBack.setOnClickListener(this);
        tvContent.setText(R.string.device_information);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
