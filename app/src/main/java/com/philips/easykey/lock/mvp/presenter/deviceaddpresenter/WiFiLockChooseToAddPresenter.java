package com.philips.easykey.lock.mvp.presenter.deviceaddpresenter;

import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.WiFiLockChooseToAddView;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.WiFiLockUtils;
import io.reactivex.disposables.Disposable;


public class WiFiLockChooseToAddPresenter<T> extends BasePresenter<WiFiLockChooseToAddView> {

    private Disposable chooseToAddDisposable;

    public void searchLockProduct(String Product) {

        LogUtils.d("--kaadas--WiFiLockUtils.pairMode(Product)=="+WiFiLockUtils.pairMode(Product));

        //equalsIgnoreCase不区分大小写
        if (WiFiLockUtils.pairMode(Product).equals("WiFi")) {
        //searchLockProductSuccessForWiFi
            mViewRef.get().searchLockProductSuccessForWiFi("WiFi");
        }
        else if(WiFiLockUtils.pairMode(Product).equals("WiFi&BLE")){
        //searchLockProductSuccessForWiFiAndBLE
            mViewRef.get().searchLockProductSuccessForWiFiAndBLE("WiFi&BLE");
        }else if(WiFiLockUtils.pairMode(Product).equals("WiFi&VIDEO")){
            mViewRef.get().searchLockProductSuccessForWiFiAndVideo("WiFi&VIDEO");
        }
        else {
            //searchLockProductThrowable
            mViewRef.get().searchLockProductThrowable();
        }
    }
}
