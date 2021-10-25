package com.philips.easykey.lock.activity.device.wifilock.password;

import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseAddToApplicationActivity;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.MyLog;
import com.philips.easykey.lock.utils.Rsa;
import com.philips.easykey.lock.utils.SharedUtil;
import com.blankj.utilcode.util.ToastUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PhilipsWifiLockPasswordShareActivity extends BaseAddToApplicationActivity {

    TextView headTitle;
    TextView tvNotice;
    TextView tvPassword1;
    TextView tvPassword2;
    TextView tvPassword3;
    TextView tvPassword4;
    TextView tvPassword5;
    TextView tvPassword6;
    TextView tvPassword7;
    TextView tvTime;

    private String password;
    private WifiLockInfo wifiLockInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_wifi_lock_password_share);

        headTitle = findViewById(R.id.head_title);
        tvNotice = findViewById(R.id.tv_notice);
        tvPassword1 = findViewById(R.id.tv_password_1);
        tvPassword2 = findViewById(R.id.tv_password_2);
        tvPassword3 = findViewById(R.id.tv_password_3);
        tvPassword4 = findViewById(R.id.tv_password_4);
        tvPassword5 = findViewById(R.id.tv_password_5);
        tvPassword6 = findViewById(R.id.tv_password_6);
        tvPassword7 = findViewById(R.id.tv_password_7);
        tvTime = findViewById(R.id.tv_time);

        findViewById(R.id.back).setOnClickListener(v -> finish());
        findViewById(R.id.tv_short_message).setOnClickListener(v -> {
            String message = String.format(getString(R.string.share_content),password,tvNotice.getText().toString().trim(),getString(R.string.app_name));
            SharedUtil.getInstance().sendShortMessage(message, this);
        });
        findViewById(R.id.tv_wei_xin).setOnClickListener(v -> {
            String message = String.format(getString(R.string.share_content), password, tvNotice.getText().toString().trim(),getString(R.string.app_name));
            if (SharedUtil.isWeixinAvilible(this)) {
                SharedUtil.getInstance().sendWeiXin(message);
            } else {
                ToastUtils.showShort(R.string.telephone_not_install_wechat);
            }
        });
        findViewById(R.id.tv_copy).setOnClickListener(v -> {
            String message = String.format(getString(R.string.share_content), password, tvNotice.getText().toString().trim(),getString(R.string.app_name));
            SharedUtil.getInstance().copyTextToSystem(this, message);
        });

        String wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);

        tvTime.setText(" " + DateUtils.getStrFromMillisecond2(System.currentTimeMillis()));
        initPassword();
    }

    public static void main(String[] args) {

    }

    public void initPassword() {
        password = getPassword();
        //TODO:用Html或者span来做，字样间距
        String temp = "";
        if (!TextUtils.isEmpty(password) && password.length() > 5) {
            tvPassword1.setText(password.substring(0,1) + "");
            tvPassword2.setText(password.substring(1,2) + "");
            tvPassword3.setText(password.substring(2,3) + "");
            tvPassword4.setText(password.substring(3,4) + "");
            tvPassword5.setText(password.substring(4,5) + "");
            tvPassword6.setText(password.substring(5,6) + "");
            tvPassword7.setText("#");
        }
        MyLog.getInstance().save("--kaadas调试--UI显示的临时密码==" + temp);

    }

    private String getPassword() {
        if (wifiLockInfo != null) {
            if (!TextUtils.isEmpty(wifiLockInfo.getWifiSN())) {

                String wifiSN = wifiLockInfo.getWifiSN();
                String randomCode = wifiLockInfo.getRandomCode();
                String time = (System.currentTimeMillis() / 1000 / 60 / 5) + "";
                LogUtils.d("--kaadas--wifiSN-  " + wifiSN);
                MyLog.getInstance().save("--kaadas调试--wifiSN  " + wifiSN);
                MyLog.getInstance().save("--kaadas调试--randomCode  " + randomCode);
                MyLog.getInstance().save("--kaadas调试--System.currentTimeMillis()  " + System.currentTimeMillis());

                String content = wifiSN + randomCode + time;
                LogUtils.d("--kaadas--服务器获取的数据是  " + randomCode);

                LogUtils.d("--kaadas--本地数据是  " + content);
                byte[] data = content.toUpperCase().getBytes();
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update(data);
                    byte[] digest = messageDigest.digest();
                    byte[] temp = new byte[4];
                    System.arraycopy(digest, 0, temp, 0, 4);
                    long l = Rsa.getInt(temp);
                    String text = (l % 1000000) + "";
                    LogUtils.d("--kaadas--转换之后的数据是     " + l + "    " + Rsa.bytes2Int(temp));
                    int offSet = (6 - text.length());
                    for (int i = 0; i < offSet; i++) {
                        text = "0" + text;
                    }
                    System.out.println("--kaadas--   testSha256 数据是   " + Rsa.bytesToHexString(messageDigest.digest()));
                    return text;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private String getVideoPassword() {
        if (wifiLockInfo != null) {
            if (!TextUtils.isEmpty(wifiLockInfo.getWifiSN())) {

                String wifiSN = wifiLockInfo.getWifiSN();
                String randomCode = wifiLockInfo.getRandomCode();
                randomCode = randomCode.substring(0,64) + randomCode.substring(randomCode.length() - 2);;
                String time = (System.currentTimeMillis() / 1000 / 60 / 5) + "";
                LogUtils.d("--kaadas--wifiSN-  " + wifiSN);
                MyLog.getInstance().save("--kaadas调试--wifiSN  " + wifiSN);
                MyLog.getInstance().save("--kaadas调试--randomCode  " + randomCode);
                MyLog.getInstance().save("--kaadas调试--System.currentTimeMillis()  " + System.currentTimeMillis());

                String content = wifiSN + randomCode + time;
                LogUtils.d("--kaadas--服务器获取的数据是  " + randomCode);

                LogUtils.d("--kaadas--本地数据是  " + content);
                byte[] data = content.toUpperCase().getBytes();
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    messageDigest.update(data);
                    byte[] digest = messageDigest.digest();
                    byte[] temp = new byte[4];
                    System.arraycopy(digest, 0, temp, 0, 4);
                    long l = Rsa.getInt(temp);
                    String text = (l % 1000000) + "";
                    LogUtils.d("--kaadas--转换之后的数据是     " + l + "    " + Rsa.bytes2Int(temp));
                    int offSet = (6 - text.length());
                    for (int i = 0; i < offSet; i++) {
                        text = "0" + text;
                    }
                    System.out.println("--kaadas--   testSha256 数据是   " + Rsa.bytesToHexString(messageDigest.digest()));
                    return text;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
