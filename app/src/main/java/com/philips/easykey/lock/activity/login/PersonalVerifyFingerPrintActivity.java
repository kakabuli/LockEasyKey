package com.philips.easykey.lock.activity.login;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.personalpresenter.PersonalFingerPrintPresenter;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BitmapUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StorageUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.mvp.view.personalview.IPersonalVerifyFingerPrintView;
import com.philips.easykey.lock.utils.cachefloder.ACache;
import com.philips.easykey.lock.utils.cachefloder.CacheFloder;
import com.philips.easykey.lock.utils.manager.BiometricPromptManager;
import com.philips.easykey.lock.widget.BottomMenuDialog;
import com.philips.easykey.lock.widget.CircleImageView;

/**
 * 指纹验证解锁页面 从启动页跳转
 */
public class PersonalVerifyFingerPrintActivity extends BaseActivity<IPersonalVerifyFingerPrintView, PersonalFingerPrintPresenter<IPersonalVerifyFingerPrintView>> implements IPersonalVerifyFingerPrintView {


    LinearLayout fingerClick;
    TextView fingerMore;
    CircleImageView fingerImage;
    ImageView fingeprintImg;

    private BottomMenuDialog.Builder dialogBuilder;
    private BottomMenuDialog bottomMenuDialog;
    private Context mContext;
    private Bitmap changeBitmap;

