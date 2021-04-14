package com.philips.easykey.lock.mvp.view.personalview;


import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

public interface IPersonalUpdateNickNameView  extends IBaseView {

        void  updateNickNameSuccess(String nickName);

        void updateNickNameError(Throwable throwable);

        void updateNickNameFail(BaseResult baseResult);
}
