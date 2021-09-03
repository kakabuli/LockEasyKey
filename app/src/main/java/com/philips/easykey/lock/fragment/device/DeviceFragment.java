package com.philips.easykey.lock.fragment.device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.DeviceAdd2Activity;

import com.philips.easykey.lock.activity.device.BleDetailActivity;
import com.philips.easykey.lock.activity.device.BleAuthActivity;
import com.philips.easykey.lock.activity.device.clotheshangermachine.ClothesHangerMachineDetailActivity;
import com.philips.easykey.lock.activity.device.gateway.GatewayActivity;
import com.philips.easykey.lock.activity.device.gatewaylock.GatewayLockAuthorizeFunctionActivity;
import com.philips.easykey.lock.activity.device.gatewaylock.GatewayLockFunctionActivity;
import com.philips.easykey.lock.activity.device.oldbluetooth.OldBleDetailActivity;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockDetailActivity;
import com.philips.easykey.lock.activity.device.wifilock.WifiLockAuthActivity;
import com.philips.easykey.lock.adapter.DeviceDetailAdapter;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.DevicePresenter;
import com.philips.easykey.lock.mvp.view.IDeviceView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.GatewayInfo;
import com.philips.easykey.lock.publiclibrary.bean.GwLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.ProductInfo;
import com.philips.easykey.lock.publiclibrary.bean.ServerGatewayInfo;
import com.philips.easykey.lock.publiclibrary.bean.ServerGwDevice;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.ServerBleDevice;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttService;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.greenDao.bean.BleLockServiceInfo;
import com.philips.easykey.lock.utils.greenDao.bean.ClothesHangerMachineAllBean;
import com.philips.easykey.lock.utils.greenDao.bean.DevicePower;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayLockServiceInfo;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayServiceInfo;
import com.philips.easykey.lock.utils.greenDao.db.BleLockServiceInfoDao;
import com.philips.easykey.lock.utils.greenDao.db.DaoSession;
import com.philips.easykey.lock.utils.greenDao.db.DevicePowerDao;
import com.philips.easykey.lock.utils.greenDao.db.GatewayLockServiceInfoDao;
import com.philips.easykey.lock.utils.greenDao.db.GatewayServiceInfoDao;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by asqw1 on 2018/3/14.
 */

public class DeviceFragment extends BaseFragment<IDeviceView, DevicePresenter<IDeviceView>> implements IDeviceView {

    ImageView noDeviceImage;
    RecyclerView deviceRecycler;
    SmartRefreshLayout refresh;
    Button buy;
    RelativeLayout noDeviceLayout;
    ImageView deviceAdd;
    RelativeLayout titleBar;

    private View mView;

    private DeviceDetailAdapter deviceDetailAdapter;

    private List<HomeShowBean> mDeviceList = new ArrayList<>();
    private List<HomeShowBean> homeShowBeanList;
    private List<ProductInfo> productList = new ArrayList<>();

    private String uid;
    private DaoSession daoSession;
    private MqttService mqttService;

    private ProductInfo productInfo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_device, container, false);
        }

        noDeviceImage = mView.findViewById(R.id.no_device_image);
        deviceRecycler = mView.findViewById(R.id.device_recycler);
        refresh = mView.findViewById(R.id.refresh);
        buy = mView.findViewById(R.id.buy);
        noDeviceLayout = mView.findViewById(R.id.no_device_layout);
        deviceAdd = mView.findViewById(R.id.device_add);
        titleBar = mView.findViewById(R.id.title_bar);

        deviceAdd.setOnClickListener(v -> {
            Intent deviceAdd = new Intent(getActivity(), DeviceAdd2Activity.class);
            startActivity(deviceAdd);
        });
        buy.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse("http://www.kaadas.com/");//此处填链接
            intent.setData(content_url);
            startActivity(intent);
        });

        deviceRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        homeShowBeanList = MyApplication.getInstance().getAllDevices();
        mqttService = MyApplication.getInstance().getMqttService();
        //获取缓存的producinfo
