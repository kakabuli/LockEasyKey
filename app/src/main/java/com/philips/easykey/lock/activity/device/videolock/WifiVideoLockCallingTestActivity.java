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
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockCallingPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVideoCallingView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.WifiLockOperationBean;
import com.philips.easykey.lock.utils.BitmapUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.greenDao.db.DaoSession;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xm.sdk.struct.stream.AVStreamHeader;
import com.xmitech.sdk.MP4Info;
import com.yuv.display.MyBitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import com.philips.easykey.core.tool.FileTool;
import com.blankj.utilcode.util.ToastUtils;

public class WifiVideoLockCallingTestActivity extends BaseActivity<IWifiLockVideoCallingView,
        WifiVideoLockCallingPresenter<IWifiLockVideoCallingView>> implements IWifiLockVideoCallingView{

    ImageView ivAnswerIcon;
    ImageView ivRefuseIcon;
    AVLoadingIndicatorView avi;
    TextView tvTips;

    ImageView ivSetting;
    ImageView back;
    SurfaceView mSufaceView;
    TextView tvTemporaryPassword;
    LinearLayout llyTemporaryPassword;
    RelativeLayout rlVideoLayout;
    RelativeLayout rlMarkLayout;
    ImageView ivScreenshot;
    ImageView ivMute;
    ImageView ivCalling;
    ImageView ivRecoring;
    ImageView ivAlbum;
    RelativeLayout llyRecord;
    ImageView ivScreenshotBitmap;
    ImageView ivRecordSpot;
    TextView tvTime;
    RelativeLayout rlCallingTime;
    TextView tvVideoTimeStamp;

    private Dialog dialog;
    private Dialog openDialog;

    private int cnt = 0;
    private Timer timer = null;
    private TimerTask timerTask = null;

    private boolean isConnect = false;
    private String wifiSn;
    private WifiLockInfo wifiLockInfo;

    private boolean isPasswordShow = false;

    private int isCalling = 0;

    private InnerRecevier mInnerRecevier = null;

    private boolean isMute = false;

    private boolean isShowAudio = false;

    //门铃调用1次
    private boolean isDoorbelling = false;

    final RxPermissions rxPermissions = new RxPermissions(this);

    private Disposable xunhaunDisposable;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_video_calling);

        ivAnswerIcon = findViewById(R.id.iv_answer_icon);
        ivRefuseIcon = findViewById(R.id.iv_refuse_icon);
        avi = findViewById(R.id.avi);
        tvTips = findViewById(R.id.tv_tips);
        ivSetting = findViewById(R.id.iv_setting);
        back = findViewById(R.id.back);
        mSufaceView = findViewById(R.id.surface_view);
        tvTemporaryPassword = findViewById(R.id.tv_temporary_password);
        llyTemporaryPassword = findViewById(R.id.lly_temporary_password);
        rlVideoLayout = findViewById(R.id.rl_video_layout);
        rlMarkLayout = findViewById(R.id.rl_mark_layout);
        ivScreenshot = findViewById(R.id.iv_screenshot);
        ivMute = findViewById(R.id.iv_mute);
        ivCalling = findViewById(R.id.iv_calling);
        ivRecoring = findViewById(R.id.iv_recoring);
        ivAlbum = findViewById(R.id.iv_album);
        llyRecord = findViewById(R.id.lly_record);
        ivScreenshotBitmap = findViewById(R.id.iv_screenshot_bitmap);
        ivRecordSpot = findViewById(R.id.iv_record_spot);
        tvTime = findViewById(R.id.tv_time);
        rlCallingTime = findViewById(R.id.rl_calling_time);
        tvVideoTimeStamp = findViewById(R.id.tv_video_timestamp);

        back.setOnClickListener(v -> finish());
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
                new Thread(() -> {
                    if(isShowAudio && mPresenter.startAudioStream() >= 0) {
                        if (!mPresenter.isEnableAudio()) {

                            mPresenter.enableAudio(true);
                            isMute = false;
                            isShowAudio = false;
                        }
                    }
                }).start();
            }
        });
        ivMute.setOnClickListener(v -> {
            if(!isMute){

                if(mPresenter.stopAudioStream() >= 0){
                    if(mPresenter.isEnableAudio()){
                        mPresenter.enableAudio(false);
                        isMute = true;
                        ivMute.setImageResource(R.mipmap.real_time_video_mute_seleted);

                    }
                }

            }else{
                if(mPresenter.startAudioStream() >=0){
                    if(!mPresenter.isEnableAudio()){
                        mPresenter.enableAudio(true);
                        isMute = false;
                        ivMute.setImageResource(R.drawable.philips_video_icon_mute);
                    }
                }
            }
        });
        ivSetting.setOnClickListener(v -> {
            Intent settingIntent = new Intent(WifiVideoLockCallingTestActivity.this, PhilipsWifiVideoLockRealTimeActivity.class);
            settingIntent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            startActivity(settingIntent);
            mPresenter.release();
        });
        ivAlbum.setOnClickListener(v -> {
            Intent intent = new Intent(WifiVideoLockCallingTestActivity.this, PhilipsWifiVideoLockAlbumActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
            startActivity(intent);
            mPresenter.release();
        });
        ivRecoring.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                ToastUtils.showShort(R.string.wifi_video_lock_read_and_write_permission);
            }else{
                   /* // 勾选了不再提示
                    if (!ActivityCompat.shouldShowRequestPermissionRationale
                            (this,Manifest.permission.WRITE_EXTERNAL_STORAGE ) &&
                            !ActivityCompat.shouldShowRequestPermissionRationale
                            (this,Manifest.permission.READ_EXTERNAL_STORAGE )) {
                        // ...
                        ToastUtil.showShort(R.string.wifi_video_lock_read_and_write_permission);
                    }else{*/
                if(!ivRecoring.isSelected()){
                    ivRecoring.setSelected(true);
                    llyRecord.setVisibility(View.VISIBLE);
                    if(wifiLockInfo != null){
                        String filePath = FileTool.getVideoLockPath(this,wifiLockInfo.getWifiSN()).getPath() + File.separator +System.currentTimeMillis()+".mp4"  ;
                        mPresenter.startRecordMP4(filePath);
                    }

                }else{
                    ivRecoring.setSelected(false);
                    llyRecord.setVisibility(View.GONE);
                    mPresenter.stopRecordMP4();
                }
                llyRecord.setVisibility(View.VISIBLE);
//                    }

            }
        });
        ivScreenshot.setOnClickListener(v -> mPresenter.snapImage());
        findViewById(R.id.iv_temporary_pwd).setOnClickListener(v -> {
            if(!isPasswordShow){
                tvTemporaryPassword.setText(getPassword() + "");
                llyTemporaryPassword.setVisibility(View.VISIBLE);
                isPasswordShow = true;
            }else{
                isPasswordShow = false;
                llyTemporaryPassword.setVisibility(View.GONE);
            }
        });

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);

        isCalling = getIntent().getIntExtra(KeyConstants.WIFI_VIDEO_LOCK_CALLING,0);
        if(isCalling == 0){
            rlCallingTime.setVisibility(View.GONE);
            isDoorbelling = false;
        }else if(isCalling == 1){
            rlCallingTime.setVisibility(View.VISIBLE);
            isDoorbelling = true;
        }

        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        if(wifiLockInfo == null){

            wifiLockInfo = searchVideoLock(wifiSn);
        }
        if (wifiLockInfo != null){
            mPresenter.settingDevice(wifiLockInfo);
            String lockNickname = wifiLockInfo.getLockNickname();
        }

        rlVideoLayout.setVisibility(View.VISIBLE);
        rlMarkLayout.setVisibility(View.GONE);
        llyRecord.setVisibility(View.GONE);
        avi.show();
        handler.post(timeout_600);
        initLinstener();
    }

    private void initLinstener() {
        ivCalling.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    if (ContextCompat.checkSelfPermission(WifiVideoLockCallingTestActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions( WifiVideoLockCallingTestActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                        ToastUtils.showShort(R.string.wifi_video_lock_microphone_permission);
                    }else{
                        // 勾选了不再提示
                        if (! ActivityCompat.shouldShowRequestPermissionRationale
                                (WifiVideoLockCallingTestActivity.this,Manifest.permission.RECORD_AUDIO )) {
                            // ...
                            ToastUtils.showShort(R.string.wifi_video_lock_microphone_permission);
                        }else{
                            if(mPresenter.isTalkback()){
                                ivCalling.setSelected(false);
                                mPresenter.talkback(false);
                                mPresenter.stopTalkback();
                            }else{
                                ivCalling.setSelected(true);
                                mPresenter.talkback(true);
                                mPresenter.startTalkback();
                            }
                        }

                    }
                }
                return false;
            }
        });
    }

    @Override
    protected WifiVideoLockCallingPresenter<IWifiLockVideoCallingView> createPresent() {
        return new WifiVideoLockCallingPresenter<>();
    }

    @Override
    protected void onResume() {
        super.onResume();

        xunhaunDisposable = Observable
                .interval(0, 1, TimeUnit.SECONDS)
                .compose(RxjavaHelper.observeOnMainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if(aLong % 300 == 0){

                            handler.post(timeout_600);
                        }
                    }
                });


