package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.GpsUtil;
import com.philips.easykey.lock.utils.WifiUtils;

/**
 * author :
 * time   : 2021/4/30
 * E-mail : wengmaowei@kaadas.com
 * desc   :
 */
public class PhilipsAddVideoLockTask1Fragment extends Fragment {

    private ImageView mIvSelect;
    private Button mBtnNext;

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;

    private boolean isChoose = false;

    public static PhilipsAddVideoLockTask1Fragment getInstance() {
        return new PhilipsAddVideoLockTask1Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_video_lock_task1, container, false);
        initTaskUI(root);
        if(getActivity() instanceof PhilipsAddVideoLockActivity) {
            mAddVideoLockActivity = (PhilipsAddVideoLockActivity) getActivity();
        }
        return root;
    }

    private void initTaskUI(View root) {
        mIvSelect = root.findViewById(R.id.ivSelect);
        mBtnNext = root.findViewById(R.id.btnNext);
        TextView tvVoicePromptFailed = root.findViewById(R.id.tvVoicePromptFailed);

        mBtnNext.setOnClickListener(v -> {
            // todo 检查权限，检查是否连接wifi
//            mPermissionDisposable = mRxPermissions
//                    .request(Manifest.permission.ACCESS_FINE_LOCATION)
//                    .subscribe(granted -> {
//                        toDisposable(mPermissionDisposable);
//                        if (granted) {
//                            // All requested permissions are granted
//                            processNextAction();
//                        } else {
//                            // At least one permission is denied
//                            ToastUtils.showShort(getString(R.string.philips_granted_local_please_open_wifi));
//                        }
//                    });
            processNextAction();
        });
        mIvSelect.setOnClickListener(v -> {
            isChoose = !isChoose;
            refreshNext();
        });
        tvVoicePromptFailed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 2021/5/6 跳转到语音提示失败的操作显示
            }
        });
    }

    private void processNextAction() {
        //打开wifi
        Context context = getContext();
        if(context == null) {
            context = MyApplication.getInstance();
        }
        WifiUtils wifiUtils = WifiUtils.getInstance(context);
        if (!wifiUtils.isWifiEnable()) {
            wifiUtils.openWifi();
            ToastUtils.showShort(getString(R.string.philips_wifi_no_open_please_open_wifi));
            return;
        }
        if (!WifiUtils.getInstance(context).isWifiEnable()) {
            showWifiDialog();
            WifiUtils.getInstance(context).openWifi();
            return;
        }
        if (!GpsUtil.isOPen(context)) {
            GpsUtil.openGPS(context);
            showLocationPermission();
            return;
        }
        if(mAddVideoLockActivity != null) {
            mAddVideoLockActivity.showFirstTask2();
        }
    }

    private void refreshNext() {
        mBtnNext.setBackgroundResource(isChoose?R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_invalid_bg);
        // TODO: 2021/5/6 缺少切图，暂时使用其他的临时替代
        mIvSelect.setImageResource(isChoose?R.drawable.philips_dms_icon_success:R.drawable.philips_dms_icon_default);
        mBtnNext.setEnabled(isChoose);
    }

    private void showWifiDialog() {
        AlertDialogUtil.getInstance().noEditTitleOneButtonDialog(
                getContext(),
                getString(R.string.philips_activity_clothes_hanger_machine_add_dialog_1),
                getString(R.string.philips_activity_clothes_hanger_machine_add_dialog_2), "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
//                        Intent wifiIntent =  new Intent(Settings.ACTION_WIFI_SETTINGS);
//                        startActivity(wifiIntent);
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void showLocationPermission() {
        AlertDialogUtil.getInstance().havaNoEditTwoButtonDialog(
                getContext(),
                getString(R.string.dialog_wifi_video_tip),
                getString(R.string.philips_activity_wifi_lock_new_add_first),
                getString(R.string.confirm), getString(R.string.cancel),"#1F96F7", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

                        startActivityForResult(intent,887);
                    }

                    @Override
                    public void right() {

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
