package com.philips.easykey.lock.activity.device.clotheshangermachine;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class ClothesHangerMachineAddThirdFailedActivity extends BaseAddToApplicationActivity {

    ImageView back;
    TextView button_next;

    private String wifiModelType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clothes_hanger_machine_add_third_failed);

        back = findViewById(R.id.back);
        button_next = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        button_next.setOnClickListener(v -> {
            Intent clothesMachineIntent = new Intent(this, ClothesHangerMachineAddFirstActivity.class);
            clothesMachineIntent.putExtra("wifiModelType",wifiModelType);
            startActivity(clothesMachineIntent);
            finish();
        });

        wifiModelType = getIntent().getStringExtra("wifiModelType");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
