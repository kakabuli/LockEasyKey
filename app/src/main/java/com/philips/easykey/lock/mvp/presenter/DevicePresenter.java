package com.philips.easykey.lock.mvp.presenter;

import com.google.gson.Gson;
import com.philips.easykey.lock.BuildConfig;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.IDeviceView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.ServerBleDevice;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.DeviceOnLineBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.GetDevicePowerBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GetBindGatewayStatusResult;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.networkListenerutil.NetWorkChangReceiver;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class DevicePresenter<T> extends BasePresenter<IDeviceView> {
    private Disposable listenerAllDevicesDisposable;
    private Disposable allBindDeviceDisposable;
    protected BleLockInfo bleLockInfo;
    protected Disposable getPowerDisposable;
    private Disposable listenerDeviceOnLineDisposable;
    private Disposable listenerGatewayOnLine;
    private Disposable networkChangeDisposable;
    private Disposable bingMimiDisposable;

    @Override
    public void attachView(IDeviceView view) {
        super.attachView(view);
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

    //刷新页面
    public void refreshData() {
        if (mqttService != null) {
            toDisposable(allBindDeviceDisposable);
            MqttMessage allBindDevice = MqttCommandFactory.getAllBindDevice(MyApplication.getInstance().getUid());
            allBindDeviceDisposable = mqttService.mqttPublish(MqttConstant.MQTT_REQUEST_APP, allBindDevice)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            return mqttData.getFunc().equalsIgnoreCase(MqttConstant.GET_ALL_BIND_DEVICE);
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(allBindDeviceDisposable);
                            String payload = mqttData.getPayload();
                            if(BuildConfig.DEBUG)
                            LogUtils.d("--kaadas--payload=="+payload);

                            AllBindDevices allBindDevices = new Gson().fromJson(payload, AllBindDevices.class);
                            if (allBindDevices != null) {
                                if ("200".equals(allBindDevices.getCode())) {
                                    MyApplication.getInstance().setAllBindDevices(allBindDevices);
                                    //重新获取数据
                                    long serverCurrentTime = Long.parseLong(allBindDevices.getTimestamp());
                                    SPUtils.put(KeyConstants.SERVER_CURRENT_TIME, serverCurrentTime);
                                } else {
                                    if (isSafe()) {
                                        mViewRef.get().deviceDataRefreshFail();
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().deviceDataRefreshThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(allBindDeviceDisposable);
        }

    }

    public void setBleLockInfo(BleLockInfo bleLockInfo) {
        if (bleService == null) { //判断
            if (MyApplication.getInstance().getBleService() == null) {
                return;
            } else {
                bleService = MyApplication.getInstance().getBleService(); //判断
            }
        }
        //如果service中有bleLockInfo  并且deviceName一致，就不重新设置。
        LogUtils.d("设置的  设备信息为  " + bleLockInfo.getServerLockInfo().toString());
        if (bleService.getBleLockInfo() != null
                && bleService.getBleLockInfo().getServerLockInfo().getLockName().equals(bleLockInfo.getServerLockInfo().getLockName())) { //1
            ServerBleDevice serviceLockInfo = bleService.getBleLockInfo().getServerLockInfo(); //1
            ServerBleDevice serverLockInfo = bleLockInfo.getServerLockInfo();
            if (serverLockInfo.getPassword1() != null && serverLockInfo.getPassword2() != null) {
                if (serverLockInfo.getPassword1().equals(serviceLockInfo.getPassword1()) && serverLockInfo.getPassword2().equals(serviceLockInfo.getPassword2())) {
                    LogUtils.d("进来了  设备  数据一致   " + bleService.getBleLockInfo().getServerLockInfo().toString()); //1
                    return;
                }
            } else {
                if ((serviceLockInfo.getPassword1() == null && serverLockInfo.getPassword1() == null) && (serviceLockInfo.getPassword2() == null && serverLockInfo.getPassword2() == null)) {
                    LogUtils.d("进来了 密码为空 设备  数据一致   " + bleService.getBleLockInfo().getServerLockInfo().toString()); //1
                    return;
                }
            }
        }
        bleService.setBleLockInfo(bleLockInfo);  //1
        this.bleLockInfo = bleLockInfo;
    }


    //请求电量
    public void getPower(String gatewayId, String deviceId, String uid) {
        if (mqttService != null) {
            MqttMessage message = MqttCommandFactory.getDevicePower(gatewayId, deviceId, uid);
            getPowerDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(uid), message)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (mqttData.getMessageId() == message.getId() && mqttData.getFunc().equalsIgnoreCase(MqttConstant.GET_POWER)) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            String payload = mqttData.getPayload();
                            GetDevicePowerBean powerBean = new Gson().fromJson(payload, GetDevicePowerBean.class);
                            if (powerBean != null && deviceId.equals(powerBean.getDeviceId())) {
                                if ("200".equals(powerBean.getReturnCode())) {
                                    if (isSafe()) {
                                        mViewRef.get().getDevicePowerSuccess(gatewayId, deviceId, powerBean.getReturnData().getPower(), powerBean.getTimestamp());
                                    }
                                } else {
                                    if (isSafe()) {
                                        mViewRef.get().getDevicePowerFail(gatewayId, deviceId);
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().getDevicePowerThrowable(gatewayId, deviceId);
                            }
                        }
                    });
            compositeDisposable.add(getPowerDisposable);
        }

    }

    //获取网关状态通知
    public void getPublishNotify() {
        if (mqttService != null) {
            toDisposable(listenerGatewayOnLine);
            listenerGatewayOnLine = mqttService.listenerNotifyData()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if (mqttData != null) {
                                GetBindGatewayStatusResult gatewayStatusResult = new Gson().fromJson(mqttData.getPayload(), GetBindGatewayStatusResult.class);
                                LogUtils.d("监听网关的Device状态" + gatewayStatusResult.getDevuuid() + " mqttData:" + mqttData);
                                if (gatewayStatusResult != null && gatewayStatusResult.getData().getState() != null) {
                                    if (isSafe()) {
                                        mViewRef.get().gatewayStatusChange(gatewayStatusResult.getDevuuid(), gatewayStatusResult.getData().getState());
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            //网关状态发生异常
                        }
                    });
            compositeDisposable.add(listenerGatewayOnLine);
        }
    }

    /**
     * 监听设备上线下线
     */
    public void listenerDeviceOnline() {
        if (mqttService != null) {
            toDisposable(listenerDeviceOnLineDisposable);
            listenerDeviceOnLineDisposable = mqttService.listenerDataBack()
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            return mqttData.getFunc().equals(MqttConstant.GW_EVENT);
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            DeviceOnLineBean deviceOnLineBean = new Gson().fromJson(mqttData.getPayload(), DeviceOnLineBean.class);
                            if (deviceOnLineBean != null) {
                                if (isSafe() && deviceOnLineBean.getEventparams().getEvent_str() != null) {
                                    mViewRef.get().deviceStatusChange(deviceOnLineBean.getGwId(), deviceOnLineBean.getDeviceId(), deviceOnLineBean.getEventparams().getEvent_str());
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                        }
                    });
            compositeDisposable.add(listenerDeviceOnLineDisposable);
        }

    }

    //网络变化通知
    public void listenerNetworkChange() {
        toDisposable(networkChangeDisposable);
        networkChangeDisposable = NetWorkChangReceiver.notifyNetworkChange().subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    if (isSafe()) {
                        mViewRef.get().networkChangeSuccess();
                    }
                }
            }
        });
        compositeDisposable.add(networkChangeDisposable);
    }


}
