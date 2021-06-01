package com.philips.easykey.lock.activity.device.wifilock.family;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.PhilipsWifiLockShareUserAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockFamilyManagerPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockFamilyManagerView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockShareResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.NetUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


public class PhilipsWifiLockFamilyManagerActivity extends BaseActivity<IWifiLockFamilyManagerView,
        WifiLockFamilyManagerPresenter<IWifiLockFamilyManagerView>> implements IWifiLockFamilyManagerView, View.OnClickListener {

    ImageView ivBack;//返回
    TextView tvContent;//标题
    RecyclerView recycleview;

    PhilipsWifiLockShareUserAdapter wifiLockShareUserAdapter;
    List< WifiLockShareResult.WifiLockShareUser> shareUsers = new ArrayList<>();
    boolean isNotData = true;
    SmartRefreshLayout refreshLayout;

    TextView tvNoUser;
    RelativeLayout llAddUser;
    LinearLayout llHasData;

    public static final int REQUEST_CODE = 100;
    boolean querySuccess = false;
    private String wifiSn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_shared_device_management);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        recycleview = findViewById(R.id.recycleview);
        refreshLayout = findViewById(R.id.refreshLayout);
        tvNoUser = findViewById(R.id.tv_no_user);
        llAddUser = findViewById(R.id.ll_add_user);
        llHasData = findViewById(R.id.ll_has_data);

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);


        wifiLockShareUserAdapter = new PhilipsWifiLockShareUserAdapter(shareUsers, R.layout.philips_item_has_shared_device);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(wifiLockShareUserAdapter);
        wifiLockShareUserAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                WifiLockShareResult.WifiLockShareUser wifiLockShareUser = shareUsers.get(position);
                Intent intent = new Intent(PhilipsWifiLockFamilyManagerActivity.this, PhilipsWiFiLockShareUserDetailActivity.class);
                intent.putExtra(KeyConstants.SHARE_USER_INFO, wifiLockShareUser);
                startActivity(intent);
            }
        });


        ivBack.setOnClickListener(this);
        llAddUser.setOnClickListener(this);
        tvContent.setText(getString(R.string.user_manage));
        initRefresh();
    }

    @Override
    protected WifiLockFamilyManagerPresenter<IWifiLockFamilyManagerView> createPresent() {
        return new WifiLockFamilyManagerPresenter<>();
    }

    private void initRefresh() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //清除数据
                shareUsers.clear();
                wifiLockShareUserAdapter.notifyDataSetChanged();
                queryUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryUser();
    }

    /**
     * 查询用户
     */
    public void queryUser() {
        if (NetUtil.isNetworkAvailable()) {
            mPresenter.queryUserList(wifiSn);
        } else {
            ToastUtils.showShort(R.string.philips_noNet);
        }
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

            case R.id.ll_add_user:
                if (querySuccess ) {
                    if (shareUsers.size() < 10) {
                        intent = new Intent(this, PhilipsWiFiLockAddShareUserActivity.class);
                        intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                        startActivity(intent );
                    } else {
                        AlertDialogUtil.getInstance().noEditSingleButtonDialog(this, "", getString(R.string.more_then_ten_member), getString(R.string.hao_de), new AlertDialogUtil.ClickListener() {
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

                } else {
                    queryUser();
                }
                break;

        }
    }

    @Override
    public void querySuccess(List<WifiLockShareResult.WifiLockShareUser> users) {
        if (users == null){
            isNotData = true;
            pageChange();
            return;
        }
        //刷新完成
        refreshLayout.finishRefresh();
        querySuccess = true;
        shareUsers.clear();
        shareUsers.addAll(users);
        if (shareUsers.size() > 0) {
            isNotData = false;
            wifiLockShareUserAdapter.notifyDataSetChanged();
        } else {
            isNotData = true;
        }
        pageChange();
    }

    @Override
    public void queryFailedServer(BaseResult result) {
        querySuccess = false;
        //刷新完成
        refreshLayout.finishRefresh();
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, result.getCode()));
    }

    @Override
    public void queryFailed(Throwable throwable) {
        querySuccess = false;
        //刷新完成
        refreshLayout.finishRefresh();
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }
}
