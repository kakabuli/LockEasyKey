package com.philips.easykey.lock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.hjq.permissions.XXPermissions;
import com.huawei.hms.push.HmsMessaging;
import com.igexin.sdk.IUserLoggerInterface;
import com.igexin.sdk.PushManager;
import com.mob.MobSDK;
import com.philips.easykey.lock.activity.login.PhilipsLoginActivity;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.bean.WifiLockActionBean;
import com.philips.easykey.lock.publiclibrary.bean.GatewayInfo;
import com.philips.easykey.lock.publiclibrary.bean.GwLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.ProductInfo;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetProductionModelListResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.ble.BleService;
import com.philips.easykey.lock.publiclibrary.http.result.GetPasswordResult;
import com.philips.easykey.lock.utils.PermissionInterceptor;
import com.philips.easykey.lock.publiclibrary.http.util.RetrofitServiceManager;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.MqttCommandFactory;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttService;
import com.philips.easykey.lock.publiclibrary.xm.XMP2PManager;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.MMKVUtils;
import com.philips.easykey.lock.utils.MyLog;
import com.philips.easykey.lock.utils.Rom;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.greenDao.bean.ClothesHangerMachineAllBean;
import com.philips.easykey.lock.utils.greenDao.db.ClothesHangerMachineAllBeanDao;
import com.philips.easykey.lock.utils.greenDao.db.DaoManager;
import com.philips.easykey.lock.utils.greenDao.db.DaoMaster;
import com.philips.easykey.lock.utils.greenDao.db.DaoSession;
import com.philips.easykey.lock.utils.greenDao.db.ProductInfoDao;
import com.philips.easykey.lock.utils.greenDao.db.WifiLockInfoDao;
import com.philips.easykey.lock.utils.greenDao.manager.WifiLockInfoManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mmkv.MMKV;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xm.sdk.log.XMLog;
import com.xmitech.sdk.log.LogCodec;

import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.greendao.database.Database;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.subjects.PublishSubject;

import net.sqlcipher.database.SQLiteDatabase;


/**
 * Create By lxj  on 2019/1/7
 * Describe
 */
public class MyApplication extends Application {
    public static final String DB_KEY = "kaadas-2021";
    private static MyApplication instance;
    private String token;
    private String uid;
    private List<Activity> activities = new ArrayList<>();
    // APP_ID 替换为你的应用从官方网站申请到的合法appID
    private static final String APP_ID = "wx2424a66f6c8a94df";

    // IWXAPI 是第三方app和微信通信的openApi接口
    protected MqttService mqttService;
    private BleService bleService;
    private Disposable allBindDeviceDisposable;
    private AllBindDevices allBindDevices;
    private IWXAPI api;
    private List<HomeShowBean> homeShowDevices = new ArrayList<>();
    private List<HomeShowBean> loclHomeShowDevices = new ArrayList<>();
    private List<ProductInfo> productLists = new ArrayList<>();
    private int listService = 0;


    // 小米
    // user your appid the key.
    private static final String M_APP_ID = "2882303761519924663";
    // user your appid the key.
    private static final String M_APP_KEY = "5861992467663";

    @Override
    public void onCreate() {
        super.onCreate();
//        initALog();
        MyLog.getInstance().init(this);
        LogUtils.d("attachView  App启动 ");
        instance = this;
        initMMKV(this);
        boolean showStatementAndTerms = (boolean) SPUtils.getProtect(KeyConstants.SHOW_STATEMENT_AND_TERMS, true);
        if(!showStatementAndTerms){
            initSDK();
        }
        initTokenAndUid();  //获取本地UUID
        listenerAppBackOrForge();
        //扫描二维码初始化
        /* ZXingLibrary.initDisplayOpinion(this);*/
        //配置数据库
        setUpWriteDataBase(DB_KEY);
        LogUtils.d("attachView  App启动完成 ");
        //去掉在Android 9以上调用反射警告提醒弹窗 （Detected problems with API compatibility(visit g.co/dev/appcompat for more info)
        closeAndroidPDialog();
        setRxJavaErrorHandler();
        initFont();

        // 设置权限申请拦截器
        XXPermissions.setInterceptor(new PermissionInterceptor());
    }

