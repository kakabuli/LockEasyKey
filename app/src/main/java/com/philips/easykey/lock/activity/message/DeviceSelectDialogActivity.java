package com.philips.easykey.lock.activity.message;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.DeviceSelectAdapter;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.bean.SevendayDataStatisticsBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceSelectDialogActivity extends BaseAddToApplicationActivity {

    @BindView(R.id.rcv_device_select)
    RecyclerView rcvDeviceSelect;
    private DeviceSelectAdapter deviceSelectAdapter;
    private int RESULT_OK = 100;
    private int RESULT_CLOSE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setContentView(R.layout.device_select_dialog);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        List<HomeShowBean> HomeShowData = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            HomeShowBean homeShowBean = new HomeShowBean();
            if(i == 0){
                homeShowBean.setDeviceNickName("wifi锁");
                homeShowBean.setDeviceType(HomeShowBean.TYPE_WIFI_LOCK);
            }else if(i == 1){
                homeShowBean.setDeviceNickName("视频锁");
                homeShowBean.setDeviceType(HomeShowBean.TYPE_WIFI_VIDEO_LOCK);
            }else {
                homeShowBean.setDeviceNickName("DDL708-1HW");
                homeShowBean.setDeviceType(HomeShowBean.TYPE_WIFI_VIDEO_LOCK);
            }
            HomeShowData.add(homeShowBean);
        }
        deviceSelectAdapter = new DeviceSelectAdapter(HomeShowData, new DeviceSelectAdapter.DeviceSelectCallBackLinstener() {
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
        rcvDeviceSelect.addItemDecoration(new SpacesItemDecoration(0, 0, 40, 0));
        rcvDeviceSelect.setLayoutManager(verticalLayoutManager);
        rcvDeviceSelect.setAdapter(deviceSelectAdapter);
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        setResult(RESULT_CLOSE);
        finish();
    }
}
