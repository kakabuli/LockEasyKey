package com.philips.easykey.lock.fragment.home;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.home.BluetoothRecordActivity;
import com.philips.easykey.lock.adapter.HomeBeanRecordAdapter;
import com.philips.easykey.lock.bean.BluetoothItemRecordBean;
import com.philips.easykey.lock.bean.BluetoothRecordBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleFragment;
import com.philips.easykey.lock.mvp.presenter.ble.BleLockPresenter;
import com.philips.easykey.lock.mvp.view.IBleLockView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.bean.ForeverPassword;
import com.philips.easykey.lock.publiclibrary.ble.BleProtocolFailedException;
import com.philips.easykey.lock.publiclibrary.ble.BleUtil;
import com.philips.easykey.lock.publiclibrary.ble.bean.OpenLockRecord;
import com.philips.easykey.lock.publiclibrary.ble.bean.OperationLockRecord;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.GetPasswordResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.AnimationsContainer;
import com.philips.easykey.lock.utils.BleLockUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.PermissionUtil;
import com.philips.easykey.lock.utils.SPUtils;
import com.philips.easykey.lock.utils.StringUtil;
import com.philips.easykey.lock.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BleLockFragment extends BaseBleFragment<IBleLockView, BleLockPresenter<IBleLockView>>
        implements View.OnClickListener, IBleLockView {

    List<BluetoothRecordBean> showDatas = new ArrayList<>();
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.iv_external_big)
    ImageView ivExternalBig;
    @BindView(R.id.iv_external_middle)
    ImageView ivExternalMiddle;
    @BindView(R.id.iv_external_small)
    ImageView ivExternalSmall;
    @BindView(R.id.iv_inner_small)
    ImageView ivInnerSmall;
    @BindView(R.id.iv_inner_middle)
    ImageView ivInnerMiddle;
    @BindView(R.id.tv_inner)
    TextView tvInner;
    @BindView(R.id.tv_external)
    TextView tvExternal;
    @BindView(R.id.rl_device_dynamic)
    RelativeLayout rlDeviceDynamic;
    @BindView(R.id.tv_more)
    TextView tvMore;
    @BindView(R.id.create_time)
    TextView createTime;
    @BindView(R.id.rl_icon)
    RelativeLayout rlIcon;
    @BindView(R.id.tv_open_lock_times)
    TextView tvOpenLockTimes;
    @BindView(R.id.rl_has_data)
    RelativeLayout rlHasData;
    @BindView(R.id.tv_no_data)
    TextView tvNoData;
    @BindView(R.id.tv_synchronized_record)
    TextView tvSynchronizedRecord;
    @BindView(R.id.iv_device_dynamic)
    ImageView ivDeviceDynamic;
    @BindView(R.id.tv_device_status)
    TextView tvDeviceStatus;
    private BleLockInfo bleLockInfo;
    private boolean isOpening;
    private Runnable lockRunnable;
    private boolean isConnectingDevice;
    private Handler handler = new Handler();
    private HomePageFragment.ISelectChangeListener listener;
    private HomePageFragment homeFragment;
    private boolean isCurrentFragment;
    private int position;
    HomeBeanRecordAdapter bluetoothRecordAdapter;
    boolean hasData;
    private boolean isLoadingBleRecord;  //正在加载锁上数据
    private boolean isDestroy = false;
    private boolean supportOperationRecord = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDestroy = false;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onResume() {
        super.onResume();
        isDestroy = false;
        if (!isDestroy) {
            if (isCurrentFragment && homeFragment.isSelectHome) {
                if (!mPresenter.isAttach()) {
                    mPresenter.attachView(this);
                }
                LogUtils.e("setBleLockInfo    52   "+bleLockInfo.getServerLockInfo().getLockNickName());
                mPresenter.setBleLockInfo(bleLockInfo);
                getRecord();
                boolean auth = mPresenter.isAuth(bleLockInfo, true);
                if (auth) {
                    changeOpenLockStatus(8);
                    mPresenter.getDeviceInfo();
                }
                LogUtils.e("切换到当前界面 52  设备 isdestroy  " + isDestroy + auth);
                LogUtils.e(this + "   设置设备52  " + bleLockInfo.getServerLockInfo().toString());
                onChangeInitView();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.e("蓝牙界面   onDetach  " + this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        bleLockInfo = (BleLockInfo) arguments.getSerializable(KeyConstants.BLE_LOCK_INFO);
        if (bleLockInfo != null && bleLockInfo.getServerLockInfo() != null && "3".equals(bleLockInfo.getServerLockInfo().getBleVersion())) {
            supportOperationRecord = BleLockUtils.isSupportOperationRecord(Integer.parseInt(bleLockInfo.getServerLockInfo().getFunctionSet()));
        }
        LogUtils.e("蓝牙界面   onCreateView   获取到的设备是否是空  " + (bleLockInfo == null));
        position = arguments.getInt(KeyConstants.FRAGMENT_POSITION);
        lockRunnable = new Runnable() {
            @Override
            public void run() {
                LogUtils.e(" 首页锁状态  反锁状态   " + bleLockInfo.getBackLock() + "    安全模式    " + bleLockInfo.getSafeMode() + "   布防模式   " + bleLockInfo.getArmMode());
                isOpening = false;
                isLoadingBleRecord = false;
                if (bleLockInfo.isAuth()) {
                    changeOpenLockStatus(8);
                } else {
                    changeOpenLockStatus(13);
                }

                if (bleLockInfo.getSafeMode() == 1) {//安全模式
                    changeOpenLockStatus(16);
                }

                if (bleLockInfo.getBackLock() == 0) {  //等于0时是反锁状态
                    changeOpenLockStatus(6);
                }

                if (bleLockInfo.getArmMode() == 1) {//布防模式
                    changeOpenLockStatus(4);
                }
                if (bleLockInfo.isAuth()) {
                    if (bleLockInfo.isLockStatusException()) {
                        tvDeviceStatus.setText(getString(R.string.no_normal));
                    } else {
                        tvDeviceStatus.setText(getString(R.string.normal));
                    }
                } else {
                    tvDeviceStatus.setText(getString(R.string.not_connected));
                }
            }
        };
        View view = inflater.inflate(R.layout.fragment_ble_lock_layout, null);
        LogUtils.e("蓝牙界面   onCreateView  " + this);
        LogUtils.e("蓝牙界面   onCreateView  " + bleLockInfo.getServerLockInfo().toString());
        ButterKnife.bind(this, view);
        initRecycleView();
        rlDeviceDynamic.setOnClickListener(this);
        tvMore.setOnClickListener(this);
        tvSynchronizedRecord.setOnClickListener(this);
        ivDeviceDynamic.setOnClickListener(this);
        initView();
        return view;
    }


    //震动milliseconds毫秒
    public static void vibrate(final Activity activity, long milliseconds) {
        Vibrator vib = (Vibrator) activity.getSystemService(Service.VIBRATOR_SERVICE);
        vib.vibrate(milliseconds);
    }

    private void initView() {
        tvDeviceStatus.setText(getString(R.string.not_connected));
        long createTime = bleLockInfo.getServerLockInfo().getCreateTime();
        long serverTime = (long) SPUtils.get(KeyConstants.SERVER_CURRENT_TIME, Long.parseLong("0"));
        //设置守护时间
        long time = (serverTime / 1000) - createTime;
        long day = 0;
        if (time > 0) {
            day = ((serverTime / 1000) - createTime) / (60 * 24 * 60);
        }
        this.createTime.setText(day + "");
        LogUtils.e("设备  HomeLockFragment  " + this);
        homeFragment = (HomePageFragment) getParentFragment();
        rlIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (isOpening) {
                    LogUtils.e("长按  但是当前正在开锁状态   ");
                    return false;
                }
                if (mPresenter.getBleLockInfo() == null && bleLockInfo != null && !isDestroy) {
                    LogUtils.e("setBleLockInfo    11   " +bleLockInfo.getServerLockInfo().getLockNickName());
                    mPresenter.setBleLockInfo(bleLockInfo);
                }
                if (mPresenter.isAuth(bleLockInfo, true)) {
                    if (bleLockInfo.getBackLock() == 0 || bleLockInfo.getSafeMode() == 1) {  //反锁状态下或者安全模式下  长按不操作
                        if (bleLockInfo.getSafeMode() == 1) {
                            ToastUtil.getInstance().showLong(R.string.safe_mode_can_not_open);
                        } else if (bleLockInfo.getBackLock() == 0) {
                            ToastUtil.getInstance().showLong(R.string.back_lock_can_not_open);
                        }
                        return false;
                    }
                    mPresenter.openLock();
                }
                vibrate(getActivity(), 150);
                return false;
            }
        });

        rlIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isConnectingDevice && !bleLockInfo.isAuth()) {  //如果没有正在连接设备
                    //连接设备
                    if (!mPresenter.isAttach() && !isDestroy) {
                        LogUtils.e("attachView   5");
                        mPresenter.attachView(BleLockFragment.this);
                    }
                    LogUtils.e(this + "   设置设备66  " + bleLockInfo.getServerLockInfo().toString()+bleLockInfo.getServerLockInfo().getLockNickName());
                    mPresenter.isAuth(bleLockInfo, true);
                    mPresenter.setBleLockInfo(bleLockInfo);
                    mPresenter.getAllPassword(bleLockInfo, true);
                }
            }
        });
        //切换到当前页面
        listener = new HomePageFragment.ISelectChangeListener() {
            @Override
            public void onSelectChange(boolean isSelect) {
                if (!isSelect) {
                    mPresenter.detachView();
                    isOpening = false;
                    isLoadingBleRecord = false;

                } else {
                    LogUtils.e("切换到当前界面  设备11  isdestroy  " + isDestroy + this + isCurrentFragment);
                    //切换到当前页面
                    if (isCurrentFragment && !isDestroy) {
                        if (!mPresenter.isAttach()  ) {
                            LogUtils.e("attachView   1");
                            mPresenter.attachView(BleLockFragment.this);
                        }
                        LogUtils.e("setBleLockInfo    55   "+bleLockInfo.getServerLockInfo().getLockNickName());
                        mPresenter.setBleLockInfo(bleLockInfo);
                        getRecord();
//                        lockRunnable.run();
                        getActivity().runOnUiThread(lockRunnable);
                        onChangeInitView();
                        boolean auth = mPresenter.isAuth(bleLockInfo, true);
                        LogUtils.e("切换到当前界面   设备 isdestroy  " + isDestroy + auth);
                        LogUtils.e(this + "   设置设备2  " + bleLockInfo.getServerLockInfo().toString());
                        mPresenter.getAllPassword(bleLockInfo, true);
                        isCurrentFragment = true;
                        if (auth) {
                            mPresenter.getDeviceInfo();
                        }
                    } else {

                    }
                }
            }
        };

        homeFragment.listenerSelect(listener);

        homeFragment.getPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                LogUtils.e("设备切换   当前index   " + i + "  position  " + position + "   " + homeFragment.isSelectHome + "   homeFragment.isSelectHome ");
                if (i == position) {
                    if (!isDestroy) {
                        isCurrentFragment = true;
                    }
                    if (homeFragment.isSelectHome && !isDestroy) {
                        if (!mPresenter.isAttach()) {
                            LogUtils.e("attachView   2");
                            mPresenter.attachView(BleLockFragment.this);
                        }
                        LogUtils.e("setBleLockInfo    22  "+bleLockInfo.getServerLockInfo().getLockNickName());
                        mPresenter.setBleLockInfo(bleLockInfo);
                        getRecord();
//                        lockRunnable.run();
                        getActivity().runOnUiThread(lockRunnable);
                        onChangeInitView();
                        LogUtils.e(this + "   设置设备1  " + bleLockInfo.getServerLockInfo().toString());
//                        mPresenter.isAuth(bleLockInfo, true);
                        mPresenter.getAllPassword(bleLockInfo, true);
                    }
                } else {
                    mPresenter.detachView();
                    isOpening = false;
                    isLoadingBleRecord = false;
                    isCurrentFragment = false;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        LogUtils.e("设备position " + position + "    " + homeFragment.getCurrentPosition() + "     " + homeFragment.isSelectHome);
        if (position == 0 && position == homeFragment.getCurrentPosition() && homeFragment.isSelectHome && !isDestroy && homeFragment.isResumed()) {
            if (!mPresenter.isAttach()) {
                LogUtils.e("attachView   3");
                mPresenter.attachView(BleLockFragment.this);
            }

            LogUtils.e("setBleLockInfo    33   "+bleLockInfo.getServerLockInfo().getLockNickName());
            mPresenter.setBleLockInfo(bleLockInfo);
            getRecord();
//            lockRunnable.run();
            getActivity().runOnUiThread(lockRunnable);
            onChangeInitView();
            mPresenter.isAuth(bleLockInfo, true);
            LogUtils.e(this + "  设置设备3  " + bleLockInfo.getServerLockInfo().toString());
            mPresenter.getAllPassword(bleLockInfo, true);
        } else {
            mPresenter.detachView();
            isOpening = false;
            isLoadingBleRecord = false;
        }

        if (position == 0 && position == homeFragment.getCurrentPosition()) {
            isCurrentFragment = true;
        } else {
            isCurrentFragment = false;
        }
    }

    @Override
    protected BleLockPresenter<IBleLockView> createPresent() {
        return new BleLockPresenter<>();
    }

    private void initRecycleView() {
        bluetoothRecordAdapter = new HomeBeanRecordAdapter(showDatas);
        recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview.setAdapter(bluetoothRecordAdapter);
        bluetoothRecordAdapter.setOnDataMoreListener(new HomeBeanRecordAdapter.onDataMoreListener() {
            @Override
            public void onClickMore() {
                Intent intent = new Intent(getActivity(), BluetoothRecordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeFragment.removeListener(listener);
        listener = null;
        LogUtils.e("蓝牙界面   onDestroyView  " + this);
        isDestroy = true;
    }


    public void changePage() {
        if (!isAdded()) {
            return;
        }
        if (hasData) {
            rlHasData.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
            rlHasData.setEnabled(false);
        } else {
            rlHasData.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
            rlHasData.setEnabled(true);
        }
    }

    public void changeOpenLockStatus(int status) {
        stopOpenLockAnimator();
        stopCloseLockAnimator();
        LogUtils.e("状态改变   " + status);
        if (!isAdded()) {
            return;
        }
        if (bleLockInfo.isAuth()) {
            if (bleLockInfo.isLockStatusException()) {
                tvDeviceStatus.setText(getString(R.string.no_normal));
            } else {
                tvDeviceStatus.setText(getString(R.string.normal));
            }
            if (bleLockInfo.getOpenNumbers() > 0) {
                tvOpenLockTimes.setText("" + bleLockInfo.getOpenNumbers());
            }
        } else {
            tvDeviceStatus.setText(getString(R.string.not_connected));
        }

        switch (status) {
            case 1:
                //手机蓝牙未打开

                break;
            case 2:
                //搜索门锁蓝牙....
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_no_connect_big_middle_icon);
                ivExternalMiddle.setVisibility(View.GONE);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_connecting_inner_middle_icon);
                ivInnerSmall.setVisibility(View.GONE);
                tvInner.setVisibility(View.VISIBLE);
                tvInner.setText(getString(R.string.bluetooth_connecting));
                tvInner.setTextColor(getResources().getColor(R.color.c15A6F5));
                tvExternal.setVisibility(View.INVISIBLE);
                break;
            case 4:
                //“已启动布防，长按开锁“
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_bu_fang_big_middle_icon);
                ivExternalMiddle.setVisibility(View.GONE);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_lock_safe_inner_midder_icon);
                ivInnerSmall.setVisibility(View.VISIBLE);
                ivInnerSmall.setImageResource(R.mipmap.bluetooth_lock_bu_fang_inner_small_icon);
                tvInner.setVisibility(View.VISIBLE);
                tvInner.setText(getString(R.string.long_press_open_lock));
                tvInner.setTextColor(getResources().getColor(R.color.white));
                tvExternal.setVisibility(View.VISIBLE);
                tvExternal.setTextColor(getResources().getColor(R.color.cC6F5FF));
                tvExternal.setText(getString(R.string.bu_fang_status));
                break;
            case 6:
                //“已反锁，请门内开锁”
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_double_lock_big_middle_icon);
                ivExternalMiddle.setVisibility(View.GONE);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_lock_safe_inner_midder_icon);
                ivInnerSmall.setVisibility(View.VISIBLE);
                ivInnerSmall.setImageResource(R.mipmap.bluetooth_lock_bu_fang_inner_small_icon);
                tvInner.setVisibility(View.GONE);
                tvExternal.setVisibility(View.VISIBLE);
                tvExternal.setTextColor(getResources().getColor(R.color.cC6F5FF));
                tvExternal.setText(getString(R.string.double_lock_status));
                break;
            case 8:
                //“长按开锁”（表示关闭状态）
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_lock_close_big_middle_icon);
                ivExternalMiddle.setVisibility(View.GONE);
