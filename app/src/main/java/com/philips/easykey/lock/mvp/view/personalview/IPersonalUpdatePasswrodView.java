package com.philips.easykey.lock.mvp.view.personalview;


import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

public interface IPersonalUpdatePasswrodView  extends IBaseView {

    void  updatePwdSuccess(String newPwd);

    void updatePwdError(Throwable throwable);


    void updatePwdFail(BaseResult baseResult);
}
