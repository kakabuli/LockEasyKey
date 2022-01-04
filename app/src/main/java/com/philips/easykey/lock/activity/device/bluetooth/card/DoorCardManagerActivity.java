package com.philips.easykey.lock.activity.device.bluetooth.card;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.DoorCardManagerAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleActivity;
import com.philips.easykey.lock.mvp.presenter.ble.CardManagerPresenter;
import com.philips.easykey.lock.mvp.view.ICardManagerView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetPasswordResult;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Created by David
 */
public class DoorCardManagerActivity extends BaseBleActivity<ICardManagerView, CardManagerPresenter<ICardManagerView>>
        implements View.OnClickListener, ICardManagerView {

    ImageView ivBack;//返回
    TextView tvContent;//标题

    RecyclerView recycleview;
    DoorCardManagerAdapter doorCardManagerAdapter;
    boolean isNotData = true;
    TextView tvSynchronizedRecord;
    LinearLayout llAdd;
    LinearLayout llHasData;
    TextView tvNoUser;
    List<GetPasswordResult.DataBean.Card> list = new ArrayList<>();
    ImageView ivRight;
    SmartRefreshLayout refreshLayout;
    private BleLockInfo bleLockInfo;
    private boolean isSync = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_card_manager);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        recycleview = findViewById(R.id.recycleview);
        tvSynchronizedRecord = findViewById(R.id.tv_synchronized_record);
        llAdd = findViewById(R.id.ll_add);
        llHasData = findViewById(R.id.ll_has_data);
        tvNoUser = findViewById(R.id.tv_no_user);
        ivRight = findViewById(R.id.iv_right);
        refreshLayout = findViewById(R.id.refreshLayout);

        if(MyApplication.getInstance().getBleService().getBleLockInfo() != null){

            bleLockInfo = MyApplication.getInstance().getBleService().getBleLockInfo();

            tvContent.setText(getString(R.string.door_card));
            ivBack.setOnClickListener(this);
            tvSynchronizedRecord.setOnClickListener(this);
            llAdd.setOnClickListener(this);
            pageChange();
            //进入默认鉴权
            mPresenter.isAuth(bleLockInfo, false);
            initRecycleview();
            initData();
            initRefresh();
            mPresenter.getAllPassword(bleLockInfo, true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mPresenter.getAllPassword(bleLockInfo, true);
    }

    private void initRefresh() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //下拉刷新   如果正在同步，不刷新  强制从服务器中获取数据
                if (isSync) {
                    ToastUtils.showShort(getString(R.string.is_sync_please_wait));
                    refreshLayout.finishRefresh();
                } else {
                    mPresenter.getAllPassword(bleLockInfo, true);
                }
            }
        });
    }

    @Override
    protected CardManagerPresenter<ICardManagerView> createPresent() {
        return new CardManagerPresenter<>();
    }

    private void initRecycleview() {
        doorCardManagerAdapter = new DoorCardManagerAdapter(list, R.layout.philips_item_door_card_manager);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(doorCardManagerAdapter);
        doorCardManagerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(DoorCardManagerActivity.this, DoorCardManagerDetailActivity.class);
                intent.putExtra(KeyConstants.TO_PWD_DETAIL, list.get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    public void pageChange() {

        if (isNotData) {
            llHasData.setVisibility(View.GONE);
            tvNoUser.setVisibility(View.VISIBLE);
        } else {
            llHasData.setVisibility(View.VISIBLE);
            tvNoUser.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_add:
                if (!NetUtil.isNetworkAvailable()) {
                    ToastUtils.showShort(getString(R.string.please_have_net_add_card));
                    return;
                }
                if (!mPresenter.isAuthAndNoConnect(bleLockInfo)) {
                    intent = new Intent(this, DoorCardNearDoorActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, DoorCardIdentificationActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.tv_synchronized_record:
                //同步
                if (isSync) {
                    ToastUtils.showShort(getString(R.string.is_sync_please_wait));
                } else {
                    if (mPresenter.isAuth(bleLockInfo, true)) {
                        mPresenter.syncPassword();
                    }
                }
                break;
        }
    }

    public void initData() {
        if (list.size() > 0) {
            isNotData = false;
        } else {
            isNotData = true;
        }
        pageChange();
        doorCardManagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onServerDataUpdate() {
        LogUtils.d("卡片更新   ");
        mPresenter.getAllPassword(bleLockInfo, true);
    }

    @Override
    public void startSync() {
        showLoading(getString(R.string.is_sync_lock_data));
        isSync = true;
    }

    @Override
    public void endSync() {
        isSync = false;
        hiddenLoading();
    }

    @Override
    public void onSyncPasswordSuccess(List<GetPasswordResult.DataBean.Card> cardList) {
        //排序
        list = cardList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(Comparator.naturalOrder());
        }
        if (cardList.size() > 0) {
            isNotData = false;
        } else {
            isNotData = true;
        }
        if (cardList.size() > 0) {
//            doorCardManagerAdapter.notifyDataSetChanged();
            initRecycleview();
        }
        pageChange();
    }

    @Override
    public void onSyncPasswordFailed(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.sync_failed_card));
    }

    @Override
    public void onUpdate(List<GetPasswordResult.DataBean.Card> cardList) {
        mPresenter.getAllPassword(bleLockInfo, true);
    }

    @Override
    public void onGetPasswordSuccess(GetPasswordResult result) {
        refreshLayout.finishRefresh();
        if (result == null ){
            isNotData = true;
            pageChange();
            return;
        }
        if (result.getData() == null ){
            isNotData = true;
            pageChange();
            return;
        }
        if (result.getData().getCardList() == null ){
            isNotData = true;
            pageChange();
            return;
        }
        if (result.getData().getCardList().size() == 0) {
            isNotData = true;
        } else {
            isNotData = false;
        }
        LogUtils.d("卡片更新");
        list = result.getData().getCardList();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(Comparator.naturalOrder());
        }
        if (result.getData().getCardList().size() > 0) {
           initRecycleview();
        }
        pageChange();
    }
    @Override
    public void onGetPasswordFailedServer(BaseResult result) {
        refreshLayout.finishRefresh();
        if (isSync) {
            return;
        }
        ToastUtils.showShort(getString(R.string.get_card_failed));
    }
    @Override
    public void onGetPasswordFailed(Throwable throwable) {
        refreshLayout.finishRefresh();
        if (isSync) {
            return;
        }
        ToastUtils.showShort(getString(R.string.get_card_failed));
    }
}
