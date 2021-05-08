package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.popup.PhilipsWifiListPopup;

import java.util.ArrayList;

import razerdp.basepopup.BasePopupWindow;

/**
 * author :
 * time   : 2021/4/30
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsAddVideoLockTask2Fragment extends Fragment {

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;
    private PhilipsWifiListPopup mWifiListPopup;
    private ImageView mIvDrop;
    private EditText mEtWifiName;

    public static PhilipsAddVideoLockTask2Fragment getInstance() {
        return new PhilipsAddVideoLockTask2Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_video_lock_task2, container, false);
        initTaskUI(root);
        if(getActivity() instanceof PhilipsAddVideoLockActivity) {
            mAddVideoLockActivity = (PhilipsAddVideoLockActivity) getActivity();
        }
        return root;
    }

    private void initTaskUI(View root) {
        mEtWifiName = root.findViewById(R.id.etWifiName);
        EditText etWifiPwd = root.findViewById(R.id.etWifiPwd);
        Button btnNext = root.findViewById(R.id.btnNext);
        mIvDrop = root.findViewById(R.id.ivDrop);
        initWifiListPopup();
        // todo 初始化当前扫描的wifi列表数据
        initCurWifiName(mEtWifiName);

        mIvDrop.setOnClickListener(v -> {
            if(mWifiListPopup != null) {
                if(mWifiListPopup.isShowing()) {
                    mWifiListPopup.dismiss();
                } else {
                    showPopup();
                }
            }
        });
        btnNext.setEnabled(false);
        etWifiPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0) {
                    String wifiName = mEtWifiName.getText().toString().trim();
                    if(!TextUtils.isEmpty(wifiName)) {
                        btnNext.setEnabled(true);
                        btnNext.setBackgroundResource(R.drawable.philips_shape_btn_bg);
                    } else {
                        btnNext.setEnabled(false);
                        btnNext.setBackgroundResource(R.drawable.philips_shape_btn_invalid_bg);
                    }
                } else {
                    btnNext.setEnabled(false);
                    btnNext.setBackgroundResource(R.drawable.philips_shape_btn_invalid_bg);
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            String wifiName = mEtWifiName.getText().toString().trim();
            String wifiPwd = etWifiPwd.getText().toString().trim();

            if(TextUtils.isEmpty(wifiName)) {
                ToastUtils.showShort(R.string.philips_wifi_name_disable_empty);
                return;
            }
            if(TextUtils.isEmpty(wifiPwd)) {
                ToastUtils.showShort(R.string.philips_password_len_not_less_8);
                return;
            }
            if(mAddVideoLockActivity != null) {
                mAddVideoLockActivity.setWifiInfo(wifiName, wifiPwd);
                mAddVideoLockActivity.showFirstTask3();
            }
        });
    }

    private void initWifiListPopup() {
        mWifiListPopup = new PhilipsWifiListPopup(getContext());
        mWifiListPopup.setOverlayNavigationBar(false);
        mWifiListPopup.setOverlayStatusbar(false);
        mWifiListPopup.setBackgroundColor(Color.TRANSPARENT);
        mWifiListPopup.setOnDismissListener(new BasePopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                refreshDropToDown();
            }
        });
        mWifiListPopup.setOnItemClickListener((adapter, view, position) -> {
            if(adapter.getItem(position) instanceof String) {
                String wifiName = (String) adapter.getItem(position);
                mEtWifiName.setText(wifiName);
                if(mWifiListPopup != null) {
                    mWifiListPopup.dismiss();
                }
            }
        });
        initWifiList();
    }

    private void initWifiList() {
        ArrayList<String> wifiList = new ArrayList<>();
        wifiList.add("Test1");
        wifiList.add("Test2");
        wifiList.add("Test3");
        if(mWifiListPopup != null) {
            mWifiListPopup.updateWifiList(wifiList);
        }
    }

    private void initCurWifiName(EditText etWifiName) {
        WifiManager wifiManager = (WifiManager) MyApplication.getInstance()
                .getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo  = wifiManager.getConnectionInfo();
        String curConnectedWifiName = wifiInfo.getSSID();
        if (curConnectedWifiName.startsWith("\"") && curConnectedWifiName.endsWith("\"")) {
            curConnectedWifiName = curConnectedWifiName.substring(1, curConnectedWifiName.length() - 1);
        }
        etWifiName.setText(TextUtils.isEmpty(curConnectedWifiName)?"":curConnectedWifiName);
    }
    
    private void showPopup() {
        if(mWifiListPopup != null && mEtWifiName != null && mIvDrop != null) {
            mWifiListPopup.setPopupGravity(Gravity.BOTTOM);
            mWifiListPopup.showPopupWindow(mEtWifiName);
            mIvDrop.setImageResource(R.drawable.philips_dms_icon_cbb_up);
        }
    }

    private void refreshDropToDown() {
        if(mIvDrop != null) {
            mIvDrop.setImageResource(R.drawable.philips_dms_icon_cbb_down);
        }
    }

}
