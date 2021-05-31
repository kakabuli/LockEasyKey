package com.philips.easykey.lock.publiclibrary.ota.gatewayota;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.GatewayOTAPresenter;
import com.philips.easykey.lock.mvp.view.GatewayOTAView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GatewayOtaNotifyBean;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;


public class GatewayOTADialogActivity extends BaseActivity<GatewayOTAView, GatewayOTAPresenter<GatewayOTAView>> implements GatewayOTAView {


    TextView tvContent;
    TextView tvLeft;
    TextView tvRight;
    private GatewayOtaNotifyBean notifyBean;

    @Override
    protected GatewayOTAPresenter<GatewayOTAView> createPresent() {
        return new GatewayOTAPresenter<>();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.d("网关ota升级通知 GatewayOTADialogActivity ");
        setContentView(R.layout.activity_otadialog1);

        tvContent = findViewById(R.id.tv_content);
        tvLeft = findViewById(R.id.tv_left);
        tvRight = findViewById(R.id.tv_right);

        tvLeft.setOnClickListener(v -> finish());
        tvRight.setOnClickListener(v -> {
            if (notifyBean != null) {
                mPresenter.confirmGatewayOtaUpgrade(notifyBean, MyApplication.getInstance().getUid());
            }
        });

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        notifyBean = (GatewayOtaNotifyBean) intent.getSerializableExtra(KeyConstants.GATEWAY_OTA_UPGRADE);
        if (notifyBean != null) {
            // 网关
            String swInfo = notifyBean.getParams().getSW();
            String deviceSn = notifyBean.getParams().getDeviceList().get(0).toString();
            String swInfoStr = String.format(getString(R.string.have_gateway_version), swInfo);


            if (deviceSn.startsWith("GW")) {
                if (swInfo.startsWith("orangeiot")) {
                    tvContent.setText(Html.fromHtml("<big><font color='black'>" + swInfoStr + "</font></big>" + "<br>" + "<font color='#999999'>"
                                    + getString(R.string.gateway) + ":" + notifyBean.getDeviceId() + "</font>"));
                } else if (swInfo.startsWith("znp")) {
                    tvContent.setText(Html.fromHtml("<big><font color='black'>" + swInfoStr + "</font></big>" + "<br>" + "<font color='#999999'>"
                            + getString(R.string.gateway_zigbeen_have_update) + ":" + deviceSn + "</font>"));
                }
            } else if (deviceSn.startsWith("ZG")) { //zigbeen
                tvContent.setText(Html.fromHtml("<big><font color='black'>" + swInfoStr + "</font></big>" + "<br>" + "<font color='#999999'>"
                        + getString(R.string.zigbeen_have_update) + ":" + deviceSn + "</font>"));
                //    tvContent.setText(getString(R.string.zigbeen_have_update) + ":" + notifyBean.getDeviceId() + "\n" + swInfoStr);
            } else if (deviceSn.startsWith("CH")) { // 猫眼
                tvContent.setText(Html.fromHtml("<big><font color='black'>" + swInfoStr + "</font></big>" + "<br>" + "<font color='#999999'>"
                        + getString(R.string.cateye) + ":" + deviceSn + "</font>"));
                //  tvContent.setText(getString(R.string.cateye) + ":" + notifyBean.getDeviceId() + "\n" + swInfoStr);
            } else {
                finish();
            }
        } else {
            finish();
        }
    }

    private void initView() {

        //设置窗口对齐屏幕宽度
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        win.getDecorView().setScaleX((float) 0.96);
        win.getDecorView().setScaleY((float) 0.96);

        WindowManager.LayoutParams lp = win.getAttributes();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        lp.width = (int) (width * 0.9);
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;//设置对话框置顶显示
        win.setAttributes(lp);
        //设置点击外部空白处可以关闭Activity
        this.setFinishOnTouchOutside(true);
    }


    @Override
    public void gatewayUpgradeingNow(String deviceId) {
        ToastUtils.showShort(deviceId + getString(R.string.gateway_upgrade_now));
        finish();
    }

    @Override
    public void gatewayUpgradeFail(String deviceId) {
        ToastUtils.showShort(deviceId + getString(R.string.gateway_upgade_fail));
        finish();
    }
}
