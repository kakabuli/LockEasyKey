package com.philips.easykey.lock.fragment.message;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.philips.easykey.core.tool.FileTool;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockAlbumDetailActivity;
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
import com.philips.easykey.lock.publiclibrary.http.result.GetWifiVideoLockAlarmScreenedRecordResult;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.AllBindDevices;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.widget.SpacesItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class PhilipsDoorLockMessageFragment extends BaseFragment<IDoorLockMessageView, DoorLockMessageFragmentPresenter<IDoorLockMessageView>> implements IDoorLockMessageView {

    RecyclerView rcvVideoLockMsg;
    RecyclerView rcvTodayLockStatistics;
    RecyclerView rcvSevenDayDataStatistics;
    TextView tvLockName;
    LinearLayout llVideoLockMsg;
    LinearLayout llSevenDayStatistics;
    ImageView ivVideoLockMsgLeft;
    ImageView ivVideoLockMsgRight;
    ImageView ivTodayLockStatisticsLeft;
    ImageView ivTodayLockStatisticsRight;
    TextView tvOpenLockNumber;
    TextView tvNoMessage;
    TextView tvNoAlarm;
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
    private List<TodayLockStatisticsBean> todayLockStatisticsData = new ArrayList<>();
    private List<SevendayDataStatisticsBean> sevendayDataStatisticsData = new ArrayList<>();
    private int earlyWarningMsgMoveLeftDistance = 0;
    private int earlyWarningMsgMoveRightDistance = 0;
    private int todayLockStatisticsMoveLeftDistance = 0;
    private int todayLockStatisticsMoveRightDistance = 0;
    private int itemDecorationLeft = SizeUtils.dp2px(4);
    private final List<HomeShowBean> mDevices = new ArrayList<>();
    private String wifiSn;
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
        llSevenDayStatistics = mView.findViewById(R.id.llSevenDayStatistics);
        ivVideoLockMsgLeft = mView.findViewById(R.id.iv_video_lock_msg_left);
        ivVideoLockMsgRight = mView.findViewById(R.id.iv_video_lock_msg_right);
        tvOpenLockNumber = mView.findViewById(R.id.tv_open_lock_number);
        ivTodayLockStatisticsLeft = mView.findViewById(R.id.iv_today_lock_statistics_left);
        ivTodayLockStatisticsRight = mView.findViewById(R.id.iv_today_lock_statistics_right);
        tvNoAlarm = mView.findViewById(R.id.tv_no_alarm);
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
        /*if (!mDevices.isEmpty() && wifiLockInfo != null ) {
            mPresenter.getDoorLockDtatisticsDay(wifiLockInfo.getUid(),wifiSn);
            mPresenter.getDoorLockDtatisticsSevenDay(wifiLockInfo.getUid(),wifiSn);
            long timeMillis = System.currentTimeMillis();
            String date = DateUtils.getDayTimeFromMillisecond(timeMillis);
            mPresenter.getWifiVideoLockGetAlarmFilterList(1,wifiSn,DateUtils.dateChangeTimestamp(date + " 00:00:00"),DateUtils.dateChangeTimestamp(date + " 23:59:59"));
        }else {
            rcvVideoLockMsg.setVisibility(View.GONE);
            tvNoAlarm.setVisibility(View.VISIBLE);
            ivVideoLockMsgLeft.setVisibility(View.INVISIBLE);
            ivVideoLockMsgRight.setVisibility(View.INVISIBLE);
        }*/
    }

    @Override
    protected DoorLockMessageFragmentPresenter<IDoorLockMessageView> createPresent() {
        return new DoorLockMessageFragmentPresenter<>();
    }

    private void initView() {
        if(videoLockWarningInformAdapter == null){
            videoLockWarningInformAdapter = new PhilipsVideoLockWarningInformAdapter( new PhilipsVideoLockWarningInformAdapter.VideoLockWarningCallBackLinstener() {
                @Override
                public void onVideoLockWarningCallBackLinstener(WifiVideoLockAlarmRecord record) {
                    if(wifiLockInfo.getPowerSave() == 1){
                        powerStatusDialog();
                        return;
                    }
                    getActivity().runOnUiThread(() -> {
                        String path = FileTool.getVideoCacheFolder(getActivity(),record.getWifiSN()).getPath();
                        String fileName = path +  File.separator + record.get_id() + ".mp4";
                        if (new File(fileName).exists()){
                            Intent intent = new Intent(getActivity(), PhilipsWifiVideoLockAlbumDetailActivity.class);
                            intent.putExtra(KeyConstants.VIDO_SHOW_DELETE,1);
                            intent.putExtra(KeyConstants.VIDEO_PIC_PATH,fileName);
                            try{
                                fileName = DateUtils.getStrFromMillisecond2(record.getStartTime() - 28800000);
                            }catch (Exception e){
                            }
                            intent.putExtra("NAME",fileName);
                            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
                            intent.putExtra("record",record);
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(getActivity(), PhilipsWifiVideoLockAlbumDetailActivity.class);
                            intent.putExtra(KeyConstants.VIDEO_PIC_PATH,fileName);
                            intent.putExtra(KeyConstants.VIDO_SHOW_DELETE,1);
                            try {
                                fileName = DateUtils.getStrFromMillisecond2(record.getStartTime() - 28800000);
                            }catch (Exception e){
                            }
                            intent.putExtra("NAME",fileName);
                            intent.putExtra(KeyConstants.WIFI_SN,wifiSn);
                            intent.putExtra("record",record);
                            startActivity(intent);
                        }
                    });
                }
            });
        }
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getContext());
        horizontalLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvVideoLockMsg.setLayoutManager(horizontalLayoutManager);
        rcvVideoLockMsg.setAdapter(videoLockWarningInformAdapter);
        rcvVideoLockMsg.setFocusable(false);
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

        if(lockStatisticsAdapter == null){
            lockStatisticsAdapter = new PhilipsTodayLockStatisticsAdapter();
        }
        LinearLayoutManager horizontalLayoutManager1 = new LinearLayoutManager(getContext());
        horizontalLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvTodayLockStatistics.addItemDecoration(new SpacesItemDecoration(itemDecorationLeft, 0, 0, 0));
        rcvTodayLockStatistics.setLayoutManager(horizontalLayoutManager1);
        rcvTodayLockStatistics.setAdapter(lockStatisticsAdapter);
        rcvTodayLockStatistics.setFocusable(false);
        refreshDtatisticsDayLayoutData(null);
        rcvTodayLockStatistics.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (todayLockStatisticsData.size() < 5) return;
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
        if(sevendayDataStatisticsAdapter == null){
            sevendayDataStatisticsAdapter = new PhilipsSevenDayDataStatisticsAdapter();
        }
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        verticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcvSevenDayDataStatistics.setLayoutManager(verticalLayoutManager);
        rcvSevenDayDataStatistics.setAdapter(sevendayDataStatisticsAdapter);
        rcvSevenDayDataStatistics.setFocusable(false);
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
            if(TextUtils.isEmpty(wifiSn)){
                refreshLayoutData((WifiLockInfo)mDevices.get(0).getObject());
            }else {
                refreshLayoutData(MyApplication.getInstance().getWifiLockInfoBySn(wifiSn));
            }
        }
    }

    private void refreshLayoutData(WifiLockInfo mWifiLockInfo) {
        wifiLockInfo = mWifiLockInfo;
        wifiSn = wifiLockInfo.getWifiSN();
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
        if(!TextUtils.isEmpty(mWifiLockInfo.getLockNickname())){
            if(mWifiLockInfo.getLockNickname().length() > 10){
                tvLockName.setText(mWifiLockInfo.getLockNickname().substring(0,10) + "...");
            }else {
                tvLockName.setText(mWifiLockInfo.getLockNickname());
            }
        }
        mPresenter.getOpenCount(wifiSn);
        mPresenter.getDoorLockDtatisticsDay(wifiLockInfo.getUid(),wifiSn);
        mPresenter.getDoorLockDtatisticsSevenDay(wifiLockInfo.getUid(),wifiSn);
        long timeMillis = System.currentTimeMillis();
        String date = DateUtils.getDayTimeFromMillisecond(timeMillis);
        mPresenter.getWifiVideoLockGetAlarmFilterList(1,wifiSn,DateUtils.dateChangeTimestamp(date + " 00:00:00"),DateUtils.dateChangeTimestamp(date + " 23:59:59"));
    }

    private void refreshAlarmRecordLayoutData(List<WifiVideoLockAlarmRecord> data){
        wifiVideoLockAlarmRecordData.clear();
        if(data != null && data.size() > 0){
            for(int i = 0 ; i < data.size() ; i ++ ){
                if(!TextUtils.isEmpty(data.get(i).getThumbUrl())){
                    wifiVideoLockAlarmRecordData.add(data.get(i));
                }
            }
        }
        if(wifiVideoLockAlarmRecordData == null){
            rcvVideoLockMsg.setVisibility(View.GONE);
            tvNoAlarm.setVisibility(View.VISIBLE);
            ivVideoLockMsgLeft.setVisibility(View.INVISIBLE);
            ivVideoLockMsgRight.setVisibility(View.INVISIBLE);
            return;
        }
        if(wifiVideoLockAlarmRecordData.size() == 0){
            rcvVideoLockMsg.setVisibility(View.GONE);
            tvNoAlarm.setVisibility(View.VISIBLE);
            ivVideoLockMsgLeft.setVisibility(View.INVISIBLE);
            ivVideoLockMsgRight.setVisibility(View.INVISIBLE);
        }else if (wifiVideoLockAlarmRecordData.size() > 0 && wifiVideoLockAlarmRecordData.size() < 5) {
            tvNoAlarm.setVisibility(View.GONE);
            ivVideoLockMsgLeft.setVisibility(View.INVISIBLE);
            ivVideoLockMsgRight.setVisibility(View.INVISIBLE);
            rcvVideoLockMsg.setVisibility(View.VISIBLE);
            videoLockWarningInformAdapter.setList(wifiVideoLockAlarmRecordData);
            videoLockWarningInformAdapter.notifyDataSetChanged();
        }else if(wifiVideoLockAlarmRecordData.size() >= 5){
            tvNoAlarm.setVisibility(View.GONE);
            ivVideoLockMsgLeft.setVisibility(View.VISIBLE);
            ivVideoLockMsgRight.setVisibility(View.VISIBLE);
            rcvVideoLockMsg.setVisibility(View.VISIBLE);
            videoLockWarningInformAdapter.setList(wifiVideoLockAlarmRecordData);
            videoLockWarningInformAdapter.notifyDataSetChanged();
        }
    }

    private void refreshDtatisticsDayLayoutData(GetStatisticsDayResult statisticsDayResult){
        todayLockStatisticsData.clear();
        tvOpenLockNumber.setText(statisticsDayResult == null ? "0" : statisticsDayResult.getData().getAllCount() + "");
        for(int i = 0 ; i < 4 ; i ++){
            TodayLockStatisticsBean todayLockStatisticsBean = new TodayLockStatisticsBean();
            if(i == 0){
                todayLockStatisticsBean.setStatisticsType(1);
                todayLockStatisticsBean.setStatisticsCount(statisticsDayResult == null ? 0 : statisticsDayResult.getData().getDoorbellCount());
            }else if(i == 1){
                todayLockStatisticsBean.setStatisticsType(2);
                todayLockStatisticsBean.setStatisticsCount(statisticsDayResult == null ? 0 : statisticsDayResult.getData().getFingerprintOpenLockCount());
            }else if(i == 2){
                todayLockStatisticsBean.setStatisticsType(3);
                todayLockStatisticsBean.setStatisticsCount(statisticsDayResult == null ? 0 : statisticsDayResult.getData().getPwdOpenLockCount());
            }else if(i == 3){
                todayLockStatisticsBean.setStatisticsType(4);
                todayLockStatisticsBean.setStatisticsCount(statisticsDayResult == null ? 0 : statisticsDayResult.getData().getCardOpenLockCount());
            }
            todayLockStatisticsData.add(todayLockStatisticsBean);
        }
        lockStatisticsAdapter.setList(todayLockStatisticsData);
        lockStatisticsAdapter.notifyDataSetChanged();
        if (todayLockStatisticsData.size() < 5) {
            ivTodayLockStatisticsLeft.setVisibility(View.INVISIBLE);
            ivTodayLockStatisticsRight.setVisibility(View.INVISIBLE);
        }
    }

    private void refreshDtatisticsSevenDayLayoutData(GetStatisticsSevenDayResult statisticsSevenDayResult){
        sevendayDataStatisticsData.clear();
        for (int i = 0; i < 3; i++) {
            SevendayDataStatisticsBean sevendayDataStatisticsBean = new SevendayDataStatisticsBean();
            if(i == 0) {
                sevendayDataStatisticsBean.setOrdinateValue(new int[]{
                        statisticsSevenDayResult.getData().getStatisticsList().get(6).getOperationCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(5).getOperationCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(4).getOperationCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(3).getOperationCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(2).getOperationCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(1).getOperationCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(0).getOperationCount()});
                sevendayDataStatisticsBean.setStatisticsTypeName(getString(R.string.philips_open_the_door_record));
            }else if(i == 1){
                sevendayDataStatisticsBean.setOrdinateValue(new int[]{
                        statisticsSevenDayResult.getData().getStatisticsList().get(6).getDoorbellCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(5).getDoorbellCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(4).getDoorbellCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(3).getDoorbellCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(2).getDoorbellCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(1).getDoorbellCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(0).getDoorbellCount()});
                sevendayDataStatisticsBean.setStatisticsTypeName(getString(R.string.philips_visitor_record));
            }else if(i == 2){
                sevendayDataStatisticsBean.setOrdinateValue(new int[]{
                        statisticsSevenDayResult.getData().getStatisticsList().get(6).getAlarmCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(5).getAlarmCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(4).getAlarmCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(3).getAlarmCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(2).getAlarmCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(1).getAlarmCount(),
                        statisticsSevenDayResult.getData().getStatisticsList().get(0).getAlarmCount()});
                sevendayDataStatisticsBean.setStatisticsTypeName(getString(R.string.warn_information));
            }
            sevendayDataStatisticsBean.setTransverseValue(new String[]{ // TODO: 2021/7/17 这里数据处理有点坑，服务器返回的数据格式跟ui要求有出入，只有二次封装
                    statisticsSevenDayResult.getData().getStatisticsList().get(6).getDate().replace("-",".").substring(5),
                    statisticsSevenDayResult.getData().getStatisticsList().get(5).getDate().replace("-",".").substring(5),
                    statisticsSevenDayResult.getData().getStatisticsList().get(4).getDate().replace("-",".").substring(5),
                    statisticsSevenDayResult.getData().getStatisticsList().get(3).getDate().replace("-",".").substring(5),
                    statisticsSevenDayResult.getData().getStatisticsList().get(2).getDate().replace("-",".").substring(5),
                    statisticsSevenDayResult.getData().getStatisticsList().get(1).getDate().replace("-",".").substring(5),
                    statisticsSevenDayResult.getData().getStatisticsList().get(0).getDate().replace("-",".").substring(5)});
            sevendayDataStatisticsData.add(sevendayDataStatisticsBean);
        }
        sevendayDataStatisticsAdapter.setList(sevendayDataStatisticsData);
        sevendayDataStatisticsAdapter.notifyDataSetChanged();
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
    public void getOpenCountSuccess(int count) {
        tvOpenLockTimes.setText("" + count);
    }

    @Override
    public void getDtatisticsDay(GetStatisticsDayResult getStatisticsDayResult) {
        refreshDtatisticsDayLayoutData(getStatisticsDayResult);
        LogUtils.d("获取门锁当天记录  数据是  " + getStatisticsDayResult.toString());
    }

    @Override
    public void getDtatisticsSevenDay(GetStatisticsSevenDayResult getStatisticsSevenDayResult) {
        LogUtils.d("获取门锁七天记录  数据是  " + getStatisticsSevenDayResult.toString());
        llSevenDayStatistics.setVisibility(View.VISIBLE);
        refreshDtatisticsSevenDayLayoutData(getStatisticsSevenDayResult);
    }

    @Override
    public void getDtatisticsSevenDayFailed() {
        llSevenDayStatistics.setVisibility(View.GONE);
    }

    @Override
    public void getWifiVideoLockAlarm(GetWifiVideoLockAlarmScreenedRecordResult getWifiVideoLockAlarmScreenedRecordResult) {
        LogUtils.d("获取当天预计视频 数据是  " + getWifiVideoLockAlarmScreenedRecordResult.toString());
        llVideoLockMsg.setVisibility(View.VISIBLE);
        List<WifiVideoLockAlarmRecord> alarmRecords = getWifiVideoLockAlarmScreenedRecordResult.getData().getAlarmList();
        refreshAlarmRecordLayoutData(alarmRecords);
    }

    @Override
    public void getWifiVideoLockAlarmFailed() {
        llVideoLockMsg.setVisibility(View.GONE);
    }

    public void powerStatusDialog(){
        AlertDialogUtil.getInstance().PhilipsSingleButtonDialog(getActivity(), getString(R.string.philips_closed_video_mode_title),
                getString(R.string.philips_closed_video_mode_content),
                getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

}
