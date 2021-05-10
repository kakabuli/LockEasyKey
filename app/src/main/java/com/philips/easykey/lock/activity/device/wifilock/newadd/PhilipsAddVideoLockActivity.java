package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.dialog.PhilipsOpenLocationDialog;
import com.philips.easykey.lock.dialog.PhilipsWiFiNotConnectDialog;
import com.philips.easykey.lock.normal.NormalBaseActivity;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockVideoBindBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockVideoUpdateBindBean;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockVideoBindResult;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.WifiLockVideoBindBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.WifiVideoLockBindErrorBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.xm.bean.QrCodeBean;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.QrCodeUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.WifiVideoPasswordFactorManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * author : Jack
 * time   : 2021/4/30
 * E-mail : wengmaowei@kaadas.com
 * desc   : 添加设备配网流程
 */
public class PhilipsAddVideoLockActivity extends NormalBaseActivity {

    private View mVTask1, mVTask2, mVTask3, mVTask4, mVTask5;
    private View mVTask1N2Line, mVTask2N3Line, mVTask3N4Line, mVTask4N5Line;
    private TextView mTvTitle;
    private ImageView mIvHelp;

    private final ArrayList<Fragment> mFragments = new ArrayList<>();

    public String mAdminPassword;
    public String mWifiName;
    private String mWifiPwd;
    private String mRandomCode;
    private int mFunc;
    private int mTimes = 1;

    private WifiLockVideoBindBean mWifiLockVideoBindBean;
    private PhilipsOpenLocationDialog mOpenLocationDialog;
    private PhilipsWiFiNotConnectDialog mWiFiNotConnectDialog;

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.philips_activity_add_video_lock;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {
        initTaskShowUI();
        mTvTitle = findViewById(R.id.tvTitle);
        mIvHelp = findViewById(R.id.ivHelp);
        mFragments.add(PhilipsAddVideoLockTask1Fragment.getInstance());
        mFragments.add(PhilipsAddVideoLockTask2Fragment.getInstance());
        mFragments.add(PhilipsAddVideoLockTask3Fragment.getInstance());
        mFragments.add(PhilipsAddVideoLockTask4Fragment.getInstance());
        mFragments.add(PhilipsAddVideoLockTask5Fragment.getInstance());
        mFragments.add(PhilipsAddVideoLockTask6Fragment.getInstance());
        FragmentUtils.add(getSupportFragmentManager(), mFragments, R.id.fcvAddVideoLock, 0);
        initDialogs();

        applyDebouncingClickListener(findViewById(R.id.ivBack), mIvHelp);
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onDebouncingClick(@NonNull View view) {
        if(view.getId() == R.id.ivHelp) {
            // TODO: 2021/5/6 进入帮助页面
        } else if(view.getId() == R.id.ivBack) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        toDisposable(mGetDeviceBindingDisposable);
        super.onDestroy();
    }

    private void initDialogs() {
        mOpenLocationDialog = new PhilipsOpenLocationDialog(this);
        mOpenLocationDialog.setOnOpenLocationListener(new PhilipsOpenLocationDialog.OnOpenLocationListener() {
            @Override
            public void setting() {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent,887);
            }

            @Override
            public void cancel() {
                if(mOpenLocationDialog != null) {
                    mOpenLocationDialog.dismiss();
                }
            }
        });
        mWiFiNotConnectDialog = new PhilipsWiFiNotConnectDialog(this);
        mWiFiNotConnectDialog.setOnWifiNotConnectListener(new PhilipsWiFiNotConnectDialog.OnWifiNotConnectListener() {
            @Override
            public void cancel() {
                if(mWiFiNotConnectDialog != null) {
                    mWiFiNotConnectDialog.dismiss();
                }
            }

            @Override
            public void gotoConnect() {
                Intent wifiIntent =  new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(wifiIntent);
            }
        });
    }

    public void showOpenLocDialog() {
        if(mOpenLocationDialog != null) {
            mOpenLocationDialog.show();
        }
    }

    public void showWifiNotConnectDialog() {
        if(mWiFiNotConnectDialog != null) {
            mWiFiNotConnectDialog.show();
        }
    }

    private void initTaskShowUI() {
        mVTask1 = findViewById(R.id.vTask1);
        mVTask2 = findViewById(R.id.vTask2);
        mVTask3 = findViewById(R.id.vTask3);
        mVTask4 = findViewById(R.id.vTask4);
        mVTask5 = findViewById(R.id.vTask5);
        mVTask1N2Line = findViewById(R.id.vTask1N2Line);
        mVTask2N3Line = findViewById(R.id.vTask2N3Line);
        mVTask3N4Line = findViewById(R.id.vTask3N4Line);
        mVTask4N5Line = findViewById(R.id.vTask4N5Line);
    }

