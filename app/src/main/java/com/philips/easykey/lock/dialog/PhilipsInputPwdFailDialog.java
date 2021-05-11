package com.philips.easykey.lock.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.philips.easykey.lock.R;

/**
 * author :
 * time   : 2021/5/11
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsInputPwdFailDialog extends Dialog {

    private OnPwdFailListener mOnPwdFailListener;
    private final TextView mTvPwdTip, mTvReEnter, mTvForgotPwd;
    private final Button mBtnSure;

    public PhilipsInputPwdFailDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.philips_dialog_input_pwd_fail);
        mTvReEnter = findViewById(R.id.tvReEnter);
        mTvForgotPwd = findViewById(R.id.tvForgotPwd);
        mBtnSure = findViewById(R.id.btnSure);
        mTvPwdTip = findViewById(R.id.tvPwdTip);

        mTvReEnter.setOnClickListener(v -> {
            if(mOnPwdFailListener != null) {
                mOnPwdFailListener.reEnter();
            }
        });
        mTvForgotPwd.setOnClickListener(v -> {
            if(mOnPwdFailListener != null) {
                mOnPwdFailListener.forgotPwd();
            }
        });
        mBtnSure.setOnClickListener(v -> {
            if(mOnPwdFailListener != null) {
                mOnPwdFailListener.sure();
            }
        });
    }

    /**
     * 设置显示的样式
     * @param isMore5TimesWrong 输入错误是否超过五次
     */
    public void setShowType(boolean isMore5TimesWrong) {
        if(isMore5TimesWrong) {
            if(mTvPwdTip != null) mTvPwdTip.setText(R.string.philips_already_5_times_wrong_plz_repair_network_after_modify_admin_pwd);
            if(mTvForgotPwd != null) mTvForgotPwd.setVisibility(View.GONE);
            if(mTvReEnter != null) mTvReEnter.setVisibility(View.GONE);
            if(mBtnSure != null) mBtnSure.setVisibility(View.VISIBLE);
        } else {
            if(mTvPwdTip != null) mTvPwdTip.setText(R.string.philips_plz_input_right_admin_pwd_5_times_need_repair);
            if(mTvForgotPwd != null) mTvForgotPwd.setVisibility(View.VISIBLE);
            if(mTvReEnter != null) mTvReEnter.setVisibility(View.VISIBLE);
            if(mBtnSure != null) mBtnSure.setVisibility(View.GONE);
        }
    }

    public void setOnPwdFailListener(OnPwdFailListener onPwdFailListener) {
        mOnPwdFailListener = onPwdFailListener;
    }

    public interface OnPwdFailListener {
        void reEnter();
        void forgotPwd();
        void sure();
    }

}
