package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philips.easykey.lock.R;

/**
 * author :
 * time   : 2021/5/6
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsAddVideoLockTask6Fragment extends Fragment {

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;

    public static PhilipsAddVideoLockTask6Fragment getInstance() {
        return new PhilipsAddVideoLockTask6Fragment();
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
        ImageView ivState = root.findViewById(R.id.ivState);
        EditText etName = root.findViewById(R.id.etName);
        RecyclerView rvNames = root.findViewById(R.id.rvNames);
        Button btnUse = root.findViewById(R.id.btnUse);
        rvNames.setLayoutManager(new GridLayoutManager(getContext(), 6));

        btnUse.setOnClickListener(v -> {
            // TODO: 2021/5/10 立即体验
            String name = etName.getText().toString();
            if(TextUtils.isEmpty(name)) {
                // TODO: 2021/5/10 提示错误
                return;
            }
            if(mAddVideoLockActivity != null) {
                mAddVideoLockActivity.setNickName(name);
            }
        });
    }

}