    public void showFirstTask1() {
        mVTask1.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask1N2Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask2.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mVTask2N3Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask3.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mVTask3N4Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask4.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mVTask4N5Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask5.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mTvTitle.setText(R.string.philips_enter_management_mode);
        mIvHelp.setVisibility(View.VISIBLE);
        FragmentUtils.showHide(0, mFragments);
    }

    public void showFirstTask2() {
        mVTask1.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask1N2Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask2.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask2N3Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask3.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mVTask3N4Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask4.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mVTask4N5Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask5.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mTvTitle.setText(R.string.philips_connect_wifi);
        mIvHelp.setVisibility(View.VISIBLE);
        FragmentUtils.showHide(1, mFragments);
    }

    public void showFirstTask3() {
        mVTask1.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask1N2Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask2.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask2N3Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask3.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask3N4Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask4.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mVTask4N5Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask5.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mTvTitle.setText(R.string.philips_qr_code_msg);
        mIvHelp.setVisibility(View.GONE);
        FragmentUtils.showHide(2, mFragments);
        getDeviceBindingStatus();
        if(mFragments.get(2) instanceof PhilipsAddVideoLockTask3Fragment) {
            ((PhilipsAddVideoLockTask3Fragment)mFragments.get(2)).refreshQrCode();
        }
    }

    public void showFirstTask4() {
        mVTask1.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask1N2Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask2.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask2N3Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask3.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask3N4Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask4.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mVTask4N5Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask5.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mTvTitle.setText(R.string.philips_network_pairing);
        mIvHelp.setVisibility(View.GONE);
        FragmentUtils.showHide(3, mFragments);
        if(mFragments.get(3) instanceof PhilipsAddVideoLockTask4Fragment) {
            startTask4((PhilipsAddVideoLockTask4Fragment) mFragments.get(3));
        }
    }

    public void showFirstTask5() {
        mVTask1.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask1N2Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask2.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask2N3Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask3.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask3N4Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask4.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask4N5Line.setBackgroundColor(getColor(R.color.cB3C8E6));
        mVTask5.setBackgroundResource(R.drawable.philips_shape_task_prepare_circle_view);
        mTvTitle.setText(R.string.philips_confirm_management_pwd);
        mIvHelp.setVisibility(View.GONE);
        FragmentUtils.showHide(4, mFragments);
    }

    public void showFirstTask6() {
        mVTask1.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask1N2Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask2.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask2N3Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask3.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask3N4Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask4.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mVTask4N5Line.setBackgroundColor(getColor(R.color.c0066A1));
        mVTask5.setBackgroundResource(R.drawable.philips_shape_task_done_circle_view);
        mTvTitle.setText(R.string.philips_add_success);
        mIvHelp.setVisibility(View.GONE);
        FragmentUtils.showHide(5, mFragments);
    }

    public void setWifiInfo(@NonNull String wifiName, @NonNull String wifiPwd) {
        this.mWifiName = wifiName;
        this.mWifiPwd = wifiPwd;
    }

    public Bitmap getQrCode() {
        if(TextUtils.isEmpty(mWifiName)||TextUtils.isEmpty(mWifiPwd)) {
            LogUtils.e("wifiName: " + mWifiName + " wifiPwd: " + mWifiPwd);
            return null;
        }
        QrCodeBean qrCodeBean = new QrCodeBean(mWifiName, MyApplication.getInstance().getUid(), mWifiPwd);
        String qrCodeJson = new Gson().toJson(qrCodeBean);
        LogUtils.d("qr code json: " + qrCodeJson);
        return QrCodeUtils.createQRCode(qrCodeJson, 240);
    }

    /*------------------------------ 绑定流程 --------------------------------*/

    private Disposable mGetDeviceBindingDisposable;

    private void getDeviceBindingStatus(){
        if(mqttService != null){
            toDisposable(mGetDeviceBindingDisposable);
            mGetDeviceBindingDisposable = mqttService.listenerDataBack()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(MQttData -> {
                        if(MQttData != null){
                            if(MQttData.getFunc().equals(MqttConstant.FUNC_WFEVENT)){

                                WifiLockVideoBindBean wifiLockVideoBindBean = new Gson()
                                        .fromJson(MQttData.getPayload(),WifiLockVideoBindBean.class);

                                if(wifiLockVideoBindBean.getEventparams() != null
                                        && wifiLockVideoBindBean.getEventtype()
                                        .equals(MqttConstant.WIFI_VIDEO_BINDINFO_NOTIFY)){
                                    onScanSuccess(wifiLockVideoBindBean);
                                }
                            }
                        }
                    }, LogUtils::e);
            mCompositeDisposable.add(mGetDeviceBindingDisposable);
        }
    }

