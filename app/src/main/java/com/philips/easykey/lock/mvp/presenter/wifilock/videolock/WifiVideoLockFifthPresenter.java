package com.philips.easykey.lock.mvp.presenter.wifilock.videolock;

import android.util.Log;
import android.view.SurfaceView;

import com.google.gson.Gson;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockRealTimeVideoView;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVideoFifthView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.WifiLockVideoBindBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PManager;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.xmitech.sdk.AudioFrame;
import com.xmitech.sdk.H264Frame;
import com.xmitech.sdk.interfaces.AVFilterListener;

import org.json.JSONObject;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WifiVideoLockFifthPresenter<T> extends BasePresenter<IWifiLockVideoFifthView> {
    private String WiFiSn;
    private Disposable getDeviceBindingDisposable;

    private boolean isBinding = false;

    @Override
    public void attachView(IWifiLockVideoFifthView view) {
        super.attachView(view);
        getDeviceBindingStatus();

    }

    @Override
    public void detachView() {
        super.detachView();
    }

    /**获取mqtt绑定信息
     *
     */
    public void getDeviceBindingStatus(){
        LogUtils.e("ssssssssssssssssssssss");
        if(mqttService != null){
            toDisposable(getDeviceBindingDisposable);
            getDeviceBindingDisposable = mqttService.listenerDataBack()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {

                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if(mqttData != null){
                                if(mqttData.getFunc().equals(MqttConstant.FUNC_WFEVENT)){

                                    WifiLockVideoBindBean mWifiLockVideoBindBean = new Gson().fromJson(mqttData.getPayload(),WifiLockVideoBindBean.class);

                                    if(mWifiLockVideoBindBean.getEventparams() != null && mWifiLockVideoBindBean.getEventtype().equals(MqttConstant.WIFI_VIDEO_BINDINFO_NOTIFY)){
                                        if (isSafe())
                                            mViewRef.get().onDeviceBinding(mWifiLockVideoBindBean);
                                    }
                                }

                            }



                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
            compositeDisposable.add(getDeviceBindingDisposable);
        }

    }
}
