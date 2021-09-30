package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.utils.GpsUtil;
import com.philips.easykey.lock.utils.WifiUtils;

/**
 * author : Jack
 * time   : 2021/4/30
 * E-mail : wengmaowei@kaadas.com
 * desc   : 提示配网第一步
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
        tvVoicePromptFailed.setOnClickListener(v -> {
            // TODO: 2021/5/6 跳转到语音提示失败的操作显示
            startActivity(new Intent(mAddVideoLockActivity,PhilipsModifyPassWordHelpActivity.class));
        });
    }

    public void initUIAndData() {
        isChoose = false;
        refreshNext();
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
            if(mAddVideoLockActivity != null) {
                mAddVideoLockActivity.showWifiNotConnectDialog();
            }
            return;
        }
        if (!GpsUtil.isOPen(context)) {
            GpsUtil.openGPS(context);
            if(mAddVideoLockActivity != null) {
                mAddVideoLockActivity.showOpenLocDialog();
            }
            return;
        }
        if(mAddVideoLockActivity != null) {
            if (isChoose){
                mAddVideoLockActivity.showFirstTask2();
            }
        }
    }

    private void refreshNext() {
        mBtnNext.setBackgroundResource(isChoose?R.drawable.philips_shape_btn_bg:R.drawable.philips_shape_btn_invalid_bg);
        mIvSelect.setImageResource(isChoose?R.drawable.philips_dms_icon_selected:R.drawable.philips_dms_icon_default);
        mBtnNext.setEnabled(isChoose);
    }

}
