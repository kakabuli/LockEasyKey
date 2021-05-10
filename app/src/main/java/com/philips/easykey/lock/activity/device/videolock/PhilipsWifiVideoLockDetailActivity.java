package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.activity.device.wifilock.PhilipsWifiLockRecordActivity;
import com.philips.easykey.lock.activity.device.wifilock.family.PhilipsWifiLockFamilyManagerActivity;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.bean.WifiLockFunctionBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.PhilipsWifiVideoLockDetailPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IPhilipsWifiVideoLockDetailView;
import com.philips.easykey.lock.publiclibrary.bean.WiFiLockPassword;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StatusBarUtils;

import java.util.List;


public class PhilipsWifiVideoLockDetailActivity extends BaseActivity<IPhilipsWifiVideoLockDetailView, PhilipsWifiVideoLockDetailPresenter<IPhilipsWifiVideoLockDetailView>>
        implements IPhilipsWifiVideoLockDetailView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.tv_device_name)
    TextView mTvDeviceName;
    @BindView(R.id.iv_detail_setting)
    ImageView mIvDetailSetting;
    @BindView(R.id.ivWifi)
    ImageView mIvWifi;
    @BindView(R.id.ivPower)
    ImageView mIvPower;
    @BindView(R.id.tv_right_mode)
    TextView mTvRightMode;
    @BindView(R.id.ivVideo)
    ImageView mIvVideo;
    @BindView(R.id.tvLastRecord)
    TextView mTvLastRecord;
    @BindView(R.id.rl_detail_record)
    RelativeLayout mRlDetailReocrd;
    @BindView(R.id.rl_detail_album)
    RelativeLayout mRlDetailAlbum;
    @BindView(R.id.rl_detail_password)
    RelativeLayout mRlDetailPassword;
    @BindView(R.id.rl_detail_share)
    RelativeLayout mRlDetailShare;
    @BindView(R.id.rl_detail_share_setting)
    RelativeLayout mRlDetailShareSetting;
    @BindView(R.id.iv_detail_delete)
    ImageView mIvDetailDelete;

    private String wifiSn = "";
    private WifiLockInfo wifiLockInfo;
    private boolean isWifiVideoLockType = false;
    private WiFiLockPassword wiFiLockPassword;
    private List<WifiLockFunctionBean> supportFunctions;
    private List<WifiLockShareResult.WifiLockShareUser> shareUsers;


    private static final int TO_MORE_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_detail);
        ButterKnife.bind(this);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected PhilipsWifiVideoLockDetailPresenter<IPhilipsWifiVideoLockDetailView> createPresent() {
        return new PhilipsWifiVideoLockDetailPresenter<>();
    }

    private void initData() {
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        if (wifiLockInfo != null){
            if(MyApplication.getInstance().getWifiVideoLockTypeBySn(wifiSn) == HomeShowBean.TYPE_WIFI_VIDEO_LOCK){
                isWifiVideoLockType = true;
            }

            String localPasswordCache = (String) SPUtils.get(KeyConstants.WIFI_LOCK_PASSWORD_LIST + wifiSn, "");
            if (!TextUtils.isEmpty(localPasswordCache)) {
                wiFiLockPassword = new Gson().fromJson(localPasswordCache, WiFiLockPassword.class);
            }
            String localShareUsers = (String) SPUtils.get(KeyConstants.WIFI_LOCK_SHARE_USER_LIST + wifiSn, "");
            if (!TextUtils.isEmpty(localShareUsers)) {
                shareUsers = new Gson().fromJson(localShareUsers, new TypeToken<List<WifiLockShareResult.WifiLockShareUser>>() {
                }.getType());
                LogUtils.d("本地的分享用户为  shareUsers  " + (shareUsers == null ? 0 : shareUsers.size()));
            }
            if(supportFunctions == null){
                String functionSet = wifiLockInfo.getFunctionSet(); //锁功能集
                int func = 0x64;
                try {
                    if(!functionSet.isEmpty()){

                        func = Integer.parseInt(functionSet);
                    }
                } catch (Exception e) {
                    func = 0x64;
                }
                supportFunctions = BleLockUtils.getWifiLockSupportFunction(func);
            }
            initPassword();
            mPresenter.getPasswordList(wifiSn);
            mPresenter.queryUserList(wifiSn);

            mTvDeviceName.setText(wifiLockInfo.getLockNickname().isEmpty() ? wifiSn : wifiLockInfo.getLockNickname());

            if(wifiLockInfo.getIsAdmin() == 1){
                mRlDetailPassword.setVisibility(View.VISIBLE);
                mRlDetailShareSetting.setVisibility(View.GONE);
                mRlDetailShare.setVisibility(View.VISIBLE);
                mRlDetailAlbum.setVisibility(View.VISIBLE);
                mIvDetailDelete.setVisibility(View.GONE);
            }else{
                mRlDetailPassword.setVisibility(View.GONE);
                mRlDetailShareSetting.setVisibility(View.VISIBLE);
                mRlDetailShare.setVisibility(View.GONE);
                mRlDetailAlbum.setVisibility(View.GONE);
                mIvDetailSetting.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.back,R.id.rl_detail_share_setting,R.id.rl_detail_share,R.id.rl_detail_password,
            R.id.rl_detail_album,R.id.tv_right_mode,R.id.rl_detail_record,R.id.iv_detail_setting,
            R.id.ivVideo,R.id.iv_detail_delete})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.rl_detail_record:
                intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiLockRecordActivity.class);
                intent.putExtra(KeyConstants.WIFI_SN, wifiLockInfo.getWifiSN());
                startActivity(intent);
                break;
            case R.id.tv_right_mode:
                break;
            case R.id.rl_detail_album:
                intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiVideoLockAlbumActivity.class);
                intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
                startActivity(intent);
                break;
            case R.id.rl_detail_password:
                intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiVideoLockPasswordTypeActivity.class);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                startActivity(intent);
                break;
            case R.id.rl_detail_share:
                intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiLockFamilyManagerActivity.class);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                startActivity(intent);
                break;
            case R.id.rl_detail_share_setting:
                intent = new Intent(this, PhilipsWifiVideoLockDeviceInfoActivity.class);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                startActivity(intent);
                break;
            case R.id.iv_detail_setting:
                intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiVideoLockMoreActivity.class);
                intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                startActivityForResult(intent, TO_MORE_REQUEST_CODE);
                break;
            case R.id.ivVideo:
                try {
                    if(wifiLockInfo.getPowerSave() == 0){
                        intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiVideoLockCallingActivity.class);
                        intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_CALLING,0);
                        intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                        startActivity(intent);

                    }else{
                        powerStatusDialog();
                    }
                }catch (Exception e){

                }
                break;
            case R.id.iv_detail_delete:
                AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(this,getString(R.string.device_delete_dialog_head),
                        getString(R.string.philips_cancel), getString(R.string.query),"#0066A1", "#FFFFFF",new AlertDialogUtil.ClickListener() {
                            @Override
                            public void left() {

                            }

                            @Override
                            public void right() {
                                showLoading(getString(R.string.is_deleting));
                                if(isWifiVideoLockType){
                                    mPresenter.deleteVideoDevice(wifiLockInfo.getWifiSN());
                                }else{
                                    mPresenter.deleteDevice(wifiLockInfo.getWifiSN());
                                }

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }

                            @Override
                            public void afterTextChanged(String toString) {
                            }
                        });
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_MORE_REQUEST_CODE && resultCode == RESULT_OK) {
            String newName = data.getStringExtra(KeyConstants.WIFI_LOCK_NEW_NAME);
            mTvDeviceName.setText(newName + "");
        }
    }

    private void initPassword() {
        if (wiFiLockPassword != null) {
            for (WifiLockFunctionBean wifiLockFunctionBean : supportFunctions) {
                switch (wifiLockFunctionBean.getType()) {
                    case BleLockUtils.TYPE_PASSWORD:
                        List<WiFiLockPassword.PwdListBean> pwdList = wiFiLockPassword.getPwdList();
                        wifiLockFunctionBean.setNumber(pwdList == null ? 0 : pwdList.size());
                        break;
                    case BleLockUtils.TYPE_FINGER:
                        List<WiFiLockPassword.FingerprintListBean> fingerprintList = wiFiLockPassword.getFingerprintList();
                        wifiLockFunctionBean.setNumber(fingerprintList == null ? 0 : fingerprintList.size());
                        break;
                    case BleLockUtils.TYPE_CARD:
                        List<WiFiLockPassword.CardListBean> cardList = wiFiLockPassword.getCardList();
                        wifiLockFunctionBean.setNumber(cardList == null ? 0 : cardList.size());
                        break;
                    case BleLockUtils.TYPE_FACE_PASSWORD:
                        List<WiFiLockPassword.FaceListBean> faceList = wiFiLockPassword.getFaceList();
                        wifiLockFunctionBean.setNumber(faceList == null ? 0 : faceList.size());
                        break;
                }
            }
        } else {
            for (WifiLockFunctionBean wifiLockFunctionBean : supportFunctions) {
                switch (wifiLockFunctionBean.getType()) {
                    case BleLockUtils.TYPE_PASSWORD:
                        wifiLockFunctionBean.setNumber(0);
                        break;
                    case BleLockUtils.TYPE_FINGER:
                        wifiLockFunctionBean.setNumber(0);
                        break;
                    case BleLockUtils.TYPE_CARD:
                        wifiLockFunctionBean.setNumber(0);
                        break;
                    case BleLockUtils.TYPE_FACE_PASSWORD:
                        wifiLockFunctionBean.setNumber(0);
                        break;
                }
            }
        }
        for (WifiLockFunctionBean wifiLockFunctionBean : supportFunctions) {
            if (wifiLockFunctionBean.getType() == BleLockUtils.TYPE_SHARE) {
                if (shareUsers != null) {
                    wifiLockFunctionBean.setNumber(shareUsers.size());
                } else {
                    wifiLockFunctionBean.setNumber(0);
                }
            }
        }
    }


    public void powerStatusDialog(){
        AlertDialogUtil.getInstance().noEditSingleButtonDialog(this, getString(R.string.set_failed), "\n"+ getString(R.string.dialog_wifi_video_power_status) +"\n",
                getString(R.string.confirm), new AlertDialogUtil.ClickListener() {
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

    @Override
    public void onGetPasswordSuccess(WiFiLockPassword wiFiLockPassword) {
        this.wiFiLockPassword = wiFiLockPassword;
        initPassword();
    }

    @Override
    public void onGetPasswordFailedServer(BaseResult baseResult) {

    }

    @Override
    public void onGetPasswordFailed(Throwable throwable) {

    }

    @Override
    public void querySuccess(List<WifiLockShareResult.WifiLockShareUser> users) {
        shareUsers = users;
        initPassword();
    }

    @Override
    public void queryFailedServer(BaseResult result) {

    }

    @Override
    public void queryFailed(Throwable throwable) {

    }

    @Override
    public void onDeleteDeviceSuccess() {
        ToastUtils.showShort(R.string.delete_success);
        hiddenLoading();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeleteDeviceFailed(Throwable throwable) {
        LogUtils.d("删除失败   " + throwable.getMessage());
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
        hiddenLoading();
    }

    @Override
    public void onDeleteDeviceFailedServer(BaseResult result) {
        LogUtils.d("删除失败   " + result.toString());
        String httpErrorCode = HttpUtils.httpErrorCode(this, result.getCode());
        ToastUtils.showLong(httpErrorCode);
        hiddenLoading();
    }
}