package com.philips.easykey.lock.activity.my;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.personalpresenter.PersonalSecuritySettingPresenter;
import com.philips.easykey.lock.mvp.view.personalview.IPersonalSecuritySettingView;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.cachefloder.ACache;
import com.philips.easykey.lock.utils.cachefloder.CacheFloder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhilipsPersonalSecuritySettingActivity extends BaseActivity<IPersonalSecuritySettingView, PersonalSecuritySettingPresenter<IPersonalSecuritySettingView>> implements IPersonalSecuritySettingView {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.security_setting_switch_text)
    TextView securitySettingSwitchText;
    @BindView(R.id.update_hand_pwd_layout)
    RelativeLayout updateHandPwdLayout;
    @BindView(R.id.iv_open_hand_pwd)
    ImageView ivOpenHandPwd;
    @BindView(R.id.iv_open_touch_id)
    ImageView ivOpenTouchId;
    boolean handPassword = false;
    boolean touchId = false;
    boolean faceId = false;
    @BindView(R.id.rl_open_hand_pwd)
    RelativeLayout rlOpenHandPwd;
    @BindView(R.id.rl_open_touch_id)
    RelativeLayout rlOpenTouchId;
    @BindView(R.id.touch_id_status)
    TextView touchIdStatus;
    @BindView(R.id.rl_open_face_id)
    RelativeLayout rlOpenFaceId;
    @BindView(R.id.face_id_status)
    TextView faceIdStatus;
    @BindView(R.id.iv_open_face_id)
    ImageView ivOpenFaceId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_personal_security_setting);
        ButterKnife.bind(this);
        initView();
    }


    @Override
    protected PersonalSecuritySettingPresenter<IPersonalSecuritySettingView> createPresent() {
        return new PersonalSecuritySettingPresenter<>();
    }

    private void initView() {
        mPresenter.setHandPwdSwitchFlag();
        mPresenter.setFingerPrintFlag();
        tvContent.setText(R.string.philips_system_setting);
        setFaceIdUI(faceId);
    }

    @OnClick({R.id.update_hand_pwd_layout, R.id.iv_back, R.id.rl_open_hand_pwd, R.id.rl_open_touch_id, R.id.rl_open_face_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.update_hand_pwd_layout:
                String code = CacheFloder.readHandPassword(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "handPassword");
                if (code != null) {
                    Intent personalUpdateVerifyIntent = new Intent(this, PhilipsPersonalUpdateVerifyGesturePwd.class);
                    personalUpdateVerifyIntent.putExtra(KeyConstants.SOURCE, "PersonalSecuritySettingActivity_Update");
                    startActivity(personalUpdateVerifyIntent);
                } else {
                    showHandPwdDilog();
                }
                break;
            case R.id.rl_open_hand_pwd:
                //开启
                handPassword = !handPassword;
                if (handPassword) {
                    ivOpenHandPwd.setImageResource(R.mipmap.iv_open);
                    Intent open = new Intent(this, PersonalUpdateGesturePwdActivity.class);
                    startActivity(open);
                } else {
                    Intent personalUpdateVerifyIntent = new Intent(this, PhilipsPersonalUpdateVerifyGesturePwd.class);
                    personalUpdateVerifyIntent.putExtra(KeyConstants.SOURCE, "PhilipsPersonalSecuritySettingActivity");
                    startActivityForResult(personalUpdateVerifyIntent, 100);
                }
                break;
            case R.id.rl_open_touch_id:
                //指纹密码
                touchId = !touchId;
                //判断是否支持指纹识别
                if (touchId) {
                    Boolean flag = mPresenter.isSupportFinger();
                    if (flag == false) {
                        //手机不支持指纹识别
                        ivOpenTouchId.setImageResource(R.mipmap.iv_close);
                        touchIdStatus.setText(getString(R.string.open_touch_id));
                        ToastUtils.showShort(R.string.no_support_fingeprint);
                    } else {
                        mPresenter.isOpenFingerPrint();
                    }
                } else {
                    ivOpenTouchId.setImageResource(R.mipmap.iv_close);
                    touchIdStatus.setText(getString(R.string.open_touch_id));
                    ACache.get(MyApplication.getInstance()).remove(MyApplication.getInstance().getUid() + "fingerStatus");
                }
                break;
            case R.id.rl_open_face_id:
                faceId = !faceId;
                setFaceIdUI(faceId);
                break;

        }

    }

    private void showHandPwdDilog() {
        AlertDialogUtil.getInstance().noEditSingleButtonDialog(this, getString(R.string.hint), getString(R.string.please_open_hand_pwd), getString(R.string.dialog_confirm), new AlertDialogUtil.ClickListener() {
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


    @Override
    protected void onResume() {
        super.onResume();
        //设置关闭手势密码的开关
        mPresenter.setHandPwdSwitchFlag();
    }


    @Override
    public void openHandPwdSuccess() {
        handPassword = true;
        ivOpenHandPwd.setImageResource(R.mipmap.iv_open);
        securitySettingSwitchText.setText(R.string.close_hand_pwd);
    }

    @Override
    public void closeHandPwdSuccess() {
        handPassword = false;
        ivOpenHandPwd.setImageResource(R.mipmap.iv_close);
        securitySettingSwitchText.setText(R.string.open_hand_pwd);
    }

    @Override
    public void phoneFigerprintOpen() {
        //已经打开
        touchId = true;
        ivOpenTouchId.setImageResource(R.mipmap.iv_open);
        touchIdStatus.setText(getString(R.string.close_touch_id));
        CacheFloder.writePhoneFingerPrint(ACache.get(MyApplication.getInstance()), MyApplication.getInstance().getUid() + "fingerStatus", "true");
    }

    @Override
    public void phoneFigerprintClose() {
        //关闭
        touchId = false;
        ivOpenTouchId.setImageResource(R.mipmap.iv_close);
        touchIdStatus.setText(getString(R.string.open_touch_id));
        ToastUtils.showLong(R.string.no_open_fingerprint);
    }

    @Override
    public void openFingerPrintSuccess() {
        touchId = true;
        ivOpenTouchId.setImageResource(R.mipmap.iv_open);
        touchIdStatus.setText(getString(R.string.close_touch_id));
    }

    @Override
    public void closeFingerPrintSuccess() {
        touchId = false;
        ivOpenTouchId.setImageResource(R.mipmap.iv_close);
        touchIdStatus.setText(getString(R.string.open_touch_id));
    }

    private void setFaceIdUI(boolean faceId){
        if (faceId) {
            ivOpenFaceId.setImageResource(R.mipmap.iv_close);
            faceIdStatus.setText(getString(R.string.open_face_id));
        } else {
            ivOpenFaceId.setImageResource(R.mipmap.iv_open);
            faceIdStatus.setText(getString(R.string.close_face_id));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (100 == requestCode) {
                //清除缓存手势密码数据
                ivOpenHandPwd.setImageResource(R.mipmap.iv_close);
                ACache.get(MyApplication.getInstance()).remove(MyApplication.getInstance().getUid() + "handPassword");
                securitySettingSwitchText.setText(R.string.open_hand_pwd);
            }
        }
    }

}
