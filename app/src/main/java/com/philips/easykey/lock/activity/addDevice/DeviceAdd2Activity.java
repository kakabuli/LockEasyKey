package com.philips.easykey.lock.activity.addDevice;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.addDevice.bluetooth.AddBluetoothFirstActivity;
import com.philips.easykey.lock.activity.addDevice.gateway.AddGatewayFirstActivity;
import com.philips.easykey.lock.activity.addDevice.zigbeelocknew.AddDeviceZigbeeLockNewZeroActivity;
import com.philips.easykey.lock.activity.addDevice.zigbeelocknew.QrCodeScanActivity;
import com.philips.easykey.lock.activity.device.clotheshangermachine.ClothesHangerMachineAddActivity;
import com.philips.easykey.lock.activity.device.clotheshangermachine.ClothesHangerMachineAddFirstActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewFirstActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewToChooseActivity;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.deviceaddpresenter.DeviceZigBeeDetailPresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.DeviceZigBeeDetailView;
import com.philips.easykey.lock.publiclibrary.bean.GatewayInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.Constants;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.PermissionUtil;
import com.philips.easykey.lock.utils.clothesHangerMachineUtil.ClothesHangerMachineUtil;
import com.philips.easykey.lock.utils.dialog.MessageDialog;

import java.util.List;

public class DeviceAdd2Activity extends BaseActivity<DeviceZigBeeDetailView, DeviceZigBeeDetailPresenter<DeviceZigBeeDetailView>> implements DeviceZigBeeDetailView {


