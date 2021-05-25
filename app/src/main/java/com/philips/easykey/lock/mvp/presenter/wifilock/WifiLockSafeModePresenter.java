package com.philips.easykey.lock.mvp.presenter.wifilock;

import android.text.TextUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockSafeModeView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.blankj.utilcode.util.LogUtils;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class WifiLockSafeModePresenter<T> extends BasePresenter<IWifiLockSafeModeView> {
    private String WiFiSn;
    private Disposable listenActionUpdateDisposable;

    public void init(String wiFiSn){
        WiFiSn = wiFiSn;
        listenActionUpdate();
    }

    private void listenActionUpdate(){
        toDisposable(listenActionUpdateDisposable);
        listenActionUpdateDisposable = MyApplication.getInstance().listenerWifiLockAction()
                  .subscribe(new Consumer<WifiLockInfo>() {
                      @Override
                      public void accept(WifiLockInfo wifiLockInfo) throws Exception {
                          if (wifiLockInfo!=null &&!TextUtils.isEmpty( wifiLockInfo.getWifiSN())){
                              if ( wifiLockInfo.getWifiSN().equals(WiFiSn) && isSafe()){
                                  mViewRef.get().onWifiLockActionUpdate();
                              }
                          }

                      }
                  });

        compositeDisposable.add(listenActionUpdateDisposable);

    }

}
