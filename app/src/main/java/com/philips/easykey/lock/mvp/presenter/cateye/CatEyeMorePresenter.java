package com.philips.easykey.lock.mvp.presenter.cateye;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.cateye.IGatEyeView;
import com.philips.easykey.lock.mvp.view.gatewaylockview.GatewayLockMoreView;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.DeleteDeviceLockBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.CatEyeInfoBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.GetSoundVolume;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.SetPirEnableBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.CatEyeInfoBeanPropertyResult;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.CatEyeInfoBeanResult;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.DeviceShareResultBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.UpdateDevNickNameResult;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LoadingDialog;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class CatEyeMorePresenter<T> extends BasePresenter<IGatEyeView> {
    private Disposable updateNameDisposable;
    private Disposable getCatEyeInfoDisposable;
    private Disposable setPirEnableDisposable;
    private Disposable deleteCatEyeDisposable;
    private Disposable deleteShareDisposable;

    //修改昵称
    public void updateDeviceName(String devuuid, String deviceId, String nickName) {
        toDisposable(updateNameDisposable);
        if (mqttService != null && mqttService.getMqttClient() != null && mqttService.getMqttClient().isConnected()) {
            MqttMessage mqttMessage = MqttCommandFactory.updateDeviceNickName(MyApplication.getInstance().getUid(), devuuid, deviceId, nickName);
            updateNameDisposable = mqttService
                    .mqttPublish(MqttConstant.PUBLISH_TO_SERVER, mqttMessage)
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.UPDATE_DEV_NICK_NAME.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(updateNameDisposable);
                            UpdateDevNickNameResult nameResult = new Gson().fromJson(mqttData.getPayload(), UpdateDevNickNameResult.class);
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

    //获取猫眼基本信息
    public void getCatEyeInfo(String gatewayId, String deviceId, String uid) {
        toDisposable(getCatEyeInfoDisposable);
        if (mqttService != null) {
            MqttMessage mqttMessage = MqttCommandFactory.getCatEyeInfo(gatewayId, deviceId, uid);
            getCatEyeInfoDisposable = mqttService
                    .mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), mqttMessage)
                    .timeout(15 * 1000, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.BASIC_INFO.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(getCatEyeInfoDisposable);
                            CatEyeInfoBeanResult catEyeInfoBean = new Gson().fromJson(mqttData.getPayload(), CatEyeInfoBeanResult.class);
                            LogUtils.e("获取到的猫眼基本信息    " + mqttData.getPayload());
                            if (catEyeInfoBean != null) {
                                if ("200".equals(catEyeInfoBean.getReturnCode())) {
                                    if (isSafe()) {
                                        mViewRef.get().getCatEyeInfoSuccess(catEyeInfoBean, mqttData.getPayload());
                                    }
                                } else {
                                    if (isSafe()) {
                                        mViewRef.get().getCatEyeInfoFail();
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().getCatEveThrowable(throwable);
                            }
                        }
                    });

            compositeDisposable.add(getCatEyeInfoDisposable);
        }

    }


    //获取猫眼夜视信息
    public void getCatNightSightInfo(String gatewayId, String deviceId, String uid) {
        toDisposable(getCatEyeInfoDisposable);
        if (mqttService != null) {
            MqttMessage mqttMessage = MqttCommandFactory.getCatNightSight(gatewayId, deviceId, uid);
            getCatEyeInfoDisposable = mqttService
                    .mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), mqttMessage)
                    .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.CATEYE_NIGHT_SIGHT.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            toDisposable(getCatEyeInfoDisposable);
                            CatEyeInfoBeanPropertyResult catEyeInfoBeanPropertyResult = new Gson().fromJson(mqttData.getPayload(), CatEyeInfoBeanPropertyResult.class);
                            LogUtils.e("获取到的猫眼基本信息    " + mqttData.getPayload());
                            if (catEyeInfoBeanPropertyResult != null) {
                                if ("200".equals(catEyeInfoBeanPropertyResult.getReturnCode())) {
                                    if (isSafe()) {
                                        //     mViewRef.get().getCatEyeInfoSuccess(catEyeInfoBean,mqttData.getPayload());

                                        mViewRef.get().getCatEyeInfoNightSightSuccess(catEyeInfoBeanPropertyResult);
                                    }
                                } else {
                                    if (isSafe()) {
                                        mViewRef.get().getCatEyeInfoNightSightFail();
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().getNightSighEveThrowable(throwable);
                            }
                        }
                    });

            compositeDisposable.add(getCatEyeInfoDisposable);
        }

    }

    //删除猫眼
    public void deleteCatEye(String gatewayId, String deviceId, String bustType) {
        toDisposable(deleteCatEyeDisposable);
        if (mqttService != null) {
            MqttMessage mqttMessage = MqttCommandFactory.deleteDevice(gatewayId, deviceId, bustType);
            deleteCatEyeDisposable = mqttService
                    .mqttPublish(MqttConstant.getCallTopic(MyApplication.getInstance().getUid()), mqttMessage)
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            //由于网关那边没有响应事件，所以暂时以接收上报删除事件来判断是否删除成功。
                            if (MqttConstant.GW_EVENT.equals(mqttData.getFunc())) {
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
                            toDisposable(deleteCatEyeDisposable);
                            DeleteDeviceLockBean deleteGatewayLockDeviceBean = new Gson().fromJson(mqttData.getPayload(), DeleteDeviceLockBean.class);
                            if (deleteGatewayLockDeviceBean != null) {
                                LogUtils.e(deleteGatewayLockDeviceBean.getDevtype() + "删除猫眼" + deleteGatewayLockDeviceBean.getEventparams().getEvent_str());
                                if (deleteGatewayLockDeviceBean.getEventparams().getEvent_str() != null) {
                                    if ("kdscateye".equals(deleteGatewayLockDeviceBean.getDevtype()) && deleteGatewayLockDeviceBean.getEventparams().getEvent_str().equals("delete") && deviceId.equals(deleteGatewayLockDeviceBean.getDeviceId())) {
                                        if (isSafe()) {
                                            mViewRef.get().deleteDeviceSuccess();
                                            MyApplication.getInstance().getAllDevicesByMqtt(true);
                                        }
                                    } else {
                                        if (isSafe()) {
                                            mViewRef.get().deleteDeviceFail();
                                        }
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().deleteDeviceThrowable(throwable);
                            }
                        }
                    });

            compositeDisposable.add(deleteCatEyeDisposable);
        }
    }

    //取消授权网关锁
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
                                    mViewRef.get().deleteShareDeviceSuccess();
                                    MyApplication.getInstance().getAllDevicesByMqtt(true);
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().deleteShareDeviceFail();
                                }
                            }

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            if (isSafe()) {
                                mViewRef.get().deleteShareDeviceThrowable();
                            }
                        }
                    });
            compositeDisposable.add(deleteShareDisposable);
        }
    }
}

