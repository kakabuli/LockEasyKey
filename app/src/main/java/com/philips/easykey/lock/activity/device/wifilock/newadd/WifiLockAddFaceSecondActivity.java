package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.password.PhilipsWiFiLockPasswordManagerActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class WifiLockAddFaceSecondActivity extends BaseAddToApplicationActivity {

    ImageView back;
//    (R.id.help)
//    ImageView help;
    TextView head;
    TextView notice;
    ImageView ivAnim;
    TextView buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_how_to_add_face_pwd_second);

        back = findViewById(R.id.back);
        head = findViewById(R.id.head);
        notice = findViewById(R.id.notice);
        ivAnim = findViewById(R.id.iv_anim);
        buttonNext = findViewById(R.id.button_next);

        back.setOnClickListener(v -> finish());
        buttonNext.setOnClickListener(v -> startActivity(new Intent(this, PhilipsWiFiLockPasswordManagerActivity.class)));
    }
}
