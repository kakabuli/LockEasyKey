package com.philips.easykey.lock.activity.addDevice.pir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

public class AddPirThirdActivity extends BaseAddToApplicationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pir_third);
    }
}
