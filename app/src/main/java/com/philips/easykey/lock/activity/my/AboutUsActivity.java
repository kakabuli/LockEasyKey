package com.philips.easykey.lock.activity.my;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.SharedUtil;


/**
 * Created by David on 2019/4/9
 */
public class AboutUsActivity extends BaseAddToApplicationActivity {

    TextView tvContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        tvContent = findViewById(R.id.tv_content);

        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        findViewById(R.id.rl_customer_service_phone).setOnClickListener(v -> SharedUtil.getInstance().callPhone(this, getResources().getString(R.string.Philips_after_sales_number)));

        tvContent.setText(getString(R.string.philips_contact_us));

    }

}
