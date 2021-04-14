package com.philips.easykey.lock.publiclibrary.http.util;

import com.philips.easykey.lock.publiclibrary.http.temp.BaseResponse;

/**
 * Created by Administrator on 2018/6/13.
 */

public class OtherException  extends RuntimeException {
    private BaseResponse response;

    public OtherException(BaseResponse response){
        this.response = response;
    }

    public BaseResponse getResponse() {
        return response;
    }
}
