package com.philips.easykey.lock.activity.cateye;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.mvp.presenter.SnapPresenter;
import com.philips.easykey.lock.mvp.view.ISnapShotView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.FtpEnable;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.SPUtils2;
import com.philips.easykey.lock.utils.ftp.FtpException;
import com.philips.easykey.lock.utils.ftp.FtpUtils;
import com.philips.easykey.lock.utils.ftp.GeTui;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordingPreviewActivity extends BaseAddToApplicationActivity implements  View.OnClickListener{

    @BindView(R.id.recording_preview_img)
    PhotoView recording_preview_img;
    @BindView(R.id.iv_back)
    ImageView iv_back;

    @BindView(R.id.tv_content)
    TextView tv_content;

    String path=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording_preview);
        ButterKnife.bind(this);
        tv_content.setText(getResources().getString(R.string.screen_img));
        path =getIntent().getStringExtra("path");
        // 启用图片缩放功能
        recording_preview_img.enable();

        iv_back.setOnClickListener(this);

        File imgPathFile=new File(path);

        if(imgPathFile.exists()){
            Glide.with(this).load(path).into(recording_preview_img);
        }

    }




    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
