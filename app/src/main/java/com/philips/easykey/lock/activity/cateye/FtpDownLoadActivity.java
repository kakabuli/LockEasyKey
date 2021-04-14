package com.philips.easykey.lock.activity.cateye;

import android.os.Bundle;
import android.view.View;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.g711mux;

public class FtpDownLoadActivity extends BaseAddToApplicationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp_down_load);
    }

    public void  hecheng(View view){

        String videopath="/storage/emulated/0/Android/data/com.philips.easykey.lock/files/catEyeVideos/CH01191510002/orangecat-20190507/1557239657_video.h264";
        String audiopath="/storage/emulated/0/Android/data/com.philips.easykey.lock/files/catEyeAudios/CH01191510002/orangecat-20190507/1557239657_audio.raw";
        String finalpath="/storage/emulated/0/Android/data/com.philips.easykey.lock/files/compoundVideos/CH01191510002/orangecat-20190507/1557239657.mp4";

        g711mux muxer = new g711mux();
//            muxer.mux("/storage/emulated/0/kaadas/videos/test_video.h264","/storage/emulated/0/kaadas/videos/test_audio.g711","/storage/emulated/0/kaadas/videos/3.mkv",25,8000);
    //    muxer.mux("/storage/emulated/0/kaadas/catEyeVideos/AC35EEA60F2F/1534904886_video.h264","/storage/emulated/0/kaadas/catEyeAudios/AC35EEA60F2F/1534904886_audio.raw","/storage/emulated/0/kaadas/compoundVideos/AC35EEA60F2F/1534904886.mkv",25,8000);
        muxer.mux(videopath,audiopath,finalpath,25,8000);
    }
}
