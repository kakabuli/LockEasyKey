package com.philips.easykey.lock.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.PhilipsDeviceTypeAdapter;
import com.philips.easykey.lock.bean.PhilipsDeviceTypeBean;

import java.util.ArrayList;

/**
 * author :
 * time   : 2021/4/21
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsDeviceFragment extends Fragment {

    private PhilipsDeviceTypeAdapter mDeviceTypeAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_device, container, false);

        RecyclerView  rvDeviceTap = root.findViewById(R.id.rvDeviceTap);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvDeviceTap.setLayoutManager(layoutManager);
        mDeviceTypeAdapter = new PhilipsDeviceTypeAdapter(R.layout.philips_item_device_tap);
        rvDeviceTap.setAdapter(mDeviceTypeAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        ArrayList<PhilipsDeviceTypeBean> list = new ArrayList<>();
        PhilipsDeviceTypeBean bean1 = new PhilipsDeviceTypeBean();
        bean1.setTypeName("所有设备");
        bean1.setSelected(true);
        list.add(bean1);
        PhilipsDeviceTypeBean bean2 = new PhilipsDeviceTypeBean();
        bean2.setTypeName("智能锁");
        bean2.setSelected(false);
        list.add(bean2);
        PhilipsDeviceTypeBean bean3 = new PhilipsDeviceTypeBean();
        bean3.setTypeName("晾衣机");
        bean3.setSelected(false);
        list.add(bean3);
        mDeviceTypeAdapter.setList(list);
    }

}
