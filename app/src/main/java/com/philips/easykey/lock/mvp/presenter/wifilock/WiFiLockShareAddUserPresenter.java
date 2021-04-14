package com.philips.easykey.lock.mvp.presenter.wifilock;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWiFiLockShareAddUserView;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;

import io.reactivex.disposables.Disposable;

public class WiFiLockShareAddUserPresenter<T> extends BasePresenter<IWiFiLockShareAddUserView> {


    public void addUser(String wifiSN, String uname, String userNickname,String adminNickname) {
        XiaokaiNewServiceImp.wifiLockAddShareUser(wifiSN, MyApplication.getInstance().getUid(), uname, userNickname,adminNickname)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult baseResult) {
                        if (isSafe()) {
                            mViewRef.get().onAddUserSuccess();
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {
                            mViewRef.get().onAddUserFailed(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().onAddUserFailedServer(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                }) ;
    }

}
