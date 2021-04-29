package com.philips.easykey.lock.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.bean.WifiLockActionBean;
import com.philips.easykey.lock.mvp.mvpbase.BlePresenter;
import com.philips.easykey.lock.mvp.view.IMainActivityView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.GatewayInfo;
import com.philips.easykey.lock.publiclibrary.bean.GwLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.ProductInfo;
import com.philips.easykey.lock.publiclibrary.bean.ServerGatewayInfo;
import com.philips.easykey.lock.publiclibrary.bean.ServerGwDevice;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.publiclibrary.ble.bean.NewVersionBean;
import com.philips.easykey.lock.publiclibrary.ble.responsebean.BleDataBean;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.ServerBleDevice;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.DeleteDeviceLockBean;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.GatewayLockAlarmEventBean;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.GatewayLockInfoEventBean;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.GatewayResetBean;
import com.philips.easykey.lock.publiclibrary.mqtt.eventbean.WifiLockAlarmBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GatewayOtaNotifyBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GetBindGatewayStatusResult;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.MMKVUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.ftp.GeTui;
import com.philips.easykey.lock.utils.greenDao.bean.BleLockServiceInfo;
import com.philips.easykey.lock.utils.greenDao.bean.ClothesHangerMachineAllBean;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayLockAlarmEventDao;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayLockPwd;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayLockServiceInfo;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayPasswordPlanBean;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayServiceInfo;
import com.philips.easykey.lock.utils.greenDao.db.BleLockServiceInfoDao;
import com.philips.easykey.lock.utils.greenDao.db.DaoSession;
import com.philips.easykey.lock.utils.greenDao.db.GatewayLockAlarmEventDaoDao;
import com.philips.easykey.lock.utils.greenDao.db.GatewayLockPwdDao;
import com.philips.easykey.lock.utils.greenDao.db.GatewayLockServiceInfoDao;
import com.philips.easykey.lock.utils.greenDao.db.GatewayServiceInfoDao;
import com.philips.easykey.lock.utils.greenDao.manager.GatewayLockPasswordManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;

/**
 * Create By lxj  on 2019/3/18
 * Describe
 */
public class MainActivityPresenter<T> extends BlePresenter<IMainActivityView> {

    private Disposable warringDisposable;
    private Disposable deviceInBootDisposable;
    private Disposable disposable;
    private Disposable catEyeEventDisposable;
    private Disposable listerBleVersionDisposable;
    private Disposable listenerGatewayOtaDisposable;
    private Disposable comfirmGatewayOtaDisposable;
    private Disposable gatewayResetDisposable;
    private Context mContext;
    private Disposable getLockPwdInfoEventDisposable;
    private GatewayLockPasswordManager manager = new GatewayLockPasswordManager();
    private Disposable wifiLockStatusListenDisposable;
    private Disposable allDeviceDisposable;

    public MainActivityPresenter(Context mContext) {
        this.mContext = mContext;
    }


    @Override
    public void authSuccess() {

    }

    @Override
    public void attachView(IMainActivityView view) {
        super.attachView(view);
        //网关上线监听
        getPublishNotify();
        //从Dao中获取蓝牙、网关、猫眼、wifi锁的昵称等信息在UI上显示
        setHomeShowBean();
        //从Dao中产品型号信息列表
        setProductInfo();
        //监听猫眼锁的报警信息
        listenCatEyeEvent();
        //监听是否有新版本
        listerBleVersion();
        //监听网关ota升级
        listenGatewayOTA();
        //监听网关重置上报
        gatewayResetListener();
        //监听密码的信息
        getLockPwdInfoEvent();
        //监听wifi锁  报警信息
        listenWifiLockStatus();
    }

