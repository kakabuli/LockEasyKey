package com.philips.easykey.lock.fragment.message;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.SevendayDataStatisticsAdapter;
import com.philips.easykey.lock.adapter.TodayLockStatisticsAdapter;
import com.philips.easykey.lock.adapter.VideoLockWarningInformAdapter;
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
import butterknife.Unbinder;

public class DoorLockMessageFragment extends BaseFragment<IDoorLockMessageView, DoorLockMessageFragmentPresenter<IDoorLockMessageView>> implements IDoorLockMessageView {

    @BindView(R.id.rcv_video_lock_msg)
    RecyclerView rcvVideoLockMsg;
    @BindView(R.id.rcv_today_lock_statistics)
    RecyclerView rcvTodayLockStatistics;
    @BindView(R.id.rcv_seven_day_data_statistics)
    RecyclerView rcvSevenDayDataStatistics;
    private VideoLockWarningInformAdapter videoLockWarningInformAdapter;
    private TodayLockStatisticsAdapter lockStatisticsAdapter;
    private SevendayDataStatisticsAdapter sevendayDataStatisticsAdapter;
    private View mView;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_door_lock_message, container, false);
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
        videoLockWarningInformAdapter = new VideoLockWarningInformAdapter(wifiVideoLockAlarmRecordData, new VideoLockWarningInformAdapter.VideoLockWarningCallBackLinstener() {
            @Override
            public void onVideoLockWarningCallBackLinstener(WifiVideoLockAlarmRecord record) {

            }
        });
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext());
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvVideoLockMsg.addItemDecoration(new SpacesItemDecoration(40, 0, 0, 0));
        rcvVideoLockMsg.setLayoutManager(horizontalLayoutManager);
        rcvVideoLockMsg.setAdapter(videoLockWarningInformAdapter);

        List<TodayLockStatisticsBean> TodayLockStatisticsData = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            TodayLockStatisticsBean todayLockStatisticsBean = new TodayLockStatisticsBean();
            todayLockStatisticsBean.setStatisticsType("预警信息");
            todayLockStatisticsBean.setStatisticsCount(10);
            TodayLockStatisticsData.add(todayLockStatisticsBean);
        }
        lockStatisticsAdapter = new TodayLockStatisticsAdapter(TodayLockStatisticsData);
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getContext());
        horizontalLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvTodayLockStatistics.addItemDecoration(new SpacesItemDecoration(40, 0, 0, 0));
        rcvTodayLockStatistics.setLayoutManager(horizontalLayoutManager1);
        rcvTodayLockStatistics.setAdapter(lockStatisticsAdapter);

        List<SevendayDataStatisticsBean> sevendayDataStatisticsData = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SevendayDataStatisticsBean sevendayDataStatisticsBean = new SevendayDataStatisticsBean();
            sevendayDataStatisticsBean.setOrdinateValue(new float[]{0, 0, 23, 10, 24, 42, 18});
            sevendayDataStatisticsBean.setTransverseValue(new String[]{"02 13", "02 14", "02 15", "02 16", "02 17", "02 18", "02 19"});
            sevendayDataStatisticsBean.setStatisticsTypeName("预警信息");
            sevendayDataStatisticsData.add(sevendayDataStatisticsBean);
        }
        sevendayDataStatisticsAdapter = new SevendayDataStatisticsAdapter(sevendayDataStatisticsData);
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        verticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvSevenDayDataStatistics.addItemDecoration(new SpacesItemDecoration(0, 0, 40, 0));
        rcvSevenDayDataStatistics.setLayoutManager(verticalLayoutManager);
        rcvSevenDayDataStatistics.setAdapter(sevendayDataStatisticsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
