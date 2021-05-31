package com.philips.easykey.lock.activity.device.gatewaylock.password;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.GatewayLockPasswordAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter.GatewayLockPasswordManagerPresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.IGatewayLockPasswordManagerView;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayPasswordPlanBean;
import com.philips.easykey.lock.publiclibrary.bean.GwLockInfo;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LoadingDialog;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.greenDao.manager.GatewayLockPasswordManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by David
 */
public class GatewayPasswordManagerActivity extends BaseActivity<IGatewayLockPasswordManagerView, GatewayLockPasswordManagerPresenter<IGatewayLockPasswordManagerView>>
        implements IGatewayLockPasswordManagerView, View.OnClickListener {

    ImageView ivBack;//返回
    TextView tvContent;//标题

    RecyclerView recycleview;
    /*   GatewayPasswordAdapter gatewayPasswordAdapter;*/

    LinearLayout llAddPassword;
    LinearLayout llHasData;
    TextView tvNoUser;
    TextView tvSynchronizedRecord;
    //List<ForeverPassword> pwdList = new ArrayList<>();
    private boolean isSync = false; //是不是正在同步锁中的密码

    private String gatewayId;
    private String deviceId;
    private GwLockInfo lockInfo;

    private List<GatewayPasswordPlanBean> mList = new ArrayList<>();

    private GatewayLockPasswordAdapter gatewayLockPasswordAdapter;

    private LoadingDialog loadingDialog;


    private String userId;

    private GatewayLockPasswordManager daoManager = new GatewayLockPasswordManager();
   String gatewayModel=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_password_manager);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        recycleview = findViewById(R.id.recycleview);
        llAddPassword = findViewById(R.id.ll_add_password);
        llHasData = findViewById(R.id.ll_has_data);
        tvNoUser = findViewById(R.id.tv_no_user);
        tvSynchronizedRecord = findViewById(R.id.tv_synchronized_record);

        initView();
        initListener();
        initData();
    }

    @Override
    protected GatewayLockPasswordManagerPresenter<IGatewayLockPasswordManagerView> createPresent() {
        return new GatewayLockPasswordManagerPresenter<>();
    }

    private void initView() {
        tvContent.setText(getString(R.string.password));
        loadingDialog = LoadingDialog.getInstance(this);
        initRecycleview();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        llAddPassword.setOnClickListener(this);
    }


    private void initRecycleview() {
        gatewayLockPasswordAdapter = new GatewayLockPasswordAdapter(mList);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(gatewayLockPasswordAdapter);
        gatewayLockPasswordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(GatewayPasswordManagerActivity.this, GatewayLockDeletePasswordActivity.class);
                if (gatewayId != null && deviceId != null) {
                    intent.putExtra(KeyConstants.GATEWAY_ID, gatewayId);
                    intent.putExtra(KeyConstants.DEVICE_ID, deviceId);
                    GatewayPasswordPlanBean item = (GatewayPasswordPlanBean) adapter.getItem(position);
                    intent.putExtra(KeyConstants.GATEWAY_PASSWORD_BEAN, item);
                    startActivityForResult(intent, KeyConstants.DELETE_PWD_REQUEST_CODE);
                }
            }
        });
        tvSynchronizedRecord.setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            if(!deviceId.isEmpty() && !gatewayId.isEmpty()){

                //从数据库获取数据
                List<GatewayPasswordPlanBean> gatewayPasswordPlanBeans = daoManager.queryAll(deviceId, MyApplication.getInstance().getUid(), gatewayId);
                mList.clear();

                List<GatewayPasswordPlanBean> temp =new ArrayList<>();
                temp.addAll(gatewayPasswordPlanBeans);

                for (int i=0;i<temp.size();i++){
                    if(temp.get(i).getPasswordNumber()==9){
                        GatewayPasswordPlanBean gatewayPasswordPlanBean= temp.get(i);
                        gatewayPasswordPlanBeans.remove(gatewayPasswordPlanBean);
                    }
                }


                mList.addAll(gatewayPasswordPlanBeans);
                if (gatewayPasswordPlanBeans.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        mList.sort(Comparator.naturalOrder());
                    }
                    gatewayLockPasswordAdapter.notifyDataSetChanged();
                    passwordPageChange(true);
                } else {
                    passwordPageChange(false);
                }
            }
        }catch (Exception e){

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d("onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void passwordPageChange(boolean havePwd) {
        if (havePwd) {
            if (llHasData != null && tvNoUser != null) {
                llHasData.setVisibility(View.VISIBLE);
                tvNoUser.setVisibility(View.GONE);
            }
        } else {
            if (llHasData != null && tvNoUser != null) {
                llHasData.setVisibility(View.GONE);
                tvNoUser.setVisibility(View.VISIBLE);
            }
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
                //新版锁
                String lockversion = lockInfo.getServerInfo().getLockversion();
                LogUtils.d("获取到 版本信息是   " + lockversion);
                if (!TextUtils.isEmpty(lockversion)) {
                    String lockModel = lockversion.split(";")[0];
                    if (!TextUtils.isEmpty(lockModel) &&( lockModel.equalsIgnoreCase("8100Z") ||  lockModel.equalsIgnoreCase("8100A"))) {
                        intent = new Intent(this, GatewayPasswordAddActivity.class);
                        intent.putExtra(KeyConstants.GATEWAY_ID, gatewayId);
                        intent.putExtra(KeyConstants.DEVICE_ID, deviceId);
                        if(!TextUtils.isEmpty(gatewayModel)  && gatewayModel.equals(KeyConstants.SMALL_GW2)){
                            intent.putExtra(KeyConstants.GATEWAY_MODEL,KeyConstants.SMALL_GW2);
                        }
                        startActivity(intent);
                        return;
                    }
                }
                intent = new Intent(this, GatewayLockTempararyPwdAddActivity.class);
                intent.putExtra(KeyConstants.GATEWAY_ID, gatewayId);
                intent.putExtra(KeyConstants.DEVICE_ID, deviceId);
                if(!TextUtils.isEmpty(gatewayModel)  && gatewayModel.equals(KeyConstants.SMALL_GW2)){
                    intent.putExtra(KeyConstants.GATEWAY_MODEL,KeyConstants.SMALL_GW2);
                }

//                intent = new Intent(this, PasswordTestActivity.class);
//                intent.putExtra(KeyConstants.GATEWAY_ID, gatewayId);
//                intent.putExtra(KeyConstants.DEVICE_ID, deviceId);
                startActivity(intent);
                break;
            case R.id.tv_synchronized_record:
                  loadingDialog.show2(getString(R.string.get_gateway_lock_pwd_waiting));
                if(!TextUtils.isEmpty(gatewayModel)  &&  gatewayModel.equals(KeyConstants.SMALL_GW2)){
                   mPresenter.sysPassworByhttp(MyApplication.getInstance().getUid(),gatewayId,deviceId,"",null);
                }else{
//                    //点击同步
                     mPresenter.syncPassword(gatewayId, deviceId);
                }

                break;
        }
    }

    public void initData() {
        Intent intent = getIntent();
        gatewayId = intent.getStringExtra(KeyConstants.GATEWAY_ID) + "";
        deviceId = intent.getStringExtra(KeyConstants.DEVICE_ID) + "";
        lockInfo = (GwLockInfo) intent.getSerializableExtra(KeyConstants.GATEWAY_LOCK_INFO);
        userId = MyApplication.getInstance().getUid();
        gatewayModel =getIntent().getStringExtra(KeyConstants.GATEWAY_MODEL);
    }



    @Override
    public void getLockInfoSuccess(int pwdNum) {
        //获取到总的次数
    }

    @Override
    public void getLockInfoFail() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        //passwordPageChange(false);
        ToastUtils.showShort(getString(R.string.get_lock_info_fail));
        LogUtils.d("获取到锁信息失败   ");
    }

    @Override
    public void getLockInfoThrowable(Throwable throwable) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        //passwordPageChange(false);
        ToastUtils.showShort(getString(R.string.get_lock_info_fail));
        LogUtils.d("获取到锁信息异常   " + throwable.getMessage());
    }

    @Override
    public void syncPasswordComplete(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if(passwordPlanBeans==null){
            ToastUtils.showLong(getString(R.string.pwd_list_null));
            return;
        }
        SPUtils.put(KeyConstants.FIRST_IN_GATEWAY_LOCK + userId + deviceId, 1);
        List<GatewayPasswordPlanBean> gatewayPasswordPlanBeans = parsePassword(passwordPlanBeans);
        LogUtils.d("获取全部密码  1  " + Arrays.toString(gatewayPasswordPlanBeans.toArray()));
        daoManager.insertAfterDelete(deviceId, MyApplication.getInstance().getUid(), gatewayId, gatewayPasswordPlanBeans);
        mList.clear();
        mList.addAll(gatewayPasswordPlanBeans);
        if (mList.size() > 0) {
            passwordPageChange(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mList.sort(Comparator.naturalOrder());
            }
            gatewayLockPasswordAdapter.notifyDataSetChanged();
        } else {
            passwordPageChange(false);
        }
    }


    @Override
    public void syncPasswordFailed(Throwable throwable) {
        LogUtils.d("获取开锁密码异常   " + throwable.getMessage());
        loadingDialog.dismiss();
        ToastUtils.showShort(R.string.get_lock_pwd_list_fail);
        passwordPageChange(false);
    }

    @Override
    public void addLockPwdFail(Throwable throwable) {

    }

    @Override
    public void addLockPwdSuccess(GatewayPasswordPlanBean gatewayPasswordPlanBean, String pwdValue) {

    }

    @Override
    public void setUserTypeSuccess(String passwordValue, GatewayPasswordPlanBean gatewayPasswordPlanBean) {

    }

    @Override
    public void onLoadPasswordPlan(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {
        LogUtils.d("");
        List<GatewayPasswordPlanBean> gatewayPasswordPlanBeans = parsePassword(passwordPlanBeans);
        LogUtils.d("获取全部密码   2 " + Arrays.toString(gatewayPasswordPlanBeans.toArray()));
        daoManager.insertAfterDelete(deviceId, MyApplication.getInstance().getUid(), gatewayId, gatewayPasswordPlanBeans);
        mList.clear();
        mList.addAll(gatewayPasswordPlanBeans);
        passwordPageChange(true);
    }

    @Override
    public void onLoadPasswordPlanFailed(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showLong(R.string.synv_failed);

    }

    @Override
    public void onLoadPasswordPlanComplete(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {
        List<GatewayPasswordPlanBean> gatewayPasswordPlanBeans = parsePassword(passwordPlanBeans);
        LogUtils.d("获取全部密码   2 " + Arrays.toString(gatewayPasswordPlanBeans.toArray()));
        daoManager.insertAfterDelete(deviceId, MyApplication.getInstance().getUid(), gatewayId, gatewayPasswordPlanBeans);
        mList.clear();
        mList.addAll(gatewayPasswordPlanBeans);
        if (mList.size() > 0) {
            passwordPageChange(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mList.sort(Comparator.naturalOrder());
            }
            gatewayLockPasswordAdapter.notifyDataSetChanged();
        } else {
            passwordPageChange(false);
        }
        hiddenLoading();
    }


    @Override
    public void setUserTypeFailed(Throwable typeFailed) {

    }

    @Override
    public void setPlanSuccess(String passwordValue, GatewayPasswordPlanBean gatewayPasswordPlanBean) {

    }

    @Override
    public void setPlanFailed(Throwable throwable) {

    }

    @Override
    public void deletePasswordSuccess() {

    }

    @Override
    public void deletePasswordFailed(Throwable throwable) {

    }

    @Override
    public void gatewayPasswordFull() {

    }

    public List<GatewayPasswordPlanBean> parsePassword(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {
        List<GatewayPasswordPlanBean> passwords = new ArrayList<>();
        for (Integer number : passwordPlanBeans.keySet()) {
            passwords.add(passwordPlanBeans.get(number));
        }
        return passwords;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void isSyncPlan() {
        showLoading(getString(R.string.is_sync_password_plan));
    }


}
