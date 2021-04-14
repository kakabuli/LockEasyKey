package com.philips.easykey.lock.mvp.presenter.deviceaddpresenter;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.IBindBleSuccessView;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;

import io.reactivex.disposables.Disposable;

/**
 * Create By lxj  on 2019/3/13
 * Describe
 */
public class BindBleSuccessPresenter<T> extends BasePresenter<IBindBleSuccessView> {


    public void modifyDeviceNickname(String devname, String user_id, String lockNickName) {
        XiaokaiNewServiceImp.modifyLockNick(devname, user_id, lockNickName).subscribe(new BaseObserver<BaseResult>() {
            @Override
            public void onSuccess(BaseResult baseResult) {
                if (isSafe()) {
                    mViewRef.get().modifyDeviceNicknameSuccess();
                }

                MyApplication.getInstance().getAllDevicesByMqtt(true);
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {
                if (isSafe()) {
                    mViewRef.get().modifyDeviceNicknameFail(baseResult);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (isSafe()) {
                    mViewRef.get().modifyDeviceNicknameError(throwable);
                }
            }

            @Override
            public void onSubscribe1(Disposable d) {
                compositeDisposable.add(d);
            }
        });
    }


}
