package com.philips.easykey.lock.mvp.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.mvp.view.ISystemSettingView;


import io.reactivex.disposables.Disposable;

/**
 * Create By lxj  on 2019/3/4
 * Describe
 */
public class SystemSettingPresenter<T> extends BasePresenter<ISystemSettingView> {

    public void getProtocolVersion() {

    }

    public void getProtocolContent() {

    }

}
