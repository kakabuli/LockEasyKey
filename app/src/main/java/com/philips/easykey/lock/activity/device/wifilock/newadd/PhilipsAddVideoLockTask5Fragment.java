package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
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

    private void initTaskUI(View root) {
        mEtAdminPwd = root.findViewById(R.id.etAdminPwd);
        Button btnNext = root.findViewById(R.id.btnNext);
        TextView tvPwdFailed = root.findViewById(R.id.tvPwdFailed);
        btnNext.setEnabled(false);

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
                ToastUtils.showShort(R.string.philips_random_verify_error);
                return;
            }
            if(mAddVideoLockActivity != null) {
                mAddVideoLockActivity.setAdminPwd(adminPwd);
            }
            // TODO: 2021/5/8 需要回调监听成功与失败
        });
        tvPwdFailed.setOnClickListener(v -> {
            // TODO: 2021/5/8 跳转到错误指引页面
        });
    }

    public void clearInputContent() {
        if(mEtAdminPwd != null) {
            mEtAdminPwd.setText("");
        }
    }

}
