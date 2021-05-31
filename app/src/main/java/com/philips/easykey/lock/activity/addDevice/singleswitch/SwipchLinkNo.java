package com.philips.easykey.lock.activity.addDevice.singleswitch;


import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


public class SwipchLinkNo extends BaseAddToApplicationActivity implements View.OnClickListener {

    RelativeLayout btn_swipch_ok;
    ImageView back;

    private String wifiSn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipch_link_no);
        btn_swipch_ok = findViewById(R.id.btn_swipch_ok);
        back = findViewById(R.id.back);


        btn_swipch_ok.setOnClickListener(this);
        back.setOnClickListener(this);

        initData();

    }

    private void initData() {

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);

    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_swipch_ok:
                Intent intent = new Intent(SwipchLinkNo.this, SwipchLinkOne.class);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                startActivity(intent);

                break;

        }

    }
}

