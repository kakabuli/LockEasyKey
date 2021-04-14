package com.philips.easykey.lock.mvp.presenter.gatewaypresenter;

import android.util.Log;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.gatewayView.GatewaySettingView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.GetNetBasicBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.GetZbChannelBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.SetNetBasicBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.SetWiFiBasic;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.SetZBChannel;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.UnBindGatewayBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.DeviceShareResultBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GwWiFiBaseInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.UpdateGatewayNickNameResultBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.ftp.GeTui;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class GatewaySettingPresenter<T> extends BasePresenter<GatewaySettingView> {
    private Disposable getNetBasicDisposable;
    private Disposable getWiFiBasicDisposable;
    private Disposable getZbChannelDisposable;
    private Disposable unbindGatewayDisposable;
    private Disposable wifiSettingDisposable;
    private Disposable setNetBasicDisposable;
    private Disposable setZBChannelDisposable;
    private Disposable unbindTestGatewayDisposable;
    private Disposable deleteShareDisposable;
    private Disposable updateNameDisposable;

    // 获取网络设置基本信息
    public void getNetBasic(String uid, String gatewayId, String deviceId) {
        MqttMessage netBasic = MqttCommandFactory.getNetBasic(uid, gatewayId, deviceId);
        if (mqttService != null) {
            toDisposable(getNetBasicDisposable);
            getNetBasicDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), netBasic)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            Log.e(GeTui.MqttLog, "mqtt--receiver..test.." + "GatewaySettingPresenter" + mqttData.toString() + "");
                            if (MqttConstant.GET_NET_BASIC.equals(mqttData.getFunc())) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if ("200".equals(mqttData.getReturnCode())) { //请求成功
                                GetNetBasicBean netBasicBean = new Gson().fromJson(mqttData.getPayload(), GetNetBasicBean.class);
                                if (isSafe()) {
                                    mViewRef.get().getNetBasicSuccess(netBasicBean);
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().getNetBasicFail();
                                }
                            }
                            toDisposable(getNetBasicDisposable);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().getNetBasicThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(getNetBasicDisposable);

        }
    }


    //获取网关的wifi名称和网关的wifi密码
    public void getGatewayWifiPwd(String gwId) {
        MqttMessage wiFiBasic = MqttCommandFactory.getWiFiBasic(MyApplication.getInstance().getUid(), gwId, gwId);
        if (mqttService != null) {
            toDisposable(getWiFiBasicDisposable);
            getWiFiBasicDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), wiFiBasic)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            LogUtils.e("获取到的数据的messageId是   " + mqttData.getMessageId() + "   发送的messageId是  " + wiFiBasic.getId());
                            return mqttData.isThisRequest(wiFiBasic.getId(), MqttConstant.GET_WIFI_BASIC);
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if ("200".equals(mqttData.getReturnCode())) { //请求成功
                                GwWiFiBaseInfo wiFiBaseInfo = new Gson().fromJson(mqttData.getPayload(), GwWiFiBaseInfo.class);
                                if (isSafe()) {
                                    mViewRef.get().onGetWifiInfoSuccess(wiFiBaseInfo);
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().onGetWifiInfoFailed();
                                }
                            }
                            toDisposable(getWiFiBasicDisposable);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().onGetWifiInfoThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(getWiFiBasicDisposable);

        }
    }

    //网关协调器信道获取
    public void getZbChannel(String uid, String gatewayId, String deviceId) {
        MqttMessage zbChannel = MqttCommandFactory.getZbChannel(uid, gatewayId, deviceId);
        if (mqttService != null) {
            toDisposable(getZbChannelDisposable);
            getZbChannelDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), zbChannel)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (mqttData.getFunc().equals(MqttConstant.GET_ZB_Channel)) {
                                return true;
                            } else {
                                return false;
                            }
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if ("200".equals(mqttData.getReturnCode())) { //请求成功

                                GetZbChannelBean getZbChannelBean = new Gson().fromJson(mqttData.getPayload(), GetZbChannelBean.class);

                                if (isSafe()) {
                                    mViewRef.get().getZbChannelSuccess(getZbChannelBean);
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().getZbChannelFail();
                                }
                            }
                            toDisposable(getZbChannelDisposable);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().getZbChannelThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(getZbChannelDisposable);
        }
    }


    //解绑网关
    public void unBindGateway(String uid, String gatewayId) {
        if (mqttService != null) {
            toDisposable(unbindGatewayDisposable);
            unbindGatewayDisposable = mqttService.mqttPublish(MqttConstant.MQTT_REQUEST_APP, MqttCommandFactory.unBindGateway(uid, gatewayId))
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.UNBIND_GATEWAY.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(unbindGatewayDisposable);
                            UnBindGatewayBean unBindGatewayBean = new Gson().fromJson(mqttData.getPayload(), UnBindGatewayBean.class);
                            if ("200".equals(unBindGatewayBean.getCode())) {
                                if (isSafe()) {
                                    mViewRef.get().unbindGatewaySuccess();
                                    MyApplication.getInstance().getAllDevicesByMqtt(true);
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().unbindGatewayFail();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().unbindGatewayThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(unbindGatewayDisposable);
        }
    }

    //测试解绑网关
    public void testUnbindGateway(String uid, String gatewayId, String devuuid) {
        if (mqttService != null) {
            toDisposable(unbindTestGatewayDisposable);
            unbindTestGatewayDisposable = mqttService.mqttPublish(MqttConstant.MQTT_REQUEST_APP, MqttCommandFactory.unBindTestGateway(uid, gatewayId, devuuid))
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.UNBIND_TEST_GATEWAY.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(unbindTestGatewayDisposable);
                            UnBindGatewayBean unBindGatewayBean = new Gson().fromJson(mqttData.getPayload(), UnBindGatewayBean.class);
                            if ("200".equals(unBindGatewayBean.getCode())) {
                                if (isSafe()) {
                                    mViewRef.get().unbindTestGatewaySuccess();
                                    MyApplication.getInstance().getAllDevicesByMqtt(true);
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().unbindTestGatewayFail();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().unbindTestGatewayThrowable(throwable);
                            }
                        }
                    });

        }
        compositeDisposable.add(unbindTestGatewayDisposable);
    }


    //设置wifi名称
    public void setWiFi(String uid, String gatewayId, String deviceId, String encryption, String name, String pwd) {
        if (mqttService != null) {
            toDisposable(wifiSettingDisposable);
            wifiSettingDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(uid), MqttCommandFactory.setWiFiBasic(uid, gatewayId, deviceId, name, pwd, encryption))
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.SET_WIFI_BASIC.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(wifiSettingDisposable);
                            SetWiFiBasic setWiFiBasic = new Gson().fromJson(mqttData.getPayload(), SetWiFiBasic.class);
                            if ("200".equals(setWiFiBasic.getReturnCode())) {
                                if (isSafe()) {
                                    mViewRef.get().setWifiSuccess(setWiFiBasic.getParams().getSsid(), setWiFiBasic.getParams().getPwd());

                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().setWifiFail();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().setWifiThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(wifiSettingDisposable);
        }
    }

    //配置局域网
    public void setNetBasic(String uid, String gatewayId, String deviceId, String lanIp, String lanNetmask) {
        if (mqttService != null) {
            toDisposable(setNetBasicDisposable);
            setNetBasicDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(uid), MqttCommandFactory.setNetBasic(uid, gatewayId, deviceId, lanIp, lanNetmask))
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.SET_NET_BASIC.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(setNetBasicDisposable);
                            SetNetBasicBean setWiFiBasic = new Gson().fromJson(mqttData.getPayload(), SetNetBasicBean.class);
                            if ("200".equals(setWiFiBasic.getReturnCode())) {
                                if (isSafe()) {
                                    mViewRef.get().setNetLanSuccess(setWiFiBasic.getParams().getLanIp(), setWiFiBasic.getParams().getLanNetmask());

                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().setNetLanFail();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().setNetLanThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(setNetBasicDisposable);
        }
    }

    //配置局域网
    public void setZbChannel(String uid, String gatewayId, String deviceId, String channel) {
        if (mqttService != null) {
            toDisposable(setZBChannelDisposable);
            setZBChannelDisposable = mqttService.mqttPublish(MqttConstant.getCallTopic(uid), MqttCommandFactory.setZBChannel(uid, gatewayId, deviceId, channel))
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.SET_ZB_CHANNEL.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(setZBChannelDisposable);
                            SetZBChannel setZBChannel = new Gson().fromJson(mqttData.getPayload(), SetZBChannel.class);
                            if ("200".equals(setZBChannel.getReturnCode())) {
                                if (isSafe()) {
                                    mViewRef.get().setZbChannelSuccess(setZBChannel.getParams().getChannel());
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().setZbChannelFail();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().setZbChannelThrowable(throwable);
                            }
                        }
                    });
            compositeDisposable.add(setZBChannelDisposable);
        }
    }


    //删除分享用户,修改昵称
    public void deleteShareDevice(int type, String gatewayId, String deviceId, String uid, String shareUser, String userName, int shareFlag) {
        if (mqttService != null) {
            toDisposable(deleteShareDisposable);
            deleteShareDisposable = mqttService.mqttPublish(MqttConstant.PUBLISH_TO_SERVER, MqttCommandFactory.shareDevice(type, gatewayId, deviceId, uid, shareUser, userName, shareFlag))
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (mqttData.getFunc().equals(MqttConstant.SHARE_DEVICE)) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(deleteShareDisposable);
                            DeviceShareResultBean shareResultBean = new Gson().fromJson(mqttData.getPayload(), DeviceShareResultBean.class);
                            if ("200".equals(shareResultBean.getCode())) {
                                if (isSafe() && gatewayId.equals(shareResultBean.getGwId()) && deviceId.equals(shareResultBean.getDeviceId())) {
                                    mViewRef.get().deleteShareUserSuccess();
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().deleteShareUserFail();
                                }
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().deleteShareUserThrowable();
                            }
                        }
                    });
            compositeDisposable.add(deleteShareDisposable);
        }
    }


    //修改昵称
    public void updateGatewayName(String gatewayId, String uid, String nickName) {
        toDisposable(updateNameDisposable);
        if (mqttService != null && mqttService.getMqttClient() != null && mqttService.getMqttClient().isConnected()) {
            MqttMessage mqttMessage = MqttCommandFactory.updateGatewayNickName(uid, gatewayId, nickName);
            updateNameDisposable = mqttService
                    .mqttPublish(MqttConstant.PUBLISH_TO_SERVER, mqttMessage)
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .compose(RxjavaHelper.observeOnMainThread())
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.UPDATE_GATEWAY_NICK_NAME.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(updateNameDisposable);
                            UpdateGatewayNickNameResultBean nameResult = new Gson().fromJson(mqttData.getPayload(), UpdateGatewayNickNameResultBean.class);
                            if (nameResult != null) {
                                if ("200".equals(nameResult.getCode())) {
                                    if (isSafe()) {
                                        mViewRef.get().updateDevNickNameSuccess(nickName);
                                        MyApplication.getInstance().getAllDevicesByMqtt(true);
                                    }
                                } else {
                                    if (isSafe()) {
                                        mViewRef.get().updateDevNickNameFail();
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().updateDevNickNameThrowable(throwable);
                            }
                        }
                    });

            compositeDisposable.add(updateNameDisposable);
        }

    }


}
