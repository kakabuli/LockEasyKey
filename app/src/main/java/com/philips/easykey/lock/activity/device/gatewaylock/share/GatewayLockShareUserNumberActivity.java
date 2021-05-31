package com.philips.easykey.lock.activity.device.gatewaylock.share;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.gatewaypresenter.GatewayDeleteSharePresenter;
import com.philips.easykey.lock.mvp.view.gatewayView.IGatewayDeleteShareView;
import com.philips.easykey.lock.publiclibrary.mqtt.publishresultbean.DeviceShareUserResultBean;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;


/**
 * Created by David on 2019/2/20
 */
public class GatewayLockShareUserNumberActivity extends BaseActivity<IGatewayDeleteShareView, GatewayDeleteSharePresenter<IGatewayDeleteShareView>> implements IGatewayDeleteShareView,View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    TextView tvNumber;
    TextView tvName;
    ImageView ivEditor;
    TextView tvTime;
    Button btnDelete;
    String data;
    private DeviceShareUserResultBean.DataBean dataBean;
    private String gatewayId;
    private String deviceId;
    private String userName;
    private String uid;
    private Context context;
    private AlertDialog deleteDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_family_member_detail);
        context=this;

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        tvNumber = findViewById(R.id.tv_number);
        tvName = findViewById(R.id.tv_name);
        ivEditor = findViewById(R.id.iv_editor);
        tvTime = findViewById(R.id.tv_time);
        btnDelete = findViewById(R.id.btn_delete);

        initListener();
        initView();

    }

    @Override
    protected GatewayDeleteSharePresenter<IGatewayDeleteShareView> createPresent() {
        return new GatewayDeleteSharePresenter<>();
    }

    private void initView() {

        tvContent.setText(getString(R.string.philips_user_detail));
        Intent intent = getIntent();
        dataBean = (DeviceShareUserResultBean.DataBean) intent.getSerializableExtra(KeyConstants.GATEWAY_SHARE_USER);
        if (dataBean!=null) {
            tvNumber.setText(dataBean.getUsername());
            tvName.setText(dataBean.getUserNickname());
            gatewayId = intent.getStringExtra(KeyConstants.GATEWAY_ID);
            deviceId = intent.getStringExtra(KeyConstants.DEVICE_ID);
            uid = MyApplication.getInstance().getUid();
            userName = dataBean.getUsername();
            String getTime = dataBean.getTime();
            if (!TextUtils.isEmpty(getTime)&&getTime.length()>19){
                String  createTime= getTime.substring(0,19);
                String time=createTime.replace("-","/");
                tvTime.setText(time);
            }
        }
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        ivEditor.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_delete:
                //删除
                if (NetUtil.isNetworkAvailable()) {
                    AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, "", getString(R.string.sure_delete_user_permission), getString(R.string.philips_cancel), getString(R.string.delete), new AlertDialogUtil.ClickListener() {
                        @Override
                        public void left() {

                        }

                        @Override
                        public void right() {
                            mPresenter.deleteShareDevice(2,gatewayId,deviceId,uid,userName,data,0);
                            deleteDialog= AlertDialogUtil.getInstance().noButtonDialog(context,getString(R.string.delete_be_being));
                            deleteDialog.setCancelable(false);
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(String toString) {

                        }
                    });
                } else {
                    ToastUtils.showLong(R.string.network_exception);
                }
                break;
            case R.id.iv_editor:
                //弹出编辑框
                View mView = LayoutInflater.from(this).inflate(R.layout.have_edit_dialog, null);
                TextView tvTitle = mView.findViewById(R.id.tv_title);
                EditText editText = mView.findViewById(R.id.et_name);
                TextView tv_cancel = mView.findViewById(R.id.tv_left);
                TextView tv_query = mView.findViewById(R.id.tv_right);
                AlertDialog alertDialog = AlertDialogUtil.getInstance().common(this, mView);
                tvTitle.setText(getString(R.string.input_user_name));
                editText.setText(dataBean.getUserNickname());
                if (dataBean.getUserNickname() != null) {
                    editText.setSelection(dataBean.getUserNickname().length());
                }
                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                tv_query.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        data = editText.getText().toString().trim();
                        if (!StringUtil.nicknameJudge(data)) {
                            ToastUtils.showShort(R.string.nickname_verify_error);
                            return;
                        }
                        if (dataBean.getUserNickname().equals(data)) {
                            ToastUtils.showShort(getString(R.string.user_nickname_no_update));
                            return;
                        }
                        if (gatewayId!=null&&deviceId!=null){
                            //修改昵称
                            if (!TextUtils.isEmpty(gatewayId)&&!TextUtils.isEmpty(deviceId)&&!TextUtils.isEmpty(userName)){
                                mPresenter.updateShareNameDevice(2,gatewayId,deviceId,uid,userName,data,1);
                            }
                        }
                        alertDialog.dismiss();
                    }
                });


                break;
        }
    }




    @Override
    public void deleteShareUserSuccess() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
       finish();
    }

    @Override
    public void deleteShareUserFail() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        AlertDialogUtil.getInstance().noButtonDialog(context,getString(R.string.delete_fialed));
    }

    @Override
    public void deleteShareUserThrowable() {
        if (deleteDialog!=null){
            deleteDialog.dismiss();
        }
        AlertDialogUtil.getInstance().noButtonDialog(context,getString(R.string.delete_fialed));
    }

    @Override
    public void updateShareUserNameSuccess(String name) {
        if (tvName!=null){
            tvName.setText(name);
        }
        if (dataBean!=null){
            dataBean.setUserNickname(name);
        }
        ToastUtils.showShort(getString(R.string.update_nick_name));
    }

    @Override
    public void updateShareUserNameFail() {
        ToastUtils.showShort(getString(R.string.update_nickname_fail));
    }

    @Override
    public void updaateShareUserNameThrowable() {
        ToastUtils.showShort(getString(R.string.update_nickname_fail));
    }
}
