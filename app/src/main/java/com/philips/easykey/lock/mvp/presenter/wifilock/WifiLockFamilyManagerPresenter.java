package com.philips.easykey.lock.mvp.presenter.wifilock;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockFamilyManagerView;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class WifiLockFamilyManagerPresenter<T> extends BasePresenter<IWifiLockFamilyManagerView> {

    public void queryUserList(String wifiSN) {
        XiaokaiNewServiceImp.wifiLockGetShareUserList(wifiSN, MyApplication.getInstance().getUid())
                .subscribe(new BaseObserver<WifiLockShareResult>() {
                    @Override
                    public void onSuccess(WifiLockShareResult wifiLockShareResult) {
                        List<WifiLockShareResult.WifiLockShareUser> shareUsers = wifiLockShareResult.getData();
                        Gson gson = new Gson();
                        SPUtils.put(KeyConstants.WIFI_LOCK_SHARE_USER_LIST + wifiSN, gson.toJson(shareUsers));
                        if (isSafe()) {
                            mViewRef.get().querySuccess(shareUsers);
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {
                            mViewRef.get().queryFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().queryFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }


}
