package com.philips.easykey.lock.activity.device.videolock;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.MyAlbumPlayerPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IMyAlbumPlayerView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.player.MediaPlayerWrapper;
import com.philips.easykey.lock.publiclibrary.player.MediaStatus;
import com.philips.easykey.lock.publiclibrary.player.StatusHelper;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PConnectError;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PManager;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.RotateTransformation;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.widget.avindicator.AVLoadingIndicatorView;
import com.xm.sdk.struct.stream.AVStreamHeader;
import com.xmitech.sdk.H264Frame;
import com.xmitech.sdk.MP4Info;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.philips.easykey.core.tool.FileTool;

public class PhilipsWifiVideoLockAlbumDetailActivity extends BaseActivity<IMyAlbumPlayerView, MyAlbumPlayerPresenter<IMyAlbumPlayerView>>  implements IMyAlbumPlayerView{

    private MediaPlayerWrapper mediaPlayer;
    private String filepath;
    private StatusHelper statusHelper;

    ImageView back;
    SeekBar durationSeekBar;
    SurfaceView surfaceView;
    SurfaceView surfaceView1;
    LinearLayout llyBootomBar;
    ImageView ivPlayStart;
    ImageView ivPause;
    TextView tvDuration;
    TextView tvTime;
    TextView tvName;
    AVLoadingIndicatorView avi;
    TextView tvTips;
    ImageView ivCache;
    ImageView ivIconBackground;
    ImageView ivMyAlbumDelete;

    private String wifiSn;
    private WifiLockInfo wifiLockInfo;
    private WifiVideoLockAlarmRecord record;

    private String path;

    private Dialog dialog;

    private boolean isPlay = false;

    private InnerRecevier mInnerRecevier = null;

    private boolean isRecordSuccess = false;

