package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.LoginResult;


/**
 * Create By lxj  on 2019/1/9
 * Describe
 */
public interface ILoginView extends IBaseView {

    void onLoginSuccess();

    void onLoginFailed(Throwable e);

    void onLoginFailedServer(LoginResult result);



}
