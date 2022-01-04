package com.philips.easykey.lock.activity.device.tmall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.PhilipsWifiLockWifiDetailActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.PhilipsAddVideoLockActivity;
import com.philips.easykey.lock.adapter.PhilipsSevenDayDataStatisticsAdapter;
import com.philips.easykey.lock.adapter.PhilipsTmallDeviceSelectAdapter;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.tmall.PhilipsTmallSelectDevicePresenter;
import com.philips.easykey.lock.mvp.view.tmall.IPhilipsTmallSelectDeviceView;
import com.philips.easykey.lock.publiclibrary.bean.TmallDeviceListBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.TmallDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.TmallDeviceListResult;
import com.philips.easykey.lock.utils.KeyConstants;
import java.util.ArrayList;
import java.util.List;

public class PhilipsTmallSelectDeviceActivity extends BaseActivity<IPhilipsTmallSelectDeviceView, PhilipsTmallSelectDevicePresenter<IPhilipsTmallSelectDeviceView>>
        implements IPhilipsTmallSelectDeviceView , PhilipsTmallDeviceSelectAdapter.OnItemClickListener{

    private RecyclerView rlDevice;
    private Button confirm,btRetry;
    private RelativeLayout rlLoad;
    private RelativeLayout rlRetry;
    private TextView tvNoDevice;
    private TextView tvFailed;
    private ImageView ivBack;
    private PhilipsTmallDeviceSelectAdapter tmallDeviceSelectAdapter;
    private TmallDeviceListBean tmallDeviceListBean = null;
    List<TmallDeviceListBean> list = new ArrayList<>();
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_tmall_select_device);
        rlDevice = findViewById(R.id.rlDevice);
        confirm = findViewById(R.id.confirm);
        rlLoad = findViewById(R.id.rlLoad);
        rlRetry = findViewById(R.id.rlRetry);
        btRetry = findViewById(R.id.btRetry);
        tvNoDevice = findViewById(R.id.tvNoDevice);
        tvFailed = findViewById(R.id.tv_failed);
        ivBack = findViewById(R.id.back);

        code = getIntent().getStringExtra("code");
        if(!TextUtils.isEmpty(code) && !TextUtils.isEmpty(MyApplication.getInstance().getUid())){
            aligenieUserlogin();
        }else {
            rlDevice.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            rlRetry.setVisibility(View.VISIBLE);
            rlLoad.setVisibility(View.GONE);
            tvNoDevice.setVisibility(View.GONE);
        }
        if(tmallDeviceSelectAdapter == null){
            tmallDeviceSelectAdapter = new PhilipsTmallDeviceSelectAdapter();
        }
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this);
        verticalLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rlDevice.setLayoutManager(verticalLayoutManager);
        rlDevice.setAdapter(tmallDeviceSelectAdapter);
        tmallDeviceSelectAdapter.setOnItemClickListener(this);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tmallDeviceListBean != null){
                    Intent wifiIntent = new Intent(PhilipsTmallSelectDeviceActivity.this, PhilipsAddTamllDeviceActivity.class);
                    wifiIntent.putExtra(KeyConstants.TMALL_DEVICE,tmallDeviceListBean);
                    startActivityForResult(wifiIntent,100);
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aligenieUserlogin();
            }
        });
    }

    @Override
    protected PhilipsTmallSelectDevicePresenter<IPhilipsTmallSelectDeviceView> createPresent() {
        return new PhilipsTmallSelectDevicePresenter<>();
    }

    private void aligenieUserlogin(){
        rlDevice.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
        rlRetry.setVisibility(View.GONE);
        tvNoDevice.setVisibility(View.GONE);
        rlLoad.setVisibility(View.VISIBLE);
        mPresenter.aligenieUserlogin(MyApplication.getInstance().getUid(), new Gson().fromJson(code, TmallDeviceBean.Code.class));
    }


    @Override
    public void aligenieUserloginFailed(BaseResult baseResult) {
        if(baseResult == null){
            rlDevice.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            rlRetry.setVisibility(View.VISIBLE);
            rlLoad.setVisibility(View.GONE);
            tvNoDevice.setVisibility(View.GONE);
        }else {
            if(baseResult.getCode().equals("810")){
                rlDevice.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
                rlRetry.setVisibility(View.VISIBLE);
                rlLoad.setVisibility(View.GONE);
                tvNoDevice.setVisibility(View.GONE);
                btRetry.setVisibility(View.GONE);
                tvFailed.setText(getString(R.string.philips_tmall_unbinding_failed_thetmall_elf_has_bound_the_door_lock));
            }else {
                rlDevice.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
                rlRetry.setVisibility(View.VISIBLE);
                rlLoad.setVisibility(View.GONE);
                tvNoDevice.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void aligenieUserloginSuccess(TmallDeviceListResult tmallDeviceListResult) {
        if(tmallDeviceListResult.getData().size() > 0){
            list.clear();
            for(int i = 0; i < tmallDeviceListResult.getData().size() ; i++ ){
                TmallDeviceListBean tmallDeviceListBean = new TmallDeviceListBean();
                tmallDeviceListBean.setSelece(false);
                tmallDeviceListBean.set_id(tmallDeviceListResult.getData().get(i).get_id());
                tmallDeviceListBean.setDeviceOpenId(tmallDeviceListResult.getData().get(i).getDeviceOpenId());
                tmallDeviceListBean.setIsBind(tmallDeviceListResult.getData().get(i).getIsBind());
                tmallDeviceListBean.setLockNickname(tmallDeviceListResult.getData().get(i).getLockNickname());
                tmallDeviceListBean.setUserOpenId(tmallDeviceListResult.getData().get(i).getUserOpenId());
                tmallDeviceListBean.setProductModel(tmallDeviceListResult.getData().get(i).getProductModel());
                tmallDeviceListBean.setWifiSN(tmallDeviceListResult.getData().get(i).getWifiSN());
                tmallDeviceListBean.setAligenieDeviceModel(tmallDeviceListResult.getData().get(i).getAligenieDeviceModel());
                tmallDeviceListBean.setAligenieMac(tmallDeviceListResult.getData().get(i).getAligenieMac());
                tmallDeviceListBean.setAligenieSN(tmallDeviceListResult.getData().get(i).getAligenieSN());
                list.add(tmallDeviceListBean);
            }
            rlDevice.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            rlRetry.setVisibility(View.GONE);
            rlLoad.setVisibility(View.GONE);
            tvNoDevice.setVisibility(View.GONE);
            tmallDeviceSelectAdapter.setList(list);
            tmallDeviceSelectAdapter.notifyDataSetChanged();
        }else {
            rlDevice.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            rlRetry.setVisibility(View.GONE);
            rlLoad.setVisibility(View.GONE);
            tvNoDevice.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onItemClick(int position) {
        for(int i = 0 ; i < list.size() ; i++ ){
            if(i == position){
                list.get(i).setSelece(!list.get(i).isSelece());
                tmallDeviceListBean = list.get(i);
                changeConfirmBtnStyle(list.get(i).isSelece());
            }else {
                list.get(i).setSelece(false);
            }
        }
        tmallDeviceSelectAdapter.setList(list);
        tmallDeviceSelectAdapter.notifyDataSetChanged();
    }

    private void changeConfirmBtnStyle(boolean isCanLogin) {
        confirm.setClickable(isCanLogin);
        confirm.setEnabled(isCanLogin);
        confirm.setSelected(isCanLogin);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (100 == requestCode) {
                finish();
            }
        }
    }
}
