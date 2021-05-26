package com.philips.easykey.lock.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.bluetooth.password.BlePasswordManagerActivity;
import com.philips.easykey.lock.activity.device.bluetooth.password.BluetoothPasswordShareActivity;
import com.philips.easykey.lock.activity.device.bluetooth.password.BluetoothUserPasswordAddActivity;
import com.philips.easykey.lock.activity.device.bluetooth.password.CycleRulesActivity;
import com.philips.easykey.lock.adapter.ShiXiaoNameAdapter;
import com.philips.easykey.lock.bean.ShiXiaoNameBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleFragment;
import com.philips.easykey.lock.mvp.presenter.ble.PasswordWeekPresenter;
import com.philips.easykey.lock.mvp.view.IPasswordLoopView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.http.postbean.AddPasswordBean;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateFormatUtils;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.philips.easykey.lock.utils.TimeUtil;
import com.blankj.utilcode.util.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


/**
 * Created by David
 */

public class PasswordWeekFragment extends BaseBleFragment<IPasswordLoopView, PasswordWeekPresenter<IPasswordLoopView>>
        implements View.OnClickListener, IPasswordLoopView {
    @BindView(R.id.recycleview)
    RecyclerView recyclerView;
    @BindView(R.id.et_name)
    EditText etName;
    List<ShiXiaoNameBean> list = new ArrayList<>();
    ShiXiaoNameAdapter shiXiaoNameAdapter;
    View mView;
    @BindView(R.id.ll_rule_repeat)
    LinearLayout llRuleRepeat;
    @BindView(R.id.btn_confirm_generation)
    Button btnConfirmGeneration;
    @BindView(R.id.btn_random_generation)
    TextView btnRandomGeneration;
    @BindView(R.id.et_password)
    EditText etPassword;
    public static final int REQUEST_CODE = 100;
    String weekRule;
    @BindView(R.id.tv_rule_repeat)
    TextView tvRuleRepeat;
    @BindView(R.id.ll_time)
    LinearLayout llTime;
    @BindView(R.id.pwd_manager_icon)
    ImageView pwdManagerIcon;
    @BindView(R.id.pwd_manager_grant_iv)
    ImageView pwdManagerGrantIv;
    @BindView(R.id.tv_start)
    TextView tvStart;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.tv_hint)
    TextView tvHint;
    private int[] days;
    String strStart;//开始
    String strEnd;//结束
    private BleLockInfo bleLockInfo;
    private int startMin;
    private int startHour;
    private int endMin;
    private int endHour;
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

    String startcurrentTime = formatter.format(new Date());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_password_period, container, false);
        }
        bleLockInfo = ((BluetoothUserPasswordAddActivity) getActivity()).getLockInfo();
        ButterKnife.bind(this, mView);
        llRuleRepeat.setOnClickListener(this);
        btnConfirmGeneration.setOnClickListener(this);
        btnRandomGeneration.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);
        initRecycleview();
