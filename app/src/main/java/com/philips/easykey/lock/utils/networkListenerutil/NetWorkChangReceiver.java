package com.philips.easykey.lock.utils.networkListenerutil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.publiclibrary.bean.GatewayInfo;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.NotifyRefreshActivity;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class NetWorkChangReceiver extends BroadcastReceiver {


    public static NotifyRefreshActivity notifityActivity;

    public static void setNotifyRefreshActivity(NotifyRefreshActivity notifyRefreshActivity) {
        notifityActivity = notifyRefreshActivity;
    }

    private static PublishSubject<Boolean> networkChangeObversable = PublishSubject.create();

    public static Observable<Boolean> notifyNetworkChange(){
        return networkChangeObversable;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
            if (!NetUtil.isNetworkAvailable()){
                //把所有网关的状态设置为离线
                List<GatewayInfo> gatewayInfos= MyApplication.getInstance().getAllGateway();
                if (gatewayInfos!=null&&gatewayInfos.size()>0){
                    for (GatewayInfo gatewayInfo:gatewayInfos){
                        gatewayInfo.setEvent_str("offline");
                    }
                }
                networkChangeObversable.onNext(true);
            }else {
                networkChangeObversable.onNext(false);
            }

    }
}