//        mPresenter.startRealTimeVideo(mSufaceView);
        ivMute.setImageResource(R.drawable.philips_video_icon_mute);
        isShowAudio = true;
        registerBroadcast();

    }

    private Runnable timeout_20 = new Runnable() {
        @Override
        public void run() {
            mPresenter.release();
        }
    };

    private Runnable timeout_600 = new Runnable() {
        @Override
        public void run() {
            mPresenter.connectP2P();
        }
    };

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
        if(xunhaunDisposable != null){
            xunhaunDisposable.dispose();
        }
    }

    @Override
    public void finish() {
        if(getIntent().getBooleanExtra("VIDEO_CALLING_IS_MAINACTIVITY",false)){
            startActivity(new Intent(this, MainActivity.class));
        }
        super.finish();
        LogUtils.d(this + "finish-----------");
        isCalling = 0;
        mPresenter.release();
    }

    @Override
    public void onConnectFailed(int paramInt) {
        mPresenter.handler.post(new Runnable() {
            @Override
            public void run() {
                if(!WifiVideoLockCallingTestActivity.this.isFinishing()){
                    if(avi != null){
                        avi.hide();
                    }
                    if(tvTips != null)
                        tvTips.setVisibility(View.GONE);
                    /*if(ivAnswerIcon != null)
                        ivAnswerIcon.setVisibility(View.GONE);
                    if(ivRefuseIcon != null)
                        ivRefuseIcon.setVisibility(View.GONE);
                    if(tvAnswer != null)
                        tvAnswer.setVisibility(View.GONE);
                    if(tvRefuse != null)
                        tvRefuse.setVisibility(View.GONE);*/
                    isConnect = false;
                    LogUtils.d(this + "");
                    if(paramInt == -3){
                        creteDialog(getString(R.string.video_lock_xm_connect_time_out) + "");
                    }else{
                        creteDialog(getString(R.string.video_lock_xm_connect_failed) + "");
                    }
                }

            }
        });


    }

    @Override
    public void onConnectSuccess() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(isCalling == 1){
                    avi.hide();
                    tvTips.setVisibility(View.GONE);

                }
            }
        });
        mPresenter.startRealTimeVideo(mSufaceView);
        handler.postDelayed(timeout_20,20000);
        this.isConnect = true;

    }

    @Override
    public void onStartConnect(String paramString) {

    }

    @Override
    public void onErrorMessage(int errno) {
        /*runOnUiThread(new Runnable() {
            @Override
            public void run() {
                avi.hide();
                tvTips.setVisibility(View.GONE);
            }
        });*/

    }

    @Override
    public void onLastFrameRgbData(int[] ints, int width, int height, boolean b) {

        if(ints != null ){
            if(!b){
                Bitmap bitmap = MyBitmapFactory.createMyBitmap(ints, width, height);

                Bitmap myBitmap = BitmapUtil.rotaingImageView(90,bitmap);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Glide.with(WifiVideoLockCallingTestActivity.this).load(myBitmap).into(ivScreenshotBitmap);
                        ivScreenshotBitmap.setVisibility(View.VISIBLE);
                    }
                });

                mPresenter.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ivScreenshotBitmap.setVisibility(View.GONE);
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
                        MediaStore.Images.Media.insertImage(WifiVideoLockCallingTestActivity.this.getContentResolver(),
                                FileTool.getVideoLockPath(this,wifiLockInfo.getWifiSN()).getAbsolutePath(), fileName, null);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                // 最后通知图库更新
                // context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
                WifiVideoLockCallingTestActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(FileTool.getVideoLockPath(this,wifiLockInfo.getWifiSN()))));



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
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvVideoTimeStamp.setText(DateUtils.getDateTimeFromMillisecond(paramAVStreamHeader.m_TimeStamp - 28800000));
                if(isCalling == 0){
                    avi.hide();
                    tvTips.setVisibility(View.GONE);
                    rlVideoLayout.setVisibility(View.VISIBLE);
                    rlMarkLayout.setVisibility(View.GONE);

                }
            }
        });
        if(isCalling == 0){
            if(isShowAudio && mPresenter.startAudioStream() >= 0) {
                if (!mPresenter.isEnableAudio()) {

                    mPresenter.enableAudio(true);
                    isMute = false;
                    isShowAudio = false;
                }
            }
        }

    }

    @Override
    public void recordAudidFailed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtils.showShort(R.string.wifi_video_lock_microphone_permission);
            }
        });
    }

    @Override
    public void openDoor(WifiLockOperationBean.EventparamsBean eventparams) {
        if(!WifiVideoLockCallingTestActivity.this.isFinishing()){
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
   /*     tvTitle = mView.findViewById(R.id.tv_hint);
        tvTitle.setVisibility(View.GONE);*/
        TextView tvContent = mView.findViewById(R.id.tv_content);
        TextView title = mView.findViewById(R.id.title);
        title.setText(R.string.dialog_wifi_video_tip);
        tvContent.setText(R.string.dialog_wifi_video_open_the_door_continue_watching);
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

        if(!WifiVideoLockCallingTestActivity.this.isFinishing()){
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
                    WifiVideoLockCallingTestActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(mp4Info.getFilePath()))));
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
                    LogUtils.d("--kaadas--转换之后的数据是     " + l + "    " + Rsa.bytes2Int(temp));
                    int offSet = (6 - text.length());
                    for (int i = 0; i < offSet; i++) {
                        text = "0" + text;
                    }
                    System.out.println("--kaadas--   testSha256 数据是   " + Rsa.bytesToHexString(messageDigest.digest()));
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
        View mView = LayoutInflater.from(this).inflate(R.layout.no_et_title_two_button_dialog, null);
   /*     tvTitle = mView.findViewById(R.id.tv_hint);
        tvTitle.setVisibility(View.GONE);*/
        TextView tvContent = mView.findViewById(R.id.tv_content);
        tvContent.setText(content + "");
        TextView tv_cancel = mView.findViewById(R.id.tv_left);
        tv_cancel.setText(getString(R.string.close));
        tv_cancel.setTextColor(Color.parseColor("#9A9A9A"));
        TextView tv_query = mView.findViewById(R.id.tv_right);
        tv_query.setTextColor(Color.parseColor("#2096F8"));
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
            }
        });
        tv_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                avi.setVisibility(View.VISIBLE);
                avi.show();
                tvTips.setVisibility(View.VISIBLE);
                dialog.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.connectP2P();
                    }
                }).start();
            }
        });

        if(!WifiVideoLockCallingTestActivity.this.isFinishing()){
            dialog.show();
        }

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

                        mPresenter.release();
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        //多任务

                        mPresenter.release();
                    }
                }
            }else if(action.equals(Intent.ACTION_SCREEN_ON)){

            }else if(action.equals(Intent.ACTION_SCREEN_OFF)){

                mPresenter.release();
            }else if(action.equals(Intent.ACTION_USER_PRESENT)){// 解锁


            }

            }
        }

    private WifiLockInfo searchVideoLock(String wifiSN){
        DaoSession daoSession = MyApplication.getInstance().getDaoWriteSession();
        if (daoSession != null && daoSession.getWifiLockInfoDao() != null) {
            List<WifiLockInfo> wifiLockInfos = daoSession.getWifiLockInfoDao().loadAll();
            if (wifiLockInfos != null && wifiLockInfos.size() > 0) {
                for (WifiLockInfo wifiLockInfo : wifiLockInfos) {
                    if(wifiLockInfo.getWifiSN().equals(wifiSN)){
                        return wifiLockInfo;
                    }
                }
            }
        }
        return null;
    }
}
