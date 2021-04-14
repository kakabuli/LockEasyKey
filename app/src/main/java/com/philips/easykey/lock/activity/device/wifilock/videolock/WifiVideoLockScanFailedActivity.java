package com.philips.easykey.lock.activity.device.wifilock.videolock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewFirstActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WifiVideoLockScanFailedActivity extends BaseAddToApplicationActivity {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.help)
    ImageView help;
    @BindView(R.id.notice)
    TextView notice;

    private String wifiModelType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_video_scan_failed);

        wifiModelType = getIntent().getStringExtra("wifiModelType");
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back, R.id.help,R.id.notice,R.id.re_scan,R.id.cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.help:
                startActivity(new Intent(this, WifiVideoLockHelpActivity.class));
                break;
           /* case R.id.notice:
                startActivity(new Intent(WifiLockVideoScanFailedActivity.this, WifiLcokSupportWifiActivity.class));
                break;*/
            case R.id.re_scan:
                finish();
                //退出当前界面
                Intent intent = new Intent(WifiVideoLockScanFailedActivity.this, WifiLockAddNewFirstActivity.class);
                intent.putExtra("wifiModelType",wifiModelType);
                startActivity(intent);
                break;
            case R.id.cancel:
                break;
        }
    }

   /* public static class WifiVideoLockSafeModelActivity extends BaseActivity<IWifiLockSafeModeView, WifiLockSafeModePresenter<IWifiLockSafeModeView>> implements
            View.OnClickListener,IWifiLockSafeModeView {


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
        @BindView(R.id.notice1)
        RelativeLayout notice1;
        @BindView(R.id.all)
        LinearLayout all;
        @BindView(R.id.iv_1)
        ImageView iv1;
        @BindView(R.id.tv_1)
        TextView tv1;
        @BindView(R.id.iv_2)
        ImageView iv2;
        @BindView(R.id.tv_2)
        TextView tv2;
        @BindView(R.id.no_card)
        LinearLayout noCard;
        @BindView(R.id.rl_notice)
        RelativeLayout rlNotice;
        private String wifiSn;

        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_bluetooth_safe_mode);
            ButterKnife.bind(this);

            ivBack.setOnClickListener(this);
            tvContent.setText(R.string.safe_mode);
            rlSafeMode.setOnClickListener(this);
            wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
            mPresenter.init(wifiSn);
            WifiLockInfo wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
            String functionSet = wifiLockInfo.getFunctionSet();
            int safeMode = wifiLockInfo.getSafeMode();
            ivSafeMode.setImageResource(safeMode == 1 ? R.mipmap.iv_open : R.mipmap.iv_close);
            int func;
            try {
                func = Integer.parseInt(functionSet);
            } catch (Exception e) {
                func = 0x04;
            }
            boolean supportCard = BleLockUtils.isSupportCard(func + "");
            boolean supportFinger = BleLockUtils.isSupportFinger(func + "");
            boolean supportPassword = BleLockUtils.isSupportPassword(func + "");
            if (supportCard && supportFinger && supportPassword) {
                all.setVisibility(View.VISIBLE);
                noCard.setVisibility(View.GONE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(rlNotice.getLayoutParams());
                lp.setMargins(0, 0, 0, Utils.dp2px(this, 60));
                rlNotice.setLayoutParams(lp);
            } else {
                all.setVisibility(View.GONE);
                noCard.setVisibility(View.VISIBLE);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(rlNotice.getLayoutParams());
                lp.setMargins(0, 0, 0, Utils.dp2px(this, 100));
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

        @Override
        protected WifiLockSafeModePresenter<IWifiLockSafeModeView> createPresent() {
            return new WifiLockSafeModePresenter<>();
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.rl_safe_mode:
                    ToastUtil.getInstance().showLong(R.string.please_operation_in_lock);
                    break;
            }
        }


        @Override
        public void onWifiLockActionUpdate() {
            WifiLockInfo wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
            if (ivSafeMode!=null && wifiLockInfo!=null){
                int safeMode = wifiLockInfo.getSafeMode();
                ivSafeMode.setImageResource(safeMode == 1 ? R.mipmap.iv_open : R.mipmap.iv_close);
            }
        }
    }*/
}
