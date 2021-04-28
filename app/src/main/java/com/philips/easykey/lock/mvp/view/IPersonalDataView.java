package com.philips.easykey.lock.mvp.view;

import android.graphics.Bitmap;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;

/**
 * Create By David
 */
public interface IPersonalDataView extends IBaseView {


    /**
     * 照片上传成功
     */
    void photoUploadSuccess();

    /**
     * 照片上传失败
     */
    void photoUploadFail(BaseResult baseResult);

    /**
     * 上传异常
     */
    void photoUploadError(Throwable throwable);

    /**
     * 下载的图片
     */
    void downloadPhoto(Bitmap bitmap);

    /**
     * 下载的图片失败
     */
    void downloadPhotoError(Throwable e);

    void onLoginOutSuccess();

    void onLoginOutFailed(Throwable throwable);

    void onLoginOutFailedServer(BaseResult result);

}
