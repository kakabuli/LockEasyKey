package com.philips.easykey.lock.mvp.presenter.wifilock.videolock;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IPhilipsSettingDuressAlarm;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.postbean.SettingPwdDuressAccountBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.SettingPwdDuressAlarmBean;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;

import io.reactivex.disposables.Disposable;

public class PhilipsSettingDuressAlarmPresenter<T> extends BasePresenter<IPhilipsSettingDuressAlarm> {
    private Disposable duressPwdAccountDisposable;
    private Disposable duressPwdAlarmDisposable;
    private Disposable duressSwitchDisposable;

    public void setDuressPwdAccount(String wifiSN,int pwdType,int num,int accountType,String duressAccount){
        SettingPwdDuressAccountBean mSettingPwdDuressAccountBean = new SettingPwdDuressAccountBean(MyApplication.getInstance().getUid(),wifiSN,
                pwdType,num,accountType,duressAccount);
        toDisposable(duressPwdAccountDisposable);
        XiaokaiNewServiceImp.wifiPwdDuressAccount(mSettingPwdDuressAccountBean).subscribe(new BaseObserver<BaseResult>() {
            @Override
            public void onSuccess(BaseResult baseResult) {
                if(isSafe()){
                    mViewRef.get().onSettingDuressAccount(baseResult);
                }
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {
                if(isSafe()){
                    mViewRef.get().onSettingDuressAccount(baseResult);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {

            }

            @Override
            public void onSubscribe1(Disposable d) {
                duressPwdAccountDisposable = d;
            }
        });
    }


    public void setDuressPwdAlarm(String wifiSN,int pwdType,int num,int pwdDuressSwitch){
        SettingPwdDuressAlarmBean mSettingPwdDuressAlarmBean =  new SettingPwdDuressAlarmBean(MyApplication.getInstance().getUid(),wifiSN,
                pwdType,num,pwdDuressSwitch);
        toDisposable(duressPwdAlarmDisposable);
        XiaokaiNewServiceImp.wifiPwdDuressAlarm(mSettingPwdDuressAlarmBean).subscribe(new BaseObserver<BaseResult>() {
            @Override
            public void onSuccess(BaseResult baseResult) {
                if(isSafe()){
                    mViewRef.get().onSettingDuress(baseResult);
                }
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {
                if(isSafe()){
                    mViewRef.get().onSettingDuress(baseResult);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {

            }

            @Override
            public void onSubscribe1(Disposable d) {
                duressPwdAlarmDisposable = d;
            }
        });
    }


}