    private BiometricPromptManager mBiometricPromptManager;
    private TranslateAnimation translateAnimation;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_fingerprint_verify);
        mContext = this;

        fingerClick = findViewById(R.id.finger_click);
        fingerMore = findViewById(R.id.finger_more);
        fingerImage = findViewById(R.id.finger_image);
        fingeprintImg = findViewById(R.id.fingeprint_img);

        fingerClick.setOnClickListener(v -> {
            //已经打开
            View mView = LayoutInflater.from(this).inflate(R.layout.personal_fingerprint_security, null);
            TextView mFingerCancel = mView.findViewById(R.id.finger_cancel);
            AlertDialog alertDialog = AlertDialogUtil.getInstance().common(this, mView);
            mFingerCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
            initTouchId();
        });
        fingerMore.setOnClickListener(v -> showMoreDialog());

        initView();
        initData();
    }

    private void initData() {
        initTouchId();
    }

    private void initTouchId() {
        if(mBiometricPromptManager == null) {
            mBiometricPromptManager = new BiometricPromptManager(PersonalVerifyFingerPrintActivity.this);
        }
        if(mBiometricPromptManager.isBiometricPromptEnable()){
            mBiometricPromptManager.authenticate(new BiometricPromptManager.BiometricIdentifyCallbackLinstener() {
                @Override
                public void onAuthenticationError(int errorCode, String errString) {
                    LogUtils.d("指纹识别码错误"+errorCode);
                    if (alertDialog != null){
                        alertDialog.dismiss();
                    }
                    if (errorCode == 7){
                        AlertDialogUtil.getInstance().noEditSingleButtonDialog(mContext, getString(R.string.app_name), getString(R.string.touch_id_call_limited), getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                            @Override
                            public void left() {

                            }

                            @Override
                            public void right() {

                            }
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {

                            }

                            @Override
                            public void afterTextChanged(String toString) {

                            }
                        });
                    }
                }

                @Override
                public void onSucceeded() {
                    if (alertDialog!=null){
                        alertDialog.dismiss();
                    }
                    Intent successIntent = new Intent(mContext, MainActivity.class);
                    startActivity(successIntent);
                    ToastUtils.showShort(getString(R.string.fingerprint_success));
                    finish();
                }

                @Override
                public void onFailed() {
                    //指纹验证失败，指纹识别失败，可再验，该指纹不是系统录入的指纹。
                    if (alertDialog!=null){
                        alertDialog.dismiss();
                    }

                    if (translateAnimation != null && fingeprintImg != null) {
                        fingeprintImg.startAnimation(translateAnimation);
                    }
                    ToastUtils.showShort(getString(R.string.fingerprint_unidentifiable));
                }

                @Override
                public void onAuthenticationHelp(int code, String reason) {
                    //指纹验证失败，可再验，可能手指过脏，或者移动过快等原因。
                    if (alertDialog!=null){
                        alertDialog.dismiss();
                    }
                    if (translateAnimation != null && fingeprintImg != null) {
                        fingeprintImg.startAnimation(translateAnimation);
                    }
                    ToastUtils.showShort(R.string.fingerprint_fail_check);
                }

                @Override
                public void onCancel() {
                    if (alertDialog!=null){
                        alertDialog.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected PersonalFingerPrintPresenter<IPersonalVerifyFingerPrintView> createPresent() {
        return new PersonalFingerPrintPresenter<>();
    }


    private void initView() {
        String photoPath = (String) SPUtils.get(KeyConstants.HEAD_PATH, "");
        if ("".equals(photoPath)) {
            mPresenter.downloadPicture(MyApplication.getInstance().getUid());
        } else {
            showImage(photoPath);
        }
        setImageAnimation();
        initDialog();
    }

    private void initDialog() {
        LogUtils.d("显示对话框");
        if (alertDialog!=null&&!alertDialog.isShowing()){
            alertDialog.show();
        }else {
            View mView = LayoutInflater.from(this).inflate(R.layout.personal_fingerprint_security, null);
            TextView mFingerCancel = mView.findViewById(R.id.finger_cancel);
            alertDialog = AlertDialogUtil.getInstance().common(this, mView);
            alertDialog.setCancelable(false);
            mFingerCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });
        }
    }


    private void setImageAnimation() {
        //摇摆
        translateAnimation = new TranslateAnimation(0f, 20f, 0, 0);
        translateAnimation.setDuration(100);
        fingeprintImg.setAnimation(translateAnimation);

    }


    //展示头像对话框
    private void showMoreDialog() {
        dialogBuilder = new BottomMenuDialog.Builder(this);
        String code = CacheFloder.readHandPassword(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "handPassword");
       //手势密码
        if (!TextUtils.isEmpty(code)) {
            dialogBuilder.addMenu(R.string.hand_pwd, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent loginIntent = new Intent(mContext, PersonalVerifyGesturePasswordActivity.class);
                    loginIntent.putExtra(KeyConstants.SOURCE,"WelcomeActivity");
                    startActivity(loginIntent);
                    if (bottomMenuDialog != null) {
                        bottomMenuDialog.dismiss();
                        if (mBiometricPromptManager.getCancellationSignal() != null){
                            mBiometricPromptManager.getCancellationSignal().cancel();
                        }
                        finish();
                    }
                }
            });
        }
        //密码登录
        dialogBuilder.addMenu(R.string.pwd_select, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(mContext, PhilipsLoginActivity.class);
                startActivity(loginIntent);
                if (bottomMenuDialog != null) {
                    bottomMenuDialog.dismiss();
                    if (mBiometricPromptManager.getCancellationSignal() != null){
                        mBiometricPromptManager.getCancellationSignal().cancel();
                    }
                    finish();
                }
            }
        });
        //切换注册页面
        dialogBuilder.addMenu(R.string.select_register, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(mContext, PhilipsRegisterActivity.class);
                startActivity(registerIntent);
                if (bottomMenuDialog != null) {
                    bottomMenuDialog.dismiss();
                    if (mBiometricPromptManager.getCancellationSignal() != null){
                        mBiometricPromptManager.getCancellationSignal().cancel();
                    }
                    finish();
                }
            }
        });
        bottomMenuDialog = dialogBuilder.create();
        bottomMenuDialog.show();
    }

    private void showImage(String photoPath) {
        if (!TextUtils.isEmpty(photoPath)) {
            Glide.with(this).load(photoPath).into(fingerImage);
        }
    }

    @Override
    public void downloadPhoto(Bitmap bitmap) {
        fingerImage.setImageBitmap(bitmap);
        StorageUtil.getInstance().saveServerPhoto(bitmap);
    }

    @Override
    public void downloadPhotoError(Throwable e) {
//        ToastUtils.showShort( HttpUtils.httpProtocolErrorCode(this,e));
    }

    private long lastClickBackTime = 0;
    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - lastClickBackTime > 2000) {
            lastClickBackTime = System.currentTimeMillis();
            ToastUtils.showLong(R.string.exit);
        } else {
            System.exit(0);
        }
    }

}
