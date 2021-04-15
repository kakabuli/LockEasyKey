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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClothesHangerMachineAddSuccessActivity extends BaseAddToApplicationActivity {

    @BindView(R.id.back)
    ImageView back;

    private MessageDialog messageDialog;

    private String wifiModelType = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_add_success);
        ButterKnife.bind(this);

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

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.back:
                Intent data = new Intent(ClothesHangerMachineAddSuccessActivity.this, MainActivity.class);
                startActivity(data);
                finish();
                break;

        }
    }

}