//        initTimerPicker();
        setEffectiveTime();
        initMonitor();
        return mView;

    }

    private void initMonitor() {
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                shiXiaoNameAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setEffectiveTime() {
        String startTime = DateUtils.currentLong2HourMin(System.currentTimeMillis());
        String endTime = DateUtils.currentLong2HourMin(System.currentTimeMillis() + 60 * 60 * 1000);
        String[] startSplit = startTime.split(":");
        String[] endSplit = endTime.split(":");
        setStartTime(startSplit[0], startSplit[1]);
        setEndTime(endSplit[0], endSplit[1]);
    }

//    private void initTimerPicker() {
//        try {
//            long after1 = formatter.parse("00:00").getTime();
//            long after2 = formatter.parse("23:59").getTime();
//            long diff = after2 - after1;
//
//            String beginTime = formatter.format(new Date(System.currentTimeMillis()));
//            String endTime = formatter.format(new Date(System.currentTimeMillis() + diff));
//            // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
//            mTimerPicker = new CustomDatePicker(getActivity(), new CustomDatePicker.Callback() {
//                @Override
//                public void onTimeSelected(long timestamp) {
//                    startcurrentTime = formatter.format(new Date(timestamp));
//                }
//            }, beginTime, endTime);
//            // 允许点击屏幕或物理返回键关闭
//            mTimerPicker.setCancelable(true);
//            // 显示时和分
//            mTimerPicker.setCanShowPreciseTime(true);
//            // 允许循环滚动
//            mTimerPicker.setScrollLoop(false);
//            // 允许滚动动画
//            mTimerPicker.setCanShowAnim(true);
//        } catch (Exception e) {
//            Log.e("denganzhi1", e.getMessage());
//        }
//    }

    @Override
    protected PasswordWeekPresenter<IPasswordLoopView> createPresent() {
        return new PasswordWeekPresenter<>();
    }

    public static PasswordWeekFragment newInstance() {
        PasswordWeekFragment fragment = new PasswordWeekFragment();
        return fragment;
    }

    private void initRecycleview() {
        list.add(new ShiXiaoNameBean(getString(R.string.father), false));
        list.add(new ShiXiaoNameBean(getString(R.string.mother), false));
        list.add(new ShiXiaoNameBean(getString(R.string.elder_brother), false));
        list.add(new ShiXiaoNameBean(getString(R.string.small_di_di), false));
        list.add(new ShiXiaoNameBean(getString(R.string.elder_sister), false));
        list.add(new ShiXiaoNameBean(getString(R.string.philips_other), false));


        shiXiaoNameAdapter = new ShiXiaoNameAdapter(list);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        recyclerView.setAdapter(shiXiaoNameAdapter);
        shiXiaoNameAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelected(false);
                }
                ShiXiaoNameBean shiXiaoNameBean = list.get(position);
                String name = shiXiaoNameBean.getName();
                etName.setText(name);
                etName.setSelection(name.length());
                list.get(position).setSelected(true);
                shiXiaoNameAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_rule_repeat:
                intent = new Intent(getActivity(), CycleRulesActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btn_confirm_generation:
                if (!NetUtil.isNetworkAvailable()) {
                    ToastUtils.showShort(R.string.please_have_net_add_pwd);
                    return;
                }
                String strPassword = etPassword.getText().toString().trim();
                String nickName = etName.getText().toString().trim();

                if (!StringUtil.randomJudge(strPassword)) {
                    ToastUtils.showShort(R.string.philips_random_verify_error);
                    return;
                }
                if (StringUtil.checkSimplePassword(strPassword)) {
                    AlertDialogUtil.getInstance().noEditTwoButtonDialog(getActivity(), getString(R.string.hint), getString(R.string.password_simple_please_reset), getString(R.string.go_on), getString(R.string.reinstall), new AlertDialogUtil.ClickListener() {

                        @Override
                        public void left() {

                        }

                        @Override
                        public void right() {
                            etPassword.setText("");
                            return;
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }
                        @Override
                        public void afterTextChanged(String toString) {
                        }
                    });
                    return;
                }
                if (TextUtils.isEmpty(nickName)) {
                    ToastUtils.showShort(R.string.nickname_not_empty);
                    return;
                }

                if (TextUtils.isEmpty(strStart)) {
                    ToastUtils.showShort(R.string.select_start_time);
                    return;
                }
                if (TextUtils.isEmpty(strEnd)) {
                    ToastUtils.showShort(R.string.select_end_time);
                    return;
                }
                if (DateFormatUtils.hourMinuteChangeMillisecond(strEnd) <= DateFormatUtils.hourMinuteChangeMillisecond(strStart)) {
                    ToastUtils.showShort(R.string.end_time_great_start_time);
                    return;
                }
                if (TextUtils.isEmpty(weekRule)) {
                    ToastUtils.showShort(R.string.select_repeat_rule);
                    return;
                }
                if (mPresenter.isAuth(bleLockInfo, true)) {
                    mPresenter.setPwd(strPassword, nickName, startHour, startMin, endHour, endMin, days);
                }

                break;
            case R.id.btn_random_generation:
                String password = StringUtil.makeRandomPassword();
                etPassword.setText(password);
                etPassword.setSelection(password.length());
                break;
            case R.id.tv_start:
                //开始
                TimeUtil.getInstance().getHourMinute(getActivity(), new TimeUtil.TimeListener() {
                    @Override
                    public void time(String hour, String minute) {
                        setStartTime(hour, minute);
                    }
                });
                break;
            case R.id.tv_end:
                //结束
                TimeUtil.getInstance().getHourMinute(getActivity(), new TimeUtil.TimeListener() {
                    @Override
                    public void time(String hour, String minute) {
                        setEndTime(hour, minute);
                    }
                });
                break;
        }

    }

    private void setStartTime(String hour, String minute) {
        strStart = hour + ":" + minute;
        tvStart.setText(hour + ":" + minute);
        startHour = Integer.parseInt(hour);
        startMin = Integer.parseInt(minute);
        hintText();
    }

    private void setEndTime(String hour, String minute) {
        strEnd = hour + ":" + minute;
        tvEnd.setText(hour + ":" + minute);
        endHour = Integer.parseInt(hour);

        endMin = Integer.parseInt(minute);
        hintText();
    }

    /**
     * 密码将于每天15：25至 16：30重复生效
     * 密码将每周一 周二 周三 15：25至 16：30重复生效
     */
    public void hintText() {
        if (TextUtils.isEmpty(strStart) || TextUtils.isEmpty(strEnd) || TextUtils.isEmpty(weekRule)) {
            tvHint.setVisibility(View.INVISIBLE);
        } else {
            tvHint.setVisibility(View.VISIBLE);
            String strHint = String.format(getString(R.string.week_hint), weekRule, strStart, strEnd);
            tvHint.setText(strHint);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (REQUEST_CODE == requestCode) {
                weekRule = data.getStringExtra(KeyConstants.WEEK_REPEAT_DATA);

                days = data.getIntArrayExtra(KeyConstants.DAY_MASK);
                LogUtils.d("收到的周计划是   " + Arrays.toString(days));
                tvRuleRepeat.setText(weekRule);
                hintText();
            }
        }
    }

    @Override
    public void onPwdFull() {
        hiddenLoading();
        AlertDialogUtil.getInstance().noEditSingleButtonDialog(getActivity(), getString(R.string.hint), getString(R.string.password_full_and_delete_exist_code), getString(R.string.hao_de), new AlertDialogUtil.ClickListener() {
            @Override
            public void left() {

            }

            @Override
            public void right() {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(String toString) {

            }
        });
    }

    @Override
    public void startSetPwd() {
        showLoading(getString(R.string.is_setting));
    }

    @Override
    public void endSetPwd() {
        hiddenLoading();
    }

    @Override
    public void onSetPasswordSuccess(AddPasswordBean.Password password) {

    }

    @Override
    public void onSetPasswordFailed(Throwable throwable) {
        ToastUtils.showShort(R.string.set_failed);
    }

    @Override
    public void setWeekPlanSuccess() {

    }

    @Override
    public void setWeekPlanFailed(Throwable throwable) {
        ToastUtils.showShort(R.string.set_failed);
    }

    @Override
    public void setUserTypeSuccess() {

    }

    @Override
    public void setUserTypeFailed(Throwable throwable) {
        ToastUtils.showShort(R.string.set_failed);
    }

    @Override
    public void onUploadPwdSuccess(String password, String number, String nickName) {
        LogUtils.d("添加密码成功   " + password.toString());
        // 获取到开始时间,结束时间 设置
        Intent intent = new Intent();
        intent.setClass(MyApplication.getInstance(), BluetoothPasswordShareActivity.class);
        intent.putExtra(KeyConstants.TO_DETAIL_NUMBER, number);
        intent.putExtra(KeyConstants.TO_DETAIL_PASSWORD, password);
        intent.putExtra(KeyConstants.TO_DETAIL_TYPE, 1);
        intent.putExtra(KeyConstants.TO_DETAIL_NICKNAME, nickName);
        intent.putExtra(KeyConstants.TIME_CE_LUE, KeyConstants.PERIOD);
        intent.putExtra(KeyConstants.PERIOD_START_TIME, strStart);
        intent.putExtra(KeyConstants.PERIOD_END_TIME, strEnd);
        intent.putExtra(KeyConstants.WEEK_REPEAT_DATA, weekRule);
        startActivity(intent);
    }

    @Override
    public void onUploadPwdFailed(Throwable throwable) {
        ToastUtils.showShort(R.string.lock_set_success_please_sync);
        startActivity(new Intent(getContext(), BlePasswordManagerActivity.class));
        getActivity().finish();
    }

    @Override
    public void onUploadPwdFailedServer(BaseResult result) {
        ToastUtils.showShort(R.string.lock_set_success_please_sync);
        startActivity(new Intent(getContext(), BlePasswordManagerActivity.class));
        getActivity().finish();
    }

    @Override
    public void onSyncPasswordFailed(Throwable throwable) {
        hiddenLoading();
        ToastUtils.showLong(getString(R.string.set_failed));
    }

    @Override
    public void onTimePwdFull() {
        hiddenLoading();
        ToastUtils.showLong(R.string.only_0to4);
    }
}
