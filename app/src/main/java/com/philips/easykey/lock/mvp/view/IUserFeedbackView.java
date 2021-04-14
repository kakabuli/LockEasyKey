package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;


/**
 * Create By lxj  on 2019/3/4
 * Describe
 */
public interface IUserFeedbackView extends IBaseView {

    void userFeedbackSubmitSuccess();

    void userFeedbackSubmitFailed(Throwable throwable);

    void userFeedbackSubmitFailedServer(BaseResult result);

}
