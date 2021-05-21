package com.philips.easykey.lock.activity.device.videolock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.PhilipsDuressAlarmAdapter;
import com.philips.easykey.lock.bean.PhilipsDuressBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.PhilipsWifiVideoLockDuressPresenter;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockMorePresenter;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IPhilipsWifiVideoLockDuressView;
import com.philips.easykey.lock.mvp.view.wifilock.videolock.IWifiVideoLockMoreView;
import com.philips.easykey.lock.publiclibrary.bean.WiFiLockPassword;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StatusBarUtils;

import java.util.List;

public class PhilipsWifiVideoLockDuressAlarmAvtivity extends BaseActivity<IPhilipsWifiVideoLockDuressView, PhilipsWifiVideoLockDuressPresenter<IPhilipsWifiVideoLockDuressView>>
        implements IPhilipsWifiVideoLockDuressView {

    private RecyclerView recycler;
    private ImageView mIvDuressSelect;
    private ImageView mBack;
    private String wifiSn = "";
    private PhilipsDuressAlarmAdapter mPhilipsDuressAlarmAdapter;
    private List<PhilipsDuressBean> duressList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_video_lock_duress_alarm);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.white);
        initView();
        initListener();
        initData();
    }

    @Override
    protected PhilipsWifiVideoLockDuressPresenter<IPhilipsWifiVideoLockDuressView> createPresent() {
        return new PhilipsWifiVideoLockDuressPresenter<>();
    }

    private void initData() {
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        initRecycleViewData();
        String localPasswordCache = (String) SPUtils.get(KeyConstants.WIFI_LOCK_PASSWORD_LIST + wifiSn, "");
        WiFiLockPassword wiFiLockPassword = null;
        if (!TextUtils.isEmpty(localPasswordCache)) {
            wiFiLockPassword = new Gson().fromJson(localPasswordCache, WiFiLockPassword.class);
            duressList = mPresenter.setWifiLockPassword(wifiSn,wiFiLockPassword);
            mPhilipsDuressAlarmAdapter.setList(duressList);
        }else{
            mPresenter.getPasswordList(wifiSn);
        }



        if(mIvDuressSelect.isSelected()){
            recycler.setVisibility(View.GONE);
            mIvDuressSelect.setSelected(false);
        }else{
            recycler.setVisibility(View.VISIBLE);
            mIvDuressSelect.setSelected(true);
        }
    }

    private void initRecycleViewData() {
        mPhilipsDuressAlarmAdapter = new PhilipsDuressAlarmAdapter(R.layout.philips_item_duress_alarm);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(mPhilipsDuressAlarmAdapter);
        mPhilipsDuressAlarmAdapter.setOnClickDuressNotificationListener((v, position, data) -> {
            Intent intent = new Intent(this,PhilipsWifiVideoLockSettingDuressAlarmAvtivity.class);
            intent.putExtra(KeyConstants.WIFI_SN,data.getWifiSN());
            intent.putExtra("key_position",position);
            intent.putExtra("duress_alarm",data);
            startActivityForResult(intent,1012);
        });
    }

    private void initListener() {
        mIvDuressSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected()){
                    recycler.setVisibility(View.GONE);
                    mIvDuressSelect.setSelected(false);
                }else{
                    recycler.setVisibility(View.VISIBLE);
                    mIvDuressSelect.setSelected(true);
                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1012){
                int position = data.getIntExtra("key_position",-1);
                if(position < 0) return;
                duressList.get(position).setDuressToggle(data.getIntExtra("duress_alarm_toggle",0));
                duressList.get(position).setDuressPhone(data.getStringExtra("duress_alarm_phone"));
                mPhilipsDuressAlarmAdapter.setList(duressList);
                mPhilipsDuressAlarmAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initView() {
        mIvDuressSelect = findViewById(R.id.iv_duress_select);
        mBack = findViewById(R.id.back);
        recycler = findViewById(R.id.recycler);

    }

    @Override
    public void onGetPasswordSuccess(WiFiLockPassword wiFiLockPassword) {
        duressList = mPresenter.setWifiLockPassword(wifiSn,wiFiLockPassword);
        mPhilipsDuressAlarmAdapter.setList(duressList);
    }

    @Override
    public void onGetPasswordFailedServer(BaseResult baseResult) {

    }

    @Override
    public void onGetPasswordFailed(Throwable throwable) {

    }
}