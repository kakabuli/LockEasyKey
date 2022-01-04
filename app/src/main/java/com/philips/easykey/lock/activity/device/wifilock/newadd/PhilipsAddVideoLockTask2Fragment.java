package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockAlbumActivity;
import com.philips.easykey.lock.activity.device.videolock.PhilipsWifiVideoLockAlbumDetailActivity;
import com.philips.easykey.lock.popup.PhilipsWifiListPopup;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.NetUtil;

import java.io.File;
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
    private ImageView mIvDrop,mIvPwd;
    private EditText mEtWifiName, mEtWifiPwd;
    private boolean passwordHide = true;

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
        if (getActivity() instanceof PhilipsAddVideoLockActivity) {
            mAddVideoLockActivity = (PhilipsAddVideoLockActivity) getActivity();
        }
        return root;
    }

    private void initTaskUI(View root) {
        mEtWifiName = root.findViewById(R.id.etWifiName);
        mEtWifiPwd = root.findViewById(R.id.etWifiPwd);
        Button btnNext = root.findViewById(R.id.btnNext);
        mIvDrop = root.findViewById(R.id.ivDrop);
        mIvPwd = root.findViewById(R.id.ivPwd);
        initWifiListPopup();
        // todo 初始化当前扫描的wifi列表数据
        initCurWifiName(mEtWifiName);

        mIvDrop.setOnClickListener(v -> {
            if (mWifiListPopup != null) {
                if (mWifiListPopup.isShowing()) {
                    mWifiListPopup.dismiss();
                } else {
                    showPopup();
                }
            }
        });

        mIvPwd.setOnClickListener(v -> {
            passwordHide = !passwordHide;
            if (passwordHide) {
                mEtWifiPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                mIvPwd.setImageResource(R.drawable.philips_dms_icon_hidden);
            } else {
                mEtWifiPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                mIvPwd.setImageResource(R.drawable.philips_dms_icon_display);
            }
            mEtWifiPwd.setSelection(mEtWifiPwd.getText().toString().length());//将光标移至文字末尾
        });
        btnNext.setEnabled(false);
        mEtWifiPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    String wifiName = mEtWifiName.getText().toString().trim();
                    if (!TextUtils.isEmpty(wifiName)) {
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
            String wifiPwd = mEtWifiPwd.getText().toString().trim();
            String ssid = NetUtil.getWifiName().replaceAll("\"", "");

            if (TextUtils.isEmpty(wifiName)) {
                ToastUtils.showShort(getString(R.string.philips_wifi_name_disable_empty));
                return;
            }
            if (TextUtils.isEmpty(wifiPwd)) {
                ToastUtils.showShort(getString(R.string.philips_password_len_not_less_8));
                return;
            }
            if (!ssid.equals(wifiName)){
                ///手机系统连接的wifi名和在wifi列表选择的wifiname不相等
                if(wifiName.contains("5G")){
                    ToastUtils.showShort(getString(R.string.philips_please_24g_network_for_door_lock_distribution));
                    return;
                }
                else {
                    if (mAddVideoLockActivity != null) {
                        mAddVideoLockActivity.setWifiInfo(wifiName, wifiPwd);
                        mAddVideoLockActivity.showFirstTask3();
                    }
                }
            }else {
                if(NetUtil.isWifi5G(getContext())){
                    ///当前手机系统连接的wifi为5G频段
                    showDual_Frequency_In_OneDialog(wifiName, wifiPwd);

                }
                else {
                    if (mAddVideoLockActivity != null) {
                        mAddVideoLockActivity.setWifiInfo(wifiName, wifiPwd);
                        mAddVideoLockActivity.showFirstTask3();
                    }
                }
            }

        });
    }

    public void initUIAndData() {
        if(mEtWifiName != null) {
            mEtWifiName.setText("");
            initCurWifiName(mEtWifiName);
        }
        if(mEtWifiPwd != null) {
            mEtWifiPwd.setText("");
        }
        initWifiList();
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
            if (adapter.getItem(position) instanceof String) {
                String wifiName = (String) adapter.getItem(position);
                mEtWifiName.setText(wifiName);
                if (mWifiListPopup != null) {
                    mWifiListPopup.dismiss();
                }
            }
        });
        initWifiList();
    }

    private void initWifiList() {
        if(getContext() == null) {
            return;
        }
        ArrayList<String> wifiList = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        NetworkUtils.WifiScanResults wifiScanResults = NetworkUtils.getWifiScanResult();
        if(wifiScanResults.getFilterResults().isEmpty()) {
            LogUtils.e("wifiScanResults.getAllResults().isEmpty()");
            return;
        }
        for (ScanResult scanResult : wifiScanResults.getFilterResults()) {
            wifiList.add(scanResult.SSID);
        }
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

    private void showDual_Frequency_In_OneDialog(String wifiName,String wifiPwd) {

        AlertDialogUtil.getInstance().noEditTitleTwoButtonPhilipsDialog(this.getActivity()
                , getString(R.string.dual_frequency_in_one) + "","#000000",
                getString(R.string.connection), getString(R.string.philips_cancel), "#0066A1", "#FFFFFF", new AlertDialogUtil.ClickListener()  {

            @Override
            public void left() {
                if (mAddVideoLockActivity != null) {
                    mAddVideoLockActivity.setWifiInfo(wifiName, wifiPwd);
                    mAddVideoLockActivity.showFirstTask3();
                }
            }

            @Override
            public void right() {
                return;
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