//                ivExternalMiddle.setImageResource();
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_lock_safe_inner_midder_icon);
                ivInnerSmall.setVisibility(View.VISIBLE);
                ivInnerSmall.setImageResource(R.mipmap.bluetooth_lock_bu_fang_inner_small_icon);
                tvInner.setVisibility(View.VISIBLE);
                tvInner.setText(R.string.long_press_open_lock);
                tvInner.setTextColor(getResources().getColor(R.color.cF7FDFD));
                tvExternal.setVisibility(View.VISIBLE);
                tvExternal.setTextColor(getResources().getColor(R.color.cC6F5FF));
                tvExternal.setText(getString(R.string.bluetooth_close_status));
                break;
            case 9:
                //”开锁中....“
//                openLockAnimator();
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_no_connect_big_icon);
                ivExternalMiddle.setVisibility(View.VISIBLE);
                ivExternalMiddle.setImageResource(R.mipmap.bluetooth_open_lock_middle_icon);
                ivExternalSmall.setVisibility(View.VISIBLE);
                ivExternalSmall.setImageResource(R.mipmap.bluetooth_open_lock_small_icon);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_lock_safe_inner_midder_icon);
                ivInnerSmall.setVisibility(View.GONE);
                tvInner.setVisibility(View.GONE);
                tvExternal.setVisibility(View.VISIBLE);
                tvExternal.setTextColor(getResources().getColor(R.color.cC6F5FF));
                tvExternal.setText(getString(R.string.is_lock));
                break;
            case 10:
                //“锁已打开”
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_no_connect_big_icon);
                ivExternalMiddle.setVisibility(View.VISIBLE);
                ivExternalMiddle.setImageResource(R.mipmap.open_lock_middle_78);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_open_lock_success_niner_middle_icon);
                ivInnerSmall.setVisibility(View.GONE);
                tvInner.setVisibility(View.GONE);
                tvExternal.setVisibility(View.VISIBLE);
                tvExternal.setTextColor(getResources().getColor(R.color.cC6F5FF));
                tvExternal.setText(getString(R.string.open_lock_success));
                break;
            case 11:
                //蓝牙连接成功
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_no_connect_big_middle_icon);
                ivExternalMiddle.setVisibility(View.GONE);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_connect_success);
                ivInnerSmall.setVisibility(View.GONE);
                tvInner.setVisibility(View.VISIBLE);
                tvInner.setText(getString(R.string.bluetooth_connect_success));
                tvInner.setTextColor(getResources().getColor(R.color.c15A6F5));
                tvExternal.setVisibility(View.INVISIBLE);
                break;
            case 12:
                //蓝牙链接失败
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_no_connect_big_middle_icon);
                ivExternalMiddle.setVisibility(View.GONE);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_connect_fail);
                ivInnerSmall.setVisibility(View.GONE);
