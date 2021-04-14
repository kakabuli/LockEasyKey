package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;


public interface IResetPasswordView extends IBaseView {
    void senRandomSuccess();

    void resetPasswordSuccess();


    void sendRandomFailed(Throwable e);

    void resetPasswordFailed(Throwable e);

    void sendRandomFailedServer(BaseResult result);

    void resetPasswordFailedServer(BaseResult result);
}
