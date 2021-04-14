package com.philips.easykey.lock.mvp.view.personalview;

import android.graphics.Bitmap;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface IPersonalVerifyFingerPrintView extends IBaseView {

    void downloadPhoto(Bitmap bitmap);

    void downloadPhotoError(Throwable e);
}
