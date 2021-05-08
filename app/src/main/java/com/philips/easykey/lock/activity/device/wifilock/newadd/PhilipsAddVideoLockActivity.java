package com.philips.easykey.lock.activity.device.wifilock.newadd;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.blankj.utilcode.util.FragmentUtils;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.normal.NormalBaseActivity;
import com.philips.easykey.lock.publiclibrary.http.XiaokaiNewServiceImp;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockVideoBindBean;
import com.philips.easykey.lock.publiclibrary.http.postbean.WiFiLockVideoUpdateBindBean;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.WifiLockVideoBindResult;
import com.philips.easykey.lock.publiclibrary.http.util.BaseObserver;
import com.philips.easykey.lock.publiclibrary.http.util.RxjavaHelper;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.WifiLockVideoBindBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.WifiVideoLockBindErrorBean;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttConstant;
import com.philips.easykey.lock.publiclibrary.mqtt.util.MqttData;
import com.philips.easykey.lock.publiclibrary.xm.bean.QrCodeBean;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.QrCodeUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.WifiVideoPasswordFactorManager;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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
    private String mWifiSn;
    private String mRandomCode;
    private int mFunc;
    private int mTimes = 1;

    private WifiLockVideoBindBean mWifiLockVideoBindBean;

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
        getDeviceBindingStatus();
        task4Fragment.startCountDown();
    }

    public void onScanSuccess(WifiLockVideoBindBean wifiLockVideoBindBean) {
        // TODO: 2021/5/8 成功
        if(mWifiLockVideoBindBean == null) {
            mWifiLockVideoBindBean = wifiLockVideoBindBean;
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

            }else{
                onAdminPasswordError();
                mTimes++;
            }
        }

    }

    public void onBindSuccess(String randomCode,int func) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(this, WifiVideoLockAddSuccessActivity.class);