//        LogUtils.d("--kaadas--productList.getProductInfos==" + MyApplication.getInstance().getProductInfos());
        productList = MyApplication.getInstance().getProductInfos();

        initData(homeShowBeanList);
        initRefresh();
        //动态设置状态栏高度
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(titleBar.getLayoutParams());
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        titleBar.setLayoutParams(lp);
        return mView;
    }

    @Override
    protected DevicePresenter<IDeviceView> createPresent() {
        return new DevicePresenter<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initAdapter() {
        if (mDeviceList != null) {

            deviceDetailAdapter = new DeviceDetailAdapter(mDeviceList,productList);

            deviceRecycler.setAdapter(deviceDetailAdapter);
            deviceDetailAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    if (mDeviceList != null) {
                        if (mDeviceList.size() > position) {
                            LogUtils.e("设备总和  " + mDeviceList.size() + "位置  " + position);
                            HomeShowBean deviceDetailBean = mDeviceList.get(position);
                            switch (deviceDetailBean.getDeviceType()) {
                                case HomeShowBean.TYPE_GATEWAY_LOCK:
                                    GwLockInfo lockInfo = (GwLockInfo) deviceDetailBean.getObject();
                                    GatewayInfo gw = MyApplication.getInstance().getGatewayById(lockInfo.getGwID());
                                    try{
                                        String gatewayModel = gw.getServerInfo().getModel();   // 6032 网关
                                        if (gw.getServerInfo().getIsAdmin() == 1) {
                                            //网关锁
                                            Intent gatewayLockintent = new Intent(getActivity(), GatewayLockFunctionActivity.class);
                                            gatewayLockintent.putExtra(KeyConstants.GATEWAY_LOCK_INFO, deviceDetailBean);
                                            gatewayLockintent.putExtra(KeyConstants.GATEWAY_MODEL,gatewayModel);
                                            startActivity(gatewayLockintent);
                                        } else {
                                            //授权锁
                                            //网关锁
                                            Intent gatewayLockintent = new Intent(getActivity(), GatewayLockAuthorizeFunctionActivity.class);
                                            gatewayLockintent.putExtra(KeyConstants.GATEWAY_LOCK_INFO, deviceDetailBean);
                                            gatewayLockintent.putExtra(KeyConstants.GATEWAY_MODEL,gatewayModel);
                                            startActivity(gatewayLockintent);
                                        }
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }


                                    break;
                                case HomeShowBean.TYPE_GATEWAY:
                                    //网关
                                    Intent gatwayInfo = new Intent(getActivity(), GatewayActivity.class);
                                    gatwayInfo.putExtra(KeyConstants.GATEWAY_INFO, deviceDetailBean);
                                    startActivity(gatwayInfo);
                                    break;
                                case HomeShowBean.TYPE_BLE_LOCK:
                                    //蓝牙
                                    BleLockInfo bleLockInfo = (BleLockInfo) deviceDetailBean.getObject();
                                    mPresenter.setBleLockInfo(bleLockInfo);
                                    if (bleLockInfo.getServerLockInfo().getIs_admin() != null && bleLockInfo.getServerLockInfo().getIs_admin().equals("1")) {
                                        if ("3".equals(bleLockInfo.getServerLockInfo().getBleVersion())) {
                                            String functionSet = bleLockInfo.getServerLockInfo().getFunctionSet();
                                            LogUtils.e("蓝牙的功能集是   " + functionSet);
                                            if (!TextUtils.isEmpty(functionSet) && Integer.parseInt(functionSet) == 0) {
                                                LogUtils.e("跳转   老蓝牙");
                                                Intent detailIntent = new Intent(getActivity(), OldBleDetailActivity.class);
                                                String model = bleLockInfo.getServerLockInfo().getModel();
                                                detailIntent.putExtra(KeyConstants.DEVICE_TYPE, model);
                                                startActivityForResult(detailIntent, KeyConstants.GET_BLE_POWER);
                                            } else {
                                                LogUtils.e("跳转   全功能  蓝牙");
                                                Intent detailIntent = new Intent(getActivity(), BleDetailActivity.class);
                                                String model = bleLockInfo.getServerLockInfo().getModel();
                                                String deviceSN = bleLockInfo.getServerLockInfo().getDeviceSN();
                                                detailIntent.putExtra(KeyConstants.BLE_DEVICE_SN, deviceSN);
                                                detailIntent.putExtra(KeyConstants.DEVICE_TYPE, model);
                                                startActivityForResult(detailIntent, KeyConstants.GET_BLE_POWER);
                                            }
                                        } else {
                                            LogUtils.e("跳转   老蓝牙");
                                            Intent detailIntent = new Intent(getActivity(), OldBleDetailActivity.class);
                                            String model = bleLockInfo.getServerLockInfo().getModel();
                                            detailIntent.putExtra(KeyConstants.DEVICE_TYPE, model);
                                            startActivityForResult(detailIntent, KeyConstants.GET_BLE_POWER);
                                        }
                                    } else {
                                        LogUtils.e("跳转 授权  蓝牙");
                                        Intent impowerIntent = new Intent(getActivity(), BleAuthActivity.class);
                                        String model = bleLockInfo.getServerLockInfo().getModel();
                                        String deviceSN = bleLockInfo.getServerLockInfo().getDeviceSN();

                                        impowerIntent.putExtra(KeyConstants.DEVICE_TYPE, model);
                                        impowerIntent.putExtra(KeyConstants.BLE_DEVICE_SN, deviceSN);

                                        startActivityForResult(impowerIntent, KeyConstants.GET_BLE_POWER);
                                    }
                                    break;
                                case HomeShowBean.TYPE_CLOTHES_HANGER:
                                    ClothesHangerMachineAllBean hangerInfo = (ClothesHangerMachineAllBean) deviceDetailBean.getObject();
                                    Intent clothesIntent = new Intent(getActivity(), ClothesHangerMachineDetailActivity.class);
                                    clothesIntent.putExtra(KeyConstants.WIFI_SN,hangerInfo.getWifiSN());
                                    startActivity(clothesIntent);
                                    break;
                                case HomeShowBean.TYPE_WIFI_LOCK:
                                case HomeShowBean.TYPE_WIFI_VIDEO_LOCK:
                                    WifiLockInfo wifiLockInfo = (WifiLockInfo) deviceDetailBean.getObject();
                                    if (!TextUtils.isEmpty(wifiLockInfo.getFunctionSet())) {
                                        if (wifiLockInfo.getIsAdmin() == 1) { //主用户
                                            Intent intent = new Intent(getActivity(), PhilipsWifiVideoLockDetailActivity.class);
                                            intent.putExtra(KeyConstants.WIFI_SN, wifiLockInfo.getWifiSN());
                                            startActivity(intent);
                                        } else { //分享用户
                                            LogUtils.e("分享 wifi锁  用户  " + wifiLockInfo.toString());
                                            Intent intent = new Intent(getActivity(), WifiLockAuthActivity.class);
                                            intent.putExtra(KeyConstants.WIFI_SN, wifiLockInfo.getWifiSN());
                                            startActivity(intent);
                                        }
                                    } else {
                                        ToastUtils.showLong(R.string.lock_info_not_push);
                                    }
                                    break;
                            }
                        } else {
                            ToastUtils.showShort(R.string.please_refresh_page_get_newdata);
                        }
                    }
                }
            });
            for (int i = 0; i < mDeviceList.size(); i++) {
                if (HomeShowBean.TYPE_GATEWAY == mDeviceList.get(i).getDeviceType()) {
                    Object obj = mDeviceList.get(i).getObject();
                    String gwid = mDeviceList.get(i).getDeviceId();
                    if (obj instanceof GatewayInfo) {
                        GatewayInfo gatewayInfo = (GatewayInfo) obj;
                        String meUsername = gatewayInfo.getServerInfo().getMeUsername();
                        String mePwd = gatewayInfo.getServerInfo().getMePwd();
                        SPUtils.put(gwid, meUsername + "&" + mePwd);
                    }
                }
            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    private void initData(List<HomeShowBean> homeShowBeanList) {
        mDeviceList.clear();
        if (homeShowBeanList != null) {
            daoSession = MyApplication.getInstance().getDaoWriteSession();
            uid = MyApplication.getInstance().getUid();
            //清除数据库,可能存在用户在其他手机删除了设备，但是服务器已经没有该设备，所以会造成本地数据库误差
            daoSession.getGatewayServiceInfoDao().queryBuilder().where(GatewayServiceInfoDao.Properties.Uid.eq(uid)).buildDelete().executeDeleteWithoutDetachingEntities();
            daoSession.getGatewayLockServiceInfoDao().queryBuilder().where(GatewayLockServiceInfoDao.Properties.Uid.eq(uid)).buildDelete().executeDeleteWithoutDetachingEntities();
            daoSession.getBleLockServiceInfoDao().queryBuilder().where(BleLockServiceInfoDao.Properties.Uid.eq(uid)).buildDelete().executeDeleteWithoutDetachingEntities();
            if (homeShowBeanList.size() > 0) {
                noDeviceLayout.setVisibility(View.GONE);
                refresh.setVisibility(View.VISIBLE);
                mPresenter.getPublishNotify();
                mPresenter.listenerDeviceOnline();
                mPresenter.listenerNetworkChange();
                for (HomeShowBean homeShowBean : homeShowBeanList) {
                    LogUtils.d(homeShowBeanList.size() + "获取到大小     " + "获取到昵称  " + homeShowBean.getDeviceNickName());
                    //请求电量
                    switch (homeShowBean.getDeviceType()) {
                        case HomeShowBean.TYPE_GATEWAY_LOCK:
                            //网关锁
                            GwLockInfo gwLockInfo = (GwLockInfo) homeShowBean.getObject();
                            if (gwLockInfo != null) {
                                GatewayInfo gate = MyApplication.getInstance().getGatewayById(gwLockInfo.getGwID());
                                if (gate != null && gate.getEvent_str() != null && gate.getEvent_str().equals("offline")) {
                                    gwLockInfo.getServerInfo().setEvent_str("offline");
                                } else if (gate != null && gate.getEvent_str() == null) {
                                    gwLockInfo.getServerInfo().setEvent_str("offline");
                                }
                            }
                            DevicePower devicePower = daoSession.getDevicePowerDao().queryBuilder().where(DevicePowerDao.Properties.DeviceIdUid.eq(gwLockInfo.getServerInfo().getDeviceId() + uid)).unique();
                            if (devicePower != null) {
                                gwLockInfo.setPower(devicePower.getPower());
                            }

                            if (!gwLockInfo.getServerInfo().getEvent_str().equals("offline")) {
                                mPresenter.getPower(gwLockInfo.getGwID(), gwLockInfo.getServerInfo().getDeviceId(), MyApplication.getInstance().getUid());
                            }

                            //插入数据库
                            ServerGwDevice gwLock = gwLockInfo.getServerInfo();
                            String deviceId = gwLock.getDeviceId();
                            GatewayLockServiceInfo gatewayLockServiceInfo = new GatewayLockServiceInfo(deviceId + uid, gwLock.getDeviceId(),
                                    gwLock.getSW(), gwLock.getDevice_type(), gwLock.getEvent_str(), gwLock.getIpaddr(), gwLock.getMacaddr(),
                                    gwLock.getNickName(), gwLock.getTime(), gwLockInfo.getGwID(), uid,
                                    gwLock.getDelectTime(), gwLock.getLockversion(), gwLock.getModuletype(), gwLock.getNwaddr(), gwLock.getOfflineTime(),
                                    gwLock.getOnlineTime(), gwLock.getShareFlag(), gwLock.getPushSwitch());
                            daoSession.insertOrReplace(gatewayLockServiceInfo);
                            mDeviceList.add(homeShowBean);
                            break;
                        case HomeShowBean.TYPE_GATEWAY:
                            //网关
                            GatewayInfo gatewayInfo = (GatewayInfo) homeShowBean.getObject();
                            LogUtils.d("网关信息进入");
                            ServerGatewayInfo serverGatewayInfo = gatewayInfo.getServerInfo();
                            String deviceSn = serverGatewayInfo.getDeviceSN();
                            //插入数据库
                            GatewayServiceInfo gatewayServiceInfo =
                                    new GatewayServiceInfo(
                                            deviceSn + uid, serverGatewayInfo.getDeviceSN(), serverGatewayInfo.getDeviceNickName(), serverGatewayInfo.getAdminuid(), serverGatewayInfo.getAdminName(), serverGatewayInfo.getAdminNickname()
                                            , serverGatewayInfo.getIsAdmin(), serverGatewayInfo.getMeUsername(),
                                            serverGatewayInfo.getMePwd(), serverGatewayInfo.getMeBindState(), uid,
                                            serverGatewayInfo.getModel());
                            daoSession.getGatewayServiceInfoDao().insertOrReplace(gatewayServiceInfo);
                            try {
                                String gwid = gatewayInfo.getServerInfo().getDeviceSN();
                                String meName = gatewayInfo.getServerInfo().getMeUsername();
                                String mePwd = gatewayInfo.getServerInfo().getMePwd();
                                String me = meName + "&" + mePwd;
                                if (!TextUtils.isEmpty(gwid) && !TextUtils.isEmpty(meName) && !TextUtils.isEmpty(mePwd)) {
                                    SPUtils.put(gwid, me);
                                }
                            } catch (Exception e) {
                                LogUtils.d(e.getMessage());
                            }

                            mDeviceList.add(homeShowBean);
                            break;
                        case HomeShowBean.TYPE_BLE_LOCK:
                            //蓝牙锁
                            BleLockInfo bleLockInfo = (BleLockInfo) homeShowBean.getObject();
                            ServerBleDevice serverBleDevice = bleLockInfo.getServerLockInfo();
                            if (serverBleDevice != null) {
                                BleLockServiceInfo bleLockServiceInfo = new BleLockServiceInfo();
                                bleLockServiceInfo.setLockName(serverBleDevice.getLockName());
                                bleLockServiceInfo.setLockNickName(serverBleDevice.getLockNickName());
                                bleLockServiceInfo.setMacLock(serverBleDevice.getMacLock());  //缓存
                                bleLockServiceInfo.setOpen_purview(serverBleDevice.getOpen_purview());
                                bleLockServiceInfo.setIs_admin(serverBleDevice.getIs_admin());
                                bleLockServiceInfo.setCenter_latitude(serverBleDevice.getCenter_latitude());
                                bleLockServiceInfo.setCenter_longitude(serverBleDevice.getCenter_longitude());
                                bleLockServiceInfo.setCircle_radius(serverBleDevice.getCircle_radius());
                                bleLockServiceInfo.setAuto_lock(serverBleDevice.getAuto_lock());
                                bleLockServiceInfo.setPassword1(serverBleDevice.getPassword1());
                                bleLockServiceInfo.setPassword2(serverBleDevice.getPassword2());
                                bleLockServiceInfo.setModel(serverBleDevice.getModel());

                                bleLockServiceInfo.setDeviceSN(serverBleDevice.getDeviceSN());
                                bleLockServiceInfo.setCreateTime(serverBleDevice.getCreateTime());
                                bleLockServiceInfo.setSoftwareVersion(serverBleDevice.getSoftwareVersion());
                                bleLockServiceInfo.setBleVersion(serverBleDevice.getBleVersion());
                                bleLockServiceInfo.setUid(uid);
                                bleLockServiceInfo.setFunctionSet(serverBleDevice.getFunctionSet());
                                daoSession.insertOrReplace(bleLockServiceInfo);

                                //请求电量
                                DevicePower blePower = daoSession.getDevicePowerDao().queryBuilder().where(DevicePowerDao.Properties.DeviceSN.eq(serverBleDevice.getDeviceSN() + uid)).unique();
                                if (blePower != null) {
                                    bleLockInfo.setBattery(blePower.getPower());
                                }

                            }
                            mDeviceList.add(homeShowBean);
                            break;
                        case HomeShowBean.TYPE_CLOTHES_HANGER:
                            mDeviceList.add(homeShowBean);
                            break;
                        case HomeShowBean.TYPE_WIFI_LOCK:
                        case HomeShowBean.TYPE_WIFI_VIDEO_LOCK:
                            WifiLockInfo wifiLockInfo = (WifiLockInfo) homeShowBean.getObject();
                            mDeviceList.add(homeShowBean);
                            break;
                    }
                }
                if (deviceDetailAdapter != null) {
                    deviceDetailAdapter.notifyDataSetChanged();
                } else {
                    initAdapter();
                }
            } else {
                noDeviceLayout.setVisibility(View.VISIBLE);
                refresh.setVisibility(View.GONE);
            }
        } else {
            noDeviceLayout.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.GONE);
        }
        if (mDeviceList != null && mDeviceList.size() > 0) {

        }else {
            noDeviceLayout.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.GONE);
        }
    }


    private void initRefresh() {
        refresh.setEnableLoadMore(false);
        refresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //刷新页面
                if (NetUtil.isNetworkAvailable()) {
                    if (mqttService != null && mqttService.getMqttClient() != null && !mqttService.getMqttClient().isConnected()) {
                        MyApplication.getInstance().getMqttService().mqttConnection(); //重新连接mqtt
                        LogUtils.d("重新连接mqtt");

                    }
                    mPresenter.refreshData();
                    refreshLayout.finishRefresh(8 * 1000);
                    if (deviceDetailAdapter != null) {
                        deviceDetailAdapter.setProductList(MyApplication.getInstance().getProductInfos());
                        deviceDetailAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.showShort(getString(R.string.network_exception));
                    refreshLayout.finishRefresh();
                }

            }
        });
    }

    @Override
    public void onDeviceRefresh(AllBindDevices allBindDevices) {
        //数据更新了
        if (refresh != null) {
            refresh.finishRefresh();
        }
        if (allBindDevices != null) {
            homeShowBeanList = MyApplication.getInstance().getAllDevices();
            initData(homeShowBeanList);
        } else {
            initData(null);
        }

    }


    @Override
    public void deviceDataRefreshFail() {
        LogUtils.d("刷新页面失败");
        refresh.finishRefresh();
        ToastUtils.showShort(R.string.refresh_data_fail);
    }

    @Override
    public void deviceDataRefreshThrowable(Throwable throwable) {
        //刷新页面异常
        refresh.finishRefresh();
        ToastUtils.showShort(R.string.refresh_data_fail);
        LogUtils.d("刷新页面异常");
    }

    @Override
    public void getDevicePowerSuccess(String gatewayId, String devciceId, int power, String timestamp) {
        LogUtils.d("设备SN" + devciceId + "设备电量" + power);
        if (mDeviceList != null && mDeviceList.size() > 0) {
            for (HomeShowBean device : mDeviceList) {
                if (HomeShowBean.TYPE_GATEWAY_LOCK == device.getDeviceType()) {
                    if (device.getDeviceId().equals(devciceId)) {
                        GwLockInfo gwLockInfo = (GwLockInfo) device.getObject();
                       /* if (gwLockInfo.getServerInfo().getEvent_str().equals("offline")) {
                            gwLockInfo.getServerInfo().setEvent_str("online");
                        }*/
                        gwLockInfo.setPower(power);
                        gwLockInfo.setPowerTimeStamp(timestamp);
                        //缓存电量
                        DevicePower devicePower = new DevicePower(devciceId + uid, devciceId, power);
                        daoSession.insertOrReplace(devicePower);
                        if (deviceDetailAdapter != null) {
                            deviceDetailAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }


    }

    @Override
    public void getDevicePowerFail(String gatewayId, String deviceId) {


    }

    @Override
    public void getDevicePowerThrowable(String gatewayId, String deviceId) {

    }

    @Override
    public void gatewayStatusChange(String gatewayId, String evnetStr) {
        //网关状态发生改变
        LogUtils.d("DeviceFragment网关状态发生改变");
        if (mDeviceList != null && mDeviceList.size() > 0) {
            for (HomeShowBean device : mDeviceList) {
                //网关
                if (device.getDeviceType() == HomeShowBean.TYPE_GATEWAY) {
                    GatewayInfo gatewayInfo = (GatewayInfo) device.getObject();
                    if (gatewayInfo.getServerInfo().getDeviceSN().equals(gatewayId)) {
                        LogUtils.d("监听网关Device的状态      " + gatewayId + "连接状态" + evnetStr);
                        gatewayInfo.setEvent_str(evnetStr);
                        //获取网关下绑定的设备,把网关下的设备设置为离线.网关离线设备也离线
                        if ("offline".equals(evnetStr)) {
                            switch (device.getDeviceType()) {
                                //网关锁
                                case HomeShowBean.TYPE_GATEWAY_LOCK:
                                    GwLockInfo gwLockInfo = (GwLockInfo) device.getObject();
                                    if (gwLockInfo.getGwID().equals(gatewayId)) {
                                        gwLockInfo.getServerInfo().setEvent_str("offline");
                                    }
                                    break;
                            }

                        }
                        if (deviceDetailAdapter != null) {
                            deviceDetailAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }

    }

    @Override
    public void deviceStatusChange(String gatewayId, String deviceId, String eventStr) {
        //设备状态发生改变
        LogUtils.d("DeviceFragment设备状态发生改变");
        if (mDeviceList != null && mDeviceList.size() > 0) {
            for (HomeShowBean homeShowBean : mDeviceList) {
                if (deviceId.equals(homeShowBean.getDeviceId())) {
                    switch (homeShowBean.getDeviceType()) {
                        //网关锁上线
                        case HomeShowBean.TYPE_GATEWAY_LOCK:
                            GwLockInfo gwLockInfo = (GwLockInfo) homeShowBean.getObject();
                            if (gwLockInfo.getGwID().equals(gatewayId) && gwLockInfo.getServerInfo().getDeviceId().equals(deviceId)) {
                                if ("online".equals(eventStr)) {
                                    gwLockInfo.getServerInfo().setEvent_str("online");
                                } else if ("offline".equals(eventStr)) {
                                    gwLockInfo.getServerInfo().setEvent_str("offline");
                                }
                                if (deviceDetailAdapter != null) {
                                    deviceDetailAdapter.notifyDataSetChanged();
                                }
                                LogUtils.d("网关锁上线下线了   " + eventStr + "网关的设备id  " + deviceId);
                            }
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void networkChangeSuccess() {
        //网络断开
        if (deviceDetailAdapter != null) {
            deviceDetailAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void bindMimiSuccess(String deviceSN) {
        //绑定咪咪网成功
        LogUtils.d("咪咪绑定注册成功");
    }

    @Override
    public void bindMimiFail(String code, String msg) {
        LogUtils.d("咪咪绑定注册失败" + code + "咪咪绑定失败原因" + msg);
    }

    @Override
    public void bindMimiThrowable(Throwable throwable) {
        LogUtils.d("咪咪绑定异常");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser == true) {
            //切换左右切换Fragment时，刷新页面
            if (mDeviceList != null && mDeviceList.size() > 0) {
                if (mqttService != null && mqttService.getMqttClient() != null && !mqttService.getMqttClient().isConnected()) {
                    LogUtils.d("重连次数" + mqttService.reconnectionNum);
                    if (mqttService.reconnectionNum == 0) {
                        ToastUtils.showShort(getString(R.string.mqtt_already_disconnect_refresh));
                    }
                }
                if (deviceDetailAdapter != null) {
                    deviceDetailAdapter.notifyDataSetChanged();
                }
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KeyConstants.GET_BLE_POWER) {
            if (resultCode == Activity.RESULT_OK) {
                BleLockInfo getBle = (BleLockInfo) data.getSerializableExtra(KeyConstants.BLE_INTO);
                if (getBle != null && mDeviceList != null && mDeviceList.size() > 0) {
                    for (HomeShowBean device : mDeviceList) {
                        //蓝牙电量
                        if (HomeShowBean.TYPE_BLE_LOCK == device.getDeviceType()) {
                            if (device.getDeviceId().equals(getBle.getServerLockInfo().getLockName())) {
                                BleLockInfo bleLockInfo = (BleLockInfo) device.getObject();
                                bleLockInfo.setBattery(getBle.getBattery());
                                String deviceSN = bleLockInfo.getServerLockInfo().getDeviceSN();
                                DevicePower devicePower = new DevicePower(deviceSN + uid, deviceSN, getBle.getBattery());
                                daoSession.insertOrReplace(devicePower);
                                if (getBle.isConnected()) {
                                    bleLockInfo.setConnected(true);
                                } else {
                                    bleLockInfo.setConnected(false);
                                }
                                if (deviceDetailAdapter != null) {
                                    deviceDetailAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
