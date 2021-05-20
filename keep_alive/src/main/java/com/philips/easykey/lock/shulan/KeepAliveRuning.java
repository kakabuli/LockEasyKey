package com.philips.easykey.lock.shulan;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.philips.easykey.lock.shulan.utils.KeepAliveUtils;

public class KeepAliveRuning implements IKeepAliveRuning {


    @Override
    public void onRuning(Context context) {
        if(BuildConfig.DEBUG)
        Log.d("runing?KeepAliveRuning", "true");

        ClassLoader classLoader = KeepAliveRuning.class.getClassLoader();
        Class<?> clz;
        try {
            clz = classLoader.loadClass("com.philips.easykey.lock.publiclibrary.xm.DoorbellingService");
            if(BuildConfig.DEBUG)
            Log.d("shulan" , clz.getName());
            Intent intent = new Intent(context, clz);
            if(!KeepAliveUtils.isServiceRunning(context,clz.getName()))
                context.startService(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onStop() {
        Log.d("runing?KeepAliveRuning", "false");
    }
}
