package com.philips.easykey.lock.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.DeviceSelectAdapter;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhilipsDeviceSelectDialogActivity extends BaseAddToApplicationActivity {

    @BindView(R.id.rcv_device_select)
    RecyclerView rcvDeviceSelect;
    private DeviceSelectAdapter deviceSelectAdapter;
    private int RESULT_OK = 100;
    private int RESULT_CLOSE = 101;
    private final List<HomeShowBean> mDevices = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.philips_device_select_dialog);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initData(){
        mDevices.clear();
        mDevices.addAll(MyApplication.getInstance().getHomeShowDevices());
    }

    private void initView(){
        deviceSelectAdapter = new DeviceSelectAdapter(mDevices, new DeviceSelectAdapter.DeviceSelectCallBackLinstener() {
            @Override
            public void onDeviceSelectCallBackLinstener(HomeShowBean homeShowBean) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("homeShowBean", homeShowBean);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this);
        verticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvDeviceSelect.setLayoutManager(verticalLayoutManager);
        rcvDeviceSelect.setAdapter(deviceSelectAdapter);
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        setResult(RESULT_CLOSE);
        finish();
    }
}
