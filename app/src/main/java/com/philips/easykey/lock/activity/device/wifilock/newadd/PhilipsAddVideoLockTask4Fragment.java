package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.widget.PhilipsWifiVideoCircleProgress;

/**
 * author : Jack
 * time   : 2021/5/6
 * E-mail : wengmaowei@kaadas.com
 * desc   : 虚假无用的倒计时
 */
public class PhilipsAddVideoLockTask4Fragment extends Fragment {

    private ImageView mIvSendingData;
    private TextView mTvSendingData;
    private ImageView mIvConnectingData;
    private TextView mTvConnectingData;
    private ImageView mIvBindingSuc;
    private TextView mTvBindingSuc;
    private TextView mTvCount;
    private PhilipsWifiVideoCircleProgress mCircleProgress;

    private PhilipsAddVideoLockActivity mAddVideoLockActivity;

    public static PhilipsAddVideoLockTask4Fragment getInstance() {
        return new PhilipsAddVideoLockTask4Fragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.philips_fragment_add_video_lock_task4, container, false);
        initTaskUI(root);
        if(getActivity() instanceof PhilipsAddVideoLockActivity) {
            mAddVideoLockActivity = (PhilipsAddVideoLockActivity) getActivity();
        }
        return root;
    }

    private void initTaskUI(View root) {

        mIvSendingData = root.findViewById(R.id.ivSendingData);
        mTvSendingData = root.findViewById(R.id.tvSendingData);
        mIvConnectingData = root.findViewById(R.id.ivConnectingData);
        mTvConnectingData = root.findViewById(R.id.tvConnectingData);
        mIvBindingSuc = root.findViewById(R.id.ivBindingSuc);
        mTvBindingSuc = root.findViewById(R.id.tvBindingSuc);
        mTvCount = root.findViewById(R.id.tvCount);
        mCircleProgress = root.findViewById(R.id.circleCountDown);

    }

    public void changeToSendingData() {
        mIvSendingData.setImageResource(R.drawable.philips_dms_icon_success);
        if(getContext() == null) return;
        mTvSendingData.setTextColor(getContext().getColor(R.color.color_333333));
    }

    public void changeToConnectingData() {
        mIvConnectingData.setImageResource(R.drawable.philips_dms_icon_success);
        if(getContext() == null) return;
        mTvConnectingData.setTextColor(getContext().getColor(R.
                color.color_333333));
    }

    public void changeToBindingSuc() {
        mIvBindingSuc.setImageResource(R.drawable.philips_dms_icon_success);
        if(getContext() == null) return;
        mTvBindingSuc.setTextColor(getContext().getColor(R.color.color_333333));
    }

    private final CountDownTimer mCountDownTimer = new CountDownTimer(100000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            int sec = (int) (millisUntilFinished / 1000);
            String value = String.valueOf(sec);
            if(sec == 60) {
                changeToSendingData();
            }
            if(sec == 30) {
                changeToConnectingData();
            }
            if(mTvCount != null) {
                mTvCount.setText(value);
            }
            if(mCircleProgress != null) {
                mCircleProgress.updateProcess(100-sec);
            }
            if(sec == 0) {
                // 超时失败
                if(mAddVideoLockActivity != null) {
                    mAddVideoLockActivity.showFirstTask4Fail();
                }
            }
        }

        @Override
        public void onFinish() {

        }
    };

    public void startCountDown() {
        mCountDownTimer.start();
    }

    public void cancelCountDown() {
        mCountDownTimer.cancel();
    }

}
