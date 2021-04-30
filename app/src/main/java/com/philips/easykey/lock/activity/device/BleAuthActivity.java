package com.philips.easykey.lock.activity.device;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.activity.device.bluetooth.BleDeviceInfoActivity;
import com.philips.easykey.lock.activity.device.oldbluetooth.OldDeviceInfoActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleActivity;
import com.philips.easykey.lock.mvp.presenter.ble.OldAndAuthBleDetailPresenter;
import com.philips.easykey.lock.mvp.view.IOldBleDetailView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.ProductInfo;
import com.philips.easykey.lock.publiclibrary.ble.BleProtocolFailedException;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
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
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 2019/4/10
 */
public class BleAuthActivity extends BaseBleActivity<IOldBleDetailView, OldAndAuthBleDetailPresenter<IOldBleDetailView>> implements IOldBleDetailView, View.OnClickListener {
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
    @BindView(R.id.ll_power)
    LinearLayout llPower;
    @BindView(R.id.rl_device_information)
    RelativeLayout rlDeviceInformation;
    @BindView(R.id.iv_lock_icon)
    ImageView ivLockIcon;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    private BleLockInfo bleLockInfo;
    private static final int TO_MORE_REQUEST_CODE = 101;
    private Runnable lockRunnable;
    private boolean isOpening = false;
    private Handler handler = new Handler();
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
        changeLockIcon(intent);
        bleLockInfo = mPresenter.getBleLockInfo();
        ivBack.setOnClickListener(this);
        tvOpenClock.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        showLockType();
        initListener();
        LogUtils.d("授权界面");
        lockRunnable = new Runnable() {
            @Override
            public void run() {
                LogUtils.d(" 首页锁状态  反锁状态   " + bleLockInfo.getBackLock() + "    安全模式    " + bleLockInfo.getSafeMode() + "   布防模式   " + bleLockInfo.getArmMode());
                isOpening = false;
                if (bleLockInfo.isAuth()) {
                    changLockStatus(0);
                    onElectricUpdata(bleLockInfo.getBattery());
                    if (bleLockInfo.getSafeMode() == 1) {//安全模式
                        changLockStatus(6);
                    }
                    if (bleLockInfo.getBackLock() == 0) {  //等于0时是反锁状态
                        changLockStatus(2);
                    }
                    if (bleLockInfo.getArmMode() == 1) {//布防模式
                        changLockStatus(7);
                    }
                }
            }
        };


