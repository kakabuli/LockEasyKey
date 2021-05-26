package com.philips.easykey.lock.mvp.presenter;

import android.text.TextUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.IDoorLockMessageView;
import com.philips.easykey.lock.mvp.view.IMessageView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DoorLockMessageFragmentPresenter<T> extends BasePresenter<IDoorLockMessageView> {

    private Disposable listenActionUpdateDisposable;
    private String wifiSN;

    @Override
    public void attachView(IDoorLockMessageView view) {
        super.attachView(view);
        listenActionUpdate();

    }

    public void setWifiSN(String wifiSN){
        this.wifiSN = wifiSN;
    }

    private void listenActionUpdate(){
        toDisposable(listenActionUpdateDisposable);
        listenActionUpdateDisposable = MyApplication.getInstance().listenerWifiLockAction()
                .subscribe(new Consumer<WifiLockInfo>() {
                    @Override
                    public void accept(WifiLockInfo wifiLockInfo) throws Exception {
                        if (wifiLockInfo!=null &&!TextUtils.isEmpty( wifiLockInfo.getWifiSN())){
                            if ( wifiLockInfo.getWifiSN().equals(wifiSN) && isSafe()){
                                mViewRef.get().onWifiLockActionUpdate();
                            }
                        }

                    }
                });

        compositeDisposable.add(listenActionUpdateDisposable);
    }

}
