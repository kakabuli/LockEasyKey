package com.philips.easykey.lock.activity.addDevice.singleswitch;

import android.content.Intent;
import android.os.Handler;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.annotations.SerializedName;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.device.wifilock.newadd.WifiLockAddToSetSwitchActivity;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.singlefireswitchpresenter.SingleFireSwitchSettingPresenter;
import com.philips.easykey.lock.mvp.view.singlefireswitchview.SingleFireSwitchView;
import com.philips.easykey.lock.publiclibrary.bean.SingleFireSwitchInfo;
import com.philips.easykey.lock.publiclibrary.bean.SwitchNumberBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.AddSingleFireSwitchBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.BindingSingleFireSwitchBean;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.CloneObjectUtils;
import com.philips.easykey.lock.utils.DensityUtil;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.blankj.utilcode.util.ToastUtils;

import com.philips.easykey.lock.publiclibrary.bean.WifiLockInfo;
import com.philips.easykey.lock.utils.KeyConstants;
import com.blankj.utilcode.util.LogUtils;
import com.philips.easykey.lock.utils.dialog.InfoDialog;

import java.util.ArrayList;
import java.util.List;

public class SwipchLinkActivity extends BaseActivity<SingleFireSwitchView, SingleFireSwitchSettingPresenter<SingleFireSwitchView>> implements View.OnClickListener,SingleFireSwitchView {

    ImageView swipch_link_center_img,swipch_one_img,swipch_two_img,swipch_three_img;
    LinearLayout swipch_two,swipch_one,swipch_three;
    View swich_view_item_two;
    ImageView swipch_setting_btn,swich_link_link_switch,back;
    RelativeLayout tv_double_rl,three_double_rl;
    TextView swipch_one_text,swipch_two_text,swipch_three_text;
    TextView swipch_one_nick_text,swipch_two_nick_text,swipch_three_nick_text,swipch_sure_to_perform;

    // 两次点击按钮之间的最小点击间隔时间(单位:ms)
    private static final int MIN_CLICK_DELAY_TIME = 500;
    // 最后一次点击的时间
    private long lastClickTime;

    private WifiLockInfo wifiLockInfo;
    private WifiLockInfo wifiLockInfoChange;
    private int SwitchNumber;
    private int switchEn;
    private boolean switchIsEn;
    private String wifiSn;
    private boolean switchOneIsEn;
    private boolean switchTwoIsEn;
    private boolean switchThreeIsEn;
    private int startTime;
    private int endTime;
    private String starHourt;
    private String endHourt;
    private String startMin;
    private String endMin;
    private Handler handler;

    private AddSingleFireSwitchBean addSingleFireSwitchBean;
    private BindingSingleFireSwitchBean bindingSingleFireSwitchBean;
    @SerializedName("switch")
    private SingleFireSwitchInfo params;
    private SwitchNumberBean switchNumberBean;
    @SerializedName("switchArray")
    private List<SwitchNumberBean> switchNumber = new ArrayList<>();

