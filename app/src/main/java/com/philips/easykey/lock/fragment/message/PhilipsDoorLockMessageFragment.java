package com.philips.easykey.lock.fragment.message;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
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
    @BindView(R.id.iv_video_lock_msg_left)
    ImageView ivVideoLockMsgLeft;
    @BindView(R.id.iv_video_lock_msg_right)
    ImageView ivVideoLockMsgRight;
    @BindView(R.id.iv_today_lock_statistics_left)
    ImageView ivTodayLockStatisticsLeft;
    @BindView(R.id.iv_today_lock_statistics_right)
    ImageView ivTodayLockStatisticsRight;
    private PhilipsVideoLockWarningInformAdapter videoLockWarningInformAdapter;
    private PhilipsTodayLockStatisticsAdapter lockStatisticsAdapter;
    private PhilipsSevenDayDataStatisticsAdapter sevendayDataStatisticsAdapter;
    private int RESULT_OK = 100;
    private View mView;
    private Unbinder unbinder;
    private List<WifiVideoLockAlarmRecord> wifiVideoLockAlarmRecordData = new ArrayList<>();
    private List<TodayLockStatisticsBean> TodayLockStatisticsData = new ArrayList<>();
    private List<SevendayDataStatisticsBean> sevendayDataStatisticsData = new ArrayList<>();
    private int earlyWarningMsgMoveLeftDistance = 0;
    private int earlyWarningMsgMoveRightDistance = 0;
    private int todayLockStatisticsMoveLeftDistance = 0;
    private int todayLockStatisticsMoveRightDistance = 0;
    private int itemDecorationLeft = SizeUtils.dp2px(4);

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
        if(wifiVideoLockAlarmRecordData.size() < 5){
            ivVideoLockMsgLeft.setVisibility(View.INVISIBLE);
            ivVideoLockMsgRight.setVisibility(View.INVISIBLE);
        }
        rcvVideoLockMsg.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(wifiVideoLockAlarmRecordData.size() < 5)return;
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
                earlyWarningMsgMoveRightDistance = itemWidth  + itemDecorationLeft;
                if(itemWidth > itemRight){
                    earlyWarningMsgMoveRightDistance = earlyWarningMsgMoveRightDistance + (itemWidth - itemRight) + itemDecorationLeft;
                    earlyWarningMsgMoveLeftDistance = earlyWarningMsgMoveLeftDistance + itemRight + itemDecorationLeft;
                }
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0){
                    ivVideoLockMsgLeft.setVisibility(View.VISIBLE);
                    ivVideoLockMsgRight.setVisibility(View.VISIBLE);
                    ivVideoLockMsgLeft.setImageResource(R.drawable.philips_icon_more_left_selected_01);
                    ivVideoLockMsgRight.setImageResource(R.drawable.philips_icon_more_right_default_01);
                }else if(lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1){
                    ivVideoLockMsgRight.setVisibility(View.VISIBLE);
                    ivVideoLockMsgLeft.setImageResource(R.drawable.philips_icon_more_left_default_01);
                    ivVideoLockMsgRight.setImageResource(R.drawable.philips_icon_more_right_selected_01);
                }else {
                    ivVideoLockMsgRight.setVisibility(View.VISIBLE);
                    ivVideoLockMsgRight.setImageResource(R.drawable.philips_icon_more_right_selected_01);
                    ivVideoLockMsgLeft.setImageResource(R.drawable.philips_icon_more_left_selected_01);
                }
            }
        });

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
        rcvTodayLockStatistics.addItemDecoration(new SpacesItemDecoration(itemDecorationLeft, 0, 0, 0));
        rcvTodayLockStatistics.setLayoutManager(horizontalLayoutManager1);
        rcvTodayLockStatistics.setAdapter(lockStatisticsAdapter);
        if(TodayLockStatisticsData.size() < 5){
            ivTodayLockStatisticsLeft.setVisibility(View.INVISIBLE);
            ivTodayLockStatisticsRight.setVisibility(View.INVISIBLE);
        }
        rcvTodayLockStatistics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(TodayLockStatisticsData.size() < 5)return;
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
                todayLockStatisticsMoveRightDistance = itemWidth  + itemDecorationLeft;
                if(itemWidth > itemRight){
                    todayLockStatisticsMoveRightDistance = todayLockStatisticsMoveRightDistance + (itemWidth - itemRight) + itemDecorationLeft;
                    todayLockStatisticsMoveLeftDistance = todayLockStatisticsMoveLeftDistance + itemRight + itemDecorationLeft;
                }
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                int lastCompletelyVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0){
                    ivTodayLockStatisticsLeft.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsRight.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsLeft.setImageResource(R.drawable.philips_icon_more_left_selected_02);
                    ivTodayLockStatisticsRight.setImageResource(R.drawable.philips_icon_more_right_default_02);
                }else if(lastCompletelyVisibleItemPosition == layoutManager.getItemCount() - 1){
                    ivTodayLockStatisticsLeft.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsRight.setVisibility(View.VISIBLE);
                    ivTodayLockStatisticsLeft.setImageResource(R.drawable.philips_icon_more_left_default_02);
                    ivTodayLockStatisticsRight.setImageResource(R.drawable.philips_icon_more_right_selected_02);
                }else {
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

    private void refreshLayoutData(HomeShowBean homeShowBean) {
        tvLockName.setText(homeShowBean.getDeviceNickName());
        if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_WIFI_LOCK) {
            llVideoLockMsg.setVisibility(View.GONE);
        } else if (homeShowBean.getDeviceType() == HomeShowBean.TYPE_WIFI_VIDEO_LOCK) {
            llVideoLockMsg.setVisibility(View.VISIBLE);
        }
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

    @OnClick({R.id.ll_device_type,R.id.iv_video_lock_msg_left, R.id.iv_video_lock_msg_right, R.id.iv_today_lock_statistics_left, R.id.iv_today_lock_statistics_right})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_device_type:
                Intent intent = new Intent(getContext(), PhilipsDeviceSelectDialogActivity.class);
                startActivityForResult(intent, RESULT_OK);
                break;
            case R.id.iv_video_lock_msg_left:
                rcvVideoLockMsg.scrollBy(earlyWarningMsgMoveLeftDistance,0);
                break;
            case R.id.iv_video_lock_msg_right:
                rcvVideoLockMsg.scrollBy(-earlyWarningMsgMoveRightDistance,0);
                break;
            case R.id.iv_today_lock_statistics_left:
                rcvTodayLockStatistics.scrollBy(todayLockStatisticsMoveLeftDistance,0);
                break;
            case R.id.iv_today_lock_statistics_right:
                rcvTodayLockStatistics.scrollBy(-todayLockStatisticsMoveRightDistance,0);
                break;
        }
    }
}
