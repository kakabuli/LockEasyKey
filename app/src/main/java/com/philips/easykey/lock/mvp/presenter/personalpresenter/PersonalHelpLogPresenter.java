package com.philips.easykey.lock.mvp.presenter.personalpresenter;


import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetHelpLogResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.mvp.view.personalview.IPersonalHelpLogView;

import io.reactivex.disposables.Disposable;

public class PersonalHelpLogPresenter<T> extends BasePresenter<IPersonalHelpLogView> {

    //获取帮助日志
    public void getHelpLog(String uid, int page) {

        XiaokaiNewServiceImp.getHelpLog(uid, page).subscribe(new BaseObserver<GetHelpLogResult>() {
            @Override
            public void onSuccess(GetHelpLogResult getHelpLogResult) {
                if ("200".equals(getHelpLogResult.getCode())) {
                    if (isSafe()) {
                        mViewRef.get().getHelpLogSuccess(getHelpLogResult);
                    }
                }
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {
                if (isSafe()) {
                    mViewRef.get().getHelpLogFail(baseResult);
                }
            }

            @Override
            public void onFailed(Throwable throwable) {
                if (isSafe()) {
                    mViewRef.get().getHelpLogError(throwable);
                }
            }

            @Override
            public void onSubscribe1(Disposable d) {
                compositeDisposable.add(d);
            }
        });


    }

}
