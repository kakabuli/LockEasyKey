package com.philips.easykey.lock.shulan;

import android.content.Context;

public interface IKeepAliveRuning {
    void onRuning(Context context);
    void onStop();
}
