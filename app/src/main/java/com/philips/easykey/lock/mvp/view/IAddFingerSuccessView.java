package com.philips.easykey.lock.mvp.view;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;


/**
 * Create By lxj  on 2019/3/8
 * Describe
 */
public interface IAddFingerSuccessView extends IBaseView {

    void onUploadSuccess();

    void onUploadFailed(Throwable throwable);

    void onUploadFailedServer(BaseResult result);


}
