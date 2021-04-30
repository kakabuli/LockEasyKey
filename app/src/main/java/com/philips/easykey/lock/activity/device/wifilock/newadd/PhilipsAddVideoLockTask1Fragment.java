package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.philips.easykey.lock.R;

/**
 * author :
 * time   : 2021/4/30
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsAddVideoLockTask1Fragment extends Fragment {

    private ImageView mIvOperatingTip;
    private TextView mTvTask1Tip;
    private ImageView mIvSelect;
    private TextView mTvSelectTip;
    private Button mBtnNext;
    private TextView mTvVoicePromptFailed;

    public static PhilipsAddVideoLockTask1Fragment getInstance() {
        return new PhilipsAddVideoLockTask1Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_video_lock_task1, container, false);
        initTask1UI(root);
        return root;
    }

    private void initTask1UI(View root) {
        mIvOperatingTip = root.findViewById(R.id.ivOperatingTip);
        mTvTask1Tip = root.findViewById(R.id.tvTask1Tip);
        mIvSelect = root.findViewById(R.id.ivSelect);
        mTvSelectTip = root.findViewById(R.id.tvSelectTip);
        mBtnNext = root.findViewById(R.id.btnNext);
        mTvVoicePromptFailed = root.findViewById(R.id.tvVoicePromptFailed);
    }

}
