package com.philips.easykey.lock.mvp.presenter.tmall;



import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.tmall.IPhilipsTmallDelDeviceView;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.TmallAddDeviceResult;
import com.philips.easykey.lock.publiclibrary.http.result.TmallQueryDeviceListResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;

import io.reactivex.disposables.Disposable;


public class PhilipsDelTmallDevicePresenter<T> extends BasePresenter<IPhilipsTmallDelDeviceView> {

    public void aligenieUserDeviceShareDel(TmallQueryDeviceListResult.TmallQueryDeviceList tmallQueryDeviceList){
        XiaokaiNewServiceImp.aligenieUserDeviceShareDel(tmallQueryDeviceList, MyApplication.getInstance().getUid())
                .subscribe(new BaseObserver<TmallAddDeviceResult>() {
                    @Override
                    public void onSuccess(TmallAddDeviceResult tmallAddDeviceResult) {
                        if (isSafe()) {
                            mViewRef.get().onDeviceShareDelSuccess();
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {
                            mViewRef.get().onDeviceShareDelFailed();
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().onDeviceShareDelFailed();
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }
}
