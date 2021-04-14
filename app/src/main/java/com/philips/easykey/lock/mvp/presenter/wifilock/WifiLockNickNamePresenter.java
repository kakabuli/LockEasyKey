package com.philips.easykey.lock.mvp.presenter.wifilock;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockNickNameView;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;

import io.reactivex.disposables.Disposable;

public class WifiLockNickNamePresenter<T> extends BasePresenter<IWifiLockNickNameView> {

    /**
     *
     * @param wifiSN
     * @param pwdType  密钥类型：1密码 2指纹密码 3卡片密码 4面容识别
     * @param num
     * @param nickName
     */
    public void updateNickName( String wifiSN, int pwdType, int num, String nickName,String userNickName){
        XiaokaiNewServiceImp.wifiLockUpdatePwdNickName(MyApplication.getInstance().getUid(),wifiSN, pwdType, num, nickName,userNickName)
            .subscribe(new BaseObserver<BaseResult>() {
                @Override
                public void onSuccess(BaseResult baseResult) {
                    if (isSafe()){
                        mViewRef.get().onUpdateNickSuccess();
                    }
                }

                @Override
                public void onAckErrorCode(BaseResult baseResult) {
                    if (isSafe()){
                        mViewRef.get().onUpdateNickFailedServer(baseResult);
                    }
                }

                @Override
                public void onFailed(Throwable throwable) {
                    if (isSafe()){
                        mViewRef.get().onUpdateNickFailed(throwable);
                    }
                }

                @Override
                public void onSubscribe1(Disposable d) {
                    compositeDisposable.add(d);
                }
            })

        ;
    }



}
