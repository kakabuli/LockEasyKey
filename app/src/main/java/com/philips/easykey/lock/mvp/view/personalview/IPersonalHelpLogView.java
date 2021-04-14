package com.philips.easykey.lock.mvp.view.personalview;


import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetHelpLogResult;

public interface IPersonalHelpLogView extends IBaseView {

    void getHelpLogSuccess(GetHelpLogResult getHelpLogResult);

    void getHelpLogError(Throwable throwable);


    void getHelpLogFail(BaseResult baseResult);
}
