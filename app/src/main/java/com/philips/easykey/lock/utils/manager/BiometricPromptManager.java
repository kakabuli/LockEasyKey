package com.philips.easykey.lock.utils.manager;

import android.app.Activity;
import android.os.Build;
import android.os.CancellationSignal;
import com.philips.easykey.lock.utils.BiometricUtils;
import com.philips.easykey.lock.utils.FingerprintUtils;


public class BiometricPromptManager {

    private static final String TAG = "BiometricPromptManager";
    private BiometricUtils biometricUtils;
    private FingerprintUtils fingerprintUtils;
    private CancellationSignal mCancellationSignal;//用于取消指纹识别
    public interface BiometricIdentifyCallbackLinstener {
        void onAuthenticationError(int errorCode, String errString);

        void onSucceeded();

        void onFailed();

        void onAuthenticationHelp(int code, String reason);

        void onCancel();

    }

    public static BiometricPromptManager from(Activity activity) {
        return new BiometricPromptManager(activity);
    }

    public BiometricPromptManager(Activity activity) {
        if (isAboveApi28()) {
            if(biometricUtils == null){
                biometricUtils = new BiometricUtils(activity);
            }
        } else if (isAboveApi23()) {
            if(fingerprintUtils == null){
                fingerprintUtils = new FingerprintUtils(activity);
            }
        }
    }

    private boolean isAboveApi28() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    private boolean isAboveApi23() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public boolean isBiometricPromptEnable() {
        if(isAboveApi28()){
           return biometricUtils.isBiometricPromptEnable();
        }else if(isAboveApi23()){
            return fingerprintUtils.isBiometricPromptEnable();
        }
        return false;
    }

    public void authenticate(BiometricPromptManager.BiometricIdentifyCallbackLinstener listener){
        if(mCancellationSignal == null){
            mCancellationSignal = new CancellationSignal();
        }
        if(isAboveApi28()){
            biometricUtils.authenticate(listener,mCancellationSignal);
        }else if(isAboveApi23()){
            fingerprintUtils.authenticate(listener,mCancellationSignal);
        }
    }

    public CancellationSignal getCancellationSignal(){
        return mCancellationSignal;
    }

}
