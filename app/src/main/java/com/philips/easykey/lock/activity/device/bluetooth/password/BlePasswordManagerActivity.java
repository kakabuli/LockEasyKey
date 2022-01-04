package com.philips.easykey.lock.activity.device.bluetooth.password;

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
import com.philips.easykey.lock.adapter.BluetoothPasswordAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleActivity;
import com.philips.easykey.lock.mvp.presenter.ble.PasswordManagerPresenter;
import com.philips.easykey.lock.mvp.view.IPasswordManagerView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;
import com.philips.easykey.lock.publiclibrary.http.postbean.AddPasswordBean;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetPasswordResult;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
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
public class BlePasswordManagerActivity extends BaseBleActivity<IPasswordManagerView, PasswordManagerPresenter<IPasswordManagerView>>
        implements View.OnClickListener, IPasswordManagerView {

    ImageView ivBack;//返回
    TextView tvContent;//标题

    RecyclerView recycleview;
    BluetoothPasswordAdapter bluetoothPasswordAdapter;
    boolean isNotPassword = true;
    TextView tvSynchronizedRecord;
    LinearLayout llAddPassword;
    LinearLayout llHasData;
    TextView tvNoUser;
    List<ForeverPassword> list = new ArrayList<>();
    SmartRefreshLayout refreshLayout;
    ImageView ivRight;
    private BleLockInfo bleLockInfo;
    private boolean isSync = false; //是不是正在同步锁中的密码


    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_password_manager);

        ivBack = findViewById(R.id.iv_back);//返回
        tvContent = findViewById(R.id.tv_content);//标题
        recycleview = findViewById(R.id.recycleview);
        tvSynchronizedRecord = findViewById(R.id.tv_synchronized_record);
        llAddPassword = findViewById(R.id.ll_add_password);
        llHasData = findViewById(R.id.ll_has_data);
        tvNoUser = findViewById(R.id.tv_no_user);
        refreshLayout = findViewById(R.id.refreshLayout);
        ivRight = findViewById(R.id.iv_right);

        tvContent.setText(getString(R.string.password));
        if(MyApplication.getInstance().getBleService().getBleLockInfo() != null){

            bleLockInfo = MyApplication.getInstance().getBleService().getBleLockInfo();
            mPresenter.getAllPassword(bleLockInfo, true);
        }
        ivBack.setOnClickListener(this);
        tvSynchronizedRecord.setOnClickListener(this);
        llAddPassword.setOnClickListener(this);
        passwordPageChange();
        initRecycleview();
        initRefresh();
    }

    @Override
    protected PasswordManagerPresenter<IPasswordManagerView> createPresent() {
        return new PasswordManagerPresenter<>();
    }

    private void initRecycleview() {
        bluetoothPasswordAdapter = new BluetoothPasswordAdapter(list, R.layout.item_bluetooth_password);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(bluetoothPasswordAdapter);
        bluetoothPasswordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(BlePasswordManagerActivity.this, BluetoothPasswordManagerDetailActivity.class);
                String num = list.get(position).getNum();
                int number = Integer.parseInt(num);
                int pwdType ;
                if (number > 4 && number < 9) {
                    pwdType = 2;
                } else   {
                    pwdType = 1;
                }
                AddPasswordBean.Password password = new AddPasswordBean.Password(pwdType, list.get(position));
                intent.putExtra(KeyConstants.TO_PWD_DETAIL, password);
                intent.putExtra(KeyConstants.CREATE_TIME, list.get(position).getCreateTime());
                startActivity(intent);
            }
        });
    }

    private void initRefresh() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //下拉刷新   如果正在同步，不刷新
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
    protected void onResume() {
        super.onResume();
        LogUtils.d("密码管理界面  onResume()   ");
        mPresenter.isAuth(bleLockInfo, false); //查看是否需要重现连接
        mPresenter.getAllPassword(bleLockInfo, true);
        LogUtils.d("密码管理界面  onResume()   ");
    }

    public void passwordPageChange() {
        if (isNotPassword) {
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
            case R.id.ll_add_password:
                intent = new Intent(this, BluetoothUserPasswordAddActivity.class);
                startActivity(intent);
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


    @Override
    public void onSyncPasswordSuccess(List<ForeverPassword> pwdList) {
        list = pwdList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(Comparator.naturalOrder());
        }
        if (list.size() > 0) {
            isNotPassword = false;
        } else {
            isNotPassword = true;
        }
        processType(list);
        passwordPageChange();
//        bluetoothPasswordAdapter.notifyDataSetChanged();
        initRecycleview();
        LogUtils.d("收到  同步的锁的密码   " + list.toString());
    }

    private void processType(List<ForeverPassword> pwdList) {
        for (int i=0;i<pwdList.size();i++){
            ForeverPassword foreverpassword = pwdList.get(i);
            String num = foreverpassword.getNum();
            if ("05".equals(num)||"06".equals(num)||"07".equals(num)||"08".equals(num)){
                foreverpassword.setType(5);
            }
        }
    }

    @Override
    public void onSyncPasswordFailed(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.syc_pwd_fail));
    }

    @Override
    public void onUpdate(List<ForeverPassword> pwdList) {
        if (bluetoothPasswordAdapter != null && !isFinishing()) {
            bluetoothPasswordAdapter.notifyDataSetChanged();
        }
        list = pwdList;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(Comparator.naturalOrder());
        }
        processType(list);
        isNotPassword = false;
        passwordPageChange();
    }

    @Override
    public void startSync() {
        isSync = true;
        showLoading(getString(R.string.is_sync_lock_data));
    }

    @Override
    public void endSync() {
        isSync = false;
        ToastUtils.showLong(R.string.sync_success);
        hiddenLoading();
    }

    @Override
    public void onServerDataUpdate() {
        LogUtils.d("密码管理   服务器数据更新   ");
        mPresenter.getAllPassword(bleLockInfo, true);
    }

    @Override
    public void onGetPasswordFailedServer(BaseResult result) {
//        isNotPassword = true;
//        passwordPageChange();
        ToastUtils.showShort(R.string.get_password_failed);
        refreshLayout.finishRefresh();
    }

    @Override
    public void onGetPasswordSuccess(GetPasswordResult result) {
        refreshLayout.finishRefresh();
        if (isSync) {
            return;
        }

        if (result == null ){
            isNotPassword = true;
            passwordPageChange();
            return;
        }
        if (result.getData() == null ){
            isNotPassword = true;
            passwordPageChange();
            return;
        }
        if (result.getData().getPwdList() == null ){
            isNotPassword = true;
            passwordPageChange();
            return;
        }

        list = result.getData().getPwdList();
        List<GetPasswordResult.DataBean.TempPassword> tempPwdList = result.getData().getTempPwdList();
        for (int i=0;i<tempPwdList.size();i++){
            GetPasswordResult.DataBean.TempPassword tempPassword = tempPwdList.get(i);
            ForeverPassword foreverPassword=new ForeverPassword();
            foreverPassword.setNum(tempPassword.getNum());
            foreverPassword.setNickName(tempPassword.getNickName());
            foreverPassword.setCreateTime(tempPassword.getCreateTime());
//            foreverPassword.setType(5);
            list.add(foreverPassword);

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            list.sort(Comparator.naturalOrder());
        }

        processType(list);
        LogUtils.d("获取到的结果，    " + result.getData().getPwdList().toString());
        initRecycleview();
        if (result.getData().getPwdList().size() > 0) {
            isNotPassword = false;
            passwordPageChange();
        } else {
            isNotPassword = true;
            passwordPageChange();
        }

    }

    @Override
    public void onGetPasswordFailed(Throwable throwable) {
        refreshLayout.finishRefresh();
        if (isSync) {
            return;
        }
        ToastUtils.showShort(R.string.get_password_failed);
    }
}
