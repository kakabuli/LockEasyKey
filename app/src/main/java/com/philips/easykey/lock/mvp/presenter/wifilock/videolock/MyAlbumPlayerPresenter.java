package com.philips.easykey.lock.mvp.presenter.wifilock.videolock;

import android.content.Context;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.widget.RelativeLayout;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IMyAlbumPlayerView;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVideoFifthView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PConnectError;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PConnectJsonError;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PManager;
import com.philips.easykey.lock.publiclibrary.xm.bean.DeviceInfo;
import com.philips.easykey.lock.utils.LogUtils;
import com.xiaomi.channel.commonutils.logger.LoggerInterface;
import com.xm.sdk.struct.stream.AVStreamHeader;
import com.xmitech.sdk.AudioFrame;
import com.xmitech.sdk.H264Frame;
import com.xmitech.sdk.MP4Info;
import com.xmitech.sdk.interfaces.AVFilterListener;
import com.xmitech.sdk.interfaces.VideoPackagedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class MyAlbumPlayerPresenter<T> extends BasePresenter<IMyAlbumPlayerView> {


    private static  String did ="";//AYIOTCN-000337-FDFTF
    private static  String sn ="";//010000000020500020

    private static  String p2pPassword ="";//ut4D0mvz

    private static  String serviceString=XMP2PManager.serviceString;;

    private int times = 4;

    private static final long OVER_TIME_SECONDS = 30000;
    private static final int OVER_TIME_TIMES = 10;
    private long startTime = 0;
    private int connectTimes = 0;

    private Handler postHandler = new Handler();

    @Override
    public void attachView(IMyAlbumPlayerView view) {
        super.attachView(view);

    }

    @Override
    public void detachView() {
        super.detachView();

    }

    public int connectP2P(){

        DeviceInfo deviceInfo=new DeviceInfo();
        deviceInfo.setDeviceDid(did);
        deviceInfo.setP2pPassword(p2pPassword);
        deviceInfo.setDeviceSn(sn);
        deviceInfo.setServiceString(serviceString);
        XMP2PManager.getInstance().setOnConnectStatusListener(listener);
        int param = XMP2PManager.getInstance().connectDevice(deviceInfo);

        return param;
    }

    XMP2PManager.ConnectStatusListener listener = new XMP2PManager.ConnectStatusListener() {
        @Override
        public void onConnectFailed(int paramInt) {
            LogUtils.e("shulan onConnectFailed------------------->" + paramInt);
            XMP2PManager.getInstance().stopCodec();//
            if(paramInt == XMP2PConnectError.XM_DYNAMIC_LIBRARY_NOT_INITIALIZED){
                XMP2PManager.getInstance().initAPI(MyApplication.getInstance(),serviceString);
                XMP2PManager.getInstance().init(MyApplication.getInstance());
            }
            if((startTime > 0 && System.currentTimeMillis() - startTime > OVER_TIME_SECONDS) || connectTimes > OVER_TIME_TIMES){

                if(isSafe()){
                    mViewRef.get().onConnectFailed(paramInt);
                }
            }else{
                connectTimes++;
                connectP2P();
            }

        }

        @Override
        public void onConnectSuccess() {
            if(isSafe()){
                mViewRef.get().onConnectSuccess();
            }

        }

        @Override
        public void onStartConnect(String paramString) {
            if(isSafe()){
                mViewRef.get().onStartConnect(paramString);
            }

        }

        @Override
        public void onErrorMessage(String message) {
//            stopConnect();
            if(isSafe()){
                mViewRef.get().onErrorMessage(message);
            }

        }

        @Override
        public void onNotifyGateWayNewVersion(String paramString) {

        }

        @Override
        public void onRebootDevice(String paramString) {

        }
    };

    public void settingDevice(WifiLockInfo wifiLockInfo) {
        did = wifiLockInfo.getDevice_did();
        sn = wifiLockInfo.getDevice_sn();
        p2pPassword = wifiLockInfo.getP2p_password();

    }

    public void release(){
        XMP2PManager.getInstance().stopConnect();//
        XMP2PManager.getInstance().stopCodec();
        this.startTime = 0;
        this.connectTimes = 0;
    }

    public void stopConnect(){
        XMP2PManager.getInstance().stopCodec();

    }

    long time=0;
    int n = 0 ;

    public void playDeviceRecordVideo(WifiVideoLockAlarmRecord record,String path){
        XMP2PManager.getInstance().setRotate(XMP2PManager.SCREEN_ROTATE);
        try {
            XMP2PManager.getInstance().setAudioFrame();
        }catch (java.lang.NegativeArraySizeException e){

        }
        XMP2PManager.getInstance().setOnAudioVideoStatusLinstener(new XMP2PManager.AudioVideoStatusListener() {
            @Override
            public void onVideoDataAVStreamHeader(AVStreamHeader paramAVStreamHeader) {
            }
        });

        XMP2PManager.getInstance().setAVFilterListener(new AVFilterListener() {
            @Override
            public void onAudioRecordData(AudioFrame audioFrame) {

            }

            @Override
            public void onVideoFrameUsed(H264Frame h264Frame) {
                LogUtils.e("shulan onVideoFrameUsed------------->" +h264Frame.frameTimeStamp);
                if(h264Frame.frameTimeStamp - time >= 1000){

                    LogUtils.e("shulan onVideoFrameUsed"+"FrameTimeStamp = " + h264Frame.frameTimeStamp + "--FrameRate = " + n);
                    n = 0;
                    time = h264Frame.frameTimeStamp;
                }else{
                    n++;
                }
                if(isSafe()){
                    mViewRef.get().onVideoFrameUsed(h264Frame);
                }
            }

            @Override
            public void onAudioFrameUsed(AudioFrame audioFrame) {
                LogUtils.e("shulan onAudioFrameUsed------------->" + audioFrame.frameTimeStamp);
            }

            @Override
            public void onLastFrameRgbData(int[] ints, int i, int i1, boolean b) {

            }

            @Override
            public void onCodecNotify(int i, Object o) {

            }
        });

        startRecordMP4(path +  File.separator + record.get_id() + ".mp4",record.getStartTime() + "");
        int ret = XMP2PManager.getInstance().playDeviceRecordVideo(record.getFileDate() ,record.getFileName() ,0,0);

        XMP2PManager.getInstance().play();
        XMP2PManager.getInstance().setAECM(false);
        XMP2PManager.getInstance().enableAudio(true);
        XMP2PManager.getInstance().setOnPlayDeviceRecordVideo(new XMP2PManager.PlayDeviceRecordVideo() {
            @Override
            public void onPlayDeviceRecordVideoProcResult(JSONObject jsonObject) {
                LogUtils.e("shulan onPlayDeviceRecordVideoProcResult------->" + jsonObject.toString());
                try {
                    if(jsonObject.getInt("errno") == XMP2PConnectJsonError.XM_JSON_ERROR_T21_INITIALIZED && times>0){
                        postHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                LogUtils.e("shulan 116");
                                playDeviceRecordVideo(record,path);
                            }
                        },2000);
                        times--;
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if(jsonObject.getString("result").equals("ok")){
//                        startRecordMP4(path +  File.separator + id + ".mp4");
                        if(isSafe()){
                            LogUtils.e("shulan onPlayDeviceRecordVideoProcResult------>onSuccessRecord");
                            mViewRef.get().onSuccessRecord(true);
                        }
                    }else if(jsonObject.getString("result").equals("fail")){
                        if(isSafe()){
                            mViewRef.get().onSuccessRecord(false);
                        }
                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onPlayRecViewCtrlResult(JSONObject jsonObject) {

            }

            @Override
            public void onPushCmdRet(int cmdCode, JSONObject jsonString) {
                if(cmdCode == 101){
                    stopRecordMP4();
                }
            }
        });
    }


    public void startRecordMP4(String filePath,String name){
        XMP2PManager.getInstance().startRecordMP4(filePath,0,0,0,XMP2PManager.SCREEN_ROTATE);
        XMP2PManager.getInstance().setVideoPackagedListener(new VideoPackagedListener() {
            @Override
            public void onStartedPackaged() {

                if(isSafe()){
                    mViewRef.get().onstartRecordMP4CallBack();
                }
            }

            @Override
            public void onStopPackaged(MP4Info mp4Info) {

                if(isSafe()){
                    mViewRef.get().onStopRecordMP4CallBack(mp4Info,name);
                }
            }
        });
    }

    public void stopRecordMP4(){
        XMP2PManager.getInstance().stopRecordMP4();
    }

    public void setStartTime() {
        this.startTime = System.currentTimeMillis();
        this.connectTimes = 0;
    }
}
