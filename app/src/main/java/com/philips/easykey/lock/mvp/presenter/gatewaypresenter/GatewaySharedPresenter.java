package com.philips.easykey.lock.mvp.presenter.gatewaypresenter;


import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.gatewayView.IGatewaySharedView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.DeviceShareResultBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.DeviceShareUserResultBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.blankj.utilcode.util.ToastUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class GatewaySharedPresenter<T> extends BasePresenter<IGatewaySharedView> {
    private Disposable shareDisposable;
    private Disposable shareUserDispoable;
    private Disposable bingMimiDisposable;

    //添加分享用户,删除分享用户
    public void shareDevice(int type, String gatewayId, String deviceId, String uid, String shareUser, String userName, int shareFlag) {
        if (mqttService != null) {
            toDisposable(shareDisposable);
            shareDisposable = mqttService.mqttPublish(MqttConstant.PUBLISH_TO_SERVER, MqttCommandFactory.shareDevice(type, gatewayId, deviceId, uid, shareUser, userName, shareFlag))
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
                            toDisposable(shareDisposable);
                            DeviceShareResultBean shareResultBean = new Gson().fromJson(mqttData.getPayload(), DeviceShareResultBean.class);
                            if ("200".equals(shareResultBean.getCode())) {
                                if (isSafe() && gatewayId.equals(shareResultBean.getGwId()) && deviceId.equals(shareResultBean.getDeviceId())) {
                                    mViewRef.get().shareDeviceSuccess(shareResultBean);
                                }
                            } else if("402".equals(shareResultBean.getCode())){
                                if (isSafe()) {
                                //    mViewRef.get().shareDeviceFail();
                                    ToastUtils.showShort(shareResultBean.getMsg());
                                }
                            }
                            else {
                                if (isSafe()) {
                                    mViewRef.get().shareDeviceFail();
                                }
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().shareDeviceThrowable();
                            }
                        }
                    });
            compositeDisposable.add(shareDisposable);
        }
    }

    //查找分享用户
    public void getShareDeviceUser(String gatewayId, String deviceId, String uid) {
        if (mqttService != null) {
            toDisposable(shareUserDispoable);
            shareUserDispoable = mqttService.mqttPublish(MqttConstant.PUBLISH_TO_SERVER, MqttCommandFactory.getShareUser(gatewayId, deviceId, uid))
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (mqttData.getFunc().equals(MqttConstant.SHARE_USER_LIST)) {
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
                            toDisposable(shareUserDispoable);
                            DeviceShareUserResultBean deviceShareUserResultBean = new Gson().fromJson(mqttData.getPayload(), DeviceShareUserResultBean.class);
                            if (deviceShareUserResultBean.getDeviceId().equals(deviceId) && deviceShareUserResultBean.getGwId().equals(gatewayId) && "200".equals(deviceShareUserResultBean.getCode())) {
                                if (isSafe()) {
                                    mViewRef.get().shareUserListSuccess(deviceShareUserResultBean.getData());
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().shareUserListFail();
                                }
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().shareUserListThrowable();
                            }
                        }
                    });
            compositeDisposable.add(shareUserDispoable);
        }

    }

}