    ImageView back;
    ImageView scan;
    RelativeLayout bleLock;
    RelativeLayout wifiLock;
    RelativeLayout zigbeeLock;
    LinearLayout catEye;
    LinearLayout rg4300;
    LinearLayout gw6032;
    LinearLayout gw6010;
    LinearLayout singleSwitch;
    LinearLayout doubleSwitch;
    private boolean flag = false; //判断是否有绑定的网列表
    private int isAdmin = 1; //管理员，非1不是管理员
    private MessageDialog messageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_add2);
        initView();
        initOnClickListener();
        initData();
    }

    @Override
    protected DeviceZigBeeDetailPresenter<DeviceZigBeeDetailView> createPresent() {
        return new DeviceZigBeeDetailPresenter<>();
    }

    private void initView() {
        back = findViewById(R.id.back);
        scan = findViewById(R.id.scan);
        bleLock = findViewById(R.id.ble_lock);
        wifiLock = findViewById(R.id.wifi_lock);
        zigbeeLock = findViewById(R.id.zigbee_lock);
        catEye = findViewById(R.id.cat_eye);
        rg4300 = findViewById(R.id.rg4300);
        gw6032 = findViewById(R.id.gw6032);
        gw6010 = findViewById(R.id.gw6010);
        singleSwitch = findViewById(R.id.single_switch);
        doubleSwitch = findViewById(R.id.double_switch);
    }

    private void initOnClickListener() {
        back.setOnClickListener(v -> finish());
        scan.setOnClickListener(v -> {
            String[] strings = PermissionUtil.getInstance().checkPermission(new String[]{  Manifest.permission.CAMERA});
            if (strings.length>0){
                ToastUtils.showShort(getString(R.string.philips_activity_deviceadd2));
                PermissionUtil.getInstance().requestPermission(new String[]{  Manifest.permission.CAMERA}, this);
            }else {
                Intent scanIntent = new Intent(this, QrCodeScanActivity.class);
                scanIntent.putExtra(KeyConstants.SCAN_TYPE, 1);
                startActivityForResult(scanIntent, KeyConstants.SCANGATEWAYNEW_REQUEST_CODE);
            }
        });
        bleLock.setOnClickListener(v -> {
            Intent bluetoothIntent = new Intent(DeviceAdd2Activity.this, AddBluetoothFirstActivity.class);
            startActivity(bluetoothIntent);
        });
        findViewById(R.id.face_lock).setOnClickListener(v -> {
            Intent faceIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
            String faveType = "WiFi";
            faceIntent.putExtra("wifiModelType", faveType);
            startActivity(faceIntent);
        });
        findViewById(R.id.video_lock).setOnClickListener(v -> {
            //视频WIFI锁
            Intent wifiIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
            String wifiModelType = "WiFi&VIDEO";
            wifiIntent.putExtra("wifiModelType", wifiModelType);
            startActivity(wifiIntent);
        });
        wifiLock.setOnClickListener(v -> {
            //                startActivity(new Intent(this,WifiLockAddNewFirstActivity.class));
            Intent chooseAddIntent = new Intent(this, WifiLockAddNewToChooseActivity.class);
            chooseAddIntent.putExtra(KeyConstants.SCAN_TYPE, 1);
            startActivityForResult(chooseAddIntent, KeyConstants.SCANGATEWAYNEW_REQUEST_CODE);
        });
        zigbeeLock.setOnClickListener(v -> {
            if ((flag == true && isAdmin == 0) || (flag == true && isAdmin == 1)) {
                Intent zigbeeIntent = new Intent(DeviceAdd2Activity.this, DeviceBindGatewayListActivity.class);
                int type = 3;
                zigbeeIntent.putExtra("type", type);
                startActivity(zigbeeIntent);

            } else if (flag == false) {
                AlertDialogUtil.getInstance().havaNoEditTwoButtonDialog(DeviceAdd2Activity.this, getString(R.string.no_usable_gateway), getString(R.string.add_zigbee_device_first_pair_gateway), getString(R.string.philips_cancel), getString(R.string.configuration), "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        //跳转到配置网关添加的流程
                        Intent gatewayIntent = new Intent(DeviceAdd2Activity.this, AddGatewayFirstActivity.class);
                        startActivity(gatewayIntent);
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(String toString) {
                    }
                });
            }
        });
        catEye.setOnClickListener(v -> {
            if ((flag == true && isAdmin == 0) || (flag == true && isAdmin == 1)) {

                Intent catEyeIntent = new Intent(this, DeviceBindGatewayListActivity.class);
                int type = 2;
                catEyeIntent.putExtra("type", type);
                startActivity(catEyeIntent);

            } else if (flag == false) {
                AlertDialogUtil.getInstance().havaNoEditTwoButtonDialog(this, getString(R.string.no_usable_gateway), getString(R.string.add_zigbee_device_first_pair_gateway), getString(R.string.philips_cancel), getString(R.string.configuration), "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }
                    @Override
                    public void right() {
                        //跳转到配置网关添加的流程
                        Intent gatewayIntent = new Intent(DeviceAdd2Activity.this, AddGatewayFirstActivity.class);
                        startActivity(gatewayIntent);
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(String toString) {
                    }
                });
//                    Intent catEyeIntent = new Intent(this, DeviceBindGatewayListActivity.class);
//                    int type =2;
//                    catEyeIntent.putExtra("type", type);
//                    startActivity(catEyeIntent);
            }
//                else{
//                    Intent catEyeIntent = new Intent(this, DeviceBindGatewayListActivity.class);
//                    int type =2;
//                    catEyeIntent.putExtra("type", type);
//                    startActivity(catEyeIntent);
//                }
        });
        gw6010.setOnClickListener(v -> {
            //跳转到添加网关
            Intent addGateway = new Intent(this, AddGatewayFirstActivity.class);
            startActivity(addGateway);
        });
        gw6032.setOnClickListener(v -> {
            //跳转到添加网关
            Intent addGateway = new Intent(this, AddGatewayFirstActivity.class);
            startActivity(addGateway);
        });
        findViewById(R.id._3d_lock).setOnClickListener(v -> {
            Intent k11fIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
            String k11fType = "WiFi";
            k11fIntent.putExtra("wifiModelType", k11fType);
            startActivity(k11fIntent);
        });
        findViewById(R.id.k11f_lock).setOnClickListener(v -> {
            Intent k11fIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
            String k11fType = "WiFi";
            k11fIntent.putExtra("wifiModelType", k11fType);
            startActivity(k11fIntent);
        });
        findViewById(R.id.k20v_lock).setOnClickListener(v -> {
            //视频WIFI锁
            Intent k20vIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
            String k20vModelType = "WiFi&VIDEO";
            k20vIntent.putExtra("wifiModelType", k20vModelType);
            startActivity(k20vIntent);
        });
        findViewById(R.id.clothes_machine).setOnClickListener(v -> {
            Intent clothesMachineAddIntent = new Intent(this, ClothesHangerMachineAddActivity.class);
            clothesMachineAddIntent.putExtra(KeyConstants.SCAN_TYPE, 1);
            startActivityForResult(clothesMachineAddIntent, KeyConstants.SCANGATEWAYNEW_REQUEST_CODE);
        });
    }

    private void initData() {
        List<HomeShowBean> gatewayList = mPresenter.getGatewayBindList();
        if (gatewayList != null) {
            if (gatewayList.size() > 0) {
                flag = true;
                if (gatewayList.size() == 1) {
                    HomeShowBean homeShowBean = gatewayList.get(0);
                    GatewayInfo gatewayInfo = (GatewayInfo) homeShowBean.getObject();
                    if (gatewayInfo.getServerInfo().getIsAdmin() == 1) {
                        isAdmin = 1;
                    } else {
                        isAdmin = 0;
                    }
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case KeyConstants.SCANGATEWAYNEW_REQUEST_CODE:
                    String result = data.getStringExtra(Constants.SCAN_QR_CODE_RESULT);
                    LogUtils.d("扫描结果是   " + result);

                    if (result.contains("SN-GW") && result.contains("MAC-") && result.contains(" ")) {
                        String[] strs = result.split(" ");
                        String deviceSN = strs[0].replace("SN-", "");
                        Intent scanSuccessIntent = new Intent(DeviceAdd2Activity.this, AddDeviceZigbeeLockNewZeroActivity.class);
                        scanSuccessIntent.putExtra("deviceSN", deviceSN);
                        LogUtils.d("设备SN是   " + deviceSN);
                        startActivity(scanSuccessIntent);
                        finish();
//                    } else if ( (result.contains("_WiFi_1")||result.contains("_WiFi_master"))){  //老的4-1配网
//                        startActivity(new Intent(this,WifiLockAPAddFirstActivity.class));
//                    } else if ( (result.contains("_WiFi_2")||result.contains("_WiFi_fast"))){  //新的快速配网
//                        startActivity(new Intent(this,WifiLockAddNewFirstActivity.class));
                    } else if ( (result.contains("_WiFi_"))){  //4-30新的配网流程
                        if(result.equals("PHILIPS_WiFi_camera")){
                            //视频WIFI锁
                            Intent wifiIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
                            String wifiModelType = "WiFi&VIDEO";
                            wifiIntent.putExtra("wifiModelType", wifiModelType);
                            startActivity(wifiIntent);
                        }else{
                            Intent wifiIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
                            String wifiModelType = "WiFi";
                            wifiIntent.putExtra("wifiModelType", wifiModelType);
                            startActivity(wifiIntent);
                        }

                    } else if ( (result.contains("http://qr01.cn/EYopdB"))){  //已生产的错误的X1二维码
                        Intent wifiIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
                        String wifiModelType = "WiFi";
                        wifiIntent.putExtra("wifiModelType", wifiModelType);
                        startActivity(wifiIntent);
                    }else if ( (result.contains("_WiFi&BLE_"))){  //5-11WiFi&BLE，蓝牙Wi-Fi模组配网
                        String[] str = result.split("_");
                        if(str.length > 0){
                            if(str.length >= 4 && result.contains("SmartHanger")){
                                if(ClothesHangerMachineUtil.pairMode(str[1]).equals(str[2])){
                                    Intent clothesMachineIntent = new Intent(this, ClothesHangerMachineAddFirstActivity.class);
                                    clothesMachineIntent.putExtra("wifiModelType",str[2]);
                                    startActivity(clothesMachineIntent);
                                    return;
                                }
                            }
                        }
                        Intent wifiIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
                        String wifiModelType = "WiFi&BLE";
                        wifiIntent.putExtra("wifiModelType", wifiModelType);
                        startActivity(wifiIntent);
                    } else if(result.contains("WiFi&VIDEO") || result.contains("kaadas_WiFi_camera")){
                        //视频WIFI锁
                        Intent wifiIntent = new Intent(this, WifiLockAddNewFirstActivity.class);
                        String wifiModelType = "WiFi&VIDEO";
                        wifiIntent.putExtra("wifiModelType", wifiModelType);
                        startActivity(wifiIntent);
                    } else {
                        unKnowQr();
                    }
                    break;
            }
        }
    }

    public void unKnowQr(){
        //信息
        messageDialog = new MessageDialog.Builder(this)
                .setMessage(R.string.unknow_qr)
                .create();
        messageDialog.show();

        new Handler().postDelayed(() -> {
            if(messageDialog != null){
                messageDialog.dismiss();

            }
        }, 3000); //延迟3秒消失
    }

    @Override
    public void onDeviceRefresh(AllBindDevices allBindDevices) {
        if (allBindDevices != null) {
            LogUtils.d("添加设备加入网关");
            List<HomeShowBean> gatewayList = mPresenter.getGatewayBindList();
            if (gatewayList != null) {
                if (gatewayList.size() > 0) {
                    flag = true;
                    if (gatewayList.size() == 1) {
                        HomeShowBean homeShowBean = gatewayList.get(0);
                        GatewayInfo gatewayInfo = (GatewayInfo) homeShowBean.getObject();
                        if (gatewayInfo.getServerInfo().getIsAdmin() == 1) {
                            isAdmin = 1;
                        } else {
                            isAdmin = 0;
                        }
                    }
                }
            }
        }
    }
}
