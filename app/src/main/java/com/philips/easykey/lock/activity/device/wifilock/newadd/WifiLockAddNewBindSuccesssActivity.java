package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.adapter.AddBluetoothPairSuccessAdapter;
import com.philips.easykey.lock.bean.deviceAdd.AddBluetoothPairSuccessBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockAddSuccessPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockAddSuccessView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.EditTextWatcher;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.StringUtil;
import com.philips.easykey.lock.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//activity_wifi_lock_add_new_bind_successs
public class WifiLockAddNewBindSuccesssActivity extends BaseActivity<IWifiLockAddSuccessView
        , WifiLockAddSuccessPresenter<IWifiLockAddSuccessView>> implements IWifiLockAddSuccessView {

    @BindView(R.id.input_name)
    EditText inputName;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.lock)
    ImageView lock;


    private List<AddBluetoothPairSuccessBean> mList;

    private AddBluetoothPairSuccessAdapter mAdapter;
    private String wifiSN;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_add_success);
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
        initMonitor();

    }

    private void initMonitor() {
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setSelected(false);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initListener() {
        inputName.addTextChangedListener(new EditTextWatcher(this, null, inputName, 50));
    }

    @Override
    protected WifiLockAddSuccessPresenter<IWifiLockAddSuccessView> createPresent() {
        return new WifiLockAddSuccessPresenter<>();
    }

    private void initData() {
        mList = new ArrayList<>();
        mList.add(new AddBluetoothPairSuccessBean(getString(R.string.wifi_lock_my_home), false));
        mList.add(new AddBluetoothPairSuccessBean(getString(R.string.wifi_lock_bedroom), false));
        mList.add(new AddBluetoothPairSuccessBean(getString(R.string.wifi_lock_company), false));
        wifiSN = getIntent().getStringExtra(KeyConstants.WIFI_SN);
    }

    private void initView() {
        recycler.setLayoutManager(new GridLayoutManager(this, 6));
        if (mList != null) {
            mAdapter = new AddBluetoothPairSuccessAdapter(mList);
            recycler.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    inputName.setCursorVisible(true);
                    for (int i = 0; i < mList.size(); i++) {
                        mList.get(i).setSelected(false);
                    }
                    AddBluetoothPairSuccessBean addBluetoothPairSuccessBean = mList.get(position);
                    String name = addBluetoothPairSuccessBean.getName();
                    inputName.setText(name);
                    if (name != null) {
                        inputName.setSelection(name.length());
                    }
                    inputName.setFocusable(true);
                    inputName.setFocusableInTouchMode(true);
                    inputName.requestFocus();
                    mList.get(position).setSelected(true);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
        String name = inputName.getText().toString().trim();
        if (name != null) {
            inputName.setCursorVisible(false);
        }
    }

    @OnClick({R.id.save, R.id.back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                Intent backIntent = new Intent(this, MainActivity.class);
                startActivity(backIntent);
                break;
            case R.id.save:
                String name = inputName.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.getInstance().showShort(R.string.not_empty);
                    return;
                }
                if (!StringUtil.nicknameJudge(name)) {
                    ToastUtil.getInstance().showShort(R.string.nickname_verify_error);
                    return;
                }

                showLoading(getString(R.string.is_saving_name));
                mPresenter.setNickName(wifiSN, name);
                break;
        }
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return isCosumenBackKey();
        }
        return false;
    }

    private boolean isCosumenBackKey() {
        Intent backIntent = new Intent(this, MainActivity.class);
        startActivity(backIntent);
        return true;
    }

    @OnClick()
    public void onViewClicked() {
    }

    @Override
    public void onSetNameSuccess() {
        hiddenLoading();
        MyApplication.getInstance().getAllDevicesByMqtt(true);
        Intent backIntent=new Intent(this, MainActivity.class);
        startActivity(backIntent);
    }

    @Override
    public void onSetNameFailedNet(Throwable throwable) {
        Toast.makeText(this, R.string.set_failed, Toast.LENGTH_SHORT).show();
        hiddenLoading();
    }

    @Override
    public void onSetNameFailedServer(BaseResult baseResult) {
        Toast.makeText(this, R.string.set_failed, Toast.LENGTH_SHORT).show();
        hiddenLoading();
    }
}
