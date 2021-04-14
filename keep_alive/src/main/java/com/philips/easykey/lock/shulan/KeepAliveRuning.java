package com.philips.easykey.lock.shulan;


import android.content.Context;
import android.content.Intent;

import com.philips.easykey.lock.shulan.utils.KeepAliveUtils;
import com.philips.easykey.lock.shulan.utils.LogUtils;

public class KeepAliveRuning implements IKeepAliveRuning {


    @Override
    public void onRuning(Context context) {

        LogUtils.e("runing?KeepAliveRuning", "true");

        ClassLoader classLoader = KeepAliveRuning.class.getClassLoader();
        Class<?> clz;
        try {
            clz = classLoader.loadClass("com.philips.easykey.lock.publiclibrary.xm.DoorbellingService");
            LogUtils.e("shulan 0000000 " + clz.getName());
            Intent intent = new Intent(context, clz);
            if(!KeepAliveUtils.isServiceRunning(context,clz.getName()))
                context.startService(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStop() {
        LogUtils.e("runing?KeepAliveRuning", "false");
    }
}
