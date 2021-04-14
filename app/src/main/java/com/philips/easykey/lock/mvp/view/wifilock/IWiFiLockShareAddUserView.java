package com.philips.easykey.lock.mvp.view.wifilock;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

public interface IWiFiLockShareAddUserView extends IBaseView {

    /**
     * 添加用户成功
     */
    void onAddUserSuccess();

    /**
     * 添加用户失败
     */
    void onAddUserFailed(BaseResult baseResult);
    /**
     * 添加用户异常
     */
    void onAddUserFailedServer(Throwable throwable);

}
