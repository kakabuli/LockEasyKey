package com.philips.easykey.lock.mvp.presenter.clotheshangermachinepresenter;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.fragment.help.PersonalFAQHangerHelpFragment;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.clotheshangermachineview.IClothesHangerMachineDetailView;
import com.philips.easykey.lock.mvp.view.clotheshangermachineview.IClothesHangerMachineNicknameView;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.postbean.MultiOTABean;
import com.philips.easykey.lock.publiclibrary.http.postbean.UpgradeMultiOTABean;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.ClothesHangerMachineUnBindResult;
import com.philips.easykey.lock.publiclibrary.http.result.MultiCheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ClothesHangerMachineNicknamePresenter<T> extends BasePresenter<IClothesHangerMachineNicknameView> {

    private Disposable settingNicknameDisposable;

    @Override
    public void attachView(IClothesHangerMachineNicknameView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    public void setNickname(String wifiSN, String trim) {
        XiaokaiNewServiceImp.hangerUpdateNickname(wifiSN,trim)
                .compose(RxjavaHelper.observeOnMainThread())
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult baseResult) {
                        if("200".equals(baseResult.getCode() + "")){
                            MyApplication.getInstance().getAllDevicesByMqtt(true);
                            if(isSafe()){
                                mViewRef.get().onSettingNicknameSuccess();
                            }
                        }else{
                            if(isSafe()){
                                mViewRef.get().onSettingNicknameFailed(baseResult);
                            }
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if(isSafe()){
                            mViewRef.get().onSettingNicknameFailed(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if(isSafe()){
                            mViewRef.get().onSettingNicknameThrowable(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {

                    }
                });
    }
}

