package com.philips.easykey.lock.activity.addDevice.bluetooth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAddHelpActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.deviceaddpresenter.BindBlePresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.IBindBleView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.OfflinePasswordFactorManager;
import com.blankj.utilcode.util.ToastUtils;


public class AddBluetoothThirdActivity extends BaseActivity<IBindBleView, BindBlePresenter<IBindBleView>> implements IBindBleView {

    ImageView back;
    Button alreadyPairNetwork;
    ImageView help;
    TextView tvNotice;
    private boolean isBind;
    private String password1;
    private String deviceName;
    private int version;
    private boolean bindSuccess = false;
    private String sn;
    private String mac;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_bluetooth_third);

        Intent intent = getIntent();
        password1 = intent.getStringExtra(KeyConstants.PASSWORD1);
        isBind = intent.getBooleanExtra(KeyConstants.IS_BIND, true);
        version = intent.getIntExtra(KeyConstants.BLE_VERSION, 0);
        sn = intent.getStringExtra(KeyConstants.BLE_DEVICE_SN);
        back = findViewById(R.id.back);
        alreadyPairNetwork = findViewById(R.id.already_pair_network);
        help = findViewById(R.id.help);
        tvNotice = findViewById(R.id.tv_notice);

        back.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra(KeyConstants.IS_BIND, mPresenter.isBind());
            setResult(RESULT_OK, result);
            finish();
        });
        help.setOnClickListener(v -> {
            Intent i = new Intent(this, DeviceAddHelpActivity.class);
            startActivity(i);
        });

        mac = intent.getStringExtra(KeyConstants.BLE_MAC);
        deviceName = intent.getStringExtra( KeyConstants.DEVICE_NAME);

        //pwd1设置给Presenter使用
        mPresenter.setPwd1(password1, isBind, version, sn,mac,deviceName);
        mPresenter.listenConnectState();


        LogUtils.d("是否绑定流程   " + isBind);

        initView();
    }

    @Override
    protected BindBlePresenter<IBindBleView> createPresent() {
        return new BindBlePresenter<>();
    }


    private void initView() {
        if (isBind) {
            tvNotice.setText(R.string.device_add_third_content_in_net);
        } else {
            tvNotice.setText(R.string.device_add_third_content_exit_net);
        }
        alreadyPairNetwork.setClickable(false);
        alreadyPairNetwork.setTextColor(Color.parseColor("#7f7f7f"));
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra(KeyConstants.IS_BIND, mPresenter.isBind());
        setResult(RESULT_OK, result);
        super.onBackPressed();
    }

    @Override
    public void onBindSuccess(String deviceName) {
        bindSuccess = true;
        this.deviceName = deviceName;
        LogUtils.d("绑定成功   " + deviceName);
//        alreadyPairNetwork.setTextColor(Color.parseColor("#1F96F7"));
//        alreadyPairNetwork.setClickable(true);

        Intent pairIntent = new Intent(this, AddBluetoothSuccessActivity.class);
        pairIntent.putExtra(KeyConstants.DEVICE_NAME, deviceName);
        startActivity(pairIntent);

    }

    @Override
    public void onBindFailed(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void onBindFailedServer(BaseResult result) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
    }


    @Override
    public void onReceiveInNetInfo() {

    }

    @Override
    public void onReceiveUnbind() {

    }

    @Override
    public void onUnbindSuccess() {
        tvNotice.setText(R.string.device_add_third_content_in_net);
        ToastUtils.showShort(getString(R.string.unbind_success_innet));
    }

    @Override
    public void onUnbindFailed(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void onUnbindFailedServer(BaseResult result) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
    }

    @Override
    public void readLockTypeFailed(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.bind_failed));
    }

    @Override
    public void readLockTypeSucces() {

    }

    @Override
    public void onDeviceStateChange(boolean isConnected) {  //设备连接状态改变   连接成功时提示正在鉴权，连接失败时直接提示用户
        if (bindSuccess) {
            return;
        }
        if (!isConnected) {
            //Context context, String title, String content, String query, ClickListener clickListener
            AlertDialogUtil.getInstance().noEditSingleCanNotDismissButtonDialog(AddBluetoothThirdActivity.this, getString(R.string.hint), getString(R.string.ble_disconnected_please_retry), getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                @Override
                public void left() {
                    finish();
                    startActivity(new Intent(AddBluetoothThirdActivity.this, AddBluetoothSearchActivity.class));
                }

                @Override
                public void right() {
                    finish();
                    startActivity(new Intent(AddBluetoothThirdActivity.this, AddBluetoothSearchActivity.class));
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

    @Override
    public void unknownFunctionSet(int functionSet) {
        String content = String.format(getString(R.string.unknow_function_set), "" + functionSet);
        ToastUtils.showLong(content);
        finish();
        startActivity(new Intent(AddBluetoothThirdActivity.this, AddBluetoothSearchActivity.class));
    }

    @Override
    public void readFunctionSetSuccess(int functionSet) {

    }

    @Override
    public void readFunctionSetFailed(Throwable throwable) {
        ToastUtils.showLong(R.string.read_lock_info_failed);
        finish();
        startActivity(new Intent(AddBluetoothThirdActivity.this, AddBluetoothSearchActivity.class));
    }

    @Override
    public void onlistenerLastNum(int lastNum) {

    }

    @Override
    public void onlistenerPasswordFactor(byte[] originalData,int pswLen,int index) {

    }

    @Override
    public void onDecodeResult(int index, OfflinePasswordFactorManager.OfflinePasswordFactorResult result) {

    }


}
