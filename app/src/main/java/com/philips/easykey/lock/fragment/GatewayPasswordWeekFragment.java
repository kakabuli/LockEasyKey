package com.philips.easykey.lock.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
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
import com.philips.easykey.lock.activity.device.bluetooth.password.CycleRulesActivity;
import com.philips.easykey.lock.activity.device.gatewaylock.password.GatewayPasswordAddActivity;
import com.philips.easykey.lock.activity.device.gatewaylock.password.GatewayLockPasswordShareActivity;
import com.philips.easykey.lock.adapter.ShiXiaoNameAdapter;
import com.philips.easykey.lock.bean.GateWayArgsBean;
import com.philips.easykey.lock.bean.ShiXiaoNameBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseFragment;
import com.philips.easykey.lock.mvp.presenter.gatewaylockpresenter.GatewayLockPasswordWeekPresenter;
import com.philips.easykey.lock.mvp.view.gatewaylockview.IGatewayLockPasswordWeekView;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateFormatUtils;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.philips.easykey.lock.utils.TimeUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayPasswordPlanBean;
import com.philips.easykey.lock.widget.CustomDatePicker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;


import static android.app.Activity.RESULT_OK;


/**
 * Created by David
 */

public class GatewayPasswordWeekFragment extends BaseFragment<IGatewayLockPasswordWeekView, GatewayLockPasswordWeekPresenter<IGatewayLockPasswordWeekView>>
        implements View.OnClickListener, IGatewayLockPasswordWeekView {

    RecyclerView recyclerView;
    EditText etName;
    List<ShiXiaoNameBean> list = new ArrayList<>();
    ShiXiaoNameAdapter shiXiaoNameAdapter;
    View mView;
    LinearLayout llRuleRepeat;
    Button btnConfirmGeneration;
    TextView btnRandomGeneration;
    EditText etPassword;
    public static final int REQUEST_CODE = 100;
    String weekRule;
    TextView tvRuleRepeat;
    LinearLayout llTime;
    ImageView pwdManagerIcon;
    ImageView pwdManagerGrantIv;
    TextView tvStart;
    TextView tvEnd;
    TextView tvHint;
    LinearLayout llNickName;
    private int[] days;
    String strStart;//开始
    String strEnd;//结束
    private int startMin;
    private int startHour;
    private int endMin;
    private int endHour;
    private CustomDatePicker mTimerPicker;
    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");

    String startcurrentTime = formatter.format(new Date());
    private String gatewayId;
    private String deviceId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_password_gateway_period , container, false);
        }

        recyclerView = mView.findViewById(R.id.recycleview);
        etName = mView.findViewById(R.id.et_name);
        llRuleRepeat = mView.findViewById(R.id.ll_rule_repeat);
        btnConfirmGeneration = mView.findViewById(R.id.btn_confirm_generation);
        btnRandomGeneration = mView.findViewById(R.id.btn_random_generation);
        etPassword = mView.findViewById(R.id.et_password);
        tvRuleRepeat = mView.findViewById(R.id.tv_rule_repeat);
        llTime = mView.findViewById(R.id.ll_time);
        pwdManagerIcon = mView.findViewById(R.id.pwd_manager_icon);
        pwdManagerGrantIv = mView.findViewById(R.id.pwd_manager_grant_iv);
        tvStart = mView.findViewById(R.id.tv_start);
        tvEnd = mView.findViewById(R.id.tv_end);
        tvHint = mView.findViewById(R.id.tv_hint);
        llNickName = mView.findViewById(R.id.ll_nick_name);

        llRuleRepeat.setOnClickListener(this);
        btnConfirmGeneration.setOnClickListener(this);
        btnRandomGeneration.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);

        gatewayId = ((GatewayPasswordAddActivity) getActivity()).gatewayId;
        deviceId = ((GatewayPasswordAddActivity) getActivity()).deviceId;

        initRecycleview();
        initTimerPicker();
        setEffectiveTime();
        llNickName.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        return mView;

    }

    @Override
    protected GatewayLockPasswordWeekPresenter<IGatewayLockPasswordWeekView> createPresent() {
        return new GatewayLockPasswordWeekPresenter<>();
    }


    private void setEffectiveTime() {
        String startTime = DateUtils.currentLong2HourMin(System.currentTimeMillis());
        String endTime = DateUtils.currentLong2HourMin(System.currentTimeMillis() + 60 * 60 * 1000);
        String[] startSplit = startTime.split(":");
        String[] endSplit = endTime.split(":");
        setStartTime(startSplit[0], startSplit[1]);
        setEndTime(endSplit[0], endSplit[1]);
    }

    private void initTimerPicker() {
        try {
            long after1 = formatter.parse("00:00").getTime();
            long after2 = formatter.parse("23:59").getTime();
            long diff = after2 - after1;

            String beginTime = formatter.format(new Date(System.currentTimeMillis()));
            String endTime = formatter.format(new Date(System.currentTimeMillis() + diff));
            // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
            mTimerPicker = new CustomDatePicker(getActivity(), new CustomDatePicker.Callback() {
                @Override
                public void onTimeSelected(long timestamp) {
                    startcurrentTime = formatter.format(new Date(timestamp));
                }
            }, beginTime, endTime);
            // 允许点击屏幕或物理返回键关闭
            mTimerPicker.setCancelable(true);
            // 显示时和分
            mTimerPicker.setCanShowPreciseTime(true);
            // 允许循环滚动
            mTimerPicker.setScrollLoop(false);
            // 允许滚动动画
            mTimerPicker.setCanShowAnim(true);
        } catch (Exception e) {
            Log.e("denganzhi1", e.getMessage());
        }
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
//                if (TextUtils.isEmpty(nickName)) {
//                    ToastUtils.showShort(R.string.nickname_not_empty);
//                    return;
//                }

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

//                if (mPresenter.isAuth(bleLockInfo, true)) {
//                    mPresenter.setPwd(strPassword, nickName, startHour, startMin, endHour, endMin, days);
//                }
                int dayMask = 0;
                for (int i = 0; i < days.length; i++) {
                    dayMask += days[i] << i;
                }
                showLoading(getString(R.string.is_setting_password));


                GatewayPasswordAddActivity gatewayPasswordAddActivity= (GatewayPasswordAddActivity) getActivity();
                String model=  gatewayPasswordAddActivity.gatewayModel;

                GateWayArgsBean gateWayArgsBean=new GateWayArgsBean();
                gateWayArgsBean.setPwdType(2);
                gateWayArgsBean.setDayMaskBits(dayMask);
                gateWayArgsBean.setStartHour(startHour);
                gateWayArgsBean.setStartMinute(startMin);
                gateWayArgsBean.setEndHour(endHour);
                gateWayArgsBean.setEndMinute(endMin);

                if(!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW2)){
                    mPresenter.sysPassworByhttp(MyApplication.getInstance().getUid(),
                            gatewayId,
                            deviceId,
                            strPassword,
                            gateWayArgsBean
                    );
                }else{
                    mPresenter.setWeekPassword(deviceId, gatewayId, strPassword, 0, dayMask, endHour, endMin, startHour, startMin);
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
    public void getLockInfoSuccess(int maxPwd) {

    }

    @Override
    public void getLockInfoFail() {
        onFailed();
    }

    @Override
    public void getLockInfoThrowable(Throwable throwable) {
        onFailed();
    }

    @Override
    public void syncPasswordComplete(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {

    }

    @Override
    public void syncPasswordFailed(Throwable throwable) {
        onFailed();
    }

    @Override
    public void addLockPwdFail(Throwable throwable) {
        onFailed();
    }

    @Override
    public void addLockPwdSuccess(GatewayPasswordPlanBean gatewayPasswordPlanBean, String pwdValue) {

    }

    @Override
    public void onLoadPasswordPlan(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {

    }

    @Override
    public void onLoadPasswordPlanFailed(Throwable throwable) {

    }

    @Override
    public void onLoadPasswordPlanComplete(Map<Integer, GatewayPasswordPlanBean> passwordPlanBeans) {

    }

    @Override
    public void setUserTypeSuccess(String passwordValue, GatewayPasswordPlanBean gatewayPasswordPlanBean) {
        hiddenLoading();
        ToastUtils.showLong(getString(R.string.set_success));

        //跳转到分享页面
        Intent intent = new Intent(getActivity(), GatewayLockPasswordShareActivity.class);
        intent.putExtra(KeyConstants.GATEWAY_ID, gatewayId);
        intent.putExtra(KeyConstants.DEVICE_ID, deviceId);
        //1表示永久密码，2表示临时密码
        intent.putExtra(KeyConstants.PWD_VALUE, passwordValue);
        intent.putExtra(KeyConstants.PWD_ID, gatewayPasswordPlanBean.getPasswordNumber());
        intent.putExtra(KeyConstants.GATEWAY_PASSWORD_BEAN, gatewayPasswordPlanBean);
        startActivity(intent);
    }

    @Override
    public void setUserTypeFailed(Throwable typeFailed) {
        onFailed();
    }

    @Override
    public void setPlanSuccess(String passwordValue, GatewayPasswordPlanBean gatewayPasswordPlanBean) {  //设置成功

    }

    @Override
    public void setPlanFailed(Throwable throwable) {
        onFailed();
    }

    @Override
    public void deletePasswordSuccess() {

    }

    @Override
    public void deletePasswordFailed(Throwable throwable) {

    }

    private void onFailed() {
        hiddenLoading();
        //密码添加异常
        LogUtils.d("添加密码异常    ");
        AlertDialogUtil.getInstance().singleButtonNoTitleDialog(getContext(), getString(R.string.add_lock_pwd_fail), getString(R.string.philips_confirm), "#1F96F7", new AlertDialogUtil.ClickListener() {
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
    public void gatewayPasswordFull() {
        hiddenLoading();
        AlertDialogUtil.getInstance().noEditSingleButtonDialog(getContext(), getString(R.string.hint), getString(R.string.password_full_and_delete_exist_code), getString(R.string.hao_de), new AlertDialogUtil.ClickListener() {
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
}
