package com.philips.easykey.lock.activity.device.videolock;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockCallingPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVideoCallingView;
import com.philips.easykey.lock.publiclibrary.bean.ProductInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.WifiLockOperationBean;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PConnectError;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PConnectJsonError;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BitmapUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.WifiUtils;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;
import com.philips.easykey.lock.widget.avindicator.AVSpeakerView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xm.sdk.struct.stream.AVStreamHeader;
import com.xmitech.sdk.MP4Info;
import com.yuv.display.MyBitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.philips.easykey.core.tool.FileTool;
import com.blankj.utilcode.util.ToastUtils;

public class PhilipsWifiVideoLockCallingActivity extends BaseActivity<IWifiLockVideoCallingView,
        WifiVideoLockCallingPresenter<IWifiLockVideoCallingView>> implements IWifiLockVideoCallingView{

    ImageView ivAnswerIcon;
    ImageView ivRefuseIcon;
    ImageView ivRefuseIcon1;
    AVLoadingIndicatorView avi;
    AVSpeakerView avSpeakerView;
    TextView tvTips;
    ImageView ivSetting;
    ImageView back;
    SurfaceView mSufaceView;
    TextView tvTemporaryPassword;
    RelativeLayout rlVideoLayout;
    RelativeLayout rlMarkLayout;
    ImageView ivScreenshot;
    ImageView ivMute;
    ImageView ivCalling;
    ImageView ivRecoring;
    ImageView ivAlbum;
    RelativeLayout llyRecord;
    ImageView ivScreenshotBitmap;
    RelativeLayout mRlScreenShot;
    ImageView ivRecordSpot;
    TextView tvTime;
    ImageView ivRealTimeRefuseIcon;
    RelativeLayout rlCallingTime;
    ImageView ivBigHeadPic;
    TextView tvVideoTimeStamp;
    TextView tvCallingTips;
    TextView tvDoorbell;
    ImageView ivCache;
    RelativeLayout rlTitleBar;
    RelativeLayout titleBar;
    TextView mTvHeadTitle;
    ImageView mIvTemporaryPwd;

    private Bitmap myBitmap;


    private Dialog dialog;
    private Dialog openDialog;

    private int cnt = 0;
    private Timer timer = null;
    private TimerTask timerTask = null;

    private boolean isConnect = false;
    private String wifiSn;
    private WifiLockInfo wifiLockInfo;

    private int isCalling = 0;

    private InnerRecevier mInnerRecevier = null;

    private boolean isMute = false;

    private boolean isShowAudio = false;

    private boolean isFirstAudio = false;

    //门铃调用1次
    private boolean isDoorbelling = false;

    private RequestOptions options;

    final RxPermissions rxPermissions = new RxPermissions(this);

    private List<ProductInfo> productList = new ArrayList<>();

    private boolean isLastPirture = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.philips_activity_wifi_lock_video_calling);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        initUI();
        initOnClickListener();

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);

        isCalling = getIntent().getIntExtra(KeyConstants.WIFI_VIDEO_LOCK_CALLING,0);
        productList = MyApplication.getInstance().getProductInfos();
        if(isCalling == 0){
            rlCallingTime.setVisibility(View.GONE);
            tvDoorbell.setVisibility(View.GONE);
            isDoorbelling = false;
        }else if(isCalling == 1){
            ivRealTimeRefuseIcon.setVisibility(View.GONE);
            tvDoorbell.setVisibility(View.VISIBLE);
            ivRefuseIcon1.setVisibility(View.GONE);
            if(avi != null){
                avi.hide();
            }
            if(tvTips != null)
                tvTips.setVisibility(View.GONE);
            isDoorbelling = true;
        }

        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        if(wifiLockInfo == null){
            wifiLockInfo = MyApplication.getInstance().searchVideoLock(wifiSn);
        }
        if (wifiLockInfo != null){
            mPresenter.settingDevice(wifiLockInfo);
//            changeIcon();
            String lockNickname = wifiLockInfo.getLockNickname();
            mTvHeadTitle.setText(TextUtils.isEmpty(lockNickname) ? wifiLockInfo.getWifiSN() : lockNickname);
        }

        rlVideoLayout.setVisibility(View.GONE);
        rlMarkLayout.setVisibility(View.VISIBLE);
        if(avSpeakerView != null){
            avSpeakerView.hide();
        }
        mPresenter.handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isCalling == 1 && rlMarkLayout.getVisibility() == View.VISIBLE && rlVideoLayout.getVisibility() == View.GONE){
                    finish();
                }
            }
        },30 * 1000);
        llyRecord.setVisibility(View.GONE);
        avi.show();

        tvTemporaryPassword.setVisibility(View.GONE);
        tvTemporaryPassword.setText("");

        initLinstener();
