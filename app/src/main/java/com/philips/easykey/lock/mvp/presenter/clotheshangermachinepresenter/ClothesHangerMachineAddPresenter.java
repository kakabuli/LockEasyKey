package com.philips.easykey.lock.mvp.presenter.clotheshangermachinepresenter;

import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.clotheshangermachineview.IClothesHangerMachineAddView;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.clothesHangerMachineUtil.ClothesHangerMachineUtil;

import io.reactivex.disposables.Disposable;

public class ClothesHangerMachineAddPresenter<T> extends BasePresenter<IClothesHangerMachineAddView> {

    private Disposable chooseToAddDisposable;

    public void searchClothesMachine(String Product) {

        LogUtils.e("--kaadas--ClothesMachineUtil.pairMode(Product)=="+ ClothesHangerMachineUtil.pairMode(Product));

        //equalsIgnoreCase不区分大小写
        if (ClothesHangerMachineUtil.pairMode(Product).equals("WiFi")) {
            mViewRef.get().searchClothesMachineSuccessForWiFi("WiFi");
        }else if(ClothesHangerMachineUtil.pairMode(Product).equals("WiFi&BLE")){
            mViewRef.get().searchClothesMachineSuccessForWiFiAndBLE("WiFi&BLE");
        }else {
            mViewRef.get().searchClothesMachineThrowable();
        }
    }
}
