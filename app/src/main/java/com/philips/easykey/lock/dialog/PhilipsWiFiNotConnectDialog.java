package com.philips.easykey.lock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.philips.easykey.lock.R;

/**
 * author : Jack
 * time   : 2021/5/10
 * E-mail : wengmaowei@kaadas.com
 * desc   : Wifi未连接
 */
public class PhilipsWiFiNotConnectDialog extends Dialog {

    private OnWifiNotConnectListener mOnWifiNotConnectListener;

    public PhilipsWiFiNotConnectDialog(@NonNull Context context) {
        super(context, R.style.CustomDialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_dialog_wifi_not_connect);
        findViewById(R.id.tvConnect).setOnClickListener(v -> {
            if(mOnWifiNotConnectListener != null) {
                mOnWifiNotConnectListener.gotoConnect();
            }
        });
        findViewById(R.id.tvCancel).setOnClickListener(v -> {
            if(mOnWifiNotConnectListener != null) {
                mOnWifiNotConnectListener.cancel();
            }
        });
    }

    public void setOnWifiNotConnectListener(OnWifiNotConnectListener onWifiNotConnectListener) {
        mOnWifiNotConnectListener = onWifiNotConnectListener;
    }

    public interface OnWifiNotConnectListener {
        void cancel();
        void gotoConnect();
    }

}
