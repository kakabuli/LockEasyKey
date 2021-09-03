package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.philips.easykey.lock.R;

/**
 * author :
 * time   : 2021/5/11
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsAddVideoLockTask5ModifyPwdFragment extends Fragment {

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;

    private ImageView mIvSelect;
    private Button mBtnNext;
    private boolean isChoose = false;

    public static PhilipsAddVideoLockTask5ModifyPwdFragment getInstance() {
        return new PhilipsAddVideoLockTask5ModifyPwdFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_video_lock_task5_modify_pwd, container, false);
        initTaskUI(root);
        if(getActivity() instanceof PhilipsAddVideoLockActivity) {
            mAddVideoLockActivity = (PhilipsAddVideoLockActivity) getActivity();
        }
        return root;
    }

    private void initTaskUI(View root) {
        mIvSelect = root.findViewById(R.id.ivSelect);
        mBtnNext = root.findViewById(R.id.btnContinueToVerify);

        mIvSelect.setOnClickListener(v -> {
            isChoose = !isChoose;
            refreshNext();
        });
        mBtnNext.setOnClickListener(v -> {
            if(mAddVideoLockActivity != null && isChoose) {
                mAddVideoLockActivity.showFirstTask1();
            }
        });
    }

    private void refreshNext() {
        mBtnNext.setBackgroundResource(isChoose?R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_invalid_bg);
        mIvSelect.setImageResource(isChoose?R.drawable.philips_dms_icon_selected:R.drawable.philips_dms_icon_default);
        mBtnNext.setEnabled(isChoose);
    }
}
