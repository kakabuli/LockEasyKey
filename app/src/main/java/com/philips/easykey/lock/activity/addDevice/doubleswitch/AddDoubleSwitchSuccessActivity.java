package com.philips.easykey.lock.activity.addDevice.doubleswitch;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.adapter.AddBluetoothPairSuccessAdapter;
import com.philips.easykey.lock.bean.deviceAdd.AddBluetoothPairSuccessBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.EditTextWatcher;

import java.util.ArrayList;
import java.util.List;


public class AddDoubleSwitchSuccessActivity   extends BaseAddToApplicationActivity {

    ImageView back;
    TextView headTitle;
    ImageView lock;
    EditText inputName;
    RecyclerView rvName;
    EditText etPosition;
    RecyclerView rvPosition;
    Button save;
    private List<AddBluetoothPairSuccessBean> nameList;
    private List<AddBluetoothPairSuccessBean> positionList;
    private AddBluetoothPairSuccessAdapter nameAdapter;
    private AddBluetoothPairSuccessAdapter positionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_single_switch_success);
        back = findViewById(R.id.back);
        headTitle = findViewById(R.id.head_title);
        lock = findViewById(R.id.lock);
        inputName = findViewById(R.id.input_name);
        rvName = findViewById(R.id.rv_name);
        etPosition = findViewById(R.id.et_position);
        rvPosition = findViewById(R.id.rv_position);
        save = findViewById(R.id.save);

        initData();
        initListener();
        initView();
        initMonitor();
    }


    private void initMonitor() {
        inputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < nameList.size(); i++) {
                    nameList.get(i).setSelected(false);
                }
                nameAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etPosition.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < positionList.size(); i++) {
                    positionList.get(i).setSelected(false);
                }
                positionAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initListener() {
        inputName.addTextChangedListener(new EditTextWatcher(this, null, inputName, 50));
        etPosition.addTextChangedListener(new EditTextWatcher(this, null, inputName, 50));
    }


    private void initData() {
        nameList = new ArrayList<>();
        nameList.add(new AddBluetoothPairSuccessBean(getString(R.string.philips_wifi_lock_my_home), false));
        nameList.add(new AddBluetoothPairSuccessBean(getString(R.string.philips_wifi_lock_bedroom), false));
        nameList.add(new AddBluetoothPairSuccessBean(getString(R.string.philips_wifi_lock_company), false));


        positionList = new ArrayList<>();
        positionList.add(new AddBluetoothPairSuccessBean(getString(R.string.single_switch_position1), false));
        positionList.add(new AddBluetoothPairSuccessBean(getString(R.string.single_switch_position2), false));
        positionList.add(new AddBluetoothPairSuccessBean(getString(R.string.single_switch_position3), false));
        positionList.add(new AddBluetoothPairSuccessBean(getString(R.string.single_switch_position4), false));

    }

    private void initView() {
        rvName.setLayoutManager(new GridLayoutManager(this, 6));
        if (nameList != null) {
            nameAdapter = new AddBluetoothPairSuccessAdapter(nameList);
            rvName.setAdapter(nameAdapter);
            nameAdapter.setOnItemClickListener((adapter, view, position) -> {
                inputName.setCursorVisible(true);
                for (int i = 0; i < nameList.size(); i++) {
                    nameList.get(i).setSelected(false);
                }
                AddBluetoothPairSuccessBean addBluetoothPairSuccessBean = nameList.get(position);
                String name = addBluetoothPairSuccessBean.getName();
                inputName.setText(name);
                if (name != null) {
                    inputName.setSelection(name.length());
                }
                inputName.setFocusable(true);
                inputName.setFocusableInTouchMode(true);
                inputName.requestFocus();
                nameList.get(position).setSelected(true);
                nameAdapter.notifyDataSetChanged();
            });
        }
        String name = inputName.getText().toString().trim();
        if (name != null) {
            inputName.setCursorVisible(false);
        }

        rvPosition.setLayoutManager(new GridLayoutManager(this, 6));
        if (positionList != null) {
            positionAdapter = new AddBluetoothPairSuccessAdapter(positionList);
            rvPosition.setAdapter(positionAdapter);
            positionAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                    etPosition.setCursorVisible(true);
                    for (int i = 0; i < positionList.size(); i++) {
                        positionList.get(i).setSelected(false);
                    }
                    AddBluetoothPairSuccessBean addBluetoothPairSuccessBean = positionList.get(position);
                    String name = addBluetoothPairSuccessBean.getName();
                    etPosition.setText(name);
                    if (name != null) {
                        etPosition.setSelection(name.length());
                    }
                    etPosition.setFocusable(true);
                    etPosition.setFocusableInTouchMode(true);
                    etPosition.requestFocus();
                    positionList.get(position).setSelected(true);
                    positionAdapter.notifyDataSetChanged();
                }
            });
        }
    }

}
