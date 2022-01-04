package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
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
import com.philips.easykey.lock.publiclibrary.bean.WifiLockOperationRecord;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StatusBarUtils;

import java.util.List;


public class PhilipsWifiVideoLockDetailActivity extends BaseActivity<IPhilipsWifiVideoLockDetailView, PhilipsWifiVideoLockDetailPresenter<IPhilipsWifiVideoLockDetailView>>
        implements IPhilipsWifiVideoLockDetailView {

    ImageView back;
    TextView mTvDeviceName;
    ImageView mIvDetailSetting;
    ImageView mIvWifi;
    ImageView mIvPower;
    TextView mTvRightMode;
    ImageView mIvVideo;
    TextView mTvLastRecord;
    RelativeLayout mRlDetailRecord;
    RelativeLayout mRlDetailAlbum;
    RelativeLayout mRlDetailPassword;
    RelativeLayout mRlDetailShare;
    RelativeLayout mRlDetailShareSetting;
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

        back = findViewById(R.id.back);
        mTvDeviceName = findViewById(R.id.tv_device_name);
        mIvDetailSetting = findViewById(R.id.iv_detail_setting);
        mIvWifi = findViewById(R.id.ivWifi);
        mIvPower = findViewById(R.id.ivPower);
        mTvRightMode = findViewById(R.id.tv_right_mode);
        mIvVideo = findViewById(R.id.ivVideo);
        mTvLastRecord = findViewById(R.id.tvLastRecord);
        mRlDetailRecord = findViewById(R.id.rl_detail_record);
        mRlDetailAlbum = findViewById(R.id.rl_detail_album);
        mRlDetailPassword = findViewById(R.id.rl_detail_password);
        mRlDetailShare = findViewById(R.id.rl_detail_share);
        mRlDetailShareSetting = findViewById(R.id.rl_detail_share_setting);
        mIvDetailDelete = findViewById(R.id.iv_detail_delete);

        back.setOnClickListener(v -> finish());
        mRlDetailRecord.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiLockRecordActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiLockInfo.getWifiSN());
            startActivity(intent);
        });
        mTvLastRecord.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiLockRecordActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiLockInfo.getWifiSN());
            startActivity(intent);
        });
        mRlDetailAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiVideoLockAlbumActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            startActivity(intent);
        });
        mRlDetailPassword.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiVideoLockPasswordTypeActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            startActivity(intent);
        });
        mRlDetailShare.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiLockFamilyManagerActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            startActivity(intent);
        });
        mRlDetailShareSetting.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhilipsWifiVideoLockDeviceInfoActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            startActivity(intent);
        });
        mIvDetailSetting.setOnClickListener(v -> {
            Intent intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiVideoLockMoreActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
            startActivityForResult(intent, TO_MORE_REQUEST_CODE);
        });
        mIvVideo.setOnClickListener(v -> {
            try {
                if(wifiLockInfo.getPowerSave() == 0){
                    Intent intent = new Intent(PhilipsWifiVideoLockDetailActivity.this, PhilipsWifiVideoLockCallingActivity.class);
                    intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_CALLING,0);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    startActivity(intent);

                }else{
                    powerStatusDialog();
                }
            }catch (Exception e){

            }
        });
        mIvDetailDelete.setOnClickListener(v -> {
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
        });

        StatusBarUtils.setWindowStatusBarColor(this,R.color.colorPrimary);
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

            if(!wifiLockInfo.getLockNickname().isEmpty()){
                if(wifiLockInfo.getLockNickname().length() > 8){
                    mTvDeviceName.setText(wifiLockInfo.getLockNickname().substring(0,8) + "...");
                }else {
                    mTvDeviceName.setText(wifiLockInfo.getLockNickname());
                }
            }else {
                mTvDeviceName.setText(wifiSn);
            }

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
            initLockMode();
            remainingCapacity(wifiLockInfo.getPower());
            initOperationRecord();
        }
    }

    private void initOperationRecord() {
        String localRecord = (String) SPUtils.get(KeyConstants.WIFI_LOCK_OPERATION_RECORD + wifiSn,"");
        List<WifiLockOperationRecord> records = new Gson().fromJson(localRecord, new TypeToken<List<WifiLockOperationRecord>>() {
        }.getType());
        if(records == null) return;
        if(records.size() <= 0) return;
        if(records.size() == 1){
            BleUtil.setTextViewOperationRecordByType(null,mTvLastRecord,records.get(0));
            mTvLastRecord.setText(DateUtils.secondToDate2(records.get(0).getCreateTime())
                    + " " + mTvLastRecord.getText().toString().trim());
            return;
        }

        long[] createTime = new long[2];
        createTime[0] = records.get(0).getCreateTime();
        createTime[1] = 0;
        for(int i = 0;i < records.size();i++){
            if(createTime[0] <= records.get(i).getCreateTime()){
                createTime[0] = records.get(i).getCreateTime();
                createTime[1] = i;
                continue;
            }
        }



        BleUtil.setTextViewOperationRecordByType(null,mTvLastRecord,records.get((int) createTime[1]));
        if(!TextUtils.isEmpty(mTvLastRecord.getText())){
            mTvLastRecord.setText(DateUtils.secondToDate2(records.get((int) createTime[1]).getCreateTime())
                    + " " + mTvLastRecord.getText().toString().trim());
        }
    }

    /**
     * 剩余电量
     * @param power
     */
    private void remainingCapacity(int power) {
        if(power <= 20){
            mIvPower.setImageResource(R.drawable.philips_home_icon_battery_low);
        }else if(power > 20 && power <= 60){
            mIvPower.setImageResource(R.drawable.philips_home_icon_battery_low2);
        }else if(power > 60 && power <= 90){
            mIvPower.setImageResource(R.drawable.philips_home_icon_battery_low1);
        }else {
            mIvPower.setImageResource(R.drawable.philips_home_icon_battery_full);
        }
    }

    /**
     *  门锁模式
     */
    private void initLockMode() {
        if(wifiLockInfo == null) return;

        int safeMode = wifiLockInfo.getSafeMode();  //安全模式
        int operatingMode = wifiLockInfo.getOperatingMode(); //反锁模式
        int defences = wifiLockInfo.getDefences();  //布防模式
        int openStatus = wifiLockInfo.getOpenStatus();
        int faceStatus = wifiLockInfo.getFaceStatus();  //面容识别已关闭
        int powerSave = wifiLockInfo.getPowerSave();   //已启动节能模式
        //安全>布防>节能>正常模式>反锁

        if (safeMode == 1) {//安全模式
            mTvRightMode.setText(R.string.safe_mode);
            return;
        }

        if (defences == 1) {//布防模式
            mTvRightMode.setText(R.string.safe_protection);
            return;
        }

        if (powerSave == 1) {//已启动节能模式
            mTvRightMode.setText(R.string.power_save_mode);
            return;
        }

        mTvRightMode.setText(R.string.real_time_video_setting_normal);

        if(operatingMode == 1){
            mTvRightMode.setText(R.string.philips_fragment_wifi_video_anti_lock_mode);
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
        AlertDialogUtil.getInstance().noEditTwoButtonTwoContentDialog(this, getString(R.string.dialog_wifi_video_keep_alive_close), getString(R.string.dialog_wifi_video_doorbell_outside_door),
                null, "", getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        finish();
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
        ToastUtils.showShort(getString(R.string.delete_success));
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