        if (mPresenter.getBleVersion() == 2 || mPresenter.getBleVersion() == 3 ||
                (bleLockInfo != null && bleLockInfo.getServerLockInfo() != null && !TextUtils.isEmpty(bleLockInfo.getServerLockInfo().getBleVersion()) &&
                        "2".equals(bleLockInfo.getServerLockInfo().getBleVersion()))
                ||
                (bleLockInfo != null && bleLockInfo.getServerLockInfo() != null && !TextUtils.isEmpty(bleLockInfo.getServerLockInfo().getBleVersion()) &&
                        "3".equals(bleLockInfo.getServerLockInfo().getBleVersion()))
                ) {
            //可以查设备信息
            rlDeviceInformation.setVisibility(View.VISIBLE);
        } else {
            //不可以查设备信息
            rlDeviceInformation.setVisibility(View.GONE);
        }
        lockRunnable.run();
        //动态设置状态栏高度
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(titleBar.getLayoutParams());
        lp.setMargins(0, getStatusBarHeight(), 0, 0);
        titleBar.setLayoutParams(lp);
    }

    private void showLockType() {
        String lockType = bleLockInfo.getServerLockInfo().getModel();
        if (!TextUtils.isEmpty(lockType)) {
            tvType.setText(StringUtil.getSubstringFive(lockType));
            //适配服务器上的产品型号，适配不上则显示锁本地的研发型号
            for (ProductInfo productInfo:productList) {
                if (productInfo.getDevelopmentModel().contentEquals(lockType)){
                    tvType.setText(productInfo.getProductModel());
                }
            }
        }
    }

    private void changeLockIcon(Intent intent) {
        String model = intent.getStringExtra(KeyConstants.DEVICE_TYPE);
        String deviceSN = intent.getStringExtra(KeyConstants.BLE_DEVICE_SN);
        if (model != null && deviceSN != null) {
        //本地图片有对应的产品则不获取缓存的产品型号图片，缓存没有则选择尝试下载
//        if (BleLockUtils.getAuthorizationImageByModel(model) == R.mipmap.bluetooth_authorization_lock_default){
            options = new RequestOptions()
                    .placeholder(R.mipmap.bluetooth_authorization_lock_default)      //加载成功之前占位图
                    .error(R.mipmap.bluetooth_authorization_lock_default)      //加载错误之后的错误图
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
                    .dontAnimate()                                      //直接显示图片
                .fitCenter();//指定图片的缩放类型为fitCenter （是一种“中心匹配”的方式裁剪方式，它裁剪出来的图片长宽都会小于等于ImageView的大小，这样一来。图片会完整地显示出来，但是ImageView可能并没有被填充满）
//                    .centerCrop();//指定图片的缩放类型为centerCrop （是一种“去除多余”的裁剪方式，它会把ImageView边界以外的部分裁剪掉。这样一来ImageView会被填充满，但是这张图片可能不会完整地显示出来(ps:因为超出部分都被裁剪掉了）

            for (ProductInfo productInfo:productList) {
//                if (productInfo.getDevelopmentModel().contentEquals(model)){
                if (productInfo.getSnHead().equals(deviceSN.substring(0,3))) {

                    //匹配型号获取下载地址
                    Glide.with(this).load(productInfo.getAuthUrl()).apply(options).into(ivLockIcon);
                    return;
                }
            }
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
    protected OldAndAuthBleDetailPresenter<IOldBleDetailView> createPresent() {
        return new OldAndAuthBleDetailPresenter<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showData();
        if (mPresenter.getBleLockInfo() != null && mPresenter.getBleLockInfo().getServerLockInfo() != null && mPresenter.getBleLockInfo().getServerLockInfo().getLockNickName() != null) {
            LogUtils.d("设备昵称是   " + mPresenter.getBleLockInfo().getServerLockInfo().getLockNickName());
            tvBluetoothName.setText(mPresenter.getBleLockInfo().getServerLockInfo().getLockNickName());
        }
    }

    @SuppressLint("SetTextI18n")
    private void showData() {
        //默认为手动模式
        if (mPresenter.isAuth(bleLockInfo, true)) {
            authResult(true);
            if (bleLockInfo.getAutoMode() == 0) {
            } else if (bleLockInfo.getAutoMode() == 1) {
            }
            if (bleLockInfo.getBattery() != -1) {
                dealWithPower(bleLockInfo.getBattery());
            }
            mPresenter.authSuccess();
        }
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
    public void onDeviceStateChange(boolean isConnected) {
        if (isConnected) {
        } else {
            changLockStatus(1);
        }
    }

    @Override
    public void onStartSearchDevice() {
    }

    @Override
    public void onSearchDeviceFailed(Throwable throwable) {
        changLockStatus(1);
    }

    @Override
    public void onNeedRebind(int errorCode) {
    }

    @Override
    public void authResult(boolean isSuccess) {
        if (isSuccess) {
            lockRunnable.run();
        } else {
            changLockStatus(1);
        }
    }


    @Override
    public void onBleOpenStateChange(boolean isOpen) {
        if (!isOpen) {   //没有打开
        }
    }


    @Override
    public void onElectricUpdata(Integer power) {
        if (bleLockInfo.getAutoMode() == 0) {
        } else if (bleLockInfo.getAutoMode() == 1) {
        }
        if (bleLockInfo.getBattery() != -1) {
            dealWithPower(bleLockInfo.getBattery());
            //删除成功
            setBatteryResult();

        }
    }

    private void setBatteryResult() {
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra(KeyConstants.BLE_INTO, bleLockInfo);
        //设置返回数据
        this.setResult(RESULT_OK, intent);
    }


    @Override
    public void onElectricUpdataFailed(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void onBleVersionUpdate(int version) {
        if (version == 1) {
            rlDeviceInformation.setVisibility(View.GONE);
        } else {
            rlDeviceInformation.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDeleteDeviceSuccess() {
        ToastUtils.showLong(R.string.delete_success);
        hiddenLoading();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeleteDeviceFailed(Throwable throwable) {
        LogUtils.d("删除失败   " + throwable.getMessage());
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
        hiddenLoading();
    }

    @Override
    public void onDeleteDeviceFailedServer(BaseResult result) {
        LogUtils.d("删除失败   " + result.toString());
        String httpErrorCode = HttpUtils.httpErrorCode(this, result.getCode());
        ToastUtils.showLong(httpErrorCode);
        hiddenLoading();
    }

    @Override
    public void onDeviceInfoLoaded() {
        lockRunnable.run();
    }

    @Override
    public void notAdminMustHaveNet() {
        ToastUtils.showLong(R.string.not_admin_must_have_net);
    }

    @Override
    public void inputPwd() {
        View mView = LayoutInflater.from(this).inflate(R.layout.have_edit_dialog, null);
        TextView tvTitle = mView.findViewById(R.id.tv_title);
        EditText editText = mView.findViewById(R.id.et_name);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView tv_cancel = mView.findViewById(R.id.tv_left);
        TextView tv_query = mView.findViewById(R.id.tv_right);
        AlertDialog alertDialog = AlertDialogUtil.getInstance().common(this, mView);
        tvTitle.setText(getString(R.string.input_open_lock_password));
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        tv_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if (!StringUtil.randomJudge(name)) {
                    ToastUtils.showShort(R.string.random_verify_error);
                    return;
                }
                mPresenter.realOpenLock(name, false);
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void authFailed(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void authServerFailed(BaseResult baseResult) {

    }

    @Override
    public void isOpeningLock() {
        isOpening = true;
        changLockStatus(3);
    }

    @Override
    public void openLockSuccess() {
        changLockStatus(4);
        handler.removeCallbacks(lockRunnable);
        handler.postDelayed(lockRunnable, 15 * 1000);  //十秒后退出开门状态
    }

    @Override
    public void onLockLock() {
        handler.removeCallbacks(lockRunnable);
        lockRunnable.run();
    }

    @Override
    public void openLockFailed(Throwable throwable) {
        changLockStatus(5);
        if (throwable instanceof TimeoutException) {
            ToastUtils.showShort(getString(R.string.open_lock_failed));
        } else if (throwable instanceof BleProtocolFailedException) {
            BleProtocolFailedException bleProtocolFailedException = (BleProtocolFailedException) throwable;
            ToastUtils.showShort(getString(R.string.open_lock_failed));
        } else {
            ToastUtils.showShort(getString(R.string.open_lock_failed));
        }
        handler.postDelayed(lockRunnable, 3000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                setBatteryResult();
                finish();
                break;
            case R.id.tv_open_clock:
                //开锁
                if (isOpening) {
                    LogUtils.d("长按  但是当前正在开锁状态   ");
                    return;
                }
                if (mPresenter.isAuth(bleLockInfo, true)) {
                    if (bleLockInfo.getBackLock() == 0 || bleLockInfo.getSafeMode() == 1) {  //反锁状态下或者安全模式下  长按不操作
                        if (bleLockInfo.getSafeMode() == 1) {
                            ToastUtils.showLong(R.string.safe_mode_can_not_open);
                        } else if (bleLockInfo.getBackLock() == 0) {
                            ToastUtils.showLong(R.string.back_lock_can_not_open);
                            changLockStatus(2);
                        }
                        return;
                    }
                    LogUtils.d("开锁   ");
                    mPresenter.openLock();
                }
                vibrate(this, 150);
                break;

            case R.id.rl_device_information:
                //先拿本地的数据    然后拿读取到的数据
                if (bleLockInfo != null && bleLockInfo.getServerLockInfo() != null &&
                        !TextUtils.isEmpty(bleLockInfo.getServerLockInfo().getBleVersion()) &&
                        "2".equals(bleLockInfo.getServerLockInfo().getBleVersion())) {
                    Intent intent = new Intent(this, OldDeviceInfoActivity.class);
                    startActivity(intent);
                } else if (bleLockInfo != null && bleLockInfo.getServerLockInfo() != null &&
                        !TextUtils.isEmpty(bleLockInfo.getServerLockInfo().getBleVersion()) &&
                        "3".equals(bleLockInfo.getServerLockInfo().getBleVersion())) {
                    String functionSet = bleLockInfo.getServerLockInfo().getFunctionSet();
                    if (!TextUtils.isEmpty(functionSet) && Integer.parseInt(functionSet) == 0) {
                        LogUtils.d("跳转   老蓝牙  设备信息");
                        Intent intent = new Intent(this, OldDeviceInfoActivity.class);
                        startActivity(intent);
                    } else {
                        LogUtils.d("跳转   全功能  蓝牙   设备信息");
                        Intent intent = new Intent(this, BleDeviceInfoActivity.class);
                        startActivity(intent);
                    }
                } else {
                    if (mPresenter.getBleVersion() == 2) {
                        Intent intent = new Intent(this, OldDeviceInfoActivity.class);
                        startActivity(intent);
                    } else if (mPresenter.getBleVersion() == 3) {
                        String functionSet = bleLockInfo.getServerLockInfo().getFunctionSet();
                        if (!TextUtils.isEmpty(functionSet) && Integer.parseInt(functionSet) == 0) {
                            LogUtils.d("跳转   老蓝牙  设备信息");
                            Intent intent = new Intent(this, OldDeviceInfoActivity.class);
                            startActivity(intent);
                        } else {
                            LogUtils.d("跳转   全功能  蓝牙   设备信息");
                            Intent intent = new Intent(this, BleDeviceInfoActivity.class);
                            startActivity(intent);
                        }
                    }
                }
                break;
            case R.id.iv_delete:
                AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.device_delete_dialog_head), getString(R.string.device_delete_lock_dialog_content), getString(R.string.cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }

                    @Override
                    public void right() {
                        showLoading(getString(R.string.is_deleting));
                        mPresenter.deleteDevice(bleLockInfo.getServerLockInfo().getLockName());
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


    @Override
    public void onBackPressed() {
        setBatteryResult();
        super.onBackPressed();

    }

    //震动milliseconds毫秒
    public static void vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    public void changLockStatus(int state) {
        if (isFinishing()) {
            return;
        }
        switch (state) {
//            case KeyConstants.OPEN_LOCK:
            case 0:
                //可以开锁
                tvOpenClock.setEnabled(true);
                tvOpenClock.setText(R.string.click_lock);
                tvOpenClock.setTextColor(getResources().getColor(R.color.c16B8FD));
                tvOpenClock.setBackgroundResource(R.mipmap.open_lock_bj);
                break;
//            case KeyConstants.DEVICE_OFFLINE:
            case 1:
                //设备离线
                tvOpenClock.setEnabled(false);
                tvOpenClock.setText(getString(R.string.device_offline));
                tvOpenClock.setTextColor(getResources().getColor(R.color.c149EF3));
                tvOpenClock.setBackgroundResource(R.mipmap.has_been_locked_bj);
                break;
//            case KeyConstants.HAS_BEEN_LOCKED:
            case 2:
                //已反锁
                tvOpenClock.setEnabled(false);
                tvOpenClock.setText(getString(R.string.has_been_locked));
                tvOpenClock.setTextColor(getResources().getColor(R.color.c149EF3));
                tvOpenClock.setBackgroundResource(R.mipmap.has_been_locked_bj);
                break;
//            case KeyConstants.IS_LOCKING:
            case 3:
                //正在开锁中
                tvOpenClock.setEnabled(false);
                tvOpenClock.setText(getString(R.string.is_locking));
                tvOpenClock.setTextColor(getResources().getColor(R.color.white));
                tvOpenClock.setBackgroundResource(R.mipmap.is_locking_bj);
                break;
//            case KeyConstants.OPEN_LOCK_SUCCESS:
            case 4:
                //开锁成功
                tvOpenClock.setEnabled(false);
                tvOpenClock.setText(getString(R.string.open_lock_success));
                tvOpenClock.setTextColor(getResources().getColor(R.color.white));
                tvOpenClock.setBackgroundResource(R.mipmap.open_lock_success_bj);
                break;
//            case KeyConstants.OPEN_LOCK_FAILED:
            case 5:
                //开锁失败
                tvOpenClock.setEnabled(false);
                tvOpenClock.setText(getString(R.string.open_lock_failed));
                tvOpenClock.setTextColor(getResources().getColor(R.color.white));
                tvOpenClock.setBackgroundResource(R.mipmap.open_lock_fail_bj);
                break;
            case 6:  //安全模式
                tvOpenClock.setEnabled(false);
                tvOpenClock.setText(getString(R.string.safe_status));
                tvOpenClock.setTextColor(getResources().getColor(R.color.c149EF3));
                tvOpenClock.setBackgroundResource(R.mipmap.has_been_locked_bj);
                break;
            case 7: //布防模式
                tvOpenClock.setEnabled(false);
                tvOpenClock.setText(getString(R.string.bu_fang_status));
                tvOpenClock.setTextColor(getResources().getColor(R.color.c149EF3));
                tvOpenClock.setBackgroundResource(R.mipmap.has_been_locked_bj);
                break;
        }
    }

    private void dealWithPower(int power) {
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

        //todo  读取电量时间
        long readDeviceInfoTime = System.currentTimeMillis();
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
