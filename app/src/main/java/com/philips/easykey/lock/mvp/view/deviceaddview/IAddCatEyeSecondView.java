package com.philips.easykey.lock.mvp.view.deviceaddview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;

public interface IAddCatEyeSecondView extends IBaseView {
    void allowCatEyeJoinSuccess();

    void allowCatEyeJoinFailed(Throwable throwable);
}
