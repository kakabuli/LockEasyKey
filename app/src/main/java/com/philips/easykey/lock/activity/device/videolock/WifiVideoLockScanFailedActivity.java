package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewFirstActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;


public class WifiVideoLockScanFailedActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    TextView notice;

    private String wifiModelType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_video_scan_failed);

        wifiModelType = getIntent().getStringExtra("wifiModelType");

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        notice = findViewById(R.id.notice);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> startActivity(new Intent(this, WifiVideoLockHelpActivity.class)));
        findViewById(R.id.re_scan).setOnClickListener(v -> {
            finish();
            //退出当前界面
            Intent intent = new Intent(WifiVideoLockScanFailedActivity.this, WifiLockAddNewFirstActivity.class);
            intent.putExtra("wifiModelType",wifiModelType);
            startActivity(intent);
        });

    }


   /* public static class WifiVideoLockSafeModelActivity extends BaseActivity<IWifiLockSafeModeView, WifiLockSafeModePresenter<IWifiLockSafeModeView>> implements
            View.OnClickListener,IWifiLockSafeModeView {


        (R.id.iv_back)
        ImageView ivBack;
        (R.id.tv_content)
        TextView tvContent;
        (R.id.iv_right)
        ImageView ivRight;
        (R.id.iv_safe_mode)
        ImageView ivSafeMode;
        (R.id.rl_safe_mode)
        RelativeLayout rlSafeMode;
        (R.id.notice1)
        RelativeLayout notice1;
        (R.id.all)
        LinearLayout all;
        (R.id.iv_1)
        ImageView iv1;
        (R.id.tv_1)
        TextView tv1;
        (R.id.iv_2)
        ImageView iv2;
        (R.id.tv_2)
        TextView tv2;
        (R.id.no_card)
        LinearLayout noCard;
        (R.id.rl_notice)
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
                    ToastUtils.showLong(R.string.please_operation_in_lock);
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
