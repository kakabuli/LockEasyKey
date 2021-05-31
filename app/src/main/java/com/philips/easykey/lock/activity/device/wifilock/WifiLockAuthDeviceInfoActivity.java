package com.philips.easykey.lock.activity.device.wifilock;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockMorePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockMoreView;
import com.philips.easykey.lock.publiclibrary.bean.ProductInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.result.MultiCheckOTAResult;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class WifiLockAuthDeviceInfoActivity extends BaseActivity<IWifiLockMoreView, WifiLockMorePresenter<IWifiLockMoreView>>
        implements IWifiLockMoreView {


    ImageView back;
    TextView headTitle;
    TextView tvDeviceName;
    TextView tvDeviceModel;
    ImageView ivMessageFree;
    RelativeLayout rlMessageFree;
    TextView tvSerialNumber;
    RelativeLayout rlFaceModelFirmwareVersion;
    TextView tvFaceModelFirmwareVersion;
    TextView tvLockFirmwareVersion;
//    (R.id.tv_lock_software_version)
//    TextView tvLockSoftwareVersion;
    TextView wifiVersion;
    private WifiLockInfo wifiLockInfo;
    private String wifiSn;
    private String sWifiVersion;
    private String sLockSoftwareVersion;
    private List<ProductInfo> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_auth_device_info);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        tvDeviceName = findViewById(R.id.tv_device_name);
        tvDeviceModel = findViewById(R.id.tv_device_model);
        ivMessageFree = findViewById(R.id.iv_message_free);
        rlMessageFree = findViewById(R.id.rl_message_free);
        tvSerialNumber = findViewById(R.id.tv_serial_number);
        rlFaceModelFirmwareVersion = findViewById(R.id.rl_face_model_firmware_version);
        tvFaceModelFirmwareVersion = findViewById(R.id.tv_face_model_firmware_version);
        tvLockFirmwareVersion = findViewById(R.id.tv_lock_firmware_version);
        wifiVersion = findViewById(R.id.wifi_version);

        ivMessageFree.setOnClickListener(v -> {
            int status = wifiLockInfo.getPushSwitch() == 2 ? 1 : 2;
            showLoading(getString(R.string.is_setting));
            mPresenter.updateSwitchStatus(status, wifiLockInfo.getWifiSN());
        });
        back.setOnClickListener(v -> finish());

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        productList = MyApplication.getInstance().getProductInfos();

        if (wifiLockInfo != null) {
            sWifiVersion = wifiLockInfo.getWifiVersion();
            sLockSoftwareVersion = wifiLockInfo.getLockSoftwareVersion();
            String wifiName = wifiLockInfo.getWifiName();
            String lockNickname = wifiLockInfo.getLockNickname();
            String productModel = wifiLockInfo.getProductModel();
            tvDeviceModel.setText(TextUtils.isEmpty(productModel) ? "" : productModel.contentEquals("K13") ? getString(R.string.lan_bo_ji_ni) : productModel);
            //适配服务器上的产品型号，适配不上则显示锁本地的研发型号
            for (ProductInfo productInfo:productList) {

                try {
                    if (productInfo.getSnHead().equals(wifiSn.substring(0,3))) {
                        tvDeviceModel.setText(productInfo.getProductModel());
                    }
                } catch (Exception e) {
                    LogUtils.d("--kaadas--:" + e.getMessage());
                }
            }
            tvSerialNumber.setText(TextUtils.isEmpty(wifiLockInfo.getWifiSN()) ? "" : wifiLockInfo.getWifiSN());
//            int func = Integer.parseInt(bleLockInfo.getServerLockInfo().getFunctionSet());

            if(BleLockUtils.isSupportWiFiFaceOTA(wifiLockInfo.getFunctionSet())){
                rlFaceModelFirmwareVersion.setVisibility(View.VISIBLE);
                tvFaceModelFirmwareVersion.setText(TextUtils.isEmpty(wifiLockInfo.getFaceVersion()) ? "" : wifiLockInfo.getFaceVersion());

            }
            else {//不支持
                rlFaceModelFirmwareVersion.setVisibility(View.GONE);
            }

            tvLockFirmwareVersion.setText(TextUtils.isEmpty(wifiLockInfo.getLockFirmwareVersion()) ? "" : wifiLockInfo.getLockFirmwareVersion());
//            tvLockSoftwareVersion.setText(TextUtils.isEmpty(wifiLockInfo.getLockSoftwareVersion()) ? "" : wifiLockInfo.getLockSoftwareVersion());
            wifiVersion.setText(TextUtils.isEmpty(wifiLockInfo.getWifiVersion()) ? "" : wifiLockInfo.getWifiVersion());
            tvDeviceName.setText(TextUtils.isEmpty(lockNickname) ? "" : lockNickname);

            int pushSwitch = wifiLockInfo.getPushSwitch();
            if (pushSwitch == 2) {
                ivMessageFree.setImageResource(R.mipmap.iv_open);
            } else {
                ivMessageFree.setImageResource(R.mipmap.iv_close);
            }

        } else {
            rlMessageFree.setVisibility(View.GONE);
        }
    }

    @Override
    protected WifiLockMorePresenter<IWifiLockMoreView> createPresent() {
        return new WifiLockMorePresenter<>();
    }


    @Override
    public void onDeleteDeviceSuccess() {

    }

    @Override
    public void onDeleteDeviceFailed(Throwable throwable) {

    }

    @Override
    public void onDeleteDeviceFailedServer(BaseResult result) {

    }

    @Override
    public void modifyDeviceNicknameSuccess() {

    }

    @Override
    public void modifyDeviceNicknameError(Throwable throwable) {

    }

    @Override
    public void modifyDeviceNicknameFail(BaseResult baseResult) {

    }

    @Override
    public void onUpdatePushStatusSuccess(int status) {
        hiddenLoading();
        wifiLockInfo.setPushSwitch(status);
        if (status == 2) {
            ivMessageFree.setImageResource(R.mipmap.iv_open);
        } else {
            ivMessageFree.setImageResource(R.mipmap.iv_close);
        }
    }

    @Override
    public void onUpdatePushStatusFailed(BaseResult result) {
        hiddenLoading();
        ToastUtils.showLong(R.string.set_failed);
    }

    @Override
    public void onUpdatePushStatusThrowable(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showLong(R.string.set_failed);
    }

    @Override
    public void onWifiLockActionUpdate() {

    }

    @Override
    public void noNeedUpdate() {

    }

    @Override
    public void snError() {

    }

    @Override
    public void dataError() {

    }

    @Override
    public void needUpdate(CheckOTAResult.UpdateFileInfo appInfo, String SN, int type) {

    }

    @Override
    public void needMultiUpdate(List<MultiCheckOTAResult.UpgradeTask> upgradeTasks) {

    }

    @Override
    public void readInfoFailed(Throwable throwable) {

    }

    @Override
    public void unknowError(String errorCode) {

    }

    @Override
    public void uploadSuccess(int type) {

    }

    @Override
    public void uploadFailed() {

    }
}
