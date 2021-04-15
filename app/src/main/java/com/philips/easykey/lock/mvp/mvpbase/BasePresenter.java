package com.philips.easykey.lock.mvp.mvpbase;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;


import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.publiclibrary.ble.BleService;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttService;
import com.philips.easykey.lock.utils.LogUtils;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Create By lxj  on 2019/1/7
 * Describe MVP  中的BasePresenter
 */
public class BasePresenter<T extends IBaseView> {
    public Handler handler = new Handler(Looper.getMainLooper());
    protected CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MqttService mqttService = MyApplication.getInstance().getMqttService();
    protected BleService bleService = MyApplication.getInstance().getBleService();

    public BasePresenter() {
        if (mqttService == null) {
            LogUtils.d("mqttService  为空   异常情况  " + (MyApplication.getInstance().getMqttService() == null));
            mqttService = MyApplication.getInstance().getMqttService();
            LogUtils.d("mqttService  为" + mqttService);
        }
        if (bleService == null) {
            LogUtils.d("bleService  为空   异常情况  " + (MyApplication.getInstance().getBleService() == null));
            bleService = MyApplication.getInstance().getBleService();
//            MyApplication.getInstance().reStartApp();
            LogUtils.d("bleService 为 " + bleService);
        }
    }

    /**
     * 弱引用
     * T Activity 中UI操作接口
     */
    protected WeakReference<T> mViewRef;
    protected boolean isAttach = false;

    public boolean isSafe(){
        return mViewRef != null && mViewRef.get() != null;
    }

    public void attachView(T view) {
        if (bleService == null) {
            bleService = MyApplication.getInstance().getBleService();
        }
        mViewRef = new WeakReference<T>(view);
        LogUtils.d("WeakReference=="+view);

        isAttach = true;
    }

    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
        if (compositeDisposable != null) {
            compositeDisposable.clear();
        }
        isAttach = false;
    }

    public void toDisposable(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }

}
