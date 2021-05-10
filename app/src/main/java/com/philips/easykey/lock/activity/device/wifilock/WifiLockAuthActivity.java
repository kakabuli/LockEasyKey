package com.philips.easykey.lock.activity.device.wifilock;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import androidx.annotation.Nullable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.bean.HomeShowBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.wifilock.WifiLockAuthPresenter;
import com.philips.easykey.lock.mvp.view.wifilock.IWifiLockAuthView;
import com.philips.easykey.lock.publiclibrary.bean.ProductInfo;
import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.BatteryView;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WifiLockAuthActivity extends BaseActivity<IWifiLockAuthView, WifiLockAuthPresenter<IWifiLockAuthView>>
        implements IWifiLockAuthView, View.OnClickListener {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_bluetooth_name)
    TextView tvBluetoothName;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_open_clock)
    TextView tvOpenClock;
    @BindView(R.id.iv_power)
    BatteryView ivPower;
    @BindView(R.id.tv_power)
    TextView tvPower;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.rl_device_information)
    RelativeLayout rlDeviceInformation;
    @BindView(R.id.iv_lock_icon)
    ImageView ivLockIcon;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    private static final int TO_MORE_REQUEST_CODE = 101;
    private Handler handler = new Handler();
    private WifiLockInfo wifiLockInfo;
    private String wifiSn;
    private List<ProductInfo> productList = new ArrayList<>();
    private RequestOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_lock_authorization);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ButterKnife.bind(this);
        productList = MyApplication.getInstance().getProductInfos();

        Intent intent = getIntent();
        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        changeLockIcon();
        ivBack.setOnClickListener(this);
        tvOpenClock.setVisibility(View.INVISIBLE);
        ivDelete.setOnClickListener(this);
        showLockType();
        initListener();
        LogUtils.d("授权界面");
        rlDeviceInformation.setVisibility(View.VISIBLE);
        try{
            dealWithPower(wifiLockInfo.getPower(), wifiLockInfo.getUpdateTime());
        }catch (Exception e){

        }
        //动态设置状态栏高度
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(titleBar.getLayoutParams());
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        titleBar.setLayoutParams(lp);
    }

    private void showLockType() {
        String lockType = "";
        if(wifiLockInfo != null){

            lockType = wifiLockInfo.getProductModel();
        }
        if (!TextUtils.isEmpty(lockType)) {
            tvType.setText(StringUtil.getSubstringFive(lockType));

            //适配服务器上的产品型号，适配不上则显示锁本地的研发型号
            for (ProductInfo productInfo:productList) {

                try {
                    if (productInfo.getSnHead().equals(wifiSn.substring(0,3))) {
                        tvType.setText(getString(R.string.bluetooth_type) + productInfo.getProductModel());
                        return;
                    }
                } catch (Exception e) {
                    com.philips.easykey.lock.utils.LogUtils.d("--kaadas--:" + e.getMessage());
                }
            }
        }
    }

    private void changeLockIcon() {
        String model = "";
        String WifiSN = wifiLockInfo.getWifiSN();

        if(wifiLockInfo != null){
            model = wifiLockInfo.getProductModel();
        }

        if (model != null && WifiSN != null) {
            //本地图片有对应的产品则不获取缓存的产品型号图片，缓存没有则选择尝试下载
//            if (BleLockUtils.getAuthorizationImageByModel(model) == R.mipmap.bluetooth_authorization_lock_default) {
                options = new RequestOptions()
                        .placeholder(R.mipmap.bluetooth_authorization_lock_default)      //加载成功之前占位图
                        .error(R.mipmap.bluetooth_authorization_lock_default)      //加载错误之后的错误图
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
                        .dontAnimate()                                  //直接显示图片
                        .fitCenter();//指定图片的缩放类型为fitCenter （是一种“中心匹配”的方式裁剪方式，它裁剪出来的图片长宽都会小于等于ImageView的大小，这样一来。图片会完整地显示出来，但是ImageView可能并没有被填充满）
//                    .centerCrop();//指定图片的缩放类型为centerCrop （是一种“去除多余”的裁剪方式，它会把ImageView边界以外的部分裁剪掉。这样一来ImageView会被填充满，但是这张图片可能不会完整地显示出来(ps:因为超出部分都被裁剪掉了）

                for (ProductInfo productInfo : productList) {
                    try {
//                        if (productInfo.getDevelopmentModel().contentEquals(model)) {
                        if (productInfo.getSnHead().equals(WifiSN.substring(0,3))) {

                            //匹配型号获取下载地址
                            Glide.with(this).load(productInfo.getAuthUrl()).apply(options).into(ivLockIcon);
                            return;
                        }
                    } catch (Exception e) {
                       LogUtils.d("--kaadas--:" + e.getMessage());
                    }
                }
//            }
        }
        ivLockIcon.setImageResource(BleLockUtils.getAuthorizationImageByModel(model));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected WifiLockAuthPresenter<IWifiLockAuthView> createPresent() {
        return new WifiLockAuthPresenter<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showData();
        tvBluetoothName.setText(wifiLockInfo.getLockNickname());
    }

    @SuppressLint("SetTextI18n")
    private void showData() {
    }


    private void initListener() {
        rlDeviceInformation.setOnClickListener(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_MORE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getBooleanExtra(KeyConstants.IS_DELETE, false)) {
                finish();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_device_information:
                //先拿本地的数据    然后拿读取到的数据
                Intent intent;
                if(MyApplication.getInstance().getWifiVideoLockTypeBySn(wifiSn) == HomeShowBean.TYPE_WIFI_VIDEO_LOCK){
                    intent = new Intent(this, WifiLockDeviceInfoActivity.class);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    startActivity(intent);
                }else{
                    intent = new Intent(this, WifiLockAuthDeviceInfoActivity.class);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    startActivity(intent);
                }

                break;
            case R.id.iv_delete:
                AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.device_delete_dialog_head), getString(R.string.device_delete_lock_dialog_content), getString(R.string.cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {
                    }

                    @Override
                    public void right() {
                        mPresenter.deleteDevice(wifiSn);
                        showLoading(getString(R.string.is_deleting));
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(String toString) {
                    }
                });
                break;
        }
    }


    private void dealWithPower(int power, long updateTime) {
        //电量：80%
        if (power > 100) {
            power = 100;
        }
        if (power < 0) {
            power = 0;
        }
        String lockPower = power + "%";
        tvPower.setText(lockPower);
        if (ivPower != null) {
            ivPower.setPower(power);
            if (power <= 20) {
                ivPower.setColor(R.color.cFF3B30);
                ivPower.setBorderColor(R.color.white);
            } else {
                ivPower.setColor(R.color.c25F290);
                ivPower.setBorderColor(R.color.white);
            }
        }

        long readDeviceInfoTime = updateTime * 1000;
        if (readDeviceInfoTime != -1) {
            if ((System.currentTimeMillis() - readDeviceInfoTime) < 60 * 60 * 1000) {
                //小于一小时
                tvDate.setText(getString(R.string.device_detail_power_date));
            } else if ((System.currentTimeMillis() - readDeviceInfoTime) < 24 * 60 * 60 * 1000) {
                //小于一天
                tvDate.setText(getString(R.string.today) + " " + DateUtils.currentLong2HourMin(readDeviceInfoTime));
            } else if ((System.currentTimeMillis() - readDeviceInfoTime) < 2 * 24 * 60 * 60 * 1000) {
                tvDate.setText(getString(R.string.yesterday) + " " + DateUtils.currentLong2HourMin(readDeviceInfoTime));
            } else {
                tvDate.setText(DateUtils.formatYearMonthDay(readDeviceInfoTime));
            }
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onDeleteDeviceSuccess() {
        hiddenLoading();
        ToastUtils.showLong(R.string.delete_success);
        finish();
    }

    @Override
    public void onDeleteDeviceFailed(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showLong(R.string.delete_fialed);
    }

    @Override
    public void onDeleteDeviceFailedServer(BaseResult result) {
        hiddenLoading();
        ToastUtils.showLong(R.string.delete_fialed);
    }

    //获取状态栏高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
