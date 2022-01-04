package com.philips.easykey.lock.activity.device.tmall;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.family.PhilipsWifiLockFamilyManagerActivity;
import com.philips.easykey.lock.adapter.AddBluetoothPairSuccessAdapter;
import com.philips.easykey.lock.bean.deviceAdd.AddBluetoothPairSuccessBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.tmall.PhilipsAddTmallDevicePresenter;
import com.philips.easykey.lock.mvp.view.tmall.IPhilipsAddTmallDeviceView;
import com.philips.easykey.lock.publiclibrary.bean.TmallDeviceListBean;
import com.philips.easykey.lock.utils.KeyConstants;

import java.util.ArrayList;
import java.util.List;


public class PhilipsAddTamllDeviceActivity extends BaseActivity<IPhilipsAddTmallDeviceView, PhilipsAddTmallDevicePresenter<IPhilipsAddTmallDeviceView>> implements IPhilipsAddTmallDeviceView{

    private List<AddBluetoothPairSuccessBean> mList;
    private AddBluetoothPairSuccessAdapter mAdapter;
    private Button btnUse;
    private ImageView ivState;
    private RecyclerView rvNames;
    private ImageView ivBack;
    private EditText etName;
    private TmallDeviceListBean tmallDeviceListBean;

    @Override
    protected PhilipsAddTmallDevicePresenter<IPhilipsAddTmallDeviceView> createPresent() {
        return new PhilipsAddTmallDevicePresenter<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_tmall_add_device);
        initTaskUI();
    }

    private void initTaskUI() {
        tmallDeviceListBean = (TmallDeviceListBean) getIntent().getSerializableExtra(KeyConstants.TMALL_DEVICE);
        ivState = findViewById(R.id.ivState);
        etName = findViewById(R.id.etName);
        rvNames = findViewById(R.id.rvNames);
        btnUse = findViewById(R.id.btnUse);
        ivBack = findViewById(R.id.back);
        rvNames.setLayoutManager(new GridLayoutManager(this, 6));
        initData(rvNames, etName);
        btnUse.setOnClickListener(v -> {
            // TODO: 2021/5/10 立即体验
            String name = etName.getText().toString();
            if(TextUtils.isEmpty(name)) {
                ToastUtils.showShort(getString(R.string.nickName_not_empty));
                return;
            }
            if (name.length() > 10 ) {
                ToastUtils.showShort(getString(R.string.philips_nickname_verify_error));
                return;
            }
            mPresenter.aligenieUserDeviceShare(MyApplication.getInstance().getUid(),name,tmallDeviceListBean);
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(101);
                finish();
            }
        });
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String account = etName.getText().toString().trim();
                if(TextUtils.isEmpty(account)) {
                    changeConfirmBtnStyle(false);
                } else {
                    changeConfirmBtnStyle(!TextUtils.isEmpty(s.toString()));
                }
            }
        });
    }

    private void initData(@NonNull RecyclerView recycler, @NonNull EditText etName) {
        mList = new ArrayList<>();
        mList.add(new AddBluetoothPairSuccessBean(getString(R.string.philips_wifi_lock_my_home), false));
        mList.add(new AddBluetoothPairSuccessBean(getString(R.string.philips_wifi_lock_bedroom), false));
        mList.add(new AddBluetoothPairSuccessBean(getString(R.string.philips_wifi_lock_company), false));
        if (mList != null) {
            mAdapter = new AddBluetoothPairSuccessAdapter(mList);
            recycler.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener((adapter, view, position) -> {
                changeConfirmBtnStyle(true);
                etName.setCursorVisible(true);
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setSelected(false);
                }
                AddBluetoothPairSuccessBean addBluetoothPairSuccessBean = mList.get(position);
                String name = addBluetoothPairSuccessBean.getName();
                etName.setText(name);
                if (name != null) {
                    etName.setSelection(name.length());
                }
                etName.setFocusable(true);
                etName.setFocusableInTouchMode(true);
                etName.requestFocus();
                mList.get(position).setSelected(true);
                mAdapter.notifyDataSetChanged();
            });
        }
    }

    private void changeConfirmBtnStyle(boolean isCanLogin) {
        btnUse.setClickable(isCanLogin);
        btnUse.setSelected(isCanLogin);
    }

    @Override
    public void onAligenieUserDeviceShareSuccess() {
        setResult(RESULT_OK);
        finish();
        Intent intent = new Intent(PhilipsAddTamllDeviceActivity.this, PhilipsWifiLockFamilyManagerActivity.class);
        intent.putExtra(KeyConstants.WIFI_SN, tmallDeviceListBean.getWifiSN());
        startActivity(intent);
    }
}
