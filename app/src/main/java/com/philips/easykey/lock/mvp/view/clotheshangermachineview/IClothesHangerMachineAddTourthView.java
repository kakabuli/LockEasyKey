package com.philips.easykey.lock.mvp.view.clotheshangermachineview;

import android.bluetooth.BluetoothDevice;

import com.philips.easykey.lock.bean.BluetoothLockBroadcastBean;
import com.philips.easykey.lock.bean.BluetoothLockBroadcastListBean;
import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

import java.util.List;

public interface IClothesHangerMachineAddTourthView extends IBaseView {


    void onDeviceStateChange(boolean isConnected);

}
