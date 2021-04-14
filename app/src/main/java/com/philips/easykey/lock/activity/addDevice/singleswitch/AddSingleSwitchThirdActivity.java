package com.philips.easykey.lock.activity.addDevice.singleswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class AddSingleSwitchThirdActivity extends BaseAddToApplicationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_single_switch_third);
    }
}
