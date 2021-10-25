package com.philips.easykey.lock.activity.device.gatewaylock.stress.old;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter.GatewayLockSharePresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.GatewayLockShareView;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.SharedUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


public class GatewayLockStressShareActivity extends BaseActivity<GatewayLockShareView,
        GatewayLockSharePresenter<GatewayLockShareView>>
        implements GatewayLockShareView {

    ImageView back;
    TextView tvNumber;
    Button btnDelete;
    TextView tvShortMessage;
    TextView tvWeiXin;
    TextView tvCopy;
    TextView tvPwdType;

    private String gatewayId;
    private String deviceId;
    private String pwdValue;
    private String pwdId;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_lock_password_share);

        back = findViewById(R.id.back);
        tvNumber = findViewById(R.id.tv_number);
        btnDelete = findViewById(R.id.btn_delete);
        tvShortMessage = findViewById(R.id.tv_short_message);
        tvWeiXin = findViewById(R.id.tv_wei_xin);
        tvCopy = findViewById(R.id.tv_copy);
        tvPwdType = findViewById(R.id.tv_pwd_type);

        back.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(pwdId)){
                Intent managerIntent=new Intent(GatewayLockStressShareActivity.this, GatewayLockStressDetailActivity.class);
                SPUtils.put(KeyConstants.ADD_STRESS_PWD_ID,pwdId);
                startActivity(managerIntent);
            }
        });
        btnDelete.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(deviceId)&&!TextUtils.isEmpty(gatewayId)&&!TextUtils.isEmpty(pwdId)){
                mPresenter.shareDeleteLockPwd(gatewayId,deviceId,pwdId);
                alertDialog= AlertDialogUtil.getInstance().noButtonDialog(this,getString(R.string.delete_be_being));
            }
        });
        tvShortMessage.setOnClickListener(v -> {
            String message = String.format(getString(R.string.share_content), pwdValue,tvPwdType.getText().toString().trim(),getString(R.string.app_name));
            SharedUtil.getInstance().sendShortMessage(message, this);
        });
        tvWeiXin.setOnClickListener(v -> {
            String message = String.format(getString(R.string.share_content), pwdValue,tvPwdType.getText().toString().trim(),getString(R.string.app_name));
            if (SharedUtil.isWeixinAvilible(this)) {
                SharedUtil.getInstance().sendWeiXin(message);
            } else {
                ToastUtils.showShort(R.string.telephone_not_install_wechat);
            }
        });
        tvCopy.setOnClickListener(v -> {
            String message = String.format(getString(R.string.share_content), pwdValue,tvPwdType.getText().toString().trim(),getString(R.string.app_name));
            SharedUtil.getInstance().copyTextToSystem(this, message);
        });

        initData();

    }

    @Override
    protected GatewayLockSharePresenter<GatewayLockShareView> createPresent() {
        return new GatewayLockSharePresenter<>();
    }


    private void initData() {
        Intent intent = getIntent();
        gatewayId = intent.getStringExtra(KeyConstants.GATEWAY_ID);
        deviceId = intent.getStringExtra(KeyConstants.DEVICE_ID);
        pwdId=intent.getStringExtra(KeyConstants.PWD_ID);
        pwdValue= intent.getStringExtra(KeyConstants.PWD_VALUE);
        if (pwdValue!=null){
            String value= StringUtil.getFileAddSpace(pwdValue);
            tvNumber.setText(value);
        }
        tvPwdType.setText(getString(R.string.stress_password));
    }

    @Override
    public void shareDeletePasswordSuccess(String pwdNum) {
        //删除锁密码成功
        alertDialog.dismiss();
        Intent intent=new Intent(this, GatewayLockStressDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void shareDeletePasswordFail() {
        //删除失败
        alertDialog.dismiss();
        AlertDialogUtil.getInstance().noButtonDialog(this,getString(R.string.delete_fialed));
    }

    @Override
    public void shareDeletePasswordThrowable(Throwable throwable) {
        //删除异常
        alertDialog.dismiss();
        AlertDialogUtil.getInstance().noButtonDialog(this,getString(R.string.delete_fialed));
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isCosumenBackKey();
        }
        return false;
    }

    private boolean isCosumenBackKey() {
        if (!TextUtils.isEmpty(pwdId)){
            Intent managerIntent=new Intent(GatewayLockStressShareActivity.this, GatewayLockStressDetailActivity.class);
            SPUtils.put(KeyConstants.ADD_STRESS_PWD_ID,pwdId);
            startActivity(managerIntent);
        }
        return true;
    }
}
