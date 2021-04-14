package com.philips.easykey.lock.mvp.presenter;


import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.RegisterResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.MMKVUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.mvp.view.IRegisterView;


import io.reactivex.disposables.Disposable;

public class RegisterPresenter<T> extends BasePresenter<IRegisterView> {

    public void sendRandomByPhone(String phone, String code) {
        XiaokaiNewServiceImp.sendMessage(phone, code)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        LogUtils.d("发送验证码成功  " + result.getMsg());
                        if (mViewRef != null) {
                            mViewRef.get().sendRandomSuccess();
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (mViewRef != null) {
                            mViewRef.get().sendRandomFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (mViewRef != null) {
                            mViewRef.get().sendRandomFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }

    public void sendRandomByEmail(String email) {
        XiaokaiNewServiceImp.sendEmailYZM(email)
                .subscribe(new BaseObserver<BaseResult>() {
                    @Override
                    public void onSuccess(BaseResult result) {
                        LogUtils.d("发送验证码成功  " + result.getMsg());
                        if (mViewRef != null) {
                            mViewRef.get().sendRandomSuccess();
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (mViewRef != null) {
                            mViewRef.get().sendRandomFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (mViewRef != null) {
                            mViewRef.get().sendRandomFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }


    public void registerByPhone(String phone, String pwd, String random) {
        XiaokaiNewServiceImp.registerByPhone(phone, pwd, random)
                .subscribe(
                        new BaseObserver<RegisterResult>() {
                            @Override
                            public void onSuccess(RegisterResult result) {
                                LogUtils.d("注册成功  " + result.getData().toString());
                                MMKVUtils.setMMKV(SPUtils.TOKEN,result.getData().getToken());
                                MMKVUtils.setMMKV(SPUtils.UID,result.getData().getUid());
//                                SPUtils.put(SPUtils.TOKEN, result.getData().getToken());
//                                SPUtils.put(SPUtils.UID, result.getData().getUid());
                                MyApplication.getInstance().setToken(result.getData().getToken());
                                MyApplication.getInstance().setUid(result.getData().getUid());

                                MMKVUtils.setMMKV(SPUtils.STORE_TOKEN,result.getData().getStoreToken());
                                if (mViewRef != null) {
                                    mViewRef.get().registerSuccess();
                                }
                            }

                            @Override
                            public void onAckErrorCode(BaseResult baseResult) {
                                if (mViewRef != null) {
                                    mViewRef.get().registerFailedServer(baseResult);
                                }
                            }

                            @Override
                            public void onFailed(Throwable throwable) {
                                if (mViewRef != null) {
                                    mViewRef.get().registerFailed(throwable);
                                }
                            }

                            @Override
                            public void onSubscribe1(Disposable d) {
                                compositeDisposable.add(d);
                            }
                        }
                );

    }

    public void registerByEmail(String phone, String pwd, String random) {
        XiaokaiNewServiceImp.registerByEmail(phone, pwd, random)
                .subscribe(
                        new BaseObserver<RegisterResult>() {
                            @Override
                            public void onSuccess(RegisterResult result) {
                                LogUtils.d("注册成功  " + result.getData().toString());
//                                SPUtils.put(SPUtils.TOKEN, result.getData().getToken());
//                                SPUtils.put(SPUtils.UID, result.getData().getUid());
                                MMKVUtils.setMMKV(SPUtils.TOKEN,result.getData().getToken());
                                MMKVUtils.setMMKV(SPUtils.UID,result.getData().getUid());
                                MyApplication.getInstance().setToken(result.getData().getToken());
                                MyApplication.getInstance().setUid(result.getData().getUid());
                                MMKVUtils.setMMKV(SPUtils.STORE_TOKEN,result.getData().getStoreToken());
                                if (mViewRef != null) {
                                    mViewRef.get().registerSuccess();
                                }
                            }

                            @Override
                            public void onAckErrorCode(BaseResult baseResult) {
                                if (mViewRef != null) {
                                    mViewRef.get().registerFailedServer(baseResult);
                                }
                            }

                            @Override
                            public void onFailed(Throwable throwable) {
                                if (mViewRef != null) {
                                    mViewRef.get().registerFailed(throwable);
                                }
                            }

                            @Override
                            public void onSubscribe1(Disposable d) {
                                compositeDisposable.add(d);
                            }
                        }
                );

    }

}
