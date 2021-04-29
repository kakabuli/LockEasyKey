package com.philips.easykey.lock.mvp.presenter.wifilock;

import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.bean.WiFiLockCardAndFingerShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockPasswordManagerView;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;
import com.philips.easykey.lock.publiclibrary.bean.FacePassword;
import com.philips.easykey.lock.publiclibrary.bean.WiFiLockPassword;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockGetPasswordListResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class WifiLockPasswordManagerPresenter<T> extends BasePresenter<IWifiLockPasswordManagerView> {

    public void getPasswordList(String wifiSn) {
        XiaokaiNewServiceImp.wifiLockGetPwdList(wifiSn, MyApplication.getInstance().getUid())
                .subscribe(new BaseObserver<WifiLockGetPasswordListResult>() {
                    @Override
                    public void onSuccess(WifiLockGetPasswordListResult wifiLockGetPasswordListResult) {

                        WiFiLockPassword wiFiLockPassword = wifiLockGetPasswordListResult.getData();
                        String object = new Gson().toJson(wiFiLockPassword);
                        LogUtils.d("服务器数据是   " + object);

                        SPUtils.put(KeyConstants.WIFI_LOCK_PASSWORD_LIST + wifiSn, object);
                        if (isSafe()) {
                            mViewRef.get().onGetPasswordSuccess(wiFiLockPassword);
                        }
                    }

                    @Override
                    public void onAckErrorCode(BaseResult baseResult) {
                        if (isSafe()) {
                            mViewRef.get().onGetPasswordFailedServer(baseResult);
                        }
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        if (isSafe()) {
                            mViewRef.get().onGetPasswordFailed(throwable);
                        }
                    }

                    @Override
                    public void onSubscribe1(Disposable d) {
                        compositeDisposable.add(d);
                    }
                });
    }


    public List<ForeverPassword> getShowPasswords(WiFiLockPassword wiFiLockPassword) {
        List<ForeverPassword> passwords = new ArrayList<>();
        if (wiFiLockPassword != null) {
            List<WiFiLockPassword.PwdListBean> pwdList = wiFiLockPassword.getPwdList();
            List<WiFiLockPassword.PwdNicknameBean> pwdNickname = wiFiLockPassword.getPwdNickname();
            if (pwdList != null) {
                //String num, String nickName, long createTime, int type, long startTime, long endTime, List<String> items
                for (WiFiLockPassword.PwdListBean password : pwdList) {
                    int num = password.getNum();
                    String sNum = num > 9 ? "" + num : "0" + num;
                    ForeverPassword foreverPassword = new ForeverPassword(sNum, sNum,
                            password.getCreateTime(), password.getType(), password.getStartTime(), password.getEndTime(), password.getItems());
                    if (pwdNickname != null) {
                        for (WiFiLockPassword.PwdNicknameBean nickname : pwdNickname) {
                            if (nickname.getNum() == num) {
                                foreverPassword.setNickName(nickname.getNickName());
                            }
                        }
                    }
//                    if (num > 4 && num < 9) {
//                        foreverPassword.setType(5);
//                    }
                    passwords.add(foreverPassword);
                }
            }
        }
        return passwords;
    }


    public List<WiFiLockCardAndFingerShowBean> getShowCardsFingers(WiFiLockPassword wiFiLockPassword, int type) {
        List<WiFiLockCardAndFingerShowBean> passwords = new ArrayList<>();
        if (wiFiLockPassword != null) {
            switch (type) {
                case 2: //指纹
                    List<WiFiLockPassword.FingerprintListBean> fingerprintList = wiFiLockPassword.getFingerprintList();
                    List<WiFiLockPassword.FingerprintNicknameBean> fingerprintNickname = wiFiLockPassword.getFingerprintNickname();
                    if (fingerprintList != null) {
                        for (WiFiLockPassword.FingerprintListBean password : fingerprintList) {
                            int num = password.getNum();
                            String sNick = num > 9 ? "" + num : "0" + num;
                            WiFiLockCardAndFingerShowBean fingerShowBean = new WiFiLockCardAndFingerShowBean(num, password.getCreateTime(), sNick, BleLockUtils.TYPE_FINGER);
                            if (fingerprintNickname != null) {
                                for (WiFiLockPassword.FingerprintNicknameBean nickname : fingerprintNickname) {
                                    if (nickname.getNum() == num) {
                                        fingerShowBean.setNickName(nickname.getNickName());
                                    }
                                }
                            }
                            passwords.add(fingerShowBean);
                        }
                    }
                    break;
                case 3:  //卡片
                    List<WiFiLockPassword.CardListBean> cardList = wiFiLockPassword.getCardList();
                    List<WiFiLockPassword.CardNicknameBean> cardNickname = wiFiLockPassword.getCardNickname();
//                    LogUtils.d("服务器数量是  " + cardList.size());
                    if (cardList != null) {
                        for (WiFiLockPassword.CardListBean password : cardList) {
                            int num = password.getNum();
                            String sNick = num > 9 ? "" + num : "0" + num;
                            WiFiLockCardAndFingerShowBean fingerShowBean = new WiFiLockCardAndFingerShowBean(num, password.getCreateTime(), sNick,BleLockUtils.TYPE_CARD);
                            if (cardNickname != null) {
                                for (WiFiLockPassword.CardNicknameBean nickname : cardNickname) {
                                    if (nickname.getNum() == num) {
                                        fingerShowBean.setNickName(nickname.getNickName());
                                    }
                                }
                            }
                            passwords.add(fingerShowBean);
                        }
                    }
                    break;
            }
        }
//        LogUtils.d("服务器数量是  " + passwords.size());
        return passwords;
    }

    public List<FacePassword> getShowFacePasswords(WiFiLockPassword wiFiLockPassword) {
        List<FacePassword> passwords = new ArrayList<>();
        if (wiFiLockPassword != null) {
            List<WiFiLockPassword.FaceListBean> faceList = wiFiLockPassword.getFaceList();
            List<WiFiLockPassword.FaceNicknameBean> faceNickname = wiFiLockPassword.getFaceNickname();

            if (faceList != null) {
                //String num, String nickName, long createTime, int type, long startTime, long endTime, List<String> items
                for (WiFiLockPassword.FaceListBean password : faceList) {
                    int num = password.getNum();

                    String sNum = num > 9 ? "" + num : "0" + num;
                    FacePassword facePassword = new FacePassword(sNum, sNum,
                            password.getCreateTime(), password.getType(), password.getStartTime(), password.getEndTime(), password.getItems());
                    LogUtils.d("服务器数据是==faceNickname="+faceNickname);
                    if (faceNickname != null) {
                        for (WiFiLockPassword.FaceNicknameBean nickname : faceNickname) {
                            if (nickname.getNum() == num) {
                                facePassword.setNickName(nickname.getNickName());
                            }
                        }
                    }
                    passwords.add(facePassword);
                }
            }
        }
        return passwords;
    }

}
