package com.philips.easykey.lock.mvp.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.philips.easykey.lock.mvp.mvpbase.BasePresenter;
import com.philips.easykey.lock.mvp.view.IMessageView;
import com.philips.easykey.lock.mvp.view.IMyFragmentView;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.UserNickResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.utils.LogUtils;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;

public class MessageFragmentPresenter<T> extends BasePresenter<IMessageView> {


}
