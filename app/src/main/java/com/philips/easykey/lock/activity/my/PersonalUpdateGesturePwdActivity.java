package com.philips.easykey.lock.activity.my;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.cachefloder.ACache;
import com.philips.easykey.lock.utils.cachefloder.CacheFloder;
import com.philips.easykey.lock.utils.handPwdUtil.GestureContentView;
import com.philips.easykey.lock.utils.handPwdUtil.GestureDrawline;
import com.philips.easykey.lock.utils.handPwdUtil.LockIndicator;


public class PersonalUpdateGesturePwdActivity extends BaseAddToApplicationActivity {

    ImageView gesturePwdBack;
    LockIndicator lockIndicator;
    TextView mTextTip;
    FrameLayout gestureContainer;

    private GestureContentView mGestureContentView;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_update_hand_pwd);

        gesturePwdBack = findViewById(R.id.gesture_pwd_back);
        lockIndicator = findViewById(R.id.lock_indicator);
        mTextTip = findViewById(R.id.text_tip);
        gestureContainer = findViewById(R.id.gesture_container);

        findViewById(R.id.gesture_pwd_back).setOnClickListener(v -> finish());

        initView();
    }

    private void initView() {
        mGestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {
            @Override
            public void onGestureCodeInput(String inputCode) {
                if (!isInputPassValidate(inputCode)) {
                    mTextTip.setText(getResources().getString(R.string.least_four_again_input));
                    mGestureContentView.clearDrawlineState(0L);
                    return;
                }
                if (mIsFirstInput) {
                    mFirstPassword = inputCode;
                    updateCodeList(inputCode);
                    mTextTip.setText(getResources().getString(R.string.set_again_gesture_pattern));
                    mGestureContentView.clearDrawlineState(0L);
                } else {
                    if (inputCode.equals(mFirstPassword)) {
                        mGestureContentView.clearDrawlineState(0L);
                        //缓存手势密码成功的值
                        String code = CacheFloder.readHandPassword(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "handPassword");
                        if (code != null) {
                            ACache.get(MyApplication.getInstance()).remove(MyApplication.getInstance().getUid() + "handPassword");
                        }
                        CacheFloder.writeHandPassword(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "handPassword", inputCode);
                        PersonalUpdateGesturePwdActivity.this.finish();
                    } else {
                        mTextTip.setText(getResources().getString(R.string.repaint_gesture_pattern));
                        // 左右移动动画
                        Animation shakeAnimation = AnimationUtils.loadAnimation(PersonalUpdateGesturePwdActivity.this, R.anim.shake);
                        mTextTip.startAnimation(shakeAnimation);
                        // 保持绘制的线，1.5秒后清除
                        mGestureContentView.clearDrawlineState(1300L);
                    }
                }
                mIsFirstInput = false;
            }

            @Override
            public void checkedSuccess() {

            }

            @Override
            public void checkedFail() {


            }
        });
        // 设置手势解锁显示到哪个布局里面
        mGestureContentView.setParentView(gestureContainer);
        updateCodeList("");

    }

    private boolean isInputPassValidate(String inputPassword) {
        if (TextUtils.isEmpty(inputPassword) || inputPassword.length() < 4) {
            return false;
        }
        return true;
    }

    private void updateCodeList(String inputCode) {
        // 更新选择的图案
        lockIndicator.setPath(inputCode);
    }
}
