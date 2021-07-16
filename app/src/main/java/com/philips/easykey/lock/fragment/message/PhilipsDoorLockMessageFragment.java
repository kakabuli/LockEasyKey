package com.philips.easykey.lock.fragment.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.philips.easykey.lock.MyApplication;
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
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.http.result.GetStatisticsDayResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetStatisticsSevenDayResult;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.widget.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class PhilipsDoorLockMessageFragment extends BaseFragment<IDoorLockMessageView, DoorLockMessageFragmentPresenter<IDoorLockMessageView>> implements IDoorLockMessageView {

    RecyclerView rcvVideoLockMsg;
    RecyclerView rcvTodayLockStatistics;
    RecyclerView rcvSevenDayDataStatistics;
    TextView tvLockName;
    LinearLayout llVideoLockMsg;
    ImageView ivVideoLockMsgLeft;
    ImageView ivVideoLockMsgRight;
    ImageView ivTodayLockStatisticsLeft;
    ImageView ivTodayLockStatisticsRight;
    TextView tvOpenLockNumber;
    TextView tvNoMessage;
    RelativeLayout llDeviceType;
    ScrollView scrollView;
    TextView createTime;
    TextView tvOpenLockTimes;

    private PhilipsVideoLockWarningInformAdapter videoLockWarningInformAdapter;
    private PhilipsTodayLockStatisticsAdapter lockStatisticsAdapter;
    private PhilipsSevenDayDataStatisticsAdapter sevendayDataStatisticsAdapter;
    private int RESULT_OK = 100;
    private View mView;
    private List<WifiVideoLockAlarmRecord> wifiVideoLockAlarmRecordData = new ArrayList<>();
    private List<TodayLockStatisticsBean> TodayLockStatisticsData = new ArrayList<>();
    private List<SevendayDataStatisticsBean> sevendayDataStatisticsData = new ArrayList<>();
    private int earlyWarningMsgMoveLeftDistance = 0;
    private int earlyWarningMsgMoveRightDistance = 0;
    private int todayLockStatisticsMoveLeftDistance = 0;
    private int todayLockStatisticsMoveRightDistance = 0;
    private int itemDecorationLeft = SizeUtils.dp2px(4);
    private final List<HomeShowBean> mDevices = new ArrayList<>();
    private WifiLockInfo wifiLockInfo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.philips_fragment_door_lock_message, container, false);
        }

        rcvVideoLockMsg = mView.findViewById(R.id.rcv_video_lock_msg);
        rcvTodayLockStatistics = mView.findViewById(R.id.rcv_today_lock_statistics);
        rcvSevenDayDataStatistics = mView.findViewById(R.id.rcv_seven_day_data_statistics);
        tvLockName = mView.findViewById(R.id.tv_lock_name);
        llVideoLockMsg = mView.findViewById(R.id.ll_video_lock_msg);
        ivVideoLockMsgLeft = mView.findViewById(R.id.iv_video_lock_msg_left);
        ivVideoLockMsgRight = mView.findViewById(R.id.iv_video_lock_msg_right);
        tvOpenLockNumber = mView.findViewById(R.id.tv_open_lock_number);
        ivTodayLockStatisticsLeft = mView.findViewById(R.id.iv_today_lock_statistics_left);
        ivTodayLockStatisticsRight = mView.findViewById(R.id.iv_today_lock_statistics_right);
        tvNoMessage = mView.findViewById(R.id.tv_no_message);
        llDeviceType = mView.findViewById(R.id.ll_device_type);
        scrollView = mView.findViewById(R.id.scrollView);
        createTime = mView.findViewById(R.id.create_time);
        tvOpenLockTimes = mView.findViewById(R.id.tv_open_lock_times);

        mView.findViewById(R.id.ll_device_type).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PhilipsDeviceSelectDialogActivity.class);
            startActivityForResult(intent, RESULT_OK);
        });
        mView.findViewById(R.id.iv_video_lock_msg_left).setOnClickListener(v -> rcvVideoLockMsg.scrollBy(earlyWarningMsgMoveLeftDistance,0));
        mView.findViewById(R.id.iv_video_lock_msg_right).setOnClickListener(v -> rcvVideoLockMsg.scrollBy(-earlyWarningMsgMoveRightDistance,0));
        mView.findViewById(R.id.iv_today_lock_statistics_left).setOnClickListener(v -> rcvTodayLockStatistics.scrollBy(todayLockStatisticsMoveLeftDistance,0));
        mView.findViewById(R.id.iv_today_lock_statistics_right).setOnClickListener(v -> rcvTodayLockStatistics.scrollBy(-todayLockStatisticsMoveRightDistance,0));
        initView();
        initDevices();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mDevices.isEmpty() && wifiLockInfo != null ) {
            mPresenter.getDoorLockDtatisticsDay(wifiLockInfo.getUid(),wifiLockInfo.getWifiSN());
            mPresenter.getDoorLockDtatisticsSevenDay(wifiLockInfo.getUid(),wifiLockInfo.getWifiSN());
        }
    }

    @Override
    protected DoorLockMessageFragmentPresenter<IDoorLockMessageView> createPresent() {
        return new DoorLockMessageFragmentPresenter<>();
    }

    private void initView() {
        for (int i = 0; i < 7; i++) {
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
        rcvVideoLockMsg.addItemDecoration(new SpacesItemDecoration(itemDecorationLeft, 0, 0, 0));
        rcvVideoLockMsg.setLayoutManager(horizontalLayoutManager);
        rcvVideoLockMsg.setAdapter(videoLockWarningInformAdapter);
        if (wifiVideoLockAlarmRecordData.size() < 5) {
            ivVideoLockMsgLeft.setVisibility(View.INVISIBLE);
            ivVideoLockMsgRight.setVisibility(View.INVISIBLE);
        }
        rcvVideoLockMsg.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (wifiVideoLockAlarmRecordData.size() < 5) return;
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //找到即将移出屏幕Item的position
                int position = layoutManager.findFirstVisibleItemPosition();
                //根据position找到这个Item
                View firstVisiableChildView = layoutManager.findViewByPosition(position);
                //获取Item的宽
                int itemWidth = firstVisiableChildView.getWidth();
                //算出该Item还未移出屏幕的高度
                int itemRight = firstVisiableChildView.getRight();
                earlyWarningMsgMoveLeftDistance = itemWidth + itemDecorationLeft;
                earlyWarningMsgMoveRightDistance = itemWidth + itemDecorationLeft;
                if (itemWidth > itemRight) {
                    earlyWarningMsgMoveRightDistance = earlyWarningMsgMoveRightDistance + (itemWidth - itemRight) + itemDecorationLeft;
                    earlyWarningMsgMoveLeftDistance = earlyWarningMsgMoveLeftDistance + itemRight + itemDecorationLeft;
                }
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    ivVideoLockMsgLeft.setVisibility(View.VISIBLE);
                    ivVideoLockMsgRight.setVisibility(View.VISIBLE);
                    ivVideoLockMsgLeft.setImageResource(R.drawable.philips_icon_more_left_selected_01);
                    ivVideoLockMsgRight.setImageResource(R.drawable.philips_icon_more_right_default_01);
                } else if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    ivVideoLockMsgRight.setVisibility(View.VISIBLE);
                    ivVideoLockMsgLeft.setImageResource(R.drawable.philips_icon_more_left_default_01);
                    ivVideoLockMsgRight.setImageResource(R.drawable.philips_icon_more_right_selected_01);
                } else {
                    ivVideoLockMsgRight.setVisibility(View.VISIBLE);
                    ivVideoLockMsgRight.setImageResource(R.drawable.philips_icon_more_right_selected_01);
                    ivVideoLockMsgLeft.setImageResource(R.drawable.philips_icon_more_left_selected_01);
                }
            }
        });

        TodayLockStatisticsBean todayLockStatisticsBean = new TodayLockStatisticsBean();
        todayLockStatisticsBean.setStatisticsType(1);
        todayLockStatisticsBean.setStatisticsCount(0);
        TodayLockStatisticsData.add(todayLockStatisticsBean);

        TodayLockStatisticsBean todayLockStatisticsBean1 = new TodayLockStatisticsBean();
        todayLockStatisticsBean1.setStatisticsType(2);
        todayLockStatisticsBean1.setStatisticsCount(0);
        TodayLockStatisticsData.add(todayLockStatisticsBean1);

        TodayLockStatisticsBean todayLockStatisticsBean2 = new TodayLockStatisticsBean();
        todayLockStatisticsBean2.setStatisticsType(3);
        todayLockStatisticsBean2.setStatisticsCount(0);
        TodayLockStatisticsData.add(todayLockStatisticsBean2);

        TodayLockStatisticsBean todayLockStatisticsBean3 = new TodayLockStatisticsBean();
        todayLockStatisticsBean3.setStatisticsType(4);
        todayLockStatisticsBean3.setStatisticsCount(0);
        TodayLockStatisticsData.add(todayLockStatisticsBean3);

        TodayLockStatisticsBean todayLockStatisticsBean4 = new TodayLockStatisticsBean();
        todayLockStatisticsBean4.setStatisticsType(3);
        todayLockStatisticsBean4.setStatisticsCount(0);
        TodayLockStatisticsData.add(todayLockStatisticsBean4);

        lockStatisticsAdapter = new PhilipsTodayLockStatisticsAdapter(TodayLockStatisticsData);
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getContext());
        horizontalLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvTodayLockStatistics.addItemDecoration(new SpacesItemDecoration(itemDecorationLeft, 0, 0, 0));
        rcvTodayLockStatistics.setLayoutManager(horizontalLayoutManager1);
        rcvTodayLockStatistics.setAdapter(lockStatisticsAdapter);
        if (TodayLockStatisticsData.size() < 5) {
            ivTodayLockStatisticsLeft.setVisibility(View.INVISIBLE);
            ivTodayLockStatisticsRight.setVisibility(View.INVISIBLE);
        }
        rcvTodayLockStatistics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (TodayLockStatisticsData.size() < 5) return;
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                //找到即将移出屏幕Item的position
                int position = layoutManager.findFirstVisibleItemPosition();
                //根据position找到这个Item
                View firstVisiableChildView = layoutManager.findViewByPosition(position);
                //获取Item的宽
                int itemWidth = firstVisiableChildView.getWidth();
                //算出该Item还未移出屏幕的高度
                int itemRight = firstVisiableChildView.getRight();
                todayLockStatisticsMoveLeftDistance = itemWidth + itemDecorationLeft;
                todayLockStatisticsMoveRightDistance = itemWidth + itemDecorationLeft;
                if (itemWidth > itemRight) {
                    todayLockStatisticsMoveRightDistance = todayLockStatisticsMoveRightDistance + (itemWidth - itemRight) + itemDecorationLeft;
                    todayLockStatisticsMoveLeftDistance = todayLockStatisticsMoveLeftDistance + itemRight + itemDecorationLeft;
                }
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    ivTodayLockStatisticsLeft.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsRight.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsLeft.setImageResource(R.drawable.philips_icon_more_left_selected_02);
                    ivTodayLockStatisticsRight.setImageResource(R.drawable.philips_icon_more_right_default_02);
                } else if (lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1) {
                    ivTodayLockStatisticsLeft.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsRight.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsLeft.setImageResource(R.drawable.philips_icon_more_left_default_02);
                    ivTodayLockStatisticsRight.setImageResource(R.drawable.philips_icon_more_right_selected_02);
                } else {
                    ivTodayLockStatisticsLeft.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsRight.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsRight.setImageResource(R.drawable.philips_icon_more_right_selected_02);
                    ivTodayLockStatisticsLeft.setImageResource(R.drawable.philips_icon_more_left_selected_02);
                }

            }
        });

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

    private void initDevices() {
        mDevices.clear();
        mDevices.addAll(MyApplication.getInstance().getHomeShowDevices());
        if (mDevices.isEmpty()) {
            tvNoMessage.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            llDeviceType.setVisibility(View.GONE);
        } else {
            tvNoMessage.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            llDeviceType.setVisibility(View.VISIBLE);
            refreshLayoutData((WifiLockInfo)mDevices.get(0).getObject());
        }
    }

    private void refreshLayoutData(WifiLockInfo mWifiLockInfo) {
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(mWifiLockInfo.getWifiSN());
        long createTime2 = wifiLockInfo.getCreateTime();

        if (createTime2 == 0) {
            createTime.setText("0");
        } else {
            long currentTimeMillis = System.currentTimeMillis();
            long day = ((currentTimeMillis / 1000) - createTime2) / (60 * 24 * 60);
            if (day < 0) {
                day = 0;
            }
            createTime.setText(day + "");
        }
        //WiFi信息并展示
        int count = (int) SPUtils.get(KeyConstants.WIFI_LOCK_OPEN_COUNT + wifiLockInfo.getWifiSN(), 0);
        tvOpenLockTimes.setText("" + count);
        tvLockName.setText(mWifiLockInfo.getLockNickname());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            HomeShowBean homeShowBean = (HomeShowBean) data.getSerializableExtra("homeShowBean");
            refreshLayoutData((WifiLockInfo)homeShowBean.getObject());
        }
    }

    @Override
    public void onDeviceRefresh(AllBindDevices allBindDevices) {
        initDevices();
    }

    @Override
    public void onWifiLockActionUpdate() {
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiLockInfo.getWifiSN());
        refreshLayoutData(wifiLockInfo);
    }

    @Override
    public void getDtatisticsDay(GetStatisticsDayResult getStatisticsDayResult) {
        tvOpenLockNumber.setText(getStatisticsDayResult.getData().getAllCount());
        LogUtils.d("获取门锁当天记录  数据是  " + getStatisticsDayResult.toString());
    }

    @Override
    public void getDtatisticsSevenDay(GetStatisticsSevenDayResult getStatisticsSevenDayResult) {
        LogUtils.d("获取门锁七天记录  数据是  " + getStatisticsSevenDayResult.toString());
    }

}
