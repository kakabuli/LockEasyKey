package com.philips.easykey.lock.mvp.presenter.wifilock.videolock;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.bean.PhilipsDuressBean;
import com.philips.easykey.lock.bean.WifiLockActionBean;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IPhilipsWifiVideoLockDuressView;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IWifiVideoLockMoreView;
import com.philips.easykey.lock.publiclibrary.bean.WiFiLockPassword;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.postbean.SettingPwdDuressAlarmSwitchBean;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockGetPasswordListResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockVideoBindResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.SetVideoLockSafeModeResult;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.SetVideoLockVolumeResult;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PManager;
import com.philips.easykey.lock.publiclibrary.xm.bean.DeviceInfo;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class PhilipsWifiVideoLockDuressPresenter<T> extends BasePresenter<IPhilipsWifiVideoLockDuressView> {
    private Disposable duressSwitchDisposable;

    @Override
    public void attachView(IPhilipsWifiVideoLockDuressView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

    public void getPasswordList(String wifiSn) {
        XiaokaiNewServiceImp.wifiLockGetPwdList(wifiSn, MyApplication.getInstance().getUid())
                .subscribe(new BaseObserver<WifiLockGetPasswordListResult>() {
                    @Override
                    public void onSuccess(WifiLockGetPasswordListResult wifiLockGetPasswordListResult) {

                        WiFiLockPassword wiFiLockPassword = wifiLockGetPasswordListResult.getData();
                        String object = new Gson().toJson(wiFiLockPassword);
                        LogUtils.d("服务器数据是   " + object);

                        SPUtils.put(KeyConstants.WIFI_LOCK_PASSWORD_LIST + wifiSn, object);
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

    public List<PhilipsDuressBean> setWifiLockPassword(String wifisn,WiFiLockPassword wiFiLockPassword) {
        if(wiFiLockPassword == null) return null;
        List<PhilipsDuressBean> list = new ArrayList<>();
        if(wiFiLockPassword.getPwdList() != null && wiFiLockPassword.getPwdList().size() > 0){
            list.add(new PhilipsDuressBean(wifisn,1,true));
            for(WiFiLockPassword.PwdListBean bean : wiFiLockPassword.getPwdList()){
                if(bean.getType() != 0) continue;

                String sNum = bean.getNum() > 9 ? "" + bean.getNum() : "0" + bean.getNum();
                String nickName = "";
                String duressAlarmAccount = "";
                int pwdDuressSwitch = 0;
                if(wiFiLockPassword.getPwdNickname() != null && wiFiLockPassword.getPwdNickname().size() > 0)
                    for(WiFiLockPassword.PwdNicknameBean li :wiFiLockPassword.getPwdNickname()){
                        if(li.getNum() == bean.getNum()){
                            if(li.getNickName().isEmpty()){
                                nickName = sNum;
                            }else{
                                nickName = li.getNickName();
                            }
                        }
                    }

                if(wiFiLockPassword.getPwdDuress() != null && wiFiLockPassword.getPwdDuress().size() > 0){
                    for(WiFiLockPassword.DuressBean oi : wiFiLockPassword.getPwdDuress()){
                        if(oi.getNum() == bean.getNum()){
                            duressAlarmAccount = oi.getDuressAlarmAccount();
                            pwdDuressSwitch = oi.getPwdDuressSwitch();
                        }
                    }
                }

                list.add(new PhilipsDuressBean(wifisn,1,pwdDuressSwitch,duressAlarmAccount,bean.getNum(),bean.getCreateTime(),nickName));
            }
        }

        if(wiFiLockPassword.getFingerprintList() != null && wiFiLockPassword.getFingerprintList().size() > 0){
            list.add(new PhilipsDuressBean(wifisn,2,true));
            for(WiFiLockPassword.FingerprintListBean bean : wiFiLockPassword.getFingerprintList()){
                String sNum = bean.getNum() > 9 ? "" + bean.getNum() : "0" + bean.getNum();
                String nickName = "";
                String duressAlarmAccount = "";
                int pwdDuressSwitch = 0;
                if(wiFiLockPassword.getFingerprintNickname() != null && wiFiLockPassword.getFingerprintNickname().size() > 0)
                    for(WiFiLockPassword.FingerprintNicknameBean li : wiFiLockPassword.getFingerprintNickname()){
                        if(li.getNum() == bean.getNum()){
                            if(li.getNickName().isEmpty()){
                                nickName = sNum;
                            }else{
                                nickName = li.getNickName();
                            }
                        }
                    }

                if(wiFiLockPassword.getFingerprintDuress() != null && wiFiLockPassword.getFingerprintDuress().size() > 0){
                    for(WiFiLockPassword.DuressBean oi : wiFiLockPassword.getFingerprintDuress()){
                        if(oi.getNum() == bean.getNum()){
                            duressAlarmAccount = oi.getDuressAlarmAccount();
                            pwdDuressSwitch = oi.getPwdDuressSwitch();
                        }
                    }
                }
                list.add(new PhilipsDuressBean(wifisn,2,pwdDuressSwitch,duressAlarmAccount,bean.getNum(),bean.getCreateTime(),nickName));
            }
        }
        return list;
    }


    public void setDuressSwitch(String wifiSN,int duressSwitch){
        SettingPwdDuressAlarmSwitchBean mSettingPwdDuressAlarmSwitchBean = new SettingPwdDuressAlarmSwitchBean(MyApplication.getInstance().getUid(),wifiSN,duressSwitch);
        toDisposable(duressSwitchDisposable);
        XiaokaiNewServiceImp.wifiPwdDuressAlarmSwitch(mSettingPwdDuressAlarmSwitchBean).subscribe(new BaseObserver<BaseResult>() {
            @Override
            public void onSuccess(BaseResult baseResult) {
                MyApplication.getInstance().getAllDevicesByMqtt(true);
                if(isSafe()){
                    LogUtils.e("shulan setDuressSwitch--> " + baseResult.toString());
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
                duressSwitchDisposable = d;
            }
        });
    }
}
