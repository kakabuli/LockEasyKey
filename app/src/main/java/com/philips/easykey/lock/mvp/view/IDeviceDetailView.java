package com.philips.easykey.lock.mvp.view;


import com.philips.easykey.lock.mvp.mvpbase.IBleView;

/**
 * Create By lxj  on 2019/2/27
 * Describe
 */
public interface IDeviceDetailView extends IBleLockDetailView {


    void onElectricUpdata(Integer electric);

    void onElectricUpdataFailed(Throwable throwable);

    void onDeviceInfoLoaded();



}
