package com.philips.easykey.lock.mvp.presenter.deviceaddpresenter;

import android.text.TextUtils;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiSetUpPresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.IBindBleView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.ble.BleCommandFactory;
import com.philips.easykey.lock.publiclibrary.ble.BleProtocolFailedException;
import com.philips.easykey.lock.publiclibrary.ble.OldBleCommandFactory;
import com.philips.easykey.lock.publiclibrary.ble.RetryWithTime;
import com.philips.easykey.lock.publiclibrary.ble.responsebean.BleDataBean;
import com.philips.easykey.lock.publiclibrary.ble.responsebean.BleStateBean;
import com.philips.easykey.lock.publiclibrary.ble.responsebean.ReadInfoBean;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.OfflinePasswordFactorManager;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import java.util.zip.CRC32;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Create By hushucong  on 2020/6/5
 * Describe 蓝牙wifi单火开关绑定流程
 *
 */
public class BindBleWiFiSwitchPresenter<T> extends BasePresenter<IBindBleView> {

    protected BleLockInfo bleLockInfo;
    private int bleVersion;
    private String mac;
    private String deviceName;
    private int functionSet = -1;
    private Disposable characterNotifyDisposable;
    private Disposable listenConnectStateDisposable;
    private Disposable featureSetDisposable;
    private OfflinePasswordFactorManager offlinePasswordFactorManager = OfflinePasswordFactorManager.getInstance();
    private OfflinePasswordFactorManager.OfflinePasswordFactorResult wifiResult;
    private int index;//命令包序号

    public void listenerCharacterNotify() {
        LogUtils.d("--kaadas--listenerCharacterNotify");

        if (bleService == null) { //判断
            if (MyApplication.getInstance().getBleService() == null) {
                return;
            } else {
                bleService = MyApplication.getInstance().getBleService(); //判断
            }
        }
        toDisposable(characterNotifyDisposable);
        characterNotifyDisposable = bleService.listeneDataChange()
//                .filter(new Predicate<BleDataBean>() {
//            @Override
//            public boolean test(BleDataBean bleDataBean) throws Exception {
//
//            }
//        })
//                .delay(100, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<BleDataBean>() {
                    @Override
                    public void accept(BleDataBean bleDataBean) throws Exception {
                        //收到入网数据
                        byte[] originalData = bleDataBean.getOriginalData();
//                        LogUtils.d("--kaadas--收到锁的配网数据" + Rsa.bytesToHexString(originalData));
                        if ((originalData[3] & 0xff) == 0x95) {//接到剩余校验次数
                            mViewRef.get().onlistenerLastNum(originalData[4] & 0xFF);
                        }
                        if ((originalData[3] & 0xff) == 0x92){//离线密码因子

                            int pswLen = originalData[4];
                            index = originalData[5];
                            //新数组
                            byte[] passwordFactor = new byte[originalData.length-6];
                            //从原始数组4位置开始截取后面所有
                            System.arraycopy(originalData, 6, passwordFactor, 0, originalData.length-6);
//                            LogUtils.d("--kaadas--密码因子分包数据==    " + Rsa.bytesToHexString(passwordFactor));
                            mViewRef.get().onlistenerPasswordFactor(passwordFactor, pswLen, index);

                        }
                        if ((originalData[3] & 0xff) == 0x94){//收到解密结果
//                            LogUtils.d("--kaadas--收到解密结果");
                            checkAdminPassWordResult();

                        }
                        //toDisposable(characterNotifyDisposable);
                    }
                });
        compositeDisposable.add(characterNotifyDisposable);
    }

    public void parsePasswordFactorData(String adminPassword, byte[] data) {
        wifiResult = OfflinePasswordFactorManager.parseOfflinePasswordFactorData(adminPassword, data);
        LogUtils.d("--Kaadas--wifiResult："+wifiResult.result);

        //发送0x94下发密码因子校验结果 通知锁端
        if (wifiResult.result == 0){
            //校验成功
            mViewRef.get().onDecodeResult(2,wifiResult);
            byte[] authKey = null;//不加密
            byte[] command = BleCommandFactory.onDecodeResult(authKey,Rsa.int2BytesArray(wifiResult.result)[0]);
            bleService.sendCommand(command);
        }
        else {
            //校验失败
            mViewRef.get().onDecodeResult(-1,wifiResult);
            wifiResult = null;
            byte[] authKey = null;//不加密
            byte[] command = BleCommandFactory.onDecodeResult(authKey,Rsa.int2BytesArray(1)[0]);
            bleService.sendCommand(command);
        }
    }

    public void readFeatureSet(){

        if (bleService == null) { //判断
            if (MyApplication.getInstance().getBleService() == null) {
                return;
            } else {
                bleService = MyApplication.getInstance().getBleService(); //判断
            }
        }
        toDisposable(featureSetDisposable);
        featureSetDisposable = bleService.readFunctionSet(1000)
                .filter(new Predicate<ReadInfoBean>() {
                    @Override
                    public boolean test(ReadInfoBean readInfoBean) throws Exception {
                        return readInfoBean.type == ReadInfoBean.TYPE_LOCK_FUNCTION_SET;
                    }
                })
                .timeout(2 * 1000, TimeUnit.MILLISECONDS)
                .retryWhen(new RetryWithTime(2, 0))
                .subscribe(new Consumer<ReadInfoBean>() {
                    @Override
                    public void accept(ReadInfoBean readInfoBean) throws Exception {
                        toDisposable(featureSetDisposable);

                        int functionSet = (int) readInfoBean.data;
                        LogUtils.d("--kaadas--BLE&wifi锁功能集==" + functionSet);
                        if (isSafe()) {
                            mViewRef.get().readFunctionSetSuccess(functionSet);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        compositeDisposable.add(featureSetDisposable);
    }
    public void checkAdminPassWordResult() {

            if (wifiResult != null){
                mViewRef.get().onDecodeResult(3, wifiResult);
            }

    }

    public void disconnectBLE() {

        if (bleService != null) { //停止扫描设备
            bleService.scanBleDevice(false);  //1
            bleService.release();
        }
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
                            mViewRef.get().onDeviceStateChange(bleStateBean.isConnected());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        compositeDisposable.add(listenConnectStateDisposable);
    }
}