    private long currentTime = 0;

    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            updateProgress();
            timerHandler.postDelayed(timerRunnable, 100);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_video_album_detail);

        back = findViewById(R.id.back);
        durationSeekBar = findViewById(R.id.duration_seek_bar);
        surfaceView = findViewById(R.id.video_surface);
        surfaceView1 = findViewById(R.id.video_surface_1);
        llyBootomBar = findViewById(R.id.lly_bottom_bar);
        ivPlayStart = findViewById(R.id.iv_play_start);
        ivPause = findViewById(R.id.iv_pause);
        tvDuration = findViewById(R.id.tv_duration);
        tvTime = findViewById(R.id.tv_time);
        tvName = findViewById(R.id.tv_name);
        avi = findViewById(R.id.avi);
        tvTips = findViewById(R.id.tv_tips);
        ivCache = findViewById(R.id.iv_cache);
        ivIconBackground = findViewById(R.id.iv_icon_background);
        ivMyAlbumDelete = findViewById(R.id.iv_myalbum_delete);

        back.setOnClickListener(v -> finish());
        ivPlayStart.setOnClickListener(v -> {
            if (mediaPlayer != null && statusHelper.getMediaStatus() == MediaStatus.PLAYING) {
//                    pause();
            } else {
                ivPlayStart.setVisibility(View.GONE);
                ivIconBackground.setVisibility(View.GONE);
                if(!isPlay){

                    play();
                }else{
                    surfaceView.setVisibility(View.VISIBLE);
                    surfaceView1.setVisibility(View.GONE);
                    playOperation();
                    isPlay = false;
                }
            }
        });
        ivPause.setOnClickListener(v -> {
            if (mediaPlayer != null && statusHelper.getMediaStatus() == MediaStatus.PLAYING) {
                pause();
            } else {
                ivPlayStart.setVisibility(View.GONE);
                ivIconBackground.setVisibility(View.GONE);
                if(!isPlay){

                    play();
                }else{
                    surfaceView.setVisibility(View.VISIBLE);
                    surfaceView1.setVisibility(View.GONE);
                    playOperation();
                    isPlay = false;
                }
            }
        });
        ivMyAlbumDelete.setOnClickListener(v -> {
            pause();
            if(!filepath.isEmpty() && new File(filepath).exists()){
                mPresenter.handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showDeleteDialog(filepath);
                    }
                },100);
            }
        });

        LogUtils.d("shulan WifiVideoLockAlbumDetailActivity------------->onCreate");
        filepath = getIntent().getStringExtra(KeyConstants.VIDEO_PIC_PATH);
        String name = getIntent().getStringExtra("NAME");

        tvName.setText(name);
        // initDate
        statusHelper = new StatusHelper(this);
        mediaPlayer = new MediaPlayerWrapper();
        mediaPlayer.setStatusHelper(statusHelper);
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mediaPlayer.setSurfaceHolder(holder);
            }
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });

        if(new File(filepath).exists()){
            avi.setVisibility(View.GONE);
            avi.hide();
            tvTips.setVisibility(View.GONE);
            // initData
            surfaceView1.setVisibility(View.GONE);
            surfaceView.setVisibility(View.VISIBLE);
            playOperation();
            isPlay = false;
            ivCache.setVisibility(View.GONE);
            isRecordSuccess = true;
            if(getIntent().getIntExtra(KeyConstants.VIDO_SHOW_DELETE,0) == 0){
                ivMyAlbumDelete.setVisibility(View.VISIBLE);
            }
        }else{
            record = (WifiVideoLockAlarmRecord) getIntent().getSerializableExtra("record");
            wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
            if(wifiSn.isEmpty()){
                finish();
                return;
            }
            path = FileTool.getVideoCacheFolder(this,wifiSn).getPath();
            wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

            avi.setVisibility(View.VISIBLE);
            avi.show();
            tvTips.setVisibility(View.VISIBLE);
            if(record.getThumbUrl()!=null && !record.getThumbUrl().isEmpty()){
                Glide.with(this).load(record.getThumbUrl())
                        .apply(new RequestOptions().dontAnimate()
                                .transform(new RotateTransformation(90f))).into(ivCache);
            }else{
                Glide.with(this).load(R.mipmap.img_video_lock_default).into(ivCache);
            }
            surfaceView1.setVisibility(View.VISIBLE);
            surfaceView.setVisibility(View.GONE);
            isPlay = true;
            isRecordSuccess = false;
            realTimeOperation();
            ivCache.setVisibility(View.VISIBLE);
            ivMyAlbumDelete.setVisibility(View.GONE);
        }

    }

    private void realTimeOperation() {
        if(wifiLockInfo!=null){
            ivPlayStart.setVisibility(View.GONE);
            llyBootomBar.setVisibility(View.GONE);
            mPresenter.settingDevice(wifiLockInfo);

            if(wifiLockInfo.getKeep_alive_status() ==1){

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.setStartTime();
                        mPresenter.connectP2P();
                    }
                }).start();
            }else{
                new Handler().postDelayed(new Runnable() {
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
    }

    //播放操作
    private void playOperation() {
        if (filepath == null) {
            ToastUtils.showShort(getResources().getString(R.string.no_video));
            return;
        }
        mediaPlayer.setMediaPlayerFromUri(filepath);
        statusHelper.setMediaStatus(MediaStatus.START);
        mediaPlayer.prepare();
        initEvent();
        autoPlayVideo();
    }

    private void initEvent() {
        ivPlayStart.setVisibility(View.GONE);
        mediaPlayer.setPlayerCallback(new MediaPlayerWrapper.PlayerCallback() {
            @Override
            public void updateProgress() {

            }

            @Override
            public void updateInfo() {
                initSeekBar();
            }

            @Override
            public void requestFinish() {
                reset();
            }
        });
    }

    //更新进度
    private void autoPlayVideo() {
        timerHandler.post(timerRunnable);
    }

    private void updateProgress() {
        if (statusHelper.getMediaStatus() == MediaStatus.PLAYING && mediaPlayer.getCurrentPosition() > 0) {
            int duration = mediaPlayer.getCurrentPosition();
            durationSeekBar.setProgress(duration);
            updateStartValueView(duration);
        }
    }

    private void updateStartValueView(int progress) {
        Date date = new Date(progress);
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        final String formatted = formatter.format(date);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTime.setText(formatted);
            }
        });
    }

    private void initSeekBar() {
        long videoDuration = mediaPlayer.getDuration();

        if (videoDuration == 0) {
            ToastUtils.showShort(getString(R.string.philips_album_not_find_video));
            finish();
        }
        durationSeekBar.setProgress(0);
        durationSeekBar.setMax((int) videoDuration);
        Date date = new Date(videoDuration);
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        String formatted = formatter.format(date);
        tvDuration.setText(formatted);

        durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mediaPlayer != null) {
                        mediaPlayer.seekTo(progress);
                    }
                    updateStartValueView(progress);
                } else if (progress >= seekBar.getMax()) {
                    reset();
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerBroadcast();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.d("shulan WifiVideoLockAlbumDetailActivity------------->onDestroy");
        stopRepeatTimer();
        if (mediaPlayer != null) {
            mediaPlayer.releaseResource();
        }

        unRegisterBroadcast();
    }


    @Override
    public void finish() {
        super.finish();
        LogUtils.d("shulan WifiVideoLockAlbumDetailActivity------------->finish");
        mPresenter.release();

        try{
            if(!isRecordSuccess){
                if(!filepath.isEmpty()){
                    if(new File(filepath).exists()){
                        new File(filepath).delete();
                    }
                }
            }
        }catch (Exception e){

        }

    }

    @Override
    protected MyAlbumPlayerPresenter<IMyAlbumPlayerView> createPresent() {
        return new MyAlbumPlayerPresenter<>();
    }

    void startRepeatTimer() {
        timerRunnable.run();
    }

    void stopRepeatTimer() {
        timerHandler.removeCallbacks(timerRunnable);
    }

    private void reset() {
        ivIconBackground.setVisibility(View.VISIBLE);
        ivPlayStart.setVisibility(View.VISIBLE);
//        pause();
        updateStartValueView(0);
        durationSeekBar.setProgress(0);
        updateButtonController();
    }

    private void updateButtonController() {
        final boolean isPlaying = statusHelper.getMediaStatus() == MediaStatus.PLAYING;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ivPause.setSelected(!isPlaying);
            }
        });
    }

    public void pause() {
        stopRepeatTimer();
        if (mediaPlayer != null && statusHelper.getMediaStatus() == MediaStatus.PLAYING) {
            mediaPlayer.pauseByUser();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivPlayStart.setVisibility(View.VISIBLE);
                    ivIconBackground.setVisibility(View.VISIBLE);
                }
            });
            updateButtonController();
        }
    }

    public void play() {
        startRepeatTimer();
        boolean isPlaying = statusHelper.getMediaStatus() == MediaStatus.PLAYING;
        if (mediaPlayer != null && !isPlaying) {
            mediaPlayer.start();
            updateButtonController();
        }
    }

    private void showDeleteDialog(String filepath) {
        AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(
                PhilipsWifiVideoLockAlbumDetailActivity.this
                , getString(R.string.dialog_wifi_video_delete_video) + "",
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#0066A1", "#FFFFFF", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        Intent intent = new Intent(PhilipsWifiVideoLockAlbumDetailActivity.this, PhilipsWifiVideoLockAlbumActivity.class);
                        intent.putExtra(KeyConstants.VIDEO_PIC_PATH,filepath);
                        intent.putExtra("NAME",tvName.getText().toString());
                        setResult(RESULT_OK,intent);
                        finish();
                        if(!filepath.isEmpty()){
                            if(new File(filepath).exists()){
                                new File(filepath).delete();
                            }
                        }
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
    public void onConnectFailed(int paramInt) {
        LogUtils.d("shulan onConnectFailed-->" + paramInt);
        mPresenter.handler.post(new Runnable() {
            @Override
            public void run() {
                if(!PhilipsWifiVideoLockAlbumDetailActivity.this.isFinishing()){
                    if(avi != null){
                        avi.hide();
                    }
                    if(tvTips != null)
                        tvTips.setVisibility(View.GONE);
                    String errorStringWithCode = XMP2PConnectError.checkP2PErrorStringWithCode(PhilipsWifiVideoLockAlbumDetailActivity.this,paramInt);
                    creteDialog(errorStringWithCode + "");
                }

            }
        });
    }

    @Override
    public void onConnectSuccess() {
        XMP2PManager.getInstance().setSurfaceView(surfaceView1);
        mPresenter.playDeviceRecordVideo(record,path);
    }

    @Override
    public void onStartConnect(String paramString) {

    }

    @Override
    public void onErrorMessage(String message) {

    }

    @Override
    public void onVideoDataAVStreamHeader(AVStreamHeader paramAVStreamHeader) {

    }

    @Override
    public void onVideoFrameUsed(H264Frame h264Frame) {
        if(h264Frame.getFrameTimeStamp() == 0){
            LogUtils.d("shulan onVideoFrameUsed--------->h264Frame.getFrameTimeStamp() == 0");
            isRecordSuccess = true;
//            mPresenter.release();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ivPlayStart.setVisibility(View.VISIBLE);
                    llyBootomBar.setVisibility(View.VISIBLE);
//                    playOperation();
                }
            });
        }else{
            isRecordSuccess = false;
        }
    }

    @Override
    public void onStopRecordMP4CallBack(MP4Info mp4Info, String name) {
        if(mp4Info.isResult()){
            filepath = mp4Info.getFilePath();

            if(PhilipsWifiVideoLockAlbumDetailActivity.this.isFinishing() && !isRecordSuccess){
                try {
                    if(!mp4Info.getFilePath().isEmpty()){

                        File file = new File(mp4Info.getFilePath());
                        if(file.exists()){
                            file.delete();
                        }
                    }
                }catch (Exception e){

                }
            }
        }else {

            try {
                if(!mp4Info.getFilePath().isEmpty()){
                    File file = new File(mp4Info.getFilePath());
                    if(file.exists()){
                        file.delete();
                    }
                }
            }catch (Exception e){

            }

        }


    }

    @Override
    public void onstartRecordMP4CallBack() {
        mPresenter.handler.post(new Runnable() {
            @Override
            public void run() {
                avi.hide();
                tvTips.setVisibility(View.GONE);
                ivCache.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onSuccessRecord(boolean b) {
        if(!b){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort(getString(R.string.wifi_video_lock_find_file_show_toast) + "");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },500);

                }
            });
        }
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
                finish();
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
                        mPresenter.setStartTime();
                        mPresenter.connectP2P();
                    }
                }).start();
            }
        });

        if(!PhilipsWifiVideoLockAlbumDetailActivity.this.isFinishing()){
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
                        try {
                            if(!isPlay)
                                pause();
                        }catch (Exception e){

                        }
                        try{
                            if(!isRecordSuccess){
                                if(!filepath.isEmpty()){

                                    if(new File(filepath).exists()){
                                        new File(filepath).delete();
                                    }
                                }
                            }
                        }catch (Exception e){

                        }

                        mPresenter.release();
                    } else if (reason.equals(SYSTEM_DIALOG_REASON_RECENT_APPS)) {
                        //多任务
                        try {
                            if(!isPlay)
                                pause();
                        }catch (Exception e){

                        }
                        try{
                            if(!isRecordSuccess){
                                if(!filepath.isEmpty()){

                                    if(new File(filepath).exists()){
                                        new File(filepath).delete();
                                    }
                                }
                            }

                        }catch (Exception e){

                        }
                        mPresenter.release();
                    }
                }
            }else if(action.equals(Intent.ACTION_SCREEN_ON)){
            }else if(action.equals(Intent.ACTION_SCREEN_OFF)){
                try {
                    if(isPlay)
                        pause();
                }catch (Exception e){

                }
                try{
                    if(!isRecordSuccess){
                        if(!filepath.isEmpty()){

                            if(new File(filepath).exists()){
                                new File(filepath).delete();
                            }
                        }
                    }
                }catch (Exception e){

                }
                mPresenter.release();
            }else if(action.equals(Intent.ACTION_USER_PRESENT)){// 解锁

            }

        }
    }

    private void showKeepAliveDialog() {
        AlertDialogUtil.getInstance().noEditTwoButtonTwoContentDialog(this, getString(R.string.dialog_wifi_video_keep_alive_close), getString(R.string.dialog_wifi_video_doorbell_outside_door),
                getString(R.string.dialog_wifi_video_waking_up_door_lock_setting), "", getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
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
}
