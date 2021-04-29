package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.PhilipsWifiLockRecordActivity;
import com.philips.easykey.lock.activity.device.wifilock.family.PhilipsWifiLockFamilyManagerActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.StatusBarUtils;


public class PhilipsWifiVideoLockDetailActivity extends BaseAddToApplicationActivity {

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

    private String wifiSn = "";
    private WifiLockInfo wifiLockInfo;


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

    private void initData() {
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        if (wifiLockInfo != null){
            mTvDeviceName.setText(wifiLockInfo.getLockNickname().isEmpty() ? wifiSn : wifiLockInfo.getLockNickname());

            if(wifiLockInfo.getIsAdmin() == 1){
                mRlDetailPassword.setVisibility(View.VISIBLE);
                mRlDetailShareSetting.setVisibility(View.GONE);
                mRlDetailShare.setVisibility(View.VISIBLE);
                mRlDetailAlbum.setVisibility(View.VISIBLE);
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
            R.id.ivVideo})
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
}