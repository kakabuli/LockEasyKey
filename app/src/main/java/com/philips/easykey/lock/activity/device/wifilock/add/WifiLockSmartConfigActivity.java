package com.philips.easykey.lock.activity.device.wifilock.add;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.blankj.utilcode.util.ToastUtils;

import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.TouchNetUtil;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LoadingDialog;
import com.blankj.utilcode.util.LogUtils;

import java.lang.ref.WeakReference;
import java.util.List;


public class WifiLockSmartConfigActivity extends BaseAddToApplicationActivity {

    ImageView back;
    ImageView help;
    CheckBox cbSendWifiAccountPassword;
    CheckBox cbSuccess;
    EditText tvSupportList;
    ImageView ivAnim;
    CheckBox bindSuccess;
    LinearLayout llBindSuccess;

    private EsptouchAsyncTask4 mTask;
    public String TAG = "WifiLockSmartConfigActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_lock_connect_device);

        back = findViewById(R.id.back);
        help = findViewById(R.id.help);
        cbSendWifiAccountPassword = findViewById(R.id.cb_send_wifi_account_password);
        cbSuccess = findViewById(R.id.cb_success);
        tvSupportList = findViewById(R.id.tv_support_list);
        ivAnim = findViewById(R.id.iv_anim);
        bindSuccess = findViewById(R.id.bind_success);
        llBindSuccess = findViewById(R.id.ll_bind_success);

        back.setOnClickListener(v -> finish());
        help.setOnClickListener(v -> startActivity(new Intent(WifiLockSmartConfigActivity.this, WifiLockHelpActivity.class)));

        //通过设置android:background时，得到AnimationDrawable 用如下方法
        final AnimationDrawable animationDrawable = (AnimationDrawable) ivAnim.getBackground();
        animationDrawable.start();


        String sSsid = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_SSID);
        byte[] ssidArrays = getIntent().getByteArrayExtra(KeyConstants.WIFI_LOCK_WIFI_SSID_ARRAYS);
        String sPassword = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_PASSWORD);
        String wifiBssid = getIntent().getStringExtra(KeyConstants.WIFI_LOCK_WIFI_BSSID);
        if (mTask != null) {
            mTask.cancelEsptouch();
        }
        mTask = new EsptouchAsyncTask4(this, new ISetUpResult() {
            @Override
            public void onSetUpFailed() {
                Intent intent = new Intent(WifiLockSmartConfigActivity.this, WifiLockAPAddFailedActivity.class);
                intent.putExtra(KeyConstants.WIFI_LOCK_SETUP_IS_AP, false);
                startActivity(intent);
                ToastUtils.showLong(getString(R.string.wifi_model_set_up_failed));
            }

            @Override
            public void onSetUpSuccess(String hostAddress) { //设置成功
                ToastUtils.showShort(getString(R.string.set_up_success2));
                Intent intent = new Intent(WifiLockSmartConfigActivity.this, WifiLockInputAdminPasswordActivity.class);
                intent.putExtra(KeyConstants.WIFI_LOCK_WIFI_SSID, sSsid);
                startActivity(intent);

            }
        });

        byte[] password = ByteUtil.getBytesByString(sPassword);
        byte[] bssid;
        if (TextUtils.isEmpty(wifiBssid)){
            bssid = new byte[6];
        }else {
              bssid = TouchNetUtil.parseBssid2bytes(wifiBssid);
        }
        byte[] deviceCount = {1};
        byte[] broadcast = {1};
        mTask.execute(ssidArrays, bssid, password, deviceCount, broadcast);
        //通过设置android:background时，得到AnimationDrawable 用如下方法
        llBindSuccess.setVisibility(View.GONE);
    }

    private static class EsptouchAsyncTask4 extends AsyncTask<byte[], IEsptouchResult, List<IEsptouchResult>> {
        private WeakReference<WifiLockSmartConfigActivity> mActivity;

        private final Object mLock = new Object();
        private AlertDialog mResultDialog;
        private IEsptouchTask mEsptouchTask;
        private LoadingDialog loadingDialog;
        private ISetUpResult setUpResult;

        EsptouchAsyncTask4(WifiLockSmartConfigActivity activity, ISetUpResult setUpResult) {
            mActivity = new WeakReference(activity);
            loadingDialog = LoadingDialog.getInstance(activity);
            this.setUpResult = setUpResult;
        }

        void cancelEsptouch() {
            cancel(true);
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            if (mResultDialog != null) {
                mResultDialog.dismiss();
            }
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(IEsptouchResult... values) {

        }

        @Override
        protected List<IEsptouchResult> doInBackground(byte[]... params) {
            WifiLockSmartConfigActivity activity = mActivity.get();
            synchronized (mLock) {
                byte[] apSsid = params[0];
                byte[] apBssid = params[1];
                byte[] apPassword = params[2];
                Context context = activity.getApplicationContext();
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, context);
                mEsptouchTask.setPackageBroadcast(true);  //true 广播方式
                mEsptouchTask.setEsptouchListener(this::publishProgress);
            }
            return mEsptouchTask.executeForResults(1);
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            WifiLockSmartConfigActivity activity = mActivity.get();
            loadingDialog.dismiss();
            if (result == null) {
                mResultDialog = new AlertDialog.Builder(activity)
                        .setMessage(R.string.configure_result_failed_port)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                mResultDialog.setCanceledOnTouchOutside(false);
                cancelEsptouch();
                return;
            }
            // check whether the task is cancelled and no results received
            IEsptouchResult firstResult = result.get(0);
            if (firstResult.isCancelled()) {
                //配网被取消
                cancelEsptouch();
                return;
            }

            if (!firstResult.isSuc()) {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                ToastUtils.showLong(R.string.set_up_failed);
                setUpResult.onSetUpFailed();
            } else {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                String deviceBssid = firstResult.getBssid();
                String hostAddress = firstResult.getInetAddress().getHostAddress();
                LogUtils.d("配置成功   deviceBssid   " + deviceBssid + "   hostAddress   " + hostAddress);
                setUpResult.onSetUpSuccess(hostAddress);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTask != null) {
            mTask.cancelEsptouch();
        }
    }


    interface ISetUpResult {
        void onSetUpFailed();

        void onSetUpSuccess(String hostAddress);
    }
}
