package com.philips.easykey.lock.mvp.view.tmall;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.CheckOTAResult;
import com.philips.easykey.lock.publiclibrary.http.result.TmallDeviceListResult;

public interface IPhilipsTmallSelectDeviceView extends IBaseView {

    void aligenieUserloginFailed(BaseResult baseResult);

    void aligenieUserloginSuccess(TmallDeviceListResult tmallDeviceListResult);
}
