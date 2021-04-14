package com.philips.easykey.lock.mvp.presenter;

import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.mvp.view.IUserFeedbackView;

import io.reactivex.disposables.Disposable;

/**
 * Create By lxj  on 2019/3/4
 * Describe
 */
public class UserFeedbackPresenter<T> extends BasePresenter<IUserFeedbackView> {

    public void userFeedback(String uid, String suggest) {
        XiaokaiNewServiceImp.putMessage(uid, suggest)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        if (isSafe()) {
                            mViewRef.get().userFeedbackSubmitSuccess();
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {
                            mViewRef.get().userFeedbackSubmitFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().userFeedbackSubmitFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }


    public void getProtocolVersion() {

    }

    public void getProtocolContent() {

    }

}
