package com.philips.easykey.lock.activity.device.videolock;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.MyAlbumPlayerPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IMyAlbumPlayerView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.xm.sdk.struct.stream.AVStreamHeader;
import com.xmitech.sdk.H264Frame;
import com.xmitech.sdk.MP4Info;

public class WifiVideoLockDeviceRecordActivity extends BaseActivity<IMyAlbumPlayerView, MyAlbumPlayerPresenter<IMyAlbumPlayerView>>  implements IMyAlbumPlayerView {


    ImageView back;
    SeekBar durationSeekBar;
    SurfaceView surfaceView;
    LinearLayout llyBootomBar;
    ImageView ivPlayStart;
    ImageView ivPause;
    TextView tvDuration;
    TextView tvTime;
    TextView tvName;

    private Dialog dialog;

    private InnerRecevier mInnerRecevier = null;

    private String wifiSn;
    private WifiLockInfo wifiLockInfo;
    private WifiVideoLockAlarmRecord mWifiVideoLockAlarmRecord;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_video_album_detail);

        back = findViewById(R.id.back);
        durationSeekBar = findViewById(R.id.duration_seek_bar);
        surfaceView = findViewById(R.id.video_surface);
        llyBootomBar = findViewById(R.id.lly_bottom_bar);
        ivPlayStart = findViewById(R.id.iv_play_start);
        ivPause = findViewById(R.id.iv_pause);
        tvDuration = findViewById(R.id.tv_duration);
        tvTime = findViewById(R.id.tv_time);
        tvName = findViewById(R.id.tv_name);

        back.setOnClickListener(v -> finish());

        mWifiVideoLockAlarmRecord = (WifiVideoLockAlarmRecord) getIntent().getSerializableExtra(KeyConstants.WIFI_VIDEO_LOCK_ALARM_RECORD_DATA);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        if(wifiLockInfo != null){
            mPresenter.settingDevice(wifiLockInfo);
        }
        if(mWifiVideoLockAlarmRecord != null){
            tvName.setText(mWifiVideoLockAlarmRecord.getFileName() + "");
        }

        initSeekBar();


    }

    private void initSeekBar() {
        int videoDuration = mWifiVideoLockAlarmRecord.getVedioTime();

        LogUtils.d("video duration = " + videoDuration);
        if (videoDuration == 0) {
            ToastUtils.showShort(getString(R.string.philips_album_not_find_video));
            finish();
        }
        durationSeekBar.setProgress(0);
        durationSeekBar.setMax(videoDuration);

        tvDuration.setText(DateUtils.getStringTime1(videoDuration) + "");

        durationSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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
        mPresenter.attachView(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mPresenter.connectP2P();
            }
        }).start();
        registerBroadcast();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.detachView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterBroadcast();
    }

    @Override
    protected MyAlbumPlayerPresenter<IMyAlbumPlayerView> createPresent() {
        return new MyAlbumPlayerPresenter<>();
    }

    @Override
    public void finish() {
        super.finish();
        mPresenter.release();
    }

    @Override
    public void onConnectFailed(int paramInt) {
        if(!WifiVideoLockDeviceRecordActivity.this.isFinishing()){
            mPresenter.handler.post(new Runnable() {

                @Override
                public void run() {
                    if(paramInt == -3){
                        creteDialog(getString(R.string.video_lock_xm_connect_time_out_1) + "");
                    }else{
                        creteDialog(getString(R.string.video_lock_xm_connect_failed_1) + "");
                    }
                }
            });

        }
    }

    @Override
    public void onConnectSuccess() {
        /*mPresenter.playDeviceRecordVideo(mWifiVideoLockAlarmRecord.getFileName(),
                mWifiVideoLockAlarmRecord.getFileDate(),surfaceView);*/
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

    }

    @Override
    public void onStopRecordMP4CallBack(MP4Info mp4Info, String name) {

    }

    @Override
    public void onstartRecordMP4CallBack() {

    }

    @Override
    public void onSuccessRecord(boolean b) {

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


                dialog.dismiss();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mPresenter.connectP2P();
                    }
                }).start();
            }
        });

        if(!WifiVideoLockDeviceRecordActivity.this.isFinishing()){
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


}
