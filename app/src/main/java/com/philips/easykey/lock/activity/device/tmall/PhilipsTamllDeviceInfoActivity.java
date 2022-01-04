package com.philips.easykey.lock.activity.device.tmall;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockAlbumActivity;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockAlbumDetailActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.tmall.PhilipsDelTmallDevicePresenter;
import com.philips.easykey.lock.mvp.view.tmall.IPhilipsTmallDelDeviceView;
import com.philips.easykey.lock.publiclibrary.bean.TmallDeviceListBean;
import com.philips.easykey.lock.publiclibrary.http.result.TmallQueryDeviceListResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;

import java.io.File;


public class PhilipsTamllDeviceInfoActivity extends BaseActivity<IPhilipsTmallDelDeviceView, PhilipsDelTmallDevicePresenter<IPhilipsTmallDelDeviceView>> implements IPhilipsTmallDelDeviceView{

    private ImageView ivBack;
    private TextView tvDeviceName;
    private TextView tvDeviceType;
    private TextView tvDeviceMac;
    private TextView tvDeviceNum;
    private Button btDel;
    private TmallQueryDeviceListResult.TmallQueryDeviceList tmallQueryDeviceList;

    @Override
    protected PhilipsDelTmallDevicePresenter<IPhilipsTmallDelDeviceView> createPresent() {
        return new PhilipsDelTmallDevicePresenter<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_tmall_device_info);
        tmallQueryDeviceList = (TmallQueryDeviceListResult.TmallQueryDeviceList) getIntent().getSerializableExtra(KeyConstants.TMALL_SHARE_DEVICE);
        ivBack = findViewById(R.id.back);
        tvDeviceName = findViewById(R.id.tv_device_name);
        tvDeviceType = findViewById(R.id.tv_device_type);
        tvDeviceMac = findViewById(R.id.tv_device_mac);
        tvDeviceNum = findViewById(R.id.tv_device_num);
        btDel = findViewById(R.id.bt_del);

        tvDeviceName.setText(tmallQueryDeviceList.getLockNickname());
        tvDeviceType.setText(tmallQueryDeviceList.getAligenieDeviceModel());
        tvDeviceMac.setText(tmallQueryDeviceList.getAligenieMac());
        tvDeviceNum.setText(tmallQueryDeviceList.getAligenieSN());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });
    }

    @Override
    public void onDeviceShareDelSuccess() {
          finish();
    }

    @Override
    public void onDeviceShareDelFailed() {
        delFailedDialog(getString(R.string.philips_tmall_unbinding_failed_please_try_again));
    }

    public void delFailedDialog(String content){
        AlertDialogUtil.getInstance().PhilipsSingleButtonDialog(this, content,"",
                getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void showDeleteDialog() {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(
                PhilipsTamllDeviceInfoActivity.this
                , getString(R.string.philips_tmall_is_unbinding) + "",
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#0066A1", "#FFFFFF", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        mPresenter.aligenieUserDeviceShareDel(tmallQueryDeviceList);
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }
}
