package com.philips.easykey.lock.mvp.view.deviceaddview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

/**
 * Create By lxj  on 2019/3/13
 * Describe
 */
public interface IBindBleSuccessView extends IBaseView {

    /**
     * 修改昵称成功
     */
    void modifyDeviceNicknameSuccess();

    /**
     * 修改昵称异常
     */
    void modifyDeviceNicknameError(Throwable throwable);

    /**
     * 修改昵称失败
     */
    void modifyDeviceNicknameFail(BaseResult baseResult);


}
