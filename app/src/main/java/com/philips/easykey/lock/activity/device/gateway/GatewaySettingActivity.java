package com.philips.easykey.lock.activity.device.gateway;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.activity.MainActivity;
import com.philips.easykey.lock.adapter.GatewaySettingAdapter;
import com.philips.easykey.lock.bean.GatewaySettingItemBean;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.gatewaypresenter.GatewaySettingPresenter;
import com.philips.easykey.lock.mvp.view.gatewayView.GatewaySettingView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.GetNetBasicBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishbean.GetZbChannelBean;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.GwWiFiBaseInfo;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.EditTextWatcher;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LoadingDialog;
import com.philips.easykey.lock.utils.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.utils.greenDao.bean.GatewayBaseInfo;
import com.philips.easykey.lock.utils.greenDao.db.DaoSession;
import com.philips.easykey.lock.utils.greenDao.db.GatewayBaseInfoDao;
import com.philips.easykey.lock.utils.greenDao.db.GatewayLockServiceInfoDao;
import com.philips.easykey.lock.utils.greenDao.db.GatewayServiceInfoDao;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GatewaySettingActivity extends BaseActivity<GatewaySettingView, GatewaySettingPresenter<GatewaySettingView>> implements GatewaySettingView {

    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    private List<GatewaySettingItemBean> gatewaySettingItemBeans = new ArrayList<>();
    private GatewaySettingAdapter gatewaySettingAdapter;
    private LoadingDialog loadingDialog;
    private String gatewayId;
    private String encryption;
    private String wifiName;
    private String wifiPwd;
    private String networkLan;
    private String networkMask;
    private String zbChannel;
    private GatewayBaseInfo gatewayBaseInfo=new GatewayBaseInfo();
    private String uid;
    private AlertDialog deleteDialog;
    private Context context;
    private int isAdmin;
    private String gatewayNickName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gateway_setting);
        ButterKnife.bind(this);
        context=this;
        initView();
        initRecycler();
        initData();
        initGatewayData();
    }

    private void initView() {
        if (loadingDialog==null){
            loadingDialog=LoadingDialog.getInstance(this);
        }
    }

    String model=null;
    private void initData() {
        Intent intent=getIntent();
        gatewayId=intent.getStringExtra(KeyConstants.GATEWAY_ID);
        isAdmin=intent.getIntExtra(KeyConstants.IS_ADMIN,0);
        gatewayNickName= intent.getStringExtra(KeyConstants.GATEWAY_NICKNAME);
        model= intent.getStringExtra(KeyConstants.GW_MODEL);

        GatewaySettingItemBean gatewaySettingItemBeanNewOne=new GatewaySettingItemBean();
        gatewaySettingItemBeanNewOne.setTitle(getString(R.string.gateway_setting_name));
    //    if (isAdmin==1){
            gatewaySettingItemBeanNewOne.setSetting(true);
    //    }else{
    //        gatewaySettingItemBeanNewOne.setSetting(false);
    //    }

        GatewaySettingItemBean gatewaySettingItemBeanOne=new GatewaySettingItemBean();
        gatewaySettingItemBeanOne.setTitle(getString(R.string.gateway_setting));
        gatewaySettingItemBeanOne.setSetting(false);

        GatewaySettingItemBean gatewaySettingItemBeanTwo=new GatewaySettingItemBean();
        gatewaySettingItemBeanTwo.setTitle(getString(R.string.gateway_setting_firmware_version));
        gatewaySettingItemBeanTwo.setSetting(false);

        GatewaySettingItemBean gatewaySettingItemBeanThree=new GatewaySettingItemBean();
        if (model != null) {
            if (model.equals(KeyConstants.SMALL_GW2)) {
                gatewaySettingItemBeanThree.setTitle(getString(R.string.gateway_coordinator_channel));
            } else {
                gatewaySettingItemBeanThree.setTitle(getString(R.string.gateway_setting_wifi_name));
            }
        }else {
            ToastUtils.showShort(R.string.gateway_confirm_version);
            return;
        }
        if (isAdmin==1){
            gatewaySettingItemBeanThree.setSetting(false);
        }else{
            gatewaySettingItemBeanThree.setSetting(false);
        }
        GatewaySettingItemBean gatewaySettingItemBeanFour=new GatewaySettingItemBean();
            gatewaySettingItemBeanFour.setTitle(getString(R.string.gateway_setting_wifi_pwd));


        if (isAdmin==1){
            gatewaySettingItemBeanFour.setSetting(false);
        }else{
            gatewaySettingItemBeanFour.setSetting(false);
        }



        GatewaySettingItemBean gatewaySettingItemBeanFive=new GatewaySettingItemBean();
        gatewaySettingItemBeanFive.setTitle(getString(R.string.gateway_setting_lan_ip));
        gatewaySettingItemBeanFive.setSetting(false);

        GatewaySettingItemBean gatewaySettingItemBeanSix=new GatewaySettingItemBean();
        gatewaySettingItemBeanSix.setTitle(getString(R.string.gateway_setting_wan_ip));
        gatewaySettingItemBeanSix.setSetting(false);


        GatewaySettingItemBean gatewaySettingItemBeanSeven=new GatewaySettingItemBean();
        gatewaySettingItemBeanSeven.setTitle(getString(R.string.gateway_setting_lan_subnet_mask));
        gatewaySettingItemBeanSeven.setSetting(false);

        GatewaySettingItemBean gatewaySettingItemBeanEight=new GatewaySettingItemBean();
        gatewaySettingItemBeanEight.setTitle(getString(R.string.gateway_setting_wan_subnet_mask));
        gatewaySettingItemBeanEight.setSetting(false);

        GatewaySettingItemBean gatewaySettingItemBeanNight=new GatewaySettingItemBean();
        gatewaySettingItemBeanNight.setTitle(getString(R.string.gateway_wan_access_mode));
        gatewaySettingItemBeanNight.setSetting(false);

        GatewaySettingItemBean gatewaySettingItemBeanTen=new GatewaySettingItemBean();
        gatewaySettingItemBeanTen.setTitle(getString(R.string.gateway_coordinator_channel));
        if (isAdmin==1){
            gatewaySettingItemBeanTen.setSetting(false);
        }else{
            gatewaySettingItemBeanTen.setSetting(false);
        }

        gatewaySettingItemBeans.add(gatewaySettingItemBeanNewOne);
        gatewaySettingItemBeans.add(gatewaySettingItemBeanOne);
        gatewaySettingItemBeans.add(gatewaySettingItemBeanTwo);
        if( (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW)) ){

        }
        else if((!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW2))){
            gatewaySettingItemBeans.add(gatewaySettingItemBeanThree);
        }

        else{
            gatewaySettingItemBeans.add(gatewaySettingItemBeanThree);
            gatewaySettingItemBeans.add(gatewaySettingItemBeanFour);
            gatewaySettingItemBeans.add(gatewaySettingItemBeanFive);
            gatewaySettingItemBeans.add(gatewaySettingItemBeanSix);
            gatewaySettingItemBeans.add(gatewaySettingItemBeanSeven);
            gatewaySettingItemBeans.add(gatewaySettingItemBeanEight);
            gatewaySettingItemBeans.add(gatewaySettingItemBeanNight);
            gatewaySettingItemBeans.add(gatewaySettingItemBeanTen);
        }

        if (gatewaySettingAdapter!=null){
            gatewaySettingAdapter.notifyDataSetChanged();
        }

    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this,LinearLayoutManager.VERTICAL);
        recycler.addItemDecoration(dividerItemDecoration);
        gatewaySettingAdapter=new GatewaySettingAdapter(gatewaySettingItemBeans);
        recycler.setAdapter(gatewaySettingAdapter);
        gatewaySettingAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                // if (isAdmin == 1) {
                switch (position) {
                    case 0:
                        //设备名字
                        View mUpdateView = LayoutInflater.from(GatewaySettingActivity.this).inflate(R.layout.have_edit_dialog, null);
                        TextView tvUpdateTitle = mUpdateView.findViewById(R.id.tv_title);
                        EditText editUpdateText = mUpdateView.findViewById(R.id.et_name);
                        TextView tv_updatecancel = mUpdateView.findViewById(R.id.tv_left);
                        TextView tv_updatequery = mUpdateView.findViewById(R.id.tv_right);
                        AlertDialog alertUpdateDialog = AlertDialogUtil.getInstance().common(GatewaySettingActivity.this, mUpdateView);
                        tvUpdateTitle.setText(getString(R.string.input_device_name));
                        //获取到设备名称设置
                        editUpdateText.setText(gatewayNickName);
                        editUpdateText.setSelection(gatewayNickName.length());
                        editUpdateText.addTextChangedListener(new EditTextWatcher(GatewaySettingActivity.this,null,editUpdateText,50));
                        tv_updatecancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertUpdateDialog.dismiss();
                            }
                        });
                        tv_updatequery.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String nickname = editUpdateText.getText().toString().trim();
                                //todo 判断名称是否修改
                                if (TextUtils.isEmpty(nickname)){
                                    ToastUtils.showShort(getString(R.string.device_name_cannot_be_empty));
                                    return;
                                }

                                if (gatewayNickName != null) {
                                    if (gatewayNickName.equals(nickname)) {
                                        ToastUtils.showShort(getString(R.string.device_nick_name_no_update));
                                        alertUpdateDialog.dismiss();
                                        return;
                                    }
                                }
                                if (gatewayId != null) {
                                    mPresenter.updateGatewayName(gatewayId, MyApplication.getInstance().getUid(), nickname);
                                }
                                alertUpdateDialog.dismiss();
                            }
                        });


                        break;

               /* case 3:
                    //wifi名称
                    View mView = LayoutInflater.from(this).inflate(R.layout.have_one_et_dialog, null);
                    TextView tvTitle = mView.findViewById(R.id.tv_title);
                    TextView tvContent = mView.findViewById(R.id.tv_content);
                    EditText editText = mView.findViewById(R.id.et_name);
                    editText.setText(wifiName);
                    editText.setHint(getString(R.string.please_input_wifi_name));
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(32)});
                    if (wifiName != null) {
                        editText.setSelection(wifiName.length());
                    }
                    TextView tv_cancel = mView.findViewById(R.id.tv_left);
                    TextView tv_query = mView.findViewById(R.id.tv_right);
                    AlertDialog alertDialog = AlertDialogUtil.getInstance().common(this, mView);
                    tvTitle.setText(getString(R.string.update_wifi_name));
                    tvContent.setText(getString(R.string.update_wifi_name_need_rebind_cateye));
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
                            if (TextUtils.isEmpty(name)) {
                                ToastUtils.showShort(R.string.wifi_name_not_null);
                                return;
                            } else if (wifiName.equals(name)) {
                                ToastUtils.showShort(R.string.wifi_name_no_update);
                                return;
                            } else {
                                mPresenter.setWiFi(MyApplication.getInstance().getUid(), gatewayId, gatewayId, encryption, name, wifiPwd);
                            }
                            alertDialog.dismiss();
                        }
                    });
                    break;*/
               /* case 4:
                    //wifi密码
                    View mViewPwd = LayoutInflater.from(this).inflate(R.layout.have_one_et_dialog, null);
                    TextView tvTitlePwd = mViewPwd.findViewById(R.id.tv_title);
                    TextView tvContentPwd = mViewPwd.findViewById(R.id.tv_content);
                    EditText editTextPwd = mViewPwd.findViewById(R.id.et_name);
                    editTextPwd.setText(wifiPwd);
                    editTextPwd.setHint(getString(R.string.please_input_wifi_pwd));
                    editTextPwd.addTextChangedListener(new EditTextWatcher(this, null, editTextPwd, 63));
                    editTextPwd.setFilters(new InputFilter[]{new InputFilter.LengthFilter(63)});
                    if (wifiPwd != null) {
                        editTextPwd.setSelection(wifiPwd.length());
                    }
                    TextView tv_left = mViewPwd.findViewById(R.id.tv_left);
                    TextView tv_right = mViewPwd.findViewById(R.id.tv_right);
                    AlertDialog alertDialogPwd = AlertDialogUtil.getInstance().common(this, mViewPwd);
                    tvTitlePwd.setText(getString(R.string.update_wifi_pwd));
                    tvContentPwd.setText(getString(R.string.update_wifi_pwd_need_rebind_cateye));
                    tv_left.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogPwd.dismiss();
                        }
                    });
                    tv_right.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String pwd = editTextPwd.getText().toString().trim();

                            if (TextUtils.isEmpty(pwd)) {
                                ToastUtils.showShort(R.string.wifi_pwd_not_null);
                                return;
                            } else if (wifiPwd.equals(pwd)) {
                                ToastUtils.showShort(R.string.wifi_pwd_no_update);
                                return;
                            } else if (pwd.length() < 8) {
                                ToastUtils.showShort(R.string.wifi_password_length);
                                return;
                            } else if (pwd.length() > 63) {
                                ToastUtils.showShort(R.string.wifi_password_length);
                            } else {
                                mPresenter.setWiFi(MyApplication.getInstance().getUid(), gatewayId, gatewayId, encryption, wifiName, pwd);
                            }
                            alertDialogPwd.dismiss();
                        }
                    });
                    break;*/
           /* case 4:
            case 6:
                //配置局域网
                View mPairLan = LayoutInflater.from(this).inflate(R.layout.have_two_et_dialog, null);
                TextView tvTitleLan = mPairLan.findViewById(R.id.tv_title);
                EditText editTextOne = mPairLan.findViewById(R.id.et_one);
                editTextOne.setText(networkLan);
                editTextOne.setHint(R.string.input_lan_ip);
                if (!TextUtils.isEmpty(networkLan)){
                    editTextOne.setSelection(networkLan.length());
                }
                EditText editTextTwo=mPairLan.findViewById(R.id.et_two);
                editTextTwo.setHint(R.string.input_lan_mask);
                if (!TextUtils.isEmpty(networkMask)){
                    editTextTwo.setText(networkMask);
                    editTextTwo.setSelection(networkMask.length());
                }
                TextView tv_lan_left= mPairLan.findViewById(R.id.tv_left);
                TextView tv_lan_right= mPairLan.findViewById(R.id.tv_right);
                AlertDialog alertDialogLan = AlertDialogUtil.getInstance().common(this, mPairLan);
                tvTitleLan.setText(getString(R.string.pair_lan_mask));
                tv_lan_left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialogLan.dismiss();
                    }
                });
                tv_lan_right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String ip=editTextOne.getText().toString().trim();
                        String mask = editTextTwo.getText().toString().trim();

                        if (TextUtils.isEmpty(ip)){
                            ToastUtils.showShort(R.string.lan_ip_not_null);
                            return;
                        }else if (!StringUtil.isInner(ip)){
                            ToastUtils.showShort(R.string.input_right_lan_ip);
                            return;
                        }else if (TextUtils.isEmpty(mask)){
                           ToastUtils.showShort(R.string.lan_mask_not_null);
                           return;
                        }
                        if (!TextUtils.isEmpty(gatewayId)){
                            mPresenter.setNetBasic(MyApplication.getInstance().getUid(),gatewayId,gatewayId,ip,mask);
                        }
                        alertDialogLan.dismiss();
                    }
                });
                break;*/
                /*case 10:
                    //配置
                    View mViewChannel = LayoutInflater.from(this).inflate(R.layout.have_one_et_dialog, null);
                    TextView tvTitleChannel = mViewChannel.findViewById(R.id.tv_title);
                    TextView tvContentChannel = mViewChannel.findViewById(R.id.tv_content);
                    EditText editTextChannel = mViewChannel.findViewById(R.id.et_name);
                    editTextChannel.setText(zbChannel);
                    editTextChannel.setHint(getString(R.string.range_channel));
                    editTextChannel.setInputType(InputType.TYPE_CLASS_NUMBER);
                    editTextChannel.setMaxEms(2);
                    if (zbChannel != null) {
                        editTextChannel.setSelection(zbChannel.length());
                    }
                    TextView tv_left_channel = mViewChannel.findViewById(R.id.tv_left);
                    TextView tv_right_channel = mViewChannel.findViewById(R.id.tv_right);
                    AlertDialog alertDialogChannel = AlertDialogUtil.getInstance().common(this, mViewChannel);
                    tvTitleChannel.setText(getString(R.string.setting_channel));
                    tvContentChannel.setText(getString(R.string.setting_channel_need_bind_lock));
                    tv_left_channel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialogChannel.dismiss();
                        }
                    });
                    tv_right_channel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String channel = editTextChannel.getText().toString().trim();
                            int channelInt = 0;
                            if (!TextUtils.isEmpty(channel)) {
                                channelInt = Integer.parseInt(channel);
                            }
                            if (TextUtils.isEmpty(channel)) {
                                ToastUtils.showShort(R.string.channel_not_null);
                                return;
                            } else if (zbChannel.equals(channel)) {
                                ToastUtils.showShort(R.string.channel_not_update);
                                return;
                            } else if (channelInt < 11) {
                                ToastUtils.showShort(R.string.range_channel);
                                return;
                            } else if (channelInt > 26) {
                                ToastUtils.showShort(R.string.range_channel);
                                return;
                            } else {
                                mPresenter.setZbChannel(MyApplication.getInstance().getUid(), gatewayId, gatewayId, channel);
                            }
                            alertDialogChannel.dismiss();
                        }
                    });
                    break;
*/
                }
                // }
            }
        });
    }

    private void initGatewayData() {

        uid=MyApplication.getInstance().getUid();
        if (!TextUtils.isEmpty(gatewayId)){
            //先读取数据库
            GatewayBaseInfo gatewayBaseInfo=MyApplication.getInstance().getDaoWriteSession().getGatewayBaseInfoDao().queryBuilder().where(GatewayBaseInfoDao.Properties.GatewayId.eq(gatewayId), GatewayBaseInfoDao.Properties.Uid.eq(uid)).unique();
            if (gatewayBaseInfo!=null){
                setGatewayBaseInfo(gatewayBaseInfo);
            }
            mPresenter.getNetBasic(MyApplication.getInstance().getUid(),gatewayId,gatewayId);
            loadingDialog.show(getString(R.string.get_gateway_info_waitting));
        }

    }

    private void setGatewayBaseInfo(GatewayBaseInfo gatewayBaseInfo) {
        if (gatewaySettingItemBeans!=null&&gatewaySettingItemBeans.size()>0){

            if (TextUtils.isEmpty(gatewayNickName)){
                gatewaySettingItemBeans.get(0).setContent(gatewayBaseInfo.getGatewayName());
            }else{
                //网关名称
                gatewaySettingItemBeans.get(0).setContent(gatewayNickName);
            }
            //网关
            gatewaySettingItemBeans.get(1).setContent(gatewayBaseInfo.getGatewayId());
            //固件版本号
            gatewaySettingItemBeans.get(2).setContent(gatewayBaseInfo.getSW());
            if( (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW)) || (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW2)) ){

            }else {
                //局域网ip
                gatewaySettingItemBeans.get(5).setContent(gatewayBaseInfo.getLanIp());
                //广域网ip
                gatewaySettingItemBeans.get(6).setContent(gatewayBaseInfo.getWanIp());
                //局域网子网掩码
                gatewaySettingItemBeans.get(7).setContent(gatewayBaseInfo.getLanNetmask());
                //广域网子网掩码
                gatewaySettingItemBeans.get(8).setContent(gatewayBaseInfo.getWanNetmask());
                //网关广域网接入方式
                gatewaySettingItemBeans.get(9).setContent(gatewayBaseInfo.getWanType());
                gatewaySettingItemBeans.get(3).setContent(gatewayBaseInfo.getSsid());
                gatewaySettingItemBeans.get(4).setContent(gatewayBaseInfo.getPwd());
                gatewaySettingItemBeans.get(10).setContent(gatewayBaseInfo.getChannel());
            }

            if (gatewaySettingAdapter!=null){
                gatewaySettingAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    protected GatewaySettingPresenter<GatewaySettingView> createPresent() {
        return new GatewaySettingPresenter<>();
    }


    @OnClick({R.id.back, R.id.btn_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.btn_delete:
                    AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.device_delete_dialog_head), getString(R.string.device_delete_gateway_dialog_content), getString(R.string.philips_cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
                        @Override
                        public void left() {

                        }

                        @Override
                        public void right() {
                            if (gatewayId != null) {
                                mPresenter.unBindGateway(MyApplication.getInstance().getUid(), gatewayId);//正常解绑
                                //mPresenter.testUnbindGateway(MyApplication.getInstance().getUid(),gatewayId,gatewayId); //测试解绑
                                deleteDialog = AlertDialogUtil.getInstance().noButtonDialog(context, getString(R.string.delete_be_being));
                                deleteDialog.setCancelable(false);
                            }

                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(String toString) {

                        }
                    });
                /*else{
                    //取消分享
                    AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.device_delete_dialog_head), getString(R.string.device_delete_gateway_dialog_content), getString(R.string.cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
                        @Override
                        public void left() {

                        }
                        @Override
                        public void right() {
                            if (gatewayId != null) {
                                String phone= (String) SPUtils.get(SPUtils.PHONEN,"");
                                if (!TextUtils.isEmpty(phone)&&!TextUtils.isEmpty(gatewayId)&&!TextUtils.isEmpty(uid)){
                                    mPresenter.deleteShareDevice(1,gatewayId,"",uid,"86"+phone,"",2);//正常解绑
                                    deleteDialog = AlertDialogUtil.getInstance().noButtonDialog(context, getString(R.string.delete_be_being));
                                    deleteDialog.setCancelable(false);
                                }

                            }

                        }
                    });





                }*/


                break;
        }
    }

    @Override
    public void getNetBasicSuccess(GetNetBasicBean basicBean) {
        if( (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW))){  // 小网关

        }
        else if((!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW2)) ){
            mPresenter.getZbChannel(MyApplication.getInstance().getUid(),gatewayId,gatewayId);

        }
        else{
            mPresenter.getGatewayWifiPwd(gatewayId);
        }

        //获取到

        if (gatewaySettingItemBeans!=null&&gatewaySettingItemBeans.size()>0){
           GetNetBasicBean.ReturnDataBean returnDataBean= basicBean.getReturnData();
            //网关名称
             gatewaySettingItemBeans.get(0).setContent(gatewayNickName);
            //网关
            gatewaySettingItemBeans.get(1).setContent(basicBean.getGwId());
            //固件版本号
            gatewaySettingItemBeans.get(2).setContent(returnDataBean.getSW());
            if( (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW)) || (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW2)) ){
                if (loadingDialog!=null){
                    loadingDialog.dismiss();
                }
//                GatewayBaseInfo gatewayBaseInfo1=new GatewayBaseInfo();
//                gatewayBaseInfo.setGatewayName(gatewayNickName);
//                gatewayBaseInfo.setGatewayId(basicBean.getGwId());
//                gatewayBaseInfo.setSW(returnDataBean.getSW());
//                MyApplication.getInstance().getDaoWriteSession().insertOrReplace(gatewayBaseInfo1);

            }else {
                //局域网ip
                gatewaySettingItemBeans.get(5).setContent(returnDataBean.getLanIp());
                networkLan=returnDataBean.getLanIp();
                //广域网ip
                gatewaySettingItemBeans.get(6).setContent(returnDataBean.getWanIp());
                //局域网子网掩码
                gatewaySettingItemBeans.get(7).setContent(returnDataBean.getLanNetmask());
                networkMask=returnDataBean.getLanNetmask();
                //广域网子网掩码
                gatewaySettingItemBeans.get(8).setContent(returnDataBean.getWanNetmask());
                //网关广域网接入方式
                gatewaySettingItemBeans.get(9).setContent(returnDataBean.getWanType());
                gatewayBaseInfo.setLanIp(returnDataBean.getLanIp());
                gatewayBaseInfo.setWanIp(returnDataBean.getWanIp());
                gatewayBaseInfo.setLanNetmask(returnDataBean.getLanNetmask());
                gatewayBaseInfo.setWanNetmask(returnDataBean.getWanNetmask());
                gatewayBaseInfo.setWanType(returnDataBean.getWanType());

            }

            gatewayBaseInfo.setUid(uid);
            gatewayBaseInfo.setGatewayId(basicBean.getGwId());
            gatewayBaseInfo.setDeviceIdUid(basicBean.getGwId()+uid);
            gatewayBaseInfo.setSW(returnDataBean.getSW());

            if( (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW)) || (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW2)) ){
                MyApplication.getInstance().getDaoWriteSession().insertOrReplace(gatewayBaseInfo);
            }else {

            }
        }
        if (gatewaySettingAdapter!=null){
            gatewaySettingAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void getNetBasicFail() {
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
        ToastUtils.showShort(R.string.get_net_basic_fail);
    }

    @Override
    public void getNetBasicThrowable(Throwable throwable) {
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
        ToastUtils.showShort(R.string.get_net_basic_fail);
    }

    @Override
    public void onGetWifiInfoSuccess(GwWiFiBaseInfo wiFiBaseInfo) {
        mPresenter.getZbChannel(MyApplication.getInstance().getUid(),gatewayId,gatewayId);
        if (gatewaySettingItemBeans!=null&&gatewaySettingItemBeans.size()>0){
            GwWiFiBaseInfo.ReturnDataBean gwWiFiBaseInfo=wiFiBaseInfo.getReturnData();
            gatewaySettingItemBeans.get(3).setContent(gwWiFiBaseInfo.getSsid());
            gatewaySettingItemBeans.get(4).setContent(gwWiFiBaseInfo.getPwd());
            encryption=gwWiFiBaseInfo.getEncryption();
            wifiName=gwWiFiBaseInfo.getSsid();
            wifiPwd=gwWiFiBaseInfo.getPwd();
            gatewayBaseInfo.setEncryption(encryption);
            gatewayBaseInfo.setSsid(wifiName);
            gatewayBaseInfo.setPwd(wifiPwd);
        }
        if (gatewaySettingAdapter!=null){
            gatewaySettingAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onGetWifiInfoFailed() {
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
        if( (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW)) || (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW2)) ){  // 小网关

        }else{
            ToastUtils.showShort(R.string.get_wifi_info_fail);
        }

    }

    @Override
    public void onGetWifiInfoThrowable(Throwable throwable) {
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
        if( (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW)) || (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW2)) ){  // 小网关

        }else{
            ToastUtils.showShort(R.string.get_wifi_info_fail);
        }
    }

    @Override
    public void getZbChannelSuccess(GetZbChannelBean getZbChannelBean) {
        LogUtils.d("getZbChannel----="+getZbChannelBean.getReturnData().getChannel());

        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
        if (gatewaySettingItemBeans!=null&&gatewaySettingItemBeans.size()>0){
            if( (!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW)) ){

            }
            else if((!TextUtils.isEmpty(model) && model.equals(KeyConstants.SMALL_GW2))){
                gatewaySettingItemBeans.get(3).setContent(getZbChannelBean.getReturnData().getChannel());
                zbChannel = getZbChannelBean.getReturnData().getChannel();
                gatewayBaseInfo.setChannel(zbChannel);
            }
            else {
                gatewaySettingItemBeans.get(10).setContent(getZbChannelBean.getReturnData().getChannel());
                zbChannel = getZbChannelBean.getReturnData().getChannel();
                gatewayBaseInfo.setChannel(zbChannel);
            }
        }
        if (gatewaySettingAdapter!=null){
            gatewaySettingAdapter.notifyDataSetChanged();
        }
        MyApplication.getInstance().getDaoWriteSession().insertOrReplace(gatewayBaseInfo);

    }

    @Override
    public void getZbChannelFail() {
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
        ToastUtils.showShort(R.string.get_zb_channel_fail);
    }

    @Override
    public void getZbChannelThrowable(Throwable throwable) {
        if (loadingDialog!=null){
            loadingDialog.dismiss();
        }
        LogUtils.d(throwable.getMessage());
        ToastUtils.showShort(throwable.getMessage());
    }

    @Override
    public void unbindGatewaySuccess() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        //清除数据库
        DaoSession daoSession=MyApplication.getInstance().getDaoWriteSession();
        daoSession.getGatewayBaseInfoDao().deleteByKey(gatewayId+uid);
        daoSession.getGatewayServiceInfoDao().queryBuilder().where(GatewayServiceInfoDao.Properties.Uid.eq(uid)).buildDelete().executeDeleteWithoutDetachingEntities();//网关
        daoSession.getGatewayLockServiceInfoDao().queryBuilder().where(GatewayLockServiceInfoDao.Properties.Uid.eq(uid)).buildDelete().executeDeleteWithoutDetachingEntities();//网关锁

        //解绑成功
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void unbindGatewayFail() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        ToastUtils.showShort(getString(R.string.delete_fialed));
    }

    @Override
    public void unbindGatewayThrowable(Throwable throwable) {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        ToastUtils.showShort(getString(R.string.delete_fialed_error));
    }

    @Override
    public void setWifiSuccess(String name,String pwd) {
        if (gatewaySettingItemBeans!=null&&gatewaySettingItemBeans.size()>0){
            gatewaySettingItemBeans.get(3).setContent(name);
            gatewaySettingItemBeans.get(4).setContent(pwd);
            wifiName=name;
            wifiPwd=pwd;
        }
        if (gatewaySettingAdapter!=null){
            gatewaySettingAdapter.notifyDataSetChanged();
        }
        ToastUtils.showShort(getString(R.string.set_success));
    }

    @Override
    public void setWifiFail() {
        ToastUtils.showShort(getString(R.string.set_failed));
    }

    @Override
    public void setWifiThrowable(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.set_failed));
    }

    @Override
    public void setNetLanSuccess(String ip,String mask) {
        if (gatewaySettingItemBeans!=null&&gatewaySettingItemBeans.size()>0){
            gatewaySettingItemBeans.get(5).setContent(ip);
            gatewaySettingItemBeans.get(7).setContent(mask);
            networkLan=ip;
            networkMask=mask;
        }
        if (gatewaySettingAdapter!=null){
            gatewaySettingAdapter.notifyDataSetChanged();
        }
        ToastUtils.showShort(getString(R.string.set_success));

    }

    @Override
    public void setNetLanFail() {
        ToastUtils.showShort(getString(R.string.set_failed));
    }

    @Override
    public void setNetLanThrowable(Throwable throwable) {
        ToastUtils.showShort(getString(R.string.set_failed));
    }

    @Override
    public void setZbChannelSuccess(String channel) {
        if (gatewaySettingItemBeans!=null&&gatewaySettingItemBeans.size()>0){
            gatewaySettingItemBeans.get(10).setContent(channel);
            zbChannel=channel;
        }
        if (gatewaySettingAdapter!=null){
            gatewaySettingAdapter.notifyDataSetChanged();
        }
        ToastUtils.showShort(R.string.set_success);
    }

    @Override
    public void setZbChannelFail() {
        ToastUtils.showShort(R.string.set_failed);
    }

    @Override
    public void setZbChannelThrowable(Throwable throwable) {
        ToastUtils.showShort(R.string.set_failed);
    }

    @Override
    public void unbindTestGatewaySuccess() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        //清除数据库
        DaoSession daoSession=MyApplication.getInstance().getDaoWriteSession();
        daoSession.getGatewayBaseInfoDao().deleteByKey(gatewayId+uid);
        daoSession.getGatewayServiceInfoDao().queryBuilder().where(GatewayServiceInfoDao.Properties.Uid.eq(uid)).buildDelete().executeDeleteWithoutDetachingEntities();//网关
        daoSession.getGatewayLockServiceInfoDao().queryBuilder().where(GatewayLockServiceInfoDao.Properties.Uid.eq(uid)).buildDelete().executeDeleteWithoutDetachingEntities();//网关锁

        //解绑成功
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void unbindTestGatewayFail() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        ToastUtils.showShort(getString(R.string.delete_fialed));
    }

    @Override
    public void unbindTestGatewayThrowable(Throwable throwable) {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        ToastUtils.showShort(getString(R.string.delete_fialed));
    }

    @Override
    public void deleteShareUserSuccess() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        //清除数据库
        DaoSession daoSession=MyApplication.getInstance().getDaoWriteSession();
        daoSession.getGatewayBaseInfoDao().deleteByKey(gatewayId+uid);
        daoSession.getGatewayServiceInfoDao().queryBuilder().where(GatewayServiceInfoDao.Properties.Uid.eq(uid)).buildDelete().executeDeleteWithoutDetachingEntities();//网关
        daoSession.getGatewayLockServiceInfoDao().queryBuilder().where(GatewayLockServiceInfoDao.Properties.Uid.eq(uid)).buildDelete().executeDeleteWithoutDetachingEntities();//网关锁

        //解绑成功
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void deleteShareUserFail() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        ToastUtils.showShort(getString(R.string.delete_fialed));
    }

    @Override
    public void deleteShareUserThrowable() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        ToastUtils.showShort(getString(R.string.delete_fialed));
    }

    @Override
    public void updateDevNickNameSuccess(String nickName) {
        if (gatewaySettingItemBeans!=null&&gatewaySettingItemBeans.size()>0){
            gatewaySettingItemBeans.get(0).setContent(nickName);
            gatewayNickName=nickName;
        }
        if (gatewaySettingAdapter!=null){
            gatewaySettingAdapter.notifyDataSetChanged();
        }
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra(KeyConstants.GATEWAY_NICKNAME, nickName);
        //设置返回数据
        GatewaySettingActivity.this.setResult(RESULT_OK, intent);
        ToastUtils.showShort(R.string.set_success);
    }

    @Override
    public void updateDevNickNameFail() {
        ToastUtils.showShort(R.string.set_failed);
    }

    @Override
    public void updateDevNickNameThrowable(Throwable throwable) {
        ToastUtils.showShort(R.string.set_failed);
    }
}
