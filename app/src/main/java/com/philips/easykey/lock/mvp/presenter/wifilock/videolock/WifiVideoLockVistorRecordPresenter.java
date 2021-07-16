package com.philips.easykey.lock.mvp.presenter.wifilock.videolock;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockAlarmRecordView;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVistorRecordView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiLockAlarmRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiVideoLockAlarmRecordResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiVideoLockDoorbellScreenedRecordResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PManager;
import com.philips.easykey.lock.publiclibrary.xm.bean.DeviceInfo;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.xm.sdk.struct.stream.AVStreamHeader;
import com.xmitech.sdk.MP4Info;
import com.xmitech.sdk.interfaces.VideoPackagedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

public class WifiVideoLockVistorRecordPresenter<T> extends BasePresenter<IWifiLockVistorRecordView> {


    public void getWifiVideoLockGetAlarmList(int page, String wifiSn) {
        XiaokaiNewServiceImp.wifiVideoLockGetDoorbellList(wifiSn,page)
                .timeout(10 *1000, TimeUnit.MILLISECONDS)
                .subscribe(new BaseObserver<GetWifiVideoLockAlarmRecordResult>() {
            @Override
            public void onSuccess(GetWifiVideoLockAlarmRecordResult getWifiVideoLockAlarmRecordResult) {
                List<WifiVideoLockAlarmRecord> alarmRecords = getWifiVideoLockAlarmRecordResult.getData();
                if (alarmRecords != null && alarmRecords.size() > 0) {
                    if (page == 1) {
                        String object = new Gson().toJson(alarmRecords);
                        SPUtils.put(KeyConstants.WIFI_VIDEO_LOCK_VISITOR_RECORD + wifiSn, object);
                    }

                    if (isSafe()) {
                        mViewRef.get().onLoadServerRecord(alarmRecords, page);
                    }
                } else {
                    if (page == 1) {
                        SPUtils.put(KeyConstants.WIFI_VIDEO_LOCK_VISITOR_RECORD + wifiSn, "");
                    }
                    if (isSafe()) {//服务器没有数据  提示用户

                        if (page == 1) { //第一次获取数据就没有
                            mViewRef.get().onServerNoData();
                        } else {
                            mViewRef.get().noMoreData();
                        }
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
                if (isSafe()) {  //
                    mViewRef.get().onLoadServerRecordFailed(throwable);
                }
            }

            @Override
            public void onSubscribe1(Disposable d) {
                compositeDisposable.add(d);
            }
        });

    }

    public void getWifiVideoLockGetDoorbellFilterList(int page, String wifiSn,long startTime,long endTime) {
        XiaokaiNewServiceImp.wifiVideoLockGetDoorbellFilterList(wifiSn,page, startTime, endTime)
                .timeout(10 *1000, TimeUnit.MILLISECONDS)
                .subscribe(new BaseObserver<GetWifiVideoLockDoorbellScreenedRecordResult>() {
                    @Override
                    public void onSuccess(GetWifiVideoLockDoorbellScreenedRecordResult getWifiVideoLockDoorbellScreenedRecordResult) {
                        List<WifiVideoLockAlarmRecord> doorbellScreenedRecord = getWifiVideoLockDoorbellScreenedRecordResult.getData().getDoorbellList();
                        if (doorbellScreenedRecord != null && doorbellScreenedRecord.size() > 0) {
                            if (isSafe()) {
                                mViewRef.get().onLoadServerRecord(doorbellScreenedRecord, page);
                            }
                        } else {
                            if (isSafe()) {//服务器没有数据  提示用户

                                if (page == 1) { //第一次获取数据就没有
                                    mViewRef.get().onServerNoData();
                                } else {
                                    mViewRef.get().noMoreData();
                                }
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
                        if (isSafe()) {  //
                            mViewRef.get().onLoadServerRecordFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });

    }

/*    private void removeGroupData() {
        for(int i = 0 ; i < wifiVideoLockAlarmRecords.size();i++){
//            String id = list.get(i).get_id();
            for(int j = wifiVideoLockAlarmRecords.size() - 1 ; j > i; j--){
                if(wifiVideoLockAlarmRecords.get(i).get_id() == wifiVideoLockAlarmRecords.get(j).get_id()){
                    wifiVideoLockAlarmRecords.remove(j);
                }
            }
        }
    }*/
}
