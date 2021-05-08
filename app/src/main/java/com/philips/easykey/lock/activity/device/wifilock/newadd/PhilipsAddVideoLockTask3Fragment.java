package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.philips.easykey.lock.R;

/**
 * author :
 * time   : 2021/5/6
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsAddVideoLockTask3Fragment extends Fragment {

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;
    private boolean isChoose = false;
    private ImageView mIvSelect;
    private Button mBtnNext;
    private ImageView mIvQrCode;

    public static PhilipsAddVideoLockTask3Fragment getInstance() {
        return new PhilipsAddVideoLockTask3Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_video_lock_task3, container, false);
        initTaskUI(root);
        if(getActivity() instanceof PhilipsAddVideoLockActivity) {
            mAddVideoLockActivity = (PhilipsAddVideoLockActivity) getActivity();
        }
        return root;
    }

    private void initTaskUI(View root) {
        mIvQrCode = root.findViewById(R.id.ivQrCode);
        mIvSelect = root.findViewById(R.id.ivSelect);
        mBtnNext = root.findViewById(R.id.btnNext);

        root.findViewById(R.id.tvVoicePromptFailed).setOnClickListener(v -> {
            // TODO: 2021/5/8 跳转页面
        });

        mIvSelect.setOnClickListener(v -> {
            isChoose = !isChoose;
            refreshNext();
        });
        mBtnNext.setOnClickListener(v -> {
            if(mAddVideoLockActivity != null) {
                mAddVideoLockActivity.showFirstTask4();
            }
        });

    }

    public void refreshQrCode() {
        if(mAddVideoLockActivity != null) {
            Bitmap qrCode = mAddVideoLockActivity.getQrCode();
            if(qrCode != null) {
                Glide.with(this).load(qrCode).into(mIvQrCode);
            }
        }
    }


    private void refreshNext() {
        mBtnNext.setBackgroundResource(isChoose?R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_invalid_bg);
        // TODO: 2021/5/6 缺少切图，暂时使用其他的临时替代
        mIvSelect.setImageResource(isChoose?R.drawable.philips_dms_icon_success:R.drawable.philips_dms_icon_default);
        mBtnNext.setEnabled(isChoose);
    }

}
