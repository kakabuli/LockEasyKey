package com.philips.easykey.lock.mvp.presenter.clotheshangermachinepresenter;


import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.clotheshangermachineview.IClothesHangerMachineAddTourthView;
import com.philips.easykey.lock.publiclibrary.ble.BleCommandFactory;
import com.philips.easykey.lock.publiclibrary.ble.responsebean.BleDataBean;
import com.philips.easykey.lock.publiclibrary.ble.responsebean.BleStateBean;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;


import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

public class ClothesHangerMachineAddTourthPresenter<T> extends BasePresenter<IClothesHangerMachineAddTourthView> {

    private Disposable listenConnectStateDisposable;
    private Disposable characterNotifyDisposable;

    private boolean isConnected = false;

    @Override
    public void attachView(IClothesHangerMachineAddTourthView view) {
        super.attachView(view);
        listenConnectState();
        listenerCharacterNotify();
    }

    @Override
    public void detachView() {
        super.detachView();

    }

    public void listenConnectState() {
        toDisposable(listenConnectStateDisposable);
        if (bleService == null) { //判断
            if (MyApplication.getInstance().getBleService() == null) {
                return;
            } else {
                bleService = MyApplication.getInstance().getBleService(); //判断
            }
        }
        listenConnectStateDisposable = bleService.subscribeDeviceConnectState() //1
                .compose(RxjavaHelper.observeOnMainThread())
                .subscribe(new Consumer<BleStateBean>() {
                    @Override
                    public void accept(BleStateBean bleStateBean) throws Exception {
                        if (isSafe()) {
                            if(!isConnected){
                                LogUtils.d("shulan isConnected------->" + isConnected);
                                mViewRef.get().onDeviceStateChange(bleStateBean.isConnected());
                            }
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        //抛出异常

                    }
                });
        compositeDisposable.add(listenConnectStateDisposable);
    }

    public void listenerCharacterNotify() {
        LogUtils.d("shulan --kaadas--listenerCharacterNotify");

        if (bleService == null) { //判断
            if (MyApplication.getInstance().getBleService() == null) {
                return;
            } else {
                bleService = MyApplication.getInstance().getBleService(); //判断
            }
        }
        toDisposable(characterNotifyDisposable);
        characterNotifyDisposable = bleService.listeneDataChange()
                .filter(new Predicate<BleDataBean>() {
                    @Override
                    public boolean test(BleDataBean bleDataBean) throws Exception {
                        return true;
                    }
                })
                .delay(100, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<BleDataBean>() {
                    @Override
                    public void accept(BleDataBean bleDataBean) throws Exception {

                        byte[] originalData = bleDataBean.getOriginalData();
                        LogUtils.d("shulan--kaadas--收到锁的配网数据" + Rsa.bytesToHexString(originalData));

                        if ((originalData[3] & 0xff) == 0x93) {
                            LogUtils.d("shulan--kaadas--收到配网结果");
                            if ((originalData[4] & 0xff) == 0x00){
                                //配网成功
                                isConnected = true;
                            }else {
                                //配网失败
                            }

                            if((originalData[3] & 0xff) == 0x97){
                                LogUtils.d("shulan--kaadas--收到Mqtt连接指令");
                                if ((originalData[4] & 0xff) == 0x00){
                                    //配网成功
                                    isConnected = true;
                                }else {
                                    //配网失败
                                }
                            }
                        }
                    }
                });
        compositeDisposable.add(characterNotifyDisposable);
    }

}
