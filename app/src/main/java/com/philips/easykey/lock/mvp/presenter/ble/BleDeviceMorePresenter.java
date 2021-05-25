package com.philips.easykey.lock.mvp.presenter.ble;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BleCheckOTAPresenter;
import com.philips.easykey.lock.mvp.view.IDeviceMoreView;
import com.philips.easykey.lock.publiclibrary.ble.BleCommandFactory;
import com.philips.easykey.lock.publiclibrary.ble.BleProtocolFailedException;
import com.philips.easykey.lock.publiclibrary.ble.RetryWithTime;
import com.philips.easykey.lock.publiclibrary.ble.responsebean.BleDataBean;
import com.philips.easykey.lock.publiclibrary.ble.responsebean.ReadInfoBean;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.rxutils.TimeOutException;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.greenDao.db.BleLockServiceInfoDao;
import com.philips.easykey.lock.utils.greenDao.db.DaoSession;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Create By lxj  on 2019/3/4
 * Describe
 */
public class BleDeviceMorePresenter<T> extends BleCheckOTAPresenter<IDeviceMoreView> {

    private Disposable getDeviceInfoDisposable;
    private Disposable voiceDisposable;
    private Disposable autoLockDisposable;
    private Disposable readSerialNumberDisposable;
    private Disposable otaDisposable;
    private Disposable readSoftwareRevDisposable;

    private Disposable deviceStateChangeDisposable;
    private Disposable checkModuleNumberDisposable;
    private Disposable checkModuleVersionDisposable;

    public void deleteDevice(String deviceName) {
        XiaokaiNewServiceImp.deleteDevice(MyApplication.getInstance().getUid(), deviceName)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        //清除消息免打扰
                        SPUtils.remove(deviceName + SPUtils.MESSAGE_STATUS);
                        SPUtils.remove(KeyConstants.SAVE_PWD_HEARD + bleLockInfo.getServerLockInfo().getMacLock()); //Key删除设备
                        MyApplication.getInstance().getAllDevicesByMqtt(true);

                        //通知homeFragment  和  device刷新界面
                        LogUtils.d("删除设备  断开连接");
                        bleService.release();  //删除设备  断开连接
//                        MyApplication.getInstance().deleteDevice(deviceName);
                        bleService.removeBleLockInfo();
                        ///删除缓存的消息
                        DaoSession daoSession = MyApplication.getInstance().getDaoWriteSession();
                        BleLockServiceInfoDao bleLockServiceInfoDao = daoSession.getBleLockServiceInfoDao();
                        bleLockServiceInfoDao.queryBuilder().where(BleLockServiceInfoDao.Properties.LockName.eq(bleLockInfo.getServerLockInfo().getLockName())).buildDelete().executeDeleteWithoutDetachingEntities();
                        // 做完所有操作再跳转界面
                        if (isSafe()) {
                            mViewRef.get().onDeleteDeviceSuccess();
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {
                            mViewRef.get().onDeleteDeviceFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().onDeleteDeviceFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });

    }