    public void startTask4(PhilipsAddVideoLockTask4Fragment task4Fragment) {
        task4Fragment.startCountDown();
    }

    public void onScanSuccess(WifiLockVideoBindBean wifiLockVideoBindBean) {
        // TODO: 2021/5/8 成功
        if(mWifiLockVideoBindBean == null) {
            mWifiLockVideoBindBean = wifiLockVideoBindBean;
            mRandomCode = wifiLockVideoBindBean.getEventparams().getRandomCode();
        } else {
            // TODO: 2021/5/8 后来重复，可能需要通过判断时间戳来进行选择
        }
        if(mFragments.get(3) instanceof PhilipsAddVideoLockTask4Fragment) {
            ((PhilipsAddVideoLockTask4Fragment) mFragments.get(3)).cancelCountDown();
        }
        showFirstTask5();
    }

    public void onScanFailed() {
        // TODO: 2021/5/8 失败
    }

    /*-------------------------------- 输入管理密码 -----------------------------*/

    public void setAdminPwd(String adminPwd) {
        if(!TextUtils.isEmpty(mRandomCode)){
            WifiVideoPasswordFactorManager.FactorResult result = WifiVideoPasswordFactorManager.parsePasswordData(adminPwd,mRandomCode);
            if(result.result == 0){

                mRandomCode = Rsa.bytesToHexString(result.password);
                mFunc = result.func;
                if(MyApplication.getInstance().getWifiLockInfoBySn(mWifiLockVideoBindBean.getWfId()) == null){
                    bindDevice(mWifiLockVideoBindBean.getWfId(),mWifiLockVideoBindBean.getWfId(),mWifiLockVideoBindBean.getUserId(),
                            Rsa.bytesToHexString(result.password),mWifiName,result.func,3,
                            mWifiLockVideoBindBean.getEventparams().getDevice_sn(),mWifiLockVideoBindBean.getEventparams().getMac(),
                            mWifiLockVideoBindBean.getEventparams().getDevice_did(),mWifiLockVideoBindBean.getEventparams().getP2p_password());

                } else {
                    updateBindDevice(mWifiLockVideoBindBean.getWfId(),
                            mWifiLockVideoBindBean.getUserId(),
                            Rsa.bytesToHexString(result.password),mWifiName,
                            result.func,mWifiLockVideoBindBean.getEventparams().getDevice_did(),
                            mWifiLockVideoBindBean.getEventparams().getP2p_password());
                }

            } else {
                adminPasswordError();
                mTimes++;
            }
        }

    }

    public void bindSuccess() {
        runOnUiThread(this::showFirstTask6);
    }

    public void bindFailed(BaseResult baseResult) {
        // TODO: 2021/5/10 绑定失败
        LogUtils.d("six-------" + baseResult.getCode());
        unBindDeviceFail(mWifiLockVideoBindBean.getWfId());
//        .post(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(WifiVideoLockSixthActivity.this, WifiVideoLockScanFailedActivity.class);
//                intent.putExtra("wifiModelType",wifiModelType);
//                startActivity(intent);
//
//                finish();
//            }
//        });
    }

