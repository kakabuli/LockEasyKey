package com.philips.easykey.lock.mvp.view;


import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;


public interface IRegisterView extends IBaseView {
    void sendRandomSuccess();

    void registerSuccess();


    void sendRandomFailed(Throwable e);
    void sendRandomFailedServer(BaseResult result);

    void registerFailed(Throwable e);
    void registerFailedServer(BaseResult result);
}
