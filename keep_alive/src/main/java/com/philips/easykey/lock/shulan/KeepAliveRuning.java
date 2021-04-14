package com.philips.easykey.lock.shulan;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.philips.easykey.lock.shulan.utils.KeepAliveUtils;

public class KeepAliveRuning implements IKeepAliveRuning {


    @Override
    public void onRuning(Context context) {

        Log.d("runing?KeepAliveRuning", "true");

        ClassLoader classLoader = KeepAliveRuning.class.getClassLoader();
        Class<?> clz;
        try {
            clz = classLoader.loadClass("com.philips.easykey.lock.publiclibrary.xm.DoorbellingService");
            Log.d("shula" , clz.getName());
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
