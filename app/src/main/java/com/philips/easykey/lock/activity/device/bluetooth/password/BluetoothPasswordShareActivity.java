package com.philips.easykey.lock.activity.device.bluetooth.password;

import android.app.AlertDialog;
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

import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseBleActivity;
import com.philips.easykey.lock.mvp.presenter.ble.PasswordDetailPresenter;
import com.philips.easykey.lock.mvp.view.IPasswordDetailView;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.LogUtils;
import com.philips.easykey.lock.utils.SharedUtil;
import com.blankj.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by David on 2019/4/17
 */
public class BluetoothPasswordShareActivity extends BaseBleActivity<IPasswordDetailView, PasswordDetailPresenter<IPasswordDetailView>>
        implements View.OnClickListener, IPasswordDetailView {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_short_message)
    TextView tvShortMessage;
    @BindView(R.id.tv_wei_xin)
    TextView tvWeiXin;
    @BindView(R.id.tv_copy)
    TextView tvCopy;
    @BindView(R.id.iv_editor)
    ImageView ivEditor;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    private int type;
    private BleLockInfo bleLockInfo;
    private String number;
    private String password;
    private String nickName;
    int timeCeLue;
    String shiXiao;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_password_share);
        ButterKnife.bind(this);
        bleLockInfo = mPresenter.getBleLockInfo();
        intent = getIntent();
        type = intent.getIntExtra(KeyConstants.TO_DETAIL_TYPE, 1);
        number = intent.getStringExtra(KeyConstants.TO_DETAIL_NUMBER);
        password = intent.getStringExtra(KeyConstants.TO_DETAIL_PASSWORD);
        nickName = intent.getStringExtra(KeyConstants.TO_DETAIL_NICKNAME);
        timeCeLue = intent.getIntExtra(KeyConstants.TIME_CE_LUE, 0);
        String pwd = "";
        if(password.toCharArray() != null){

            for (char c : password.toCharArray()) {
                pwd += " " + c;
            }
        }
        tvPassword.setText(pwd);
        ivBack.setOnClickListener(this);
        tvContent.setText(getString(R.string.password_detail));
        ivEditor.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        tvName.setText(nickName);
        tvTime.setText(" " + DateUtils.getStrFromMillisecond2(System.currentTimeMillis()));
        initData();
        tvShortMessage.setOnClickListener(this);
        tvWeiXin.setOnClickListener(this);
        tvCopy.setOnClickListener(this);
    }

    private void initData() {
        switch (timeCeLue) {
            case KeyConstants.YONG_JIU:
                shiXiao = getString(R.string.password_yong_jiu_valid);
                tvNumber.setText(shiXiao);
                break;
            case KeyConstants.ONE_DAY:
                shiXiao = getString(R.string.password_one_day_valid);
                tvNumber.setText(shiXiao);
                break;
            case KeyConstants.CUSTOM:
                long startTime = intent.getLongExtra(KeyConstants.CUSTOM_START_TIME, 0);
                long endTime = intent.getLongExtra(KeyConstants.CUSTOM_END_TIME, 0);
                String strStart = DateUtils.formatDetailTime(startTime);
                String strEnd = DateUtils.formatDetailTime(endTime);
                String content = getString(R.string.password_valid_shi_xiao) + "  " + strStart + "~" + strEnd;
                shiXiao = content;
                tvNumber.setText(shiXiao);
                break;
            case KeyConstants.PERIOD:
                strStart = intent.getStringExtra(KeyConstants.PERIOD_START_TIME);
                strEnd = intent.getStringExtra(KeyConstants.PERIOD_END_TIME);
                String weekRules = intent.getStringExtra(KeyConstants.WEEK_REPEAT_DATA);
                String strHint = String.format(getString(R.string.week_hint), weekRules,
                        strStart, strEnd);
                shiXiao = strHint;
                tvNumber.setText(shiXiao);
                break;
            case KeyConstants.TEMP:
                shiXiao = getString(R.string.password_once_valid);
                tvNumber.setText(shiXiao);
                break;
        }
    }

    @Override
    protected PasswordDetailPresenter<IPasswordDetailView> createPresent() {
        return new PasswordDetailPresenter<>();
    }

    @Override
    public void onClick(View v) {
        String message = String.format(getString(R.string.share_content), password,shiXiao);
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_editor:
                //弹出编辑框
                View mView = LayoutInflater.from(this).inflate(R.layout.have_edit_dialog, null);
                TextView tvTitle = mView.findViewById(R.id.tv_title);
                EditText editText = mView.findViewById(R.id.et_name);
                if (nickName!=null){
                    editText.setText(nickName);
                    editText.setSelection(nickName.length());
                }
                TextView tv_cancel = mView.findViewById(R.id.tv_left);
                TextView tv_query = mView.findViewById(R.id.tv_right);
                AlertDialog alertDialog = AlertDialogUtil.getInstance().common(this, mView);
                tvTitle.setText(getString(R.string.please_input_password_name));
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
                            ToastUtils.showShort(R.string.name_not_empty);
                            return;
                        }
                        if (name.equals(nickName)){
                            ToastUtils.showShort(R.string.nickname_not_modify);
                            return;
                        }
                        if (tvName != null) {
                            tvName.setText(name);
                            showLoading(getString(R.string.is_setting));
                            mPresenter.updateNick(type, number, name);
                        }
                        alertDialog.dismiss();
                    }
                });
                break;
            case R.id.btn_delete:
                AlertDialogUtil.getInstance().noEditTwoButtonDialog(this, getString(R.string.hint), getString(R.string.sure_delete_password), getString(R.string.cancel), getString(R.string.query), new AlertDialogUtil.ClickListener() {
                    @Override
                    public void left() {

                    }
                    @Override
                    public void right() {
                        if (mPresenter.isAuth(bleLockInfo, true)) {
                            showLoading(getString(R.string.is_deleting));
                            mPresenter.deletePwd(type, Integer.parseInt(number), 1, true);
                        }
                    }
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @Override
                    public void afterTextChanged(String toString) {
                    }
                });
                break;
            case R.id.tv_short_message:
                SharedUtil.getInstance().sendShortMessage(message,this);
                break;
            case R.id.tv_wei_xin:
                if (SharedUtil.isWeixinAvilible(this)){
                    SharedUtil.getInstance().sendWeiXin(message);
                }else {
                    ToastUtils.showShort(R.string.telephone_not_install_wechat);
                }

                break;
            case R.id.tv_copy:
                SharedUtil.getInstance().copyTextToSystem(this, message);
                break;
        }
    }

    @Override
    public void onDeletePwdSuccess() {

    }

    @Override
    public void onDeletePwdFailed(Throwable throwable) {
        ToastUtils.showShort(R.string.delete_fialed);
        hiddenLoading();
    }

    @Override
    public void onDeleteServerPwdSuccess() {
        Intent intent = new Intent();
        if (type == 1) {
            intent.setClass(this, BlePasswordManagerActivity.class);
        } else {
            //todo 跳转到临时密码管理页面
            intent.setClass(this, BlePasswordManagerActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void onDeleteServerPwdFailed(Throwable throwable) {
        LogUtils.d("删除失败  " + throwable.getMessage());
        ToastUtils.showShort(R.string.delete_fialed);
        hiddenLoading();
    }

    @Override
    public void onDeleteServerPwdFailedServer(BaseResult result) {
        LogUtils.d("删除失败  " + result.toString());
        ToastUtils.showShort(R.string.delete_fialed);
        hiddenLoading();
    }

    @Override
    public void updateNickNameSuccess(String nickName) {
        hiddenLoading();
        if (type == 1) {
            Intent intent = new Intent(this, BlePasswordManagerActivity.class);
            startActivity(intent);
        } else if (type == 2) {
            //todo 跳转到临时密码管理页面
            Intent intent = new Intent(this, BlePasswordManagerActivity.class);
            startActivity(intent);
        }
        hiddenLoading();
        ToastUtils.showLong(R.string.nickname_modify_success);
        finish();
    }

    @Override
    public void updateNickNameFailed(Throwable throwable) {
        hiddenLoading();
    }

    @Override
    public void updateNickNameFailedServer(BaseResult result) {
        hiddenLoading();
    }

    @Override
    public void onLockNoThisNumber() {

    }

    @Override
    public void onGetLockNumberFailed(Throwable throwable) {

    }
}
