package com.philips.easykey.lock.activity.device.bluetooth;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.easykey.lock.MyApplication;
import com.philips.easykey.lock.R;
import com.philips.easykey.lock.mvp.mvpbase.BaseActivity;
import com.philips.easykey.lock.mvp.presenter.ble.FamilyMemberDetailPresenter;
import com.philips.easykey.lock.publiclibrary.bean.BleLockInfo;
import com.philips.easykey.lock.publiclibrary.http.result.BaseResult;
import com.philips.easykey.lock.publiclibrary.http.result.BluetoothSharedDeviceBean;
import com.philips.easykey.lock.publiclibrary.http.util.HttpUtils;
import com.philips.easykey.lock.utils.AlertDialogUtil;
import com.philips.easykey.lock.utils.DateUtils;
import com.philips.easykey.lock.utils.KeyConstants;
import com.philips.easykey.lock.utils.NetUtil;
import com.philips.easykey.lock.utils.StringUtil;
import com.blankj.utilcode.util.ToastUtils;
import com.philips.easykey.lock.mvp.view.IFamilyMemberDeatilView;


/**
 * Created by David on 2019/2/20
 */
public class FamilyMemberDetailActivity extends BaseActivity<IFamilyMemberDeatilView, FamilyMemberDetailPresenter<IFamilyMemberDeatilView>> implements IFamilyMemberDeatilView, View.OnClickListener {

    ImageView ivBack;
    TextView tvContent;
    TextView tvNumber;
    TextView tvName;
    ImageView ivEditor;
    TextView tvTime;
    BluetoothSharedDeviceBean.DataBean dataBean;
    BleLockInfo bleLockInfo;
    Button btnDelete;
    private String nickname;
    String data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.philips_activity_family_member_detail);

        ivBack = findViewById(R.id.iv_back);
        tvContent = findViewById(R.id.tv_content);
        tvNumber = findViewById(R.id.tv_number);
        tvName = findViewById(R.id.tv_name);
        ivEditor = findViewById(R.id.iv_editor);
        tvTime = findViewById(R.id.tv_time);
        btnDelete = findViewById(R.id.btn_delete);

        bleLockInfo = MyApplication.getInstance().getBleService().getBleLockInfo();
        ivBack.setOnClickListener(this);
        ivEditor.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        tvContent.setText(getString(R.string.philips_user_detail));
        Intent intent = getIntent();
        dataBean = (BluetoothSharedDeviceBean.DataBean) intent.getSerializableExtra(KeyConstants.COMMON_FAMILY_MEMBER_DATA);
        tvNumber.setText(dataBean.getUname());
        tvName.setText(dataBean.getUnickname());
        long createTime = dataBean.getCreateTime();
        if (createTime == 0) {
            getAuthorizationTime();
            createTime = System.currentTimeMillis() / 1000;
        }
        String time = DateUtils.secondToDate(createTime);
        tvTime.setText(time);
    }

    private void getAuthorizationTime() {
    }

    @Override
    protected FamilyMemberDetailPresenter<IFamilyMemberDeatilView> createPresent() {
        return new FamilyMemberDetailPresenter<>();
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
                            String uid = MyApplication.getInstance().getUid();
                            if (bleLockInfo == null) {
                                return;
                            }
                            mPresenter.deleteUserList(uid, dataBean.getUname(), bleLockInfo.getServerLockInfo().getLockName());
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
                editText.setText(dataBean.getUnickname());
                if (dataBean.getUnickname() != null) {
                    editText.setSelection(dataBean.getUnickname().length());
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
                        if (dataBean.getUnickname().equals(data)) {
                            ToastUtils.showShort(getString(R.string.user_nickname_no_update));
                            return;
                        }
                        String uid = MyApplication.getInstance().getUid();
                        if (bleLockInfo == null) {
                            return;
                        }

                        mPresenter.modifyCommonUserNickname(dataBean.get_id(), data);
                        alertDialog.dismiss();
                    }
                });


                break;
        }
    }

    @Override
    public void deleteCommonUserListSuccess(BaseResult baseResult) {
        ToastUtils.showShort(R.string.delete_common_user_success);
        finish();
    }

    @Override
    public void deleteCommonUserListFail(BaseResult baseResult) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, baseResult.getCode()));
    }

    @Override
    public void deleteCommonUserListError(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }

    @Override
    public void modifyCommonUserNicknameSuccess(BaseResult baseResult) {
        nickname = data;
        tvName.setText(nickname);
        dataBean.setUnickname(nickname);
        ToastUtils.showShort(R.string.modify_user_nickname_success);
    }

    @Override
    public void modifyCommonUserNicknameFail(BaseResult baseResult) {
        ToastUtils.showShort(HttpUtils.httpErrorCode(this, baseResult.getCode()));
    }

    @Override
    public void modifyCommonUserNicknameError(Throwable throwable) {
        ToastUtils.showShort(HttpUtils.httpProtocolErrorCode(this, throwable));
    }
}
