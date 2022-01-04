package com.philips.easykey.lock.mvp.presenter.tmall;



import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.tmall.IPhilipsTmallSelectDeviceView;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.postbean.TmallDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetStatisticsDayResult;
import com.philips.easykey.lock.publiclibrary.http.result.TmallDeviceListResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;

import io.reactivex.disposables.Disposable;

public class PhilipsTmallSelectDevicePresenter<T> extends BasePresenter<IPhilipsTmallSelectDeviceView> {

    public void aligenieUserlogin(String uid , TmallDeviceBean.Code code){
        XiaokaiNewServiceImp.aligenieUserlogin(uid,code)
                .subscribe(new BaseObserver<TmallDeviceListResult>() {
                    @Override
                    public void onSuccess(TmallDeviceListResult tmallDeviceListResult) {
                        if (isSafe()) {
                            mViewRef.get().aligenieUserloginSuccess(tmallDeviceListResult);
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        mViewRef.get().aligenieUserloginFailed(baseResult);
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        mViewRef.get().aligenieUserloginFailed(null);
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }
}
