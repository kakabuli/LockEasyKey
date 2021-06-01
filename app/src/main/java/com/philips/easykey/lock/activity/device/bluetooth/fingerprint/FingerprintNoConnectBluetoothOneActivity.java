package com.philips.easykey.lock.activity.device.bluetooth.fingerprint;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


/**
 * Created by David on 2019/4/17
 */
public class FingerprintNoConnectBluetoothOneActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    Button btnNextStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_no_connect_bluetooth_one);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        btnNextStep = findViewById(R.id.btn_next_step);

        ivBack.setOnClickListener(this);
        tvContent.setText(R.string.add_fingerprint);
        btnNextStep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_next_step:
                Intent intent = new Intent(this, FingerprintNoConnectBluetoothTwoActivity.class);
                startActivity(intent);
                break;
        }
    }
}
