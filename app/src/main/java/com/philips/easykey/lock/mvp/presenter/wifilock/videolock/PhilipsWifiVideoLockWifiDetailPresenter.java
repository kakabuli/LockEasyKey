package com.philips.easykey.lock.mvp.presenter.wifilock.videolock;

import android.util.Log;

import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IPhilipsWifiDetailView;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IWifiVideoLockSetLanguageView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PManager;
import com.philips.easykey.lock.publiclibrary.xm.bean.DeviceInfo;

import org.json.JSONException;
import org.json.JSONObject;

public class PhilipsWifiVideoLockWifiDetailPresenter<T> extends BasePresenter<IPhilipsWifiDetailView> {
    private static  String did ="";//AYIOTCN-000337-FDFTF
    private static  String sn ="";//010000000020500020

    private static  String p2pPassword ="";//ut4D0mvz

    private static  String serviceString= XMP2PManager.serviceString;;

    @Override
    public void attachView(IPhilipsWifiDetailView view) {
        super.attachView(view);
    }


    @Override
    public void detachView() {
        super.detachView();
    }

    public void settingDevice(WifiLockInfo wifiLockInfo) {
        did = wifiLockInfo.getDevice_did();
        sn = wifiLockInfo.getDevice_sn();
        p2pPassword = wifiLockInfo.getP2p_password();

    }

    public void release(){
        XMP2PManager.getInstance().stopConnect();//
        XMP2PManager.getInstance().stopCodec();
    }

    public void stopConnect(){
        XMP2PManager.getInstance().stopCodec();

    }

    public void connectP2P() {
        DeviceInfo deviceInfo=new DeviceInfo();
        deviceInfo.setDeviceDid(did);
        deviceInfo.setP2pPassword(p2pPassword);
        deviceInfo.setDeviceSn(sn);
        deviceInfo.setServiceString(serviceString);

        XMP2PManager.getInstance().setOnConnectStatusListener(new XMP2PManager.ConnectStatusListener() {
            @Override
            public void onConnectFailed(int paramInt) {
                if(isSafe()){
                    mViewRef.get().getSignalFailed(paramInt);
                }
            }

            @Override
            public void onConnectSuccess() {
                XMP2PManager.getInstance().getDeviceInformation(new XMP2PManager.DeviceInformationListener() {
                    @Override
                    public void onSignal(JSONObject jsonObject) {
                        try {
                            if (jsonObject.getString("result").equals("ok")){
                                int signal = jsonObject.getInt("signal");
                                String wifiStrength = "";
                                if(signal < 0 && signal > -40){
                                    wifiStrength = "100%";
                                }else{
                                    wifiStrength = (signal + 90) * 2 + "%" ;
                                }
                                if(isSafe()){
                                    mViewRef.get().getSignalSuccess(signal + "dBm",wifiStrength);
                                }
                            }else{
                                mViewRef.get().getSignalFailed(0);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onStartConnect(String paramString) {

            }

            @Override
            public void onErrorMessage(String message) {

            }

            @Override
            public void onNotifyGateWayNewVersion(String paramString) {

            }

            @Override
            public void onRebootDevice(String paramString) {

            }
        });

        XMP2PManager.getInstance().connectDevice(deviceInfo);
    }
}
