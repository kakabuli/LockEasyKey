package com.philips.easykey.lock.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.RequiresApi;
import com.philips.easykey.lock.utils.manager.BiometricPromptManager;

public class FingerprintUtils {

    private final String mAlias = "touch_id_key";//用于获取加密key
    private Activity mActivity;
    private BiometricPromptManager.BiometricIdentifyCallbackLinstener mListener;
    public  FingerprintUtils(Activity activity){
        this.mActivity = activity;
    }

    public boolean isBiometricPromptEnable() {
        return isHardwareDetected()
                && hasEnrolledFingerprints();
    }

    public boolean isHardwareDetected() {
        final FingerprintManager fm = getFingerprintManagerOrNull();
        return fm != null && fm.isHardwareDetected();
    }

    public boolean hasEnrolledFingerprints() {
        final FingerprintManager manager = getFingerprintManagerOrNull();
        return manager != null && manager.hasEnrolledFingerprints();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public FingerprintManager getFingerprintManagerOrNull() {
        if (mActivity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            return mActivity.getSystemService(FingerprintManager.class);
        } else {
            return null;
        }
    }

    public void authenticate(BiometricPromptManager.BiometricIdentifyCallbackLinstener listener,CancellationSignal mCancellationSignal) {
        this.mListener = listener;
        if(getFingerprintManagerOrNull() != null) {
            getFingerprintManagerOrNull().authenticate(new
                            FingerprintManager.CryptoObject(EncryUtils.getInstance().getCipher(mAlias))
                    , mCancellationSignal, 0, fingerprintManagercallback, null);
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private FingerprintManager.AuthenticationCallback fingerprintManagercallback = new FingerprintManager.AuthenticationCallback() {
        @Override
        public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
            mListener.onSucceeded();
        }

        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            mListener.onAuthenticationError(errorCode,errString.toString());
        }

        @Override
        public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
            mListener.onAuthenticationHelp(helpCode,helpString.toString());
        }

        @Override
        public void onAuthenticationFailed() {
            mListener.onFailed();
        }
    };
}
