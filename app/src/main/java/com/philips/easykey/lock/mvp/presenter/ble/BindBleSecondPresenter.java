package com.philips.easykey.lock.mvp.presenter.ble;

import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.mvpbase.BlePresenter;
import com.philips.easykey.lock.mvp.view.IBindBleSecondView;
import com.philips.easykey.lock.publiclibrary.ble.responsebean.BleStateBean;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class BindBleSecondPresenter<T> extends BasePresenter<IBindBleSecondView> {

    private Disposable listenConnectStateDisposable;

    public void listenConnectState() {
        if(bleService != null){

            toDisposable(listenConnectStateDisposable);
            listenConnectStateDisposable = bleService.subscribeDeviceConnectState()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<BleStateBean>() {
                        @Override
                        public void accept(BleStateBean bleStateBean) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().onDeviceStateChange(bleStateBean.isConnected());
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
            compositeDisposable.add(listenConnectStateDisposable);
        }
    }

}