//                ivInnerSmall.setImageResource();
                tvInner.setVisibility(View.VISIBLE);
                tvInner.setText(getString(R.string.bluetooth_connect_fail));
                tvInner.setTextColor(getResources().getColor(R.color.c15A6F5));
                tvExternal.setVisibility(View.INVISIBLE);
                break;
            case 13:
                //手机蓝牙未链接
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_no_connect_big_middle_icon);
                ivExternalMiddle.setVisibility(View.GONE);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_noconnect_inner_middle_icon);
                ivInnerSmall.setVisibility(View.GONE);
                tvInner.setVisibility(View.VISIBLE);
                tvInner.setText(getString(R.string.bluetooth_no_connect));
                tvInner.setTextColor(getResources().getColor(R.color.c14A6F5));
                tvExternal.setVisibility(View.VISIBLE);
                tvExternal.setTextColor(getResources().getColor(R.color.white));
                tvExternal.setText(getString(R.string.equipment_out_of_range));
                break;
            case 15:
                //蓝牙锁布防被开启
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_bu_fang_big_middle_icon);
                ivExternalMiddle.setVisibility(View.GONE);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_bu_fang_inner_middle_icon);
                ivInnerSmall.setVisibility(View.VISIBLE);
                ivInnerSmall.setImageResource(R.mipmap.bluetooth_lock_bu_fang_inner_small_icon);
                tvInner.setVisibility(View.GONE);
                tvExternal.setVisibility(View.VISIBLE);
                tvExternal.setTextColor(getResources().getColor(R.color.cC6F5FF));
                tvExternal.setText(getString(R.string.bu_fang_status));
                break;
            case 16:
                //蓝牙锁安全
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_lock_security_big_icon);
                ivExternalMiddle.setVisibility(View.VISIBLE);
                ivExternalMiddle.setImageResource(R.mipmap.bluetooth_safe_external_middle_icon);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_lock_safe_inner_midder_icon);
                ivInnerSmall.setVisibility(View.VISIBLE);
                ivInnerSmall.setImageResource(R.mipmap.bluetooth_lock_bu_fang_inner_small_icon);
                tvInner.setVisibility(View.VISIBLE);
                tvInner.setText(getString(R.string.no_permission_unlock));
                tvInner.setTextColor(getResources().getColor(R.color.cFEFEFE));
                tvExternal.setVisibility(View.VISIBLE);
                tvExternal.setTextColor(getResources().getColor(R.color.cC6F5FF));
                tvExternal.setText(getString(R.string.safe_status));
                break;
            case 17:
                //蓝牙链接失败
                ivExternalBig.setVisibility(View.VISIBLE);
                ivExternalBig.setImageResource(R.mipmap.bluetooth_no_connect_big_middle_icon);
                ivExternalMiddle.setVisibility(View.GONE);
                ivExternalSmall.setVisibility(View.GONE);
                ivInnerMiddle.setVisibility(View.VISIBLE);
                ivInnerMiddle.setImageResource(R.mipmap.bluetooth_connect_fail);
                ivInnerSmall.setVisibility(View.GONE);
