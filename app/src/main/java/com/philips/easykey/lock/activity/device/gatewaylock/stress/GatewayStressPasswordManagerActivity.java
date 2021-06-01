package com.philips.easykey.lock.activity.device.gatewaylock.stress;

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
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.GatewayStressFingerprintAdapter;
import com.philips.easykey.lock.adapter.GatewayStressPasswordAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;
import com.philips.easykey.lock.publiclibrary.http.postbean.AddPasswordBean;
import com.philips.easykey.lock.publiclibrary.http.result.GetPasswordResult;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by David
 */
public class GatewayStressPasswordManagerActivity extends BaseAddToApplicationActivity
        implements View.OnClickListener {

    ImageView ivBack;//返回
    TextView tvContent;//标题

    RecyclerView recycleviewPassword;
    GatewayStressPasswordAdapter gatewayStressPasswordAdapter;
    LinearLayout llAddPassword;
    List<ForeverPassword> pwdList = new ArrayList<>();
    List<GetPasswordResult.DataBean.Fingerprint> finperprintList = new ArrayList<>();
    LinearLayout llAddFingerprint;
    RecyclerView recycleviewFingerprint;
    ImageView ivAppNotification;
    RelativeLayout rlAppNotification;
    GatewayStressFingerprintAdapter gatewayStressFingerprintAdapter;
    boolean appNotificationStatus = true;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_stress_password_manager);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        recycleviewPassword = findViewById(R.id.recycleview_password);
        llAddPassword = findViewById(R.id.ll_add_password);
        llAddFingerprint = findViewById(R.id.ll_add_fingerprint);
        recycleviewFingerprint = findViewById(R.id.recycleview_fingerprint);
        ivAppNotification = findViewById(R.id.iv_app_notification);
        rlAppNotification = findViewById(R.id.rl_app_notification);

        tvContent.setText(getString(R.string.stress_warn));
        ivBack.setOnClickListener(this);
        llAddPassword.setOnClickListener(this);
        llAddFingerprint.setOnClickListener(this);
        rlAppNotification.setOnClickListener(this);
        initRecycleview();
        initData();
        pwdList.add(new ForeverPassword("fff", "fff", 1));
        gatewayStressPasswordAdapter.notifyDataSetChanged();
        finperprintList.add(new GetPasswordResult.DataBean.Fingerprint("fff", "fff", 1));
        gatewayStressFingerprintAdapter.notifyDataSetChanged();
    }

    private void initData() {
        appNotificationStatus = (boolean) SPUtils.get(KeyConstants.APP_NOTIFICATION_STATUS, true);
        if (appNotificationStatus) {
            ivAppNotification.setImageResource(R.mipmap.iv_open);
        } else {
            ivAppNotification.setImageResource(R.mipmap.iv_close);
        }
    }

    private void initRecycleview() {
        gatewayStressPasswordAdapter = new GatewayStressPasswordAdapter(pwdList, R.layout.item_gateway_stress_password);
        recycleviewPassword.setLayoutManager(new LinearLayoutManager(this));
        recycleviewPassword.setAdapter(gatewayStressPasswordAdapter);
        gatewayStressPasswordAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(GatewayStressPasswordManagerActivity.this, GatewayStressWarnDetailActivity.class);
                AddPasswordBean.Password password = new AddPasswordBean.Password(1, pwdList.get(position));
                intent.putExtra(KeyConstants.TO_PWD_DETAIL, password);
                intent.putExtra(KeyConstants.CREATE_TIME, pwdList.get(position).getCreateTime());
                startActivity(intent);
            }
        });

        gatewayStressFingerprintAdapter = new GatewayStressFingerprintAdapter(finperprintList, R.layout.item_gateway_stress_fingerprint);
        recycleviewFingerprint.setLayoutManager(new LinearLayoutManager(this));
        recycleviewFingerprint.setAdapter(gatewayStressFingerprintAdapter);
        gatewayStressFingerprintAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(GatewayStressPasswordManagerActivity.this, GatewayStressWarnDetailActivity.class);
                intent.putExtra(KeyConstants.PASSWORD_NICK, finperprintList.get(position));
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d("密码管理界面  onResume()   ");
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_add_password:
                intent = new Intent(this, GatewayStressPasswordAddActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_add_fingerprint:

                break;
            case R.id.rl_app_notification:
                if (appNotificationStatus) {
                    //打开状态 现在关闭
                    ivAppNotification.setImageResource(R.mipmap.iv_close);
                    SPUtils.put(KeyConstants.APP_NOTIFICATION_STATUS, false);
                } else {
                    //关闭状态 现在打开
                    ivAppNotification.setImageResource(R.mipmap.iv_open);
                    SPUtils.put(KeyConstants.APP_NOTIFICATION_STATUS, true);
                }
                appNotificationStatus = !appNotificationStatus;
                break;

        }
    }

}
