package com.philips.easykey.lock.mvp.presenter.wifilock;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockAuthView;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;

import io.reactivex.disposables.Disposable;

public class WifiLockAuthPresenter<T> extends BasePresenter<IWifiLockAuthView> {

    public void deleteDevice(String wifiSn) {
        XiaokaiNewServiceImp.wifiLockUnbind(wifiSn, MyApplication.getInstance().getUid())
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult baseResult) {
                        MyApplication.getInstance().getAllDevicesByMqtt(true);
                        if (isSafe()) {
                            mViewRef.get().onDeleteDeviceSuccess();
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {
                            mViewRef.get().onDeleteDeviceFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().onDeleteDeviceFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }
}
