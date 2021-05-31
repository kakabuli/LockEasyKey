package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.dialog.MessageDialog;

public class ClothesHangerMachineAddSuccessActivity extends BaseAddToApplicationActivity {

    ImageView back;

    private MessageDialog messageDialog;

    private String wifiModelType = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_add_success);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            Intent data = new Intent(ClothesHangerMachineAddSuccessActivity.this, MainActivity.class);
            startActivity(data);
            finish();
        });

        wifiModelType = getIntent().getStringExtra("wifiModelType") + "";
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent data = new Intent(ClothesHangerMachineAddSuccessActivity.this, MainActivity.class);
                data.putExtra(KeyConstants.WIFI_LOCK_SHOW_HOME_PAGE,1);
                startActivity(data);
                finish();
            }
        },3000);
        MyApplication.getInstance().getAllDevicesByMqtt(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent data = new Intent(ClothesHangerMachineAddSuccessActivity.this, MainActivity.class);
        startActivity(data);
        finish();
    }

}
