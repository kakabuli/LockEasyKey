package com.philips.easykey.lock.mvp.presenter.wifilock;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockDetailView;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IPhilipsWifiVideoLockDetailView;
import com.philips.easykey.lock.publiclibrary.bean.WiFiLockPassword;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockGetPasswordListResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockVideoBindResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.List;

import io.reactivex.disposables.Disposable;

public class PhilipsWifiVideoLockDetailPresenter<T> extends BasePresenter<IPhilipsWifiVideoLockDetailView> {

    public void getPasswordList(String wifiSn) {
        XiaokaiNewServiceImp.wifiLockGetPwdList(wifiSn, MyApplication.getInstance().getUid())
                .subscribe(new BaseObserver<WifiLockGetPasswordListResult>() {
                    @Override
                    public void onSuccess(WifiLockGetPasswordListResult wifiLockGetPasswordListResult) {
                        WiFiLockPassword wiFiLockPassword = wifiLockGetPasswordListResult.getData();
                        SPUtils.put(KeyConstants.WIFI_LOCK_PASSWORD_LIST + wifiSn, new Gson().toJson(wiFiLockPassword));
                        if (isSafe()) {
                            mViewRef.get().onGetPasswordSuccess(wiFiLockPassword);
                        }
                    }
                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {
                            mViewRef.get().onGetPasswordFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().onGetPasswordFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }

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

    public void deleteVideoDevice(String wifiSn){
        XiaokaiNewServiceImp.wifiVideoLockUnbind(wifiSn, MyApplication.getInstance().getUid())
                .subscribe(new BaseObserver<WifiLockVideoBindResult>() {
                    @Override
                    public void onSuccess(WifiLockVideoBindResult wifiLockVideoBindResult) {
                        MyApplication.getInstance().getAllDevicesByMqtt(true);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_ALARM_RECORD + wifiSn);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_OPERATION_RECORD + wifiSn);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_OPEN_COUNT + wifiSn);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_SHARE_USER_LIST + wifiSn);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_PASSWORD_LIST + wifiSn);
                        SPUtils.remove(KeyConstants.WIFI_VIDEO_LOCK_VISITOR_RECORD + wifiSn);
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

    public void deleteDevice(String wifiSn) {
        XiaokaiNewServiceImp.wifiLockUnbind(wifiSn, MyApplication.getInstance().getUid())
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult baseResult) {
                        MyApplication.getInstance().getAllDevicesByMqtt(true);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_ALARM_RECORD + wifiSn);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_OPERATION_RECORD + wifiSn);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_OPEN_COUNT + wifiSn);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_SHARE_USER_LIST + wifiSn);
                        SPUtils.remove(KeyConstants.WIFI_LOCK_PASSWORD_LIST + wifiSn);
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