//                ivInnerSmall.setImageResource();
                tvInner.setVisibility(View.VISIBLE);
                tvInner.setText(getString(R.string.auth_failed));
                tvInner.setTextColor(getResources().getColor(R.color.c15A6F5));
                tvExternal.setVisibility(View.INVISIBLE);
                break;
        }
    }


    AnimationsContainer.FramesSequenceAnimation openLockBig;
    AnimationsContainer.FramesSequenceAnimation openLockMiddle;
    AnimationsContainer.FramesSequenceAnimation openLockSmall;
    AnimationsContainer.FramesSequenceAnimation closeLock;

    public void openLockAnimator() {
        LogUtils.d("davi 开始新1 " + System.currentTimeMillis());
        ivExternalBig.setVisibility(View.VISIBLE);
        //优化后的帧动画
        if (openLockBig == null) {
            openLockBig = AnimationsContainer.getInstance(R.array.open_lock_big, 26).createProgressDialogAnim(ivExternalBig);
        }
        ivExternalMiddle.setVisibility(View.VISIBLE);
        if (openLockMiddle == null) {
            openLockMiddle = AnimationsContainer.getInstance(R.array.open_lock_middle, 26).createProgressDialogAnim(ivExternalMiddle);
        }
        ivExternalSmall.setVisibility(View.GONE);
        ivInnerMiddle.setVisibility(View.VISIBLE);
        if (openLockSmall == null) {
            openLockSmall = AnimationsContainer.getInstance(R.array.open_lock_small, 26).createProgressDialogAnim(ivInnerMiddle);
        }
        ivInnerSmall.setVisibility(View.GONE);
        tvInner.setVisibility(View.GONE);
        tvExternal.setVisibility(View.VISIBLE);
        tvExternal.setTextColor(getResources().getColor(R.color.cC6F5FF));
        tvExternal.setText(getString(R.string.is_lock));


        LogUtils.d("davi 开始新2 " + System.currentTimeMillis());
        if (openLockBig != null) {
            openLockBig.start();
        }
        if (openLockMiddle != null) {
            openLockMiddle.start();
        }
        if (openLockSmall != null) {
            openLockSmall.start();
        }
    }

    /**
     * 关锁动画
     */
    public void closeLockAnimator() {
        ivExternalBig.setVisibility(View.VISIBLE);
        ivExternalBig.setImageResource(R.mipmap.bluetooth_lock_close_big_middle_icon);
        ivExternalMiddle.setVisibility(View.GONE);
        ivExternalSmall.setVisibility(View.GONE);
        ivInnerMiddle.setVisibility(View.VISIBLE);
        if (closeLock == null) {
            closeLock = AnimationsContainer.getInstance(R.array.bluetooth_lock_close, 14).createProgressDialogAnim(ivInnerMiddle);
        }
        ivInnerSmall.setVisibility(View.VISIBLE);
        ivInnerSmall.setImageResource(R.mipmap.bluetooth_lock_bu_fang_inner_small_icon);
        tvInner.setVisibility(View.VISIBLE);
        tvInner.setText(R.string.long_press_open_lock);
        tvInner.setTextColor(getResources().getColor(R.color.cF7FDFD));
        tvExternal.setVisibility(View.VISIBLE);
        tvExternal.setTextColor(getResources().getColor(R.color.cC6F5FF));
        tvExternal.setText(getString(R.string.bluetooth_close_status));
        if (closeLock != null) {
            closeLock.start();
        }
    }

    /**
     * 停止关锁动画
     */
    public void stopCloseLockAnimator() {
        if (closeLock != null) {
            closeLock.stop();
        }
    }

    /**
     * 停止开锁动画
     */
    public void stopOpenLockAnimator() {
        if (openLockBig != null) {
            openLockBig.stop();
        }
        if (openLockMiddle != null) {
            openLockMiddle.stop();
        }
        if (openLockSmall != null) {
            openLockSmall.stop();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.rl_device_dynamic:
            case R.id.iv_device_dynamic:
                bleLockInfo.setLockStatusException(false);
                LogUtils.e("跳转设备动态界面   设备当前状态是   " + bleLockInfo.isAuth() + "当前的设备  mac  " + bleLockInfo.getServerLockInfo().getMacLock());
                LogUtils.e("跳转设备动态界面   设备当前状态是   " + bleLockInfo.isAuth() + "Service设备  mac  " + MyApplication.getInstance().getBleService().getBleLockInfo().getServerLockInfo().getMacLock());
                if (bleLockInfo.isAuth()) {
                    if (bleLockInfo.isLockStatusException()) {
                        tvDeviceStatus.setText(getString(R.string.no_normal));
                    } else {
                        tvDeviceStatus.setText(getString(R.string.normal));
                    }
                } else {
                    tvDeviceStatus.setText(getString(R.string.not_connected));
                }
                intent = new Intent(getActivity(), BluetoothRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_more:
                intent = new Intent(getActivity(), BluetoothRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_synchronized_record:
                if (isLoadingBleRecord) { //如果正在加载锁上数据  不让用户再次点击
                    ToastUtil.getInstance().showShort(R.string.is_loading_lock_record);
                    return;
                }
                if (mPresenter.isAuth(bleLockInfo, true)) {
                    LogUtils.e("同步开锁记录");
                    showDatas.clear();
                    if (supportOperationRecord) {
                        mPresenter.getOperationRecordFromBle();
                    } else {
                        mPresenter.getOpenRecordFromBle();
                    }
                    if (bluetoothRecordAdapter != null) {
                        bluetoothRecordAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    /////////////////////////////////////////        回调           //////////////////////////////////////
    @Override
    public void onDeviceStateChange(boolean isConnected) {
        LogUtils.e("连接状态改变   " + isConnected);
        if (isConnected) {

        } else {
            if (!isConnectingDevice) {
                changeOpenLockStatus(12);
            }
        }
    }

    @Override
    public void onStartSearchDevice() {
        LogUtils.e("开始搜索   ");
        changeOpenLockStatus(2);
    }

    @Override
    public void onSearchDeviceFailed(Throwable throwable) {
        changeOpenLockStatus(13);
    }

    @Override
    public void onSearchDeviceSuccess() {

    }

    @Override
    public void onNeedRebind(int errorCode) {
        changeOpenLockStatus(11);
    }

    @Override
    public void authResult(boolean isSuccess) {
        if (isSuccess) {
            changeOpenLockStatus(8);
        } else {
            changeOpenLockStatus(12);
        }
    }

    @Override
    public void onElectricUpdata(int power) {

    }

    @Override
    public void onElectricUpdataFailed(Throwable throwable) {

    }

    @Override
    public void onBleOpenStateChange(boolean isOpen) {
        if (isOpen) {
            changeOpenLockStatus(2);
        } else {
            changeOpenLockStatus(1);
        }
    }

    @Override
    public void onGetOpenNumberSuccess(int number) {
//        LogUtils.e("界面获取到开锁次数    " + number);
        tvOpenLockTimes.setText(number + "");
    }

    @Override
    public void onGetOpenNumberFailed(Throwable throwable) { //获取开锁次数失败

    }

    @Override
    public void notAdminMustHaveNet() {  //不是管理员开锁必须要有网络
        ToastUtil.getInstance().showLong(R.string.not_admin_must_have_net);
    }

    @Override
    public void inputPwd() {
        View mView = LayoutInflater.from(getActivity()).inflate(R.layout.have_edit_dialog, null);
        TextView tvTitle = mView.findViewById(R.id.tv_title);
        EditText editText = mView.findViewById(R.id.et_name);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextView tv_cancel = mView.findViewById(R.id.tv_left);
        TextView tv_query = mView.findViewById(R.id.tv_right);
        AlertDialog alertDialog = AlertDialogUtil.getInstance().common(getActivity(), mView);
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
                    ToastUtil.getInstance().showShort(R.string.random_verify_error);
                    return;
                }
                alertDialog.dismiss();
                LogUtils.d("davi 准备 " + System.currentTimeMillis());
                mPresenter.realOpenLock(name, false);

            }
        });
    }

    @Override
    public void authFailed(Throwable throwable) {
        changeOpenLockStatus(17);

    }


    @Override
    public void authServerFailed(BaseResult baseResult) {

    }


    @Override
    public void isOpeningLock() {
        openLockAnimator();
        isOpening = true;
    }

    @Override
    public void openLockSuccess() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopOpenLockAnimator();
                changeOpenLockStatus(10);
                handler.removeCallbacks(lockRunnable);
                handler.postDelayed(lockRunnable, 15 * 1000);  //十秒后退出开门状态
            }
        }, 3000);
    }

    @Override
    public void onLockLock() {  //关门
        closeLockAnimator();
        LogUtils.d("davi 关锁 " + System.currentTimeMillis());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.d("davi 停止关锁 " + System.currentTimeMillis());
                stopCloseLockAnimator();
                handler.removeCallbacks(lockRunnable);
                lockRunnable.run();
            }
        }, 1000);

    }

    @Override
    public void openLockFailed(Throwable throwable) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (throwable instanceof TimeoutException) {
                    ToastUtil.getInstance().showShort(getString(R.string.open_lock_failed));
                } else if (throwable instanceof BleProtocolFailedException) {
                    BleProtocolFailedException bleProtocolFailedException = (BleProtocolFailedException) throwable;
                    ToastUtil.getInstance().showShort(getString(R.string.open_lock_failed));
                } else {
                    ToastUtil.getInstance().showShort(getString(R.string.open_lock_failed));
                }
                lockRunnable.run();
                stopOpenLockAnimator();
            }
        }, 3000);


    }

    @Override
    public void onSafeMode() {
        changeOpenLockStatus(16);
    }

    @Override
    public void onArmMode() {
        changeOpenLockStatus(4);
    }

    @Override
    public void onBackLock() {
        changeOpenLockStatus(6);
    }


    /**
     * @param type -1为锁状态改变   -2  为了更新正常异常状态
     */
    @Override
    public void onWarringUp(int type) {
        if (type == -1) {
            //为type==-1时实际是锁状态发生改变
            if (!isOpening) {
                onChangeInitView();
            }
            return;
        }
        /**
         * bit0：低电量报警
         * =0：无低电量报警
         * =1：有低电量报警
         * bit1：锁定报警
         * =0：无锁定报警
         * =1：有锁定报警
         * bit2：三次错误报警
         * =0：无三次错误报警
         * =1：有三次错误报警
         * bit3：布防状态
         * =0：撤防
         * =1：布防报警
         * bit4：温度状态
         * =0：锁具温度正常
         * =1：锁具温度异常
         * bit5：胁迫状态
         * =0：未发生胁迫事件
         * =1：发生胁迫事件
         * bit6：锁具恢复出厂设置
         * =0：未发生
         * =1：发生
         * bit7：暴力开锁
         * =0：未发生
         * =1：发生
         * bit8：钥匙遗落状态
         * =0：钥匙没有遗落在锁上
         * =1：钥匙遗落在锁上
         * bit9：安全模式
         * =0：未发生
         * =1：发生
         * Bit10：未完全上锁
         * =0：未发生
         * =1：发生
         */

        if (type == 9) {     //安全模式
            if (!isOpening) {
                onChangeInitView();
            }
        } else if (type == 6) {
            //发生恢复出厂设置
            //设置守护次数为0
            LogUtils.e("恢复出厂设置");
            tvOpenLockTimes.setText("0");
        }
        if (bleLockInfo.isConnected()) {
            if (bleLockInfo.isLockStatusException()) {
                tvDeviceStatus.setText(getString(R.string.no_normal));
            } else {
                tvDeviceStatus.setText(getString(R.string.normal));
            }
        } else {
            tvDeviceStatus.setText(getString(R.string.not_connected));
        }
    }

    @Override
    public void onLoadBleOperationRecord(List<OperationLockRecord> lockRecords) {
        hiddenLoading();
        groupOperationData(lockRecords);
        bluetoothRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadServerOperationRecord(List<OperationLockRecord> lockRecords, int page) {
        hiddenLoading();
        groupOperationData(lockRecords);
        bluetoothRecordAdapter.notifyDataSetChanged();
    }

    private String getOpenLockType(GetPasswordResult passwordResults, OpenLockRecord record) {
        String nickName = record.getUser_num() + "";
        if (passwordResults != null) {
            switch (record.getOpen_type()) {
                case BleUtil.PASSWORD:
                    List<ForeverPassword> pwdList = passwordResults.getData().getPwdList();
                    if (pwdList != null && pwdList.size() > 0) {
                        for (ForeverPassword password : pwdList) {
                            if (Integer.parseInt(password.getNum()) == Integer.parseInt(record.getUser_num())) {
                                nickName = password.getNickName();
                            }
                        }
                    }

                    break;
                case BleUtil.FINGERPRINT:
                    List<GetPasswordResult.DataBean.Fingerprint> fingerprints = passwordResults.getData().getFingerprintList();
                    if (fingerprints != null && fingerprints.size() > 0) {
                        for (GetPasswordResult.DataBean.Fingerprint password : fingerprints) {
                            if (Integer.parseInt(password.getNum()) == Integer.parseInt(record.getUser_num())) {
                                nickName = password.getNickName();
                            }
                        }
                    }

                    break;
                case BleUtil.RFID:  //卡片
                    List<GetPasswordResult.DataBean.Card> cards = passwordResults.getData().getCardList();
                    if (cards != null && cards.size() > 0) {
                        for (GetPasswordResult.DataBean.Card password : cards) {
                            if (Integer.parseInt(password.getNum()) == Integer.parseInt(record.getUser_num())) {
                                nickName = password.getNickName();
                            }
                        }
                    }
                    break;
                case BleUtil.PHONE:  //103
                    nickName = "App";
                    break;
            }
        } else {
            nickName = record.getUser_num() + "";
        }
        return nickName;
    }

    private void groupData(List<OpenLockRecord> lockRecords) {
        if (lockRecords.size() > 0) {
            hasData = true;
            changePage();
        } else {
            hasData = false;
            changePage();
        }
        showDatas.clear();
        String lastTimeHead = "";
        for (int i = 0; i < lockRecords.size(); i++) {
            if (i >= 3) {
                break;
            }
            OpenLockRecord record = lockRecords.get(i);
            String timeHead = "";
            String hourSecond = "";
            try {
                  timeHead = record.getOpen_time().substring(0, 10);
                  hourSecond = record.getOpen_time().substring(11, 16);
            }catch (Exception e){
                LogUtils.e("获取时间错误    " +  record.getOpen_time());
            }

            GetPasswordResult passwordResult = MyApplication.getInstance().getPasswordResults(bleLockInfo.getServerLockInfo().getLockName());
            String nickName = getOpenLockType(passwordResult, record);

            if (!timeHead.equals(lastTimeHead)) { //添加头
                lastTimeHead = timeHead;
                List<BluetoothItemRecordBean> itemList = new ArrayList<>();
                itemList.add(new BluetoothItemRecordBean(nickName, record.getOpen_type(), KeyConstants.BLUETOOTH_RECORD_COMMON,
                        hourSecond, false, false));
                showDatas.add(new BluetoothRecordBean(timeHead, itemList, false));
            } else {
                BluetoothRecordBean bluetoothRecordBean = showDatas.get(showDatas.size() - 1);
                List<BluetoothItemRecordBean> bluetoothItemRecordBeanList = bluetoothRecordBean.getList();
                bluetoothItemRecordBeanList.add(new BluetoothItemRecordBean(nickName, record.getOpen_type(), KeyConstants.BLUETOOTH_RECORD_COMMON,
                        hourSecond, false, false));
            }

        }

        for (int i = 0; i < showDatas.size(); i++) {
            BluetoothRecordBean bluetoothRecordBean = showDatas.get(i);
            List<BluetoothItemRecordBean> bluetoothRecordBeanList = bluetoothRecordBean.getList();

            for (int j = 0; j < bluetoothRecordBeanList.size(); j++) {
                BluetoothItemRecordBean bluetoothItemRecordBean = bluetoothRecordBeanList.get(j);

                if (j == 0) {
                    bluetoothItemRecordBean.setFirstData(true);
                }
                if (j == bluetoothRecordBeanList.size() - 1) {
                    bluetoothItemRecordBean.setLastData(true);
                }

            }
            if (i == showDatas.size() - 1) {
                bluetoothRecordBean.setLastData(true);
            }
        }
    }

    @Override
    public void onLoseRecord(List<Integer> numbers) {

    }

    @Override
    public void noData() {
        ToastUtil.getInstance().showShort(R.string.lock_no_record);
        hiddenLoading();//加载完了   设置正在加载数据
        isLoadingBleRecord = false;
    }

    @Override
    public void onLoadBleRecord(List<OpenLockRecord> lockRecords) {
        //获取到蓝牙的开锁记录
        showDatas.clear();
        hiddenLoading();
        groupData(lockRecords);
        bluetoothRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadBleRecordFinish(boolean isComplete) {
        if (isComplete) {
            ToastUtil.getInstance().showShort(R.string.sync_success);
        } else {
            ToastUtil.getInstance().showShort(R.string.get_record_failed_please_wait);
        }
        hiddenLoading();
        //加载完了   设置正在加载数据
        isLoadingBleRecord = false;
    }

    @Override
    public void onLoadServerRecord(List<OpenLockRecord> lockRecords, int page) {
        LogUtils.e("收到服务器数据  " + lockRecords.size());
//        currentPage = page + 1;
        hiddenLoading();
        groupData(lockRecords);
        LogUtils.d("davi showDatas " + showDatas.toString());
        bluetoothRecordAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadServerRecordFailed(Throwable throwable) {
        hiddenLoading();
    }

    @Override
    public void onLoadServerRecordFailedServer(BaseResult result) {
        hiddenLoading();

    }

    @Override
    public void onServerNoData() {
        hasData = false;
        changePage();

    }

    @Override
    public void noMoreData() {

    }


    @Override
    public void onUploadServerRecordSuccess() {
        LogUtils.e("记录上传成功");

    }

    @Override
    public void onUploadServerRecordFailed(Throwable throwable) {
//        ToastUtil.getInstance().showShort(HttpUtils.httpProtocolErrorCode(getActivity(), throwable));
        LogUtils.e("记录上传失败");
    }

    @Override
    public void onUploadServerRecordFailedServer(BaseResult result) {
//        ToastUtil.getInstance().showShort(HttpUtils.httpErrorCode(getActivity(), result.getCode()));
    }

    @Override
    public void startBleRecord() {
        //开始获取蓝牙记录，禁止掉下拉刷新和上拉加载更多
        isLoadingBleRecord = true;
        showLoading(getString(R.string.is_loading_lock_record));
    }

    @Override
    public void onAuthFailed(boolean isFailed) {
        if (isFailed){
            changeOpenLockStatus(17);
        }else {
            if (!isOpening){
                onChangeInitView();
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onStartConnectDevice() {
        isConnectingDevice = true;
    }

    @Override
    public void onEndConnectDevice(boolean isSuccess) {
        isConnectingDevice = false;
        if (!isSuccess) {
            changeOpenLockStatus(12);
        }
    }

    @Override
    public void onGetPasswordSuccess(GetPasswordResult result) {

    }

    @Override
    public void onGetPasswordFailedServer(BaseResult result) {

    }

    @Override
    public void onGetPasswordFailed(Throwable throwable) {

    }

    /**
     * 没有权限
     */
    @Override
    public void noPermissions() {
        PermissionUtil.getInstance().requestPermission(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, getActivity());
        ToastUtil.getInstance().showLong(R.string.please_allow_ble_permission);
        changeOpenLockStatus(12);
    }

    /**
     * 没有打开GPS 提示
     */
    @Override
    public void noOpenGps() {
        ToastUtil.getInstance().showLong(R.string.check_phone_not_open_gps_please_open);
    }

    private void onChangeInitView() {
        LogUtils.e("设备状态   1");
        if (mPresenter.isAuth(bleLockInfo, true)) {
            LogUtils.e("设备状态   2");
            changeOpenLockStatus(8);  //鉴权成功之后没有特殊状态
            onElectricUpdata(bleLockInfo.getBattery());

            if (bleLockInfo.getSafeMode() == 1) {//安全模式
                changeOpenLockStatus(16);
            }
            if (bleLockInfo.getBackLock() == 0) {  //等于0时是反锁状态
                changeOpenLockStatus(6);
            }
            if (bleLockInfo.getArmMode() == 1) {//布防模式
                changeOpenLockStatus(4);
            }
            //
            if (bleLockInfo.isConnected()) {
                if (bleLockInfo.isLockStatusException()) {
                    tvDeviceStatus.setText(getString(R.string.no_normal));
                } else {
                    tvDeviceStatus.setText(getString(R.string.normal));
                }
            } else {
                tvDeviceStatus.setText(getString(R.string.not_connected));
            }
        }
    }

    private void groupOperationData(List<OperationLockRecord> lockRecords) {
        if (lockRecords.size() > 0) {
            hasData = true;
        } else {
            hasData = false;
        }
        changePage();
        showDatas.clear();
        String lastTimeHead = "";
        for (int i = 0; i < lockRecords.size() && i < 3; i++) {
            OperationLockRecord record = lockRecords.get(i);
            //获取开锁时间的毫秒数
            String timeHead = record.getEventTime().substring(0, 10);
            String hourSecond = record.getEventTime().substring(11, 16);

            GetPasswordResult passwordResult = MyApplication.getInstance().getPasswordResults(bleLockInfo.getServerLockInfo().getLockName());
            //获取昵称

            String nickName = BleUtil.getNickName(record, passwordResult);
            String openType = BleUtil.getOpenType(record);
            if (!timeHead.equals(lastTimeHead)) { //添加头
                lastTimeHead = timeHead;
                List<BluetoothItemRecordBean> itemList = new ArrayList<>();
                itemList.add(new BluetoothItemRecordBean(nickName, openType, KeyConstants.BLUETOOTH_RECORD_COMMON,
                        hourSecond, false, false));
                showDatas.add(new BluetoothRecordBean(timeHead, itemList, false));
            } else {
                BluetoothRecordBean bluetoothRecordBean = showDatas.get(showDatas.size() - 1);
                List<BluetoothItemRecordBean> bluetoothItemRecordBeanList = bluetoothRecordBean.getList();
                bluetoothItemRecordBeanList.add(new BluetoothItemRecordBean(nickName, openType, KeyConstants.BLUETOOTH_RECORD_COMMON, hourSecond, false, false));
            }
        }

        for (int i = 0; i < showDatas.size(); i++) {
            BluetoothRecordBean bluetoothRecordBean = showDatas.get(i);
            List<BluetoothItemRecordBean> bluetoothRecordBeanList = bluetoothRecordBean.getList();

            for (int j = 0; j < bluetoothRecordBeanList.size(); j++) {
                BluetoothItemRecordBean bluetoothItemRecordBean = bluetoothRecordBeanList.get(j);

                if (j == 0) {
                    bluetoothItemRecordBean.setFirstData(true);
                }
                if (j == bluetoothRecordBeanList.size() - 1) {
                    bluetoothItemRecordBean.setLastData(true);
                }

            }
            if (i == showDatas.size() - 1) {
                bluetoothRecordBean.setLastData(true);
            }
        }
    }



    public void getRecord(){
        LogUtils.e("获取操作记录   "+supportOperationRecord);
        if (supportOperationRecord) {
            mPresenter.getOperationRecordFromServer(1);
        } else {
            mPresenter.getOpenRecordFromServer(1, bleLockInfo);
        }
    }
}
