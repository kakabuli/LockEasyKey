package com.philips.easykey.lock.activity.device.bluetooth.card;

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
 * Created by David on 2019/4/18
 */
public class DoorCardConnectSuccessActivity extends BaseAddToApplicationActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    ImageView ivRight;
    Button btn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_card_connect_success);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        ivRight = findViewById(R.id.iv_right);
        btn = findViewById(R.id.btn);

        ivBack.setOnClickListener(this);
        tvContent.setText(R.string.add_door_card);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn:
//                Intent intent=new Intent(this,DoorCardIdentificationActivity.class);
                Intent intent = new Intent(this, AddDoorCardSuccessActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
