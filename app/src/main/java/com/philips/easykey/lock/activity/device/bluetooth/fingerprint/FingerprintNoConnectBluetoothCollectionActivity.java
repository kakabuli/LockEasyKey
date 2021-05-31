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
public class FingerprintNoConnectBluetoothCollectionActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    Button btnFinish;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fingerprint_no_connect_bluetooth_collection);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        btnFinish = findViewById(R.id.btn_finish);

        ivBack.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        tvContent.setText(R.string.add_fingerprint);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_finish:
                Intent intent = new Intent(this, FingerprintManagerActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
