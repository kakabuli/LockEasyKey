package com.philips.easykey.lock.fragment.record;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.PhilipsWifiLockRecordIAdapter;
import com.philips.easykey.lock.bean.WifiLockOperationRecordGroup;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockOpenRecordPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockOpenRecordView;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockOperationRecord;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;;

/**
 * Created by David on 2019/4/22
 */
public class PhilipsWifiLockOpenRecordFragment extends BaseFragment<IWifiLockOpenRecordView, WifiLockOpenRecordPresenter<IWifiLockOpenRecordView>>
        implements IWifiLockOpenRecordView, View.OnClickListener {

    RecyclerView recycleview;
    List<WifiLockOperationRecord> records = new ArrayList<>();
    PhilipsWifiLockRecordIAdapter operationGroupRecordAdapter;
    SmartRefreshLayout refreshLayout;
    TextView tvSynchronizedRecord;
    RelativeLayout rlHead;
    TextView tvNoMore;

    private int currentPage = 1;   //当前的开锁记录时间
    View view;
    private String wifiSn;
    private String screenedDate;
    private String startTime = " 00:00:00";
    private String endTime = " 23:59:59";
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

        tvSynchronizedRecord.setOnClickListener(this);
        wifiSn = getArguments().getString(KeyConstants.WIFI_SN);
        rlHead.setVisibility(View.GONE);

        initRecycleView();
        initRefresh();
        initData();
        return view;
    }

    private void initData() {
        String localRecord = (String) SPUtils.get(KeyConstants.WIFI_LOCK_OPERATION_RECORD + wifiSn, "");
        Gson gson = new Gson();
        List<WifiLockOperationRecord> records = gson.fromJson(localRecord, new TypeToken<List<WifiLockOperationRecord>>() {
        }.getType());
        groupData(records);
        operationGroupRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getOpenRecordFromServer(1, wifiSn);
    }


    @Override
    protected WifiLockOpenRecordPresenter<IWifiLockOpenRecordView> createPresent() {
        return new WifiLockOpenRecordPresenter<>();
    }

    private void initRecycleView() {
        operationGroupRecordAdapter = new PhilipsWifiLockRecordIAdapter(records);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setAdapter(operationGroupRecordAdapter);
    }

    public void getOpenScreenedRecordFromServer(boolean isScreenedMode , String screenedDate){
        this.isScreenedMode = isScreenedMode;
        this.screenedDate = screenedDate;
        mPresenter.getOpenScreenedRecordFromServer(1, wifiSn,DateUtils.dateChangeTimestamp(screenedDate + startTime)
                ,DateUtils.dateChangeTimestamp(screenedDate + endTime));
    }

    public void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.setEnableLoadMore(true);
                if(isScreenedMode && !TextUtils.isEmpty(screenedDate)){
                    mPresenter.getOpenScreenedRecordFromServer(1, wifiSn,DateUtils.dateChangeTimestamp(screenedDate + startTime)
                            ,DateUtils.dateChangeTimestamp(screenedDate + endTime));
                }else {
                    mPresenter.getOpenRecordFromServer(1, wifiSn);
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                if(isScreenedMode && !TextUtils.isEmpty(screenedDate)){
                    mPresenter.getOpenScreenedRecordFromServer(currentPage, wifiSn,DateUtils.dateChangeTimestamp(screenedDate + startTime)
                            ,DateUtils.dateChangeTimestamp(screenedDate + endTime));
                }else {
                    mPresenter.getOpenRecordFromServer(currentPage, wifiSn);
                }
            }
        });
    }


    @Override
    public void onLoseRecord(List<Integer> numbers) {
    }


    @Override
    public void onLoadServerRecord(List<WifiLockOperationRecord> lockRecords, int page) {
        LogUtils.d("收到服务器数据  " + lockRecords.size());
        recycleview.setVisibility(View.VISIBLE);
        tvNoMore.setVisibility(View.GONE);
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

    private int groupData(List<WifiLockOperationRecord> lockRecords) {
        String lastTimeHead = "";
        int sum = 0;
        WifiLockOperationRecord lastRecord = null;
        if (lockRecords != null && lockRecords.size() > 0) {
            for (int i = 0; i < lockRecords.size(); i++) {
                if (records.size() > 0) {
                    lastRecord = records.get(records.size() - 1);
                    lastTimeHead = lastRecord.getDayTime();
                }
                WifiLockOperationRecord record = lockRecords.get(i);
                boolean falg = false;
                for(WifiLockOperationRecord list : records){
                    if(list.getCreateTime() == record.getCreateTime()){
                        falg = true;
                        break;
                    }
                }
                if(falg){
                    continue;
                }
                //获取开锁时间的毫秒数
                long openTime = record.getTime();
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
        Toast.makeText(getActivity(),R.string.no_more_data,Toast.LENGTH_LONG).show();
        refreshLayout.finishLoadMore();
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void onClick(View v) {

    }

    class DemoDiffCallback extends DiffUtil.Callback {
        private List<WifiLockOperationRecordGroup> mOldList;
        private List<WifiLockOperationRecordGroup> mNewList;

        public DemoDiffCallback(List<WifiLockOperationRecordGroup> olds, List<WifiLockOperationRecordGroup> news) {
            this.mOldList = olds;
            this.mNewList = news;
        }

        @Override
        public int getOldListSize() {
            return mOldList == null ? 0 : mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return mNewList == null ? 0 : mNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).equals(mNewList.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return mOldList.get(oldItemPosition).equals(mNewList.get(newItemPosition));
        }
    }


}
