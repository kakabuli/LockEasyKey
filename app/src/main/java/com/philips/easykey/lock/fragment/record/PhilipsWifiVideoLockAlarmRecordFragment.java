package com.philips.easykey.lock.fragment.record;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockAlbumDetailActivity;
import com.philips.easykey.lock.adapter.PhilipsWifiVideoLockAlarmIAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockAlarmRecordPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IWifiVideoLockAlarmRecordView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


import com.philips.easykey.core.tool.FileTool;


public class PhilipsWifiVideoLockAlarmRecordFragment extends BaseFragment<IWifiVideoLockAlarmRecordView, WifiVideoLockAlarmRecordPresenter<IWifiVideoLockAlarmRecordView>>
        implements IWifiVideoLockAlarmRecordView {

    RecyclerView recycleview;
    SmartRefreshLayout refreshLayout;
    PhilipsWifiVideoLockAlarmIAdapter wifiLockAlarmGroupRecordAdapter;
    RelativeLayout rlHead;
    TextView tvNoMore;
    private int currentPage = 1;   //当前的开锁记录时间
    View view;
    private String wifiSn;
    List<WifiVideoLockAlarmRecord> list = new ArrayList<>();
    private WifiLockInfo wifiLockInfoBySn;
    private String startTime = " 00:00:00";
    private String endTime = " 23:59:59";
    private String screenedDate;
    private boolean isScreenedMode = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.philips_fragment_bluetooth_open_lock_record, null);

        recycleview = view.findViewById(R.id.recycleview);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        rlHead = view.findViewById(R.id.rl_head);
        tvNoMore = view.findViewById(R.id.tv_no_more);

        wifiSn = getArguments().getString(KeyConstants.WIFI_SN);
        wifiLockInfoBySn = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        initRecycleView();
        initRefresh();
        initData();
        rlHead.setVisibility(View.GONE);
        return view;
    }

    private void initData() {
        String alarmCache = (String) SPUtils.get(KeyConstants.WIFI_VIDEO_LOCK_ALARM_RECORD + wifiSn, "");
        Gson gson = new Gson();
        List<WifiVideoLockAlarmRecord> records = gson.fromJson(alarmCache, new TypeToken<List<WifiVideoLockAlarmRecord>>() {
        }.getType());
        groupData(records);
        wifiLockAlarmGroupRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getWifiVideoLockGetAlarmList(1, wifiSn);
    }

    private void initRecycleView() {
        wifiLockAlarmGroupRecordAdapter = new PhilipsWifiVideoLockAlarmIAdapter(list, new PhilipsWifiVideoLockAlarmIAdapter.VideoRecordCallBackLinstener() {
            @Override
            public void onVideoRecordCallBackLinstener(WifiVideoLockAlarmRecord record) {
                if(wifiLockInfoBySn.getPowerSave() == 1){
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
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setAdapter(wifiLockAlarmGroupRecordAdapter);


    }

    public void getWifiVideoLockGetAlarmFilterList(boolean isScreenedMode ,String screenedDate){
        this.isScreenedMode = isScreenedMode;
        this.screenedDate = screenedDate;
        mPresenter.getWifiVideoLockGetAlarmFilterList(1, wifiSn,DateUtils.dateChangeTimestamp(screenedDate + startTime)
                ,DateUtils.dateChangeTimestamp(screenedDate + endTime));
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(refreshlayout -> {
            refreshlayout.setEnableLoadMore(true);
            if(isScreenedMode && !TextUtils.isEmpty(screenedDate)){
                mPresenter.getWifiVideoLockGetAlarmFilterList(1, wifiSn,DateUtils.dateChangeTimestamp(screenedDate + startTime)
                        ,DateUtils.dateChangeTimestamp(screenedDate + endTime));
            }else {
                mPresenter.getWifiVideoLockGetAlarmList(1, wifiSn);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if(isScreenedMode && !TextUtils.isEmpty(screenedDate)){
                    mPresenter.getWifiVideoLockGetAlarmFilterList(currentPage, wifiSn,DateUtils.dateChangeTimestamp(screenedDate + startTime)
                            ,DateUtils.dateChangeTimestamp(screenedDate + endTime));
                }else {
                    mPresenter.getWifiVideoLockGetAlarmList(currentPage, wifiSn);
                }
            }
        });
    }

    @Override
    protected WifiVideoLockAlarmRecordPresenter<IWifiVideoLockAlarmRecordView> createPresent() {
        return new WifiVideoLockAlarmRecordPresenter<>();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ((ViewGroup) view.getParent()).removeView(view);
    }


    @Override
    public void onLoadServerRecord(List<WifiVideoLockAlarmRecord> alarmRecords, int page) {
        tvNoMore.setVisibility(View.GONE);
        recycleview.setVisibility(View.VISIBLE);
        if (page == 1) {
            list.clear();
        }
        int size = list.size();
        currentPage = page + 1;
        groupData(alarmRecords);

        if (size>0){
            wifiLockAlarmGroupRecordAdapter.notifyItemRangeInserted(size,alarmRecords.size());
        }else {
            wifiLockAlarmGroupRecordAdapter.notifyDataSetChanged();
        }

        if (page == 1) { //这时候是刷新
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(true);
        } else {
            refreshLayout.finishLoadMore();
        }

    }

    private void groupData(List<WifiVideoLockAlarmRecord> lockRecords) {
        String lastTimeHead = "";
        WifiVideoLockAlarmRecord lastRecord = null;
        if (lockRecords != null && lockRecords.size() > 0) {
            for (int i = 0; i < lockRecords.size(); i++) {
                if (list.size() > 0) {
                    lastRecord = list.get(list.size() - 1);
                    lastTimeHead = lastRecord.getDayTime();
                }
                WifiVideoLockAlarmRecord record = lockRecords.get(i);
                boolean falg = false;
                for(WifiVideoLockAlarmRecord li : list){
                    if(li.get_id() != null && record.get_id() != null
                            && li.get_id().equals(record.get_id()) ){
                        falg = true;
                        break;
                    }
                }
                if(falg){
                    continue;
                }
                long openTime = 0;
                try{
                    openTime = Long.parseLong(record.getTime());
                }catch (Exception re){
                    re.printStackTrace();
                }

                String sOpenTime = DateUtils.getDateTimeFromMillisecond(openTime * 1000);
                String timeHead = sOpenTime.substring(0, 10);
                record.setDayTime(timeHead);
                if (!timeHead.equals(lastTimeHead)) { //添加头
                    record.setFirst(true);
                    if (lastRecord != null) {
                        lastRecord.setLast(true);
                    }
                }
                list.add(record);

            }
        }
    }

    @Override
    public void onLoadServerRecordFailed(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(getActivity(), throwable));
        //加载服务器开锁记录失败
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onLoadServerRecordFailedServer(BaseResult result) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(getActivity(), result.getCode()));
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onServerNoData() {
        //服务器没有开锁记录
        refreshLayout.finishRefresh();
        refreshLayout.setEnableLoadMore(false);  //服务器没有数据时，不让上拉加载更多
        tvNoMore.setVisibility(View.VISIBLE);
        ToastUtils.showShort(R.string.server_no_data_2);
        recycleview.setVisibility(View.GONE);
    }

    @Override
    public void noMoreData() {
        ToastUtils.showShort(R.string.no_more_data);
        refreshLayout.finishLoadMore();
        refreshLayout.setEnableLoadMore(false);
    }

    public void powerStatusDialog(){
        AlertDialogUtil.getInstance().PhilipsSingleButtonDialog(getActivity(), getString(R.string.philips_deviceinfo__power_save_mode),"",
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
