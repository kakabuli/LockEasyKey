package com.philips.easykey.lock.activity.addDevice.zigbeelocknew;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDeviceZigbeeLockNewFirstActivity extends BaseAddToApplicationActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.head_layout)
    RelativeLayout headLayout;
    @BindView(R.id.cancel_bind)
    Button cancelBind;
    private String gatewayId;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_cateye_suit_first);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        Intent intent=getIntent();
        gatewayId=intent.getStringExtra(KeyConstants.GATEWAY_ID);

      if (!TextUtils.isEmpty(gatewayId)){
          Intent successIntent = new Intent(this, AddDeviceZigbeeLockNewSuccessActivity.class);
          startActivity(successIntent);
          finish();

      }else{
            //绑定套装失败
          Intent successIntent = new Intent(this, AddDeviceZigbeeLockNewFailActivity.class);
          startActivity(successIntent);
          finish();
      }
    }

    @OnClick({R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

}
