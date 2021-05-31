package com.philips.easykey.lock.activity.device.gatewaylock.stress.old;

import android.app.Activity;
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
import com.blankj.utilcode.util.ToastUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.GatewayLockStressPasswordAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter.GatewayLockStressDetailPresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.IGatewayLockStressDetailView;
import com.philips.easykey.lock.publiclibrary.http.result.SwitchStatusResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LoadingDialog;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils2;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.ftp.GeTui;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayLockPwd;
import com.philips.easykey.lock.utils.greenDao.db.GatewayLockPwdDao;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by David
 */
public class GatewayLockStressDetailActivity extends BaseActivity<IGatewayLockStressDetailView, GatewayLockStressDetailPresenter<IGatewayLockStressDetailView>> implements IGatewayLockStressDetailView {

    ImageView back;
    TextView headTitle;
    LinearLayout llAddPassword;
    RecyclerView recycleviewPassword;
    ImageView ivAppNotification;
    RelativeLayout rlAppNotification;

    List<String> pwdList = new ArrayList<>();
    //boolean appNotificationStatus = true;
    GatewayLockStressPasswordAdapter gatewayLockStressPasswordAdapter;
    TextView tvSynchronizedRecord;
    LinearLayout llHasData;
    TextView tvNoUser;

