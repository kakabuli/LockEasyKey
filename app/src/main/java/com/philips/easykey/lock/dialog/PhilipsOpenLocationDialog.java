package com.philips.easykey.lock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.philips.easykey.lock.R;

/**
 * author : Jack
 * time   : 2021/5/10
 * E-mail : wengmaowei@kaadas.com
 * desc   : 开启定位
 */
public class PhilipsOpenLocationDialog extends Dialog {

    private OnOpenLocationListener mOnOpenLocationListener;

    public PhilipsOpenLocationDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.philips_dialog_open_location);
        findViewById(R.id.tvCancel).setOnClickListener(v -> {
            if(mOnOpenLocationListener != null) {
                mOnOpenLocationListener.cancel();
            }
        });
        findViewById(R.id.tvSetting).setOnClickListener(v -> {
            if(mOnOpenLocationListener != null) {
                mOnOpenLocationListener.setting();
            }
        });
    }

    public void setOnOpenLocationListener(OnOpenLocationListener onOpenLocationListener) {
        mOnOpenLocationListener = onOpenLocationListener;
    }

    public interface OnOpenLocationListener {
        void setting();
        void cancel();
    }

}