    private static final int TO_SET_NICK_NAME_REQUEST_CODE = 10101;
    private static final int TO_SET_TIME_REQUEST_CODE = 10102;
    private static final int TO_SET_ALL_REQUEST_CODE = 10103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipch_link);

        swipch_link_center_img = findViewById(R.id.swipch_link_center_img);
        swipch_one = findViewById(R.id.swipch_one);
        swipch_two = findViewById(R.id.swipch_two);
        swich_link_link_switch = findViewById(R.id.swich_link_link_switch);

        swipch_one_img = findViewById(R.id.swipch_one_img);
        swipch_two_img = findViewById(R.id.swipch_two_img);
        swipch_three_img = findViewById(R.id.swipch_three_img);

        swipch_three = findViewById(R.id.swipch_three);
        swich_view_item_two = findViewById(R.id.swich_view_item_two);
        swipch_setting_btn = findViewById(R.id.swipch_setting_btn);

        tv_double_rl = findViewById(R.id.tv_double_rl);
        three_double_rl = findViewById(R.id.three_double_rl);
        back = findViewById(R.id.back);
        swipch_one_text = findViewById(R.id.swipch_one_text);
        swipch_two_text = findViewById(R.id.swipch_two_text);
        swipch_three_text = findViewById(R.id.swipch_three_text);
        swipch_one_nick_text = findViewById(R.id.swipch_one_nick_text);
        swipch_two_nick_text = findViewById(R.id.swipch_two_nick_text);
        swipch_three_nick_text = findViewById(R.id.swipch_three_nick_text);
        swipch_sure_to_perform = findViewById(R.id.swipch_sure_to_perform);

        swipch_one.setOnClickListener(this);
        swipch_two.setOnClickListener(this);
        swipch_three.setOnClickListener(this);
        swipch_setting_btn.setOnClickListener(this);
        tv_double_rl.setOnClickListener(this);
        three_double_rl.setOnClickListener(this);
        swipch_link_center_img.setOnClickListener(this);
        swich_link_link_switch.setOnClickListener(this);
        back.setOnClickListener(this);
        swipch_one_img.setOnClickListener(this);
        swipch_two_img.setOnClickListener(this);
        swipch_three_img.setOnClickListener(this);
        swipch_sure_to_perform.setOnClickListener(this);

        initData();
        initRecycleview();

    }

    @Override
    protected SingleFireSwitchSettingPresenter<SingleFireSwitchView> createPresent() {
        return new SingleFireSwitchSettingPresenter<>();
    }

    private void initData() {

        wifiSn = getIntent().getStringExtra(KeyConstants.WIFI_SN);
        wifiLockInfo = MyApplication.getInstance().getWifiLockInfoBySn(wifiSn);
        wifiLockInfoChange = CloneObjectUtils.cloneObject(wifiLockInfo);
//        wifiLockInfoChange =  wifiLockInfo;
//        LogUtils.d("--kaadas--=wifiLockInfo.hashCode="+wifiLockInfo.hashCode());
//        LogUtils.d("--kaadas--=wifiLockInfoChange.hashCode="+wifiLockInfoChange.hashCode());
//        LogUtils.d("--kaadas--=wifiLockInfo.identityHashCode="+System.identityHashCode(wifiLockInfo));
//        LogUtils.d("--kaadas--=wifiLockInfoChange.identityHashCode="+System.identityHashCode(wifiLockInfoChange));

    }
    private void initRecycleview() {

        //使能开关
        switchEn = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchEn();
        if (switchEn != 0) {
            switchIsEn = true;
            swich_link_link_switch.setBackgroundResource(R.mipmap.swipperlinkleft_blue);
        } else {
            switchIsEn = false;
            swich_link_link_switch.setBackgroundResource(R.mipmap.swipperlinkleft_no);
        }

        //键位开关
        List<SwitchNumberBean> switchNumberList = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber();
        for (SwitchNumberBean switchNumber : switchNumberList) {

            switch (switchNumber.getType()) {
                case 1://键位1开关
                    //开关显示
                    if (switchNumber.getTimeEn() == 0) {
                        switchOneIsEn = false;
                        swipch_one_img.setBackgroundResource(R.mipmap.swipperlinkleft_no);
                    } else {
                        switchOneIsEn = true;
                        swipch_one_img.setBackgroundResource(R.mipmap.swipperlinkleft_yes);
                    }
                    //时间显示
                    startTime = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(0).getStartTime();
                    endTime = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(0).getStopTime();
                    if ((startTime/60)<10){
                        starHourt = "0"+(startTime/60);
                    } else {
                        starHourt = String.valueOf((startTime/60));
                    }
                    if ((startTime%60)<10){
                        startMin = "0"+(startTime%60);
                    } else {
                        startMin = String.valueOf((startTime%60));
                    }
                    if ((endTime/60)<10){
                        endHourt = "0"+(endTime/60);
                    } else {
                        endHourt = String.valueOf((endTime/60));
                    }
                    if ((endTime%60)<10){
                        endMin = "0"+(endTime%60);
                    } else {
                        endMin = String.valueOf((endTime%60));
                    }
                    swipch_one_text.setText(starHourt + ":" + startMin +"-"+endHourt + ":" + endMin);
                    //键位昵称
                    if (wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(0).getNickname()!=null) {
                        if (wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(0).getNickname().length() > 0)
                            swipch_one_nick_text.setText( getString(R.string.philips_activity_swipchlink_1,wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(0).getNickname()));
                    }
                    break;
                case 2://键位2开关
                    //开关显示
                    if (switchNumber.getTimeEn() == 0) {
                        switchTwoIsEn = false;
                        swipch_two_img.setBackgroundResource(R.mipmap.swipperlinkleft_no);
                    } else {
                        switchTwoIsEn = true;
                        swipch_two_img.setBackgroundResource(R.mipmap.swipperlinkleft_yes);
                    }
                    //时间显示
                    startTime = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(1).getStartTime();
                    endTime = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(1).getStopTime();
                    if ((startTime/60)<10){
                        starHourt = "0"+(startTime/60);
                    } else {
                        starHourt = String.valueOf((startTime/60));
                    }
                    if ((startTime%60)<10){
                        startMin = "0"+(startTime%60);
                    } else {
                        startMin = String.valueOf((startTime%60));
                    }
                    if ((endTime/60)<10){
                        endHourt = "0"+(endTime/60);
                    } else {
                        endHourt = String.valueOf((endTime/60));
                    }
                    if ((endTime%60)<10){
                        endMin = "0"+(endTime%60);
                    } else {
                        endMin = String.valueOf((endTime%60));
                    }
                    swipch_two_text.setText(starHourt + ":" + startMin +"-"+endHourt + ":" + endMin);
                    //键位昵称
                    if (wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(1).getNickname()!=null) {
                        if (wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(1).getNickname().length() > 0)
                            swipch_two_nick_text.setText( getString(R.string.philips_activity_swipchlink_2,wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(1).getNickname()));
                    }
                    break;
                case 3://键位3开关
                    //开关显示
                    if (switchNumber.getTimeEn() == 0) {
                        switchThreeIsEn = false;
                        swipch_three_img.setBackgroundResource(R.mipmap.swipperlinkleft_no);
                    } else {
                        switchThreeIsEn = true;
                        swipch_three_img.setBackgroundResource(R.mipmap.swipperlinkleft_yes);
                    }
                    //时间显示
                    startTime = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(2).getStartTime();
                    endTime = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(2).getStopTime();
                    if ((startTime/60)<10){
                        starHourt = "0"+(startTime/60);
                    } else {
                        starHourt = String.valueOf((startTime/60));
                    }
                    if ((startTime%60)<10){
                        startMin = "0"+(startTime%60);
                    } else {
                        startMin = String.valueOf((startTime%60));
                    }
                    if ((endTime/60)<10){
                        endHourt = "0"+(endTime/60);
                    } else {
                        endHourt = String.valueOf((endTime/60));
                    }
                    if ((endTime%60)<10){
                        endMin = "0"+(endTime%60);
                    } else {
                        endMin = String.valueOf((endTime%60));
                    }
                    swipch_three_text.setText(starHourt + ":" + startMin +"-"+endHourt + ":" + endMin);
                    //键位昵称
                    if (wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(2).getNickname()!=null) {

                        if (wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(2).getNickname().length() > 0)
                            swipch_three_nick_text.setText(getString(R.string.philips_activity_swipchlink_3,wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber().get(2).getNickname()));
                    }
                    break;
            }

        }

        SwitchNumber = getIntent().getIntExtra(KeyConstants.SWITCH_NUMBER,1);

        switch (SwitchNumber){
            case 1:
                swipch_link_center_img.setBackgroundResource(R.mipmap.swipch_link_one);
                swipch_two.setVisibility(View.GONE);
                swipch_three.setVisibility(View.GONE);
                tv_double_rl.setVisibility(View.GONE);
                three_double_rl.setVisibility(View.GONE);
                break;
            case  2:
                swipch_link_center_img.setBackgroundResource(R.mipmap.swipch_link_two);
                swipch_three.setVisibility(View.GONE);
                swich_view_item_two.setVisibility(View.GONE);
                tv_double_rl.setVisibility(View.VISIBLE);
                three_double_rl.setVisibility(View.GONE);
                break;

            case  3:
                swipch_link_center_img.setBackgroundResource(R.mipmap.swipch_link_three);

                ViewGroup.LayoutParams mylayoutParam1 =  swipch_one_img.getLayoutParams();
                RelativeLayout.LayoutParams rllayoutParam1 =( RelativeLayout.LayoutParams) (mylayoutParam1);
                rllayoutParam1.addRule(RelativeLayout.ALIGN_PARENT_TOP);

                ViewGroup.LayoutParams mylayoutParam2 =  swipch_two_img.getLayoutParams();
                RelativeLayout.LayoutParams rllayoutParam2 =( RelativeLayout.LayoutParams) (mylayoutParam2);
                rllayoutParam2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                rllayoutParam2.topMargin= DensityUtil.dip2px(this,10);

                ViewGroup.LayoutParams mylayoutParam3 =  swipch_three_img.getLayoutParams();
                RelativeLayout.LayoutParams rllayoutParam3 =( RelativeLayout.LayoutParams) (mylayoutParam3);
                rllayoutParam3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                rllayoutParam3.topMargin= DensityUtil.dip2px(this,10);

                if(mylayoutParam1 instanceof  RelativeLayout.LayoutParams){

                    swipch_one_img.setLayoutParams(rllayoutParam1);
                    swipch_two_img.setLayoutParams(mylayoutParam2);
                    swipch_three_img.setLayoutParams(mylayoutParam3);
                }else if(mylayoutParam1 instanceof  LinearLayout.LayoutParams){

                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {// 两次点击的时间间隔大于最小限制时间，则触发点击事件
            lastClickTime = currentTime;
            Intent intent;
            // 这里触发点击事件
            switch (v.getId()) {

                case R.id.swipch_one:
                    intent = new Intent(this, SwipchLinkSettingActivity.class);
                    intent.putExtra(KeyConstants.SWITCH_KEY_NUMBER, 1);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    intent.putExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE, wifiLockInfoChange);
                    startActivityForResult(intent,TO_SET_TIME_REQUEST_CODE);
                    break;
                case R.id.swipch_two:
                    intent = new Intent(this, SwipchLinkSettingActivity.class);
                    intent.putExtra(KeyConstants.SWITCH_KEY_NUMBER, 2);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    intent.putExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE, wifiLockInfoChange);
                    startActivityForResult(intent,TO_SET_TIME_REQUEST_CODE);
                    break;
                case R.id.swipch_three:
                    intent = new Intent(this, SwipchLinkSettingActivity.class);
                    intent.putExtra(KeyConstants.SWITCH_KEY_NUMBER, 3);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    intent.putExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE, wifiLockInfoChange);
                    startActivityForResult(intent,TO_SET_TIME_REQUEST_CODE);

                    break;
                case R.id.swipch_one_img:
                    if (switchOneIsEn) {
                        switchOneIsEn = false;
                        swipch_one_img.setBackgroundResource(R.mipmap.swipperlinkleft_no);
                        List<SwitchNumberBean> switchNumber = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber();
                        if (switchNumber.size()>0){
                            switchNumber.get(0).setTimeEn(0);
                        }
//                        mPresenter.settingDevice(wifiLockInfoChange);
                    } else {
                        switchOneIsEn = true;
                        swipch_one_img.setBackgroundResource(R.mipmap.swipperlinkleft_yes);
                        List<SwitchNumberBean> switchNumber = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber();
                        if (switchNumber.size()>0){
                            switchNumber.get(0).setTimeEn(1);
                        }
//                        mPresenter.settingDevice(wifiLockInfoChange);
                    }
                    break;
                case R.id.swipch_two_img:
                    if (switchTwoIsEn) {
                        switchTwoIsEn = false;
                        swipch_two_img.setBackgroundResource(R.mipmap.swipperlinkleft_no);
                        List<SwitchNumberBean> switchNumber = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber();
                        if (switchNumber.size()>1){
                            switchNumber.get(1).setTimeEn(0);
                        }
//                        mPresenter.settingDevice(wifiLockInfoChange);
                    } else {
                        switchTwoIsEn = true;
                        swipch_two_img.setBackgroundResource(R.mipmap.swipperlinkleft_yes);
                        List<SwitchNumberBean> switchNumber = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber();
                        if (switchNumber.size()>1){
                            switchNumber.get(1).setTimeEn(1);
                        }
//                        mPresenter.settingDevice(wifiLockInfoChange);
                    }

                    break;
                case R.id.swipch_three_img:
                    if (switchThreeIsEn) {
                        switchThreeIsEn = false;
                        swipch_three_img.setBackgroundResource(R.mipmap.swipperlinkleft_no);
                        List<SwitchNumberBean> switchNumber = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber();
                        if (switchNumber.size()>2){
                            switchNumber.get(2).setTimeEn(0);
                        }
//                        mPresenter.settingDevice(wifiLockInfoChange);
                    } else {
                        switchThreeIsEn = true;
                        swipch_three_img.setBackgroundResource(R.mipmap.swipperlinkleft_yes);
                        List<SwitchNumberBean> switchNumber = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber();
                        if (switchNumber.size()>2){
                            switchNumber.get(2).setTimeEn(1);
                        }
//                        mPresenter.settingDevice(wifiLockInfoChange);
                    }
                    break;
                case R.id.swipch_setting_btn:
                    intent = new Intent(this, SwipchSeetingArgus.class);
                    intent.putExtra(KeyConstants.SWITCH_NUMBER, SwitchNumber);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    intent.putExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE, wifiLockInfoChange);
                    startActivityForResult(intent,TO_SET_NICK_NAME_REQUEST_CODE);
                    break;
                case R.id.swich_link_link_switch:

                    if (switchIsEn) {
                        switchIsEn = false;
                        swich_link_link_switch.setBackgroundResource(R.mipmap.swipperlinkleft_no);
                        wifiLockInfoChange.getSingleFireSwitchInfo().setSwitchEn(0);
//                        mPresenter.settingDevice(wifiLockInfoChange);
                    } else {
                        switchIsEn = true;
                        swich_link_link_switch.setBackgroundResource(R.mipmap.swipperlinkleft_blue);
                        wifiLockInfoChange.getSingleFireSwitchInfo().setSwitchEn(1);
//                        mPresenter.settingDevice(wifiLockInfoChange);
                    }
                    break;
                case R.id.back:
                    finish();
                    break;
                case R.id.swipch_sure_to_perform:

                    if (swipch_one.getVisibility() == View.VISIBLE){
                        if (swipch_one_text.getText().equals("00:00-00:00")||swipch_one_text == null){
                            haveNotSetTime();
                            break;
                        }
                    }
                    if (swipch_two.getVisibility() == View.VISIBLE){
                        if (swipch_two_text.getText().equals("00:00-00:00")||swipch_two_text == null){
                            haveNotSetTime();
                            break;
                        }
                    }
                    if (swipch_three.getVisibility() == View.VISIBLE){
                        if (swipch_three_text.getText().equals("00:00-00:00")||swipch_three_text == null){
                            haveNotSetTime();
                            break;
                        }

                    }

                    intent = new Intent(this, WifiLockAddToSetSwitchActivity.class);
                    intent.putExtra(KeyConstants.WIFI_SN, wifiSn);
                    intent.putExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE, wifiLockInfoChange);
