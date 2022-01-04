package com.philips.easykey.lock.activity.device.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.BluetoothSharedDeviceManagementAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.ble.BluetoothSharedDeviceManagementPresenter;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.BluetoothSharedDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.mvp.view.IBluetoothSharedDeviceManagementView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by David
 */
public class BluetoothSharedDeviceManagementActivity extends BaseActivity<IBluetoothSharedDeviceManagementView, BluetoothSharedDeviceManagementPresenter<IBluetoothSharedDeviceManagementView>> implements IBluetoothSharedDeviceManagementView, View.OnClickListener {

    ImageView ivBack;//返回
    TextView tvContent;//标题
    RecyclerView recycleview;

    BluetoothSharedDeviceManagementAdapter bluetoothSharedDeviceManagementAdapter;
    List<BluetoothSharedDeviceBean.DataBean> list = new ArrayList<>();
    boolean isNotData = true;
    SmartRefreshLayout refreshLayout;

    TextView tvNoUser;
    RelativeLayout llAddUser;
    LinearLayout llHasData;

    private BleLockInfo bleLockInfo;//蓝牙锁信息
    public static final int REQUEST_CODE = 100;
    boolean querySuccess = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_shared_device_management);

        ivBack = findViewById(R.id.iv_back);//返回
        tvContent = findViewById(R.id.tv_content);//标题
        recycleview = findViewById(R.id.recycleview);
        refreshLayout = findViewById(R.id.refreshLayout);
        tvNoUser = findViewById(R.id.tv_no_user);
        llAddUser = findViewById(R.id.ll_add_user);
        llHasData = findViewById(R.id.ll_has_data);

        bluetoothSharedDeviceManagementAdapter = new BluetoothSharedDeviceManagementAdapter(list, R.layout.item_has_bluetooth_shared_device);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(bluetoothSharedDeviceManagementAdapter);
        bluetoothSharedDeviceManagementAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                BluetoothSharedDeviceBean.DataBean dataBean = list.get(position);
                Intent intent = new Intent(BluetoothSharedDeviceManagementActivity.this, FamilyMemberDetailActivity.class);
                intent.putExtra(KeyConstants.COMMON_FAMILY_MEMBER_DATA, dataBean);
                startActivity(intent);
            }
        });
        ivBack.setOnClickListener(this);
        llAddUser.setOnClickListener(this);
        tvContent.setText(getString(R.string.user_manage));
        bleLockInfo = MyApplication.getInstance().getBleService().getBleLockInfo();
        initRefresh();
    }

    @Override
    protected BluetoothSharedDeviceManagementPresenter<IBluetoothSharedDeviceManagementView> createPresent() {
        return new BluetoothSharedDeviceManagementPresenter<>();
    }

    private void initRefresh() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //清除数据
                list.clear();
                bluetoothSharedDeviceManagementAdapter.notifyDataSetChanged();
                //todo 获取数据 李绪进---家庭成员
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
        if (bleLockInfo != null && bleLockInfo.getServerLockInfo() != null && bleLockInfo.getServerLockInfo().getLockName() != null) {
            if (NetUtil.isNetworkAvailable()) {
                mPresenter.queryUserList(MyApplication.getInstance().getUid(), bleLockInfo.getServerLockInfo().getLockName());
            } else {
                ToastUtils.showShort(getString(R.string.philips_noNet));
            }

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
                if (querySuccess == true) {
                    if (list.size() < 10) {
                        intent = new Intent(this, AddBluetoothFamilyMemberActivity.class);
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

                } else {
                    ToastUtils.showShort(getString(R.string.query_fail_requery));
                }
                break;

        }
    }

    @Override
    public void queryCommonUserListSuccess(BluetoothSharedDeviceBean bluetoothSharedDeviceBean) {
        //刷新完成
        refreshLayout.finishRefresh();
        querySuccess = true;
        List<BluetoothSharedDeviceBean.DataBean> dataBeanList = bluetoothSharedDeviceBean.getData();
        if (dataBeanList !=null){
            SPUtils.putProtect(KeyConstants.USER_MANAGE_NUMBER+""+bleLockInfo.getServerLockInfo().getLockName(),dataBeanList.size() );
        }
        if (dataBeanList.size() > 0) {
            list.clear();
            list.addAll(dataBeanList);
            isNotData = false;
            bluetoothSharedDeviceManagementAdapter.notifyDataSetChanged();
        } else {
            isNotData = true;
        }
        pageChange();

    }

    @Override
    public void queryCommonUserListFail(BluetoothSharedDeviceBean bluetoothSharedDeviceBean) {
        querySuccess = false;
        //刷新完成
        refreshLayout.finishRefresh();
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, bluetoothSharedDeviceBean.getCode()));
    }

    @Override
    public void queryCommonUserListError(Throwable throwable) {
        querySuccess = false;
        //刷新完成
        refreshLayout.finishRefresh();
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void addCommonUserSuccess(BaseResult baseResult) {
        queryUser();
        ToastUtils.showShort(getString(R.string.add_common_user_success));
    }

    @Override
    public void addCommonUserFail(BaseResult baseResult) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, baseResult.getCode()));
    }

    @Override
    public void addCommonUserError(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE == requestCode) {
                String phone = data.getStringExtra(KeyConstants.AUTHORIZATION_TELEPHONE);
                String uid = MyApplication.getInstance().getUid();
                if (bleLockInfo == null) {
                    return;
                }
                String devmac = bleLockInfo.getServerLockInfo().getMacLock();   // 添加用户
                String device_name = bleLockInfo.getServerLockInfo().getLockName();
                String device_nickname = bleLockInfo.getServerLockInfo().getLockNickName();
                String time = System.currentTimeMillis() + "";
                List<String> items = new ArrayList<>();
                mPresenter.addCommonUser(uid, phone, devmac, device_name, System.currentTimeMillis() + "", device_nickname, "3", time, items);
            }
        }
    }
}
