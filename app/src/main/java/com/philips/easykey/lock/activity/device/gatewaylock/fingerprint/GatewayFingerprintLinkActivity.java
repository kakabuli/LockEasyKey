package com.philips.easykey.lock.activity.device.gatewaylock.fingerprint;

import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


/**
 * Created by David on 2019/4/17
 */
public class GatewayFingerprintLinkActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_fingerprint_link);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);

        ivBack.setOnClickListener(this);
        tvContent.setText(R.string.add_fingerprint);
/*        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if (bluetoothConnected) {
                    intent = new Intent(GatewayFingerprintLinkActivity.this, FingerprintCollectionActivity.class);
                } else {
                    intent = new Intent(GatewayFingerprintLinkActivity.this, FingerprintNoConnectBluetoothOneActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, 3000);*/
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
