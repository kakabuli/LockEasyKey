package com.philips.easykey.lock.fragment.message;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.message.PhilipsDeviceSelectDialogActivity;
import com.philips.easykey.lock.adapter.PhilipsSevenDayDataStatisticsAdapter;
import com.philips.easykey.lock.adapter.PhilipsTodayLockStatisticsAdapter;
import com.philips.easykey.lock.adapter.PhilipsVideoLockWarningInformAdapter;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.bean.SevendayDataStatisticsBean;
import com.philips.easykey.lock.bean.TodayLockStatisticsBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.DoorLockMessageFragmentPresenter;
import com.philips.easykey.lock.mvp.view.IDoorLockMessageView;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PhilipsDoorLockMessageFragment extends BaseFragment<IDoorLockMessageView, DoorLockMessageFragmentPresenter<IDoorLockMessageView>> implements IDoorLockMessageView {

    @BindView(R.id.rcv_video_lock_msg)
    RecyclerView rcvVideoLockMsg;
    @BindView(R.id.rcv_today_lock_statistics)
    RecyclerView rcvTodayLockStatistics;
    @BindView(R.id.rcv_seven_day_data_statistics)
    RecyclerView rcvSevenDayDataStatistics;
    @BindView(R.id.tv_lock_name)
    TextView tvLockName;
    @BindView(R.id.ll_video_lock_msg)
    LinearLayout llVideoLockMsg;
    private PhilipsVideoLockWarningInformAdapter videoLockWarningInformAdapter;
    private PhilipsTodayLockStatisticsAdapter lockStatisticsAdapter;
    private PhilipsSevenDayDataStatisticsAdapter sevendayDataStatisticsAdapter;
    private int RESULT_OK = 100;
    private View mView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.philips_fragment_door_lock_message, container, false);
        }
        unbinder = ButterKnife.bind(this, mView);
        initView();
        return mView;
    }

    @Override
    protected DoorLockMessageFragmentPresenter<IDoorLockMessageView> createPresent() {
        return new DoorLockMessageFragmentPresenter<>();
    }

    private void initView() {
        List<WifiVideoLockAlarmRecord> wifiVideoLockAlarmRecordData = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            WifiVideoLockAlarmRecord wifiVideoLockAlarmRecord = new WifiVideoLockAlarmRecord();
            wifiVideoLockAlarmRecordData.add(wifiVideoLockAlarmRecord);
        }
        videoLockWarningInformAdapter = new PhilipsVideoLockWarningInformAdapter(wifiVideoLockAlarmRecordData, new PhilipsVideoLockWarningInformAdapter.VideoLockWarningCallBackLinstener() {
            @Override
            public void onVideoLockWarningCallBackLinstener(WifiVideoLockAlarmRecord record) {

            }
        });
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext());
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvVideoLockMsg.setLayoutManager(horizontalLayoutManager);
        rcvVideoLockMsg.setAdapter(videoLockWarningInformAdapter);

        List<TodayLockStatisticsBean> TodayLockStatisticsData = new ArrayList<>();
        TodayLockStatisticsBean todayLockStatisticsBean = new TodayLockStatisticsBean();
        todayLockStatisticsBean.setStatisticsType(1);
        todayLockStatisticsBean.setStatisticsCount(10);
        TodayLockStatisticsData.add(todayLockStatisticsBean);

        TodayLockStatisticsBean todayLockStatisticsBean1 = new TodayLockStatisticsBean();
        todayLockStatisticsBean1.setStatisticsType(2);
        todayLockStatisticsBean1.setStatisticsCount(3);
        TodayLockStatisticsData.add(todayLockStatisticsBean1);

        TodayLockStatisticsBean todayLockStatisticsBean2 = new TodayLockStatisticsBean();
        todayLockStatisticsBean2.setStatisticsType(3);
        todayLockStatisticsBean2.setStatisticsCount(110);
        TodayLockStatisticsData.add(todayLockStatisticsBean2);

        TodayLockStatisticsBean todayLockStatisticsBean3 = new TodayLockStatisticsBean();
        todayLockStatisticsBean3.setStatisticsType(4);
        todayLockStatisticsBean3.setStatisticsCount(20);
        TodayLockStatisticsData.add(todayLockStatisticsBean3);

        TodayLockStatisticsBean todayLockStatisticsBean4 = new TodayLockStatisticsBean();
        todayLockStatisticsBean4.setStatisticsType(3);
        todayLockStatisticsBean4.setStatisticsCount(55);
        TodayLockStatisticsData.add(todayLockStatisticsBean4);

        lockStatisticsAdapter = new PhilipsTodayLockStatisticsAdapter(TodayLockStatisticsData);
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getContext());
        horizontalLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvTodayLockStatistics.setLayoutManager(horizontalLayoutManager1);
        rcvTodayLockStatistics.setAdapter(lockStatisticsAdapter);

        List<SevendayDataStatisticsBean> sevendayDataStatisticsData = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SevendayDataStatisticsBean sevendayDataStatisticsBean = new SevendayDataStatisticsBean();
            sevendayDataStatisticsBean.setOrdinateValue(new float[]{20, 20, 27, 26, 24, 21});
            sevendayDataStatisticsBean.setTransverseValue(new String[]{"02.13", "02.14", "02.15", "02.16", "02.17", "02.18"});
            sevendayDataStatisticsBean.setStatisticsTypeName(getString(R.string.warn_information));
            sevendayDataStatisticsData.add(sevendayDataStatisticsBean);
        }
        sevendayDataStatisticsAdapter = new PhilipsSevenDayDataStatisticsAdapter(sevendayDataStatisticsData);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        verticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvSevenDayDataStatistics.setLayoutManager(verticalLayoutManager);
        rcvSevenDayDataStatistics.setAdapter(sevendayDataStatisticsAdapter);
    }

    private void refreshLayoutData(HomeShowBean homeShowBean) {
        tvLockName.setText(homeShowBean.getDeviceNickName());
        if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_WIFI_LOCK) {
            llVideoLockMsg.setVisibility(View.GONE);
        } else if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_WIFI_VIDEO_LOCK) {
            llVideoLockMsg.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.ll_device_type)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), PhilipsDeviceSelectDialogActivity.class);
        startActivityForResult(intent, RESULT_OK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            HomeShowBean homeShowBean = (HomeShowBean) data.getSerializableExtra("homeShowBean");
            refreshLayoutData(homeShowBean);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
