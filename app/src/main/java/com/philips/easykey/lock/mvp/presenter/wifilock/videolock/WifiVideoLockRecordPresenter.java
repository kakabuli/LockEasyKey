package com.philips.easykey.lock.mvp.presenter.wifilock.videolock;

import android.view.SurfaceView;

import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVideoCallingView;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVideoRecordView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PManager;
import com.philips.easykey.lock.publiclibrary.xm.bean.DeviceInfo;
import com.philips.easykey.lock.utils.LogUtils;
import com.xm.sdk.struct.stream.AVStreamHeader;
import com.xmitech.sdk.AudioFrame;
import com.xmitech.sdk.H264Frame;
import com.xmitech.sdk.MP4Info;
import com.xmitech.sdk.interfaces.AVFilterListener;
import com.xmitech.sdk.interfaces.VideoPackagedListener;

import io.reactivex.disposables.Disposable;

public class WifiVideoLockRecordPresenter<T> extends BasePresenter<IWifiLockVideoRecordView>  {

    @Override
    public void attachView(IWifiLockVideoRecordView view) {
        super.attachView(view);

    }


    @Override
    public void detachView() {
        super.detachView();
    }



}
