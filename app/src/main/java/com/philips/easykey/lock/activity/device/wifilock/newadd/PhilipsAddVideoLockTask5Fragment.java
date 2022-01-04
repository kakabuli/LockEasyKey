package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.StringUtil;

/**
 * author : Jack
 * time   : 2021/5/6
 * E-mail : wengmaowei@kaadas.com
 * desc   : 输入管理密码
 */
public class PhilipsAddVideoLockTask5Fragment extends Fragment {

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;
    private EditText mEtAdminPwd;
    private ImageView mIvShow;
    private boolean passwordHide = true;

    public static PhilipsAddVideoLockTask5Fragment getInstance() {
        return new PhilipsAddVideoLockTask5Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_video_lock_task5, container, false);
        initTaskUI(root);
        if(getActivity() instanceof PhilipsAddVideoLockActivity) {
            mAddVideoLockActivity = (PhilipsAddVideoLockActivity) getActivity();
        }
        return root;
    }
    Button btnNext;
    public void initUIAndData() {
        if(btnNext != null){
            btnNext.setEnabled(true);
        }
    }

    private void initTaskUI(View root) {
        mEtAdminPwd = root.findViewById(R.id.etAdminPwd);
        btnNext = root.findViewById(R.id.btnNext);
        mIvShow = root.findViewById(R.id.ivShow);
        TextView tvPwdFailed = root.findViewById(R.id.tvPwdFailed);
        btnNext.setEnabled(false);
        mIvShow.setOnClickListener(v -> {
            passwordHide = !passwordHide;
            if (passwordHide) {
                mEtAdminPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mIvShow.setImageResource(R.drawable.philips_dms_icon_hidden);
            } else {
                mEtAdminPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mIvShow.setImageResource(R.drawable.philips_dms_icon_display);
            }
            mEtAdminPwd.setSelection(mEtAdminPwd.getText().toString().length());//将光标移至文字末尾
        });

        mEtAdminPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                btnNext.setEnabled(s.length() > 0);
                btnNext.setBackgroundResource(s.length()>0?R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_invalid_bg);
            }
        });

        btnNext.setOnClickListener(v -> {
            String adminPwd = mEtAdminPwd.getText().toString();
            if (!StringUtil.randomJudge(adminPwd)) {
                ToastUtils.showShort(getString(R.string.philips_random_verify_error));
                return;
            }
            if(mAddVideoLockActivity != null) {
                btnNext.setEnabled(false);
                mAddVideoLockActivity.setAdminPwd(adminPwd);
            }
            // TODO: 2021/5/8 需要回调监听成功与失败
        });
        tvPwdFailed.setOnClickListener(v -> {
            // TODO: 2021/5/8 跳转到错误指引页面
            AlertDialogUtil.getInstance().singleButtonNoTitleDialog(getContext(), getString(R.string.philips_lock_restore_tip), getString(R.string.philips_confirm), "#ffffff", new AlertDialogUtil.ClickListener() {
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
        });
    }

    public void clearInputContent() {
        if(mEtAdminPwd != null) {
            mEtAdminPwd.setText("");
        }
    }

}
