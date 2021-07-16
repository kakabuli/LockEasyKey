package com.philips.easykey.lock.mvp.presenter.wifilock;

import android.util.Log;

import com.google.gson.Gson;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockOpenRecordView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockOperationRecord;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiLockOperationRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiLockOperationScreenedRecordResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

public class WifiLockOpenRecordPresenter<T> extends BasePresenter<IWifiLockOpenRecordView> {
//    private List<WifiLockOperationRecord> wifiLockOperationRecords = new ArrayList<>();

    public void getOpenRecordFromServer(int page, String wifiSn) {
        XiaokaiNewServiceImp.wifiLockGetOperationList(wifiSn, page)
                .timeout(10 *1000, TimeUnit.MILLISECONDS)
                .subscribe(new BaseObserver<GetWifiLockOperationRecordResult>() {
                    @Override
                    public void onSuccess(GetWifiLockOperationRecordResult operationRecordResult) {
                        if (operationRecordResult.getData() != null && operationRecordResult.getData().size() > 0) {  //服务器没有数据  提示用户
                            List<WifiLockOperationRecord> operationRecords = operationRecordResult.getData();
//                            wifiLockOperationRecords.addAll(operationRecords);
                            if (page == 1) {
                                SPUtils.put(KeyConstants.WIFI_LOCK_OPERATION_RECORD + wifiSn, new Gson().toJson(operationRecords));
                            }
                            if (isSafe()) {
                                mViewRef.get().onLoadServerRecord(operationRecords, page);
                            }
                        } else {
                            if (page == 1) {
                                SPUtils.put(KeyConstants.WIFI_LOCK_OPERATION_RECORD + wifiSn, "");
                            }
                            if (isSafe()) {
                                if (page == 1) { //第一次获取数据就没有
                                    mViewRef.get().onServerNoData();
                                } else {
                                    mViewRef.get().noMoreData();
                                }
                                return;
                            }
                        }

                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {  //
                            mViewRef.get().onLoadServerRecordFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().onLoadServerRecordFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }

    public void getOpenScreenedRecordFromServer(int page, String wifiSn,long startTime, long endTime) {
        XiaokaiNewServiceImp.wifiLockGetOperationFilterList(wifiSn, page,startTime,endTime)
                .timeout(10 *1000, TimeUnit.MILLISECONDS)
                .subscribe(new BaseObserver<GetWifiLockOperationScreenedRecordResult>() {
                    @Override
                    public void onSuccess(GetWifiLockOperationScreenedRecordResult operationScreenedRecordResult) {
                        if (operationScreenedRecordResult.getData() != null && operationScreenedRecordResult.getData().getOperationList().size() > 0) {  //服务器没有数据  提示用户
                            List<WifiLockOperationRecord> operationRecords = operationScreenedRecordResult.getData().getOperationList();
//                            wifiLockOperationRecords.addAll(operationRecords);
                            if (isSafe()) {
                                mViewRef.get().onLoadServerRecord(operationRecords, page);
                            }
                        } else {
                            if (isSafe()) {
                                if (page == 1) { //第一次获取数据就没有
                                    mViewRef.get().onServerNoData();
                                } else {
                                    mViewRef.get().noMoreData();
                                }
                                return;
                            }
                        }

                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {  //
                            mViewRef.get().onLoadServerRecordFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().onLoadServerRecordFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }

}