    private void showDialog(String content){
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(
                this
                , content,
                getString(R.string.re_input), getString(R.string.forget_password_symbol), "#A4A4A4", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
//                        Intent intent = new Intent(this, WifiLockChangeAdminPasswordActivity.class);
//                        intent.putExtra("video",true);
//                        intent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES,mTimes + 1);
//                        startActivity(intent);
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }

    private void adminPasswordError() {
        if (mTimes < 5) {
            if(mTimes < 3){ // 正常提示
                showDialog(getString(R.string.activity_wifi_video_sixth_fail));

            }else {
                showDialog(getString(R.string.philips_tip_input_admin_pwd_fail, mTimes));
            }
        } else { //都五次输入错误提示   退出
            AlertDialogUtil.getInstance().noEditSingleCanNotDismissButtonDialog(this,
                    "", getString(R.string.activity_wifi_video_sixth_fail_3) +
                            getString(R.string.activity_wifi_video_sixth_fail_4), getString(R.string.confirm),
                    new AlertDialogUtil.ClickListener() {
                        @Override
                        public void left() {

                        }

                        @Override
                        public void right() {
                            unBindDeviceFail(mWifiLockVideoBindBean.getWfId());
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

    public void bindDevice(String wifiSN, String lockNickName, String uid,
                           String randomCode, String wifiName, int func,
                           int distributionNetwork, String device_sn,
                           String mac, String device_did, String p2p_password) {

        WiFiLockVideoBindBean bean = new WiFiLockVideoBindBean(wifiSN,lockNickName,
                uid,randomCode,wifiName,func,distributionNetwork,
                device_sn,mac,device_did,p2p_password);
        LogUtils.d(bean.toString());
        XiaokaiNewServiceImp.wifiVideoLockBind(bean)
                .safeSubscribe(new Observer<WifiLockVideoBindResult>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull WifiLockVideoBindResult wifiLockVideoBindResult) {
                        MyApplication.getInstance().getAllDevicesByMqtt(true);
                        bindSuccess();
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        LogUtils.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void unBindDeviceFail(String wifiSN) {
        XiaokaiNewServiceImp.wifiVideoLockBindFail(wifiSN,0)
                .safeSubscribe(new Observer<WifiLockVideoBindResult>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull WifiLockVideoBindResult wifiLockVideoBindResult) {
                        bindFailed(wifiLockVideoBindResult);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        LogUtils.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateBindDevice(String wifiSN, String uid, String randomCode, String wifiName,
                                 int functionSet,String device_did, String p2p_password){
        WiFiLockVideoUpdateBindBean bean = new WiFiLockVideoUpdateBindBean(wifiSN,uid,randomCode,wifiName,functionSet,device_did,p2p_password);
        XiaokaiNewServiceImp.wifiVideoLockUpdateBind(bean)
                .safeSubscribe(new Observer<WifiLockVideoBindResult>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull WifiLockVideoBindResult wifiLockVideoBindResult) {
                        String code = wifiLockVideoBindResult.getCode();
                        if(TextUtils.isEmpty(code)) {
                            LogUtils.e("code is empty");
                            return;
                        }
                        if(!code.equals("200")) {
                            String msg = wifiLockVideoBindResult.getMsg();
                            LogUtils.e("code: " + code + " msg: " + msg);
                            if(!TextUtils.isEmpty(msg)) ToastUtils.showShort(msg);
                            // TODO: 2021/5/10 错误跳转
                            return;
                        }
                        bindSuccess();
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        LogUtils.e(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void setNickName(String lockNickname){
        // TODO: 2021/5/10 设置名字
        XiaokaiNewServiceImp.wifiLockUpdateNickname(mWifiLockVideoBindBean.getEventparams().getDevice_sn(),
                MyApplication.getInstance().getUid(), lockNickname)
                .safeSubscribe(new Observer<BaseResult>() {
                    @Override
                    public void onSubscribe(@NotNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NotNull BaseResult baseResult) {
                        // TODO: 2021/5/10 设置名称成功
                        String code = baseResult.getCode();
                        if(TextUtils.isEmpty(code)) {
                            LogUtils.e("code is empty");
                            return;
                        }
                        if(!code.equals("200")) {
                            String msg = baseResult.getMsg();
                            LogUtils.e("code: " + code + " msg: " + msg);
                            if(!TextUtils.isEmpty(msg)) ToastUtils.showShort(msg);
                            return;
                        }
                        // TODO: 2021/5/10 可能需要做出提示
                        finish();
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        // TODO: 2021/5/10 设置失败
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private Disposable mListenerBindStatus;

    public void listenerBindingStatus(){
        if(mqttService != null){
            toDisposable(mListenerBindStatus);
            mListenerBindStatus = mqttService.listenerDataBack()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(mqttData -> {
                        if(mqttData != null){
                            if(mqttData.getFunc().equals(MqttConstant.FUNC_WFEVENT)){

                                WifiVideoLockBindErrorBean mWifiVideoLockBindErrorBean = new Gson()
                                        .fromJson(mqttData.getPayload(),WifiVideoLockBindErrorBean.class);

                                if(mWifiVideoLockBindErrorBean.getDevtype().equals(MqttConstant.WIFI_VIDEO_LOCK_XM)
                                        && mWifiVideoLockBindErrorBean.getEventtype().equals("errorNotify")){
                                    bindFailed(new BaseResult());
                                }
                            }

                        }

                    }, LogUtils::e);
            mCompositeDisposable.add(mListenerBindStatus);
        }

    }


}
