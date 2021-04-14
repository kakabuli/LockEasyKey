package com.philips.easykey.lock.mvp.view;

import android.graphics.Bitmap;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.UserNickResult;

/**
 * Create By David
 */
public interface IMyFragmentView extends IBaseView {


    /**
     * 下载的图片
     */
    void downloadPhoto(Bitmap bitmap);

    /**
     * 下载的图片失败
     */
    void downloadPhotoError(Throwable e);
    /**
     * 获取昵称成功
     * */
    void getNicknameSuccess(UserNickResult userNickResult);
    /**
     * 获取昵称失败
     * */
    void getNicknameFail(BaseResult baseResult);
    /**
     * 获取昵称异常
     * */
    void getNicknameError(Throwable throwable);
}
