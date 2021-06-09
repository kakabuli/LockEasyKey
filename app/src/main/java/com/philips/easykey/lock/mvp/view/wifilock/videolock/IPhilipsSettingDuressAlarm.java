package com.philips.easykey.lock.mvp.view.wifilock.videolock;


import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

public interface IPhilipsSettingDuressAlarm extends IBaseView {

    void onSettingDuressAccount(BaseResult baseResult);

    void onSettingDuress(BaseResult baseResult);
}
