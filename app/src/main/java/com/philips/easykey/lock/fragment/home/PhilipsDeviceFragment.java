package com.philips.easykey.lock.fragment.home;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.PhilipsAddDeviceActivity;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockCallingActivity;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockDetailActivity;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockPasswordTypeActivity;
import com.philips.easykey.lock.activity.device.wifilock.PhilipsWifiLockRecordActivity;
import com.philips.easykey.lock.adapter.PhilipsDeviceTypeAdapter;
import com.philips.easykey.lock.adapter.PhilipsRvHomeDeviceAdapter;
import com.philips.easykey.lock.adapter.PhilipsVpHomeDevicesAdapter;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.bean.PhilipsDeviceBean;
import com.philips.easykey.lock.bean.PhilipsDeviceTypeBean;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockOperationRecord;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiLockOperationRecordResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.greenDao.bean.ClothesHangerMachineAllBean;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * author :
 * time   : 2021/4/21
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsDeviceFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private PhilipsDeviceTypeAdapter mDeviceTypeAdapter;
    private LinearLayout mllNoDevice;
    private ViewPager mVPDevices;
    private RecyclerView mRvDevices;
    private TextView mTvCount, mTvCurrentPage;
    private ImageView mIvGrid, mIvList;

    private PhilipsVpHomeDevicesAdapter mVpHomeDevicesAdapter;
    private PhilipsRvHomeDeviceAdapter mRvHomeDeviceAdapter;
    private boolean isCardShow = true;

    private int mCurrentTab = 0;

    private final List<HomeShowBean> mDevices = new ArrayList<>();
    private final ArrayList<PhilipsDeviceBean> mAllDeviceBeans = new ArrayList<>();
    private final ArrayList<PhilipsDeviceBean> mWillShowDeviceBeans = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_device, container, false);

        mllNoDevice = root.findViewById(R.id.llNoDevice);
        mTvCount = root.findViewById(R.id.tvCount);
        mTvCurrentPage = root.findViewById(R.id.tvCurrentPage);

        initDataViewFromCardType(root);
        initTab(root);
        initSelectBtn(root);
        initDataViewFromListType(root);

        ImageView ivAddDevice = root.findViewById(R.id.ivAddDevice);
        Button btnAddDevice = root.findViewById(R.id.btnAddDevice);
        btnAddDevice.setOnClickListener(v -> rcQRCodePermissions());
        ivAddDevice.setOnClickListener(v -> rcQRCodePermissions());
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void gotoAddDeviceAct() {
        Intent intent = new Intent(getActivity(), PhilipsAddDeviceActivity.class);
        startActivity(intent);
    }

    private void initDataViewFromListType(View root) {
        mRvDevices = root.findViewById(R.id.rvDevices);
        mRvHomeDeviceAdapter = new PhilipsRvHomeDeviceAdapter(R.layout.philips_item_home_device_rv);
        mRvHomeDeviceAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), PhilipsWifiVideoLockDetailActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, mWillShowDeviceBeans.get(position).getWifiSn());
            startActivity(intent);
        });
        mRvDevices.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvDevices.setAdapter(mRvHomeDeviceAdapter);
    }

    private void initSelectBtn(View root) {
        mIvGrid = root.findViewById(R.id.ivGrid);
        mIvList = root.findViewById(R.id.ivList);
        mIvGrid.setOnClickListener(v -> {
            isCardShow = true;
            showCardView();
        });
        mIvList.setOnClickListener(v -> {
            isCardShow = false;
            showListView();
        });
    }

    private void initDataViewFromCardType(View root) {
        mVPDevices = root.findViewById(R.id.vpDevices);
        mVPDevices.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTvCurrentPage.setText(StringUtils.format("%1d", mWillShowDeviceBeans.size()==0?0:mVPDevices.getCurrentItem()+1));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mVpHomeDevicesAdapter = new PhilipsVpHomeDevicesAdapter(getContext(), R.layout.philips_item_home_device_vp, mWillShowDeviceBeans);
        mVpHomeDevicesAdapter.setOnClickMoreListener((v, data) -> {
            Intent intent = new Intent(getActivity(), PhilipsWifiVideoLockDetailActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, data.getWifiSn());
            startActivity(intent);
        });
        mVpHomeDevicesAdapter.setOnClickMessageListener((v, data) -> {
            Intent intent = new Intent(getActivity(), PhilipsWifiLockRecordActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, data.getWifiSn());
            startActivity(intent);
        });
        mVpHomeDevicesAdapter.setOnClickPasswordListener((v, data) -> {
            Intent intent = new Intent(getActivity(), PhilipsWifiVideoLockPasswordTypeActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, data.getWifiSn());
            startActivity(intent);
        });
        mVpHomeDevicesAdapter.setOnClickCallingListener((v, data) -> {
            if(data.getDeviceType() != HomeShowBean.TYPE_WIFI_VIDEO_LOCK) return;
            if(data.getPowerSave() == 1){
                powerStatusDialog();
                return;
            }
            Intent intent = new Intent(getActivity(), PhilipsWifiVideoLockCallingActivity.class);
            intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_CALLING,0);
            intent.putExtra(KeyConstants.WIFI_SN, data.getWifiSn());
            startActivity(intent);

        });
        mVPDevices.setOffscreenPageLimit(4);
        mVPDevices.setPageTransformer(false, new PhilipsVpTransform());
        mVPDevices.setAdapter(mVpHomeDevicesAdapter);
    }

    private void initTab(View root) {
        mDeviceTypeAdapter = new PhilipsDeviceTypeAdapter(R.layout.philips_item_device_tap);
        mDeviceTypeAdapter.setOnItemClickListener((adapter, view, position) -> {
            for (PhilipsDeviceTypeBean bean : mDeviceTypeAdapter.getData()) {
                bean.setSelected(false);
            }
            mDeviceTypeAdapter.getData().get(position).setSelected(true);
            mCurrentTab = position;
            initCardData(position);
            mDeviceTypeAdapter.notifyDataSetChanged();
        });

        RecyclerView rvDeviceTap = root.findViewById(R.id.rvDeviceTap);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvDeviceTap.setLayoutManager(layoutManager);
        rvDeviceTap.setAdapter(mDeviceTypeAdapter);
    }

    private void initCardData(int position) {
        if(position == 0) {
            changedWillShowDevice(0);
        } else if(position == 1) {
            changedWillShowDevice(6);
        } else if(position == 2) {
            changedWillShowDevice(7);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initTabData();
        initDevices();
        initOperationRecord();
    }

    private void initTabData() {
        ArrayList<PhilipsDeviceTypeBean> list = new ArrayList<>();
        PhilipsDeviceTypeBean bean1 = new PhilipsDeviceTypeBean();
        // TODO: 2021/4/28 抽离文字
        bean1.setTypeName("所有设备");
        bean1.setSelected(true);
        list.add(bean1);
        PhilipsDeviceTypeBean bean2 = new PhilipsDeviceTypeBean();
        bean2.setTypeName("智能锁");
        bean2.setSelected(false);
        list.add(bean2);
        PhilipsDeviceTypeBean bean3 = new PhilipsDeviceTypeBean();
        bean3.setTypeName("晾衣机");
        bean3.setSelected(false);
        list.add(bean3);
        mDeviceTypeAdapter.setList(list);
    }

    private void initDevices() {
        mAllDeviceBeans.clear();
        mDevices.clear();
        mDevices.addAll(MyApplication.getInstance().getHomeShowDevices());
        if(mDevices.isEmpty()) {
            mllNoDevice.setVisibility(View.VISIBLE);
            mTvCurrentPage.setVisibility(View.GONE);
            mTvCount.setVisibility(View.GONE);
        } else {
            mllNoDevice.setVisibility(View.GONE);
            if(isCardShow) {
                mTvCurrentPage.setVisibility(View.VISIBLE);
                mTvCount.setVisibility(View.VISIBLE);
            }
        }
        for (HomeShowBean bean : mDevices) {
            PhilipsDeviceBean deviceBean = new PhilipsDeviceBean();
            deviceBean.setDeviceName(bean.getDeviceNickName());
            deviceBean.setDeviceType(bean.getDeviceType());
            if(bean.getObject() instanceof WifiLockInfo) {
                deviceBean.setLastRecordDetail(getLastOperationRecord(((WifiLockInfo) bean.getObject()).getWifiSN()));
                deviceBean.setWifiSn(((WifiLockInfo) bean.getObject()).getWifiSN());
                deviceBean.setPowerSave(((WifiLockInfo) bean.getObject()).getPowerSave());
                deviceBean.setPower(((WifiLockInfo) bean.getObject()).getPower());
            } else if(bean.getObject() instanceof ClothesHangerMachineAllBean) {
                deviceBean.setWifiSn(((ClothesHangerMachineAllBean) bean.getObject()).getWifiSN());
            }
            mAllDeviceBeans.add(deviceBean);
        }
        mWillShowDeviceBeans.clear();
        mWillShowDeviceBeans.addAll(mAllDeviceBeans);
        mVpHomeDevicesAdapter.notifyDataSetChanged();
        mTvCurrentPage.setText(StringUtils.format("%1d", mWillShowDeviceBeans.size()==0?0:mVPDevices.getCurrentItem()+1));
        mTvCount.setText(getString(R.string.philips_device_count, mWillShowDeviceBeans.size()));
    }

    private WifiLockOperationRecord getLastOperationRecord(String wifiSn) {
        String localRecord = (String) SPUtils.get(KeyConstants.WIFI_LOCK_OPERATION_RECORD + wifiSn,"");
        List<WifiLockOperationRecord> records = new Gson().fromJson(localRecord, new TypeToken<List<WifiLockOperationRecord>>() {
        }.getType());
        if(records == null) return null;
        if(records.size() <= 0) return null;
        if(records.size() == 1){
            return records.get(0);
        }
        if(records.size() > 1){
            long[] createTime = new long[2];
            createTime[0] = records.get(0).getCreateTime();
            createTime[1] = 0;
            for(int i = 0;i < records.size();i++){
                if(createTime[0] <= records.get(i).getCreateTime()){
                    createTime[0] = records.get(i).getCreateTime();
                    createTime[1] = i;
                    continue;
                }
            }
            return (records.get((int) createTime[1]));
        }
        return null;
    }

    private void changedWillShowDevice(int type) {
        mWillShowDeviceBeans.clear();
        if(type == 0) {
            mWillShowDeviceBeans.addAll(mAllDeviceBeans);
        } else {
            for (PhilipsDeviceBean bean : mAllDeviceBeans) {
                if(bean.getDeviceType() == type) {
                    mWillShowDeviceBeans.add(bean);
                }
            }
        }
        if(isCardShow) {
            mVpHomeDevicesAdapter.notifyDataSetChanged();
            if(!mWillShowDeviceBeans.isEmpty()) {
                mVPDevices.setCurrentItem(0);
            }
            mTvCurrentPage.setText(StringUtils.format("%1d", mWillShowDeviceBeans.size()==0?0:mVPDevices.getCurrentItem()+1));
            mTvCount.setText(getString(R.string.philips_device_count, mWillShowDeviceBeans.size()));
        } else {
            mRvHomeDeviceAdapter.setList(mWillShowDeviceBeans);
        }

        LogUtils.d("changedWillShowDevice type: " + type + " size: " + mWillShowDeviceBeans.size());
    }

    private void showCardView() {
        if(getContext() == null) return;
        mIvList.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.philips_icon_list_default));
        mIvGrid.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.philips_icon_card_selected));
        mTvCurrentPage.setVisibility(mWillShowDeviceBeans.isEmpty()?View.GONE:View.VISIBLE);
        mTvCount.setVisibility(mWillShowDeviceBeans.isEmpty()?View.GONE:View.VISIBLE);
        mVPDevices.setVisibility(View.VISIBLE);
        mRvDevices.setVisibility(View.GONE);

        initCardData(mCurrentTab);
    }

    private void showListView() {
        if(getContext() == null) return;
        mIvList.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.philips_icon_list_selected));
        mIvGrid.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.philips_icon_card_default));
        mTvCurrentPage.setVisibility(View.GONE);
        mTvCount.setVisibility(View.GONE);
        mVPDevices.setVisibility(View.GONE);
        mRvDevices.setVisibility(View.VISIBLE);

        mRvHomeDeviceAdapter.setList(mWillShowDeviceBeans);
    }


    /*------------------------------ 权限处理 ------------------------------*/

    private static final int RC_QR_CODE_PERMISSIONS = 9999;
    private static final int RC_CAMERA_PERMISSIONS = 7777;
    private static final int RC_READ_EXTERNAL_STORAGE_PERMISSIONS = 8888;


    @AfterPermissionGranted(RC_QR_CODE_PERMISSIONS)
    private void rcQRCodePermissions() {
        String[] perms = new String[] {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(getContext(), perms)) {
            // TODO: 2021/4/29 抽离提示语
            EasyPermissions.requestPermissions(this, "扫描二维码需要请求的权限",
                    RC_QR_CODE_PERMISSIONS, perms);
        } else {
            gotoAddDeviceAct();
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERMISSIONS)
    private void rcCameraPermission() {
        if(!hasCameraPermission()) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要的相机权限",
                    RC_CAMERA_PERMISSIONS, Manifest.permission.CAMERA);
        }
    }


    @AfterPermissionGranted(RC_READ_EXTERNAL_STORAGE_PERMISSIONS)
    private void rcReadStoragePermission(){
        if(!hasReadExternalStoragePermission()) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要的读取权限",
                    RC_READ_EXTERNAL_STORAGE_PERMISSIONS, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(getContext(), Manifest.permission.CAMERA);
    }

    private boolean hasReadExternalStoragePermission() {
        return EasyPermissions.hasPermissions(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        LogUtils.d("onPermissionsGranted requestCode: " + requestCode);
        if(perms.isEmpty()) {
            LogUtils.e("onPermissionsGranted 返回的权限不存在数据 perms size: " + perms.size());
            return;
        }
        if(requestCode == RC_QR_CODE_PERMISSIONS) {
            if(perms.size() == 2) {
                LogUtils.d("onPermissionsGranted 同时两条权限都请求成功");
            } else if(perms.get(0).equals(Manifest.permission.CAMERA)) {
                LogUtils.d("onPermissionsGranted 只有相机权限成功");
                if(hasReadExternalStoragePermission()) {
                } else {
                    rcReadStoragePermission();
                }
            } else if(perms.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                LogUtils.d("onPermissionsGranted 只有存储权限成功");
                if(hasCameraPermission()) {
                } else {
                    rcCameraPermission();
                }
            }
        } else if(requestCode == RC_CAMERA_PERMISSIONS || requestCode == RC_READ_EXTERNAL_STORAGE_PERMISSIONS) {
            LogUtils.d("onPermissionsGranted 请求剩下的权限成功");
            gotoAddDeviceAct();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        LogUtils.d("onPermissionsDenied requestCode: " + requestCode);
        if(perms.get(0).equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            LogUtils.e("onPermissionsDenied 拒绝了扫描二维码需要的储存权限, requestCode: " + requestCode);
        } else if(perms.get(0).equals(Manifest.permission.CAMERA)) {
            LogUtils.e("onPermissionsDenied 拒绝了扫描二维码需要的相机权限, requestCode: " + requestCode);
        }

    }

    public void powerStatusDialog(){
        AlertDialogUtil.getInstance().noEditSingleButtonDialog(getActivity(), getString(R.string.set_failed), "\n"+ getString(R.string.dialog_wifi_video_power_status) +"\n",
                getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void initOperationRecord() {
        for(HomeShowBean bean : MyApplication.getInstance().getHomeShowDevices()){
            if(bean.getDeviceType() == HomeShowBean.TYPE_WIFI_VIDEO_LOCK
                    || bean.getDeviceType() == HomeShowBean.TYPE_WIFI_LOCK){
                saveOperationRecordForNet(((WifiLockInfo)bean.getObject()).getWifiSN());
            }
        }
    }

    private void saveOperationRecordForNet(String wifiSN) {
        getOpenRecordFromServer(1,wifiSN);
    }

    public void getOpenRecordFromServer(int page, String wifiSn) {
        XiaokaiNewServiceImp.wifiLockGetOperationList(wifiSn, page)
                .timeout(10 *1000, TimeUnit.MILLISECONDS)
                .subscribe(new BaseObserver<GetWifiLockOperationRecordResult>() {
                    @Override
                    public void onSuccess(GetWifiLockOperationRecordResult operationRecordResult) {
                        if (operationRecordResult.getData() != null && operationRecordResult.getData().size() > 0) {  //服务器没有数据  提示用户
                            if (page == 1) {
                                SPUtils.put(KeyConstants.WIFI_LOCK_OPERATION_RECORD + wifiSn, new Gson().toJson(operationRecordResult.getData()));
                                for(PhilipsDeviceBean bean : mWillShowDeviceBeans){
                                    if(bean.getWifiSn().equals(wifiSn)){
                                        if(operationRecordResult.getData().size() >= 1){
                                            long[] createTime = new long[2];
                                            createTime[0] = operationRecordResult.getData().get(0).getCreateTime();
                                            createTime[1] = 0;
                                            for(int i = 0;i < operationRecordResult.getData().size();i++){
                                                if(createTime[0] <= operationRecordResult.getData().get(i).getCreateTime()){
                                                    createTime[0] = operationRecordResult.getData().get(i).getCreateTime();
                                                    createTime[1] = i;
                                                    continue;
                                                }
                                            }
                                            bean.setLastRecordDetail(operationRecordResult.getData().get((int) createTime[1]));
                                            mRvHomeDeviceAdapter.notifyDataSetChanged();
                                            mVpHomeDevicesAdapter.notifyDataSetChanged();
                                            break;
                                        }
                                    }
                                }
                            }
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
                    }
                });
    }
}
