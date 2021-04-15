package com.philips.easykey.lock.activity.device.gatewaylock.fingerprint;

import android.content.Intent;
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
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.GatewayFingerprintManagerAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.http.result.GetPasswordResult;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David
 */
public class GatewayFingerprintManagerActivity extends BaseAddToApplicationActivity
        implements View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;//返回
    @BindView(R.id.tv_content)
    TextView tvContent;//标题

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    GatewayFingerprintManagerAdapter gatewayFingerprintManagerAdapter;
    boolean isNotData = true;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.ll_has_data)
    LinearLayout llHasData;
    @BindView(R.id.tv_no_user)
    TextView tvNoUser;
    List<GetPasswordResult.DataBean.Fingerprint> list = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_fingerprint_manager);
        ButterKnife.bind(this);
        tvContent.setText(getString(R.string.fingerprint));
        ivBack.setOnClickListener(this);
        llAdd.setOnClickListener(this);
        pageChange();
        initRecycleview();
        list.add(new GetPasswordResult.DataBean.Fingerprint("fff", "fff", 1));
        initData();
    }

    private void initRecycleview() {
        gatewayFingerprintManagerAdapter = new GatewayFingerprintManagerAdapter(list, R.layout.item_gateway_fingerprint_manager);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.setAdapter(gatewayFingerprintManagerAdapter);
        gatewayFingerprintManagerAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(GatewayFingerprintManagerActivity.this, GatewayFingerprintManagerDetailActivity.class);
                intent.putExtra(KeyConstants.PASSWORD_NICK, list.get(position));
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
                    ToastUtil.getInstance().showShort(R.string.please_add_finger);
                    return;
                }
//                intent = new Intent(this, FingerprintLinkBluetoothActivity.class);
//                startActivity(intent);
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
        gatewayFingerprintManagerAdapter.notifyDataSetChanged();
    }
}
