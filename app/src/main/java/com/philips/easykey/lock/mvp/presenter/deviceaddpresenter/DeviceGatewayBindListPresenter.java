package com.philips.easykey.lock.mvp.presenter.deviceaddpresenter;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GetBindGatewayStatusResult;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.mvp.view.deviceaddview.DeviceGatewayBindListView;
import java.util.ArrayList;
import java.util.List;


import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class DeviceGatewayBindListPresenter<T> extends BasePresenter<DeviceGatewayBindListView> {

    private Disposable disposable;
    private Disposable getWiFiBasicDisposable;
    private ArrayList<HomeShowBean> showBeansList = new ArrayList<>();

    //获取绑定的网关列表
    public List<HomeShowBean> getGatewayBindList() {
        List<HomeShowBean> homeShowBeans = MyApplication.getInstance().getAllDevices();
        for (HomeShowBean showBean : homeShowBeans) {
            if (showBean.getDeviceType() == HomeShowBean.TYPE_GATEWAY) {
                if (showBeansList != null) {
                    showBeansList.add(showBean);
                }
            }
        }
        return showBeansList;


    }

    //获取通知
    public void getGatewayState() {
        if (mqttService != null) {
            disposable = mqttService.listenerNotifyData()
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if (mqttData != null) {
                                if (MqttConstant.GATEWAY_STATE.equals(mqttData.getFunc())) {
                                    GetBindGatewayStatusResult gatewayStatusResult = new Gson().fromJson(mqttData.getPayload(), GetBindGatewayStatusResult.class);
                                    if (gatewayStatusResult != null) {
                                        if (isSafe()) {
                                            mViewRef.get().getGatewayStateSuccess(gatewayStatusResult.getDevuuid(), gatewayStatusResult.getData().getState());
                                        }
                                    }
                                }
                            }
                        }
                    });
            compositeDisposable.add(disposable);
        }
    }

}
