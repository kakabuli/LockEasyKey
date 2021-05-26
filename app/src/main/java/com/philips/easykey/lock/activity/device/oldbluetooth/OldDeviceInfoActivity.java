package com.philips.easykey.lock.activity.device.oldbluetooth;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleCheckInfoActivity;
import com.philips.easykey.lock.mvp.presenter.ble.OldDeviceInfoPresenter;
import com.philips.easykey.lock.mvp.view.IOldDeviceInfoView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.ProductInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OldDeviceInfoActivity extends BaseBleCheckInfoActivity<IOldDeviceInfoView, OldDeviceInfoPresenter>
        implements IOldDeviceInfoView, View.OnClickListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_serial_number)
    TextView tvSerialNumber;
    @BindView(R.id.tv_device_model)
    TextView tvDeviceModel;
    @BindView(R.id.tv_lock_firmware_version)
    TextView tvLockFirmwareVersion;
    @BindView(R.id.rl_bluetooth_module_version)
    RelativeLayout rlBluetoothModuleVersion;
    @BindView(R.id.tv_bluetooth_module_version)
    TextView tvBluetoothModuleVersion;
    @BindView(R.id.tv_lock_software_version)
    TextView tvLockSoftwareVersion;
    @BindView(R.id.tv_device_name)
    TextView tvDeviceName;
    @BindView(R.id.rl_device_name)
    RelativeLayout rlDeviceName;
    @BindView(R.id.ble_mode)
    TextView bleMode;
    @BindView(R.id.device_info_right)
    ImageView deviceInfoRight;
    private BleLockInfo bleLockInfo;
    String deviceNickname;//设备名称
    String name;
    private String sn;
    private String version;
    private List<ProductInfo> productList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_bluetooth_more);
        ButterKnife.bind(this);

        productList = MyApplication.getInstance().getProductInfos();
        if(MyApplication.getInstance().getBleService().getBleLockInfo() != null){

            bleLockInfo = MyApplication.getInstance().getBleService().getBleLockInfo();
            deviceNickname = bleLockInfo.getServerLockInfo().getLockNickName();
            tvDeviceName.setText(deviceNickname);
            showLoading(getString(R.string.being_get_device_information));
            if (mPresenter.isAuth(bleLockInfo, true)) {
                mPresenter.getBluetoothDeviceInformation();
            }
            if (!"1".equals(bleLockInfo.getServerLockInfo().getIs_admin())){
                deviceInfoRight.setVisibility(View.GONE);
            }else {
                deviceInfoRight.setVisibility(View.VISIBLE);
            }
            ivBack.setOnClickListener(this);
            rlDeviceName.setOnClickListener(this);
            tvContent.setText(R.string.device_info);
            rlBluetoothModuleVersion.setOnClickListener(this);
        }
    }

    @Override
    protected OldDeviceInfoPresenter createPresent() {
        return new OldDeviceInfoPresenter();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_bluetooth_module_version:
                if (!"1".equals(bleLockInfo.getServerLockInfo().getIs_admin())) {  //非主用户不允许升级
                    return;
                }
                showLoading(getString(R.string.is_check_vle_version));
                sn = tvSerialNumber.getText().toString().trim();
                version = tvBluetoothModuleVersion.getText().toString().replace("V", "");
                if (TextUtils.isEmpty(sn) || TextUtils.isEmpty(version)) {
                    ToastUtils.showLong(R.string.get_device_info);
                    if (mPresenter.isAuth(bleLockInfo, true)) {
                        mPresenter.getBluetoothDeviceInformation();
                    }
                    return;
                }
                mPresenter.uploadBleSoftware(sn, version);
                break;
            case R.id.rl_device_name:
                //设备名字
                View mView = LayoutInflater.from(this).inflate(R.layout.have_edit_dialog, null);
                TextView tvTitle = mView.findViewById(R.id.tv_title);
                EditText editText = mView.findViewById(R.id.et_name);
                TextView tv_cancel = mView.findViewById(R.id.tv_left);
                TextView tv_query = mView.findViewById(R.id.tv_right);
                AlertDialog alertDialog = AlertDialogUtil.getInstance().common(this, mView);
                tvTitle.setText(getString(R.string.input_device_name));
                //获取到设备名称设置
                editText.setText(deviceNickname);
                editText.setSelection(deviceNickname.length());
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                tv_query.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name = editText.getText().toString().trim();
                        if (!StringUtil.nicknameJudge(name)) {
                            ToastUtils.showShort(R.string.nickname_verify_error);
                            return;
                        }
                        if (deviceNickname != null) {
                            if (deviceNickname.equals(name)) {
                                ToastUtils.showShort(getString(R.string.device_nick_name_no_update));
                                alertDialog.dismiss();
                                return;
                            }
                        }
                        showLoading(getString(R.string.upload_device_name));
                        if (bleLockInfo != null && bleLockInfo.getServerLockInfo() != null && bleLockInfo.getServerLockInfo().getLockName() != null) {
                            mPresenter.modifyDeviceNickname(bleLockInfo.getServerLockInfo().getLockName(), MyApplication.getInstance().getUid(), name);
                        }
                        alertDialog.dismiss();
                    }
                });
                break;
        }
    }

    @Override
    public void SoftwareRevDataSuccess(String data) {
        String strModuleHardwareVersion = "";
        String strLockHardwareVersion = "";
        if (data.contains("-")) {
            String[] split = data.split("-");
            strModuleHardwareVersion = split[0];
            strLockHardwareVersion = split[1];
        } else {
            strModuleHardwareVersion = data;
        }
        tvLockSoftwareVersion.setText(strLockHardwareVersion.trim());
        tvBluetoothModuleVersion.setText(strModuleHardwareVersion.trim());
    }

    @Override
    public void SoftwareRevDataError(Throwable throwable) {
        ToastUtils.showShort(R.string.read_device_info_fail);
    }

    @Override
    public void HardwareRevDataSuccess(String data) {
        String[] split = data.split("-");
        String strModuleHardwareVersion = split[0];
        String strLockHardwareVersion = split[1];
        tvLockFirmwareVersion.setText(strLockHardwareVersion.trim());
    }

    @Override
    public void HardwareRevDataError(Throwable throwable) {
        ToastUtils.showShort(R.string.read_device_info_fail);
    }

    @Override
    public void FirmwareRevDataSuccess(String data) {
        if (!TextUtils.isEmpty(data.trim())) {
            tvDeviceModel.setText(data.trim());
            //适配服务器上的产品型号，适配不上则显示锁本地的研发型号
            for (ProductInfo productInfo : productList) {
                if (productInfo.getDevelopmentModel().contentEquals(data.trim())) {
                    tvDeviceModel.setText(productInfo.getProductModel());
                }
            }
        }
    }

    @Override
    public void FirmwareRevDataError(Throwable throwable) {
        ToastUtils.showShort(R.string.read_device_info_fail);
    }

    @Override
    public void SerialNumberDataSuccess(String data) {
        tvSerialNumber.setText(data.trim());
    }

    @Override
    public void SerialNumberDataError(Throwable throwable) {
        ToastUtils.showShort(R.string.read_device_info_fail);
    }

    @Override
    public void ModelNumberDataSuccess(String data) {
        bleMode.setText(data.trim());
        hiddenLoading();
    }

    @Override
    public void ModelNumberDataError(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(R.string.read_device_info_fail);
    }


    @Override
    public void modifyDeviceNicknameSuccess() {
        hiddenLoading();
        deviceNickname = name;
        tvDeviceName.setText(deviceNickname);
        bleLockInfo.getServerLockInfo().setLockNickName(deviceNickname);
        ToastUtils.showLong(R.string.device_nick_name_update_success);
    }

    @Override
    public void modifyDeviceNicknameError(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void modifyDeviceNicknameFail(BaseResult baseResult) {
        hiddenLoading();
        ToastUtils.showLong(HttpUtils.httpErrorCode(this, baseResult.getCode()));
    }

    @Override
    public void onUpdateSoftFailed(Throwable throwable) {
        hiddenLoading();
        LogUtils.d(getString(R.string.update_ble_version_failed));
    }

    @Override
    public void onUpdateSoftFailedServer(BaseResult baseResult) {
        hiddenLoading();
        LogUtils.d(getString(R.string.update_ble_version_failed));
    }

    @Override
    public void onDeviceStateChange(boolean isConnected) {  //设备连接状态改变   连接成功时提示正在鉴权，连接失败时直接提示用户
        if (isConnected) {
            showLoading(getString(R.string.is_authing));
        } else {
            hiddenLoading();
            if (!isEnterOta ){
                ToastUtils.showLong(R.string.connet_failed_please_near);
            }
        }
    }

    @Override
    public void onEnterOta() {
    }

}
