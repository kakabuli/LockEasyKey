package com.philips.easykey.lock.activity.device.gatewaylock.share;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.DeviceShareAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.gatewaypresenter.GatewaySharedPresenter;
import com.philips.easykey.lock.mvp.view.gatewayView.IGatewaySharedView;
import com.philips.easykey.lock.publiclibrary.bean.GatewayInfo;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.DeviceShareResultBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.DeviceShareUserResultBean;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David
 */
public class GatewayLockSharedActivity extends BaseActivity<IGatewaySharedView, GatewaySharedPresenter<IGatewaySharedView>> implements IGatewaySharedView,View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;//返回
    @BindView(R.id.tv_content)
    TextView tvContent;//标题
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    @BindView(R.id.tv_no_user)
    TextView tvNoUser;
    @BindView(R.id.ll_add_user)
    RelativeLayout llAddUser;
    @BindView(R.id.ll_has_data)
    LinearLayout llHasData;

    public static final int REQUEST_CODE = 11111;
    boolean querySuccess = false;

    private  List<DeviceShareUserResultBean.DataBean> shareData=new ArrayList<>();
    private DeviceShareAdapter shareAdapter;
    private String gatewayId;
    private String deviceId;
    private String uid;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_shared_device_management);
        ButterKnife.bind(this);
        initView();
        initData();
        initRecyclerView();
        initListener();
        initRefresh();

    }


    private void initData() {
        Intent intent=getIntent();
        gatewayId=intent.getStringExtra(KeyConstants.GATEWAY_ID);
        deviceId=intent.getStringExtra(KeyConstants.DEVICE_ID);
        uid=MyApplication.getInstance().getUid();
    }

    private void initRecyclerView() {
        if (shareData!=null){
            recycleview.setLayoutManager(new LinearLayoutManager(this));
            shareAdapter=new DeviceShareAdapter(shareData);
            recycleview.setAdapter(shareAdapter);
        }

    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        llAddUser.setOnClickListener(this);
        if (shareAdapter!=null){
            shareAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    DeviceShareUserResultBean.DataBean shareResultBean = shareData.get(position);
                    Intent intent = new Intent(GatewayLockSharedActivity.this, GatewayLockShareUserNumberActivity.class);
                    intent.putExtra(KeyConstants.GATEWAY_SHARE_USER, shareResultBean);
                    intent.putExtra(KeyConstants.GATEWAY_ID,gatewayId);
                    intent.putExtra(KeyConstants.DEVICE_ID,deviceId);
                    startActivity(intent);
                }
            });
        }
    }

    private void initView() {
        tvContent.setText(getString(R.string.user_manage));
    }




    @Override
    protected GatewaySharedPresenter<IGatewaySharedView> createPresent() {
        return new GatewaySharedPresenter<>();
    }


    private void initRefresh() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //清除数据
                shareData.clear();
                shareAdapter.notifyDataSetChanged();
                //查找分享用户
                if (gatewayId!=null&&deviceId!=null){
                    mPresenter.getShareDeviceUser(gatewayId,deviceId,uid);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (gatewayId!=null&&deviceId!=null){
            mPresenter.getShareDeviceUser(gatewayId,deviceId,uid);
        }
    }

    public void pageChange(boolean isNotData) {
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
                if (NetUtil.isNetworkAvailable()) {
                    if (shareData.size() < 10) {
                        intent = new Intent(this, AddGatewayLockShareActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
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
                }else {
                    ToastUtils.showShort(R.string.query_fail_requery);
                }
                break;

        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==REQUEST_CODE){
                String phone = data.getStringExtra(KeyConstants.AUTHORIZATION_TELEPHONE);
                if (gatewayId!=null&&deviceId!=null){
                    GatewayInfo gatewayInfo=MyApplication.getInstance().getGatewayById(gatewayId);
                    if (gatewayInfo!=null) {
                        String model = gatewayInfo.getServerInfo().getModel();
                        if ("6010".equals(model)){
                            if (!TextUtils.isEmpty(gatewayInfo.getServerInfo().getMeUsername()) && !TextUtils.isEmpty(gatewayInfo.getServerInfo().getMePwd())) {
                                mPresenter.shareDevice(2, gatewayId, deviceId, uid, phone, "", 1);
                            }
                        }else {
                            mPresenter.shareDevice(2, gatewayId, deviceId, uid, phone, "", 1);
                        }
                    }

                }
            }
        }

    }


    @Override
    public void shareDeviceSuccess(DeviceShareResultBean resultBean) {
        if (gatewayId!=null&&deviceId!=null&&uid!=null){
            mPresenter.getShareDeviceUser(gatewayId,deviceId,uid);
        }
        ToastUtils.showShort(R.string.add_common_user_success);
    }

    @Override
    public void shareDeviceFail() {
        ToastUtils.showShort(R.string.add_common_user_fail);
    }

    @Override
    public void shareDeviceThrowable() {
        ToastUtils.showShort(R.string.add_common_user_fail);
    }

    @Override
    public void shareUserListSuccess(List<DeviceShareUserResultBean.DataBean> shareUserBeans) {
        if (refreshLayout!=null){
            refreshLayout.finishRefresh();
        }
        if (shareUserBeans!=null&&shareUserBeans.size()>0){
            shareData.clear();
            shareData.addAll(shareUserBeans);
            pageChange(false);
        }else{
            pageChange(true);
        }
        if (shareAdapter!=null){
            shareAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void shareUserListFail() {
        if (refreshLayout!=null){
            refreshLayout.finishRefresh();
        }
        pageChange(true);
        ToastUtils.showShort(R.string.get_share_user_fail);
    }

    @Override
    public void shareUserListThrowable() {
        if (refreshLayout!=null){
            refreshLayout.finishRefresh();
        }
        pageChange(true);
        ToastUtils.showShort(R.string.get_share_user_fail);
    }

    @Override
    public void bindMimiSuccess(String deviceSN) {
        LogUtils.d("绑定咪咪网成功");
    }

    @Override
    public void bindMimiFail(String code, String msg) {
        LogUtils.d("绑定咪咪网失败");
    }

    @Override
    public void bindMimiThrowable(Throwable throwable) {
        LogUtils.d("绑定咪咪网失败");
    }
}