    /**
     * 监听网关ota升级通知
     */
    private void listenGatewayOTA() {
        if (mqttService != null) {
            toDisposable(listenerGatewayOtaDisposable);
            listenerGatewayOtaDisposable = mqttService.listenerDataBack()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if (mqttData != null) {
                                if (MqttConstant.NOTIFY_GATEWAY_OTA.equals(mqttData.getFunc())) {
                                    //接收到网关ota升级
                                    GatewayOtaNotifyBean gatewayOtaNotifyBean = new Gson().fromJson(mqttData.getPayload(), GatewayOtaNotifyBean.class);
                                    if (isSafe()) {
                                        mViewRef.get().gatewayNotifyOtaSuccess(gatewayOtaNotifyBean);
                                    }

                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
            compositeDisposable.add(listenerGatewayOtaDisposable);
        }
    }

    private void listerBleVersion() {
        //监听是否有新版本
        if (bleService != null) {
            toDisposable(listerBleVersionDisposable);
            listerBleVersionDisposable = bleService.listenBleVersionUpdate()
                    .subscribe(new Consumer<NewVersionBean>() {
                        @Override
                        public void accept(NewVersionBean newVersionBean) throws Exception {
                            updateBleVersion(newVersionBean.getDevname(), newVersionBean.getBleVersion());

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {


                        }
                    });
            compositeDisposable.add(listerBleVersionDisposable);

            //设置警报提醒
            toDisposable(warringDisposable);
            warringDisposable = bleService.listeneDataChange()
                    .filter(new Predicate<BleDataBean>() {
                        @Override
                        public boolean test(BleDataBean bleDataBean) throws Exception {
                            //最新的蓝牙模块才有报警提示
                            return bleDataBean.getCmd() == 0x07 && MyApplication.getInstance().getBleService().getBleVersion() == 3;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<BleDataBean>() {
                        @Override
                        public void accept(BleDataBean bleDataBean) throws Exception {
                            if (!MyApplication.getInstance().getBleService().getBleLockInfo().isAuth() || MyApplication.getInstance().getBleService().getBleLockInfo().getAuthKey() == null || MyApplication.getInstance().getBleService().getBleLockInfo().getAuthKey().length == 0) {
                                LogUtils.d("收到报警记录，但是鉴权帧为空");
                                return;
                            }
                            bleDataBean.getDevice().getName();
                            bleDataBean.getCmd();
                            byte[] deValue = Rsa.decrypt(bleDataBean.getPayload(), MyApplication.getInstance().getBleService().getBleLockInfo().getAuthKey());
                            LogUtils.d("收到报警上报    " + Rsa.toHexString(deValue));
                            String nickNameByDeviceName = getNickNameByDeviceName(bleDataBean.getDevice().getName());
                            int state9 = (deValue[5] & 0b00000010) == 0b00000010 ? 1 : 0;
                            MyApplication.getInstance().getBleService().getBleLockInfo().setSafeMode(state9);
                            if (!TextUtils.isEmpty(nickNameByDeviceName)) {
                                String warringContent = BleUtil.parseWarring(MyApplication.getInstance(), deValue, nickNameByDeviceName);
                                if (isSafe() && !TextUtils.isEmpty(warringContent)) {
                                    mViewRef.get().onWarringUp(warringContent);
                                }
                            } else {
                                LogUtils.d("收到报警记录，但是缓存信息中没有该设备  ");
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
            compositeDisposable.add(warringDisposable);

            //监听是否是boot模式
            toDisposable(deviceInBootDisposable);
            deviceInBootDisposable = bleService.onDeviceStateInBoot()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<BleLockInfo>() {
                        @Override
                        public void accept(BleLockInfo bleLockInfo) throws Exception {
                            LogUtils.d("设备  正在升级模式   " + bleLockInfo.getServerLockInfo().toString());
                            if (isSafe()) {
                                mViewRef.get().onDeviceInBoot(bleLockInfo);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.d("设备   正在升级模式   监听失败    " + bleLockInfo.getServerLockInfo().toString());
                        }
                    });
            compositeDisposable.add(deviceInBootDisposable);
        }
    }


    public void updateBleVersion(String deviceNam, String version) {
        XiaokaiNewServiceImp.updateBleVersion(deviceNam, MyApplication.getInstance().getUid(), version)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult baseResult) {
                        LogUtils.d("更新版本号成功   " + baseResult.toString());
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        LogUtils.d("更新版本号失败   " + baseResult.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        LogUtils.d("更新版本号失败   " + throwable.getMessage());
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }


    public String getNickNameByDeviceName(String name) {
        List<HomeShowBean> homeShowDevices = MyApplication.getInstance().getHomeShowDevices();
        for (HomeShowBean homeShowBean : homeShowDevices) {
            if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_BLE_LOCK) {
                BleLockInfo bleLockInfo = (BleLockInfo) homeShowBean.getObject();
                if (bleLockInfo.getServerLockInfo().getLockName().equals(name)) {
                    return bleLockInfo.getServerLockInfo().getLockNickName();
                }
            }
        }
        return "";
    }

    //获取网关状态通知
    public void getPublishNotify() {
        toDisposable(disposable);
        if (mqttService != null) {
            disposable = mqttService.listenerNotifyData()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if (mqttData != null) {
                                if (MqttConstant.GATEWAY_STATE.equals(mqttData.getFunc())) {
                                    GetBindGatewayStatusResult gatewayStatusResult = new Gson().fromJson(mqttData.getPayload(), GetBindGatewayStatusResult.class);
                                    LogUtils.d("监听网关的状态" + gatewayStatusResult.getDevuuid());
                                    if (gatewayStatusResult != null) {
                                        List<HomeShowBean> homeShowBeans = MyApplication.getInstance().getAllDevices();
                                        SPUtils.put(gatewayStatusResult.getDevuuid(), gatewayStatusResult.getData().getState());
                                    }
                                }
                            }
                        }
                    });
            compositeDisposable.add(disposable);
        }
    }

    //猫眼信息上报
    public void listenCatEyeEvent() {
        toDisposable(catEyeEventDisposable);
        if (mqttService != null) {
            catEyeEventDisposable = mqttService.listenerDataBack()
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.EVENT.equals(mqttData.getMsgtype())
                                    && MqttConstant.GW_EVENT.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            JSONObject jsonObject = new JSONObject(mqttData.getPayload());
                            String devtype = jsonObject.getString("devtype");
                            String eventtype = jsonObject.getString("eventtype");
                            if (TextUtils.isEmpty(devtype)) { //devtype为空   无法处理数据
                                return;
                            }

                            if (KeyConstants.DEV_TYPE_LOCK.equals(devtype)) {
                                //保存告警信息
                                if ("alarm".equals(eventtype)) {
                                    GatewayLockAlarmEventBean gatewayLockAlarmEventBean = new Gson().fromJson(mqttData.getPayload(), GatewayLockAlarmEventBean.class);
                                    if (gatewayLockAlarmEventBean.getEventparams() != null && gatewayLockAlarmEventBean.getEventparams().getAlarmCode() != 0 && gatewayLockAlarmEventBean.getEventparams().getClusterID() != 0) {
                                        //保存到数据库
                                        int alarmCode = gatewayLockAlarmEventBean.getEventparams().getAlarmCode();
                                        String deviceId = gatewayLockAlarmEventBean.getDeviceId();
                                        String gatewayId = gatewayLockAlarmEventBean.getGwId();
                                        int clusterID = gatewayLockAlarmEventBean.getEventparams().getClusterID();
                                        GatewayLockAlarmEventDao gatewayLockAlarmEventDao = new GatewayLockAlarmEventDao();
                                        gatewayLockAlarmEventDao.setDeviceId(deviceId); //设备id
                                        gatewayLockAlarmEventDao.setGatewayId(gatewayLockAlarmEventBean.getGwId()); //网关id
                                        gatewayLockAlarmEventDao.setTimeStamp(gatewayLockAlarmEventBean.getTimestamp()); //时间戳
                                        gatewayLockAlarmEventDao.setDevtype(gatewayLockAlarmEventBean.getDevtype()); //设备类型
                                        gatewayLockAlarmEventDao.setAlarmCode(alarmCode); //报警代码
                                        if (alarmCode == 1 && clusterID == 257) {
                                            //锁重置
                                            MyApplication.getInstance().getAllDevicesByMqtt(true);
                                            //删除锁的全部密码
                                            deleteAllPwd(gatewayId, deviceId, MyApplication.getInstance().getUid());

                                        }
                                        gatewayLockAlarmEventDao.setClusterID(clusterID); //257 代表锁的信息;1 代表电量信息
                                        gatewayLockAlarmEventDao.setEventcode(gatewayLockAlarmEventBean.getEventcode());
                                        //插入到数据库
                                        if (!checkSame(gatewayLockAlarmEventBean.getTimestamp(), gatewayLockAlarmEventBean.getEventparams().getAlarmCode())) {
                                            MyApplication.getInstance().getDaoWriteSession().getGatewayLockAlarmEventDaoDao().insert(gatewayLockAlarmEventDao);
                                            if (isSafe()) {
                                                mViewRef.get().onGwLockEvent(alarmCode, clusterID, deviceId, gatewayId);
                                            }
                                        }

                                    } else {
                                        DeleteDeviceLockBean deleteGatewayLockDeviceBean = new Gson().fromJson(mqttData.getPayload(), DeleteDeviceLockBean.class);
                                        if (deleteGatewayLockDeviceBean != null) {
                                            if ("zigbee".equals(deleteGatewayLockDeviceBean.getEventparams().getDevice_type()) && deleteGatewayLockDeviceBean.getEventparams().getEvent_str().equals("delete")) {
                                                refreshData(deleteGatewayLockDeviceBean.getGwId(), deleteGatewayLockDeviceBean.getDeviceId());

                                            }
                                        }
                                    }
                                } else if ("info".equals(eventtype)) {
                                    GatewayLockInfoEventBean gatewayLockInfoEventBean = new Gson().fromJson(mqttData.getPayload(), GatewayLockInfoEventBean.class);
                                    String gatewayId = gatewayLockInfoEventBean.getGwId();
                                    String deviceId = gatewayLockInfoEventBean.getDeviceId();
                                    String uid = MyApplication.getInstance().getUid();
                                    String eventParmDeveType = gatewayLockInfoEventBean.getEventparams().getDevetype();
                                    int num = gatewayLockInfoEventBean.getEventparams().getUserID();
                                    int devecode = gatewayLockInfoEventBean.getEventparams().getDevecode();
                                    int pin = gatewayLockInfoEventBean.getEventparams().getPin();
                                    if (eventParmDeveType.equals("lockprom") && devecode == 2 && pin == 255) {
                                        //添加单个密码
                                        //删除密码
                                        deleteOnePwd(gatewayId, deviceId, uid, "0" + num);
                                        //添加
                                        AddOnePwd(gatewayId, deviceId, uid, "0" + num);
                                        LogUtils.d("单个添加");
                                    } else if (eventParmDeveType.equals("lockprom") && devecode == 3 && num == 255 && pin == 255) {
                                        //全部删除
                                        deleteAllPwd(gatewayId, deviceId, uid);
                                        LogUtils.d("全部删除");
                                    } else if (eventParmDeveType.equals("lockprom") && devecode == 3 && pin == 255) {
                                        //删除单个密码
                                        deleteOnePwd(gatewayId, deviceId, uid, "0" + num);
                                        LogUtils.d("删除单个密码");
                                    } else if (eventParmDeveType.equals("lockop") && devecode == 2 && pin == 255) {
                                        //使用一次性开锁密码
                                        if (num > 4 && num <= 8) {
                                            deleteOnePwd(gatewayId, deviceId, uid, "0" + num);
                                            LogUtils.d("使用一次性开锁");
                                        }
                                        LogUtils.d("开锁上报");
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.d("报警消息失败   " + throwable.getMessage());
                        }
                    });
            compositeDisposable.add(catEyeEventDisposable);
        }
    }

    private void refreshData(String gatewayId, String deviceId) {
        GatewayInfo gatewayInfo = MyApplication.getInstance().getGatewayById(gatewayId);
        if (gatewayInfo != null) {
            MyApplication.getInstance().getAllDevicesByMqtt(true);
        }
    }

    // 个推 2
    // 华为 3
    // 小米 4
    public void uploadpushmethod(int type , String JpushId) {
        String uid = MMKVUtils.getStringMMKV(SPUtils.UID);
    //    String JpushId = (String) SPUtils2.get(MyApplication.getInstance(), GeTui.JPUSH_ID, "");
        //uploadPushId(String uid, String jpushId, int type)
        // 个推
     //   int type = 2;
     //   String phoneType = "other";
        //  华为
//        if (Rom.isEmui()) {
//            String token = (String) SPUtils.get(GeTui.HUAWEI_KEY, "");
//            if (!TextUtils.isEmpty(token)) {
//                JpushId = token;
//                type = 3;
//                phoneType = GeTui.HUAWEI_KEY;
//            } else {
//                Log.e(GeTui.VideoLog, "huawei-->MainAcvitiyPresenter=>获取token为null..上传失败");
//                return;
//            }
//        }
       // Log.e(GeTui.VideoLog, "MainActivityPresenter-->phoneType:" + phoneType + " uid:" + uid + " jpushid:" + JpushId + " token:" + MyApplication.getInstance().getToken());
        LogUtils.d(GeTui.VideoLog, "uid:" + uid + " JpushId:" + JpushId);
        LogUtils.d("shulan------"+ "uid:" + uid + " JpushId:" + JpushId);
        if (!TextUtils.isEmpty(uid) && !TextUtils.isEmpty(JpushId)) {
            XiaokaiNewServiceImp.uploadPushId(uid, JpushId, type).subscribe(new BaseObserver<BaseResult>() {
                @Override
                public void onSuccess(BaseResult baseResult) {
                    if (mViewRef != null) {
                        mViewRef.get().uploadpush(baseResult);

                    }
                }

                @Override
                public void onAckErrorCode(BaseResult baseResult) {
                    Log.e(GeTui.VideoLog, "pushid上传失败,服务返回:" + baseResult);
                }

                @Override
                public void onFailed(Throwable throwable) {
                    Log.e(GeTui.VideoLog, "pushid上传失败");
                }

                @Override
                public void onSubscribe1(Disposable d) {
                    compositeDisposable.add(d);
                }
            });
        }else {
            LogUtils.d("jpushid上传失败");
        }

    }


    public void uploadPhoneMessage() {
        String uid = MMKVUtils.getStringMMKV(SPUtils.UID);
        String phone = (String) SPUtils.get(SPUtils.PHONEN, "");
        // 获取Android系统版本号 android8.0
        String version = android.os.Build.VERSION.RELEASE;
        // 手机型号  SM-C7000
        String model = android.os.Build.MODEL;
        //手机厂商 samsung
        String manufacturer = android.os.Build.BRAND;
        if (TextUtils.isEmpty(uid) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(version) || TextUtils.isEmpty(model) || TextUtils.isEmpty(manufacturer)) {
            Log.e(GeTui.VideoLog, "信息为null,上传手机信息失败");
            return;
        }
        XiaokaiNewServiceImp.uploadPushPhoneMsg(uid, phone, model, manufacturer, version)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult baseResult) {
                        if (baseResult.getCode().equals("200")) {
                            SPUtils.put(Constants.PHONE_MSG_UPLOAD_STATUS, true);
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }


    @Override
    public void detachView() {
        super.detachView();
        if (bleService != null) {
            LogUtils.d("MainActivity 退出界面   斷開連接");
            bleService.release();  //MainActivity 退出界面   斷開連接
        }
        handler.removeCallbacksAndMessages(null);
    }


    //添加某个
    private void AddOnePwd(String gatewayId, String deviceId, String uid, String num) {
        GatewayLockPwdDao gatewayLockPwdDao = MyApplication.getInstance().getDaoWriteSession().getGatewayLockPwdDao();
        GatewayLockPwd gatewayLockPwd = new GatewayLockPwd();
        gatewayLockPwd.setUid(uid);
        gatewayLockPwd.setNum(num);
        gatewayLockPwd.setStatus(1);
        gatewayLockPwd.setName("");
        gatewayLockPwd.setGatewayId(gatewayId);
        gatewayLockPwd.setDeviceId(deviceId);
        Integer keyInt = Integer.parseInt(num);
        //用于zigbee锁是根据编号识别是永久密码，临时密码，还是胁迫密码
        if (keyInt <= 4) {
            gatewayLockPwd.setTime(1);
        } else if (keyInt <= 8 && keyInt > 4) {
            gatewayLockPwd.setTime(2);
        } else if (keyInt == 9) {
            gatewayLockPwd.setTime(3);
        }
        if (gatewayLockPwdDao != null) {
            gatewayLockPwdDao.insert(gatewayLockPwd);
        }


    }

    //删除某个数据
    private void deleteOnePwd(String gatewayId, String deviceId, String uid, String num) {
        GatewayLockPwdDao gatewayLockPwdDao = MyApplication.getInstance().getDaoWriteSession().getGatewayLockPwdDao();
        GatewayLockPwd gatewayLockPwd = gatewayLockPwdDao.queryBuilder().where(GatewayLockPwdDao.Properties.GatewayId.eq(gatewayId), GatewayLockPwdDao.Properties.DeviceId.eq(deviceId), GatewayLockPwdDao.Properties.Uid.eq(uid), GatewayLockPwdDao.Properties.Num.eq(num)).unique();
        if (gatewayLockPwd != null) {
            gatewayLockPwdDao.delete(gatewayLockPwd);
        }
    }

    //删除该锁的所有密码
    private void deleteAllPwd(String gatewayId, String deviceId, String uid) {
        GatewayLockPwdDao gatewayLockPwdDao = MyApplication.getInstance().getDaoWriteSession().getGatewayLockPwdDao();
        List<GatewayLockPwd> gatewayLockPwdList = gatewayLockPwdDao.queryBuilder().where(GatewayLockPwdDao.Properties.GatewayId.eq(gatewayId), GatewayLockPwdDao.Properties.DeviceId.eq(deviceId), GatewayLockPwdDao.Properties.Uid.eq(uid)).list();
        if (gatewayLockPwdList != null && gatewayLockPwdList.size() > 0) {
            for (GatewayLockPwd gatewayLockPwd : gatewayLockPwdList) {
                gatewayLockPwdDao.delete(gatewayLockPwd);
            }
        }
    }

    //去掉时间戳相同
    public boolean checkSame(String time, int alarmCode) {
        GatewayLockAlarmEventDao gatewayLockAlarmEventDao = MyApplication.getInstance().getDaoWriteSession().queryBuilder(GatewayLockAlarmEventDao.class).where(GatewayLockAlarmEventDaoDao.Properties.TimeStamp.eq(time), GatewayLockAlarmEventDaoDao.Properties.AlarmCode.eq(alarmCode)).unique();
        if (gatewayLockAlarmEventDao != null) {
            return true;
        }
        return false;
    }

    //设置HomeShowBean
    public void setHomeShowBean() {
        DaoSession daoSession = MyApplication.getInstance().getDaoWriteSession();
        String uid = MyApplication.getInstance().getUid();
        List<HomeShowBean> homeShowBeans = new ArrayList<>();
        //获取WiFi锁
        if (daoSession != null && daoSession.getWifiLockInfoDao() != null) {
            List<WifiLockInfo> wifiLockInfos = daoSession.getWifiLockInfoDao().loadAll();
            if (wifiLockInfos != null && wifiLockInfos.size() > 0) {
                for (WifiLockInfo wifiLockInfo : wifiLockInfos) {
                    if(!TextUtils.isEmpty(wifiLockInfo.getDevice_did()) && !TextUtils.isEmpty(wifiLockInfo.getP2p_password())
                            && !TextUtils.isEmpty(wifiLockInfo.getDevice_sn()) && !TextUtils.isEmpty(wifiLockInfo.getMac())){
                        homeShowBeans.add(new HomeShowBean(HomeShowBean.TYPE_WIFI_VIDEO_LOCK, wifiLockInfo.getWifiSN(),
                                wifiLockInfo.getLockNickname(), wifiLockInfo));
                    }else{
                        homeShowBeans.add(new HomeShowBean(HomeShowBean.TYPE_WIFI_LOCK, wifiLockInfo.getWifiSN(),
                                wifiLockInfo.getLockNickname(), wifiLockInfo));
                    }
                }
            }
        }
        //获取晾衣机
        if(daoSession != null && daoSession.getClothesHangerMachineAllBeanDao() != null){
            List<ClothesHangerMachineAllBean> clothesHangerMachineAllBeans = daoSession.getClothesHangerMachineAllBeanDao().loadAll();
            if(clothesHangerMachineAllBeans != null && clothesHangerMachineAllBeans.size() > 0){
                for(ClothesHangerMachineAllBean clothesHangerMachineAllBean : clothesHangerMachineAllBeans){
                    homeShowBeans.add(new HomeShowBean(HomeShowBean.TYPE_CLOTHES_HANGER,clothesHangerMachineAllBean.getWifiSN(),
                            clothesHangerMachineAllBean.getHangerNickName(),clothesHangerMachineAllBean));
                }
            }
        }
        //获取网关锁
        if (daoSession != null && daoSession.getGatewayLockServiceInfoDao() != null) {
            List<GatewayLockServiceInfo> gatewayLockList = daoSession.getGatewayLockServiceInfoDao().queryBuilder().where(GatewayLockServiceInfoDao.Properties.Uid.eq(uid)).list();
            if (gatewayLockList != null && gatewayLockList.size() > 0) {
                for (GatewayLockServiceInfo gwLock : gatewayLockList) {
                    GwLockInfo gwLockInfo = new GwLockInfo(gwLock.getGatewayId(), new ServerGwDevice(gwLock.getSW(), gwLock.getDeviceId(),
                            gwLock.getDevice_type(), gwLock.getEvent_str(), gwLock.getIpaddr(),
                            gwLock.getMacaddr(), gwLock.getNickName(), gwLock.getTime()
                            , "", gwLock.getDelectTime(), gwLock.getLockversion(), gwLock.getModuletype()
                            , gwLock.getNwaddr(), gwLock.getOfflineTime(), gwLock.getOnlineTime(), gwLock.getShareFlag(), gwLock.getPushSwitch()
                    ));
                    homeShowBeans.add(new HomeShowBean(HomeShowBean.TYPE_GATEWAY_LOCK, gwLock.getDeviceId(),
                            gwLock.getNickName(), gwLockInfo));
                }
            }
        }
        //获取网关
        if (daoSession != null && daoSession.getGatewayServiceInfoDao() != null) {
            List<GatewayServiceInfo> gatewayServiceInfoList = daoSession.getGatewayServiceInfoDao().queryBuilder().where(GatewayServiceInfoDao.Properties.Uid.eq(uid)).list();
            if (gatewayServiceInfoList != null && gatewayServiceInfoList.size() > 0) {
                for (GatewayServiceInfo gatewayServiceInfo : gatewayServiceInfoList) {
                    GatewayInfo newGatewayInfo = new GatewayInfo(new ServerGatewayInfo(gatewayServiceInfo.getDeviceSN(), gatewayServiceInfo.getDeviceNickName(), gatewayServiceInfo.getAdminuid(), gatewayServiceInfo.getAdminName(), gatewayServiceInfo.getAdminNickname(), gatewayServiceInfo.getIsAdmin(), gatewayServiceInfo.getMeUsername(), gatewayServiceInfo.getMePwd(), gatewayServiceInfo.getMeBindState()));
                    homeShowBeans.add(new HomeShowBean(HomeShowBean.TYPE_GATEWAY, gatewayServiceInfo.getDeviceSN(), gatewayServiceInfo.getDeviceNickName(), newGatewayInfo));
                }
            }
        }
        //获取蓝牙
        if (daoSession != null && daoSession.getBleLockServiceInfoDao() != null) {
            List<BleLockServiceInfo> bleLockList = daoSession.getBleLockServiceInfoDao().queryBuilder().where(BleLockServiceInfoDao.Properties.Uid.eq(uid)).list();
            if (bleLockList != null && bleLockList.size() > 0) {
                for (BleLockServiceInfo bleDevice : bleLockList) {
                    ServerBleDevice serverBleDevice = new ServerBleDevice();
                    serverBleDevice.setAuto_lock(bleDevice.getAuto_lock());
                    serverBleDevice.setCenter_latitude(bleDevice.getCenter_latitude());
                    serverBleDevice.setCenter_longitude(bleDevice.getCenter_longitude());
                    serverBleDevice.setCircle_radius(bleDevice.getCircle_radius());
                    serverBleDevice.setLockName(bleDevice.getLockName());
                    serverBleDevice.setLockNickName(bleDevice.getLockNickName());
                    serverBleDevice.setMacLock(bleDevice.getMacLock()); //设置值
                    serverBleDevice.setIs_admin(bleDevice.getIs_admin());
                    serverBleDevice.setModel(bleDevice.getModel());
                    serverBleDevice.setOpen_purview(bleDevice.getOpen_purview());
                    serverBleDevice.setPassword1(bleDevice.getPassword1());
                    serverBleDevice.setPassword2(bleDevice.getPassword2());
                    serverBleDevice.setBleVersion(bleDevice.getBleVersion());
                    serverBleDevice.setModel(bleDevice.getModel());
                    serverBleDevice.setDeviceSN(bleDevice.getDeviceSN());
                    serverBleDevice.setSoftwareVersion(bleDevice.getSoftwareVersion());
                    serverBleDevice.setBleVersion(bleDevice.getBleVersion());
                    serverBleDevice.setCreateTime(bleDevice.getCreateTime());

                    serverBleDevice.setFunctionSet(bleDevice.getFunctionSet());

                    BleLockInfo bleLockInfo = new BleLockInfo(serverBleDevice);
                    homeShowBeans.add(new HomeShowBean(HomeShowBean.TYPE_BLE_LOCK, bleDevice.getLockName(), bleDevice.getLockNickName(), bleLockInfo));
                }
            }
        }
        MyApplication.getInstance().setHomeShowDevice(homeShowBeans);

    }

    //设置产品型号信息列表
    public void setProductInfo() {
        DaoSession daoSession = MyApplication.getInstance().getDaoWriteSession();
        //获取产品型号信息列表
        if (daoSession != null && daoSession.getProductInfoDao() != null) {
            List<ProductInfo> ProductInfoList = daoSession.getProductInfoDao().loadAll();
            if (ProductInfoList != null && ProductInfoList.size() > 0) {
                MyApplication.getInstance().setProductInfos(ProductInfoList);
            }
        }
    }

    //监听网关重置上报
    public void gatewayResetListener() {
        if (mqttService != null) {
            toDisposable(gatewayResetDisposable);
            gatewayResetDisposable = mqttService.listenerDataBack()
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (mqttData.getFunc().equals(MqttConstant.GATEWAY_RESET)) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            GatewayResetBean gatewayResetBean = new Gson().fromJson(mqttData.getPayload(), GatewayResetBean.class);
                            if (gatewayResetBean != null && gatewayResetBean.getMsgtype().equals("event")) {
                                String gatewayId = gatewayResetBean.getDeviceId();
                                if (gatewayId != null) {
                                    GatewayInfo gatewayInfo = MyApplication.getInstance().getGatewayById(gatewayId);
                                    if (gatewayInfo != null) {
                                        if (isSafe()) {
                                            mViewRef.get().gatewayResetSuccess(gatewayId);
                                        }
                                        MyApplication.getInstance().getAllDevicesByMqtt(true);
                                    }
                                }
                            }
                        }
                    });
            compositeDisposable.add(gatewayResetDisposable);
        }
    }


    //监听密码的信息
    public void getLockPwdInfoEvent() {
        if (mqttService != null) {
            toDisposable(getLockPwdInfoEventDisposable);
            getLockPwdInfoEventDisposable = mqttService.listenerDataBack()
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (MqttConstant.EVENT.equals(mqttData.getMsgtype())
                                    && MqttConstant.GW_EVENT.equals(mqttData.getFunc())) {
                                return true;
                            }
                            return false;
                        }
                    })
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {
                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            JSONObject jsonObject = new JSONObject(mqttData.getPayload());
                            String devtype = jsonObject.getString("devtype");
                            String eventtype = jsonObject.getString("eventtype");
                            if (TextUtils.isEmpty(devtype)) { //devtype为空   无法处理数据
                                return;
                            }
                            //网关锁信息上报
                            if (KeyConstants.DEV_TYPE_LOCK.equals(devtype)) {
                                if ("info".equals(eventtype)) {
                                    GatewayLockInfoEventBean gatewayLockInfoEventBean = new Gson().fromJson(mqttData.getPayload(), GatewayLockInfoEventBean.class);
                                    String gatewayId = gatewayLockInfoEventBean.getGwId();
                                    String deviceId = gatewayLockInfoEventBean.getDeviceId();
                                    String uid = MyApplication.getInstance().getUid();
                                    String eventParmDeveType = gatewayLockInfoEventBean.getEventparams().getDevetype();
                                    int num = gatewayLockInfoEventBean.getEventparams().getUserID();
                                    int devecode = gatewayLockInfoEventBean.getEventparams().getDevecode();
                                    int pin = gatewayLockInfoEventBean.getEventparams().getPin();
                                    if (eventParmDeveType.equals("lockprom") && devecode == 2 && pin == 255) {
//                                         //添加单个密码
                                        LogUtils.d("添加单个密码   ");
                                        manager.insertWhenNoExist(deviceId, MyApplication.getInstance().getUid(), gatewayId,
                                                new GatewayPasswordPlanBean(deviceId, gatewayId, MyApplication.getInstance().getUid(), num)
                                        );
                                    } else if (eventParmDeveType.equals("lockprom") && devecode == 3 && num == 255 && pin == 255) {
//                                         //全部删除
                                        LogUtils.d("全部删除   ");
                                        manager.deleteAll(deviceId, MyApplication.getInstance().getUid(), gatewayId);
                                    } else if (eventParmDeveType.equals("lockprom") && devecode == 3 && pin == 255) {
//                                         //删除单个密码
                                        LogUtils.d("删除单个密码   ");
                                        manager.deleteByNumber(deviceId, MyApplication.getInstance().getUid(), gatewayId, num);
                                    } else if (eventParmDeveType.equals("lockop") && devecode == 2 && pin == 255) {
//                                         //使用一次性开锁密码
                                        LogUtils.d("使用一次性开锁密码   ");
                                        if (num > 4 && num < 9) {
                                            manager.deleteByNumber(deviceId, MyApplication.getInstance().getUid(), gatewayId, num);
                                        }
                                    }
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.d("报警消息失败   " + throwable.getMessage());
                        }
                    });
            compositeDisposable.add(getLockPwdInfoEventDisposable);
        }
    }


    //    /监听网关重置上报
    public void listenWifiLockStatus() {
        if (mqttService != null) {
            toDisposable(wifiLockStatusListenDisposable);
            wifiLockStatusListenDisposable = mqttService.listenerDataBack()
                    .filter(new Predicate<MqttData>() {
                        @Override
                        public boolean test(MqttData mqttData) throws Exception {
                            if (mqttData.getFunc().equals(MqttConstant.FUNC_WFEVENT)) {
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

                            JSONObject jsonObject = new JSONObject(payload);

                            String eventtype = "";
                            try {
                                if (payload.contains("eventtype")) {
                                    eventtype = jsonObject.getString("eventtype");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if ("alarm".equals(eventtype)) {
                                WifiLockAlarmBean wifiLockAlarmBean = new Gson().fromJson(payload, WifiLockAlarmBean.class);

                                if (wifiLockAlarmBean != null && wifiLockAlarmBean.getEventparams() != null) {
                                    WifiLockAlarmBean.EventparamsBean eventparams = wifiLockAlarmBean.getEventparams();
                                    if (isSafe()) {
                                        mViewRef.get().onWifiLockAlarmEvent(wifiLockAlarmBean.getWfId(), eventparams.getAlarmCode());
                                    }
                                }
                            } else if ("action".equals(eventtype)) {
                                WifiLockActionBean wifiLockActionBean = new Gson().fromJson(payload, WifiLockActionBean.class);
                                if (wifiLockActionBean != null && wifiLockActionBean.getEventparams() != null) {
                                    WifiLockActionBean.EventparamsBean eventparams = wifiLockActionBean.getEventparams();
                                    MyApplication.getInstance().updateWifiLockInfo(wifiLockActionBean.getWfId(), wifiLockActionBean);
                                }
                            }
                        }
                    });
            compositeDisposable.add(wifiLockStatusListenDisposable);
        }
    }


}


