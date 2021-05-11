package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.philips.easykey.lock.R;

/**
 * author : Jack
 * time   : 2021/5/11
 * E-mail : wengmaowei@kaadas.com
 * desc   : 配网超时失败
 */
public class PhilipsAddVideoLockTask4FailFragment extends Fragment {

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;

    public static PhilipsAddVideoLockTask4FailFragment getInstance() {
        return new PhilipsAddVideoLockTask4FailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_video_lock_task4_fail, container, false);
        initTaskUI(root);
        if(getActivity() instanceof PhilipsAddVideoLockActivity) {
            mAddVideoLockActivity = (PhilipsAddVideoLockActivity) getActivity();
        }
        return root;
    }

    private void initTaskUI(View root) {

        Button btnReConnect = root.findViewById(R.id.btnReConnect);
        Button btnCancel = root.findViewById(R.id.btnCancel);

        btnReConnect.setOnClickListener(v -> {
            if(mAddVideoLockActivity != null) {
                mAddVideoLockActivity.showFirstTask2();
            }
        });
        btnCancel.setOnClickListener(v -> {
            if(mAddVideoLockActivity != null) {
                mAddVideoLockActivity.finish();
            }
        });

    }

}
