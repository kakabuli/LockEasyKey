package com.philips.easykey.lock.fragment.record;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.BluetoothWarnMessageAdapter;
import com.philips.easykey.lock.bean.BluetoothItemRecordBean;
import com.philips.easykey.lock.bean.BluetoothRecordBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter.GatewayLockAlaramRecordPresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.IGatewayAlarmLockRecordView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GetAlarmRecordResult;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by David on 2019/4/22
 */
public class Gateway8100AlarmRecordFragment extends BaseFragment<IGatewayAlarmLockRecordView, GatewayLockAlaramRecordPresenter<IGatewayAlarmLockRecordView>>
        implements IGatewayAlarmLockRecordView {

    List<BluetoothRecordBean> mOpenLockList = new ArrayList<>(); //全部数据

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.no_have_record)
    TextView noHaveRecord;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private String gatewayId;
    private String deviceId;
    private BluetoothWarnMessageAdapter openLockRecordAdapter;
    private int page = 1;
    private int lastPage = 0;
    private List<GetAlarmRecordResult.DataBean> alarmRecordList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_gateway_open_lock_record, null);
        unbinder = ButterKnife.bind(this, view);
        initRecycleView();
        initData();
        initRefresh();
        return view;
    }

    private void initRefresh() {

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (!TextUtils.isEmpty(gatewayId) && !TextUtils.isEmpty(deviceId)) {
                    page = 1;
                    lastPage = 0;
                    if (mOpenLockList != null) {
                        mOpenLockList.clear();
                    }
                    if (alarmRecordList != null) {
                        alarmRecordList.clear();
                    }
                    //String gwId, String deviceId, int pageNum
                    mPresenter.openGatewayLockRecord(gatewayId, deviceId, 1);
                    refreshLayout.finishRefresh(5 * 1000);
                }
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (!TextUtils.isEmpty(gatewayId) && !TextUtils.isEmpty(deviceId)) {
                    if (lastPage == 0) {
                        mPresenter.openGatewayLockRecord(gatewayId, deviceId, page);
                        refreshLayout.finishRefresh(5 * 1000);
                    } else {
                        refreshLayout.finishLoadMore();
                    }
                }
            }
        });


    }

    private void initData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            gatewayId = bundle.getString(KeyConstants.GATEWAY_ID);
            deviceId = bundle.getString(KeyConstants.DEVICE_ID);
        }
        if (NetUtil.isNetworkAvailable()) {
            if (!TextUtils.isEmpty(gatewayId) && !TextUtils.isEmpty(deviceId)) {
                mPresenter.openGatewayLockRecord(gatewayId, deviceId, 1);
            }
        } else {
            changeView(false);
        }

    }

    @Override
    protected GatewayLockAlaramRecordPresenter<IGatewayAlarmLockRecordView> createPresent() {
        return new GatewayLockAlaramRecordPresenter<>();
    }

    private void initRecycleView() {

        if (mOpenLockList != null) {
            openLockRecordAdapter = new BluetoothWarnMessageAdapter(mOpenLockList);  //网关锁开锁记录
            recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
            recycleview.setAdapter(openLockRecordAdapter);

        }
    }

    private void changeView(boolean noHaveOpenRecord) {
        if (noHaveOpenRecord) {
            if (noHaveRecord != null && recycleview != null && refreshLayout != null) {
                noHaveRecord.setVisibility(View.GONE);
                refreshLayout.setVisibility(View.VISIBLE);
                recycleview.setVisibility(View.VISIBLE);
            }
        } else {
            if (noHaveRecord != null && recycleview != null && refreshLayout != null) {
                noHaveRecord.setVisibility(View.VISIBLE);
                refreshLayout.setVisibility(View.GONE);
                recycleview.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }


    @Override
    public void getOpenLockRecordSuccess(List<GetAlarmRecordResult.DataBean> alarmList) {
        //获取开锁记录成功
        LogUtils.d("获取到开锁记录多少条  " + alarmList.size());

        if (alarmList.size() == 0 && page == 1) {
            changeView(false);
        }
        if (alarmList.size() == 20) {
            page++;
        } else {
            lastPage = page + 1;
        }
        alarmRecordList.addAll(alarmList);
        groupData(alarmRecordList);
        if (openLockRecordAdapter != null) {
            openLockRecordAdapter.notifyDataSetChanged();
        }

        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    public void getOpenLockRecordFail() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
        ToastUtils.showShort(R.string.philips_code_get_open_lock_record_fail);
        changeView(false);
    }

    @Override
    public void getOpenLockRecordThrowable(Throwable throwable) {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
        }
        LogUtils.d("获取开锁记录异常  网关锁");
        ToastUtils.showShort(R.string.philips_code_get_open_lock_record_fail);
        changeView(false);
    }

    private void groupData(List<GetAlarmRecordResult.DataBean> alarmList) {
        if (mOpenLockList != null) {
            mOpenLockList.clear();
        }
        String lastDayTime = "";
        for (int i = 0; i < alarmList.size(); i++) {
            GetAlarmRecordResult.DataBean dataBean = alarmList.get(i);
            //获取开锁时间的毫秒数
            LogUtils.d("网关的开锁毫秒时间" + dataBean.getWarningTime());
            List<BluetoothItemRecordBean> itemList = new ArrayList<>();
            String open_time = DateUtils.getDateTimeFromMillisecond(dataBean.getWarningTime()  );//将毫秒时间转换成功年月日时分秒的格式
            LogUtils.d("网关的开锁毫秒时间" + open_time);
            //获取开锁时间的毫秒数
            String timeHead = open_time.substring(0, 10);
            String hourSecond = open_time.substring(11, 16);
            String titleTime = "";
            String name = "";
            String openType = "";

//            ：1 低电量 2 钥匙开门 3 验证错误 4 防撬提醒 5 即时性推送消息 6 胁迫开门 7 上锁故障
            switch (dataBean.getWarningType()) {
                case 1:
                    openType = getString(R.string.warring_low_power);
                    break;
                case 2:
                    openType = getString(R.string.warring_mechanical_key);
                    break;
                case 3:
                    openType = getString(R.string.warring_three_times);
                    break;
                case 4:
                    openType = getString(R.string.warring_broken);
                    break;
                case 5:
                    openType = getString(R.string.philips_fragment_gateway_alarm_record);
                    break;
                case 6:
                    openType = getString(R.string.warrign_hijack);
                    break;
                case 7:
                    openType = getString(R.string.warring_door_not_lock);
                    break;
                default:
                    openType = getString(R.string.warring_unkonw);
                    break;
            }
            if (!timeHead.equals(lastDayTime)) { //添加头
                lastDayTime = timeHead;

                itemList.add(new BluetoothItemRecordBean(openType, "", KeyConstants.BLUETOOTH_RECORD_WARN,
                        hourSecond, false, false));
                mOpenLockList.add(new BluetoothRecordBean(timeHead, itemList, false));
            } else {
                BluetoothRecordBean bluetoothRecordBean = mOpenLockList.get(mOpenLockList.size() - 1);
                List<BluetoothItemRecordBean> bluetoothItemRecordBeanList = bluetoothRecordBean.getList();
                bluetoothItemRecordBeanList.add(new BluetoothItemRecordBean(openType, "", KeyConstants.BLUETOOTH_RECORD_WARN,
                        hourSecond, false, false));
            }
        }

        for (int i = 0; i < mOpenLockList.size(); i++) {
            BluetoothRecordBean bluetoothRecordBean = mOpenLockList.get(i);
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
            if (i == mOpenLockList.size() - 1) {
                bluetoothRecordBean.setLastData(true);
            }
        }
    }


}
