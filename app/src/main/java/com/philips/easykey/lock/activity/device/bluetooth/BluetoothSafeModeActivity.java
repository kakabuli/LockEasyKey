package com.philips.easykey.lock.activity.device.bluetooth;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SizeUtils;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.BleDetailActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleActivity;
import com.philips.easykey.lock.mvp.presenter.ble.SafeModePresenter;
import com.philips.easykey.lock.mvp.view.ISafeModeView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.LogUtils;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 2019/4/15
 */
public class BluetoothSafeModeActivity extends BaseBleActivity<ISafeModeView, SafeModePresenter<ISafeModeView>>
        implements ISafeModeView, View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_safe_mode)
    ImageView ivSafeMode;
    @BindView(R.id.rl_safe_mode)
    RelativeLayout rlSafeMode;
    boolean safeModeStatus;
    @BindView(R.id.no_card)
    LinearLayout noCard;
    @BindView(R.id.all)
    LinearLayout all;
    @BindView(R.id.rl_notice)
    RelativeLayout rlNotice;
    @BindView(R.id.notice1)
    RelativeLayout notice1;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.tv_2)
    TextView tv2;
    private BleLockInfo bleLockInfo;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_safe_mode);
        ButterKnife.bind(this);
        bleLockInfo = mPresenter.getBleLockInfo();
        initData();
        if (mPresenter.isAuth(bleLockInfo, false)) {
            mPresenter.getDeviceInfo();
        } else {
            ToastUtils.showLong(getString(R.string.please_connect_lock));
        }
        ivBack.setOnClickListener(this);
        tvContent.setText(R.string.safe_mode);
        rlSafeMode.setOnClickListener(this);


        if (bleLockInfo != null && bleLockInfo.getServerLockInfo() != null) {
            String functionSet = bleLockInfo.getServerLockInfo().getFunctionSet();
//            functionSet = ""+0x41;
//            functionSet = ""+0x36;
            boolean supportCard = BleLockUtils.isSupportCard(functionSet);
            boolean supportFinger = BleLockUtils.isSupportFinger(functionSet);
            boolean supportPassword = BleLockUtils.isSupportPassword(functionSet);
            if (supportCard && supportFinger && supportPassword) {
                all.setVisibility(View.VISIBLE);
                noCard.setVisibility(View.GONE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(rlNotice.getLayoutParams());
                lp.setMargins(0, 0, 0, SizeUtils.dp2px(60));
                rlNotice.setLayoutParams(lp);
            } else {
                all.setVisibility(View.GONE);
                noCard.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(rlNotice.getLayoutParams());
                lp.setMargins(0, 0, 0, SizeUtils.dp2px(100));
                rlNotice.setLayoutParams(lp);
                if (supportFinger && supportCard) {
                    iv1.setImageResource(R.mipmap.safe_finger);
                    iv1.setImageResource(R.mipmap.safe_card);
                    tv1.setText(R.string.finger);
                    tv2.setText(R.string.card);
                } else if (supportPassword && supportFinger) {
                    iv1.setImageResource(R.mipmap.safe_password);
                    iv1.setImageResource(R.mipmap.safe_finger);
                    tv1.setText(R.string.password);
                    tv2.setText(R.string.finger);
                } else if (supportPassword && supportCard) {
                    iv1.setImageResource(R.mipmap.safe_password);
                    iv1.setImageResource(R.mipmap.safe_card);
                    tv1.setText(R.string.password);
                    tv2.setText(R.string.card);
                }
            }
        }

    }

    private void initData() {
        if (bleLockInfo != null) {
            name = bleLockInfo.getServerLockInfo().getModel();
        }

    }

    @Override
    protected SafeModePresenter<ISafeModeView> createPresent() {
        return new SafeModePresenter<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_safe_mode:
                /**
                 * 开关监听事件
                 */
                if (mPresenter.isAuth(bleLockInfo, false)) {
                    //打开时
                    if (safeModeStatus) {
                        mPresenter.openSafeMode(false);
                    } else {
                        mPresenter.openSafeMode(true);
                    }
                }
                showLoading(getString(R.string.is_setting));

                break;
        }
    }

    @Override
    public void onSetSuccess(boolean isOpen) {
        LogUtils.d("设置安全模式成功   " + isOpen);
        if (isOpen) {
            ivSafeMode.setImageResource(R.mipmap.iv_open);
            safeModeStatus = true;
        } else {
            ivSafeMode.setImageResource(R.mipmap.iv_close);
            safeModeStatus = false;
        }
        hiddenLoading();
    }

    @Override
    public void onSetFailed(Throwable throwable) {
        ToastUtils.showLong(getString(R.string.set_failed));
        hiddenLoading();
    }

    @Override
    public void onGetStateSuccess(boolean isOpen) {
        if (isOpen) {
            safeModeStatus = true;
            ivSafeMode.setImageResource(R.mipmap.iv_open);
        } else {
            safeModeStatus = false;
            ivSafeMode.setImageResource(R.mipmap.iv_close);
        }
    }

    @Override
    public void onGetStateFailed(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showShort(getString(R.string.get_lock_state_fail));
        LogUtils.d("获取门锁状态失败   " + throwable.getMessage());
    }

    @Override
    public void onPasswordTypeLess() {
        hiddenLoading();
        AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.hint), getString(R.string.safe_mode_dialog), getString(R.string.philips_cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
            @Override
            public void left() {

            }

            @Override
            public void right() {
                String lockType = bleLockInfo.getServerLockInfo().getModel();
                if (!TextUtils.isEmpty(lockType)) {
                    Intent intent = new Intent();
                    intent.setClass(BluetoothSafeModeActivity.this, BleDetailActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(String toString) {
            }
        });
    }

    @Override
    public void onSendCommand() {
        showLoading(getString(R.string.is_setting));
    }
}
