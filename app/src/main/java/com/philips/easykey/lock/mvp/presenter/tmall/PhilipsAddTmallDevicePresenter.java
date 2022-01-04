package com.philips.easykey.lock.mvp.presenter.tmall;





import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.tmall.IPhilipsAddTmallDeviceView;

import com.philips.easykey.lock.publiclibrary.bean.TmallDeviceListBean;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.TmallAddDeviceResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import io.reactivex.disposables.Disposable;

public class PhilipsAddTmallDevicePresenter<T> extends BasePresenter<IPhilipsAddTmallDeviceView> {

    public void aligenieUserDeviceShare(String uid ,  String nickName, TmallDeviceListBean tmallDeviceListBean){
        XiaokaiNewServiceImp.aligenieUserDeviceShare(uid,nickName,tmallDeviceListBean)
                .subscribe(new BaseObserver<TmallAddDeviceResult>() {
                    @Override
                    public void onSuccess(TmallAddDeviceResult tmallDeviceListResult) {
                        if (isSafe()) {
                            mViewRef.get().onAligenieUserDeviceShareSuccess();
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().onAligenieUserDeviceShareSuccess();
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }
}
