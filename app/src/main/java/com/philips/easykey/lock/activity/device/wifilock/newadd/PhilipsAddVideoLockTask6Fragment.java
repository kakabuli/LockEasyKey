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

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.AddBluetoothPairSuccessAdapter;
import com.philips.easykey.lock.bean.deviceAdd.AddBluetoothPairSuccessBean;
import com.philips.easykey.lock.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author :
 * time   : 2021/5/6
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsAddVideoLockTask6Fragment extends Fragment {

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;

    private List<AddBluetoothPairSuccessBean> mList;
    private AddBluetoothPairSuccessAdapter mAdapter;

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
        initData(rvNames, etName);
        btnUse.setOnClickListener(v -> {
            // TODO: 2021/5/10 立即体验
            String name = etName.getText().toString();
            if(TextUtils.isEmpty(name)) {
                ToastUtils.showShort(R.string.nickName_not_empty);
                return;
            }
            if (name.length() > 10 ) {
                ToastUtils.showShort(R.string.philips_nickname_verify_error);
                return;
            }

            if(mAddVideoLockActivity != null) {
                mAddVideoLockActivity.setNickName(name);
            }
        });
    }

    private void initData(@NonNull RecyclerView recycler, @NonNull EditText etName) {
        mList = new ArrayList<>();
        mList.add(new AddBluetoothPairSuccessBean(getString(R.string.philips_wifi_lock_my_home), false));
        mList.add(new AddBluetoothPairSuccessBean(getString(R.string.philips_wifi_lock_bedroom), false));
        mList.add(new AddBluetoothPairSuccessBean(getString(R.string.philips_wifi_lock_company), false));
        if (mList != null) {
            mAdapter = new AddBluetoothPairSuccessAdapter(mList);
            recycler.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                etName.setCursorVisible(true);
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setSelected(false);
                }
                AddBluetoothPairSuccessBean addBluetoothPairSuccessBean = mList.get(position);
                String name = addBluetoothPairSuccessBean.getName();
                etName.setText(name);
                if (name != null) {
                    etName.setSelection(name.length());
                }
                etName.setFocusable(true);
                etName.setFocusableInTouchMode(true);
                etName.requestFocus();
                mList.get(position).setSelected(true);
                mAdapter.notifyDataSetChanged();
            });
        }
    }

}
