package com.philips.easykey.lock.mvp.view;


import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;


public interface IFamilyMemberDeatilView extends IBaseView {
    /**
     * 删除普通用户列表成功
     */
    void deleteCommonUserListSuccess(BaseResult baseResult);

    /**
     * 删除普通用户列表失败
     */
    void deleteCommonUserListFail(BaseResult baseResult);

    /**
     * 删除普通用户列表异常
     */
    void deleteCommonUserListError(Throwable throwable);

    void modifyCommonUserNicknameSuccess(BaseResult baseResult);

    void modifyCommonUserNicknameFail(BaseResult baseResult);

    void modifyCommonUserNicknameError(Throwable throwable);
}