/*        //动态设置状态栏高度
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(titleBar.getLayoutParams());
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        titleBar.setLayoutParams(lp);*/
        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(rlTitleBar.getLayoutParams());
        rllp.setMargins(0, getStatusBarHeight(), 0, 0);
        rlTitleBar.setLayoutParams(rllp);
    }

    private void initUI() {
        avSpeakerView = findViewById(R.id.av_speaker_view);
        ivAnswerIcon = findViewById(R.id.iv_answer_icon);
        ivRefuseIcon = findViewById(R.id.iv_refuse_icon);
        ivRefuseIcon1 = findViewById(R.id.iv_refuse_icon_1);
        avi = findViewById(R.id.avi);
        tvTips = findViewById(R.id.tv_tips);
        ivSetting = findViewById(R.id.iv_setting);
        back = findViewById(R.id.back);
        mSufaceView = findViewById(R.id.surface_view);
        tvTemporaryPassword = findViewById(R.id.tv_temporary_password);
        rlVideoLayout = findViewById(R.id.rl_video_layout);
        rlMarkLayout = findViewById(R.id.rl_mark_layout);
        ivScreenshot = findViewById(R.id.iv_screenshot);
        ivMute = findViewById(R.id.iv_mute);
        ivCalling = findViewById(R.id.iv_calling);
        ivRecoring = findViewById(R.id.iv_recoring);
        ivAlbum = findViewById(R.id.iv_album);
        llyRecord = findViewById(R.id.lly_record);
        ivScreenshotBitmap = findViewById(R.id.iv_screenshot_bitmap);
        mRlScreenShot = findViewById(R.id.ll_screenshot);
        ivRecordSpot = findViewById(R.id.iv_record_spot);
        tvTime = findViewById(R.id.tv_time);
        ivRealTimeRefuseIcon = findViewById(R.id.iv_real_time_refuse_icon);
        rlCallingTime = findViewById(R.id.rl_calling_time);
        ivBigHeadPic = findViewById(R.id.iv_big_head_pic);
        tvVideoTimeStamp = findViewById(R.id.tv_video_timestamp);
        tvCallingTips = findViewById(R.id.tv_calling_tip);
        tvDoorbell = findViewById(R.id.tv_doorbell);
        ivCache = findViewById(R.id.iv_cache);
        rlTitleBar = findViewById(R.id.rl_title_bar);
        titleBar = findViewById(R.id.title_bar);
        mTvHeadTitle = findViewById(R.id.head_title);
        mIvTemporaryPwd = findViewById(R.id.iv_temporary_pwd);
    }

    private void initOnClickListener() {
        back.setOnClickListener(v -> finish());
        findViewById(R.id.mark_back).setOnClickListener(v -> finish());
        ivRealTimeRefuseIcon.setOnClickListener(v -> {
            mPresenter.stopConnect();
            avi.hide();
            tvTips.setVisibility(View.GONE);
            finish();
        });
        ivRefuseIcon1.setOnClickListener(v -> {
            mPresenter.stopConnect();
            avi.hide();
            tvTips.setVisibility(View.GONE);
            finish();
        });
        ivRefuseIcon.setOnClickListener(v -> {
            mPresenter.stopConnect();
            avi.hide();
            tvTips.setVisibility(View.GONE);
            finish();
        });
        ivAnswerIcon.setOnClickListener(v -> {
            if(this.isConnect){
                rlVideoLayout.setVisibility(View.VISIBLE);
                rlMarkLayout.setVisibility(View.GONE);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        if(isShowAudio && mPresenter.startAudioStream() >= 0) {
                            if (!mPresenter.isEnableAudio()) {
                                mPresenter.enableAudio(true);
                                isMute = false;
                                isShowAudio = false;
                            }
                        }
                    }
                }).start();
            }else{
                ivAnswerIcon.setVisibility(View.GONE);
                ivRefuseIcon.setVisibility(View.GONE);
                tvDoorbell.setVisibility(View.GONE);
                avi.setVisibility(View.VISIBLE);
                avi.show();
                tvTips.setVisibility(View.VISIBLE);
                isDoorbelling = false;
            }
        });
        ivMute.setOnClickListener(v -> {
            if(!isMute){
                if(mPresenter.stopAudioStream() >= 0){
                    if(mPresenter.isEnableAudio()){
                        mPresenter.enableAudio(false);
                        isMute = true;
                        ivMute.setImageResource(R.drawable.philips_video_icon_mute_selected);
                        showShort(getString(R.string.wifi_video_lock_mute_on));
                    }
                }

            }else{
                if(mPresenter.startAudioStream() >=0){
                    if(!mPresenter.isEnableAudio()){
                        mPresenter.enableAudio(true);
                        isMute = false;
                        ivMute.setImageResource(R.drawable.philips_video_icon_mute);
                        showShort(getString(R.string.philips_wifi_video_lock_mute_off));
                    }
                }

            }
        });
        ivSetting.setOnClickListener(v -> {
            isLastPirture = true;
            Intent settingIntent = new Intent(PhilipsWifiVideoLockCallingActivity.this, PhilipsWifiVideoLockRealTimeActivity.class);
            settingIntent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            startActivity(settingIntent);
            mPresenter.release();
        });
        ivAlbum.setOnClickListener(v -> {
            isLastPirture = true;
            Intent intent = new Intent(PhilipsWifiVideoLockCallingActivity.this, PhilipsWifiVideoLockAlbumActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            startActivity(intent);
            mPresenter.release();
        });
        ivRecoring.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ToastUtils.showShort(getString(R.string.wifi_video_lock_read_and_write_permission));
            }else{
                if(!ivRecoring.isSelected()){
                    ivRecoring.setSelected(true);
                    llyRecord.setVisibility(View.VISIBLE);
                    if(wifiLockInfo != null){
                        String filePath = FileTool.getVideoLockPath(this,wifiLockInfo.getWifiSN()).getPath() + File.separator +System.currentTimeMillis()+".mp4"  ;
                        LogUtils.d("shulan videocalling filepath-->" + filePath);
                        mPresenter.startRecordMP4(filePath);
                        showShort(getString(R.string.wifi_video_lock_screen_recording_enable));
                    }

                }else{
                    ivRecoring.setSelected(false);
                    llyRecord.setVisibility(View.GONE);
                    mPresenter.stopRecordMP4();
                    showShort(getString(R.string.wifi_video_lock_screen_recording_disable));
                }
                llyRecord.setVisibility(View.VISIBLE);


            }
        });
        ivScreenshot.setOnClickListener(v -> mPresenter.snapImage());
        mIvTemporaryPwd.setOnClickListener(v -> {
            if(!mIvTemporaryPwd.isSelected()){
                tvTemporaryPassword.setText(getPassword() + "#");
//                    tvTemporaryPassword.setText(XMP2PManager.getInstance().getMode() + "");//test查看讯美p2p连接直连还是转发
                tvTemporaryPassword.setVisibility(View.VISIBLE);
                mIvTemporaryPwd.setSelected(true);
            }else{
                mIvTemporaryPwd.setSelected(false);
                tvTemporaryPassword.setVisibility(View.GONE);
            }

        });
        tvTemporaryPassword.setTextIsSelectable(true);
    }

    @Deprecated
    private void changeIcon() {
        ivBigHeadPic.setImageResource(BleLockUtils.getDetailImageByModel(wifiLockInfo.getProductModel()));
        if (!TextUtils.isEmpty(wifiLockInfo.getProductModel())){
            ivBigHeadPic.setImageResource(BleLockUtils.getDetailImageByModel(wifiLockInfo.getProductModel()));
            String model = wifiLockInfo.getProductModel();
            String WifiSN = wifiLockInfo.getWifiSN();

            if (model != null && WifiSN != null) {
                //本地图片有对应的产品则不获取缓存的产品型号图片，缓存没有则选择尝试下载
                if (BleLockUtils.getDetailImageByModel(model) == R.mipmap.bluetooth_lock_default) {
                    options = new RequestOptions()
                            .placeholder(R.mipmap.bluetooth_lock_default)      //加载成功之前占位图
                            .error(R.mipmap.bluetooth_lock_default)      //加载错误之后的错误图
                            .diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
                            .dontAnimate()                                    //直接显示图片
                            .fitCenter();

                    for (ProductInfo productInfo : productList) {
                        try {
                            if (productInfo.getSnHead().equals(WifiSN.substring(0,3))) {

                                Glide.with(this).load(productInfo.getAdminUrl()).apply(options).into(ivBigHeadPic);
                            }
                        } catch (Exception e) {
                            LogUtils.d("--kaadas--:" + e.getMessage());
                        }
                    }
                }
            }
        }
    }

    private void initLinstener() {
        ivCalling.setOnClickListener(v -> {
            if(isFirstAudio){
                if(ivCalling.isSelected()){
                    ivCalling.setSelected(false);
                    mPresenter.talkback(false);
                    mPresenter.stopTalkback();
                    tvCallingTips.setText(getString(R.string.wifi_video_lock_talk_back));
                    showShort(getString(R.string.philips_wifi_video_lock_close_talk_back));
                }else{
                    ivCalling.setSelected(true);
                    mPresenter.talkback(true);
                    mPresenter.startTalkback();
                    showShort(getString(R.string.philips_wifi_video_lock_open_talk_back));
                    tvCallingTips.setText(getString(R.string.wifi_video_lock_talking_back));
                    ivCalling.setVisibility(View.INVISIBLE);
                    avSpeakerView.show();
                }
            }
        });
        avSpeakerView.setOnClickListener(v -> {
            if(isFirstAudio){
                ivCalling.setSelected(false);
                mPresenter.talkback(false);
                mPresenter.stopTalkback();
                tvCallingTips.setText(getString(R.string.wifi_video_lock_talk_back));
                showShort(getString(R.string.philips_wifi_video_lock_close_talk_back));
                ivCalling.setVisibility(View.VISIBLE);
                avSpeakerView.hide();
            }
        });
    }

    @Override
    protected WifiVideoLockCallingPresenter<IWifiLockVideoCallingView> createPresent() {
        return new WifiVideoLockCallingPresenter<>();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showShort(String ss) {
        ToastUtils.getDefaultMaker().setGravity(Gravity.CENTER,0,0).showShort(ss);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSufaceView.setVisibility(View.GONE);
        mSufaceView.setVisibility(View.VISIBLE);
        int keepAliveStatus = 0;
        try{
            keepAliveStatus = wifiLockInfo.getPowerSave();
        }catch (Exception e){

        }
        if(!WifiUtils.getInstance(this).isWifiEnable()){
            openWifiDialog();
            return;
        }
        if(keepAliveStatus == 0){
            switchBackConnectP2P();
        }else{
            if(isCalling == 1){
                switchBackConnectP2P();
            }else{
                mPresenter.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(avi != null){
                            avi.hide();
                        }
                        if(tvTips != null)
                            tvTips.setVisibility(View.GONE);
                        showKeepAliveDialog();
                    }
                },500);
            }
        }
        resetStatus();
        registerBroadcast();
        if(isLastPirture){
            if(myBitmap != null) {
                if (ivCache != null) {
                    ivCache.setVisibility(View.VISIBLE);
                    ivCache.setImageBitmap(myBitmap);
                }
            }
        }else{
            if(ivCache != null)
                ivCache.setVisibility(View.GONE);
        }
    }

    private void openWifiDialog() {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(this, getString(R.string.philips_network_not_turned_on),
                "#333333", getString(R.string.philips_cancel), getString(R.string.setting),
                "#0066A1", "#ffffff", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {
                        finish();
                    }

                    @Override
                    public void right() {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
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

    private void showKeepAliveDialog() {
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

    @Override
    public void finish() {
        isCalling = 0;
        mPresenter.release();
        if(getIntent().getBooleanExtra("VIDEO_CALLING_IS_MAINACTIVITY",false)){
            startActivity(new Intent(this, MainActivity.class));
        }
        super.finish();
    }

    @Override
    public void onConnectFailed(int paramInt) {
        mPresenter.handler.post(new Runnable() {
            @Override
            public void run() {
                if(!PhilipsWifiVideoLockCallingActivity.this.isFinishing()){
                    if(avi != null){
                        avi.hide();
                    }
                    if(tvTips != null)
                        tvTips.setVisibility(View.GONE);
                    LogUtils.d("shulan"+this + " paramInt=" + paramInt);
                    String errorStringWithCode;
                    if(paramInt >0){
                        errorStringWithCode = XMP2PConnectJsonError.checkP2PJSONErrorStringWithCode(PhilipsWifiVideoLockCallingActivity.this,paramInt);
                    }else{
                        if(isConnect && (paramInt == -13 || paramInt == -12)){
                            mPresenter.talkback(false);
                            mPresenter.stopTalkback();
                            resetStatus();
                            mPresenter.setStartTime();
                            mPresenter.connectP2P();
                            return;
                        }
                        errorStringWithCode = XMP2PConnectError.checkP2PErrorStringWithCode(PhilipsWifiVideoLockCallingActivity.this,paramInt);
                    }
                    isConnect = false;
                    creteDialog(errorStringWithCode + "");

                }

            }
        });


    }

    private void resetStatus() {
        ivMute.setImageResource(R.drawable.philips_video_icon_mute);
        isShowAudio = true;
        isFirstAudio = false;
        llyRecord.setVisibility(View.GONE);
        ivRecoring.setSelected(false);
        ivCalling.setSelected(false);
        tvCallingTips.setText(getString(R.string.wifi_video_lock_talk_back));
        tvTemporaryPassword.setVisibility(View.GONE);
        tvTemporaryPassword.setText("");
    }


    @Override
    public void onConnectSuccess() {
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isCalling == 1){
                    avi.hide();
                    tvTips.setVisibility(View.GONE);
                }else{

                }
            }
        });*/
        mPresenter.startRealTimeVideo(mSufaceView);

    }

    @Override
    public void onStartConnect(String paramString) {

    }

    @Override
    public void onErrorMessage(int errno) {
        mPresenter.handler.post(new Runnable() {
            @Override
            public void run() {
                if(errno == XMP2PConnectJsonError.XM_JSON_ERROR_TALK_OCCUPIED){
                    mPresenter.talkback(false);
                    mPresenter.stopTalkback();
                    ivCalling.setSelected(false);
                    tvCallingTips.setText(getString(R.string.wifi_video_lock_talk_back));
                    showShort(getString(R.string.xm_json_error_talk_occupied) + "");
                }

            }
        });
    }

    @Override
    public void onLastFrameRgbData(int[] ints, int width, int height, boolean b) {
        if(ints != null ){
            Bitmap bitmap = MyBitmapFactory.createMyBitmap(ints, width, height);
            if(bitmap == null){
                return;
            }
            myBitmap = BitmapUtil.rotaingImageView(90,bitmap);
            if(!b){
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if(ivScreenshotBitmap != null){
                            Glide.with(PhilipsWifiVideoLockCallingActivity.this).load(myBitmap).into(ivScreenshotBitmap);
                            mRlScreenShot.setVisibility(View.VISIBLE);
                        }
                    }
                });

                mPresenter.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(ivScreenshotBitmap != null)
                            mRlScreenShot.setVisibility(View.GONE);
                    }
                },3000);

                String fileName = System.currentTimeMillis()+".png";
                BitmapUtil.save(FileTool.getVideoLockPath(this,wifiLockInfo.getWifiSN()).getPath() ,fileName,myBitmap);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                     ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, FileTool.getVideoLockPath(this,wifiLockInfo.getWifiSN()).getPath());
                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                }else{
                    // 其次把文件插入到系统图库
                    try {
                        MediaStore.Images.Media.insertImage(PhilipsWifiVideoLockCallingActivity.this.getContentResolver(),
                                FileTool.getVideoLockPath(this,wifiLockInfo.getWifiSN()).getAbsolutePath(), fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                // 最后通知图库更新
                // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
                PhilipsWifiVideoLockCallingActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(FileTool.getVideoLockPath(this,wifiLockInfo.getWifiSN()))));

            }

        }

    }

    @Override
    public void onstartRecordMP4CallBack() {
        runOnUiThread(new Runnable() {
              @Override
              public void run() {
                  if(tvTime != null)
                    tvTime.setText("00:00:00");
                  ivRecordSpot.setVisibility(View.VISIBLE);
                  startTimer();

              }
          });

    }

    //开始视频回调，时间戳数据...
    @Override
    public void onVideoDataAVStreamHeader(AVStreamHeader paramAVStreamHeader) {

        if(!isFirstAudio){
            if(isCalling == 0 || !isDoorbelling){
                if(isShowAudio && mPresenter.startAudioStream() >= 0) {
                    if (!mPresenter.isEnableAudio()) {
                        mPresenter.enableAudio(true);
                        isMute = false;
                        isShowAudio = false;
                    }
                }
            }
            isFirstAudio = true;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                tvVideoTimeStamp.setText(DateUtils.getDateTimeFromMillisecond(paramAVStreamHeader.m_TimeStamp - 28800000));
                if(ivCache!= null)
                    ivCache.setVisibility(View.GONE);
                if(isCalling == 0 || !isDoorbelling){
                    if(avi!=null){
                        avi.hide();
                    }
                    if(tvTips != null){
                        tvTips.setVisibility(View.GONE);
                    }
                    if(rlVideoLayout != null){
                        rlVideoLayout.setVisibility(View.VISIBLE);
                    }
                    if(rlMarkLayout != null){
                        rlMarkLayout.setVisibility(View.GONE);
                    }
                }
            }
        });
        this.isConnect = true;

    }

    @Override
    public void recordAudidFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(getString(R.string.wifi_video_lock_microphone_permission));
            }
        });
    }

    @Override
    public void openDoor(WifiLockOperationBean.EventparamsBean eventparams) {
        if(!PhilipsWifiVideoLockCallingActivity.this.isFinishing()){
            if(eventparams.getEventCode() == 2){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        openDialog();
                    }
                });
            }
        }
    }

    private void openDialog() {
        if(openDialog == null){
            openDialog = new Dialog(this, R.style.MyDialog);
        }
        // 获取Dialog布局
        View mView = LayoutInflater.from(this).inflate(R.layout.dialog_title_two_button_dialog, null);
        TextView tvContent = mView.findViewById(R.id.tv_content);
        TextView title = mView.findViewById(R.id.title);
        title.setText(getString(R.string.dialog_wifi_video_tip));
        tvContent.setText(getString(R.string.dialog_wifi_video_open_the_door_continue_watching));
        TextView tv_cancel = mView.findViewById(R.id.tv_left);
        tv_cancel.setText(getString(R.string.dialog_wifi_video_continue_playing));
        tv_cancel.setTextColor(Color.parseColor("#9A9A9A"));
        TextView tv_query = mView.findViewById(R.id.tv_right);
        tv_query.setTextColor(Color.parseColor("#2096F8"));
        tv_query.setText(getString(R.string.close));
        openDialog.setContentView(mView);

        Window window = openDialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams params = window.getAttributes();
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        params.width = (int) (width * 0.8);
        window.setAttributes(params);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });
        tv_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
                finish();
            }
        });
        if(!PhilipsWifiVideoLockCallingActivity.this.isFinishing()){
            openDialog.show();
        }
    }

    @Override
    public void onStopRecordMP4CallBack(MP4Info mp4Info) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivRecordSpot.setAnimation(null);
                stopTimer();
                // 最后通知图库更新
                if(mp4Info.isResult()){
                    PhilipsWifiVideoLockCallingActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(mp4Info.getFilePath()))));
                    mPresenter.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            llyRecord.setVisibility(View.GONE);
                        }
                    },300);
                }
            }
        });
    }

    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
        cnt = 0;
    }

    private void startTimer(){
        if (timer == null) {
            timer = new Timer();
        }

        if (timerTask == null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(tvTime != null)
                                tvTime.setText(DateUtils.getStringTime(cnt++));
                            ivRecordSpot.setVisibility(View.INVISIBLE);
                            mPresenter.handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ivRecordSpot.setVisibility(View.VISIBLE);
                                }
                            },500);
                        }
                    });
                }
            };
        }

        if(timer != null && timerTask != null )
            timer.schedule(timerTask,0,1000);

    }


    private String getPassword() {
        if (wifiLockInfo != null) {
            if (!TextUtils.isEmpty(wifiLockInfo.getWifiSN())) {

                String wifiSN = wifiLockInfo.getWifiSN();
                String randomCode = wifiLockInfo.getRandomCode();
                String time = (System.currentTimeMillis() / 1000 / 60 / 5) + "";
                String content = wifiSN + randomCode + time;
                byte[] data = content.toUpperCase().getBytes();
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update(data);
                    byte[] digest = messageDigest.digest();
                    byte[] temp = new byte[4];
                    System.arraycopy(digest, 0, temp, 0, 4);
                    long l = Rsa.getInt(temp);
                    String text = (l % 1000000) + "";
                    int offSet = (6 - text.length());
                    for (int i = 0; i < offSet; i++) {
                        text = "0" + text;
                    }
                    return text;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }



    public void creteDialog(String content){
        if(dialog == null){
            dialog = new Dialog(this, R.style.MyDialog);
        }
        // 获取Dialog布局
        View mView = LayoutInflater.from(this).inflate(R.layout.philips_no_et_title_two_button_dialog, null);
        TextView tvContent = mView.findViewById(R.id.tv_content);
        tvContent.setTextColor(Color.parseColor("#333333"));
        tvContent.setText(content + "");
        TextView tv_cancel = mView.findViewById(R.id.tv_left);
        tv_cancel.setText(getString(R.string.close));
        tv_cancel.setTextColor(Color.parseColor("#0066A1"));
        TextView tv_query = mView.findViewById(R.id.tv_right);
        tv_query.setTextColor(Color.parseColor("#FFFFFF"));
        tv_query.setText(getString(R.string.philips_clothes_hanger_add_next));
        dialog.setContentView(mView);

        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams params = window.getAttributes();
        WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        int width = display.getWidth();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        params.width = (int) (width * 0.8);
        window.setAttributes(params);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });
        tv_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetStatus();
                avi.setVisibility(View.VISIBLE);
                avi.show();
                tvTips.setVisibility(View.VISIBLE);
                dialog.dismiss();
                connectP2P();
            }
        });

        if(!PhilipsWifiVideoLockCallingActivity.this.isFinishing()){
            if(dialog != null)
            	dialog.show();
        }

    }

    private void switchBackConnectP2P(){
        if(dialog != null){
            if(!dialog.isShowing()){
                connectP2P();
            }
        }else{
            connectP2P();
        }
    }

    private void connectP2P(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPresenter.setStartTime();
                mPresenter.connectP2P();
            }
        }).start();
    }

    private void registerBroadcast(){
        if(mInnerRecevier == null){
            mInnerRecevier = new InnerRecevier();
        }
        IntentFilter homeFilter = new IntentFilter();
        homeFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        homeFilter.addAction(Intent.ACTION_SCREEN_ON);
        homeFilter.addAction(Intent.ACTION_SCREEN_OFF);
        homeFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mInnerRecevier, homeFilter);
    }

    private void unRegisterBroadcast(){
        if(mInnerRecevier != null){
            unregisterReceiver(mInnerRecevier);
        }
    }

    private class InnerRecevier extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";

        final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";

        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (reason != null) {
                    if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                        // home键
                        isLastPirture = true;
                        mPresenter.release();
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        //多任务
                        isLastPirture = true;
                        mPresenter.release();
                    }
                }
            }else if(action.equals(Intent.ACTION_SCREEN_ON)){

            }else if(action.equals(Intent.ACTION_SCREEN_OFF)){

                isLastPirture = true;
                mPresenter.release();
            }else if(action.equals(Intent.ACTION_USER_PRESENT)){// 解锁


            }

            }
        }


    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