    public void initSDK(){
        // TODO: 2021/9/23 为解决商城隐私问题，一些sdk的初始化要特殊判断
        CrashReport.initCrashReport(getApplicationContext(), "37003f935b", true);
        initBleService();
        initMqttService();
        regToWx();
        initXMP2PManager();
        // HuaWei phone
        if (Rom.isEmui()) {
            HmsMessaging.getInstance(this).setAutoInitEnabled(true);
        } else if (Rom.isMiui()) {
            MiPushClient.registerPush(this, M_APP_ID, M_APP_KEY);
        }
        PushManager.getInstance().initialize(this);
        if(BuildConfig.DEBUG)
            PushManager.getInstance().setDebugLogger(this, new IUserLoggerInterface() {
                @Override
                public void log(String s) {
                    LogUtils.d("Getui PushManager -->" + s);
                }
            });
        MobSDK.submitPolicyGrantResult(true,null);
    }

    private void initFont() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = getResources().getConfiguration().locale;
        }
        Typeface typeface;
        if(locale.getLanguage().startsWith("zh")){
            typeface = Typeface.createFromAsset(getAssets(),"fonts/msyh.ttc");
        }else{
            typeface = Typeface.createFromAsset(getAssets(),"fonts/centrale_sans_book.otf");
        }
        Field field = null;
        try {
            field = Typeface.class.getDeclaredField("MONOSPACE");
            field.setAccessible(true);
            field.set(null,typeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private void initMMKV(MyApplication myApplication) {
        String rootDir = MMKV.initialize(myApplication);
        if (BuildConfig.DEBUG)
            LogUtils.d("shulan mmkv root: " + rootDir);
    }

    private void initXMP2PManager() {
        XMP2PManager.getInstance().initAPI(getApplicationContext(), XMP2PManager.serviceString);
        XMP2PManager.getInstance().init(getApplicationContext());
        XMLog.DEBUG = false;
        LogCodec.DEBUG = false;
    }

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(APP_ID);
    }

    public IWXAPI getApi() {
        return api;
    }

    /**
     * 启动蓝牙服务
     */
    private void initBleService() {
        Intent intent = new Intent(this, BleService.class);
        bindService(intent, new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (service instanceof BleService.MyBinder) {
                    BleService.MyBinder binder = (BleService.MyBinder) service;
                    bleService = binder.getService();
                    listService++;
                    getServiceConnected.onNext(listService);
                    if (listService == 2) {
                        listService = 0;
                    }
                    LogUtils.d("服务启动成功    " + (bleService == null) + "当前服务中编号是多少 " + listService);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);

    }

    public BleService getBleService() {
        if(bleService == null)return null;
        return bleService;
    }

    /**
     * 监听程序是前台还是后台
     */
    private int count;

    public void listenerAppBackOrForge() {
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityStopped(Activity activity) {
                count--;
                if (count == 0) {
                    LogUtils.d("程序切换", ">>>>>>>>>>>>>>>>>>>切到后台  lifecycle");
                    listenerAppChange.onNext(true);
                }

            }

            @Override
            public void onActivityStarted(Activity activity) {
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//                LogUtils.d("程序切换", activity + "onActivitySaveInstanceState");
            }

            @Override
            public void onActivityResumed(Activity activity) {
//                LogUtils.d("程序切换", activity + "onActivityResumed");
                if (count == 0) {
                    LogUtils.d("程序切换", ">>>>>>>>>>>>>>>>>>>切到前台  lifecycle");
                    //是不是
                    listenerAppChange.onNext(false);
                }
                count++;
//                LogUtils.d("程序切换", activity + "onActivityStarted  " + count);
            }

            @Override
            public void onActivityPaused(Activity activity) {
//                LogUtils.d("程序切换", activity + "onActivityPaused");

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
//                Log.d("程序切换", activity + "onActivityDestroyed");
            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//                LogUtils.d("viclee", activity + "onActivityCreated");
            }
        });

    }

    /**
     * 启动MQTT服务
     */
    private void initMqttService() {
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                if (service instanceof MqttService.MyBinder) {
                    MqttService.MyBinder binder = (MqttService.MyBinder) service;
                    mqttService = binder.getService();
                    listService++;
                    getServiceConnected.onNext(listService);
                    if (listService == 2) {
                        listService = 0;
                    }
                    LogUtils.d("attachView service启动" + (mqttService == null) + "当前服务中编号是多少  " + listService);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        }, Context.BIND_AUTO_CREATE);
    }


    public MqttService getMqttService() {
        return mqttService;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public void initTokenAndUid() {
        try {
            token = MMKVUtils.getStringMMKV(SPUtils.TOKEN);
            uid = MMKVUtils.getStringMMKV(SPUtils.UID);
//            token = (String) SPUtils.get(SPUtils.TOKEN, "");//类型转换有崩溃
//            uid = (String) SPUtils.get(SPUtils.UID, "");
            RetrofitServiceManager.updateToken();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {  //每次设置token  都更新一下
        this.token = token;
        RetrofitServiceManager.updateToken();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @param isShowDialog 是否弹出对话框，提示用户token失效，需要重新登陆。如果是主动退出登录的那么不需要提示 是false
     *                     <p>
     *                     <p>
     *                     Token过期  需要处理的事情
     *                     清除token和UID
     *                     bleService  断开蓝牙连接  清除蓝牙数据
     *                     清除缓存到的本地数据
     *                     关闭所有界面，退出到登陆界面
     *                     ....
     */
    public void tokenInvalid(boolean isShowDialog) {
        deleSQL();  //清除数据库数据
        LogUtils.d("token过期   ");
        SPUtils.put(KeyConstants.HEAD_PATH, "");
        boolean alreadyStart = false;
        Boolean appUpdate = (Boolean) SPUtils.get(SPUtils.APPUPDATE, false);
        String phone = (String) SPUtils.get(SPUtils.PHONEN, "");
        MMKVUtils.removeKey(SPUtils.STORE_TOKEN);
        //退出登录  清除数据
        SPUtils.clear();
        if (appUpdate == true) {
            SPUtils.put(SPUtils.APPUPDATE, true);
        }
        if (!TextUtils.isEmpty(phone)) {
            SPUtils.put(SPUtils.PHONEN, phone);
        }
        //清除内存中缓存的数据
        if (bleService != null) {
            LogUtils.d("token过期   断开连接  ");
            bleService.release();  //token过期   断开连接
            bleService.removeBleLockInfo();
        }
        homeShowDevices.clear();
        productLists.clear();

        MyApplication.getInstance().initTokenAndUid();
        //清除数据库数据
        for (Activity activity : activities) {
            if (activity != null) {
                if (!activity.getClass().equals(PhilipsLoginActivity.class)){
                    if (!alreadyStart) { //如果还没有启动Activity
                        alreadyStart = true;
                        Intent intent = new Intent(activity, PhilipsLoginActivity.class);
                        intent.putExtra(KeyConstants.IS_SHOW_DIALOG, isShowDialog);
                        activity.startActivity(intent);
                    /*if (isShowDialog) {  //
                        ToastUtils.showShort(R.string.token_invalid_relogin_please);
                    }*/
                    }
                    activity.finish();
                }
            }
        }
    }

    public void deleSQL() {  //删除所有表 然后创建所有表
        Database database = getDaoWriteSession().getDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        DaoMaster.dropAllTables(daoMaster.getDatabase(), true);
        DaoMaster.createAllTables(daoMaster.getDatabase(), true);
    }


    public void addActivity(Activity activity) {
        if (!activities.contains(activity)) {
            activities.add(activity);
        }
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    static {
        //static 代码段可以防止内存泄露
        // 设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
                //全局设置主题颜色
                return new ClassicsHeader(context).setSpinnerStyle(SpinnerStyle.Translate);
                //指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setSpinnerStyle(SpinnerStyle.Translate);
            }
        });
    }

    /**
     * 密码列表的存取
     */
    private Map<String, GetPasswordResult> passwordResults = new HashMap();

    public void setPasswordResults(String deviceName, GetPasswordResult passwordList, boolean isNotify) {
        LogUtils.d("设置数据  密码列表  " + deviceName + (passwordList.getData() == null));
        passwordResults.put(deviceName, passwordList);
        if (isNotify) {
            passwordLoaded.onNext(true);
        }
    }

    public GetPasswordResult getPasswordResults(String deviceName) {
        return passwordResults.get(deviceName);
    }

    /**
     * 密码变动通知获取
     */
    private PublishSubject<Boolean> passwordChange = PublishSubject.create();

    public PublishSubject<Boolean> passwordChangeListener() {
        return passwordChange;
    }

    /**
     * 密码获取成功，通知更新界面
     */
    private PublishSubject<Boolean> passwordLoaded = PublishSubject.create();

    public PublishSubject<Boolean> passwordLoadedListener() {
        return passwordLoaded;
    }

    private PublishSubject<Boolean> listenerAppChange = PublishSubject.create();

    public PublishSubject<Boolean> listenerAppState() {
        return listenerAppChange;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //程序终止时
        LogUtils.d("程序终止了");
    }

    //清除所有与的Actvity
    public void removeAllActivity() {
        LogUtils.d("清除所有的Activity");
        for (Activity activity : activities) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 全局的获取所有设备信息
     *
     * @param isForce 是否强制刷新
     */

    public void getAllDevicesByMqtt(boolean isForce){
        if (!isForce) {
            if (allBindDevices != null) {
                getDevicesFromServer.onNext(allBindDevices);
                return;
            }
        }
        XiaokaiNewServiceImp.getAllBindDevice(getUid(),2)
                .subscribe(new BaseObserver<AllBindDevices>() {
                    @Override
                    public void onSuccess(AllBindDevices allBindDevices) {
                        LogUtils.d("getAllBindDevice onSuccess " + allBindDevices.toString());
                        if (!"200".equals(allBindDevices.getCode())) {  ///服务器获取设备列表失败
                            LogUtils.d("   获取列表失败  " + allBindDevices.getCode());
                            useHomeShowDeviceFromLocal();
                        }else {
                            //使用服务器的数据
                            if (!loclHomeShowDevices.isEmpty()) {
                                loclHomeShowDevices.clear();
                            }

                            List<AllBindDevices.ReturnDataBean.GwListBean> gwList = allBindDevices.getData().getGwList();
                            if (gwList != null) {
                                int allgwSize = gwList.size();
                                for (int j = 0; j < allgwSize; j++) {
                                    SPUtils.put(Constants.RELAYTYPE + gwList.get(j).getDeviceSN(), gwList.get(j).getRelayType());
                                }
                            }

                            //SPUtils.put(Constants.ALL_DEVICES_DATA, payload);

                            long serverCurrentTime = allBindDevices.getNowTime();
                            SPUtils.put(KeyConstants.SERVER_CURRENT_TIME, serverCurrentTime);

                            if (allBindDevices != null) {
                                homeShowDevices = allBindDevices.getHomeShow();
                                com.blankj.utilcode.util.LogUtils.d("homeShowDevices length: " + homeShowDevices.size());
                                LogUtils.d("设备更新  application");
                                getDevicesFromServer.onNext(allBindDevices);
                                // 增加一个数据回调监听
                                if(mOnHomeShowDeviceChangeListener != null) {
                                    mOnHomeShowDeviceChangeListener.dataChanged();
                                    LogUtils.d("mOnHomeShowDeviceChangeListener.dataChanged()");
                                }

                                //缓存WiFi锁设备信息 到Dao
                                if (allBindDevices.getData() != null && allBindDevices.getData().getWifiList() != null) {
                                    List<WifiLockInfo> wifiList = allBindDevices.getData().getWifiList();
                                    WifiLockInfoDao wifiLockInfoDao = getDaoWriteSession().getWifiLockInfoDao();
                                    wifiLockInfoDao.deleteAll();
                                    wifiLockInfoDao.insertInTx(wifiList);
                                }

                                //缓存晾衣机设备信息到Dao
                                if (allBindDevices.getData() != null && allBindDevices.getData().getHangerList() != null) {
                                    List<ClothesHangerMachineAllBean> hangerList = allBindDevices.getData().getHangerList();
                                    ClothesHangerMachineAllBeanDao clothesHangerMachineAllBeanDao = getDaoWriteSession().getClothesHangerMachineAllBeanDao();
                                    clothesHangerMachineAllBeanDao.deleteAll();
                                    clothesHangerMachineAllBeanDao.insertInTx(hangerList);
                                }

                                //缓存产品型号信息列表 到Dao，主要是图片下载地址（下载过的图片不再下载）
                                if (allBindDevices.getData() != null && allBindDevices.getData().getProductInfoList() != null) {
                                    productLists = allBindDevices.getData().getProductInfoList();
                                    ProductInfoDao productInfoDao = getDaoWriteSession().getProductInfoDao();
                                    productInfoDao.deleteAll();
                                    productInfoDao.insertInTx(productLists);
                                }
                            }
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        LogUtils.d("getAllBindDevice onAckErrorCode " + baseResult.toString());
                        LogUtils.d("   获取列表失败  " + allBindDevices.getCode());
                        useHomeShowDeviceFromLocal();
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        useHomeShowDeviceFromLocal();
                        LogUtils.d("getAllBindDevice onFailed " + throwable.toString());
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });

    }

   /*public void getAllDevicesByMqtt(boolean isForce) {
        if (!isForce) {
            if (allBindDevices != null) {
                getDevicesFromServer.onNext(allBindDevices);
                return;
            }
        }
        MqttMessage allBindDevice = MqttCommandFactory.getAllBindDevice(getUid());
        if (allBindDeviceDisposable != null && !allBindDeviceDisposable.isDisposed()) {
            allBindDeviceDisposable.dispose();
        }
        if(mqttService == null)return;
        allBindDeviceDisposable = mqttService.mqttPublish(MqttConstant.MQTT_REQUEST_APP, allBindDevice)
                .filter(mqttData -> mqttData.getFunc().equalsIgnoreCase(MqttConstant.GET_ALL_BIND_DEVICE))
                .timeout(10 * 1000, TimeUnit.MILLISECONDS)
                .compose(RxjavaHelper.observeOnMainThread())
                .subscribe(mqttData -> {
                    if (allBindDeviceDisposable != null && !allBindDeviceDisposable.isDisposed()) {
                        allBindDeviceDisposable.dispose();
                    }
                    String payload = mqttData.getPayload();

                    allBindDevices = new Gson().fromJson(payload, AllBindDevices.class);

                    if (!"200".equals(allBindDevices.getCode())) {  ///服务器获取设备列表失败
                        LogUtils.d("   获取列表失败  " + allBindDevices.getCode());
                        useHomeShowDeviceFromLocal();
                        return;
                    }

                    //使用服务器的数据
                    if (!loclHomeShowDevices.isEmpty()) {
                        loclHomeShowDevices.clear();
                    }

                    List<AllBindDevices.ReturnDataBean.GwListBean> gwList = allBindDevices.getData().getGwList();
                    if (gwList != null) {
                        int allgwSize = gwList.size();
                        for (int j = 0; j < allgwSize; j++) {
                            SPUtils.put(Constants.RELAYTYPE + gwList.get(j).getDeviceSN(), gwList.get(j).getRelayType());
                        }
                    }

                    SPUtils.put(Constants.ALL_DEVICES_DATA, payload);

                    long serverCurrentTime = allBindDevices.getNowTime();
                    SPUtils.put(KeyConstants.SERVER_CURRENT_TIME, serverCurrentTime);

                    if (allBindDevices != null) {
                        homeShowDevices = allBindDevices.getHomeShow();
                        com.blankj.utilcode.util.LogUtils.d("homeShowDevices length: " + homeShowDevices.size());
                        LogUtils.d("设备更新  application");
                        getDevicesFromServer.onNext(allBindDevices);
                        // 增加一个数据回调监听
                        if(mOnHomeShowDeviceChangeListener != null) {
                            mOnHomeShowDeviceChangeListener.dataChanged();
                            com.blankj.utilcode.util.LogUtils.d("mOnHomeShowDeviceChangeListener.dataChanged()");
                        }

                        //缓存WiFi锁设备信息 到Dao
                        if (allBindDevices.getData() != null && allBindDevices.getData().getWifiList() != null) {
//                                LogUtils.d("--kaadas--allBindDevices.getData().getWifiList=="+allBindDevices.getData().getWifiList());
                            List<WifiLockInfo> wifiList = allBindDevices.getData().getWifiList();
                            WifiLockInfoDao wifiLockInfoDao = getDaoWriteSession().getWifiLockInfoDao();
                            wifiLockInfoDao.deleteAll();
                            wifiLockInfoDao.insertInTx(wifiList);
                        }

                        //缓存晾衣机设备信息到Dao
                        if (allBindDevices.getData() != null && allBindDevices.getData().getHangerList() != null) {
                            List<ClothesHangerMachineAllBean> hangerList = allBindDevices.getData().getHangerList();
                            ClothesHangerMachineAllBeanDao clothesHangerMachineAllBeanDao = getDaoWriteSession().getClothesHangerMachineAllBeanDao();
                            clothesHangerMachineAllBeanDao.deleteAll();
                            clothesHangerMachineAllBeanDao.insertInTx(hangerList);
                        }

                        //缓存产品型号信息列表 到Dao，主要是图片下载地址（下载过的图片不再下载）
                        if (allBindDevices.getData() != null && allBindDevices.getData().getProductInfoList() != null) {
                            productLists = allBindDevices.getData().getProductInfoList();
//                                LogUtils.d("--kaadas--productLists=="+productLists);
                            ProductInfoDao productInfoDao = getDaoWriteSession().getProductInfoDao();
                            productInfoDao.deleteAll();
                            productInfoDao.insertInTx(productLists);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        useHomeShowDeviceFromLocal();
                    }
                });
    }*/

    private void useHomeShowDeviceFromLocal() {
        if (!loclHomeShowDevices.isEmpty()) {
            for (HomeShowBean bean : loclHomeShowDevices) {
                HomeShowBean newBean = new HomeShowBean(bean.getDeviceType(), bean.getDeviceId(), bean.getDeviceNickName(), bean.getObject());
                homeShowDevices.add(newBean);

            }
            loclHomeShowDevices.clear();
        }
    }

    public ClothesHangerMachineAllBean searchClothesHangerMachine(String wifiSn) {
        DaoSession daoSession = MyApplication.getInstance().getDaoWriteSession();
        if (daoSession != null && daoSession.getClothesHangerMachineAllBeanDao() != null) {
            List<ClothesHangerMachineAllBean> hangerList = daoSession.getClothesHangerMachineAllBeanDao().loadAll();
            if (hangerList != null && hangerList.size() > 0) {
                for (ClothesHangerMachineAllBean bean : hangerList) {
                    if (bean.getWifiSN().equals(wifiSn)) {
                        return bean;
                    }
                }
            }
        }
        return null;
    }

    public ClothesHangerMachineAllBean getClothesHangerMachineBySn(String sn) {
        if (TextUtils.isEmpty(sn)) {
            return null;
        }
        if (homeShowDevices != null) {
            for (int i = homeShowDevices.size() - 1; i >= 0; i--) {
                HomeShowBean homeShowBean = homeShowDevices.get(i);
                if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_CLOTHES_HANGER) {
                    ClothesHangerMachineAllBean bean = (ClothesHangerMachineAllBean) homeShowBean.getObject();
                    if (bean.getWifiSN().equals(sn)) {
                        return bean;
                    }
                }
            }
        }
        return null;
    }

    public WifiLockInfo searchVideoLock(String wifiSN) {
        DaoSession daoSession = MyApplication.getInstance().getDaoWriteSession();
        if (daoSession != null && daoSession.getWifiLockInfoDao() != null) {
            List<WifiLockInfo> wifiLockInfos = daoSession.getWifiLockInfoDao().loadAll();
            if (wifiLockInfos != null && wifiLockInfos.size() > 0) {
                for (WifiLockInfo wifiLockInfo : wifiLockInfos) {
                    if (wifiLockInfo.getWifiSN().equals(wifiSN)) {
                        return wifiLockInfo;
                    }
                }
            }
        }
        return null;
    }

    public WifiLockInfo getWifiLockInfoBySn(String sn) {
        if (TextUtils.isEmpty(sn)) {
            return null;
        }
        if (homeShowDevices != null) {
            for (int i = homeShowDevices.size() - 1; i >= 0; i--) {
                HomeShowBean homeShowBean = homeShowDevices.get(i);
                if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_WIFI_LOCK || homeShowBean.getDeviceType() == HomeShowBean.TYPE_WIFI_VIDEO_LOCK) {
                    WifiLockInfo wifiLockInfo = (WifiLockInfo) homeShowBean.getObject();
                    if (wifiLockInfo.getWifiSN().equals(sn)) {
                        return wifiLockInfo;
                    }
                }
            }
        }
        return null;
    }

    public int getWifiVideoLockTypeBySn(String sn) {
        if (homeShowDevices != null) {
            for (int i = homeShowDevices.size() - 1; i >= 0; i--) {
                HomeShowBean homeShowBean = homeShowDevices.get(i);
                if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_WIFI_VIDEO_LOCK) {
                    WifiLockInfo wifiLockInfo = (WifiLockInfo) homeShowBean.getObject();
                    if (wifiLockInfo.getWifiSN().equals(sn)) {
                        return HomeShowBean.TYPE_WIFI_VIDEO_LOCK;
                    }
                }
            }
        }
        return HomeShowBean.TYPE_UNKOWN_LOCK;
    }

    public void updateWifiLockInfo(String sn, WifiLockActionBean actionBean) {
        if (homeShowDevices != null) {
            for (int i = homeShowDevices.size() - 1; i >= 0; i--) {
                HomeShowBean homeShowBean = homeShowDevices.get(i);
                if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_WIFI_LOCK || homeShowBean.getDeviceType() == HomeShowBean.TYPE_WIFI_VIDEO_LOCK) {
                    WifiLockInfo wifiLockInfo = (WifiLockInfo) homeShowBean.getObject();
                    /**
                     * amMode : 1
                     * defences : 1
                     * language : zh/en
                     * operatingMode : 1
                     * safeMode : 1
                     * volume : 0
                     * powerSave : 0
                     * faceStatus : 1
                     */
                    if (wifiLockInfo.getWifiSN().equals(sn)) {
                        wifiLockInfo.setAmMode(actionBean.getEventparams().getAmMode());
                        wifiLockInfo.setDefences(actionBean.getEventparams().getDefences());
                        wifiLockInfo.setLanguage(actionBean.getEventparams().getLanguage());
                        wifiLockInfo.setOperatingMode(actionBean.getEventparams().getOperatingMode());
                        wifiLockInfo.setSafeMode(actionBean.getEventparams().getSafeMode());
                        wifiLockInfo.setVolume(actionBean.getEventparams().getVolume());
                        wifiLockInfo.setVolLevel(actionBean.getEventparams().getVolLevel());
                        wifiLockInfo.setPowerSave(actionBean.getEventparams().getPowerSave());
                        wifiLockInfo.setFaceStatus(actionBean.getEventparams().getFaceStatus());
                        wifiLockInfo.setOpenForce(actionBean.getEventparams().getOpenForce());
                        wifiLockInfo.setLockingMethod(actionBean.getEventparams().getLockingMethod());
                        wifiLockInfo.setOpenDirection(actionBean.getEventparams().getOpenDirection());
                        wifiLockInfo.setBodySensor(actionBean.getEventparams().getBodySensor());
                        wifiLockInfo.setTouchHandleStatus(actionBean.getEventparams().getTouchHandleStatus());

                        if(BleLockUtils.isSupportFacePir(wifiLockInfo.getFunctionSet())){
                            wifiLockInfo.setHoverAlarm(actionBean.getEventparams().getHoverAlarm());
                            wifiLockInfo.setHoverAlarmLevel(actionBean.getEventparams().getHoverAlarmLevel());
                        }
                        long updateTime = Long.parseLong(actionBean.getTimestamp());
                        LogUtils.d("更新的时间为   " + DateUtils.getDateTimeFromMillisecond(updateTime * 1000));
                        wifiLockInfo.setUpdateTime(updateTime);
                    }
                    new WifiLockInfoManager().insertOrReplace(wifiLockInfo);
                    wifiLockActionChange.onNext(wifiLockInfo);
                }
            }
        }
    }

    /**
     * wifi锁动态更新界面
     */
    private PublishSubject<WifiLockInfo> wifiLockActionChange = PublishSubject.create();

    public Observable<WifiLockInfo> listenerWifiLockAction() {
        return wifiLockActionChange;
    }


    public void setAllBindDevices(AllBindDevices allBindDevices) {
        homeShowDevices = allBindDevices.getHomeShow();
        LogUtils.d("获取到的首页设备个数是   " + homeShowDevices.size());
        getDevicesFromServer.onNext(allBindDevices);
    }

    public void setHomeShowDevice(List<HomeShowBean> homeShowDeviceList) {
        homeShowDevices = homeShowDeviceList;
        loclHomeShowDevices = homeShowDeviceList;
        com.blankj.utilcode.util.LogUtils.d("homeShowDevices length: " + homeShowDevices.size());
    }

    public List<HomeShowBean> getHomeShowDevices() {
        List<HomeShowBean> tem = new ArrayList<>();
        for (HomeShowBean homeShowBean : homeShowDevices) {
            if (homeShowBean.getDeviceType() != HomeShowBean.TYPE_GATEWAY) {
                tem.add(homeShowBean);
            }
        }
        com.blankj.utilcode.util.LogUtils.d("homeShowDevices length: " + homeShowDevices.size());
        return tem;
    }

    public List<HomeShowBean> getAllDevices() {
        return homeShowDevices;
    }

    private OnHomeShowDeviceChangeListener mOnHomeShowDeviceChangeListener;

    public void setOnHomeShowDeviceChangeListener(OnHomeShowDeviceChangeListener onHomeShowDeviceChangeListener) {
        mOnHomeShowDeviceChangeListener = onHomeShowDeviceChangeListener;
    }

    public interface OnHomeShowDeviceChangeListener {
        void dataChanged();
    }

    public String getNickByDeviceId(String deviceId) {
        for (HomeShowBean homeShowBean : homeShowDevices) {
            if (deviceId.equals(homeShowBean.getDeviceId())) {
                return homeShowBean.getDeviceNickName();
            }
        }
        return deviceId;

    }

    //根据网关id查找出网关
    public GatewayInfo getGatewayById(String gatewayId) {
        if (homeShowDevices != null && homeShowDevices.size() > 0) {
            for (HomeShowBean homeShowBean : homeShowDevices) {
                if (gatewayId.equals(homeShowBean.getDeviceId())) {
                    return (GatewayInfo) homeShowBean.getObject();
                }
            }
        }
        return null;
    }

    //根据设备id查找出设备
    public GwLockInfo getGatewayLockById(String deviceId) {
        for (HomeShowBean homeShowBean : homeShowDevices) {
            if (deviceId.equals(homeShowBean.getDeviceId())) {
                return (GwLockInfo) homeShowBean.getObject();
            }
        }
        return null;
    }

    //遍历网关下的设备
    public List<HomeShowBean> getGatewayBindList(String gatewayId) {
        //遍历绑定的网关设备
        List<HomeShowBean> gatewayBindList = new ArrayList<>();
        for (HomeShowBean homeShowBean : homeShowDevices) {
            if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_GATEWAY_LOCK) {
                GwLockInfo gwLockInfo = (GwLockInfo) homeShowBean.getObject();
                if (gwLockInfo.getGwID().equals(gatewayId)) {
                    gatewayBindList.add(homeShowBean);
                }
            }
        }
        return gatewayBindList;
    }

    //所有网关
    public List<GatewayInfo> getAllGateway() {
        List<GatewayInfo> gatewayList = new ArrayList<>();
        if (homeShowDevices == null) {
            return gatewayList;
        }
        for (HomeShowBean homeShowBean : homeShowDevices) {
            if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_GATEWAY) {
                gatewayList.add((GatewayInfo) homeShowBean.getObject());
            }
        }
        return gatewayList;
    }


    /**
     * 获取缓存的设备
     */

    public AllBindDevices getAllBindDevices() {
        return allBindDevices;
    }

    /**
     * 从服务器获取到设备
     */
    public PublishSubject<AllBindDevices> getDevicesFromServer = PublishSubject.create();

    /**
     *
     */
    public Observable<AllBindDevices> listenerAllDevices() {
        return getDevicesFromServer;
    }

    /**
     * 监听蓝牙服务是否启动成功和mqtt服务是否启动成功
     */
    public PublishSubject<Integer> getServiceConnected = PublishSubject.create();

    public Observable<Integer> listenerServiceConnect() {
        return getServiceConnected;
    }

    private static DaoSession daoWriteSession;
    private DaoManager manager;

    private void setUpWriteDataBase(String password) {
        SQLiteDatabase.loadLibs(this);
        manager = DaoManager.getInstance(this);
        daoWriteSession = manager.getDaoSession(password);
    }

    public DaoSession getDaoWriteSession() {
        return daoWriteSession;
    }

    public void reStartApp() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent); // 1秒钟后重启应用
        System.exit(0);
    }

    private void setRxJavaErrorHandler() {
        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                MyLog.getInstance().save("Rxjava throwable-->" + throwable);
            }
        });
    }


    /**
     * @param sn
     * @param version    新版本的版本号
     *                   param customer   客户：1凯迪仕 2小凯 3桔子物联 4飞利浦
     * @param resultCode 结果：0升级成功 1升级失败 （可自定义其他错误码）
     * @param devNum     模块：1主模块 2算法模块 3相机模块（空：默认1）
     * @return
     */
    public void uploadOtaResult(String sn, String version, String resultCode, int devNum) {
        if (!TextUtils.isEmpty(resultCode) && resultCode.length() > 200) {
            resultCode = resultCode.substring(0, 200);
        }
        XiaokaiNewServiceImp.uploadOtaResult(sn, version, 1, resultCode, devNum)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult baseResult) {
                        LogUtils.d("上传OTA结果成功    " + baseResult.toString());
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        LogUtils.d("上传OTA结果失败    " + baseResult.toString());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        LogUtils.d("上传OTA结果失败    " + throwable.getMessage());
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }

    private void closeAndroidPDialog() {
//        try {
//            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
//            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
//            declaredConstructor.setAccessible(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        try {
//            Class cls = Class.forName("android.app.ActivityThread");
//            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
//            declaredMethod.setAccessible(true);
//            Object activityThread = declaredMethod.invoke(null);
//            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
//            mHiddenApiWarningShown.setAccessible(true);
//            mHiddenApiWarningShown.setBoolean(activityThread, true);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public void setProductInfos(List<ProductInfo> productList) {
        productLists = productList;
    }

    public List<ProductInfo> getProductInfos() {
        return productLists;
    }
}



