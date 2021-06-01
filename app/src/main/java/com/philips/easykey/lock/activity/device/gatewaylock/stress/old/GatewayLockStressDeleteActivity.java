package com.philips.easykey.lock.activity.device.gatewaylock.stress.old;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter.GatewayLockDeleteStressPasswordPresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.IGatewayLockDeleteStressPasswordView;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.ToastUtils;


public class GatewayLockStressDeleteActivity extends BaseActivity<IGatewayLockDeleteStressPasswordView,
        GatewayLockDeleteStressPasswordPresenter<IGatewayLockDeleteStressPasswordView>>
        implements IGatewayLockDeleteStressPasswordView{

    ImageView back;
    TextView headTitle;
    TextView tvNumber;
    Button btnDelete;
    private String gatewayId;
    private String deviceId;
    private String lockNumber;
    private Context context;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gateway_lock_delete_passwrod);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        tvNumber = findViewById(R.id.tv_number);
        btnDelete = findViewById(R.id.btn_delete);

        back.setOnClickListener(v -> finish());
        btnDelete.setOnClickListener(v -> {
            AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(context, getString(R.string.sure_delete_password), getString(R.string.philips_cancel), getString(R.string.delete), "#333333","#FF3B30", new AlertDialogUtil.ClickListener() {
                @Override
                public void left() {
                }
                @Override
                public void right() {
                    if (!TextUtils.isEmpty(gatewayId)&&!TextUtils.isEmpty(deviceId)&&!TextUtils.isEmpty(lockNumber)){
                        mPresenter.gatewayLockDeletePwd(gatewayId,deviceId,lockNumber);
                        alertDialog=AlertDialogUtil.getInstance().noButtonDialog(context,getString(R.string.delete_be_being));
                        alertDialog.setCancelable(false);
                    }
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(String toString) {

                }
            });
        });

        initView();
        context=this;
    }

    @Override
    protected GatewayLockDeleteStressPasswordPresenter<IGatewayLockDeleteStressPasswordView> createPresent() {
        return new GatewayLockDeleteStressPasswordPresenter<>();
    }


    private void initView() {
        tvNumber.setText(getString(R.string.stress_password));
        Intent intent=getIntent();
        lockNumber=intent.getStringExtra(KeyConstants.LOCK_PWD_NUMBER);
        gatewayId=intent.getStringExtra(KeyConstants.GATEWAY_ID);
        deviceId=intent.getStringExtra(KeyConstants.DEVICE_ID);

    }


    @Override
    public void deleteLockPwdSuccess() {
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
        //删除成功
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra(KeyConstants.LOCK_PWD_NUMBER, lockNumber);
        //设置返回数据
        this.setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    public void deleteLockPwdFail() {
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
        //删除失败
        AlertDialogUtil.getInstance().noButtonDialog(context,getString(R.string.delete_fialed));
    }

    @Override
    public void delteLockPwdThrowable(Throwable throwable) {
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
        //删除异常
        ToastUtils.showShort(getString(R.string.delete_fialed));

    }
}

