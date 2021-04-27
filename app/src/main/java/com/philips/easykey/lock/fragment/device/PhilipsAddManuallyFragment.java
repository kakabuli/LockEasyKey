package com.philips.easykey.lock.fragment.device;

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
 * time   : 2021/4/26
 * E-mail : wengmaowei@kaadas.com
 * desc   : 手动添加设备
 */
public class PhilipsAddManuallyFragment extends Fragment {

    public static PhilipsAddManuallyFragment newInstance() {
        return new PhilipsAddManuallyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_manually, container, false);

        return root;
    }
}
