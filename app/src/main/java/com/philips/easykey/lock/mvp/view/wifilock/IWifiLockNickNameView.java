package com.philips.easykey.lock.mvp.view.wifilock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

public interface IWifiLockNickNameView extends IBaseView {

    void onUpdateNickSuccess();

    void onUpdateNickFailed(Throwable throwable);

    void onUpdateNickFailedServer(BaseResult result);


}