//                    startActivity(intent);
                    startActivityForResult(intent, TO_SET_ALL_REQUEST_CODE);

                    break;
            }
        }
    }
    public void haveNotSetTime() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                AlertDialogUtil.getInstance().singleButtonNoTitleDialogNoLine(SwipchLinkActivity.this, getString(R.string.have_not_set_time),
                        getString(R.string.hao_de), "#1F96F7", new AlertDialogUtil.ClickListener() {
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
        });
    }
    @Override
    public void settingDeviceSuccess() {
        LogUtils.d("--kaadas--设置成功");
//        params = wifiLockInfoChange.getSingleFireSwitchInfo();
//        bindingSingleFireSwitchBean = new BindingSingleFireSwitchBean(wifiSn,wifiLockInfoChange.getUid(),wifiLockInfoChange.getLockNickname(),params);
//        mPresenter.updateSwitchNickname(bindingSingleFireSwitchBean);
    }

    @Override
    public void settingDeviceFail() {
//        LogUtils.d("--kaadas--设置失败");
//        refresh();
        ToastUtils.showShort(getString(R.string.set_failed));
    }

    @Override
    public void settingDeviceThrowable() {
        ToastUtils.showShort(R.string.philips_activity_swipchlink_4);

    }

    @Override
    public void gettingDeviceSuccess() {

    }

    @Override
    public void gettingDeviceFail() {

    }

    @Override
    public void gettingDeviceThrowable() {

    }

    @Override
    public void addDeviceSuccess(AddSingleFireSwitchBean addSingleFireSwitchBean) {
        LogUtils.d("--kaadas--添加成功");
    }

    @Override
    public void addDeviceFail() {
        LogUtils.d("--kaadas--添加失败");
    }

    @Override
    public void addDeviceThrowable() {
        LogUtils.d("--kaadas--添加超时");
    }

    @Override
    public void bindingAndModifyDeviceSuccess() {
        LogUtils.d("--kaadas--修改信息成功");

//        switchIsEn = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchEn() == 1?true:false;
//        List<SwitchNumberBean> switchNumber = wifiLockInfoChange.getSingleFireSwitchInfo().getSwitchNumber();
//        for (SwitchNumberBean switchNumberBean:switchNumber){
//            switchNumberBean.getTimeEn();
//        }
//        for (int i=0;i<switchNumber.size();i++){
//            switch (i){
//                case 0:
//                    switchOneIsEn = switchNumber.get(i).getTimeEn() == 1?true:false;
//                    break;
//                case 1:
//                    switchTwoIsEn = switchNumber.get(i).getTimeEn() == 1?true:false;
//                    break;
//                case 2:
//                    switchThreeIsEn = switchNumber.get(i).getTimeEn() == 1?true:false;
//                    break;
//            }
//
//        }
//
//        MyApplication.getInstance().getAllDevicesByMqtt(true);
    }

    @Override
    public void bindingAndModifyDeviceFail() {
        ToastUtils.showShort(R.string.set_failed);
    }

    @Override
    public void bindingAndModifyDeviceThrowable() {
        ToastUtils.showShort(R.string.philips_activity_swipchlink_4);
    }