//                intent.putExtra(KeyConstants.WIFI_SN, wifiLockVideoBindBean.getWfId());
//                intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, randomCode);
//                intent.putExtra(KeyConstants.WIFI_LOCK_FUNC, func);
//                intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID,sSsid);
//                intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_DEVICE_DATA,wifiLockVideoBindBean);
//                startActivity(intent);
                finish();
            }
        });
    }

    public void onBindFailed(BaseResult baseResult) {
        com.philips.easykey.lock.utils.LogUtils.d("six-------" + baseResult.getCode());
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

    private void onAdminPasswordError() {
        if (mTimes < 5) {
            if(mTimes < 3){ // 正常提示
                showDialog(getString(R.string.activity_wifi_video_sixth_fail));

            }else {
                showDialog(getString(R.string.activity_wifi_video_sixth_fail_1)+ mTimes +getString(R.string.activity_wifi_video_sixth_fail_2));
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

    public void bindDevice(String wifiSN, String lockNickName, String uid, String randomCode, String wifiName, int func,
                           int distributionNetwork, String device_sn, String mac, String device_did, String p2p_password) {

        WiFiLockVideoBindBean bean = new WiFiLockVideoBindBean(wifiSN,lockNickName,uid,randomCode,wifiName,func,distributionNetwork,
                device_sn,mac,device_did,p2p_password);
        com.philips.easykey.lock.utils.LogUtils.d("WifiLockVideoSixthPresenter WiFiLockVideoBindBean-->" + bean.toString());
        XiaokaiNewServiceImp.wifiVideoLockBind(bean).subscribe(new BaseObserver<BaseResult>() {
            @Override
            public void onSuccess(BaseResult baseResult) {
                MyApplication.getInstance().getAllDevicesByMqtt(true);
//                        SPUtils.put(KeyConstants.WIFI_VIDEO_LOCK_OPERATION_RECORD + wifiSN, "");
//                        SPUtils.put(KeyConstants.WIFI_VIDEO_LOCK_VISITOR_RECORD + wifiSN, "");
                SPUtils.put(KeyConstants.WIFI_VIDEO_LOCK_ALARM_RECORD + wifiSN, "");
                SPUtils.put(KeyConstants.WIFI_LOCK_OPERATION_RECORD + wifiSN, "");
                SPUtils.put(KeyConstants.WIFI_VIDEO_LOCK_VISITOR_RECORD + wifiSN, "");
                SPUtils.put(KeyConstants.WIFI_LOCK_ALARM_RECORD + wifiSN, "");
                SPUtils.put(KeyConstants.WIFI_VIDEO_LOCK_RANDOMCODE + wifiSN,true);
                onBindSuccess(mRandomCode, mFunc);
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {
                onBindFailed(baseResult);
            }

            @Override
            public void onFailed(Throwable throwable) {
                LogUtils.e(throwable);
            }

            @Override
            public void onSubscribe1(Disposable d) {
                mCompositeDisposable.add(d);
            }
        });
    }

    public void unBindDeviceFail(String wifiSN) {
        XiaokaiNewServiceImp.wifiVideoLockBindFail(wifiSN,0).subscribe(new BaseObserver<WifiLockVideoBindResult>() {
            @Override
            public void onSuccess(WifiLockVideoBindResult wifiLockVideoBindResult) {
                onBindFailed(wifiLockVideoBindResult);
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {
                onBindFailed(baseResult);
            }

            @Override
            public void onFailed(Throwable throwable) {
                LogUtils.e(throwable);
            }

            @Override
            public void onSubscribe1(Disposable d) {
                mCompositeDisposable.add(d);
            }


        });
    }

    public void updateBindDevice(String wifiSN, String uid, String randomCode, String wifiName,
                                 int functionSet,String device_did, String p2p_password){
        WiFiLockVideoUpdateBindBean bean = new WiFiLockVideoUpdateBindBean(wifiSN,uid,randomCode,wifiName,functionSet,device_did,p2p_password);
        XiaokaiNewServiceImp.wifiVideoLockUpdateBind(bean).subscribe(new BaseObserver<WifiLockVideoBindResult>() {
            @Override
            public void onSuccess(WifiLockVideoBindResult wifiLockVideoBindResult) {
                onUpdateSuccess(wifiSN);
            }

            @Override
            public void onAckErrorCode(BaseResult baseResult) {
//                onUpdateFailed(baseResult);
            }

            @Override
            public void onFailed(Throwable throwable) {
//                onUpdateThrowable(throwable);
            }

            @Override
            public void onSubscribe1(Disposable d) {

            }
        });
    }

    public void onUpdateSuccess(String wifiSn) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(this, WifiVideoLockAddSuccessActivity.class);
//                intent.putExtra(KeyConstants.WIFI_SN, wifiLockVideoBindBean.getWfId());
//                intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID,sSsid);
//                intent.putExtra("update",true);
//                intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, password);
//                intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_DEVICE_DATA,wifiLockVideoBindBean);
//                startActivity(intent);
//                finish();
//            }
//        });
    }

    public void setNickName(String wifiSN, String lockNickname){
//        XiaokaiNewServiceImp.wifiLockUpdateNickname(wifiSN, MyApplication.getInstance().getUid(), lockNickname)
//                .subscribe(new BaseObserver<BaseResult>() {
//                    @Override
//                    public void onSuccess(BaseResult baseResult) {
//                        onSetNameSuccess();
//                    }
//
//                    @Override
//                    public void onAckErrorCode(BaseResult baseResult) {
//                        onSetNameFailedServer(baseResult);
//                    }
//
//                    @Override
//                    public void onFailed(Throwable throwable) {
//                        onSetNameFailedNet(throwable);
//                    }
//
//                    @Override
//                    public void onSubscribe1(Disposable d) {
//                        mCompositeDisposable.add(d);
//                    }
//                });

    }

    private Disposable listenerBindStatus;

    public void listenerBindingStatus(){
        if(mqttService != null){
            toDisposable(listenerBindStatus);
            listenerBindStatus = mqttService.listenerDataBack()
                    .compose(RxjavaHelper.observeOnMainThread())
                    .subscribe(new Consumer<MqttData>() {

                        @Override
                        public void accept(MqttData mqttData) throws Exception {
                            if(mqttData != null){
                                if(mqttData.getFunc().equals(MqttConstant.FUNC_WFEVENT)){

                                    WifiVideoLockBindErrorBean mWifiVideoLockBindErrorBean = new Gson().fromJson(mqttData.getPayload(),WifiVideoLockBindErrorBean.class);


                                    if(mWifiVideoLockBindErrorBean.getDevtype().equals(MqttConstant.WIFI_VIDEO_LOCK_XM)
                                            && mWifiVideoLockBindErrorBean.getEventtype().equals("errorNotify")){
                                        onBindFailed(new BaseResult());
                                    }
                                }

                            }



                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {

                        }
                    });
            mCompositeDisposable.add(listenerBindStatus);
        }

    }


}
