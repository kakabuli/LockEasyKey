package com.philips.easykey.lock.mvp.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.IDoorLockMessageView;
import com.philips.easykey.lock.mvp.view.IMessageView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetOpenCountResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetStatisticsDayResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetStatisticsSevenDayResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiVideoLockAlarmScreenedRecordResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DoorLockMessageFragmentPresenter<T> extends BasePresenter<IDoorLockMessageView> {

    private Disposable listenActionUpdateDisposable;
    private Disposable listenerAllDevicesDisposable;
    private String wifiSN;

    @Override
    public void attachView(IDoorLockMessageView view) {
        super.attachView(view);
        listenActionUpdate();
        listenerAllDevices();
    }

    public void setWifiSN(String wifiSN){
        this.wifiSN = wifiSN;
    }

    private void listenerAllDevices(){
        toDisposable(listenerAllDevicesDisposable);
        listenerAllDevicesDisposable = MyApplication.getInstance().listenerAllDevices()
                .subscribe(new Consumer<AllBindDevices>() {
                    @Override
                    public void accept(AllBindDevices allBindDevices) throws Exception {
                        if (isSafe()) {
                            mViewRef.get().onDeviceRefresh(allBindDevices);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        compositeDisposable.add(listenerAllDevicesDisposable);
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

    public void getDoorLockDtatisticsDay(String uid,String wifiSN){
        XiaokaiNewServiceImp.getDoorLockDtatisticsDay(uid,wifiSN)
                .subscribe(new BaseObserver<GetStatisticsDayResult>() {
                    @Override
                    public void onSuccess(GetStatisticsDayResult getStatisticsDayResult) {
                        if (isSafe()) {
                            mViewRef.get().getDtatisticsDay(getStatisticsDayResult);
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        LogUtils.d("getDoorLockDtatisticsDay onAckErrorCode = "  + baseResult.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        LogUtils.d("getDoorLockDtatisticsDay onFailed = "  + throwable.toString());
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

    public void getDoorLockDtatisticsSevenDay(String uid,String wifiSN){
        XiaokaiNewServiceImp.getDoorLockDtatisticsSevenDay(uid,wifiSN)
                .subscribe(new BaseObserver<GetStatisticsSevenDayResult>() {
                    @Override
                    public void onSuccess(GetStatisticsSevenDayResult getStatisticsSevenDayResult) {
                        if (isSafe()) {
                            mViewRef.get().getDtatisticsSevenDay(getStatisticsSevenDayResult);
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        mViewRef.get().getDtatisticsSevenDayFailed();
                        LogUtils.d("getDoorLockDtatisticsSevenDay onAckErrorCode = "  + baseResult.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        mViewRef.get().getDtatisticsSevenDayFailed();
                        LogUtils.d("getDoorLockDtatisticsSevenDay onFailed = "  + throwable.toString());
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

    public void getWifiVideoLockGetAlarmFilterList(int page, String wifiSn,long startTime,long endTime) {
        XiaokaiNewServiceImp.wifiVideoLockGetAlarmFilterList(wifiSn,page,startTime,endTime)
                .timeout(10 *1000, TimeUnit.MILLISECONDS)
                .subscribe(new BaseObserver<GetWifiVideoLockAlarmScreenedRecordResult>() {
                    @Override
                    public void onSuccess(GetWifiVideoLockAlarmScreenedRecordResult getWifiVideoLockAlarmScreenedRecordResult) {
                        mViewRef.get().getWifiVideoLockAlarm(getWifiVideoLockAlarmScreenedRecordResult);
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        mViewRef.get().getWifiVideoLockAlarmFailed();
                        LogUtils.d("getWifiVideoLockGetAlarmFilterList onAckErrorCode = "  + baseResult.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        mViewRef.get().getWifiVideoLockAlarmFailed();
                        LogUtils.d("getWifiVideoLockGetAlarmFilterList onFailed = "  + throwable.toString());
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }

    public void getOpenCount(String wifiSn) {
        XiaokaiNewServiceImp.wifiLockGetOpenCount(wifiSn)
                .subscribe(new BaseObserver<GetOpenCountResult>() {
                    @Override
                    public void onSuccess(GetOpenCountResult getOpenCountResult) {
                        int count = getOpenCountResult.getData().getCount();
                        SPUtils.put(KeyConstants.WIFI_LOCK_OPEN_COUNT + wifiSn, count);
                        if (isSafe()) {
                            mViewRef.get().getOpenCountSuccess(count);
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        LogUtils.d("getOpenCount onAckErrorCode = "  + baseResult.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        LogUtils.d("getOpenCount onFailed = "  + throwable.toString());
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });

    }
}