//    public void refresh() {
//        if (switchIsEn) {
//            swich_link_link_switch.setBackgroundResource(R.mipmap.swipperlinkleft_blue);
//        } else {
//            swich_link_link_switch.setBackgroundResource(R.mipmap.swipperlinkleft_no);
//        }
//        if (switchOneIsEn) {
//            swipch_one_img.setBackgroundResource(R.mipmap.swipperlinkleft_yes);
//        } else {
//            swipch_one_img.setBackgroundResource(R.mipmap.swipperlinkleft_no);
//        }
//        if (switchTwoIsEn) {
//            swipch_two_img.setBackgroundResource(R.mipmap.swipperlinkleft_yes);
//        } else {
//            swipch_two_img.setBackgroundResource(R.mipmap.swipperlinkleft_no);
//        }
//        if (switchThreeIsEn) {
//            swipch_three_img.setBackgroundResource(R.mipmap.swipperlinkleft_yes);
//        } else {
//            swipch_three_img.setBackgroundResource(R.mipmap.swipperlinkleft_no);
//        }
//
//        finish();
//        startActivity(getIntent());
//    }

    protected void reload(boolean result) {

        if (result) {

                wifiLockInfo = wifiLockInfoChange;

                MyApplication.getInstance().getAllDevicesByMqtt(true);
            } else  {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
                        initData();
                        initRecycleview();
//                    }
//                });
            }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TO_SET_NICK_NAME_REQUEST_CODE || requestCode == TO_SET_TIME_REQUEST_CODE ) {
            if (data != null) {
                wifiLockInfoChange = (WifiLockInfo) data.getSerializableExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE);
                initRecycleview();
            }
        }
        else if (requestCode == TO_SET_ALL_REQUEST_CODE ){
//            LogUtils.d("--kaadas--TO_SET_ALL_REQUEST_CODE=="+resultCode);
            if (resultCode == RESULT_OK && data != null) {

                //                LogUtils.d("--kaadas--WIFI_LOCK_INFO_CHANGE_RESULT=="+data.getBooleanExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE_RESULT,false));
                reload(data.getBooleanExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE_RESULT,false));

                    if (data.getBooleanExtra(KeyConstants.WIFI_LOCK_INFO_CHANGE_RESULT,false)) {
                        //图标+信息+点击事件
                        InfoDialog infoDialog = new InfoDialog.Builder(SwipchLinkActivity.this)
                                .setIcon(R.mipmap.set_right,this)
                                .setMessage(R.string.set_success)
                                .create();
                        infoDialog.show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        if (infoDialog != null) {
                                            infoDialog.dismiss();
                                        }
                                    }
                                }, 3000); //延迟3秒消失
                            }
                        });
                    } else  {
                        InfoDialog infoDialog = new InfoDialog.Builder(this)
                                .setIcon(R.mipmap.set_false,this)
                                .setMessage(R.string.set_failed_and_reset)
                                .create();
                        infoDialog.show();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        if (infoDialog != null) {
                                            infoDialog.dismiss();
                                        }
                                    }
                                }, 3000); //延迟3秒消失
                            }
                        });
                    }
            }
        }

    }

}