    public void getDeviceInfo() {
        byte[] command = BleCommandFactory.syncLockInfoCommand(bleLockInfo.getAuthKey());  //4
        bleService.sendCommand(command);
        toDisposable(getDeviceInfoDisposable);
        /**
         * 门锁状态
         * bit0：锁斜舌状态     =0：Lock     =1：Unlock – 阻塞（Blocked）
         * bit1：主锁舌（联动锁舌）状态    =0：Lock     =1：Unlock
         * bit2：反锁（独立锁舌）状态     =0：Lock     =1：Unlock
         * bit3：门状态                    =0：Lock    =1：Unlock
         * bit4：门磁状态       =0：Close        =1：Open
         * bit5：安全模式       =0：不启用或不支持      =1：启用安全模式
         * bit6：默认管理密码         =0：出厂密码         =1：已修改
         * bit7：手自动模式（LockFun：bit10=1）     =0：手动       =1：自动
         * bit8：布防状态（LockFun：bit4=1）       =0：未布防      =1：已布防
         */
        getDeviceInfoDisposable = bleService.listeneDataChange()
                .filter(new Predicate<BleDataBean>() {
                    @Override
                    public boolean test(BleDataBean bleDataBean) throws Exception {
                        return command[1] == bleDataBean.getTsn();
                    }
                })
                .compose(RxjavaHelper.observeOnMainThread())
                .timeout(5 * 1000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<BleDataBean>() {
                    @Override
                    public void accept(BleDataBean bleDataBean) throws Exception {
                        if (bleDataBean.isConfirm()) {
                            return;
                        }
                        //判断是否是当前指令
                        if (bleDataBean.getCmd() != command[3]) {
                            return;
                        }

                        byte[] deValue = Rsa.decrypt(bleDataBean.getPayload(), bleLockInfo.getAuthKey());
                        byte lockState = deValue[4]; //第五个字节为锁状态信息

                        int voice = deValue[8] & 0xff;  //是否是静音模式 0静音  1有声音
                        LogUtils.d("获取到音量   " + voice);
                        String lang = new String(new byte[]{deValue[9], deValue[10]});  //语言设置
                        int battery = deValue[11] & 0xff; //电量
                        if (bleLockInfo.getBattery() == -1) {
                            bleLockInfo.setBattery(battery);
                        }
                        byte[] time = new byte[]{deValue[12], deValue[13], deValue[14], deValue[15]};  //锁的时间
                        long time1 = Rsa.bytes2Int(time);
                        //开门时间秒
                        long openTimes = time1 + BleCommandFactory.defineTime;
                        String lockTime = DateUtils.getDateTimeFromMillisecond(openTimes * 1000);//要上传的开锁时间
                        LogUtils.d("锁上时间为    " + lockTime);
                        toDisposable(getDeviceInfoDisposable);
                        if (isSafe()) {
                            mViewRef.get().getVoice(voice);
                        }
                        byte[] bytes = Rsa.byteToBit(deValue[4]);
                        int openLock = bytes[0];
//                        0：手动 1：自动
                        boolean isOpen = openLock == 1 ? true : false;
                        if (isSafe()) {
                            mViewRef.get().getAutoLock(isOpen);
                        }
                        //如果获取锁信息成功，那么直接获取开锁次数
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
        compositeDisposable.add(getDeviceInfoDisposable);
    }

    public void modifyDeviceNickname(String devname, String user_id, String lockNickName) {
        XiaokaiNewServiceImp.modifyLockNick(devname, user_id, lockNickName).subscribe(new BaseObserver<BaseResult>() {
            @Override
            public void onSuccess(BaseResult baseResult) {
                if (isSafe()) {
                    mViewRef.get().modifyDeviceNicknameSuccess();
                }
                bleLockInfo.getServerLockInfo().setLockNickName(lockNickName);
                bleService.getBleLockInfo().getServerLockInfo().setLockNickName(lockNickName);
                MyApplication.getInstance().getAllDevicesByMqtt(true);
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {
                if (isSafe()) {
                    mViewRef.get().modifyDeviceNicknameFail(baseResult);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (isSafe()) {
                    mViewRef.get().modifyDeviceNicknameError(throwable);
                }
            }

            @Override
            public void onSubscribe1(Disposable d) {
                compositeDisposable.add(d);
            }
        });
    }

    @Override
    public void authSuccess() {
        getDeviceInfo();
    }


    /**
     * 设置音量
     *
     * @param voice 0 静音  1 低音量  2高音量
     */
    public void setVoice(int voice) {
        byte[] command = BleCommandFactory.setLockParamsCommand((byte) 0x02, new byte[]{(byte) voice}, bleLockInfo.getAuthKey());
        bleService.sendCommand(command);
        toDisposable(voiceDisposable);
        voiceDisposable = bleService.listeneDataChange()
                .filter(new Predicate<BleDataBean>() {
                    @Override
                    public boolean test(BleDataBean bleDataBean) throws Exception {
                        return command[1] == bleDataBean.getTsn();
                    }
                })
                .compose(RxjavaHelper.observeOnMainThread())
                .subscribe(new Consumer<BleDataBean>() {
                    @Override
                    public void accept(BleDataBean bleDataBean) throws Exception {
                        if (bleDataBean.isConfirm()) { //设置成功
                            if (bleDataBean.getPayload()[0] == 0) {
                                if (isSafe()) {
                                    mViewRef.get().setVoiceSuccess(voice);
                                }
                            } else {  //设置失败
                                if (isSafe()) {
                                    mViewRef.get().setVoiceFailed(new BleProtocolFailedException(0xff & bleDataBean.getPayload()[0]), voice);
                                }
                            }
                            toDisposable(voiceDisposable);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (isSafe()) {
                            mViewRef.get().setVoiceFailed(throwable, voice);
                        }
                    }
                });
        compositeDisposable.add(voiceDisposable);
    }

    /**
     * 自动关门
     * 0x00：开启
     * 0x01：关闭
     */
    public void setAutoLock(boolean isOpen) {
        byte[] command;
        if (isOpen) {
            command = BleCommandFactory.setLockParamsCommand((byte) 0x04, new byte[]{(byte) 0}, bleLockInfo.getAuthKey());
        } else {
            command = BleCommandFactory.setLockParamsCommand((byte) 0x04, new byte[]{(byte) 1}, bleLockInfo.getAuthKey());
        }

        bleService.sendCommand(command);
        toDisposable(autoLockDisposable);
        autoLockDisposable = bleService.listeneDataChange()
                .filter(new Predicate<BleDataBean>() {
                    @Override
                    public boolean test(BleDataBean bleDataBean) throws Exception {
                        return command[1] == bleDataBean.getTsn();
                    }
                })
                .compose(RxjavaHelper.observeOnMainThread())
                .subscribe(new Consumer<BleDataBean>() {
                    @Override
                    public void accept(BleDataBean bleDataBean) throws Exception {
                        int b = bleDataBean.getPayload()[0] & 0xff;
                     /*   0x00	成功
                        0x01	失败
                        0x85	某个字段错误
                        0x94	超时
                        0x9A	命令正在执行（TSN重复）
                        0xC2	校验错误
                        0xFF	锁接收到命令，但无结果返回*/
                        if (bleDataBean.getOriginalData()[0] == 0) {
                            if (0 == b) {
                                if (isSafe()) {
                                    mViewRef.get().setAutoLockSuccess(isOpen);
                                }
                            } else {
                                if (isSafe()) {
                                    mViewRef.get().setAutoLockFailed(b);
                                }
                            }
                            toDisposable(autoLockDisposable);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (isSafe()) {
                            mViewRef.get().setAutoLockError(throwable);
                        }
                    }
                });
        compositeDisposable.add(autoLockDisposable);
    }


    public void readSerialNumber() {
        toDisposable(readSerialNumberDisposable);
        readSerialNumberDisposable = Observable.just(0)
                .flatMap(new Function<Integer, ObservableSource<ReadInfoBean>>() {
                    @Override
                    public ObservableSource<ReadInfoBean> apply(Integer integer) throws Exception {
                        return bleService.readSN(500);
                    }
                })
                .filter(new Predicate<ReadInfoBean>() {
                    @Override
                    public boolean test(ReadInfoBean readInfoBean) throws Exception {
                        if (readInfoBean.type == ReadInfoBean.TYPE_SN) {
                            return true;
                        }
                        return false;
                    }
                })
                .timeout(2000, TimeUnit.MILLISECONDS)         //2秒没有读取到SerialNumber  则认为超时
                .retryWhen(new RetryWithTime(2, 0))
                .compose(RxjavaHelper.observeOnMainThread())
                .subscribe(new Consumer<ReadInfoBean>() {
                    @Override
                    public void accept(ReadInfoBean readInfoBean) throws Exception {
                        LogUtils.d("读取SerialNumber成功 " + readInfoBean.data);  //进行下一步
                        bleLockInfo.setSerialNumber((String) readInfoBean.data);
                        if (isSafe()) {
                            mViewRef.get().readSnSuccess((String) readInfoBean.data);
                        }
                        toDisposable(readSerialNumberDisposable);
                        readSoftwareRev((String) readInfoBean.data);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.d(" 读取SerialNumber失败  " + (throwable instanceof TimeOutException) + "   " + throwable.getMessage());
                        if (isSafe()) {
                            mViewRef.get().readInfoFailed(throwable);
                        }
                    }
                });

        compositeDisposable.add(readSerialNumberDisposable);
    }


    private void readSoftwareRev(String sn) {
        toDisposable(readSoftwareRevDisposable);
        readSoftwareRevDisposable = Observable.just(0)
                .flatMap(new Function<Integer, ObservableSource<ReadInfoBean>>() {
                    @Override
                    public ObservableSource<ReadInfoBean> apply(Integer integer) throws Exception {
                        return bleService.readBleVersion();
                    }
                })
                .filter(new Predicate<ReadInfoBean>() {
                    @Override
                    public boolean test(ReadInfoBean readInfoBean) throws Exception {
                        if (readInfoBean.type == ReadInfoBean.TYPE_BLEINFO) {
                            return true;
                        }
                        return false;
                    }
                })
                .timeout(2000, TimeUnit.MILLISECONDS)         //2秒没有读取到SoftwareRev  则认为超时
                .retryWhen(new RetryWithTime(2, 0))
                .compose(RxjavaHelper.observeOnMainThread())
                .subscribe(new Consumer<ReadInfoBean>() {
                    @Override
                    public void accept(ReadInfoBean readInfoBean) throws Exception {
                        LogUtils.d(" 读取SoftwareRev成功 " + readInfoBean.data);  //进行下一步
                        bleLockInfo.setSoftware((String) readInfoBean.data);
                        String version = (String) readInfoBean.data;
                        if (version.contains("-")) {
                            String[] split = version.split("-");
                            if (split.length > 0) {
                                version = split[0];
                            }
                        }
                        toDisposable(readSoftwareRevDisposable);
                        if (isSafe()) {
                            mViewRef.get().readVersionSuccess(version);
                        }
                        LogUtils.d("获取到的版本号是   " + version);
                        if (version.length() >= 9) {
                            version = version.substring(1, 9);
                        }
                        String serverBleVersion = bleLockInfo.getServerLockInfo().getSoftwareVersion();
                        String deviceSN = bleLockInfo.getServerLockInfo().getDeviceSN();
                        LogUtils.d("服务器数据是  serverBleVersion " + serverBleVersion + "  deviceSN  " + deviceSN + "  本地数据是  sn " + sn + "  version " + version);
                        if (version.equals(serverBleVersion) && sn.equals(deviceSN)) {
                            checkOTAInfo(sn, version, 1);
                        } else {
                            uploadBleSoftware(sn, version);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        LogUtils.d(" 读取SoftwareRev失败  " + (throwable instanceof TimeOutException) + "   " + throwable.getMessage());
                        if (isSafe()) {
                            mViewRef.get().readInfoFailed(throwable);
                        }
                    }
                });
        compositeDisposable.add(readSoftwareRevDisposable);
    }


    public void uploadBleSoftware(String sn, String version) {
        XiaokaiNewServiceImp.updateSoftwareVersion(
                bleLockInfo.getServerLockInfo().getLockName(), MyApplication.getInstance().getUid()
                , version, sn
        ).subscribe(new BaseObserver<BaseResult>() {
            @Override
            public void onSuccess(BaseResult baseResult) {
                LogUtils.d("上传蓝牙信息成功");
                checkOTAInfo(sn, version, 1);
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {
                LogUtils.d(" 上传蓝牙软件信息失败  " + baseResult.getCode());
                if (isSafe()) {
                    mViewRef.get().onUpdateSoftFailedServer(baseResult);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (isSafe()) {
                    mViewRef.get().onUpdateSoftFailed(throwable);
                }
            }

            @Override
            public void onSubscribe1(Disposable d) {

            }
        });
    }


    @Override
    public void attachView(IDeviceMoreView view) {
        super.attachView(view);
        //通知界面更新显示设备状态
        if(bleService != null){
            toDisposable(deviceStateChangeDisposable);
            deviceStateChangeDisposable = bleService.listenerDeviceStateChange()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<BleDataBean>() {
                        @Override
                        public void accept(BleDataBean bleDataBean) throws Exception {
                            if (isSafe()) {   //通知界面更新显示设备状态
                                mViewRef.get().onStateUpdate(-1);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.d("监听设备状态改变出错   " + throwable.toString());
                        }
                    });
            compositeDisposable.add(deviceStateChangeDisposable);
        }
    }
}
