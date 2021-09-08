package com.philips.easykey.lock.activity.device.videolock;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewFirstActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddNewScanFailedActivity;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockChangeAdminPasswordActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.videolock.WifiVideoLockSixthPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockVideoSixthView;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.WifiLockVideoBindBean;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.WifiUtils;
import com.philips.easykey.lock.utils.WifiVideoPasswordFactorManager;


public class WifiVideoLockSixthActivity extends BaseActivity<IWifiLockVideoSixthView
        , WifiVideoLockSixthPresenter<IWifiLockVideoSixthView>> implements IWifiLockVideoSixthView {

    ImageView back;
    ImageView help;
    EditText apPasswordEdit;
    ImageView ivPasswordStatus;//密码状态图标
    boolean passwordHide = true;//密码图标

    private int times = 1; //次数从1开始

    byte[] data;

    public String wifiSn = "";

    private String randomCode = "";

    private String sSsid = "";

    private String password = "";

    private int func ;

    private WifiLockVideoBindBean wifiLockVideoBindBean;

    private String wifiModelType;

    private long time = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_video_sixth_input_admin_passwotd);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        apPasswordEdit = findViewById(R.id.ap_password_edit);
        ivPasswordStatus = findViewById(R.id.iv_password_status);

        back.setOnClickListener(v -> showWarring());
        help.setOnClickListener(v -> startActivity(new Intent(this, WifiVideoLockHelpActivity.class)));
        findViewById(R.id.button_next).setOnClickListener(v -> {
            String adminPassword = apPasswordEdit.getText().toString().trim();
            if (!StringUtil.randomJudge(adminPassword)) {
                ToastUtils.showShort(R.string.philips_random_verify_error);
                return;
            }
            //打开wifi
            WifiUtils wifiUtils = WifiUtils.getInstance(MyApplication.getInstance());
            if (!wifiUtils.isWifiEnable()) {
                ToastUtils.showShort(getString(R.string.philips_wifi_no_open_please_open_wifi));
            }
            if(System.currentTimeMillis() - time > 500){
                checkAdminPassword(adminPassword);
                time = System.currentTimeMillis();
            }
        });
        findViewById(R.id.iv_password_status).setOnClickListener(v -> {
            passwordHide = !passwordHide;
            if (passwordHide) {
                apPasswordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                /* etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);*/
                apPasswordEdit.setSelection(apPasswordEdit.getText().toString().length());//将光标移至文字末尾
                ivPasswordStatus.setImageResource(R.mipmap.eye_close_has_color);

            } else {
                //默认状态显示密码--设置文本 要一起写才能起作用 InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                //etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                apPasswordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                apPasswordEdit.setSelection(apPasswordEdit.getText().toString().length());//将光标移至文字末尾
                ivPasswordStatus.setImageResource(R.mipmap.eye_open_has_color);
            }
        });

        data =  getIntent().getByteArrayExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_DATA);
        times =  getIntent().getIntExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES,1);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        sSsid = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_SSID);
        wifiLockVideoBindBean = (WifiLockVideoBindBean) getIntent().getSerializableExtra(KeyConstants.WIFI_VIDEO_LOCK_DEVICE_DATA);
        wifiModelType = getIntent().getStringExtra("wifiModelType");

        randomCode = wifiLockVideoBindBean.getEventparams().getRandomCode();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        data =  getIntent().getByteArrayExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_DATA);
        times =  getIntent().getIntExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES,1);
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        sSsid = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_SSID);
        wifiLockVideoBindBean = (WifiLockVideoBindBean) getIntent().getSerializableExtra(KeyConstants.WIFI_VIDEO_LOCK_DEVICE_DATA);
        wifiModelType = getIntent().getStringExtra("wifiModelType");

        randomCode = wifiLockVideoBindBean.getEventparams().getRandomCode();
    }

    @Override
    public void onBackPressed() {
        showWarring();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected WifiVideoLockSixthPresenter<IWifiLockVideoSixthView> createPresent() {
        return new WifiVideoLockSixthPresenter<>();
    }


    private void onSuccess(String randomCode,int func) {
        runOnUiThread(() -> {
            Intent intent = new Intent(WifiVideoLockSixthActivity.this, WifiVideoLockAddSuccessActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiLockVideoBindBean.getWfId());
            intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, randomCode);
            intent.putExtra(KeyConstants.WIFI_LOCK_FUNC, func);
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID,sSsid);
            intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_DEVICE_DATA,wifiLockVideoBindBean);
            startActivity(intent);
            finish();
        });
    }

    public void onError(int errorCode) {
        runOnUiThread(() -> {
            finish();
            ToastUtils.showShort(R.string.bind_failed);
            startActivity(new Intent(WifiVideoLockSixthActivity.this, WifiLockAddNewScanFailedActivity.class));
        });
    }

    private void showDialog(String content){
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(
                WifiVideoLockSixthActivity.this
                , content,
                getString(R.string.philips_re_input), getString(R.string.forget_password_symbol), "#A4A4A4", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        Intent intent = new Intent(WifiVideoLockSixthActivity.this, WifiLockChangeAdminPasswordActivity.class);
                        intent.putExtra("video",true);
                        intent.putExtra(KeyConstants.WIFI_LOCK_ADMIN_PASSWORD_TIMES,times + 1);
                        startActivity(intent);
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
        if (times < 5) {
            if(times < 3){ // 正常提示
                showDialog(getString(R.string.activity_wifi_video_sixth_fail));

            }else {
                showDialog(getString(R.string.activity_wifi_video_sixth_fail_1)+ times +getString(R.string.activity_wifi_video_sixth_fail_2));
            }
        } else { //都五次输入错误提示   退出
            AlertDialogUtil.getInstance().noEditSingleCanNotDismissButtonDialog(
                    WifiVideoLockSixthActivity.this, "", getString(R.string.activity_wifi_video_sixth_fail_3) +
                            getString(R.string.activity_wifi_video_sixth_fail_4), getString(R.string.philips_confirm), new AlertDialogUtil.ClickListener() {
                        @Override
                        public void left() {

                        }

                        @Override
                        public void right() {
                            mPresenter.unBindDeviceFail(wifiLockVideoBindBean.getWfId());
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

    private void showWarring(){
        AlertDialogUtil.getInstance().noEditTitleTwoButtonDialog(
                this
                , getString(R.string.activity_wifi_video_fifth_network),
                getString(R.string.philips_cancel), getString(R.string.philips_confirm), "#A4A4A4", "#1F96F7", new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        //退出当前界面
                        Intent intent = new Intent(WifiVideoLockSixthActivity.this, WifiLockAddNewFirstActivity.class);
                        intent.putExtra("wifiModelType",wifiModelType);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(String toString) {

                    }
                });
    }





    private void checkAdminPassword(String adminPassword){
        if(!TextUtils.isEmpty(randomCode)){
            WifiVideoPasswordFactorManager.FactorResult result = WifiVideoPasswordFactorManager.parsePasswordData(adminPassword,randomCode);
            if(result.result == 0){

                randomCode = Rsa.bytesToHexString(result.password);
                func = result.func;
                if(MyApplication.getInstance().getWifiLockInfoBySn(wifiLockVideoBindBean.getWfId()) == null){
                    mPresenter.bindDevice(wifiLockVideoBindBean.getWfId(),wifiLockVideoBindBean.getWfId(),wifiLockVideoBindBean.getUserId(),
                            Rsa.bytesToHexString(result.password),sSsid,result.func,3,
                        wifiLockVideoBindBean.getEventparams().getDevice_sn(),wifiLockVideoBindBean.getEventparams().getMac(),
                        wifiLockVideoBindBean.getEventparams().getDevice_did(),wifiLockVideoBindBean.getEventparams().getP2p_password()
                );


                }else{

                    mPresenter.updateBindDevice(wifiLockVideoBindBean.getWfId(),wifiLockVideoBindBean.getUserId(),
                            Rsa.bytesToHexString(result.password),sSsid,result.func,wifiLockVideoBindBean.getEventparams().getDevice_did(),
                            wifiLockVideoBindBean.getEventparams().getP2p_password());
                }

            }else{
                mPresenter.handler.post(() -> {
                    onAdminPasswordError();
                    times++;
                });
            }
        }



    }

    @Override
    public void onBindSuccess(String wifiSn) {
        onSuccess(randomCode,func);
    }

    @Override
    public void onBindFailed(BaseResult baseResult) {
        LogUtils.d("six-------" + baseResult.getCode());
        mPresenter.unBindDeviceFail(wifiLockVideoBindBean.getWfId());
        mPresenter.handler.post(() -> {
            Intent intent = new Intent(WifiVideoLockSixthActivity.this, WifiVideoLockScanFailedActivity.class);
            intent.putExtra("wifiModelType",wifiModelType);
            startActivity(intent);

            finish();
        });
    }

    @Override
    public void onBindThrowable(Throwable throwable) {

    }

    @Override
    public void onUpdateSuccess(String wifiSn) {
        runOnUiThread(() -> {
            Intent intent = new Intent(WifiVideoLockSixthActivity.this, WifiVideoLockAddSuccessActivity.class);
            intent.putExtra(KeyConstants.WIFI_SN, wifiLockVideoBindBean.getWfId());
            intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID,sSsid);
            intent.putExtra("update",true);
            intent.putExtra(KeyConstants.WIFI_LOCK_RANDOM_CODE, password);
            intent.putExtra(KeyConstants.WIFI_VIDEO_LOCK_DEVICE_DATA,wifiLockVideoBindBean);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onUpdateFailed(BaseResult baseResult) {

    }

    @Override
    public void onUpdateThrowable(Throwable throwable) {

    }

    @Override
    public void onCheckError(byte[] data) {

    }

    @Override
    public void onSetNameSuccess() {

    }

    @Override
    public void onSetNameFailedNet(Throwable throwable) {

    }

    @Override
    public void onSetNameFailedServer(BaseResult baseResult) {

    }
}
