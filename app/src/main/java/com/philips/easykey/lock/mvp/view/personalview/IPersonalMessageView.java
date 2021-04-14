package com.philips.easykey.lock.mvp.view.personalview;


import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetMessageResult;

public interface IPersonalMessageView extends IBaseView {

    //获取消息列表成功
    void getMessageSuccess(GetMessageResult getMessageResult);
    void getMessageFail(GetMessageResult getMessageResult);
    //获取消息列表失败
    void getMessageError(Throwable e);

    //删除成功
    void deleteSuccess(int position);

    //删除失败
    void deleteError(Throwable throwable);

    void deleteFail(BaseResult baseResult);
}
