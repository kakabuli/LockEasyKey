package com.philips.easykey.lock.fragment.record;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.home.BluetoothRecordActivity;
import com.philips.easykey.lock.adapter.BluetoothRecordAdapter;
import com.philips.easykey.lock.bean.BluetoothItemRecordBean;
import com.philips.easykey.lock.bean.BluetoothRecordBean;
import com.philips.easykey.lock.bean.OperationSection;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleFragment;
import com.philips.easykey.lock.mvp.presenter.ble.OperationRecordPresenter;
import com.philips.easykey.lock.mvp.view.IOpenLockRecordView;
import com.philips.easykey.lock.mvp.view.IOperationRecordView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.publiclibrary.ble.bean.OperationLockRecord;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetPasswordResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class OperationRecordFragment extends BaseBleFragment<IOperationRecordView, OperationRecordPresenter<IOpenLockRecordView>>
        implements View.OnClickListener, IOperationRecordView {

    RecyclerView recyclerView;
    SmartRefreshLayout refreshLayout;//刷新
    TextView tvSynchronizedRecord;
    private View mView;
    private int type = 0; //默认是开锁记录0   1为警报记录
    private int currentPage = 1;   //当前的开锁记录时间
    private BluetoothRecordAdapter openLockRecordAdapter;
    private List<OperationSection> showList = new ArrayList<>();
    private boolean isLoadingBleRecord;  //正在加载锁上数据
    private BleLockInfo bleLockInfo;
    private BluetoothRecordActivity activity;

    private List<BluetoothRecordBean> showDatas = new ArrayList<>();


    public void setType(int type) {
        this.type = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (BluetoothRecordActivity) getActivity();
        bleLockInfo = activity.getBleDeviceInfo();
        if (mView == null) {
            mView = inflater.inflate(R.layout.philips_fragment_bluetooth_open_lock_record, container, false);
        }

        recyclerView = mView.findViewById(R.id.recycleview);
        refreshLayout = mView.findViewById(R.id.refreshLayout);
        tvSynchronizedRecord = mView.findViewById(R.id.tv_synchronized_record);

        LogUtils.d("当前类型是  " + type);
        tvSynchronizedRecord.setOnClickListener(this);
        initRefresh();
        return mView;
    }

    @Override
    protected OperationRecordPresenter<IOpenLockRecordView> createPresent() {
        return new OperationRecordPresenter<>();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.getOperationRecordFromServer(1);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup) mView.getParent()).removeView(mView);

    }

    private void initRefresh() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (isLoadingBleRecord) {  //正在获取开锁记录，提示用户
                    ToastUtils.showShort(R.string.is_loading_ble_record_please_wait);
                    refreshlayout.finishRefresh();
                } else {   //真正在
                    refreshlayout.setEnableLoadMore(true);
                    mPresenter.getOperationRecordFromServer(1);
                }
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                mPresenter.getOperationRecordFromServer(currentPage);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_synchronized_record:
                if (isLoadingBleRecord) { //如果正在加载锁上数据  不让用户再次点击
                    ToastUtils.showShort(R.string.is_loading_lock_record);
                    return;
                }
                if (mPresenter.isAuth(bleLockInfo, true)) {
                    LogUtils.d("同步开锁记录");
                    mPresenter.getOperationRecordFromBle();
                    showDatas.clear();
                    if (openLockRecordAdapter != null) {
                        openLockRecordAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Override
    public void onLoseRecord(List<Integer> numbers) {

    }

    /**
     * 没有开锁记录
     */
    @Override
    public void noData() {
        ToastUtils.showShort(R.string.lock_no_record);
        hiddenLoading();
    }

    @Override
    public void onLoadBleOperationRecord(List<OperationLockRecord> lockRecords) {
        LogUtils.d("获取到一组操作记录   "+lockRecords.size());
        //获取到蓝牙的开锁记录
        showList.clear();
        String lastTimeHead = "";
        hiddenLoading();
        for (OperationLockRecord record : lockRecords) {
            //获取开锁时间的毫秒数
            String timeHead = record.getEventTime().substring(0, 10);
            if (!timeHead.equals(lastTimeHead)) { //添加头
                lastTimeHead = timeHead;
                showList.add(new OperationSection(true, timeHead));
            }
            showList.add(new OperationSection(record));
        }
        groupData(lockRecords);
        if (openLockRecordAdapter == null) {
            openLockRecordAdapter = new BluetoothRecordAdapter(showDatas);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(openLockRecordAdapter);
//            openLockRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//
//                }
//            });
        } else {
            openLockRecordAdapter.notifyDataSetChanged();
        }

        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void onLoadBleRecordFinish(boolean isComplete) {
        if (isComplete) {
            ToastUtils.showShort(R.string.sync_success);
        } else {
            ToastUtils.showShort(R.string.get_record_failed_please_wait);
            hiddenLoading();
        }
        //加载完了   设置正在加载数据
        isLoadingBleRecord = false;
    }

    @Override
    public void onLoadServerOperationRecord(List<OperationLockRecord> lockRecords, int page) {
        LogUtils.d("收到服务器数据  " + lockRecords.size());
        currentPage = page + 1;
        showList.clear();
        String lastTimeHead = "";
        for (OperationLockRecord record : lockRecords) {
            //获取开锁时间的毫秒数
            String timeHead = record.getEventTime().substring(0, 10);
            if (!timeHead.equals(lastTimeHead)) { //添加头
                lastTimeHead = timeHead;
                showList.add(new OperationSection(true, timeHead));
            }
            showList.add(new OperationSection(record));
        }
        groupData(lockRecords);
        if (openLockRecordAdapter == null) {
            openLockRecordAdapter = new BluetoothRecordAdapter(showDatas);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(openLockRecordAdapter);
//            openLockRecordAdapter.setOnItemClickListener(new OnItemClickListener() {
//                @Override
//                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
//
//                }
//            });
        } else {
            openLockRecordAdapter.notifyDataSetChanged();
        }
        if (page == 1) { //这时候是刷新
            refreshLayout.finishRefresh();
            refreshLayout.setEnableLoadMore(true);
        } else {
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void onLoadServerRecordFailed(Throwable throwable) {
        //加载服务器开锁记录失败
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
//        ToastUtils.showShort(R.string.get_record_failed);
        ToastUtils.showShort( HttpUtils.httpProtocolErrorCode(getActivity(),throwable));
    }

    @Override
    public void onLoadServerRecordFailedServer(BaseResult result) {
        ToastUtils.showShort( HttpUtils.httpErrorCode(getActivity(), result.getCode()));
        refreshLayout.finishRefresh();
        refreshLayout.finishLoadMore();
    }

    @Override
    public void onServerNoData() {
        //服务器没有开锁记录
        refreshLayout.finishRefresh();
        refreshLayout.setEnableLoadMore(false);  //服务器没有数据时，不让上拉加载更多
        ToastUtils.showShort(R.string.server_no_data);
    }

    @Override
    public void noMoreData() {   //没有更多数据   提示用户  且禁止掉上拉加载更多功能
        ToastUtils.showShort(R.string.no_more_data);
        refreshLayout.finishLoadMore();
        refreshLayout.setEnableLoadMore(false);
    }

    @Override
    public void onUploadServerRecordSuccess() {
        LogUtils.d("记录上传成功");
        ToastUtils.showShort(R.string.lock_record_upload_success);
    }

    @Override
    public void onUploadServerRecordFailed(Throwable throwable) {
        ToastUtils.showShort( HttpUtils.httpProtocolErrorCode(getActivity(),throwable));
        LogUtils.d("记录上传失败");
//        ToastUtils.showShort(R.string.record_upload_failed);
    }

    @Override
    public void onUploadServerRecordFailedServer(BaseResult result) {
        ToastUtils.showShort( HttpUtils.httpErrorCode(getActivity(), result.getCode()));
    }


    @Override
    public void startBleRecord() {
        //开始获取蓝牙记录，禁止掉下拉刷新和上拉加载更多
        isLoadingBleRecord = true;
        refreshLayout.setEnableLoadMore(false);
        showLoading(getString(R.string.is_loading_lock_record));
    }


    private void groupData(List<OperationLockRecord> lockRecords) {
        showDatas.clear();
        String lastTimeHead = "";
        for (int i = 0; i < lockRecords.size(); i++) {
            OperationLockRecord record = lockRecords.get(i);
            //获取开锁时间的毫秒数
            String timeHead = record.getEventTime().substring(0, 10);
            String hourSecond = record.getEventTime().substring(11, 16);

            GetPasswordResult passwordResult = MyApplication.getInstance().getPasswordResults(bleLockInfo.getServerLockInfo().getLockName());
            //获取昵称

            String nickName = BleUtil.getNickName(record,passwordResult );
            String openType = BleUtil.getOpenType(record);
            if (!timeHead.equals(lastTimeHead)) { //添加头
                lastTimeHead = timeHead;
                List<BluetoothItemRecordBean> itemList = new ArrayList<>();
                itemList.add(new BluetoothItemRecordBean(nickName, openType, KeyConstants.BLUETOOTH_RECORD_COMMON,
                        hourSecond, false, false));
                showDatas.add(new BluetoothRecordBean(timeHead, itemList, false));
            } else {
                BluetoothRecordBean bluetoothRecordBean = showDatas.get(showDatas.size() - 1);
                List<BluetoothItemRecordBean> bluetoothItemRecordBeanList = bluetoothRecordBean.getList();
                bluetoothItemRecordBeanList.add(new BluetoothItemRecordBean(nickName, openType, KeyConstants.BLUETOOTH_RECORD_COMMON, hourSecond, false, false));
            }
        }

        for (int i = 0; i < showDatas.size(); i++) {
            BluetoothRecordBean bluetoothRecordBean = showDatas.get(i);
            List<BluetoothItemRecordBean> bluetoothRecordBeanList = bluetoothRecordBean.getList();

            for (int j = 0; j < bluetoothRecordBeanList.size(); j++) {
                BluetoothItemRecordBean bluetoothItemRecordBean = bluetoothRecordBeanList.get(j);

                if (j == 0) {
                    bluetoothItemRecordBean.setFirstData(true);
                }
                if (j == bluetoothRecordBeanList.size() - 1) {
                    bluetoothItemRecordBean.setLastData(true);
                }

            }
            if (i == showDatas.size() - 1) {
                bluetoothRecordBean.setLastData(true);
            }
        }
    }


}
