package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.FtpEnable;

/**
 * Create By denganzhi  on 2019/5/5
 * Describe
 */

public interface ISnapShotView extends IBaseView {


    void showFTPResultSuccess(FtpEnable ftpEnable);

    void showFTPResultFail();


    void showFTPOverTime();



}
