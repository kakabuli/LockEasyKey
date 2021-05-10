package com.philips.easykey.lock.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.CancellationSignal;
import android.hardware.biometrics.BiometricPrompt;

import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.utils.manager.BiometricPromptManager;


public class BiometricUtils {

    private final String mAlias = "touch_id_key";//用于获取加密key
    private BiometricPromptManager.BiometricIdentifyCallbackLinstener mListener;

    private Activity mActivity;
    public  BiometricUtils(Activity activity){
        this.mActivity = activity;
    }

    public boolean isBiometricPromptEnable() {
        return isHardwareDetected();
    }

    public boolean isHardwareDetected() {
        BiometricManager biometricManager = BiometricManager.from(mActivity);
        return  biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;
    }



    @RequiresApi(api = Build.VERSION_CODES.P)
    public void authenticate(BiometricPromptManager.BiometricIdentifyCallbackLinstener listener ,CancellationSignal mCancellationSignal) {
        this.mListener = listener;
        BiometricPrompt  mBiometricPrompt = new BiometricPrompt
                .Builder(mActivity)
                .setTitle(mActivity.getResources().getString(R.string.philips_biometric_login))
                .setSubtitle(mActivity.getResources().getString(R.string.philips_login_using_your_biometric_credential))
                .setNegativeButton(mActivity.getResources().getString(R.string.philips_cancel),
                mActivity.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.onCancel();
                    }
                })
                .build();
        mCancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() {
            @Override
            public void onCancel() {
                mListener.onCancel();
            }
        });
        mBiometricPrompt.authenticate(new BiometricPrompt.CryptoObject(EncryUtils.getInstance().getCipher(mAlias)),mCancellationSignal, mActivity.getMainExecutor(), biometricPromptcallback);
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private BiometricPrompt.AuthenticationCallback biometricPromptcallback = new BiometricPrompt.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            mListener.onAuthenticationError(errorCode,errString.toString());
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            super.onAuthenticationHelp(helpCode, helpString);
            mListener.onAuthenticationHelp(helpCode,helpString.toString());
        }

        @Override
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            mListener.onSucceeded();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            mListener.onFailed();
        }
    };
}
