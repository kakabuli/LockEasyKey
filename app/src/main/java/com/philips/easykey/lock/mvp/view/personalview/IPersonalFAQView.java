package com.philips.easykey.lock.mvp.view.personalview;

import com.philips.easykey.lock.mvp.mvpbase.IBaseView;
import com.philips.easykey.lock.publiclibrary.http.result.GetFAQResult;


/**
 * 常见问题
 */
public interface IPersonalFAQView extends IBaseView {

    void getFAQSuccessListView(GetFAQResult baseResult, String s);

    void getFAQError(Throwable throwable);

    void getFAQFail(GetFAQResult baseResult);
}
