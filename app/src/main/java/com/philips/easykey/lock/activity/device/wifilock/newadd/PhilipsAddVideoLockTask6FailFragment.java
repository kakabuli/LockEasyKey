package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.philips.easykey.lock.R;

/**
 * author : Jack
 * time   : 2021/5/11
 * E-mail : wengmaowei@kaadas.com
 * desc   : 添加名字失败
 */
public class PhilipsAddVideoLockTask6FailFragment extends Fragment {

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;

    public PhilipsAddVideoLockTask6FailFragment getInstance() {
        return new PhilipsAddVideoLockTask6FailFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_video_lock_task6, container, false);
        initTaskUI(root);
        if(getActivity() instanceof PhilipsAddVideoLockActivity) {
            mAddVideoLockActivity = (PhilipsAddVideoLockActivity) getActivity();
        }
        return root;
    }

    private void initTaskUI(View root) {

    }

}
