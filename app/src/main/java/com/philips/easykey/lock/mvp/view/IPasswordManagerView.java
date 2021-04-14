package com.philips.easykey.lock.mvp.view;


import com.philips.easykey.lock.mvp.mvpbase.IBleView;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;

import java.util.List;

/**
 * Create By lxj  on 2019/3/5
 * Describe
 */
public interface IPasswordManagerView extends IBleView {


    void onSyncPasswordSuccess(List<ForeverPassword> pwdList);

    void onSyncPasswordFailed(Throwable throwable);

    void onUpdate(List<ForeverPassword> pwdList);

    void startSync();

    void endSync();

    void onServerDataUpdate();

}
