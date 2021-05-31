package com.philips.easykey.lock.activity.addDevice.bluetooth;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.adapter.AddBluetoothPairSuccessAdapter;
import com.philips.easykey.lock.bean.deviceAdd.AddBluetoothPairSuccessBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.deviceaddpresenter.BindBleSuccessPresenter;
import com.philips.easykey.lock.mvp.view.deviceaddview.IBindBleSuccessView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.EditTextWatcher;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


public class AddBluetoothSuccessActivity extends BaseActivity<IBindBleSuccessView, BindBleSuccessPresenter<IBindBleSuccessView>> implements IBindBleSuccessView {

    EditText inputName;
    RecyclerView recycler;
    Button save;
    ImageView lock;
    ImageView back;

    private List<AddBluetoothPairSuccessBean> mList;

    private AddBluetoothPairSuccessAdapter mAdapter;
    private String deviceName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_add_success);
        inputName = findViewById(R.id.input_name);
        recycler = findViewById(R.id.recycler);
        save = findViewById(R.id.save);
        lock = findViewById(R.id.lock);
        back = findViewById(R.id.back);
        deviceName = getIntent().getStringExtra(KeyConstants.DEVICE_NAME);
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
        back.setOnClickListener(v -> {
            Intent backIntent = new Intent(this, MainActivity.class);
            startActivity(backIntent);
        });
        save.setOnClickListener(v -> {
            String name = inputName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastUtils.showShort(R.string.not_empty);
                return;
            }
            if (!StringUtil.nicknameJudge(name)) {
                ToastUtils.showShort(R.string.nickname_verify_error);
                return;
            }

            showLoading(getString(R.string.is_saving_name));
            mPresenter.modifyDeviceNickname(deviceName, MyApplication.getInstance().getUid(), inputName.getText().toString().trim());
        });
    }

    @Override
    protected BindBleSuccessPresenter<IBindBleSuccessView> createPresent() {
        return new BindBleSuccessPresenter<>();
    }

    private void initData() {
        AddBluetoothPairSuccessBean father = new AddBluetoothPairSuccessBean(getString(R.string.father), false);
        AddBluetoothPairSuccessBean mother = new AddBluetoothPairSuccessBean(getString(R.string.mother), false);
        AddBluetoothPairSuccessBean brother = new AddBluetoothPairSuccessBean(getString(R.string.brother), false);
        AddBluetoothPairSuccessBean little_brother = new AddBluetoothPairSuccessBean(getString(R.string.little_brother), false);
        AddBluetoothPairSuccessBean sister = new AddBluetoothPairSuccessBean(getString(R.string.sister), false);
        AddBluetoothPairSuccessBean other = new AddBluetoothPairSuccessBean(getString(R.string.philips_other), false);
        mList = new ArrayList<>();
        mList.add(father);
        mList.add(mother);
        mList.add(brother);
        mList.add(little_brother);
        mList.add(sister);
        mList.add(other);
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

    @Override
    public void modifyDeviceNicknameSuccess() {
        hiddenLoading();
        ToastUtils.showShort(R.string.save_success);
        //设置成功  跳转到设备列别界面
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void modifyDeviceNicknameError(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void modifyDeviceNicknameFail(BaseResult baseResult) {
        hiddenLoading();
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, baseResult.getCode()));
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

}