    private LoadingDialog loadingDialog;
    private String gatewayId;
    private String deviceId;
    //0表示列表还没有获取完成，无法添加，1表示列表获取异常无法添加，2表示可以进行添加
    private int isAddLockPwd = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_lock_stress_password_manager);

        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        llAddPassword = findViewById(R.id.ll_add_password);
        recycleviewPassword = findViewById(R.id.recycleview_password);
        ivAppNotification = findViewById(R.id.iv_app_notification);
        rlAppNotification = findViewById(R.id.rl_app_notification);
        tvSynchronizedRecord = findViewById(R.id.tv_synchronized_record);
        llHasData = findViewById(R.id.ll_has_data);
        tvNoUser = findViewById(R.id.tv_no_user);

        back.setOnClickListener(v -> finish());
        llAddPassword.setOnClickListener(v -> {
            if (isAddLockPwd == 0) {
                ToastUtils.showShort(R.string.be_beging_get_lock_pwd_no_add_pwd);
                return;
            } else if (isAddLockPwd == 1) {
                ToastUtils.showShort(R.string.get_lock_pwd_throwable);
                return;
            } else if (isAddLockPwd == 2) {
                if (pwdList.size() < 1) {
                    Intent intent = new Intent(this, GatewayLockStressAddActivity.class);
                    intent.putExtra(KeyConstants.GATEWAY_ID, gatewayId);
                    intent.putExtra(KeyConstants.DEVICE_ID, deviceId);
                    startActivity(intent);
                } else {
                    AlertDialogUtil.getInstance().singleButtonNoTitleDialog(this, getString(R.string.password_full_and_delete_exist_code), getString(R.string.hao_de), "#1F96F7", new AlertDialogUtil.ClickListener() {
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
        });
        rlAppNotification.setOnClickListener(v -> {
            //同步
            mPresenter.getLockPwd(gatewayId, deviceId, "09");
            loadingDialog.show(getString(R.string.get_stress_pwd_stop));
        });
        tvSynchronizedRecord.setOnClickListener(v -> {
            isopenlockPushSwitch = !isopenlockPushSwitch;
            mPresenter.updatePushSwitch(isopenlockPushSwitch);
//                if(isopenlockPushSwitch){
//                     // 打开状态
//                    ivAppNotification.setImageResource(R.mipmap.iv_close);
//                }else{
//                    // 关闭状态
//                    ivAppNotification.setImageResource(R.mipmap.iv_open);
//                }

//                if (appNotificationStatus) {
//                    //打开状态 现在关闭
//                    ivAppNotification.setImageResource(R.mipmap.iv_close);
//                    SPUtils.put(KeyConstants.APP_NOTIFICATION_STATUS, false);
//                } else {
//                    //关闭状态 现在打开
//                    ivAppNotification.setImageResource(R.mipmap.iv_open);
//                    SPUtils.put(KeyConstants.APP_NOTIFICATION_STATUS, true);
//                }
//                appNotificationStatus = !appNotificationStatus;
        });

        initView();
        initRecycleview();
        initData();

    }

    private void initView() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog.getInstance(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (pwdList != null) {
            String pwdId = (String) SPUtils2.get(this, KeyConstants.ADD_STRESS_PWD_ID, "");
            if (!TextUtils.isEmpty(pwdId)) {
                if (!pwdList.contains(pwdId)){
                    pwdList.clear();
                    pwdList.add(pwdId);
                    gatewayLockStressPasswordAdapter.notifyDataSetChanged();
                    passwordPageChange(true);
                    SPUtils2.remove(this, KeyConstants.ADD_STRESS_PWD_ID);
                }
            }
        }
    }

    @Override
    protected GatewayLockStressDetailPresenter<IGatewayLockStressDetailView> createPresent() {
        return new GatewayLockStressDetailPresenter<>();
    }

    private void initData() {
        Intent intent = getIntent();
        gatewayId = intent.getStringExtra(KeyConstants.GATEWAY_ID);
        deviceId = intent.getStringExtra(KeyConstants.DEVICE_ID);

        //   appNotificationStatus = (boolean) SPUtils.get(KeyConstants.APP_NOTIFICATION_STATUS, true);
        mPresenter.getLockPwdInfoEvent();
        if (gatewayId != null && deviceId != null) {
            //胁迫密码固定09编号
            //查找本地数据是否有编号09
            List<GatewayLockPwd> gatewayLockPwd = MyApplication.getInstance().getDaoWriteSession().queryBuilder(GatewayLockPwd.class).where(GatewayLockPwdDao.Properties.Num.eq("09"),GatewayLockPwdDao.Properties.GatewayId.eq(gatewayId),GatewayLockPwdDao.Properties.DeviceId.eq(deviceId)).list();
            if (gatewayLockPwd != null&&gatewayLockPwd.size()==1) {
                isAddLockPwd = 2;
                if (gatewayLockPwd.get(0).getStatus() == 1) {
                    pwdList.add("09");
                    gatewayLockStressPasswordAdapter.notifyDataSetChanged();
                    passwordPageChange(true);
                }else{
                    passwordPageChange(false);
                }
            } else {
                mPresenter.getLockPwd(gatewayId, deviceId, "09");
                loadingDialog.show(getString(R.string.get_stress_pwd_stop));
            }
        }
        //mPresenter.getPushSwitch();

    }

    private void initRecycleview() {
        gatewayLockStressPasswordAdapter = new GatewayLockStressPasswordAdapter(pwdList, R.layout.item_gateway_stress_password);
        recycleviewPassword.setLayoutManager(new LinearLayoutManager(this));
        recycleviewPassword.setAdapter(gatewayLockStressPasswordAdapter);
        gatewayLockStressPasswordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(GatewayLockStressDetailActivity.this, GatewayLockStressDeleteActivity.class);
                intent.putExtra(KeyConstants.GATEWAY_ID, gatewayId);
                intent.putExtra(KeyConstants.DEVICE_ID, deviceId);
                intent.putExtra(KeyConstants.LOCK_PWD_NUMBER, "09");
                startActivityForResult(intent, KeyConstants.DELETE_PWD_REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("密码管理界面  onResume()   ");

    }

    public void passwordPageChange(boolean havePwd) {

        if (havePwd) {
            llHasData.setVisibility(View.VISIBLE);
            tvNoUser.setVisibility(View.GONE);
        } else {
            llHasData.setVisibility(View.GONE);
            tvNoUser.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void getStressPwdSuccess(int status) {
        //获取成功
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        isAddLockPwd = 2;
        if (status == 1) {
            pwdList.clear();
            pwdList.add("09");
            gatewayLockStressPasswordAdapter.notifyDataSetChanged();
            passwordPageChange(true);
        }else{
            passwordPageChange(false);
        }
        ToastUtils.showShort(R.string.get_stress_list_success);
    }

    @Override
    public void getStressPwdSuccessNoPwd(int status) {
        //获取成功
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        isAddLockPwd = 2;
        if (status == 0) {
            pwdList.clear();
         //   pwdList.add("09");
            gatewayLockStressPasswordAdapter.notifyDataSetChanged();
            passwordPageChange(false);
        }else{
            passwordPageChange(false);
        }
        ToastUtils.showShort(R.string.get_stress_list_success_no);
    }

    @Override
    public void getStressPwdFail() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        isAddLockPwd = 1;
        passwordPageChange(false);
        //获取失败
        ToastUtils.showShort(R.string.get_stress_list_fail);

    }

    @Override
    public void getStreessPwdThrowable(Throwable throwable) {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        isAddLockPwd = 1;
        //获取异常
        ToastUtils.showShort(R.string.get_stress_list_fail);
        passwordPageChange(false);
        LogUtils.d("获取胁迫密码异常   " + throwable.getMessage());
    }

    boolean isopenlockPushSwitch = true;

    @Override
    public void getSwitchStatus(SwitchStatusResult switchStatusResult) {
        Log.e(GeTui.VideoLog, "switchStatusResult:" + switchStatusResult);
        // Toast.makeText(this,switchStatusResult.toString(), Toast.LENGTH_LONG).show();
        if (switchStatusResult.getCode().equals("200")) {
            isopenlockPushSwitch = switchStatusResult.getData().isOpenlockPushSwitch();
            if (isopenlockPushSwitch) {
                ivAppNotification.setImageResource(R.mipmap.iv_open);
            } else {
                ivAppNotification.setImageResource(R.mipmap.iv_close);
            }
        }
    }

    @Override
    public void getSwitchFail() {
        ToastUtils.showShort(getString(R.string.get_swtich_status_fail));
    }

    @Override
    public void updateSwitchStatus(SwitchStatusResult switchStatusResult) {
        Log.e(GeTui.VideoLog, "更新以后状态是:" + switchStatusResult);
        if (isopenlockPushSwitch) {
            // 打开状态
            ivAppNotification.setImageResource(R.mipmap.iv_open);
        } else {
            // 关闭状态
            ivAppNotification.setImageResource(R.mipmap.iv_close);
        }
    }

    @Override
    public void updateSwitchUpdateFail() {
        isopenlockPushSwitch = !isopenlockPushSwitch;
        ToastUtils.showShort(getString(R.string.update_swtich_status_fail));

    }

    @Override
    public void addOnePwdLock(String pwdId) {
        if (pwdId.equals("09")){
            pwdList.clear();
            pwdList.add(pwdId);
        }
        if (gatewayLockStressPasswordAdapter!=null) {
            gatewayLockStressPasswordAdapter.notifyDataSetChanged();
            passwordPageChange(true);
        }
    }

    @Override
    public void deleteOnePwdLock(String pwdId) {
        if (pwdId.equals("09")){
            if (pwdList.contains(pwdId)){
                pwdList.remove(pwdId);
            }
        }
        if (gatewayLockStressPasswordAdapter!=null) {
            gatewayLockStressPasswordAdapter.notifyDataSetChanged();
            passwordPageChange(false);
        }
    }

    @Override
    public void deleteAllPwdLock() {
        pwdList.clear();
        if (gatewayLockStressPasswordAdapter!=null) {
            gatewayLockStressPasswordAdapter.notifyDataSetChanged();
            passwordPageChange(false);
        }
    }

    @Override
    public void useSingleUse(String pwdId) {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KeyConstants.DELETE_PWD_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String lockNumber = data.getStringExtra(KeyConstants.LOCK_PWD_NUMBER);
                if (!TextUtils.isEmpty(lockNumber)) {
                    pwdList.remove(lockNumber);
                    passwordPageChange(false);
                    gatewayLockStressPasswordAdapter.notifyDataSetChanged();
                }
            }
        }
    }

}
