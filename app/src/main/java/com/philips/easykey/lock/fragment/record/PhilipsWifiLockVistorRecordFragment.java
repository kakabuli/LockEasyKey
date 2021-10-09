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
import com.philips.easykey.lock.adapter.PhilipsWifiLockVistorIAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockVistorRecordPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVistorRecordView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiVideoLockAlarmRecord;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.philips.easykey.core.tool.FileTool;

/**
 * Created by David on 2019/4/22
 */
public class PhilipsWifiLockVistorRecordFragment extends BaseFragment<IWifiLockVistorRecordView, WifiVideoLockVistorRecordPresenter<IWifiLockVistorRecordView>>
        implements IWifiLockVistorRecordView {

    RecyclerView recycleview;
    List<WifiVideoLockAlarmRecord> records = new ArrayList<>();
    PhilipsWifiLockVistorIAdapter operationGroupRecordAdapter;
    SmartRefreshLayout refreshLayout;
    TextView tvSynchronizedRecord;
    RelativeLayout rlHead;
    TextView tvNoMore;

    private int currentPage = 1;   //当前的开锁记录时间
    View view;
    private String wifiSn;

    private WifiLockInfo wifiLockInfoBySn;
    private String startTime = " 00:00:00";
    private String endTime = " 23:59:59";
    private String screenedDate;
    private boolean isScreenedMode = false;//判断是否为筛选消息记录模式

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = View.inflate(getActivity(), R.layout.philips_fragment_bluetooth_open_lock_record, null);

        recycleview = view.findViewById(R.id.recycleview);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        tvSynchronizedRecord = view.findViewById(R.id.tv_synchronized_record);
        rlHead = view.findViewById(R.id.rl_head);
        tvNoMore = view.findViewById(R.id.tv_no_more);

        wifiSn = getArguments().getString(KeyConstants.WIFI_SN);
        wifiLockInfoBySn = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        rlHead.setVisibility(View.GONE);

        initRecycleView();
        initRefresh();
        initData();
        return view;
    }

    private void initData() {
        String localRecord = (String) SPUtils.get(KeyConstants.WIFI_VIDEO_LOCK_VISITOR_RECORD + wifiSn, "");
        Gson gson = new Gson();
        List<WifiVideoLockAlarmRecord> records = gson.fromJson(localRecord, new TypeToken<List<WifiVideoLockAlarmRecord>>() {
        }.getType());
        groupData(records);
        operationGroupRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getWifiVideoLockGetAlarmList(1, wifiSn);
    }


    @Override
    protected WifiVideoLockVistorRecordPresenter<IWifiLockVistorRecordView> createPresent() {
        return new WifiVideoLockVistorRecordPresenter<>();
    }

    private void initRecycleView() {
        operationGroupRecordAdapter = new PhilipsWifiLockVistorIAdapter(records, new PhilipsWifiLockVistorIAdapter.VideoRecordCallBackLinstener() {
            @Override
            public void onVideoRecordCallBackLinstener(WifiVideoLockAlarmRecord record) {

                if(wifiLockInfoBySn.getPowerSave() == 1){
                    powerStatusDialog();
                    return;
                }
                getActivity().runOnUiThread(() -> {
//                        int ret = mPresenter.searchRecordFileList(record.getFileDate());

                    String path = FileTool.getVideoCacheFolder(getActivity(),record.getWifiSN()).getPath();
                    String fileName = path +  File.separator + record.get_id() + ".mp4";
                    if (new File(fileName).exists()){
                        Intent intent = new Intent(getActivity(), PhilipsWifiVideoLockAlbumDetailActivity.class);
                        intent.putExtra(KeyConstants.VIDEO_PIC_PATH,fileName);
                        intent.putExtra(KeyConstants.VIDO_SHOW_DELETE,1);
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
                        try{

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
        recycleview.setAdapter(operationGroupRecordAdapter);
    }

    public void getWifiVideoLockGetDoorbellFilterList(boolean isScreenedMode , String screenedDate){
        this.isScreenedMode = isScreenedMode;
        this.screenedDate = screenedDate;
        mPresenter.getWifiVideoLockGetDoorbellFilterList(1, wifiSn,DateUtils.dateChangeTimestamp(screenedDate + startTime)
                ,DateUtils.dateChangeTimestamp(screenedDate + endTime));
    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.setEnableLoadMore(true);
                if(isScreenedMode && !TextUtils.isEmpty(screenedDate)){
                    mPresenter.getWifiVideoLockGetDoorbellFilterList(1, wifiSn,DateUtils.dateChangeTimestamp(screenedDate + startTime)
                            ,DateUtils.dateChangeTimestamp(screenedDate + endTime));
                }else {
                    mPresenter.getWifiVideoLockGetAlarmList(1, wifiSn);
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if(isScreenedMode && !TextUtils.isEmpty(screenedDate)){
                    mPresenter.getWifiVideoLockGetDoorbellFilterList(currentPage, wifiSn,DateUtils.dateChangeTimestamp(screenedDate + startTime)
                            ,DateUtils.dateChangeTimestamp(screenedDate + endTime));
                }else {
                    mPresenter.getWifiVideoLockGetAlarmList(currentPage, wifiSn);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ((ViewGroup) view.getParent()).removeView(view);
    }


    @Override
    public void onLoadServerRecord(List<WifiVideoLockAlarmRecord> lockRecords, int page) {
        LogUtils.d("收到服务器数据  " + lockRecords.size());
        tvNoMore.setVisibility(View.GONE);
        recycleview.setVisibility(View.VISIBLE);
        if (page == 1) {
            records.clear();
        }
        int size = records.size();
        currentPage = page + 1;
        int sum = groupData(lockRecords);

        if (size>0){
            operationGroupRecordAdapter.notifyItemRangeInserted(size,sum);
        }else {
            operationGroupRecordAdapter.notifyDataSetChanged();
        }

        if (page == 1) { //这时候是刷新load
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(true);
        } else {
            refreshLayout.finishLoadMore();
        }
    }

    private int groupData(List<WifiVideoLockAlarmRecord> lockRecords) {
        String lastTimeHead = "";
        int sum = 0;
        WifiVideoLockAlarmRecord lastRecord = null;
        if (lockRecords != null && lockRecords.size() > 0) {
            for (int i = 0; i < lockRecords.size(); i++) {
                if (records.size() > 0) {
                    lastRecord = records.get(records.size() - 1);
                    lastTimeHead = lastRecord.getDayTime();
                }
                WifiVideoLockAlarmRecord record = lockRecords.get(i);
                boolean falg = false;
                for(WifiVideoLockAlarmRecord list : records){
                    if(list.getCreateTime() == record.getCreateTime()){
                        falg = true;
                        break;
                    }
                }
                if(falg){
                    continue;
                }
                long openTime = 0;
                try {
                    openTime = Long.parseLong(record.getTime());
                }catch (Exception e){
                    e.printStackTrace();
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
                sum += 1;
                records.add(record);

            }
        }
        return sum;
    }


    @Override
    public void onLoadServerRecordFailed(Throwable throwable) {
        //加载服务器开锁记录失败
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(getActivity(), throwable));
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
        //ToastUtils.showShort(R.string.server_no_data_2);
        tvNoMore.setVisibility(View.VISIBLE);
        recycleview.setVisibility(View.GONE);
    }

    @Override
    public void noMoreData() {
        ToastUtils.showShort(R.string.no_more_data);
        refreshLayout.finishLoadMore();
        refreshLayout.setEnableLoadMore(false);
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
